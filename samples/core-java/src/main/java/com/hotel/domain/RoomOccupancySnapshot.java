package com.hotel.domain;

/** Immutable DTO produced by stream map koans; also a target for record pattern matching. */
public record RoomOccupancySnapshot(String roomNumber, Room.HousekeepingStatus status, int floor) {
}
