package com.hotel.domain;

/** Thrown when a staff member lacks a certification a task requires. */
public class StaffNotCertifiedException extends HotelException {

    public StaffNotCertifiedException(String message) {
        super(message);
    }

    public StaffNotCertifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
