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

    /**
     * Builds a plain-text summary of this inspection. Declares a checked
     * exception to model a boundary that would realistically be fallible in
     * a real system (e.g. writing to a file, calling a template service) —
     * implemented here without real I/O so it stays deterministic, but callers
     * must still handle it as a checked exception.
     */
    public String generateReport() throws InspectionReportException {
        if (getResult() == null) {
            throw new InspectionReportException("Cannot generate a report for an inspection with no result recorded");
        }
        return "Inspection Report\n"
                + "==================\n"
                + inspectionId + " | " + location.getName() + " | " + inspectionType
                + " | " + inspector.getName() + " | " + result;
    }
}
