package com.example.gcp.pubsub.demo;

import java.util.Map;

import org.springframework.boot.health.contributor.Status;
import org.springframework.context.SmartLifecycle;

public class SubscriberManagerRegistry implements SmartLifecycle {

    private final Map<String, SubscriberManager> managers;

    public SubscriberManagerRegistry(Map<String, SubscriberManager> managers) {
        this.managers = managers;
    }

    public Map<String, SubscriberManager> getManagers() {
        return managers;
    }

    public SubscriberManager get(String name) {
        return managers.get(name);
    }

    @Override
    public void start() {
        managers.values().forEach(SubscriberManager::start);
    }

    @Override
    public void stop() {
        managers.values().forEach(SubscriberManager::stop);
    }

    @Override
    public boolean isRunning() {
        return managers.values().stream().allMatch(m -> m.health().getStatus() == Status.UP);
    }
}
