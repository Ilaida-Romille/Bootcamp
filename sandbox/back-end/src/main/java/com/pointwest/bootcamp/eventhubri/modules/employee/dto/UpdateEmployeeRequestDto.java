package com.pointwest.bootcamp.eventhubri.modules.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateEmployeeRequestDto(
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Email @Size(max = 255) String email,
        @Size(max = 255) String company,
        @Size(max = 100) String department,
        @Size(max = 100) String jobTitle,
        @Size(max = 512) String avatarUrl) {
}