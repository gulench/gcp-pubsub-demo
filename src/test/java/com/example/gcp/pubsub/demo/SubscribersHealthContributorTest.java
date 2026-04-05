package com.example.gcp.pubsub.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthContributor;
import org.springframework.boot.health.contributor.HealthIndicator;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscribersHealthContributorTest {

    @Mock
    private SubscriberManagerRegistry registry;

    @Mock
    private SubscriberManager manager;

    private SubscribersHealthContributor contributor;

    @BeforeEach
    void setUp() {
        when(registry.getManagers()).thenReturn(Map.of("test-sub", manager));
        contributor = new SubscribersHealthContributor(registry);
    }

    @Test
    void getContributor_shouldReturnHealthIndicator() {
        when(manager.health()).thenReturn(Health.up().build());

        HealthContributor healthContributor = contributor.getContributor("test-sub");

        assertThat(healthContributor).isInstanceOf(HealthIndicator.class);
        Health health = ((HealthIndicator) healthContributor).health(false);
        assertThat(health.getStatus()).isEqualTo(org.springframework.boot.health.contributor.Status.UP);
    }

    @Test
    void stream_shouldReturnStreamOfEntries() {
        when(manager.health()).thenReturn(Health.up().build());

        var entries = contributor.stream().toList();

        assertThat(entries).hasSize(1);

        var entry = entries.get(0);
        assertThat(entry.name()).isEqualTo("test-sub");
        assertThat(entry.contributor()).isInstanceOf(HealthIndicator.class);

        Health health = ((HealthIndicator) entry.contributor()).health(false);
        assertThat(health.getStatus()).isEqualTo(org.springframework.boot.health.contributor.Status.UP);
    }

}