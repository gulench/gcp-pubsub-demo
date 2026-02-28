package com.example.gcp.pubsub.demo;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.common.util.concurrent.MoreExecutors;

public class ManagedSubscriber {

    private final String subscriptionId;
    private final PubSubSubscriberTemplate template;
    private final Consumer<BasicAcknowledgeablePubsubMessage> messageHandler;
    private final Runnable onFailure;

    private final AtomicReference<Subscriber> subscriberRef = new AtomicReference<>();

    public ManagedSubscriber(String subscriptionId,
            PubSubSubscriberTemplate template,
            Consumer<BasicAcknowledgeablePubsubMessage> messageHandler,
            Runnable onFailure) {
        this.subscriptionId = subscriptionId;
        this.template = template;
        this.messageHandler = messageHandler;
        this.onFailure = onFailure;
    }

    public synchronized void start() {
        if (subscriberRef.get() != null) {
            return;
        }
        Subscriber subscriber = template.subscribe(subscriptionId, messageHandler);
        subscriber.addListener(new Subscriber.Listener() {
            @Override
            public void failed(Subscriber.State from, Throwable failure) {
                onFailure.run();
            }
        }, MoreExecutors.directExecutor());

        subscriberRef.set(subscriber);
    }

    public synchronized void stop() {
        Subscriber sub = subscriberRef.getAndSet(null);
        if (sub != null) {
            sub.stopAsync();
        }
    }

    public boolean isRunning() {
        Subscriber sub = subscriberRef.get();
        return sub != null && sub.state() == Subscriber.State.RUNNING;
    }

    public String getState() {
        Subscriber sub = subscriberRef.get();
        return sub != null ? sub.state().name() : "STOPPED";
    }
}