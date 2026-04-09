package com.ticket.inventory_service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class SeatEventTest {

    @Test
    void testSeatEvent() {

        SeatEvent event = new SeatEvent();

        event.setSeatId("A1");
        event.setEventId("E1");
        event.setStatus("RESERVED");

        assertEquals("A1", event.getSeatId());
        assertEquals("E1", event.getEventId());
        assertEquals("RESERVED", event.getStatus());

        assertNotNull(event.toString());
    }
}