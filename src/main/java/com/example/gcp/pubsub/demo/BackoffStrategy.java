package com.example.gcp.pubsub.demo;

import java.time.Duration;

public interface BackoffStrategy {
    Duration nextDelay(int attempt);

    void reset();
}
