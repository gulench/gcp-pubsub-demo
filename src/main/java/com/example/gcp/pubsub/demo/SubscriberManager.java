package com.example.gcp.pubsub.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.health.contributor.Health;

public class SubscriberManager {
    private static final Logger log = LoggerFactory.getLogger(SubscriberManager.class);

    private final String name;
    private final SubscribersProperties.SubscriptionConfig config;

    private final BackoffStrategy backoff;
    private final SubscriberComponentFactory factory;

    // Components
    private final ManagedSubscriber managedSubscriber;
    private final MessageReceiverHandler messageHandler;
    private final SubscriberMetrics metrics;

    // State
    private final AtomicBoolean active = new AtomicBoolean(false); // User intention (start/stop)
    private final AtomicBoolean paused = new AtomicBoolean(false); // User intention (pause/resume)
    private final AtomicInteger restartAttempts = new AtomicInteger(0);

    private ScheduledFuture<?> monitorTask;

    public SubscriberManager(String name,
                             SubscribersProperties.SubscriptionConfig config,
                             MessageProcessor processor,
                             SubscriberComponentFactory factory) {
        this.name = name;
        this.config = config;
        this.factory = factory;
        this.backoff = factory.createBackoff(config);

        // The manager and the handler share the same metrics instance
        this.metrics = factory.createMetrics(name);

        this.messageHandler = factory.createHandler(processor, this.metrics, this::onSuccessfulMessage);
        this.managedSubscriber = factory.createSubscriber(
                config.getSubscriptionId(),
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
        monitorTask = factory.getScheduler().scheduleWithFixedDelay(
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

        messageHandler.recordIdleMetrics();

        if (messageHandler.isStalled(config.getStallThreshold())) {
            metrics.incrementStall();

            if (config.getStallDetectionMode() == StallDetectionMode.DETECT_AND_WARN) {
                log.warn("Stall detected for subscriber {} (last message received at {}).", name, messageHandler.getLastMessageTime());
            } else {
                log.warn("Stall detected for subscriber {} (last message received at {}); scheduling restart.", name, messageHandler.getLastMessageTime());
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

        factory.getScheduler().schedule(
                this::restartNow,
                Instant.now(factory.getClock()).plus(delay)
        );
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

        if (!managedSubscriber.isRunning()) {
            if (restartAttempts.get() <= config.getMaxRestartAttempts()) {
                return Health.up().withDetail("state", "recovering").build();
            }
            return Health.down()
                    .withDetail("subscriber", name)
                    .withDetail("state", "failed")
                    .withDetail("restartAttempts", restartAttempts.get())
                    .build();
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
        return health().getStatus().equals(org.springframework.boot.health.contributor.Status.UP);
    }
}
