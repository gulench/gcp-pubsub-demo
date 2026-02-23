package com.example.gcp.pubsub.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;

@Component
public class CatalogSubscriber extends AbstractPubSubSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogSubscriber.class);

    public CatalogSubscriber(PubSubSubscriberTemplate subscriberTemplate, @Value("${myapp.catalog.subscription-name}") String catalogSubscriptionName) {
        super(subscriberTemplate, catalogSubscriptionName);
    }

    @Override
    protected void processMessage(String payload) {
        LOGGER.info("Processing message: [{}]", payload);
    }

}