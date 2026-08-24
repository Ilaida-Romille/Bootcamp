package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.Attendee;

public class AttendeeDto {

    private String userId;
    private String name;
    private String email;
    private String attendeeId;

    public AttendeeDto() {
    }

    public AttendeeDto(String userId, String name, String email, String attendeeId) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.attendeeId = attendeeId;
    }

    // Convenient constructor to map directly from Attendee entity
    public AttendeeDto(Attendee attendee) {
        if (attendee != null) {
            this.userId = attendee.getUserId();
            this.name = attendee.getName();
            this.email = attendee.getEmail();
            this.attendeeId = attendee.getAttendeeId();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }
}