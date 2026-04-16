package com.ticket.payment_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ticket.payment_service.kafka.PaymentProducer;
import com.ticket.payment_service.model.PaymentRequest;
import com.ticket.payment_service.service.PaymentService;

public class PaymentServiceTest {

    private PaymentProducer producer;
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        producer = mock(PaymentProducer.class);
        paymentService = new PaymentService(producer);
    }

    @Test
    void testPaymentSuccess() {

        PaymentRequest request = new PaymentRequest("B1", "A1", 100);

        paymentService.processPayment(request);

        verify(producer, atLeastOnce())
                .sendPaymentEvent(any());
    }

    @Test
    void testPaymentForcedFailure() {

        PaymentRequest request = new PaymentRequest("B1", "A1", 100);
        request.setForceFail(true);

        paymentService.processPayment(request);

        verify(producer, atLeastOnce())
                .sendPaymentEvent(any());

        verify(producer, atLeastOnce())
                .sendToDLQ(any());
    }
}
