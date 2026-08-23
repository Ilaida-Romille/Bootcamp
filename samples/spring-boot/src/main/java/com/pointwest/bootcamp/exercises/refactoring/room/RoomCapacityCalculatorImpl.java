package com.pointwest.bootcamp.exercises.refactoring.room;

import java.util.Locale;

public class RoomCapacityCalculatorImpl implements RoomCapacityCalculator {
    @Override
    public int getCapacity(String roomType) {
        return switch (roomType.toLowerCase(Locale.ROOT)) {
            case "standard" -> 2;
            case "deluxe" -> 4;
            case "suite" -> 6;
            case "penthouse" -> 8;
            default -> throw new IllegalArgumentException("unsupported room type: " + roomType);
        };
    }
}
