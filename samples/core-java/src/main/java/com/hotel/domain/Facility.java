package com.hotel.domain;

public class Facility extends Space {

    private final String category; // Pool, Gym, Spa, Breakfast Hall, ...
    private final int capacity;

    public Facility(String facilityId, String name, String category, int capacity) {
        super(facilityId, name);
        this.category = category;
        this.capacity = capacity;
        System.out.println("[Facility] " + name + " (" + category + ") capacity=" + capacity);
    }

    public String getCategory() { return category; }
    public int getCapacity() { return capacity; }
}
