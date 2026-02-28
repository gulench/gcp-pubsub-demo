package com.example.gcp.pubsub.demo;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Clock} that can be manually advanced for testing time-dependent logic.
 */
public class ManuallyAdvancedClock extends Clock {

    private final AtomicReference<Instant> now;
    private final ZoneId zone;

    public ManuallyAdvancedClock(Instant initialInstant, ZoneId zone) {
        this.now = new AtomicReference<>(initialInstant);
        this.zone = zone;
    }

    public static ManuallyAdvancedClock at(Instant initialInstant, ZoneId zone) {
        return new ManuallyAdvancedClock(initialInstant, zone);
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new ManuallyAdvancedClock(now.get(), zone);
    }

    @Override
    public Instant instant() {
        return now.get();
    }

    public void advanceBy(Duration duration) {
        now.updateAndGet(current -> current.plus(duration));
    }
}