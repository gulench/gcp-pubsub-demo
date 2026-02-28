package com.example.gcp.pubsub.demo;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerSubscriberMetrics implements SubscriberMetrics {

    private final Counter restartCounter;
    private final Counter stallCounter;
    private final Counter messageCounter;
    private final AtomicReference<Double> idleGauge = new AtomicReference<>(0.0);
    private final AtomicReference<Double> stateGauge = new AtomicReference<>(0.0);

    public MicrometerSubscriberMetrics(
            MeterRegistry registry,
            String subscriptionName) {

        this.restartCounter = registry.counter(
                "subscriber_restarts_total",
                "subscription", subscriptionName);

        this.stallCounter = registry.counter(
                "subscriber_stalls_total",
                "subscription", subscriptionName);

        this.messageCounter = registry.counter(
                "subscriber_messages_total",
                "subscription", subscriptionName);

        Gauge.builder("subscriber_idle_seconds", idleGauge, AtomicReference::get)
                .tag("subscription", subscriptionName)
                .register(registry);

        Gauge.builder("subscriber_state", stateGauge, AtomicReference::get)
                .tag("subscription", subscriptionName)
                .register(registry);
    }

    @Override
    public void incrementRestart() {
        restartCounter.increment();
    }

    @Override
    public void incrementStall() {
        stallCounter.increment();
    }

    @Override
    public void incrementMessage() {
        messageCounter.increment();
    }

    @Override
    public void recordIdle(Duration idle) {
        idleGauge.set((double) idle.toSeconds());
    }

    @Override
    public void recordState(String state) {
        stateGauge.set(mapState(state));
    }

    private double mapState(String state) {
        return switch (state) {
            case "RUNNING" -> 1;
            case "PAUSED" -> 2;
            case "FAILED" -> 3;
            default -> 0;
        };
    }
}
