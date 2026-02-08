package com.example.gcp.pubsub.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class CatalogSubscriber {
    private static final String SUBSCRIPTION_NAME = "catalog-subscription-myapp";

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogSubscriber.class);

    private final PubSubSubscriberTemplate subscriberTemplate;

    private Subscriber subscriber;

    public CatalogSubscriber(PubSubSubscriberTemplate subscriberTemplate) {
        this.subscriberTemplate = subscriberTemplate;
    }

    @PostConstruct
    public void start() {
        this.subscriber = subscriberTemplate.subscribe(SUBSCRIPTION_NAME, this::handleMessage);
        LOGGER.info("Started subscriber for {}", SUBSCRIPTION_NAME);
    }

    @PreDestroy
    public void stop() {
        if (this.subscriber != null) {
            LOGGER.info("Stopping subscriber for {}", SUBSCRIPTION_NAME);
            this.subscriber.stopAsync().awaitTerminated();
            LOGGER.info("Stopped subscriber for {}", SUBSCRIPTION_NAME);
        }
    }
    private void handleMessage(BasicAcknowledgeablePubsubMessage message) {
        String payload = message.getPubsubMessage().getData().toStringUtf8();
        LOGGER.info("Handled message with payload: {}", payload);
    
        message.ack();
    }
}
