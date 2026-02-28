package com.example.gcp.pubsub.demo;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "subscribers")
public class SubscriberManagementEndpoint {
    private final SubscriberManagerRegistry registry;

    public SubscriberManagementEndpoint(SubscriberManagerRegistry registry) {
        this.registry = registry;
    }

    @ReadOperation
    public Map<String, Object> list() {
        return registry.getManagers().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> (Object) Map.of(
                                "state", entry.getValue().getState(),
                                "running", entry.getValue().isRunning(),
                                "healthy", entry.getValue().isHealthy())));
    }

    @WriteOperation
    public void execute(@Selector String operation, @Selector String name) {
        switch (operation) {
            case "stop":
                registry.get(name).stop();
                break;
            case "start":
                registry.get(name).start();
                break;
            case "pause":
                registry.get(name).pause();
                break;
            case "resume":
                registry.get(name).resume();
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

}
