package com.example.gcp.pubsub.demo;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.example.gcp.pubsub.demo.SubscribersProperties.SubscriptionConfig;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;

@Configuration
@EnableConfigurationProperties(SubscribersProperties.class)
public class SubscriberManagerConfiguration {

    @Bean
    public SubscriberManagerRegistry subscriberManagerRegistry(
            SubscribersProperties properties,
            PubSubSubscriberTemplate template,
            @Qualifier("subscriberSupervisionScheduler") TaskScheduler scheduler,
            Clock clock,
            SubscriberMetricsFactory metricsFactory,
            Map<String, MessageProcessor> processors) {

        Map<String, SubscriberManager> managers = new HashMap<>();

        for (Map.Entry<String, SubscriptionConfig> entry : properties.getSubscriptions().entrySet()) {

            String name = entry.getKey();
            SubscriptionConfig config = entry.getValue();
            SubscriptionConfig resolvedConfig = resolveSubscriberConfig(config, properties);

            MessageProcessor processor = resolveProcessor(name, processors);

            SubscriberManager manager = new SubscriberManager(
                    name,
                    resolvedConfig,
                    processor,
                    template,
                    scheduler,
                    clock,
                    new ExponentialBackoffStrategy(resolvedConfig.getMinBackoff(), resolvedConfig.getMaxBackoff(),
                            new Random()),
                    metricsFactory);

            managers.put(name, manager);
        }

        return new SubscriberManagerRegistry(managers);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public TaskScheduler subscriberSupervisionScheduler() {
        var scheduler = new ThreadPoolTaskScheduler();
        // TODO: Use configuration or determine based on the number of subscribers
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("subscriber-reconnect-");
        // TODO: Use logger
        scheduler.setErrorHandler(t -> System.err.println("Task failed: " + t.getMessage()));
        scheduler.initialize();

        return scheduler;
    }

    private SubscriptionConfig resolveSubscriberConfig(SubscriptionConfig config,
            SubscribersProperties properties) {
        return config;
    }

    private MessageProcessor resolveProcessor(String name, Map<String, MessageProcessor> processors) {
        return processors.get(name + "-processor");
    }
}
