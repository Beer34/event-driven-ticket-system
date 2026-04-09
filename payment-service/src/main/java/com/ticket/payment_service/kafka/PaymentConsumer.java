package com.ticket.payment_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ticket.payment_service.model.PaymentRequest;
import com.ticket.payment_service.service.PaymentService;

@Service
public class PaymentConsumer {

    private final PaymentService paymentService;

    public PaymentConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "payment-requests", groupId = "payment-group")
    public void consume(PaymentRequest request) {

        System.out.println("Received Payment Request: " + request.getBookingId());

        paymentService.processPayment(request);
    }
}