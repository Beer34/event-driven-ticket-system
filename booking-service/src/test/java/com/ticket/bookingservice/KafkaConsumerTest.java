package com.ticket.bookingservice;

import org.junit.jupiter.api.Test;

import com.ticket.bookingservice.event.SeatEvent;
import com.ticket.bookingservice.kafka.PaymentEventConsumer;
import com.ticket.bookingservice.kafka.SeatEventConsumer;
import com.ticket.bookingservice.model.PaymentEvent;

public class KafkaConsumerTest {

    @Test
    void testSeatEventConsumer() {
        SeatEventConsumer consumer = new SeatEventConsumer();

        SeatEvent event = new SeatEvent();
        event.setSeatId("A1");
        event.setStatus("RESERVED");

        consumer.consumeSeatEvent(event);
    }

    @Test
    void testPaymentEventConsumerSuccess() {
        PaymentEventConsumer consumer = new PaymentEventConsumer();

        PaymentEvent event = new PaymentEvent();
        event.setBookingId("B1");
        event.setStatus("SUCCESS");

        consumer.consumePayment(event);
    }

    @Test
    void testPaymentEventConsumerFailure() {
        PaymentEventConsumer consumer = new PaymentEventConsumer();

        PaymentEvent event = new PaymentEvent();
        event.setBookingId("B1");
        event.setStatus("FAILED");

        consumer.consumePayment(event);
    }
}