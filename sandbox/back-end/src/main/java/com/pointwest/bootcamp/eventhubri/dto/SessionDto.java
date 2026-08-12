package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.Session;
import java.util.Date;

public class SessionDto {
    private String sessionId;
    private String title;
    private String description;
    private Date startDateTime;
    private Date endDateTime;
    private String location;

    public SessionDto() {
    }

    public SessionDto(Session session) {
        if (session != null) {
            this.sessionId = session.getSessionId();
            this.title = session.getTitle();
            this.description = session.getDescription();
            this.startDateTime = session.getStartDateTime();
            this.endDateTime = session.getEndDateTime();
            this.location = session.getLocation();
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}