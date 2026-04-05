package com.example.gcp.pubsub.demo;

import java.util.Map;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.CompositeHealthContributor;
import org.springframework.boot.health.contributor.HealthContributor;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("subscribersHealthContributor")
public class SubscribersHealthContributor
        implements CompositeHealthContributor {

    private final Map<String, SubscriberManager> managers;

    public SubscribersHealthContributor(
            SubscriberManagerRegistry registry) {
        this.managers = registry.getManagers();
    }

    @Override
    public @Nullable HealthContributor getContributor(String name) {
        SubscriberManager manager = managers.get(name);
        return (manager != null) ? asContributor(manager) : null;
    }

    @Override
    public Stream<Entry> stream() {
        return managers.entrySet().stream()
                .map(e -> new Entry(
                        e.getKey(),
                        asContributor(e.getValue())));
    }

    private HealthContributor asContributor(SubscriberManager manager) {
        return (HealthIndicator) manager::health;
    }
}
