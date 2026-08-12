package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.Registration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationDto {
    private String registrationId;
    private String attendeeId;
    private String eventId;
    private Date registeredAt;
    private RegistrationStatusDto status;
    private String dietaryRestrictions;
    private List<SessionSelectionDto> sessionSelections = new ArrayList<>();

    public RegistrationDto() {
    }

    public RegistrationDto(Registration registration) {
        if (registration != null) {
            this.registrationId = registration.getRegistrationId();
            this.attendeeId = registration.getAttendeeId();
            this.eventId = registration.getEventId();
            this.registeredAt = registration.getRegisteredAt();

            if (registration.getStatus() != null) {
                this.status = RegistrationStatusDto.valueOf(registration.getStatus().name());
            }

            this.dietaryRestrictions = registration.getDietaryRestrictions();

            if (registration.getSessionSelections() != null) {
                this.sessionSelections = registration.getSessionSelections().stream()
                        .map(SessionSelectionDto::new)
                        .collect(Collectors.toList());
            }
        }
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

    public RegistrationStatusDto getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatusDto status) {
        this.status = status;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public List<SessionSelectionDto> getSessionSelections() {
        return sessionSelections;
    }

    public void setSessionSelections(List<SessionSelectionDto> sessionSelections) {
        this.sessionSelections = sessionSelections;
    }
}