package com.ticket.bookingservice.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ticket.bookingservice.kafka.PaymentRequestProducer;
import com.ticket.bookingservice.model.Event;
import com.ticket.bookingservice.model.Seat;
import com.ticket.bookingservice.service.EventService;
import com.ticket.bookingservice.service.InventoryServiceClient;

public class EventControllerTest {

    private EventService eventService;
    private InventoryServiceClient inventoryServiceClient;
    private PaymentRequestProducer paymentRequestProducer;

    private EventController eventController;

    @BeforeEach
    void setup() {
        eventService = mock(EventService.class);
        inventoryServiceClient = mock(InventoryServiceClient.class);
        paymentRequestProducer = mock(PaymentRequestProducer.class);

        eventController = new EventController(
                eventService,
                inventoryServiceClient,
                paymentRequestProducer
        );
    }

    @Test
    void testGetEvents() {
        List<Event> events = List.of(
                new Event("E1", "Avengers", "18:00"),
                new Event("E2", "Batman", "21:00")
        );

        when(eventService.getEvents()).thenReturn(events);

        List<Event> result = eventController.getEvents();

        assertEquals(2, result.size());
    }

    @Test
    void testGetSeats() {
        List<Seat> seats = List.of(new Seat(), new Seat());

        when(inventoryServiceClient.getSeats("E1")).thenReturn(seats);

        List<Seat> result = eventController.getSeats("E1");

        assertEquals(2, result.size());
    }

    @Test
    void testPaySuccess() {
        String response = eventController.pay("A1", false);

        assertTrue(response.contains("Payment initiated"));

        verify(paymentRequestProducer, times(1))
                .sendPaymentRequest(any());
    }

    @Test
    void testPayFailureFlag() {
        String response = eventController.pay("A1", true);

        assertTrue(response.contains("Payment initiated"));

        verify(paymentRequestProducer, times(1))
                .sendPaymentRequest(any());
    }
}