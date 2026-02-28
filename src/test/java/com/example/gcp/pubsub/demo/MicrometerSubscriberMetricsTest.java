package com.example.gcp.pubsub.demo;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class MicrometerSubscriberMetricsTest {

    private MeterRegistry registry;
    private MicrometerSubscriberMetrics metrics;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        metrics = new MicrometerSubscriberMetrics(registry, "test-sub");
    }

    @Test
    void incrementRestart_shouldIncrementCounter() {
        metrics.incrementRestart();

        assertThat(registry.get("subscriber_restarts_total")
                .tag("subscription", "test-sub")
                .counter().count()).isEqualTo(1.0);
    }

    @Test
    void incrementStall_shouldIncrementCounter() {
        metrics.incrementStall();

        assertThat(registry.get("subscriber_stalls_total")
                .tag("subscription", "test-sub")
                .counter().count()).isEqualTo(1.0);
    }

    @Test
    void incrementMessage_shouldIncrementCounter() {
        metrics.incrementMessage();

        assertThat(registry.get("subscriber_messages_total")
                .tag("subscription", "test-sub")
                .counter().count()).isEqualTo(1.0);
    }

    @Test
    void recordIdle_shouldUpdateGauge() {
        metrics.recordIdle(Duration.ofSeconds(60));

        Gauge gauge = registry.get("subscriber_idle_seconds")
                .tag("subscription", "test-sub")
                .gauge();

        assertThat(gauge.value()).isEqualTo(60.0);
    }

    @Test
    void recordState_shouldMapStatesToValues() {
        // RUNNING -> 1
        metrics.recordState("RUNNING");
        assertThat(getStateGaugeValue()).isEqualTo(1.0);

        // PAUSED -> 2
        metrics.recordState("PAUSED");
        assertThat(getStateGaugeValue()).isEqualTo(2.0);

        // FAILED -> 3
        metrics.recordState("FAILED");
        assertThat(getStateGaugeValue()).isEqualTo(3.0);

        // Unknown -> 0
        metrics.recordState("UNKNOWN");
        assertThat(getStateGaugeValue()).isEqualTo(0.0);
    }

    private double getStateGaugeValue() {
        return registry.get("subscriber_state")
                .tag("subscription", "test-sub")
                .gauge()
                .value();
    }
}