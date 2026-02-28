package com.example.gcp.pubsub.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

class SubscriberManagerRegistryTest {

    private SubscriberManager manager1;
    private SubscriberManager manager2;
    private SubscriberManagerRegistry registry;

    @BeforeEach
    void setUp() {
        manager1 = mock(SubscriberManager.class);
        manager2 = mock(SubscriberManager.class);
        Map<String, SubscriberManager> managers = Map.of("sub1", manager1, "sub2", manager2);
        registry = new SubscriberManagerRegistry(managers);
    }

    @Test
    void start_startsAllManagers() {
        registry.start();
        verify(manager1).start();
        verify(manager2).start();
    }

    @Test
    void stop_stopsAllManagers() {
        registry.stop();
        verify(manager1).stop();
        verify(manager2).stop();
    }

    @Test
    void isRunning_returnsTrue_whenAllManagersAreUp() {
        when(manager1.health()).thenReturn(Health.up().build());
        when(manager2.health()).thenReturn(Health.up().build());

        assertThat(registry.isRunning()).isTrue();
    }

    @Test
    void isRunning_returnsFalse_whenOneManagerIsDown() {
        when(manager1.health()).thenReturn(Health.up().build());
        when(manager2.health()).thenReturn(Health.down().build());

        assertThat(registry.isRunning()).isFalse();
    }

    @Test
    void get_returnsCorrectManager() {
        assertThat(registry.get("sub1")).isEqualTo(manager1);
        assertThat(registry.get("sub2")).isEqualTo(manager2);
        assertThat(registry.get("non-existent")).isNull();
    }
}