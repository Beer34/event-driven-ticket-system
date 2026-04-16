package com.ticket.notification_service.kafka;

import org.junit.jupiter.api.Test;

import com.ticket.notification_service.model.PaymentEvent;

class NotificationConsumerEdgeTest {

    private final NotificationConsumer consumer = new NotificationConsumer();

    @Test
    void testConsumeWithNullStatus() {

        PaymentEvent event = new PaymentEvent();
        event.setBookingId("B3");
        event.setStatus(null);

        consumer.consume(event);

        // covers null branch
    }

    @Test
    void testConsumeWithUnknownStatus() {

        PaymentEvent event = new PaymentEvent();
        event.setBookingId("B4");
        event.setStatus("UNKNOWN");

        consumer.consume(event);

        // covers non-success unexpected branch
    }
}