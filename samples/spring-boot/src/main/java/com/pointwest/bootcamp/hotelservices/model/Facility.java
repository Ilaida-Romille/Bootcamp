package com.pointwest.bootcamp.hotelservices.model;

import jakarta.persistence.Entity;

@Entity
public class Facility extends Space {

    private final String category; // Pool, Gym, Spa, Breakfast Hall, ...
    private final int capacity;

    public Facility(String category, int capacity) {
        this.category = category;
        this.capacity = capacity;
    }

    public Facility(String facilityId, String name, String category, int capacity, Space.Status status) {
        super(name, status);
        this.category = category;
        this.capacity = capacity;
        System.out.println("[Facility] " + name + " (" + category + ") capacity=" + capacity);
    }

    public String getCategory() { return category; }
    public int getCapacity() { return capacity; }

    
}
