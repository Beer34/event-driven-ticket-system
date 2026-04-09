package com.ticket.bookingservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.bookingservice.kafka.PaymentRequestProducer;
import com.ticket.bookingservice.model.Event;
import com.ticket.bookingservice.model.PaymentRequest;
import com.ticket.bookingservice.model.Seat;
import com.ticket.bookingservice.service.EventService;
import com.ticket.bookingservice.service.InventoryServiceClient;

@RestController
public class EventController {

    private final EventService eventService;
    private final InventoryServiceClient inventoryServiceClient;
    private final PaymentRequestProducer paymentRequestProducer;

    public EventController(EventService eventService,
                       InventoryServiceClient inventoryServiceClient,
                       PaymentRequestProducer paymentRequestProducer) {
    this.eventService = eventService;
    this.inventoryServiceClient = inventoryServiceClient;
    this.paymentRequestProducer = paymentRequestProducer;
}

    @GetMapping("/events")
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/events/{eventId}/seats")
    public List<Seat> getSeats(@PathVariable String eventId) {
        return inventoryServiceClient.getSeats(eventId);
    }
   @PostMapping("/pay/{seatId}")
    public String pay(@PathVariable String seatId,
                  @RequestParam(defaultValue = "false") boolean fail) {

        String bookingId = "B" + System.currentTimeMillis();

        PaymentRequest request = new PaymentRequest(bookingId, seatId, 100);
        request.setForceFail(fail);

        paymentRequestProducer.sendPaymentRequest(request);

        return "Payment initiated for seat " + seatId;
}
}