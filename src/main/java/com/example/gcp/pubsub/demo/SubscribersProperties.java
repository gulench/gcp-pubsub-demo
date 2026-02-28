package com.example.gcp.pubsub.demo;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.subscribers")
public class SubscribersProperties {

    private Duration monitorInterval = Duration.ofSeconds(30);
    private Map<String, SubscriptionConfig> subscriptions = new HashMap<>();

    public Duration getMonitorInterval() {
        return monitorInterval;
    }

    public void setMonitorInterval(Duration monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    public Map<String, SubscriptionConfig> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<String, SubscriptionConfig> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public static class SubscriptionConfig {
        private boolean enabled = true;

        private String projectId;
        private String subscriptionId;

        private Duration monitorInterval;
        private Duration stallThreshold = Duration.ofMinutes(5);
        private StallDetectionMode stallDetectionMode = StallDetectionMode.NO_DETECTION;

        private Duration minBackoff = Duration.ofSeconds(5);
        private Duration maxBackoff = Duration.ofMinutes(2);

        private int maxRestartAttempts = 5;

        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        public String getProjectId() {
            return projectId;
        }
        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
        public String getSubscriptionId() {
            return subscriptionId;
        }
        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }
        public Duration getMonitorInterval() {
            return monitorInterval;
        }
        public void setMonitorInterval(Duration monitorInterval) {
            this.monitorInterval = monitorInterval;
        }
        public Duration getStallThreshold() {
            return stallThreshold;
        }
        public void setStallThreshold(Duration stallThreshold) {
            this.stallThreshold = stallThreshold;
        }
        public StallDetectionMode getStallDetectionMode() {
            return stallDetectionMode;
        }
        public void setStallDetectionMode(StallDetectionMode stallDetectionMode) {
            this.stallDetectionMode = stallDetectionMode;
        }
        public Duration getMinBackoff() {
            return minBackoff;
        }
        public void setMinBackoff(Duration minBackoff) {
            this.minBackoff = minBackoff;
        }
        public Duration getMaxBackoff() {
            return maxBackoff;
        }
        public void setMaxBackoff(Duration maxBackoff) {
            this.maxBackoff = maxBackoff;
        }

        public int getMaxRestartAttempts() {
            return maxRestartAttempts;
        }
        public void setMaxRestartAttempts(int maxRestartAttempts) {
            this.maxRestartAttempts = maxRestartAttempts;
        }    }
}
