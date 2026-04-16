package com.ticket.payment_service.service;

import java.util.Random;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ticket.payment_service.kafka.PaymentProducer;
import com.ticket.payment_service.model.PaymentEvent;
import com.ticket.payment_service.model.PaymentRequest;

@Service
public class PaymentService {

    private final PaymentProducer producer;

    public PaymentService(PaymentProducer producer) {
        this.producer = producer;
    }

    @Async
public void processPayment(PaymentRequest request) {

    int retries = 3;

    while (retries > 0) {
        try {

            Thread.sleep(2000);

            boolean success;

            // 
            if (request.isForceFail()) {
                success = false;
            } else {
                success = new Random().nextBoolean();
            }

            if (success) {
                System.out.println("Payment SUCCESS for " + request.getBookingId());

                producer.sendPaymentEvent(
                    new PaymentEvent(request.getBookingId(), "SUCCESS")
                );
                return;
            }

            throw new RuntimeException("Payment failed");

        } catch (Exception e) {
            retries--;
            System.out.println("Retrying... left: " + retries);
        }
    }

    System.out.println("Payment FAILED for " + request.getBookingId());

    producer.sendPaymentEvent(
        new PaymentEvent(request.getBookingId(), "FAILED")
    );
    // DLQ
    producer.sendToDLQ(
    new PaymentEvent(request.getBookingId(), "FAILED")
);
}
}