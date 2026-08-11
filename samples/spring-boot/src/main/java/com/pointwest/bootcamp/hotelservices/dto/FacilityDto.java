package com.pointwest.bootcamp.hotelservices.dto;

import com.pointwest.bootcamp.hotelservices.model.Facility;

public class FacilityDto {

    private String category; // Pool, Gym, Spa, Breakfast Hall, ...
    private int capacity;
    private Long spaceId;
	private String name;
	private Status status;

    public FacilityDto() {

    }

    public FacilityDto(Facility facility) {
        this.category = facility.getCategory();
        this.capacity = facility.getCapacity();
        this.spaceId = facility.getSpaceId();
        this.name = facility.getName();
        this.status = Status.valueOf(facility.getStatus().name());
    }

    public int getCapacity() {
        return capacity;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public Status getStatus() {
        return status;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
