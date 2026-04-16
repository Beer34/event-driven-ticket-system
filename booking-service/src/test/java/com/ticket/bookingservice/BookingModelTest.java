package com.ticket.bookingservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.ticket.bookingservice.model.PaymentEvent;
import com.ticket.bookingservice.model.PaymentRequest;
import com.ticket.bookingservice.model.Seat;

public class BookingModelTest {

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
        PaymentEvent event = new PaymentEvent();

        event.setBookingId("B1");
        event.setStatus("SUCCESS");

        assertEquals("B1", event.getBookingId());
        assertEquals("SUCCESS", event.getStatus());
    }

    @Test
    void testSeat() {
        Seat seat = new Seat();

        assertNull(seat.getId());
        assertNull(seat.getEventId());
        assertNull(seat.getStatus());
    }

    @Test
    void testSeatEvent() {
        com.ticket.bookingservice.event.SeatEvent event =
                new com.ticket.bookingservice.event.SeatEvent();

        event.setSeatId("A1");
        event.setEventId("E1");
        event.setStatus("RESERVED");

        assertEquals("A1", event.getSeatId());
        assertEquals("E1", event.getEventId());
        assertEquals("RESERVED", event.getStatus());
    }
}