package com.pointwest.bootcamp.eventhubri.model;

import java.util.Date;

public class Event {
    private String eventId;
    private String organizerId;
    private String title;
    private String description;
    private EventStatus status;
    private Date startDateTime;
    private Date endDateTime;
    private Date registrationOpensAt;
    private Date registrationClosesAt;
    private String venue;
    private Integer capacity;
    private Boolean isFoodProvided;
    private Agenda agenda;

    public Event() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOrganizerId(){
        return organizerId;
    }

    public void setOrganizerId(String organizerId){
        this.organizerId = organizerId;
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

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Date getRegistrationOpensAt() {
        return registrationOpensAt;
    }

    public void setRegistrationOpensAt(Date registrationOpensAt) {
        this.registrationOpensAt = registrationOpensAt;
    }

    public Date getRegistrationClosesAt() {
        return registrationClosesAt;
    }

    public void setRegistrationClosesAt(Date registrationClosesAt) {
        this.registrationClosesAt = registrationClosesAt;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getIsFoodProvided() {
        return isFoodProvided;
    }

    public void setIsFoodProvided(Boolean isFoodProvided) {
        this.isFoodProvided = isFoodProvided;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }
}
