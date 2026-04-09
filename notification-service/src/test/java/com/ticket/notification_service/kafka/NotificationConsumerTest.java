package com.ticket.notification_service.kafka;

import org.junit.jupiter.api.Test;

import com.ticket.notification_service.model.PaymentEvent;

class NotificationConsumerTest {

    private final NotificationConsumer consumer = new NotificationConsumer();

    @Test
    void testConsumeSuccess() {

        PaymentEvent event = new PaymentEvent();
        event.setBookingId("B1");
        event.setStatus("SUCCESS");

        consumer.consume(event);

        // No assertion needed → just verifying no crash
    }

    @Test
    void testConsumeFailure() {

        PaymentEvent event = new PaymentEvent();
        event.setBookingId("B2");
        event.setStatus("FAILED");

        consumer.consume(event);

        // No assertion needed → just verifying no crash
    }
}