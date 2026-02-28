package com.example.gcp.pubsub.demo;

import java.time.Duration;
import java.util.Random;

public class ExponentialBackoffStrategy implements BackoffStrategy {

    private final Duration min;
    private final Duration max;
    private final Random random;

    public ExponentialBackoffStrategy(Duration min, Duration max, Random random) {
        this.min = min;
        this.max = max;
        this.random = random;
    }

    @Override
    public Duration nextDelay(int attempt) {
        long exp = (long) (min.toMillis() * Math.pow(2, attempt));
        long capped = Math.min(exp, max.toMillis());
        long jitter = random.nextLong(min.toMillis());
        return Duration.ofMillis(capped + jitter);
    }

    @Override
    public void reset() {
        // no-op
    }
}
