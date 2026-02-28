package com.example.gcp.pubsub.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.common.util.concurrent.MoreExecutors;

class ManagedSubscriberTest {

    private PubSubSubscriberTemplate template;
    private Consumer<BasicAcknowledgeablePubsubMessage> messageHandler;
    private Runnable onFailure;
    private ManagedSubscriber managedSubscriber;
    private Subscriber mockGoogleSubscriber;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        template = mock(PubSubSubscriberTemplate.class);
        messageHandler = mock(Consumer.class);
        onFailure = mock(Runnable.class);
        mockGoogleSubscriber = mock(Subscriber.class);

        managedSubscriber = new ManagedSubscriber("sub-id", template, messageHandler, onFailure);
    }

    @Test
    void start_subscribesAndAddsListener() {
        when(template.subscribe(eq("sub-id"), any())).thenReturn(mockGoogleSubscriber);

        managedSubscriber.start();

        verify(template).subscribe("sub-id", messageHandler);
        verify(mockGoogleSubscriber).addListener(any(Subscriber.Listener.class), eq(MoreExecutors.directExecutor()));
    }

    @Test
    void start_doesNothingIfAlreadyStarted() {
        when(template.subscribe(eq("sub-id"), any())).thenReturn(mockGoogleSubscriber);
        managedSubscriber.start();
        managedSubscriber.start(); // Second call

        verify(template, times(1)).subscribe(eq("sub-id"), any());
    }

    @Test
    void stop_stopsSubscriberIfRunning() {
        when(template.subscribe(eq("sub-id"), any())).thenReturn(mockGoogleSubscriber);
        managedSubscriber.start();

        managedSubscriber.stop();

        verify(mockGoogleSubscriber).stopAsync();
    }

    @Test
    void stop_doesNothingIfAlreadyStopped() {
        when(template.subscribe(eq("sub-id"), any())).thenReturn(mockGoogleSubscriber);
        managedSubscriber.start();

        managedSubscriber.stop();
        managedSubscriber.stop(); // Second call

        verify(mockGoogleSubscriber, times(1)).stopAsync();
    }

    @Test
    void stop_doesNothingIfNotStarted() {
        managedSubscriber.stop();
        verify(mockGoogleSubscriber, never()).stopAsync();
    }

    @Test
    void isRunning_returnsCorrectStatus() {
        // Not started yet
        assertThat(managedSubscriber.isRunning()).isFalse();

        when(template.subscribe(eq("sub-id"), any())).thenReturn(mockGoogleSubscriber);
        managedSubscriber.start();

        // Running state
        when(mockGoogleSubscriber.state()).thenReturn(Subscriber.State.RUNNING);
        assertThat(managedSubscriber.isRunning()).isTrue();

        // Other states
        when(mockGoogleSubscriber.state()).thenReturn(Subscriber.State.STARTING);
        assertThat(managedSubscriber.isRunning()).isFalse();

        when(mockGoogleSubscriber.state()).thenReturn(Subscriber.State.TERMINATED);
        assertThat(managedSubscriber.isRunning()).isFalse();

        // After stopping
        managedSubscriber.stop();
        assertThat(managedSubscriber.isRunning()).isFalse();
    }

    @Test
    void getState_returnsCorrectStateName() {
        // Not started yet
        assertThat(managedSubscriber.getState()).isEqualTo("STOPPED");

        when(template.subscribe(eq("sub-id"), any())).thenReturn(mockGoogleSubscriber);
        managedSubscriber.start();

        // Various states
        when(mockGoogleSubscriber.state()).thenReturn(Subscriber.State.RUNNING);
        assertThat(managedSubscriber.getState()).isEqualTo("RUNNING");

        when(mockGoogleSubscriber.state()).thenReturn(Subscriber.State.FAILED);
        assertThat(managedSubscriber.getState()).isEqualTo("FAILED");

        when(mockGoogleSubscriber.state()).thenReturn(Subscriber.State.TERMINATED);
        assertThat(managedSubscriber.getState()).isEqualTo("TERMINATED");

        // After stopping
        managedSubscriber.stop();
        assertThat(managedSubscriber.getState()).isEqualTo("STOPPED");
    }

    @Test
    void failureListener_triggersCallback() {
        when(template.subscribe(eq("sub-id"), any())).thenReturn(mockGoogleSubscriber);
        managedSubscriber.start();

        ArgumentCaptor<Subscriber.Listener> listenerCaptor = ArgumentCaptor.forClass(Subscriber.Listener.class);
        verify(mockGoogleSubscriber).addListener(listenerCaptor.capture(), any());

        // Simulate failure
        listenerCaptor.getValue().failed(Subscriber.State.RUNNING, new RuntimeException("boom"));
        verify(onFailure).run();
    }
}