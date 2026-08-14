package com.pointwest.bootcamp.hotelservices.service.booking;

import org.springframework.stereotype.Component;

import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationRequest;
import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationResult;

@Component
public class QrCodeBookingVerificationStrategy implements BookingVerificationStrategy {
    @Override
    public BookingVerificationResult verifyBooking(BookingVerificationRequest request) {
        String resultStatus = String.format("Request processed via %s on %s for booking ref %s", getVerificationMethod(), request.getRequestDate(), request.getBookingReference());
        // send to qr code service for validation 
        return new BookingVerificationResult(resultStatus);
    }

    @Override
    public String getVerificationMethod() {
        return "QR_CODE";
    }
}
