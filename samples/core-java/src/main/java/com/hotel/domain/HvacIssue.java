package com.hotel.domain;

public record HvacIssue(String unit, int targetTempCelsius) implements MaintenanceIssue {
}
