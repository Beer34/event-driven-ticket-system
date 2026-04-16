package com.ticket.payment_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ticket.payment_service.model.PaymentEvent;

@Service
public class PaymentProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentEvent(PaymentEvent event) {
        kafkaTemplate.send("payment-events", event);
        System.out.println("Payment Event Sent: " + event.getStatus() + " for " + event.getBookingId());
    }
    public void sendToDLQ(PaymentEvent event) {
    kafkaTemplate.send("payment-dlq", event);
    System.out.println("Sent to DLQ: " + event.getBookingId());
}
}