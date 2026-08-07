package com.hotel.domain;

import java.math.BigDecimal;
import java.util.Set;

public class Room extends Space {

    public enum HousekeepingStatus {
        VACANT_CLEAN, VACANT_DIRTY, OCCUPIED_CLEAN, OCCUPIED_DIRTY, OUT_OF_ORDER, OUT_OF_SERVICE
    }

    private final int floor;
    private final String roomType;
    private final Set<String> tags;
    private final BigDecimal nightlyRate;
    private HousekeepingStatus housekeepingStatus;

    public Room(String roomNumber, int floor, String roomType) {
        this(roomNumber, floor, roomType, Set.of(), BigDecimal.ZERO);
    }

    public Room(String roomNumber, int floor, String roomType, Set<String> tags, BigDecimal nightlyRate) {
        super(roomNumber, "Room " + roomNumber);
        this.floor = floor;
        this.roomType = roomType;
        this.tags = Set.copyOf(tags);
        this.nightlyRate = nightlyRate;
        this.housekeepingStatus = HousekeepingStatus.VACANT_CLEAN;
        System.out.println("[Room] " + roomNumber + " (floor " + floor + ", " + roomType
                + ") ready as " + housekeepingStatus);
    }

    /** A room is available when it is operationally open and housekeeping has released it. */
    public boolean isAvailable() {
        return getStatus() == Status.OPERATIONAL
                && housekeepingStatus == HousekeepingStatus.VACANT_CLEAN;
    }

    public void setHousekeepingStatus(HousekeepingStatus newStatus) {
        System.out.println("[Room] " + name + " housekeeping: " + housekeepingStatus + " -> " + newStatus);
        this.housekeepingStatus = newStatus;
    }

    public HousekeepingStatus getHousekeepingStatus() { return housekeepingStatus; }
    public int getFloor() { return floor; }
    public String getRoomType() { return roomType; }
    public Set<String> getTags() { return tags; }
    public BigDecimal getNightlyRate() { return nightlyRate; }
}
