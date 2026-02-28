package com.example.gcp.pubsub.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.example.gcp.pubsub.demo.SubscribersProperties.SubscriptionConfig;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;

class SubscriberManagerConfigurationTest {

    private SubscriberManagerConfiguration configuration;
    private SubscribersProperties properties;
    private PubSubSubscriberTemplate template;
    private TaskScheduler scheduler;
    private Clock clock;
    private SubscriberMetricsFactory metricsFactory;
    private MessageProcessor processor1;
    private MessageProcessor processor2;

    @BeforeEach
    void setUp() {
        configuration = new SubscriberManagerConfiguration();
        properties = new SubscribersProperties();
        template = mock(PubSubSubscriberTemplate.class);
        scheduler = mock(TaskScheduler.class);
        clock = Clock.systemUTC();
        metricsFactory = mock(SubscriberMetricsFactory.class);
        processor1 = mock(MessageProcessor.class);
        processor2 = mock(MessageProcessor.class);
    }

    @Test
    void subscriberManagerRegistry_createsManagersOnlyForEnabledSubscriptions() {
        // Arrange
        SubscriptionConfig config1 = new SubscriptionConfig();
        config1.setEnabled(true);
        config1.setSubscriptionId("sub-id-1");
        config1.setMinBackoff(Duration.ofSeconds(1));
        config1.setMaxBackoff(Duration.ofSeconds(10));

        SubscriptionConfig config2 = new SubscriptionConfig();
        config2.setEnabled(false); // This one is disabled
        config2.setSubscriptionId("sub-id-2");

        SubscriptionConfig config3 = new SubscriptionConfig();
        config3.setEnabled(true);
        config3.setSubscriptionId("sub-id-3");
        config3.setMinBackoff(Duration.ofSeconds(2));
        config3.setMaxBackoff(Duration.ofSeconds(20));

        properties.setSubscriptions(Map.of(
                "sub1", config1,
                "sub2", config2,
                "sub3", config3));

        Map<String, MessageProcessor> processors = Map.of(
                "sub1-processor", processor1,
                "sub3-processor", processor2);

        // Act
        SubscriberManagerRegistry registry = configuration.subscriberManagerRegistry(
                properties, template, scheduler, clock, metricsFactory, processors);

        // Assert
        Map<String, SubscriberManager> managers = registry.getManagers();
        assertThat(managers).hasSize(2);
        assertThat(managers).containsOnlyKeys("sub1", "sub3");
    }

    @Test
    void subscriberManagerRegistry_throwsException_whenProcessorIsMissing() {
        // Arrange
        SubscriptionConfig config1 = new SubscriptionConfig();
        config1.setEnabled(true);
        config1.setSubscriptionId("sub-id-1");
        config1.setMinBackoff(Duration.ofSeconds(1));
        config1.setMaxBackoff(Duration.ofSeconds(10));

        properties.setSubscriptions(Map.of("sub1", config1));

        // Processor for "sub1" is missing from the map
        Map<String, MessageProcessor> processors = Map.of("another-processor", processor1);

        // Act & Assert
        assertThatThrownBy(() -> configuration.subscriberManagerRegistry(
                properties, template, scheduler, clock, metricsFactory, processors))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No MessageProcessor bean found for subscriber 'sub1'. Expected a bean named 'sub1-processor'.");
    }

    @Test
    void clock_returnsSystemClock() {
        Clock createdClock = configuration.clock();
        assertThat(createdClock).isNotNull();
    }

    @Test
    void subscriberSupervisionScheduler_createsScheduler() {
        TaskScheduler createdScheduler = configuration.subscriberSupervisionScheduler();
        assertThat(createdScheduler).isInstanceOf(ThreadPoolTaskScheduler.class);

        // Clean up the scheduler to prevent resource leaks in the test environment.
        ((ThreadPoolTaskScheduler) createdScheduler).shutdown();
    }
}