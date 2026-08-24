package com.pointwest.bootcamp.hotelservices.service.booking;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class BookingVerificationStrategyFactory {

    private final Map<String, BookingVerificationStrategy> strategies;

    public BookingVerificationStrategyFactory(List<BookingVerificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(BookingVerificationStrategy::getVerificationMethod, Function.identity()));
    }

    public BookingVerificationStrategy getStrategy(String verificationMethod) {
        BookingVerificationStrategy strategy = strategies.get(verificationMethod);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy for verification method: " + verificationMethod);
        }
        return strategy;
    }
}
