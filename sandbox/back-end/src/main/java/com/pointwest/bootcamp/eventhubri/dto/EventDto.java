package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.Event;
import java.util.Date;

public class EventDto {
    private Long eventId;
    private String title;
    private String description;
    private String status;
    private Date startDateTime;
    private Date endDateTime;
    private String venue;
    private Integer capacity;

    public EventDto() {
    }

    public EventDto(Event event) {
        if (event != null) {
            this.eventId = event.getEventId();
            this.title = event.getTitle();
            this.description = event.getDescription();
            this.status = event.getStatus() != null ? event.getStatus().name() : null;
            this.startDateTime = event.getStartDateTime();
            this.endDateTime = event.getEndDateTime();
            this.venue = event.getVenue();
            this.capacity = event.getCapacity();
        }
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
}
