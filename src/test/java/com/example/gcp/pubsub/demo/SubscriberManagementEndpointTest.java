package com.example.gcp.pubsub.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriberManagementEndpointTest {

    @Mock
    private SubscriberManagerRegistry registry;

    @Mock
    private SubscriberManager manager;

    private SubscriberManagementEndpoint endpoint;

    @BeforeEach
    void setUp() {
        endpoint = new SubscriberManagementEndpoint(registry);
    }

    @Test
    void list_shouldReturnAllSubscribersWithStatus() {
        // Arrange
        when(registry.getManagers()).thenReturn(Map.of("test-sub", manager));
        when(manager.getState()).thenReturn("RUNNING");
        when(manager.isRunning()).thenReturn(true);
        when(manager.isHealthy()).thenReturn(true);

        // Act
        Map<String, Object> result = endpoint.list();

        // Assert
        assertThat(result).hasSize(1).containsKey("test-sub");

        @SuppressWarnings("unchecked")
        Map<String, Object> details = (Map<String, Object>) result.get("test-sub");

        assertThat(details)
                .containsEntry("state", "RUNNING")
                .containsEntry("running", true)
                .containsEntry("healthy", true);
    }

    @Test
    void execute_start_shouldStartSubscriber() {
        when(registry.get("test-sub")).thenReturn(manager);
        endpoint.execute("start", "test-sub");
        verify(manager).start();
    }

    @Test
    void execute_stop_shouldStopSubscriber() {
        when(registry.get("test-sub")).thenReturn(manager);
        endpoint.execute("stop", "test-sub");
        verify(manager).stop();
    }

    @Test
    void execute_pause_shouldPauseSubscriber() {
        when(registry.get("test-sub")).thenReturn(manager);
        endpoint.execute("pause", "test-sub");
        verify(manager).pause();
    }

    @Test
    void execute_resume_shouldResumeSubscriber() {
        when(registry.get("test-sub")).thenReturn(manager);
        endpoint.execute("resume", "test-sub");
        verify(manager).resume();
    }

    @Test
    void execute_unknownOperation_shouldThrowException() {
        assertThatThrownBy(() -> endpoint.execute("dance", "test-sub"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown operation: dance");
    }
}