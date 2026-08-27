package com.pointwest.bootcamp.eventhubri.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrganizerRequestDto(
        @NotBlank @Size(max = 255) String companyName,
        @Size(max = 255) String legalEntityName,
        @NotBlank @Email @Size(max = 255) String billingEmail,
        @Size(max = 255) String addressLine1,
        @Size(max = 255) String addressLine2,
        @Size(max = 100) String city,
        @Size(max = 100) String stateProvince,
        @Size(max = 20) String postalCode,
        @Size(max = 100) String country
) {
}
