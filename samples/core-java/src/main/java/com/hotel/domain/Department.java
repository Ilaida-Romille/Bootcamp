package com.hotel.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department {

    private final String name;
    private final List<Staff> members = new ArrayList<>();

    public Department(String name) {
        this.name = name;
        System.out.println("[Department] established: " + name);
    }

    void addMember(Staff staff) { members.add(staff); }

    public String getName() { return name; }
    public List<Staff> getMembers() { return Collections.unmodifiableList(members); }
}
