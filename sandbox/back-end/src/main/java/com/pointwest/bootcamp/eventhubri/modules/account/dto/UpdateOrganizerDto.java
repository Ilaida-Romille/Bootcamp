package com.pointwest.bootcamp.eventhubri.modules.account.dto;

import jakarta.validation.constraints.Size;

public record UpdateOrganizerDto(
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Size(max = 255) String company,
        @Size(max = 255) String organizationName) {
}
