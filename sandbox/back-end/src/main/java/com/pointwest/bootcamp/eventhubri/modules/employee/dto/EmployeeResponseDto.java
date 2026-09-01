package com.pointwest.bootcamp.eventhubri.modules.employee.dto;

import java.util.List;

public record EmployeeResponseDto(
                Long id,
                String firstName,
                String lastName,
                String email,
                String company,
                String avatarUrl,
                String role,
                List<RegisteredEventSummaryDto> registeredEvents) {
}