package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.EventStatus;
import java.util.Date;

public class ImportEventDto {
    private String title;
    private String description;
    private String status;
    private String startDateTime;
    private String endDateTime;
    private String registrationOpensAt;
    private String registrationClosesAt;
    private String venue;
    private String capacity;
    private String isFoodProvided;

    public ImportEventDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getRegistrationOpensAt() {
        return registrationOpensAt;
    }

    public void setRegistrationOpensAt(String registrationOpensAt) {
        this.registrationOpensAt = registrationOpensAt;
    }

    public String getRegistrationClosesAt() {
        return registrationClosesAt;
    }

    public void setRegistrationClosesAt(String registrationClosesAt) {
        this.registrationClosesAt = registrationClosesAt;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getIsFoodProvided() {
        return isFoodProvided;
    }

    public void setIsFoodProvided(String isFoodProvided) {
        this.isFoodProvided = isFoodProvided;
    }
}
