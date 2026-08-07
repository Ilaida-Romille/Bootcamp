package com.hotel.domain;

public record StructuralIssue(String area, String severity) implements MaintenanceIssue {
}
