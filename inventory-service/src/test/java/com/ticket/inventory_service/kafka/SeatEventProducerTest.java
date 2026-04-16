package com.ticket.inventory_service.kafka;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.kafka.core.KafkaTemplate;

import com.ticket.inventory_service.event.SeatEvent;

public class SeatEventProducerTest {

    @Test
    void testSendSeatEvent() {

        KafkaTemplate<String, SeatEvent> kafkaTemplate = mock(KafkaTemplate.class);

        SeatEventProducer producer = new SeatEventProducer(kafkaTemplate);

        SeatEvent event = new SeatEvent("A1", "E1", "RESERVED");

        producer.sendSeatEvent(event);

        verify(kafkaTemplate, times(1)).send("seat-events", event);
    }
}