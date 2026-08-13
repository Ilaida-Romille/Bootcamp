package com.pointwest.bootcamp.hotelservices.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class GuestStay {

    @Id
    private String stayId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;
    private int partySize;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean active;

    public GuestStay() {

    }

    public GuestStay(String stayId, Room room, int partySize, LocalDate checkInDate) {
        this.stayId = stayId;
        this.room = room;
        this.partySize = partySize;
        this.checkInDate = checkInDate;
        this.active = true;
        room.setHousekeepingStatus(Room.HousekeepingStatus.OCCUPIED_DIRTY);
    }

    public void checkOut() {
        checkOut(LocalDate.now());
    }

    public void checkOut(LocalDate date) {
        this.active = false;
        this.checkOutDate = date;
        room.setHousekeepingStatus(Room.HousekeepingStatus.VACANT_DIRTY);
    }

    /** Nights between check-in and check-out; an in-house stay counts up to today. */
    public long nightsStayed() {
        LocalDate end = getCheckOutDate().orElse(LocalDate.now());
        return ChronoUnit.DAYS.between(checkInDate, end);
    }

    public Guest getGuest() {
        return null;
    }

    public StayDuration stayDuration() {
        return new StayDuration(nightsStayed());
    }

    public String getStayId() { return stayId; }
    public Room getRoom() { return room; }
    public int getPartySize() { return partySize; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public Optional<LocalDate> getCheckOutDate() { return Optional.ofNullable(checkOutDate); }
    public boolean isActive() { return active; }
}
