package com.example.gcp.pubsub.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExponentialBackoffStrategyTest {

    @Mock
    private Random random;

    private ExponentialBackoffStrategy strategy;
    private final Duration min = Duration.ofMillis(100);
    private final Duration max = Duration.ofMillis(1000);

    @BeforeEach
    void setUp() {
        strategy = new ExponentialBackoffStrategy(min, max, random);
    }

    @Test
    void nextDelay_shouldCalculateExponentialBackoff() {
        // Mock jitter to 0 to test base calculation
        when(random.nextLong(anyLong())).thenReturn(0L);

        // Attempt 1: 100 * 2^1 = 200
        assertThat(strategy.nextDelay(1)).isEqualTo(Duration.ofMillis(200));

        // Attempt 2: 100 * 2^2 = 400
        assertThat(strategy.nextDelay(2)).isEqualTo(Duration.ofMillis(400));

        // Attempt 3: 100 * 2^3 = 800
        assertThat(strategy.nextDelay(3)).isEqualTo(Duration.ofMillis(800));
    }

    @Test
    void nextDelay_shouldCapAtMax() {
        when(random.nextLong(anyLong())).thenReturn(0L);

        // Attempt 4: 100 * 2^4 = 1600 > 1000 (max)
        assertThat(strategy.nextDelay(4)).isEqualTo(max);
    }

    @Test
    void nextDelay_shouldAddJitter() {
        // Jitter is added to the calculated delay.
        // For attempt 1 (base 200ms), jitter range is [0, min) -> [0, 100).
        when(random.nextLong(100)).thenReturn(50L);

        assertThat(strategy.nextDelay(1)).isEqualTo(Duration.ofMillis(250));
    }

    @Test
    void reset_shouldDoNothing() {
        // Strategy is stateless regarding reset, just verify it can be called without exception
        strategy.reset();
    }
}