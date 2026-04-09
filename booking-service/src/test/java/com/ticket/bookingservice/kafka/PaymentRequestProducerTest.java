package com.ticket.bookingservice.kafka;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.kafka.core.KafkaTemplate;

import com.ticket.bookingservice.model.PaymentRequest;

public class PaymentRequestProducerTest {

    @Test
    void testSendPaymentRequest() {

        KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);
        PaymentRequestProducer producer = new PaymentRequestProducer(kafkaTemplate);

        PaymentRequest request = new PaymentRequest("B1", "A1", 100);

        producer.sendPaymentRequest(request);

        verify(kafkaTemplate, times(1))
                .send("payment-requests", request);
    }
}