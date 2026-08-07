package com.hotel.domain;

public class Inspection {

    public enum Result { PASS, FAIL, NEEDS_ACTION }

    private final String inspectionId;
    private final Space location;
    private final String inspectionType;
    private final Staff inspector;
    private Result result;

    public Inspection(String inspectionId, Space location, String inspectionType, Staff inspector) {
        this.inspectionId = inspectionId;
        this.location = location;
        this.inspectionType = inspectionType;
        this.inspector = inspector;
        System.out.println("[Inspection] " + inspectionType + " started at " + location.getName()
                + " by " + inspector.getName());
    }

    public void recordResult(Result result) {
        this.result = result;
        System.out.println("[Inspection] " + inspectionId + " result: " + result + " (" + location.getName() + ")");
    }

    public String getInspectionId() { return inspectionId; }
    public Space getLocation() { return location; }
    public String getInspectionType() { return inspectionType; }
    public Staff getInspector() { return inspector; }
    public Result getResult() { return result; }
}
