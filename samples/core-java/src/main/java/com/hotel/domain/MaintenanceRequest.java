package com.hotel.domain;

public class MaintenanceRequest {

    public enum Status { REPORTED, ASSIGNED, IN_PROGRESS, RESOLVED, VERIFIED }

    private final String requestId;
    private final Space location;
    private final String issueType;
    private final MaintenanceIssue issue;
    private Status status;
    private Staff assignee;

    public MaintenanceRequest(String requestId, Space location, MaintenanceIssue issue) {
        this.requestId = requestId;
        this.location = location;
        this.issue = issue;
        this.issueType = switch (issue) {
            case PlumbingIssue p -> "PLUMBING";
            case ElectricalIssue e -> "ELECTRICAL";
            case HvacIssue h -> "HVAC";
            case StructuralIssue s -> "STRUCTURAL";
        };
        this.status = Status.REPORTED;
        location.setStatus(Space.Status.UNDER_MAINTENANCE);
        System.out.println("[MaintenanceRequest] " + requestId + " reported: " + issueType
                + " at " + location.getName());
    }

    public void assignTo(Staff staff) {
        this.assignee = staff;
        this.status = Status.ASSIGNED;
        staff.assignRequest(this);
        System.out.println("[MaintenanceRequest] " + requestId + " assigned to " + staff.getName());
    }

    public void resolve() {
        this.status = Status.RESOLVED;
        location.setStatus(Space.Status.OPERATIONAL);
        System.out.println("[MaintenanceRequest] " + requestId + " resolved at " + location.getName());
    }

    public String getRequestId() { return requestId; }
    public Space getLocation() { return location; }
    public String getIssueType() { return issueType; }
    public MaintenanceIssue getIssue() { return issue; }
    public Status getStatus() { return status; }
    public Staff getAssignee() { return assignee; }
}
