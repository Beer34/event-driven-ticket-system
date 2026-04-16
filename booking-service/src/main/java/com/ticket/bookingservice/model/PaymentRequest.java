package com.ticket.bookingservice.model;

public class PaymentRequest {

    private String bookingId;
    private String seatId;
    private double amount;
    
    private boolean forceFail;

    public PaymentRequest() {}

    public PaymentRequest(String bookingId, String seatId, double amount) {
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.amount = amount;
    }

    public String getBookingId() { return bookingId; }
    public String getSeatId() { return seatId; }
    public double getAmount() { return amount; }


    public boolean isForceFail() {
        return forceFail;
    }

    public void setForceFail(boolean forceFail) {
        this.forceFail = forceFail;
    }
}
