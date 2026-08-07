package com.hotel.domain;

public class CleaningTask {

    public enum Status { PENDING, IN_PROGRESS, COMPLETED, INSPECTED }

    private final String taskId;
    private final Space location;
    private final String taskType;
    private Status status;
    private Staff assignee;

    public CleaningTask(String taskId, Space location, String taskType) {
        this.taskId = taskId;
        this.location = location;
        this.taskType = taskType;
        this.status = Status.PENDING;
        System.out.println("[CleaningTask] " + taskId + " (" + taskType + ") queued for " + location.getName());
    }

    public void assignTo(Staff staff) {
        this.assignee = staff;
        this.status = Status.IN_PROGRESS;
        staff.assignTask(this);
        System.out.println("[CleaningTask] " + taskId + " assigned to " + staff.getName() + " -> IN_PROGRESS");
    }

    public void complete() {
        this.status = Status.COMPLETED;
        if (location instanceof Room room) {
            Room.HousekeepingStatus cleaned =
                    room.getHousekeepingStatus() == Room.HousekeepingStatus.OCCUPIED_DIRTY
                            ? Room.HousekeepingStatus.OCCUPIED_CLEAN
                            : Room.HousekeepingStatus.VACANT_CLEAN;
            room.setHousekeepingStatus(cleaned);
        }
        System.out.println("[CleaningTask] " + taskId + " completed at " + location.getName());
    }

    public String getTaskId() { return taskId; }
    public Space getLocation() { return location; }
    public String getTaskType() { return taskType; }
    public Status getStatus() { return status; }
    public Staff getAssignee() { return assignee; }
}
