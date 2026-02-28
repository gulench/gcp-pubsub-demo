package com.example.gcp.pubsub.demo;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class SubscriberMetricsFactory {
    private final MeterRegistry meterRegistry;

    public SubscriberMetricsFactory(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public SubscriberMetrics create(String subscriptionName) {
        return new MicrometerSubscriberMetrics(meterRegistry, subscriptionName);
    }

}
