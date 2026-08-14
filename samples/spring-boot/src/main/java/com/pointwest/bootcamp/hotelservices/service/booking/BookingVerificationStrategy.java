package com.pointwest.bootcamp.hotelservices.service.booking;

import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationRequest;
import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationResult;

public interface BookingVerificationStrategy {

    public BookingVerificationResult verifyBooking(BookingVerificationRequest request);

    public String getVerificationMethod();
}
