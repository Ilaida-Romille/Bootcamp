package com.pointwest.bootcamp.hotelservices.dto;

import com.pointwest.bootcamp.hotelservices.model.Room;

public class RoomDto {

	public enum HousekeepingStatus {
		VACANT_CLEAN, VACANT_DIRTY, OCCUPIED_CLEAN, OCCUPIED_DIRTY, OUT_OF_ORDER, OUT_OF_SERVICE
	}

	private Long spaceId;
	private String name;
	private Status status;
	private String roomNumber;
	private int floor;
	private String roomType;
	private int maxOccupancy;
	private HousekeepingStatus housekeepingStatus;

	public RoomDto() {
	}

	public RoomDto(Long spaceId, String name, Status status, String roomNumber, int floor, String roomType,
			int maxOccupancy, HousekeepingStatus housekeepingStatus) {
		this.spaceId = spaceId;
		this.name = name;
		this.status = status;
		this.roomNumber = roomNumber;
		this.floor = floor;
		this.roomType = roomType;
		this.maxOccupancy = maxOccupancy;
		this.housekeepingStatus = housekeepingStatus;
	}

	public RoomDto(Room room) {
		this.spaceId = room.getSpaceId();
		this.name = room.getName();
		this.status = Status.valueOf(room.getStatus().name());
		this.roomNumber = room.getRoomNumber();
		this.floor = room.getFloor();
		this.roomType = room.getRoomType();
		this.maxOccupancy = room.getMaxOccupancy();
		this.housekeepingStatus = HousekeepingStatus.valueOf(room.getHousekeepingStatus().name());
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public HousekeepingStatus getHousekeepingStatus() {
		return housekeepingStatus;
	}

	public void setHousekeepingStatus(HousekeepingStatus housekeepingStatus) {
		this.housekeepingStatus = housekeepingStatus;
	}
}
