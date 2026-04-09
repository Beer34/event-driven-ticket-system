package com.ticket.inventory_service.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ticket.inventory_service.entity.Seat;
import com.ticket.inventory_service.entity.SeatStatus;
import com.ticket.inventory_service.service.SeatService;

public class SeatControllerTest {

    @Mock
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    public SeatControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableSeats() {

        Seat seat = new Seat("A1", "E1", SeatStatus.AVAILABLE);

        when(seatService.getAvailableSeats("E1"))
                .thenReturn(List.of(seat));

        List<Seat> result = seatController.getAvailableSeats("E1");

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getId());
    }

    @Test
    void testReserveSeatSuccess() {

        when(seatService.reserveSeat("A1"))
                .thenReturn(new Seat("A1", "E1", SeatStatus.RESERVED));

        String response = seatController.reserveSeat("A1");

        assertEquals("Seat A1 reserved successfully", response);
    }

    @Test
    void testReserveSeatFailure() {

        when(seatService.reserveSeat("A1"))
                .thenThrow(new RuntimeException("Seat already reserved"));

        String response = seatController.reserveSeat("A1");

        assertEquals("Seat already reserved", response);
    }
}