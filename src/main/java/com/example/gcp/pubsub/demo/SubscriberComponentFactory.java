package com.example.gcp.pubsub.demo;

import java.time.Clock;
import java.util.Random;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

/**
 * Encapsulates infrastructure dependencies, allowing the SubscriberManager
 * to remain unaware of low-level details like Clocks and Templates.
 */
@Component
public class SubscriberComponentFactory {

    private final PubSubSubscriberTemplate template;
    private final TaskScheduler scheduler;
    private final Clock clock;
    private final SubscriberMetricsFactory metricsFactory;

    public SubscriberComponentFactory(
            PubSubSubscriberTemplate template,
            @Qualifier("subscriberSupervisionScheduler") TaskScheduler scheduler,
            Clock clock,
            SubscriberMetricsFactory metricsFactory) {
        this.template = template;
        this.scheduler = scheduler;
        this.clock = clock;
        this.metricsFactory = metricsFactory;
    }

    public ManagedSubscriber createSubscriber(String subId, Consumer<BasicAcknowledgeablePubsubMessage> handler, Runnable onFailure) {
        return new ManagedSubscriber(subId, template, handler, onFailure);
    }

    public SubscriberMetrics createMetrics(String name) {
        return metricsFactory.create(name);
    }

    public MessageReceiverHandler createHandler(MessageProcessor p, SubscriberMetrics metrics, Runnable onSuccess) {
        return new MessageReceiverHandler(p, metrics, clock, onSuccess);
    }

    public BackoffStrategy createBackoff(SubscribersProperties.SubscriptionConfig config) {
        return new ExponentialBackoffStrategy(config.getMinBackoff(), config.getMaxBackoff(), new Random());
    }

    public TaskScheduler getScheduler() {
        return scheduler;
    }

    public Clock getClock() {
        return clock;
    }
}