package com.example.gcp.pubsub.demo;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SubscriberMetricsFactoryTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Test
    void create_shouldReturnMicrometerSubscriberMetrics() {
        SubscriberMetricsFactory factory = new SubscriberMetricsFactory(meterRegistry);
        String subscriptionName = "test-subscription";

        SubscriberMetrics metrics = factory.create(subscriptionName);

        assertThat(metrics).isNotNull();
        assertThat(metrics).isInstanceOf(MicrometerSubscriberMetrics.class);
    }
}