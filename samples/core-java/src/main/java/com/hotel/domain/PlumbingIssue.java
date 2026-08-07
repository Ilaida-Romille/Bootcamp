package com.hotel.domain;

public record PlumbingIssue(String fixture, boolean activeLeak) implements MaintenanceIssue {
}
