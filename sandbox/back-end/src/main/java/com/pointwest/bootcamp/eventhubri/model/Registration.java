package com.pointwest.bootcamp.eventhubri.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Registration {
    private String registrationId;
    private String attendeeId;
    private String eventId;
    private Date registeredAt;
    private RegistrationStatus status;
    private String dietaryRestrictions;
    private List<SessionSelection> sessionSelections = new ArrayList<>();

    public Registration() {
    }

    public void cancel() {
        this.status = RegistrationStatus.CANCELLED;
    }

    public void updateDietaryInfo(String info) {
        this.dietaryRestrictions = info;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public List<SessionSelection> getSessionSelections() {
        return sessionSelections;
    }

    public void setSessionSelections(List<SessionSelection> sessionSelections) {
        this.sessionSelections = sessionSelections;
    }
}
