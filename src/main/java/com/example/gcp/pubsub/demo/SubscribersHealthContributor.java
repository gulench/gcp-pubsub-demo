package com.example.gcp.pubsub.demo;

import java.util.Iterator;
import java.util.Map;

import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.NamedContributor;
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
    public HealthContributor getContributor(String name) {
        SubscriberManager manager = managers.get(name);
        return (HealthIndicator) manager::health;
    }

    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return managers.entrySet().stream()
                .map(e -> NamedContributor.of(
                        e.getKey(),
                        asContributor(e.getValue())))
                .iterator();
    }

    private HealthContributor asContributor(SubscriberManager manager) {
        return (HealthIndicator) manager::health;
    }
}
