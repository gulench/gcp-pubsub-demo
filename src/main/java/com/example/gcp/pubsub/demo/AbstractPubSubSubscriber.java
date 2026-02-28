package com.example.gcp.pubsub.demo;

import java.time.Clock;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public abstract class AbstractPubSubSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPubSubSubscriber.class);

    private static final long RECONNECT_DELAY_SECONDS = 30;
    private static final long HEALTH_CHECK_INTERVAL_SECONDS = 60;
    private static final long MIN_WARN_INTERVAL_SECONDS = 60;
    private static final long MAX_IDLE_SECONDS = 120;

    private final PubSubSubscriberTemplate subscriberTemplate;
    private final String subscriptionName;
    private final ScheduledExecutorService scheduler;
    private final Clock clock;

    private final AtomicBoolean stopping = new AtomicBoolean(false);

    private Subscriber subscriber;
    private long lastMessageReceivedAt;
    private long lastWarnedAt = Long.MIN_VALUE;

    // ----------------------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------------------

    /**
     * Production constructor. Uses a dedicated daemon-threaded scheduler and
     * the system UTC clock; both are created internally so callers don't need
     * to supply them.
     */
    protected AbstractPubSubSubscriber(PubSubSubscriberTemplate subscriberTemplate,
                                       String subscriptionName) {
        this(subscriberTemplate, subscriptionName,
                defaultScheduler(subscriptionName), Clock.systemUTC());
    }

    /**
     * Testing constructor. Accepts an externally supplied scheduler and clock so
     * that tests can use a {@code ManuallyAdvancedClock} or a
     * {@code DirectExecutorService} to control timing deterministically.
     */
    protected AbstractPubSubSubscriber(PubSubSubscriberTemplate subscriberTemplate,
                                       String subscriptionName,
                                       ScheduledExecutorService scheduler,
                                       Clock clock) {
        if (subscriptionName == null || subscriptionName.isBlank()) {
            throw new IllegalArgumentException("subscriptionName must not be null or blank");
        }
        this.subscriberTemplate = subscriberTemplate;
        this.subscriptionName = subscriptionName;
        this.scheduler = scheduler;
        this.clock = clock;
        this.lastMessageReceivedAt = clock.millis();
    }

    private static ScheduledExecutorService defaultScheduler(String subscriptionName) {
        return java.util.concurrent.Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "pubsub-reconnect-" + subscriptionName);
            t.setDaemon(true);
            return t;
        });
    }

    // ----------------------------------------------------------------------------------
    // Contract for subclasses
    // ----------------------------------------------------------------------------------

    /**
     * Processes a successfully received message. Throw an unchecked exception if
     * processing fails; the base class calls {@code nack()} automatically so the
     * message is redelivered by Pub/Sub.
     */
    protected abstract void processMessage(String payload);

    // ----------------------------------------------------------------------------------
    // Lifecycle — called by Spring but also directly callable from tests
    // ----------------------------------------------------------------------------------

    @PostConstruct
    public void start() {
        connect();
        scheduler.scheduleAtFixedRate(
                this::healthCheck,
                HEALTH_CHECK_INTERVAL_SECONDS,
                HEALTH_CHECK_INTERVAL_SECONDS,
                TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stop() {
        LOGGER.info("Stopping subscriber for {}", subscriptionName);
        stopping.set(true);

        synchronized (this) {
            if (subscriber != null) {
                subscriber.stopAsync().awaitTerminated();
                subscriber = null;
            }
        }

        scheduler.shutdown();
        LOGGER.info("Stopped subscriber for {}", subscriptionName);
    }

    /**
     * Exposes the health check for direct invocation from tests, bypassing the
     * scheduler so there is no need for {@code Thread.sleep()} or time manipulation
     * to trigger a check.
     */
    public void runHealthCheck() {
        healthCheck();
    }

    // ----------------------------------------------------------------------------------
    // Internals
    // ----------------------------------------------------------------------------------

    private synchronized void connect() {
        if (stopping.get()) {
            return;
        }

        if (subscriber != null) {
            try {
                subscriber.stopAsync();
            } catch (Exception e) {
                LOGGER.warn("Error stopping previous subscriber for {} before reconnect",
                        subscriptionName, e);
            }
            subscriber = null;
        }

        try {
            subscriber = subscriberTemplate.subscribe(subscriptionName, this::handleMessage);

            subscriber.addListener(new Subscriber.Listener() {
                @Override
                public void failed(Subscriber.State from, Throwable failure) {
                    LOGGER.error(
                            "Subscriber for {} failed (was in state {}), scheduling reconnect in {}s",
                            subscriptionName, from, RECONNECT_DELAY_SECONDS, failure);
                    scheduleReconnect();
                }
            }, Runnable::run);

            LOGGER.info("Started subscriber for {}", subscriptionName);

        } catch (Exception e) {
            LOGGER.error("Failed to create subscriber for {}, scheduling reconnect in {}s",
                    subscriptionName, RECONNECT_DELAY_SECONDS, e);
            subscriber = null;
            scheduleReconnect();
        }
    }

    private synchronized void healthCheck() {
        if (stopping.get()) {
            return;
        }

        boolean isUnhealthy = subscriber == null
                || subscriber.state() == Subscriber.State.FAILED
                || subscriber.state() == Subscriber.State.TERMINATED;

        if (isUnhealthy) {
            maybeWarn();
            connect();
            return;
        }

        long idleSeconds = TimeUnit.MILLISECONDS.toSeconds(
                clock.millis() - lastMessageReceivedAt);

        if (idleSeconds > MAX_IDLE_SECONDS) {
            LOGGER.warn(
                    "Subscriber for {} has been idle for {}s with no messages received, "
                    + "stream may be silently broken — reconnecting",
                    subscriptionName, idleSeconds);
            lastMessageReceivedAt = clock.millis();
            connect();
            return;
        }

        LOGGER.debug("Health check passed for {}, state: {}, idle for {}s",
                subscriptionName, subscriber.state(), idleSeconds);
    }

    private void maybeWarn() {
        long now = clock.millis();
        long elapsedMs = now - lastWarnedAt;

        if (elapsedMs >= TimeUnit.SECONDS.toMillis(MIN_WARN_INTERVAL_SECONDS)) {
            LOGGER.warn(
                    "Health check detected broken subscriber for {} (state: {}), reconnecting",
                    subscriptionName, subscriber == null ? "null" : subscriber.state());
            lastWarnedAt = now;
        } else {
            LOGGER.debug(
                    "Health check detected broken subscriber for {} but suppressing warn log "
                    + "(last warned {}s ago, min interval is {}s)",
                    subscriptionName,
                    TimeUnit.MILLISECONDS.toSeconds(elapsedMs),
                    MIN_WARN_INTERVAL_SECONDS);
        }
    }

    private void scheduleReconnect() {
        if (stopping.get()) {
            LOGGER.debug("Shutdown in progress; skipping reconnect for {}", subscriptionName);
            return;
        }

        try {
            scheduler.schedule(this::connect, RECONNECT_DELAY_SECONDS, TimeUnit.SECONDS);
            LOGGER.info("Reconnect for {} scheduled in {}s", subscriptionName, RECONNECT_DELAY_SECONDS);
        } catch (Exception e) {
            LOGGER.warn("Could not schedule reconnect for {} (scheduler may be shut down)",
                    subscriptionName, e);
        }
    }

    private void handleMessage(BasicAcknowledgeablePubsubMessage message) {
        lastMessageReceivedAt = clock.millis();

        String payload = message.getPubsubMessage().getData().toStringUtf8();
        LOGGER.info("Handling message for {}, payload: {}", subscriptionName, payload);

        try {
            processMessage(payload);
            message.ack();
        } catch (Exception e) {
            LOGGER.error("Failed to process message for {}, sending nack", subscriptionName, e);
            message.nack();
        }
    }
}
