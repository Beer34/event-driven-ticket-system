package com.ticket.payment_service;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.kafka.core.KafkaTemplate;

import com.ticket.payment_service.kafka.PaymentProducer;
import com.ticket.payment_service.model.PaymentEvent;

public class PaymentProducerTest {

    @Test
    void testSendPaymentEvent() {

        KafkaTemplate<String, PaymentEvent> kafkaTemplate = mock(KafkaTemplate.class);
        PaymentProducer producer = new PaymentProducer(kafkaTemplate);

        PaymentEvent event = new PaymentEvent("B1", "SUCCESS");

        producer.sendPaymentEvent(event);

        verify(kafkaTemplate, times(1))
                .send("payment-events", event);
    }

    @Test
    void testSendToDLQ() {

        KafkaTemplate<String, PaymentEvent> kafkaTemplate = mock(KafkaTemplate.class);
        PaymentProducer producer = new PaymentProducer(kafkaTemplate);

        PaymentEvent event = new PaymentEvent("B1", "FAILED");

        producer.sendToDLQ(event);

        verify(kafkaTemplate, times(1))
                .send("payment-dlq", event);
    }
}