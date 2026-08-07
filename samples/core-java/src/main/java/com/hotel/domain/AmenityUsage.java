package com.hotel.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/** Record of a {@link GuestStay} using a {@link Facility} (pool, gym, spa). */
public class AmenityUsage {

    private final Facility facility;
    private final GuestStay stay;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public AmenityUsage(Facility facility, GuestStay stay, LocalDateTime entryTime) {
        this.facility = facility;
        this.stay = stay;
        this.entryTime = entryTime;
        stay.addAmenityUsage(this);
        System.out.println("[AmenityUsage] " + stay.getGuest().getName() + " entered " + facility.getName());
    }

    public void exit() {
        exit(LocalDateTime.now());
    }

    public void exit(LocalDateTime time) {
        this.exitTime = time;
        System.out.println("[AmenityUsage] " + stay.getGuest().getName() + " exited " + facility.getName());
    }

    /** Minutes spent inside; 0 while the guest is still inside. */
    public long minutesInside() {
        return exitTime == null ? 0 : Duration.between(entryTime, exitTime).toMinutes();
    }

    public Facility getFacility() { return facility; }
    public GuestStay getStay() { return stay; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public Optional<LocalDateTime> getExitTime() { return Optional.ofNullable(exitTime); }
}
