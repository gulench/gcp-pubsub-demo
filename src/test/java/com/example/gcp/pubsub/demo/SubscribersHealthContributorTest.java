package com.example.gcp.pubsub.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.NamedContributor;

import java.util.Iterator;
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
        Health health = ((HealthIndicator) healthContributor).getHealth(false);
        assertThat(health.getStatus()).isEqualTo(org.springframework.boot.actuate.health.Status.UP);
    }

    @Test
    void iterator_shouldReturnNamedContributors() {
        Iterator<NamedContributor<HealthContributor>> iterator = contributor.iterator();

        assertThat(iterator.hasNext()).isTrue();
        NamedContributor<HealthContributor> namedContributor = iterator.next();
        assertThat(namedContributor.getName()).isEqualTo("test-sub");
        assertThat(namedContributor.getContributor()).isInstanceOf(HealthIndicator.class);
    }
}