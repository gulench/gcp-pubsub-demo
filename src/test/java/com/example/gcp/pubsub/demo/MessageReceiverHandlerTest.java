package com.example.gcp.pubsub.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.pubsub.v1.PubsubMessage;

class MessageReceiverHandlerTest {

    private MessageProcessor processor;
    private SubscriberMetrics metrics;
    private Runnable onSuccess;
    private MessageReceiverHandler handler;
    private Clock clock;
    private Instant now;

    @BeforeEach
    void setUp() {
        processor = mock(MessageProcessor.class);
        metrics = mock(SubscriberMetrics.class);
        onSuccess = mock(Runnable.class);
        now = Instant.parse("2023-10-01T12:00:00Z");
        clock = Clock.fixed(now, ZoneId.of("UTC"));

        handler = new MessageReceiverHandler(processor, metrics, clock, onSuccess);
    }

    @Test
    void accept_successfulProcessing_acksAndUpdatesMetrics() throws Exception {
        // Arrange
        BasicAcknowledgeablePubsubMessage ackMessage = mock(BasicAcknowledgeablePubsubMessage.class);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().build();
        when(ackMessage.getPubsubMessage()).thenReturn(pubsubMessage);

        // Act
        handler.accept(ackMessage);

        // Assert
        verify(metrics).incrementMessage();
        verify(processor).process(pubsubMessage);
        verify(ackMessage).ack();
        verify(onSuccess).run();

        assertThat(handler.getLastMessageTime()).isEqualTo(now);
    }

    @Test
    void accept_failedProcessing_nacksAndUpdatesMetrics() throws Exception {
        // Arrange
        BasicAcknowledgeablePubsubMessage ackMessage = mock(BasicAcknowledgeablePubsubMessage.class);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().build();
        when(ackMessage.getPubsubMessage()).thenReturn(pubsubMessage);

        doThrow(new RuntimeException("Processing failed")).when(processor).process(pubsubMessage);

        // Act
        handler.accept(ackMessage);

        // Assert
        verify(metrics).incrementMessage(); // We still count the attempt
        verify(processor).process(pubsubMessage);
        verify(ackMessage).nack();
        verifyNoInteractions(onSuccess); // Success callback not triggered

        assertThat(handler.getLastMessageTime()).isEqualTo(now);
    }

    @Test
    void getLastMessageTime_updatesOnCall() {
        Instant later = now.plusSeconds(10);
        Clock laterClock = Clock.fixed(later, ZoneId.of("UTC"));
        handler = new MessageReceiverHandler(processor, metrics, laterClock, onSuccess);

        handler.accept(mock(BasicAcknowledgeablePubsubMessage.class));
        assertThat(handler.getLastMessageTime()).isEqualTo(later);
    }
}
