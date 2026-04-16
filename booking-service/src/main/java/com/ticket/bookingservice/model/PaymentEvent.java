package com.ticket.bookingservice.model;

public class PaymentEvent {

    private String bookingId;
    private String status;

    public PaymentEvent() {}

    public String getBookingId() { return bookingId; }
    public String getStatus() { return status; }

    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public void setStatus(String status) { this.status = status; }
}