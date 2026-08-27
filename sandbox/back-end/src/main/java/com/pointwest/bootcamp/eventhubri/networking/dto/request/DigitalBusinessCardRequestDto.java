package com.pointwest.bootcamp.eventhubri.networking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DigitalBusinessCardRequestDto(
        @NotBlank @Size(max = 255) String displayName,
        @Size(max = 255) String jobTitle,
        @Size(max = 255) String company,
        @Size(max = 30) String phoneNumber,
        @Email @Size(max = 255) String email,
        @Size(max = 255) String linkedinUrl,
        boolean isDirectoryOptIn
) {
}
