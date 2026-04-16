package com.ticket.payment_service;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ticket.payment_service.kafka.PaymentConsumer;
import com.ticket.payment_service.kafka.PaymentDLQConsumer;
import com.ticket.payment_service.model.PaymentRequest;
import com.ticket.payment_service.service.PaymentService;

public class KafkaTest {

    @Test
    void testPaymentConsumer() {

        PaymentService service = mock(PaymentService.class);
        PaymentConsumer consumer = new PaymentConsumer(service);

        PaymentRequest request = new PaymentRequest("B1", "A1", 100);

        consumer.consume(request);

        verify(service, times(1)).processPayment(request);
    }

    @Test
    void testDLQConsumerWithDifferentObject() {

        PaymentDLQConsumer consumer = new PaymentDLQConsumer();

        // Test with PaymentRequest
        PaymentRequest request = new PaymentRequest("B1", "A1", 100);
        consumer.consume(request);

        // Test with unrelated object (covers branch)
        consumer.consume("Some Random Object");
    }
}
