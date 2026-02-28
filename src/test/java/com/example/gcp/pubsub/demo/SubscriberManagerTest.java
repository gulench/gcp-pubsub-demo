package com.example.gcp.pubsub.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.TaskScheduler;

import com.example.gcp.pubsub.demo.SubscribersProperties.SubscriptionConfig;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

class SubscriberManagerTest {

    private SubscriberManager manager;
    private ManagedSubscriber mockSubscriber;
    private MessageReceiverHandler mockHandler;
    private TaskScheduler scheduler;
    private BackoffStrategy backoff;
    private SubscriberMetrics metrics;
    private SubscriberMetricsFactory metricsFactory;
    private MessageProcessor processor;
    private PubSubSubscriberTemplate template;
    private SubscriptionConfig config;
    private Clock clock;
    private Instant now;
    private Runnable capturedFailureCallback;
    private Runnable capturedSuccessCallback;

    @BeforeEach
    void setUp() {
        mockSubscriber = mock(ManagedSubscriber.class);
        mockHandler = mock(MessageReceiverHandler.class);
        scheduler = mock(TaskScheduler.class);
        backoff = mock(BackoffStrategy.class);
        metrics = mock(SubscriberMetrics.class);
        metricsFactory = mock(SubscriberMetricsFactory.class);
        processor = mock(MessageProcessor.class);
        template = mock(PubSubSubscriberTemplate.class);

        when(metricsFactory.create(anyString())).thenReturn(metrics);

        config = new SubscriptionConfig();
        config.setSubscriptionId("test-sub");
        config.setStallDetectionMode(StallDetectionMode.DETECT_AND_RESTART);
        config.setStallThreshold(Duration.ofSeconds(10));
        config.setMonitorInterval(Duration.ofSeconds(30));

        now = Instant.parse("2023-01-01T10:00:00Z");
        clock = Clock.fixed(now, ZoneId.of("UTC"));

        // Factory to inject mocks and capture callbacks
        SubscriberManager.ComponentsFactory factory = new SubscriberManager.ComponentsFactory() {
            @Override
            public MessageReceiverHandler createHandler(MessageProcessor p, SubscriberMetrics m, Clock c, Runnable onSuccess) {
                capturedSuccessCallback = onSuccess;
                return mockHandler;
            }

            @Override
            public ManagedSubscriber createSubscriber(String subId, PubSubSubscriberTemplate t, Consumer<BasicAcknowledgeablePubsubMessage> h, Runnable onFailure) {
                capturedFailureCallback = onFailure;
                return mockSubscriber;
            }
        };

        manager = new SubscriberManager("test-manager", config, processor, template, scheduler, clock, backoff, metricsFactory, factory);
    }

    @Test
    void start_startsSubscriberAndSchedulesMonitor() {
        when(scheduler.scheduleWithFixedDelay(any(), any(Duration.class))).thenReturn(mock(ScheduledFuture.class));

        manager.start();

        verify(mockSubscriber).start();
        verify(scheduler).scheduleWithFixedDelay(any(), eq(config.getMonitorInterval()));
        assertThat(manager.isRunning()).isTrue();
    }

    @Test
    void stop_stopsSubscriberAndCancelsMonitor() {
        ScheduledFuture<?> future = mock(ScheduledFuture.class);
        when(scheduler.scheduleWithFixedDelay(any(), any(Duration.class))).thenReturn((ScheduledFuture) future);

        manager.start();
        manager.stop();

        verify(mockSubscriber).stop();
        verify(future).cancel(true);
        assertThat(manager.isRunning()).isFalse();
    }

    @Test
    void monitor_detectsStall_andSchedulesRestart() {
        // Arrange
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);

        // Last message was 20 seconds ago (threshold is 10s)
        when(mockHandler.getLastMessageTime()).thenReturn(now.minusSeconds(20));

