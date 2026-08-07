package com.hotel.domain;

import java.util.Objects;

/** Value object for guest contact details; compact constructor demonstrates validation. */
public record ContactInfo(String email, String phone) {

    public ContactInfo {
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(phone, "phone must not be null");
    }
}
