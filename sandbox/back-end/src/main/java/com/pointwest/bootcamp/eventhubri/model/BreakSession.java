package com.pointwest.bootcamp.eventhubri.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "break_sessions")
public class BreakSession extends Session {
    private String breakType;

    public BreakSession() {
    }

    public BreakSession(String sessionId, String title, String description, Date startDateTime, Date endDateTime,
            String location, String breakType) {
        super(sessionId, title, description, startDateTime, endDateTime, location);
        this.breakType = breakType;
    }

    public String getBreakType() {
        return breakType;
    }

    public void setBreakType(String breakType) {
        this.breakType = breakType;
    }
}
