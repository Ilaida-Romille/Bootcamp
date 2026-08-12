package com.pointwest.bootcamp.hotelservices.model;

import com.pointwest.bootcamp.hotelservices.exception.RoomUnavailableException;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;

@Entity
public class Room extends Space {

	public enum HousekeepingStatus {
		VACANT_CLEAN, VACANT_DIRTY, OCCUPIED_CLEAN, OCCUPIED_DIRTY, OUT_OF_ORDER, OUT_OF_SERVICE
	}

	private String roomNumber;
	private int floor;
	private String roomType;
	private int maxOccupancy;
	
	@Enumerated
	private HousekeepingStatus housekeepingStatus;

	protected Room() {
	}

	public Room(String roomNumber, int floor, String roomType, int maxOccupancy,
			HousekeepingStatus housekeepingStatus) {
		super("Room " + roomNumber, Status.OPERATIONAL);
		this.roomNumber = roomNumber;
		this.floor = floor;
		this.roomType = roomType;
		this.maxOccupancy = maxOccupancy;
		this.housekeepingStatus = housekeepingStatus;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public int getFloor() {
		return floor;
	}

	public String getRoomType() {
		return roomType;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public HousekeepingStatus getHousekeepingStatus() {
		return housekeepingStatus;
	}

	public void setHousekeepingStatus(HousekeepingStatus housekeepingStatus) {
		this.housekeepingStatus = housekeepingStatus;
	}

	    /**
     * Throws if this room isn't currently available for assignment.
     */
    public void requireAvailable() {
        if (!(getStatus() == Status.OPERATIONAL
                && housekeepingStatus == HousekeepingStatus.VACANT_CLEAN)) {
            throw new RoomUnavailableException(
                    "Room " + getSpaceId() + " is not available (status: " + getHousekeepingStatus() + ")");
        }
    }
}
