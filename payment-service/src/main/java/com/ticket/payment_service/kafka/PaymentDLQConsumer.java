package com.ticket.payment_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ticket.payment_service.model.PaymentRequest;

@Service
public class PaymentDLQConsumer {

    @KafkaListener(topics = "payment-dlq", groupId = "dlq-group")
public void consume(Object event) {

    System.out.println(" DLQ ALERT RECEIVED");

    if (event instanceof PaymentRequest req) {
        System.out.println("Failed Booking ID: " + req.getBookingId());
    }
}
}