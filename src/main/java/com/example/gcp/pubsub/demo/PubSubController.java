package com.example.gcp.pubsub.demo;

import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class PubSubController {
    private static final String TOPIC_NAME = "catalog-topic";

    private final PubSubPublisherTemplate publisherTemplate;

    public PubSubController(PubSubPublisherTemplate publisherTemplate) {
        this.publisherTemplate = publisherTemplate;
    }

    @PostMapping("publish")
    public String postMethodName(@RequestBody String message) {
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(message))
                .build();
        publisherTemplate.publish(TOPIC_NAME, pubsubMessage);
        return message;
    }

}
