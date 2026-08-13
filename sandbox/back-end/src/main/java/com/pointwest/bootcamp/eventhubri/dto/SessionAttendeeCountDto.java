package com.pointwest.bootcamp.eventhubri.dto;

public class SessionAttendeeCountDto {
    private String sessionId;
    private String title;
    private long attendeeCount;

    public SessionAttendeeCountDto(String sessionId, String title, long attendeeCount) {
        this.sessionId = sessionId;
        this.title = title;
        this.attendeeCount = attendeeCount;
    }

    public String getSessionId() { return sessionId; }
    public String getTitle() { return title; }
    public long getAttendeeCount() { return attendeeCount; }
}