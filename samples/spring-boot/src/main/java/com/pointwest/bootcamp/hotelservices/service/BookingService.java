package com.pointwest.bootcamp.hotelservices.service;

import org.springframework.stereotype.Service;

import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationRequest;
import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationResult;
import com.pointwest.bootcamp.hotelservices.service.booking.BookingVerificationStrategy;
import com.pointwest.bootcamp.hotelservices.service.booking.BookingVerificationStrategyFactory;

@Service
public class BookingService {

    private BookingVerificationStrategyFactory bookingFactory;

    public BookingService(BookingVerificationStrategyFactory bookingFactory) {
        this.bookingFactory = bookingFactory;
    }

    public BookingVerificationResult verifyBooking(BookingVerificationRequest request) {
       BookingVerificationStrategy strategy = bookingFactory.getStrategy(request.getVerificationMethod());
       return strategy.verifyBooking(request);
    }
}
