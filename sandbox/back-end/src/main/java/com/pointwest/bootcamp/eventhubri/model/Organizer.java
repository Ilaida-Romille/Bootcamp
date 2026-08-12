package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Entity
@Table(name = "organizers")
public class Organizer extends User {
    private String organizerId;
    private String companyName;

    public Organizer() {}

    public Organizer(String userId, String name, String email, String organizerId, String companyName) {
        super(userId, name, email);
        this.organizerId = organizerId;
        this.companyName = companyName;
    }

    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}