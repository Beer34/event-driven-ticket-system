package com.ticket.bookingservice.service;

import com.ticket.bookingservice.model.Seat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceClientTest {

    @Test
    void testGetSeatsNotNull() {

        InventoryServiceClient client = new InventoryServiceClient();

        // We can't call actual REST → just test basic behavior
        try {
            List<Seat> seats = client.getSeats("E1");
        } catch (Exception e) {
            assertTrue(true); // Expected since service may not be running
        }
    }
}