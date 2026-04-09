package com.ticket.notification_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ticket.notification_service.model.PaymentEvent;

@Service
public class NotificationConsumer {

    @KafkaListener(topics = "payment-events", groupId = "notification-group")
    public void consume(PaymentEvent event) {

        if ("SUCCESS".equals(event.getStatus())) {
            System.out.println("📧 EMAIL SENT: Booking confirmed for " + event.getBookingId());
        } else {
            System.out.println("📧 EMAIL SENT: Payment failed for " + event.getBookingId());
        }
    }
}