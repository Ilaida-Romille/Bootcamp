package com.pointwest.bootcamp.hotelservices.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A scheduled service window (e.g. breakfast) hosted at a {@link Facility}. */
public class MealSession {

    private final String sessionId;
    private final Facility venue;
    private final String mealType;
    private final List<MealAttendance> attendances = new ArrayList<>();

    public MealSession(String sessionId, Facility venue, String mealType) {
        this.sessionId = sessionId;
        this.venue = venue;
        this.mealType = mealType;
        System.out.println("[MealSession] " + mealType + " session " + sessionId + " opened at " + venue.getName());
    }

    void addAttendance(MealAttendance attendance) { attendances.add(attendance); }

    public String getSessionId() { return sessionId; }
    public Facility getVenue() { return venue; }
    public String getMealType() { return mealType; }
    public List<MealAttendance> getAttendances() { return Collections.unmodifiableList(attendances); }
}
