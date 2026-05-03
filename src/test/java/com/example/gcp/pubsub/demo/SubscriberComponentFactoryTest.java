package com.example.gcp.pubsub.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

@ExtendWith(MockitoExtension.class)
class SubscriberComponentFactoryTest {

    @Mock
    private PubSubSubscriberTemplate template;

    @Mock
    private TaskScheduler scheduler;

    @Mock
    private Clock clock;

    @Mock
    private SubscriberMetricsFactory metricsFactory;

    private SubscriberComponentFactory factory;

    @BeforeEach
    void setUp() {
        factory = new SubscriberComponentFactory(template, scheduler, clock, metricsFactory);
    }

    @Test
    void createSubscriber_shouldReturnManagedSubscriber() {
        Consumer<BasicAcknowledgeablePubsubMessage> handler = msg -> {};
        Runnable onFailure = () -> {};

        ManagedSubscriber subscriber = factory.createSubscriber("test-sub", handler, onFailure);

        assertThat(subscriber).isNotNull();
        assertThat(subscriber).isInstanceOf(ManagedSubscriber.class);
    }

    @Test
    void createMetrics_shouldDelegateToFactory() {
        SubscriberMetrics mockMetrics = mock(SubscriberMetrics.class);
        when(metricsFactory.create("test-metrics")).thenReturn(mockMetrics);

        SubscriberMetrics metrics = factory.createMetrics("test-metrics");

        assertThat(metrics).isEqualTo(mockMetrics);
        verify(metricsFactory).create("test-metrics");
    }

    @Test
    void createHandler_shouldReturnMessageReceiverHandler() {
        MessageProcessor processor = mock(MessageProcessor.class);
        SubscriberMetrics metrics = mock(SubscriberMetrics.class);
        Runnable onSuccess = () -> {};

        MessageReceiverHandler handler = factory.createHandler(processor, metrics, onSuccess);

        assertThat(handler).isNotNull();
        assertThat(handler).isInstanceOf(MessageReceiverHandler.class);
    }

    @Test
    void createBackoff_shouldReturnExponentialBackoffStrategy() {
        SubscribersProperties.SubscriptionConfig config = new SubscribersProperties.SubscriptionConfig();
        config.setMinBackoff(Duration.ofSeconds(1));
        config.setMaxBackoff(Duration.ofSeconds(10));

        BackoffStrategy backoff = factory.createBackoff(config);

        assertThat(backoff).isNotNull();
        assertThat(backoff).isInstanceOf(ExponentialBackoffStrategy.class);
    }

    @Test
    void getScheduler_shouldReturnInjectedScheduler() {
        assertThat(factory.getScheduler()).isEqualTo(scheduler);
    }

    @Test
    void getClock_shouldReturnInjectedClock() {
        assertThat(factory.getClock()).isEqualTo(clock);
    }
}