package com.pointwest.bootcamp.hotelservices.model;

import java.util.ArrayList;
import java.util.List;

import com.pointwest.bootcamp.hotelservices.exception.RoomUnavailableException;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;

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

	@OneToMany(mappedBy = "room")
    private List<GuestStay> stays;

	protected Room() {
        this.stays = new ArrayList<>();
	}

	public Room(String roomNumber, int floor, String roomType, int maxOccupancy,
			HousekeepingStatus housekeepingStatus) {
		super("Room " + roomNumber, Status.OPERATIONAL);
        this.stays = new ArrayList<>();
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

    public List<GuestStay> getStays() {
        return stays;
    }

    public void setStays(List<GuestStay> stays) {
        this.stays = stays;
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
