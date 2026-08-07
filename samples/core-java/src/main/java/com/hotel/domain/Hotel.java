package com.hotel.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Aggregate root: the whole property. Enables koans that traverse floors, facilities,
 * departments, and the append-only stay log. Not part of any reservation system. */
public class Hotel {

    private final String name;
    private final List<Floor> floors = new ArrayList<>();
    private final List<Facility> facilities = new ArrayList<>();
    private final List<Department> departments = new ArrayList<>();
    private final List<GuestStay> stayHistory = new ArrayList<>();

    public Hotel(String name) {
        this.name = name;
        System.out.println("[Hotel] opened " + name);
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }

    public void addFacility(Facility facility) {
        facilities.add(facility);
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }

    public void recordStay(GuestStay stay) {
        stayHistory.add(stay);
    }

    public String getName() { return name; }
    public List<Floor> getFloors() { return Collections.unmodifiableList(floors); }
    public List<Facility> getFacilities() { return Collections.unmodifiableList(facilities); }
    public List<Department> getDepartments() { return Collections.unmodifiableList(departments); }
    public List<GuestStay> getStayHistory() { return Collections.unmodifiableList(stayHistory); }
}
