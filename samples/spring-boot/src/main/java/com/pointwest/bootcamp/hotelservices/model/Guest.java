package com.pointwest.bootcamp.hotelservices.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Guest {

    private final String guestId;
    private final String name;
    private final ContactInfo contact;
    private final List<GuestStay> stays = new ArrayList<>();
    private String loyaltyTier;

    public Guest(String guestId, String name) {
        this(guestId, name, null);
    }

    public Guest(String guestId, String name, ContactInfo contact) {
        this.guestId = guestId;
        this.name = name;
        this.contact = contact;
        System.out.println("[Guest] registered " + name + " (" + guestId + ")");
    }

    public void addStay(GuestStay stay) {
        stays.add(stay);
    }

    public void setLoyaltyTier(String tier) {
        this.loyaltyTier = tier;
    }

    public String getGuestId() { return guestId; }
    public String getName() { return name; }
    public Optional<ContactInfo> getContact() { return Optional.ofNullable(contact); }
    public Optional<String> getLoyaltyTier() { return Optional.ofNullable(loyaltyTier); }
    public List<GuestStay> getStays() { return Collections.unmodifiableList(stays); }
}
