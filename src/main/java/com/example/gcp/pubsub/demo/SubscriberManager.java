package com.example.gcp.pubsub.demo;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.scheduling.TaskScheduler;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.common.util.concurrent.MoreExecutors;

public class SubscriberManager {
    private static final Logger log = LoggerFactory.getLogger(SubscriberManager.class);

    private final PubSubSubscriberTemplate template;
    private final String name;
    private final MessageProcessor processor;
    private final SubscribersProperties.SubscriptionConfig config;

    private final TaskScheduler scheduler;
    private final Clock clock;
    private final BackoffStrategy backoff;

    private final AtomicReference<Subscriber> subscriberRef = new AtomicReference<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final AtomicInteger restartAttempts = new AtomicInteger(0);

    private final AtomicReference<Instant> lastMessageTime = new AtomicReference<>();
    private final AtomicBoolean firstMessageReceived = new AtomicBoolean(false);

    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition unpaused = pauseLock.newCondition();

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

        this.name = name;
        this.template = template;
        this.processor = processor;
        this.config = config;
        this.scheduler = scheduler;
        this.clock = clock;
        this.backoff = backoff;
        this.metrics = metricsFactory.create(name);
    }

    // ---------------- Lifecycle ----------------

    public synchronized void start() {
        if (running.get())
            return;

        running.set(true);
        firstMessageReceived.set(false);
        createAndStartSubscriber();
        scheduleMonitor();
    }

    public synchronized void stop() {
        running.set(false);
        cancelMonitor();
        Subscriber sub = subscriberRef.getAndSet(null);
        if (sub != null)
            sub.stopAsync();
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        pauseLock.lock();
        try {
            paused.set(false);
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    // ---------------- Core ----------------

    private void createAndStartSubscriber() {

        Subscriber subscriber = template.subscribe(config.getSubscriptionId(), this::processMessage);
        subscriber
                .addListener(new Subscriber.Listener() {
                    @Override
                    public void failed(Subscriber.State from, Throwable failure) {
                        scheduleRestart();
                    }
                }, MoreExecutors.directExecutor());

        subscriberRef.set(subscriber);
        lastMessageTime.set(Instant.now(clock));
    }

    private void processMessage(BasicAcknowledgeablePubsubMessage ackableMessage) {

        blockIfPaused();

        lastMessageTime.set(Instant.now(clock));
        metrics.incrementMessage();

        try {
            processor.process(ackableMessage.getPubsubMessage());
            ackableMessage.ack();
        } catch (Exception e) {
            ackableMessage.nack();
        }

        if (firstMessageReceived.compareAndSet(false, true)) {
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

        if (!running.get())
            return;
        if (paused.get())
            return;

        Subscriber sub = subscriberRef.get();
        if (sub == null)
            return;

        if (sub.state() != Subscriber.State.RUNNING) {
            scheduleRestart();
            return;
        }

        if (config.getStallDetectionMode() == StallDetectionMode.NO_DETECTION) {
            return;
        }

        Duration idle = Duration.between(
                lastMessageTime.get(),
                Instant.now(clock));

        if (idle.compareTo(config.getStallThreshold()) > 0) {

            metrics.incrementStall();

            if (config.getStallDetectionMode() == StallDetectionMode.DETECT_AND_WARN) {
                log.warn("Subscriber {} stalled for {}", name, idle);
            } else {
                scheduleRestart();
            }
        }
    }

    private void scheduleRestart() {

        if (!running.get())
            return;

        int attempt = restartAttempts.incrementAndGet();
        Duration delay = backoff.nextDelay(attempt);

        metrics.incrementRestart();

        scheduler.schedule(this::restartNow, Instant.now().plus(delay));
    }

    private synchronized void restartNow() {
        if (!running.get())
            return;
        stop();
        start();
    }

    private void blockIfPaused() {
        pauseLock.lock();
        try {
            while (paused.get()) {
                unpaused.awaitUninterruptibly();
            }
        } finally {
            pauseLock.unlock();
        }
    }

    // ---------------- Health ----------------

    public Health health() {

        if (paused.get()) {
            return Health.outOfService()
                    .withDetail("subscriber", name)
                    .withDetail("state", "paused")
                    .build();
        }

        Subscriber sub = subscriberRef.get();
        if (!running.get() || sub == null || sub.state() != Subscriber.State.RUNNING) {
            return Health.down()
                    .withDetail("subscriber", name)
                    .withDetail("state", "stopped")
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
        Subscriber sub = subscriberRef.get();
        if (!running.get() || sub == null) {
            return "STOPPED";
        }
        return sub.state().name();
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isHealthy() {
        return health().getStatus().equals(org.springframework.boot.actuate.health.Status.UP);
    }
}
