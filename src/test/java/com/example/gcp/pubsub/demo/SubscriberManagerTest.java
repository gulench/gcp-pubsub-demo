package com.example.gcp.pubsub.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.health.contributor.Status;
import org.springframework.scheduling.TaskScheduler;

import com.example.gcp.pubsub.demo.SubscribersProperties.SubscriptionConfig;

class SubscriberManagerTest {

    private SubscriberManager manager;
    private ManagedSubscriber mockSubscriber;
    private MessageReceiverHandler mockHandler;
    private SubscriberComponentFactory factory;
    private TaskScheduler scheduler;
    private BackoffStrategy backoff;
    private SubscriberMetrics metrics;

    private SubscriptionConfig config;
    private Instant now;

    @BeforeEach
    void setUp() {
        mockSubscriber = mock(ManagedSubscriber.class);
        mockHandler = mock(MessageReceiverHandler.class);
        scheduler = mock(TaskScheduler.class);
        backoff = mock(BackoffStrategy.class);
        metrics = mock(SubscriberMetrics.class);
        factory = mock(SubscriberComponentFactory.class);
        MessageProcessor processor = mock(MessageProcessor.class);

        config = new SubscriptionConfig();
        config.setSubscriptionId("test-sub");
        config.setStallDetectionMode(StallDetectionMode.DETECT_AND_RESTART);
        config.setStallThreshold(Duration.ofSeconds(10));
        config.setMonitorInterval(Duration.ofSeconds(30));
        config.setMaxRestartAttempts(5);

        now = Instant.parse("2023-01-01T10:00:00Z");
        Clock clock = Clock.fixed(now, ZoneId.of("UTC"));

        when(factory.getScheduler()).thenReturn(scheduler);
        when(factory.getClock()).thenReturn(clock);
        when(factory.createMetrics(anyString())).thenReturn(metrics);
        when(factory.createBackoff(any(SubscriptionConfig.class))).thenReturn(backoff);
        when(factory.createHandler(any(MessageProcessor.class), any(SubscriberMetrics.class), any(Runnable.class))).thenReturn(mockHandler);
        when(factory.createSubscriber(anyString(), any(), any(Runnable.class))).thenReturn(mockSubscriber);

        manager = new SubscriberManager("test-manager", config, processor, factory);
    }

    private Runnable captureFailureCallback() {
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(factory, atLeastOnce()).createSubscriber(anyString(), any(), captor.capture());
        return captor.getValue();
    }

    private Runnable captureSuccessCallback() {
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(factory, atLeastOnce()).createHandler(any(MessageProcessor.class), any(SubscriberMetrics.class), captor.capture());
        return captor.getValue();
    }

    @Test
    void start_startsSubscriberAndSchedulesMonitor() {
        ScheduledFuture<?> future = mock(ScheduledFuture.class);

        doReturn(future)
            .when(scheduler)
            .scheduleWithFixedDelay(any(Runnable.class), any(Duration.class));

        manager.start();

        verify(mockSubscriber).start();
        verify(scheduler).scheduleWithFixedDelay(any(), eq(config.getMonitorInterval()));
        assertThat(manager.isRunning()).isTrue();
    }

    @Test
    void start_isIdempotent() {
        manager.start();
        manager.start();

        verify(mockSubscriber, times(1)).start();
        verify(scheduler, times(1)).scheduleWithFixedDelay(any(), any());
    }

    @Test
    void stop_stopsSubscriberAndCancelsMonitor() {
        ScheduledFuture<?> future = mock(ScheduledFuture.class);

        doReturn(future)
            .when(scheduler)
            .scheduleWithFixedDelay(any(Runnable.class), any(Duration.class));

        manager.start();
        manager.stop();

        verify(mockSubscriber).stop();
        verify(future).cancel(true);
        assertThat(manager.isRunning()).isFalse();
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
    void pause_onInactiveManager_doesNotStopSubscriberAgain() {
        manager.stop(); // Already calls managedSubscriber.stop()
        clearInvocations(mockSubscriber);

        manager.pause();

        verify(mockSubscriber, never()).stop();
        assertThat(manager.getState()).isEqualTo("PAUSED");
    }

    @Test
    void resume_onInactiveManager_doesNotStartSubscriber() {
        manager.resume();
        verify(mockSubscriber, never()).start();
        assertThat(manager.getState()).isEqualTo("STOPPED");
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
    void restartTask_restartsSubscriber() {
        manager.start();

        ScheduledFuture<?> future = mock(ScheduledFuture.class);

        doReturn(future)
            .when(scheduler)
            .scheduleWithFixedDelay(any(Runnable.class), any(Duration.class));

        // Capture the restart task scheduled by a failure
        captureFailureCallback().run();
        ArgumentCaptor<Runnable> restartTaskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).schedule(restartTaskCaptor.capture(), any(Instant.class));

        // Execute restart
        restartTaskCaptor.getValue().run();

        // Should stop and start
        verify(mockSubscriber).stop();
        verify(mockSubscriber, times(2)).start(); // Once initial, once restart
    }

    @Test
    void restart_doesNotRun_ifStopped() {
        manager.start();

        ScheduledFuture<?> future = mock(ScheduledFuture.class);

        doReturn(future)
            .when(scheduler)
            .scheduleWithFixedDelay(any(Runnable.class), any(Duration.class));

        // Capture restart task
        captureFailureCallback().run();
        ArgumentCaptor<Runnable> restartTaskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).schedule(restartTaskCaptor.capture(), any(Instant.class));

        // Stop manager before task runs
        manager.stop();
        clearInvocations(mockSubscriber);

        // Run restart task
        restartTaskCaptor.getValue().run();

        verify(mockSubscriber, never()).start();
    }

