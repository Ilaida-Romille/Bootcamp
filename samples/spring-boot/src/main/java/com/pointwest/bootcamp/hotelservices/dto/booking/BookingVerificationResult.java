package com.pointwest.bootcamp.hotelservices.dto.booking;

public class BookingVerificationResult {

    private String status;

    public BookingVerificationResult() {

    }

    public BookingVerificationResult(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
