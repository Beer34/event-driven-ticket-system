package com.ticket.notification_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PaymentEventTest {

    @Test
    void testGettersAndSetters() {

        PaymentEvent event = new PaymentEvent();

        event.setBookingId("B123");
        event.setStatus("SUCCESS");

        assertEquals("B123", event.getBookingId());
        assertEquals("SUCCESS", event.getStatus());
    }
}