    @Test
    void restartTask_respectsPausedState() {
        manager.start();

        // 1. Capture restart task scheduled by a failure
        captureFailureCallback().run();
        ArgumentCaptor<Runnable> restartTaskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).schedule(restartTaskCaptor.capture(), any(Instant.class));

        // 2. Pause the manager before the task executes
        manager.pause();
        clearInvocations(mockSubscriber);

        // 3. Execute the pending restart task
        restartTaskCaptor.getValue().run();

        // 4. Subscriber should NOT be started because manager is paused
        verify(mockSubscriber, never()).start();
    }

    @Test
    void monitor_detectsStall_andSchedulesRestart() {
        // Arrange
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);
        // Force stall detection via the handler's new encapsulated logic
        when(mockHandler.isStalled(config.getStallThreshold())).thenReturn(true);
        when(mockHandler.getLastMessageTime()).thenReturn(now.minus(config.getStallThreshold().plus(Duration.ofSeconds(1))));

        // Capture the monitor task
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).scheduleWithFixedDelay(taskCaptor.capture(), any(Duration.class));

        ScheduledFuture<?> future = mock(ScheduledFuture.class);

        doReturn(future)
            .when(scheduler)
            .scheduleWithFixedDelay(any(Runnable.class), any(Duration.class));

        // Act: Run monitor
        taskCaptor.getValue().run();

        // Assert
        verify(mockHandler).recordIdleMetrics();
        verify(metrics).incrementStall();
        // Should schedule a restart task
        verify(scheduler).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void monitor_noStall_ifWithinThreshold() {
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);
        // Mock handler reporting no stall
        when(mockHandler.isStalled(config.getStallThreshold())).thenReturn(false);
        when(mockHandler.getLastMessageTime()).thenReturn(now.minus(config.getStallThreshold().minus(Duration.ofSeconds(1))));

        // Capture the monitor task

        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).scheduleWithFixedDelay(taskCaptor.capture(), any(Duration.class));

        taskCaptor.getValue().run();

        // Should NOT schedule a restart task
        verify(metrics, never()).incrementStall();
        verify(scheduler, never()).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void monitor_recordsIdleMetrics_evenWhenHealthy() {
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);
        when(mockHandler.isStalled(any())).thenReturn(false);

        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).scheduleWithFixedDelay(taskCaptor.capture(), any(Duration.class));

        // Run monitor
        taskCaptor.getValue().run();

        verify(mockHandler).recordIdleMetrics();
    }

    @Test
    void monitor_skipsIfPaused() {
        manager.start();
        manager.pause();

        // Ensure monitor is a no-op when paused
        when(mockHandler.isStalled(any(Duration.class))).thenReturn(true);

        // Run monitor
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).scheduleWithFixedDelay(taskCaptor.capture(), any(Duration.class));
        taskCaptor.getValue().run();

        verify(mockHandler, never()).recordIdleMetrics();
        verify(metrics, never()).incrementStall();
    }

    @Test
    void monitor_doesNothing_whenDetectionDisabled() {
        config.setStallDetectionMode(StallDetectionMode.NO_DETECTION);
        manager.start();

        // Even if stalled internally
        when(mockHandler.isStalled(any(Duration.class))).thenReturn(true);
        when(mockHandler.getLastMessageTime()).thenReturn(now.minus(config.getStallThreshold().plus(Duration.ofSeconds(1))));

        // Run monitor
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).scheduleWithFixedDelay(taskCaptor.capture(), any(Duration.class));
        taskCaptor.getValue().run();

        verify(mockHandler, never()).recordIdleMetrics();
        verify(metrics, never()).incrementStall();
        verify(scheduler, never()).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void monitor_logsWarningOnly_whenModeIsWarn() {
        config.setStallDetectionMode(StallDetectionMode.DETECT_AND_WARN);
        manager.start();

        // Force stall detection
        when(mockHandler.isStalled(config.getStallThreshold())).thenReturn(true);
        when(mockHandler.getLastMessageTime()).thenReturn(now.minus(config.getStallThreshold().plus(Duration.ofSeconds(1))));

        // Run monitor
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, atLeastOnce()).scheduleWithFixedDelay(taskCaptor.capture(), any(Duration.class));
        taskCaptor.getValue().run();

        verify(mockHandler).recordIdleMetrics();
        verify(metrics).incrementStall();
        // Should NOT schedule restart
        verify(scheduler, never()).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void failureCallback_schedulesRestart() {
        manager.start();

        ScheduledFuture<?> future = mock(ScheduledFuture.class);

        doReturn(future)
            .when(scheduler)
            .scheduleWithFixedDelay(any(Runnable.class), any(Duration.class));

        // Simulate subscriber failure
        captureFailureCallback().run();

        verify(metrics).incrementRestart();
        verify(backoff).nextDelay(1);
        verify(scheduler).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void successfulMessage_resetsBackoff_afterFailure() {
        manager.start();

        // 1. Fail once
        captureFailureCallback().run();
        verify(backoff).nextDelay(1);

        // 2. Success
        captureSuccessCallback().run();
        verify(backoff).reset();

        // 3. Fail again - should be attempt 1 again if reset worked.
        clearInvocations(backoff);
        captureFailureCallback().run();
        verify(backoff).nextDelay(1);
    }

    @Test
    void health_reportsUp_whenRunning() {
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);

        assertThat(manager.health().getStatus()).isEqualTo(Status.UP);
        assertThat(manager.health().getDetails()).containsEntry("state", "running");
    }

    @Test
    void health_reportsDown_whenStopped() {
        manager.stop();
        assertThat(manager.health().getStatus()).isEqualTo(Status.DOWN);
        assertThat(manager.getState()).isEqualTo("STOPPED");
    }

    @Test
    void health_reportsRecovering_whenBackingOff() {
        manager.start();

        // Simulate failure and backoff
        when(mockSubscriber.isRunning()).thenReturn(false);
        captureFailureCallback().run(); // attempt 1

        // Health should be UP (Recovering) because attempts (1) <= max (default 5)
        assertThat(manager.health().getStatus()).isEqualTo(Status.UP);
        assertThat(manager.health().getDetails()).containsEntry("state", "recovering");
    }

    @Test
    void health_reportsDown_whenRestartAttemptsExhausted() {
        manager.start();
        config.setMaxRestartAttempts(1);

        // Simulate multiple failures to exceed max attempts
        captureFailureCallback().run(); // attempt 1
        captureFailureCallback().run(); // attempt 2

        when(mockSubscriber.isRunning()).thenReturn(false);

        assertThat(manager.health().getStatus()).isEqualTo(Status.DOWN);
        assertThat(manager.health().getDetails()).containsEntry("state", "failed");
    }

    @Test
    void isHealthy_returnsTrue_whenRunning() {
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(true);
        assertThat(manager.isHealthy()).isTrue();
    }

    @Test
    void isHealthy_returnsTrue_whenRecovering() {
        manager.start();
        when(mockSubscriber.isRunning()).thenReturn(false);
        captureFailureCallback().run(); // attempt 1 <= default 5
        assertThat(manager.isHealthy()).isTrue();
    }

    @Test
    void isHealthy_returnsFalse_whenStopped() {
        manager.stop();
        assertThat(manager.isHealthy()).isFalse();
    }

    @Test
    void isHealthy_returnsFalse_whenPaused() {
        manager.start();
        manager.pause();
        assertThat(manager.isHealthy()).isFalse();
    }

    @Test
    void isHealthy_returnsFalse_whenFailed() {
        manager.start();
        config.setMaxRestartAttempts(0);
        when(mockSubscriber.isRunning()).thenReturn(false);
        captureFailureCallback().run(); // attempt 1 > max 0
        assertThat(manager.isHealthy()).isFalse();
    }

    @Test
    void getState_returnsStateFromSubscriber_whenActive() {
        manager.start();

        when(mockSubscriber.getState()).thenReturn("STARTING");
        assertThat(manager.getState()).isEqualTo("STARTING");

        when(mockSubscriber.getState()).thenReturn("RUNNING");
        assertThat(manager.getState()).isEqualTo("RUNNING");

        when(mockSubscriber.getState()).thenReturn("FAILED");
        assertThat(manager.getState()).isEqualTo("FAILED");
    }
}
