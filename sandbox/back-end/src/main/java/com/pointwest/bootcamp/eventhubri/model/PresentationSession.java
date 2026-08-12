package com.pointwest.bootcamp.eventhubri.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "presentation_sessions")
public class PresentationSession extends Session {
    private String speaker;

    public PresentationSession() {
    }

    public PresentationSession(String sessionId, String title, String description, Date startDateTime, Date endDateTime,
            String location, String speaker) {
        super(sessionId, title, description, startDateTime, endDateTime, location);
        this.speaker = speaker;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}
