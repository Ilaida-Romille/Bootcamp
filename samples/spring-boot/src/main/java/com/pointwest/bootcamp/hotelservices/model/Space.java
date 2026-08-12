package com.pointwest.bootcamp.hotelservices.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;

@Entity
@Inheritance
public abstract class Space {

	public enum Status {
		OPERATIONAL, UNDER_MAINTENANCE, CLOSED
	}

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long spaceId;
	private String name;
	
	@Enumerated
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
