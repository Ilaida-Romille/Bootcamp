package com.pointwest.bootcamp.eventhubri.dto;

import java.util.List;

public class SessionPopularitySummaryDto {
    private Long eventId;
    private List<SessionAttendeeCountDto> sessionDetails;
    
    // Aggregates required for Group 2
    private long count;     // Total number of sessions evaluated
    private long sum;       // Total session registrations across all sessions
    private double average; // Average attendees per session
    private long min;       // Min attendees in a session
    private long max;       // Max attendees in a session

    public SessionPopularitySummaryDto(Long eventId, List<SessionAttendeeCountDto> sessionDetails, 
                                       long count, long sum, double average, long min, long max) {
        this.eventId = eventId;
        this.sessionDetails = sessionDetails;
        this.count = count;
        this.sum = sum;
        this.average = average;
        this.min = min;
        this.max = max;
    }

    // Getters
    public Long getEventId() { return eventId; }
    public List<SessionAttendeeCountDto> getSessionDetails() { return sessionDetails; }
    public long getCount() { return count; }
    public long getSum() { return sum; }
    public double getAverage() { return average; }
    public long getMin() { return min; }
    public long getMax() { return max; }
}