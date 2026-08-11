package com.pointwest.bootcamp.hotelservices.model;

public abstract class Space {

	public enum Status {
		OPERATIONAL, UNDER_MAINTENANCE, CLOSED
	}

	private Long spaceId;
	private String name;
	private Status status;

	protected Space() {
	}

	protected Space(String name, Status status) {
		this.name = name;
		this.status = status;
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
}
