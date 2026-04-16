package com.ticket.inventory_service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ticket.inventory_service.entity.Seat;
import com.ticket.inventory_service.entity.SeatStatus;
import com.ticket.inventory_service.kafka.SeatEventProducer;
import com.ticket.inventory_service.repository.SeatRepository;
import com.ticket.inventory_service.service.SeatService;

public class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatEventProducer seatEventProducer;

    @InjectMocks
    private SeatService seatService;

    public SeatServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Test successful seat reservation
    @Test
    void testReserveSeatSuccess() {

        Seat seat = new Seat("A1", "E1", SeatStatus.AVAILABLE);

        when(seatRepository.findById("A1")).thenReturn(Optional.of(seat));
        when(seatRepository.save(any())).thenReturn(seat);

        Seat reservedSeat = seatService.reserveSeat("A1");

        assertEquals(SeatStatus.RESERVED, reservedSeat.getStatus());

        verify(seatRepository, times(1)).save(any());
        verify(seatEventProducer, times(1)).sendSeatEvent(any());
    }

    // Test reservation when seat already reserved
    @Test
    void testReserveSeatAlreadyReserved() {

        Seat seat = new Seat("A1", "E1", SeatStatus.RESERVED);

        when(seatRepository.findById("A1")).thenReturn(Optional.of(seat));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            seatService.reserveSeat("A1");
        });

        assertEquals("Seat already reserved", exception.getMessage());
    }

    // Test fetching available seats
    @Test
    void testGetAvailableSeats() {

        Seat seat1 = new Seat("A1", "E1", SeatStatus.AVAILABLE);
        Seat seat2 = new Seat("A2", "E1", SeatStatus.AVAILABLE);

        List<Seat> seats = List.of(seat1, seat2);

        when(seatRepository.findByEventIdAndStatus("E1", SeatStatus.AVAILABLE))
                .thenReturn(seats);

        List<Seat> result = seatService.getAvailableSeats("E1");

        assertEquals(2, result.size());

        verify(seatRepository, times(1))
                .findByEventIdAndStatus("E1", SeatStatus.AVAILABLE);
    }
    
    // Test seat not found
    @Test
    void testReserveSeatNotFound() {

        when(seatRepository.findById("A1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        seatService.reserveSeat("A1");
        });

        assertEquals("Seat not found", exception.getMessage());
    }

    // Test release expired seats
    @Test
    void testReleaseExpiredSeats() {

        Seat seat = new Seat("A1", "E1", SeatStatus.RESERVED);
        seat.setExpiryTime(LocalDateTime.now().minusMinutes(1));

        when(seatRepository.findAll()).thenReturn(List.of(seat));
        when(seatRepository.save(any())).thenReturn(seat);

        seatService.releaseExpiredSeats();

        assertEquals(SeatStatus.AVAILABLE, seat.getStatus());

        verify(seatRepository, times(1)).save(any());
        verify(seatEventProducer, times(1)).sendSeatEvent(any());
    }

    // Test releaseExpiredSeats when nothing to release
    @Test
    void testReleaseExpiredSeats_NoExpired() {

        Seat seat = new Seat("A1", "E1", SeatStatus.RESERVED);
        seat.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        when(seatRepository.findAll()).thenReturn(List.of(seat));

        seatService.releaseExpiredSeats();

        verify(seatRepository, times(0)).save(any());
        verify(seatEventProducer, times(0)).sendSeatEvent(any());
    }
}