package com.hotel.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Thin boundary object: "who is in-house right now." Not part of the reservation system. */
public class GuestStay {

    private final String stayId;
    private final Guest guest;
    private final Room room;
    private final int partySize;
    private final LocalDate checkInDate;
    private LocalDate checkOutDate;
    private final List<AmenityUsage> amenityUsages = new ArrayList<>();
    private final List<MealAttendance> mealAttendances = new ArrayList<>();
    private boolean active;

    public GuestStay(String stayId, Guest guest, Room room, int partySize, LocalDate checkInDate) {
        this.stayId = stayId;
        this.guest = guest;
        this.room = room;
        this.partySize = partySize;
        this.checkInDate = checkInDate;
        this.active = true;
        guest.addStay(this);
        room.setHousekeepingStatus(Room.HousekeepingStatus.OCCUPIED_DIRTY);
        System.out.println("[GuestStay] " + guest.getName() + " checked into " + room.getName()
                + " (party of " + partySize + ")");
    }

    public void checkOut() {
        checkOut(LocalDate.now());
    }

    public void checkOut(LocalDate date) {
        this.active = false;
        this.checkOutDate = date;
        room.setHousekeepingStatus(Room.HousekeepingStatus.VACANT_DIRTY);
        System.out.println("[GuestStay] " + guest.getName() + " checked out of " + room.getName());
    }

    /** Nights between check-in and check-out; an in-house stay counts up to today. */
    public long nightsStayed() {
        LocalDate end = getCheckOutDate().orElse(LocalDate.now());
        return ChronoUnit.DAYS.between(checkInDate, end);
    }

    public StayDuration stayDuration() {
        return new StayDuration(nightsStayed());
    }

    void addAmenityUsage(AmenityUsage usage) { amenityUsages.add(usage); }
    void addMealAttendance(MealAttendance attendance) { mealAttendances.add(attendance); }

    public String getStayId() { return stayId; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public int getPartySize() { return partySize; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public Optional<LocalDate> getCheckOutDate() { return Optional.ofNullable(checkOutDate); }
    public List<AmenityUsage> getAmenityUsages() { return Collections.unmodifiableList(amenityUsages); }
    public List<MealAttendance> getMealAttendances() { return Collections.unmodifiableList(mealAttendances); }
    public boolean isActive() { return active; }
}
