package com.hotel.domain;

public record ElectricalIssue(String component, boolean sparking) implements MaintenanceIssue {
}
