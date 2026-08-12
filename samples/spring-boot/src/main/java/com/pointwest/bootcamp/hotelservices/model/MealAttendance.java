package com.pointwest.bootcamp.hotelservices.model;

import java.time.LocalDateTime;

/** Record of a {@link GuestStay} attending a {@link MealSession} (who showed up, party size). */
public class MealAttendance {

    private final MealSession session;
    private final GuestStay stay;
    private final int headcount;
    private final LocalDateTime timestamp;

    public MealAttendance(MealSession session, GuestStay stay, int headcount) {
        this(session, stay, headcount, LocalDateTime.now());
    }

    public MealAttendance(MealSession session, GuestStay stay, int headcount, LocalDateTime timestamp) {
        this.session = session;
        this.stay = stay;
        this.headcount = headcount;
        this.timestamp = timestamp;
        stay.addMealAttendance(this);
        session.addAttendance(this);
        System.out.println("[MealAttendance] " + stay.getGuest().getName() + " (party " + headcount
                + ") attended " + session.getSessionId() + " at " + session.getVenue().getName());
    }

    public MealSession getSession() { return session; }
    public GuestStay getStay() { return stay; }
    public int getHeadcount() { return headcount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
