package com.example.gcp.pubsub.demo;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class ExponentialBackoff {

    private final Duration initial;
    private final Duration max;
    private final double multiplier;

    private Duration current;

    public ExponentialBackoff(Duration initial,
                              Duration max,
                              double multiplier) {
        this.initial = initial;
        this.max = max;
        this.multiplier = multiplier;
        this.current = initial;
    }

    public Duration next() {
        Duration next = current;
        long nextMillis = Math.min(
                (long) (current.toMillis() * multiplier),
                max.toMillis());

        current = Duration.ofMillis(nextMillis);

        // Add jitter (±20%)
        long jitter = (long) (next.toMillis() * 0.2 * ThreadLocalRandom.current().nextDouble());
        return next.plusMillis(jitter);
    }

    public void reset() {
        current = initial;
    }
}
