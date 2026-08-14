package com.pointwest.bootcamp.hotelservices.dto.booking;

import java.time.LocalDate;

public class BookingVerificationRequest {

    private LocalDate requestDate;
    private String bookingReference;
    private String verificationMethod;

    public BookingVerificationRequest() {

    }

    public BookingVerificationRequest(LocalDate requestDate, String bookingReference, String verificationMethod) {
        this.requestDate = requestDate;
        this.bookingReference = bookingReference;
        this.verificationMethod = verificationMethod;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public void setVerificationMethod(String verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

}
