package com.hotel.domain;

/** Value object returned by computed stay analysis; compact constructor rejects invalid input. */
public record StayDuration(long nights) {

    public StayDuration {
        if (nights < 0) {
            throw new IllegalArgumentException("nights must not be negative: " + nights);
        }
    }
}
