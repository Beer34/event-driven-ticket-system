package com.ticket.payment_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.ticket.payment_service.model.PaymentEvent;
import com.ticket.payment_service.model.PaymentRequest;

public class PaymentModelTest {

    @Test
    void testPaymentRequest() {

        PaymentRequest req = new PaymentRequest("B1", "A1", 100);
        req.setForceFail(true);

        assertEquals("B1", req.getBookingId());
        assertEquals("A1", req.getSeatId());
        assertEquals(100, req.getAmount());
        assertTrue(req.isForceFail());
    }

    @Test
    void testPaymentEvent() {

        PaymentEvent event = new PaymentEvent("B1", "SUCCESS");

        assertEquals("B1", event.getBookingId());
        assertEquals("SUCCESS", event.getStatus());
    }
    @Test
    void testPaymentRequestDefaultValues() {

        PaymentRequest req = new PaymentRequest();

        assertNull(req.getBookingId());
        assertNull(req.getSeatId());
        assertEquals(0.0, req.getAmount());
        assertFalse(req.isForceFail());
    }
}