package com.ticket.bookingservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ticket.bookingservice.model.PaymentEvent;

@Service
public class PaymentEventConsumer {

    @KafkaListener(
    topics = "payment-events",
    groupId = "booking-service-group",
    properties = {
        "spring.json.value.default.type=com.ticket.bookingservice.model.PaymentEvent"
    }
)
public void consumePayment(PaymentEvent event) {

    System.out.println("Received Payment Status: " + event.getStatus());

    if ("SUCCESS".equals(event.getStatus())) {
        System.out.println("Booking Confirmed for: " + event.getBookingId());
    } else {
        System.out.println("Payment Failed for: " + event.getBookingId());
    }
}
}