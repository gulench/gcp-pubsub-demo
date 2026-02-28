package com.example.gcp.pubsub.demo;

import java.time.Duration;

public interface SubscriberMetrics {

    void incrementRestart();

    void incrementStall();

    void incrementMessage();

    void recordIdle(Duration idle);

    void recordState(String state);
}
