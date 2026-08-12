package com.hotel.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Staff {

    private final String staffId;
    private final String name;
    private final String role;
    private final Department department;
    private final Set<Certification> certifications = new HashSet<>();
    private final List<CleaningTask> assignedTasks = new ArrayList<>();
    private final List<MaintenanceRequest> assignedRequests = new ArrayList<>();
    private Staff supervisor;

    public Staff(String staffId, String name, String role, Department department) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.department = department;
        department.addMember(this);
        System.out.println("[Staff] " + name + " (" + role + ") joined " + department.getName());
    }

    public void setSupervisor(Staff supervisor) {
        this.supervisor = supervisor;
    }

    public void addCertification(Certification certification) {
        certifications.add(certification);
    }

    public boolean isCertifiedFor(Certification certification) {
        return certifications.contains(certification);
    }

    /**
     * Throws if this staff member isn't certified for the given certification.
     */
    public void requireCertifiedFor(Certification certification) {
        if (!isCertifiedFor(certification)) {
            throw new StaffNotCertifiedException(
                    getName() + " is not certified for " + certification);
        }
    }

    /**
     * Verifies this staff member's certification records are complete (i.e.
     * they hold at least one certification on file). Declares a checked
     * exception for the same reason as {@link Inspection#generateReport()}.
     */
    public void verifyCertificationRecords() throws CertificationVerificationException {
        if (getCertifications().isEmpty()) {
            throw new CertificationVerificationException(getName() + " has no certification records on file");
        }
    }

    void assignTask(CleaningTask task) { assignedTasks.add(task); }
    void assignRequest(MaintenanceRequest request) { assignedRequests.add(request); }

    public String getStaffId() { return staffId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public Department getDepartment() { return department; }
    public Set<Certification> getCertifications() { return Collections.unmodifiableSet(certifications); }
    public List<CleaningTask> getAssignedTasks() { return Collections.unmodifiableList(assignedTasks); }
    public List<MaintenanceRequest> getAssignedRequests() { return Collections.unmodifiableList(assignedRequests); }
    public Optional<Staff> getSupervisor() { return Optional.ofNullable(supervisor); }
}
