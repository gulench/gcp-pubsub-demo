package com.example.gcp.pubsub.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.pubsub.v1.PubsubMessage;

@Component("catalog-processor")
public class CatalogMessageProcessor implements MessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(CatalogMessageProcessor.class);

    @Override
    public void process(PubsubMessage message) throws Exception {
        log.info("Processed message with payload [{}]", message.getData().toStringUtf8());
    }

}
