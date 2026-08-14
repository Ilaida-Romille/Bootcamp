package com.pointwest.bootcamp.hotelservices;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationRequest;
import com.pointwest.bootcamp.hotelservices.dto.booking.BookingVerificationResult;
import com.pointwest.bootcamp.hotelservices.service.BookingService;

@SpringBootTest
public class BookingVerificationStrategyFactoryTest {

    @Autowired
    private BookingService service;

    @Test
    void shouldReturnEmailStrategy() {
        BookingVerificationRequest request = new BookingVerificationRequest(LocalDate.now(), "BKG123", "EMAIL");

        BookingVerificationResult result = service.verifyBooking(request);
        System.out.println(result.getStatus());
    }

    @Test
    void shouldReturnQrCodeStrategy() {
        BookingVerificationRequest request = new BookingVerificationRequest(LocalDate.now(), "BKG123", "QR_CODE");

        BookingVerificationResult result = service.verifyBooking(request);
        System.out.println(result.getStatus());
    }
}
