package com.example.gcp.pubsub.demo;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.scheduling.TaskScheduler;

import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

public class SubscriberManager {
    private static final Logger log = LoggerFactory.getLogger(SubscriberManager.class);

    private final String name;
    private final SubscribersProperties.SubscriptionConfig config;

    private final TaskScheduler scheduler;
    private final Clock clock;
    private final BackoffStrategy backoff;

    // Components
    private final ManagedSubscriber managedSubscriber;
    private final MessageReceiverHandler messageHandler;

    // State
    private final AtomicBoolean active = new AtomicBoolean(false); // User intention (start/stop)
    private final AtomicBoolean paused = new AtomicBoolean(false); // User intention (pause/resume)
    private final AtomicInteger restartAttempts = new AtomicInteger(0);

    private ScheduledFuture<?> monitorTask;

    // Metrics
    private final SubscriberMetrics metrics;

    public SubscriberManager(
            String name,
            SubscribersProperties.SubscriptionConfig config,
            MessageProcessor processor,
            PubSubSubscriberTemplate template,
            TaskScheduler scheduler,
            Clock clock,
            BackoffStrategy backoff,
            SubscriberMetricsFactory metricsFactory) {
        this(name, config, processor, template, scheduler, clock, backoff, metricsFactory, new DefaultComponentsFactory());
    }

    public SubscriberManager(String name, SubscribersProperties.SubscriptionConfig config, MessageProcessor processor, PubSubSubscriberTemplate template, TaskScheduler scheduler, Clock clock, BackoffStrategy backoff, SubscriberMetricsFactory metricsFactory, ComponentsFactory factory) {
        this.name = name;
        this.config = config;
        this.scheduler = scheduler;
        this.clock = clock;
        this.backoff = backoff;
        this.metrics = metricsFactory.create(name);

        this.messageHandler = factory.createHandler(processor, metrics, clock, this::onSuccessfulMessage);
        this.managedSubscriber = factory.createSubscriber(
                config.getSubscriptionId(),
                template,
                messageHandler,
                this::onSubscriberFailure);
    }

    // ---------------- Lifecycle ----------------

    public synchronized void start() {
        if (active.get())
            return;

        active.set(true);
        restartAttempts.set(0);
        managedSubscriber.start();
        scheduleMonitor();
    }

    public synchronized void stop() {
        active.set(false);
        cancelMonitor();
        managedSubscriber.stop();
    }

    public synchronized void pause() {
        paused.set(true);
        if (active.get()) {
            managedSubscriber.stop();
        }
    }

    public synchronized void resume() {
        paused.set(false);
        if (active.get()) {
            managedSubscriber.start();
        }
    }

    private void onSuccessfulMessage() {
        if (restartAttempts.get() > 0) {
            restartAttempts.set(0);
            backoff.reset();
        }
    }

    // ---------------- Monitor ----------------

    private void scheduleMonitor() {
        monitorTask = scheduler.scheduleWithFixedDelay(
                this::monitor,
                config.getMonitorInterval());
    }

    private void cancelMonitor() {
        if (monitorTask != null) {
            monitorTask.cancel(true);
        }
    }

    private void monitor() {

        if (!active.get())
            return;
        if (paused.get())
            return;

        // If not running (and not paused), we might be in a failed state or backoff
        if (!managedSubscriber.isRunning()) {
            // We rely on the failure listener to schedule restarts,
            // but we could add a check here if needed.
        }

        if (config.getStallDetectionMode() == StallDetectionMode.NO_DETECTION) {
            return;
        }

        // Stall detection
        Duration idle = Duration.between(
                messageHandler.getLastMessageTime(),
                Instant.now(clock));

        metrics.recordIdle(idle);

        if (idle.compareTo(config.getStallThreshold()) > 0) {

            metrics.incrementStall();

            if (config.getStallDetectionMode() == StallDetectionMode.DETECT_AND_WARN) {
                log.warn("Subscriber {} stalled for {}", name, idle);
            } else {
                scheduleRestart();
            }
        }
    }

    private void onSubscriberFailure() {
        scheduleRestart();
    }

    private void scheduleRestart() {

        if (!active.get() || paused.get())
            return;

        int attempt = restartAttempts.incrementAndGet();
        Duration delay = backoff.nextDelay(attempt);

        metrics.incrementRestart();

        scheduler.schedule(this::restartNow, Instant.now().plus(delay));
    }

    private synchronized void restartNow() {
        if (!active.get() || paused.get())
            return;
        managedSubscriber.stop();
        managedSubscriber.start();
    }

    // ---------------- Health ----------------

    public Health health() {

        if (paused.get()) {
            return Health.outOfService()
                    .withDetail("subscriber", name)
                    .withDetail("state", "paused")
                    .build();
        }

        if (!active.get()) {
            return Health.down()
                    .withDetail("subscriber", name)
                    .withDetail("state", "stopped")
                    .build();
        }

        if (!managedSubscriber.isRunning() && restartAttempts.get() <= config.getMaxRestartAttempts()) {
            return Health.up().withDetail("state", "recovering").build();
        }

        return Health.up()
                .withDetail("subscriber", name)
                .withDetail("state", "running")
                .build();
    }

    // ---------------- State Accessors ----------------

    public String getState() {
        if (paused.get()) {
            return "PAUSED";
        }
        if (!active.get()) {
            return "STOPPED";
        }
        return managedSubscriber.getState();
    }

    public boolean isRunning() {
        return active.get();
    }

    public boolean isHealthy() {
        return health().getStatus().equals(org.springframework.boot.actuate.health.Status.UP);
    }

    // Internal interface to allow mocking components in tests
    public interface ComponentsFactory {
        MessageReceiverHandler createHandler(MessageProcessor p, SubscriberMetrics m, Clock c, Runnable onSuccess);
        ManagedSubscriber createSubscriber(String subId, PubSubSubscriberTemplate t, Consumer<BasicAcknowledgeablePubsubMessage> h, Runnable onFailure);
    }

    public static class DefaultComponentsFactory implements ComponentsFactory {
        @Override
        public MessageReceiverHandler createHandler(MessageProcessor p, SubscriberMetrics m, Clock c, Runnable onSuccess) {
            return new MessageReceiverHandler(p, m, c, onSuccess);
        }

        @Override
        public ManagedSubscriber createSubscriber(String subId, PubSubSubscriberTemplate t, Consumer<BasicAcknowledgeablePubsubMessage> h, Runnable onFailure) {
            return new ManagedSubscriber(subId, t, h, onFailure);
        }
    }
}