        // Capture the monitor task
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).scheduleWithFixedDelay(taskCaptor.capture(), any());

        // Act: Run monitor
        taskCaptor.getValue().run();

        // Assert
        verify(metrics).incrementStall();
        // Should schedule a restart task
        verify(scheduler).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void monitor_noStall_ifWithinThreshold() {
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);
        when(mockHandler.getLastMessageTime()).thenReturn(now.minusSeconds(5)); // 5s < 10s

        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).scheduleWithFixedDelay(taskCaptor.capture(), any());

        taskCaptor.getValue().run();

        verify(metrics, never()).incrementStall();
        verify(scheduler, never()).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void failureCallback_schedulesRestart() {
        manager.start();

        // Simulate subscriber failure
        capturedFailureCallback.run();

        verify(metrics).incrementRestart();
        verify(backoff).nextDelay(1);
        verify(scheduler).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void restartTask_restartsSubscriber() {
        manager.start();

        // Capture the restart task scheduled by a failure
        capturedFailureCallback.run();
        ArgumentCaptor<Runnable> restartTaskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).schedule(restartTaskCaptor.capture(), any(Instant.class));

        // Execute restart
        restartTaskCaptor.getValue().run();

        // Should stop and start
        verify(mockSubscriber).stop();
        verify(mockSubscriber, times(2)).start(); // Once initial, once restart
    }

    @Test
    void health_reportsRecovering_whenBackingOff() {
        manager.start();

        // Simulate failure and backoff
        when(mockSubscriber.isRunning()).thenReturn(false);
        capturedFailureCallback.run(); // attempt 1

        // Health should be UP (Recovering) because attempts (1) <= max (default 5)
        assertThat(manager.health().getStatus()).isEqualTo(Status.UP);
        assertThat(manager.health().getDetails()).containsEntry("state", "recovering");
    }

    @Test
    void health_reportsUp_whenRunning() {
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);

        assertThat(manager.health().getStatus()).isEqualTo(Status.UP);
        assertThat(manager.health().getDetails()).containsEntry("state", "running");
    }

    @Test
    void pause_stopsSubscriber_andUpdatesHealth() {
        manager.start();
        manager.pause();

        verify(mockSubscriber).stop();
        assertThat(manager.health().getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
        assertThat(manager.getState()).isEqualTo("PAUSED");
    }

    @Test
    void resume_startsSubscriber_ifPreviouslyActive() {
        manager.start();
        manager.pause();
        clearInvocations(mockSubscriber);

        manager.resume();

        verify(mockSubscriber).start();
        assertThat(manager.health().getStatus()).isNotEqualTo(Status.OUT_OF_SERVICE);
    }

    @Test
    void monitor_skipsIfPaused() {
        manager.start();
        manager.pause();

        // Force stall condition
        when(mockHandler.getLastMessageTime()).thenReturn(now.minusSeconds(20));

        // Run monitor
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).scheduleWithFixedDelay(taskCaptor.capture(), any());
        taskCaptor.getValue().run();

        verify(metrics, never()).incrementStall();
    }

    @Test
    void monitor_doesNothing_whenDetectionDisabled() {
        config.setStallDetectionMode(StallDetectionMode.NO_DETECTION);
        manager.start();

        // Force stall condition
        when(mockHandler.getLastMessageTime()).thenReturn(now.minusSeconds(20));

        // Run monitor
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).scheduleWithFixedDelay(taskCaptor.capture(), any());
        taskCaptor.getValue().run();

        verify(metrics, never()).incrementStall();
        verify(scheduler, never()).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void monitor_logsWarningOnly_whenModeIsWarn() {
        config.setStallDetectionMode(StallDetectionMode.DETECT_AND_WARN);
        manager.start();

        // Force stall condition
        when(mockHandler.getLastMessageTime()).thenReturn(now.minusSeconds(20));

        // Run monitor
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).scheduleWithFixedDelay(taskCaptor.capture(), any());
        taskCaptor.getValue().run();

        verify(metrics).incrementStall();
        // Should NOT schedule restart
        verify(scheduler, never()).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void successfulMessage_resetsBackoff_afterFailure() {
        manager.start();

        // 1. Fail once
        capturedFailureCallback.run();
        verify(backoff).nextDelay(1);

        // 2. Success
        capturedSuccessCallback.run();
        verify(backoff).reset();

        // 3. Fail again - should be attempt 1 again if reset worked
        clearInvocations(backoff);
        capturedFailureCallback.run();
        verify(backoff).nextDelay(1);
    }

    @Test
    void health_reportsDown_whenStopped() {
        manager.stop();
        assertThat(manager.health().getStatus()).isEqualTo(Status.DOWN);
        assertThat(manager.getState()).isEqualTo("STOPPED");
    }

    @Test
    void restart_doesNotRun_ifStopped() {
        manager.start();

        // Capture restart task
        capturedFailureCallback.run();
        ArgumentCaptor<Runnable> restartTaskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).schedule(restartTaskCaptor.capture(), any(Instant.class));

        // Stop manager before task runs
        manager.stop();
        clearInvocations(mockSubscriber);

        // Run restart task
        restartTaskCaptor.getValue().run();

        verify(mockSubscriber, never()).start();
    }
}
