package com.pointwest.bootcamp.exercises.refactoring.stay;

import java.util.Optional;

import com.pointwest.bootcamp.hotelservices.model.GuestStay;

public interface StayRepository {
    String generateNextId();

    void save(GuestStay stay);

    Optional<GuestStay> findById(String id);
}
