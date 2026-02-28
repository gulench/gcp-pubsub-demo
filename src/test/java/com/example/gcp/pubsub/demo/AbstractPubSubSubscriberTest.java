package com.example.gcp.pubsub.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

// MockitoExtension resets and reinitialises every @Mock field before each test,
// so stubs set up in one test can never bleed into the next.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AbstractPubSubSubscriberTest {

    static class TestSubscriber extends AbstractPubSubSubscriber {
        TestSubscriber(PubSubSubscriberTemplate template,
                ScheduledExecutorService scheduler,
                Clock clock) {
            super(template, "test-subscription", scheduler, clock);
        }

        @Override
        protected void processMessage(String payload) {
        }
    }

    // Fresh mock injected by Mockito before every test — no stub bleed-over.
    @Mock
    private PubSubSubscriberTemplate template;

    @Mock
    private Subscriber mockSubscriber;

    // A mock scheduler so no tasks ever fire in the background automatically,
    // keeping all tests fully deterministic.
    @Mock
    private ScheduledExecutorService scheduler;

    private ManuallyAdvancedClock clock;
    private TestSubscriber subscriber;

    @BeforeEach
    void setUp() {
        // Use a fixed start time for deterministic tests.
        clock = ManuallyAdvancedClock.at(Instant.parse("2023-01-01T10:00:00Z"), ZoneOffset.UTC);

        when(template.subscribe(anyString(), any())).thenReturn(mockSubscriber);
        when(mockSubscriber.state()).thenReturn(Subscriber.State.RUNNING);
        when(mockSubscriber.stopAsync()).thenReturn(mockSubscriber);

        subscriber = new TestSubscriber(template, scheduler, clock);
        subscriber.start();
    }

    @AfterEach
    void tearDown() {
        subscriber.stop();
    }

    @Test
    void start_subscribesToCorrectSubscription() {
        verify(template).subscribe(anyString(), any());
    }

    @Test
    void healthCheck_doesNotReconnect_whenSubscriberIsRunning() {
        clearInvocations(template);

        subscriber.runHealthCheck();

        verify(template, never()).subscribe(anyString(), any());
    }

    @Test
    void healthCheck_reconnects_whenSubscriberIsFailed() {
        when(mockSubscriber.state()).thenReturn(Subscriber.State.FAILED);
        clearInvocations(template);

        subscriber.runHealthCheck();

        verify(template).subscribe(anyString(), any());
    }

    @Test
    void healthCheck_reconnects_whenSubscriberIsNull() {
        // Use a fresh template mock so the thenThrow stub is scoped entirely to
        // this test and cannot affect any other test's setUp().
        PubSubSubscriberTemplate isolatedTemplate = mock(PubSubSubscriberTemplate.class);
        when(isolatedTemplate.subscribe(anyString(), any()))
                .thenThrow(new RuntimeException("unavailable"));

        TestSubscriber failingSubscriber = new TestSubscriber(isolatedTemplate, scheduler, clock);
        failingSubscriber.start(); // connect() catches the exception; subscriber stays null

        // Re-stub to succeed so the health check can now recover.
        doReturn(mockSubscriber).when(isolatedTemplate).subscribe(anyString(), any());
        clearInvocations(isolatedTemplate);

        failingSubscriber.runHealthCheck();

        verify(isolatedTemplate).subscribe(anyString(), any());

        failingSubscriber.stop();
    }

    @Test
    void healthCheck_reconnects_whenSubscriberIsIdleTooLong() {
        // MAX_IDLE_SECONDS is 120.
        // Advance the clock beyond MAX_IDLE_SECONDS with no message arriving.
        clock.advanceBy(Duration.ofSeconds(121));
        clearInvocations(template);

        subscriber.runHealthCheck();

        // It should reconnect because it has been idle for too long.
        verify(template).subscribe(anyString(), any());
    }

    @Test
    void healthCheck_doesNotReconnect_whenIdleButWithinThreshold() {
        // MAX_IDLE_SECONDS is 120.
        // Advance the clock but stay within the idle threshold.
        clock.advanceBy(Duration.ofSeconds(119));
        clearInvocations(template);

        subscriber.runHealthCheck();

        // It should NOT reconnect because it is still within the idle threshold.
        verify(template, never()).subscribe(anyString(), any());
    }

    @Test
    void handleMessage_resetsIdleTimer_andAcks() {
        // 1. Advance time to just before the idle threshold.
        clock.advanceBy(Duration.ofSeconds(119));

        // 2. Deliver a message, which should reset the idle timer.
        BasicAcknowledgeablePubsubMessage message = mockMessage("hello");
        captureMessageConsumer().accept(message);

        // 3. Advance time again, but by an amount that would have previously
        //    pushed it over the threshold.
        clock.advanceBy(Duration.ofSeconds(2)); // Total idle time from start is 121s

        // 4. A health check should NOT reconnect, because the timer was reset.
        clearInvocations(template);
        subscriber.runHealthCheck();
        verify(template, never()).subscribe(anyString(), any());

        // 5. Verify the message was acked.
        verify(message).ack();
        verify(message, never()).nack();

        // 6. Now, advance time past the idle threshold *from when the message arrived*
        //    and verify it DOES reconnect.
        clock.advanceBy(Duration.ofSeconds(121));
        subscriber.runHealthCheck();
        verify(template).subscribe(anyString(), any());
    }

    @Test
    void handleMessage_nacks_whenProcessMessageThrows() {
        PubSubSubscriberTemplate isolatedTemplate = mock(PubSubSubscriberTemplate.class);
        when(isolatedTemplate.subscribe(anyString(), any())).thenReturn(mockSubscriber);
        when(mockSubscriber.stopAsync()).thenReturn(mockSubscriber);

        TestSubscriber failingSubscriber = new TestSubscriber(isolatedTemplate, scheduler, clock) {
            @Override
            protected void processMessage(String payload) {
                throw new RuntimeException("processing failed");
            }
        };
        failingSubscriber.start();

        BasicAcknowledgeablePubsubMessage message = mockMessage("bad-payload");
        captureMessageConsumer(isolatedTemplate).accept(message);

        verify(message, never()).ack();
        verify(message).nack();

        failingSubscriber.stop();
    }

    @Test
    void stop_doesNotReconnect_afterShutdown() {
        subscriber.stop();
        clearInvocations(template);

        subscriber.runHealthCheck();

        verify(template, never()).subscribe(anyString(), any());
    }

    // ----------------------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------------------

    private Consumer<BasicAcknowledgeablePubsubMessage> captureMessageConsumer() {
        return captureMessageConsumer(template);
    }

    private Consumer<BasicAcknowledgeablePubsubMessage> captureMessageConsumer(
            PubSubSubscriberTemplate targetTemplate) {
        ArgumentCaptor<Consumer<BasicAcknowledgeablePubsubMessage>> captor = ArgumentCaptor.captor();
        verify(targetTemplate, atLeastOnce()).subscribe(anyString(), captor.capture());
        return captor.getValue();
    }

    private BasicAcknowledgeablePubsubMessage mockMessage(String payload) {
        BasicAcknowledgeablePubsubMessage message = mock(BasicAcknowledgeablePubsubMessage.class);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(payload))
                .build();
        when(message.getPubsubMessage()).thenReturn(pubsubMessage);
        return message;
    }
}
