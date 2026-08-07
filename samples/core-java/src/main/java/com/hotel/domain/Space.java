package com.hotel.domain;

/** Anything that can need cleaning, maintenance, or inspection: rooms and public facilities. */
public abstract class Space {

    public enum Status { OPERATIONAL, UNDER_MAINTENANCE, CLOSED }

    protected final String spaceId;
    protected final String name;
    protected Status status;

    protected Space(String spaceId, String name) {
        this.spaceId = spaceId;
        this.name = name;
        this.status = Status.OPERATIONAL;
        System.out.println("[Space] created " + name + " (" + spaceId + ")");
    }

    public void setStatus(Status newStatus) {
        System.out.println("[Space] " + name + " status: " + status + " -> " + newStatus);
        this.status = newStatus;
    }

    public String getSpaceId() { return spaceId; }
    public String getName() { return name; }
    public Status getStatus() { return status; }
}
