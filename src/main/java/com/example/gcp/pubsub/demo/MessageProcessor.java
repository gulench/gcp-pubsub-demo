package com.example.gcp.pubsub.demo;

import com.google.pubsub.v1.PubsubMessage;

public interface MessageProcessor {
    void process(PubsubMessage message) throws Exception;
}
