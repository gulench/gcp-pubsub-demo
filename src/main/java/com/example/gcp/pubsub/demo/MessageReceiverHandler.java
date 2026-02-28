package com.example.gcp.pubsub.demo;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

public class MessageReceiverHandler implements Consumer<BasicAcknowledgeablePubsubMessage> {

    private static final Logger log = LoggerFactory.getLogger(MessageReceiverHandler.class);

    private final MessageProcessor processor;
    private final SubscriberMetrics metrics;
    private final Clock clock;
    private final Runnable onSuccess;
    private final AtomicReference<Instant> lastMessageTime;

    public MessageReceiverHandler(MessageProcessor processor, SubscriberMetrics metrics, Clock clock,
            Runnable onSuccess) {
        this.processor = processor;
        this.metrics = metrics;
        this.clock = clock;
        this.onSuccess = onSuccess;
        this.lastMessageTime = new AtomicReference<>(Instant.now(clock));
    }

    @Override
    public void accept(BasicAcknowledgeablePubsubMessage message) {
        lastMessageTime.set(Instant.now(clock));
        metrics.incrementMessage();
        try {
            processor.process(message.getPubsubMessage());
            message.ack();
            onSuccess.run();
        } catch (Exception e) {
            message.nack();
        }
    }

    public Instant getLastMessageTime() {
        return lastMessageTime.get();
    }
}