package com.pointwest.bootcamp.exercises.refactoring.stay;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.pointwest.bootcamp.hotelservices.model.Guest;
import com.pointwest.bootcamp.hotelservices.model.GuestStay;

public class StayRepositoryImpl implements StayRepository {
    private final Map<String, GuestStay> stays = new LinkedHashMap<>();
    private int nextStayNumber = 1;

    @Override
    public String generateNextId() {
        return "STAY-%04d".formatted(nextStayNumber++);
    }

    @Override
    public void save(GuestStay stay) {
        stays.put(stay.getStayId(), stay);
    }

    @Override
    public Optional<GuestStay> findById(String id) {
        return Optional.ofNullable(stays.get(id));
    }
}
