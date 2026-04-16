package com.ticket.payment_service.model;

public class PaymentEvent {

    private String bookingId;
    private String status;

    public PaymentEvent() {}

    public PaymentEvent(String bookingId, String status) {
        this.bookingId = bookingId;
        this.status = status;
    }

    public String getBookingId() { return bookingId; }
    public String getStatus() { return status; }
}