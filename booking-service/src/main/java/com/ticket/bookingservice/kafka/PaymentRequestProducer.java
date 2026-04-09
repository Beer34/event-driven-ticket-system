package com.ticket.bookingservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ticket.bookingservice.model.PaymentRequest;

@Service
public class PaymentRequestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentRequestProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentRequest(PaymentRequest request) {
        kafkaTemplate.send("payment-requests", request);
        System.out.println("Payment request sent: " + request.getBookingId());
    }
}