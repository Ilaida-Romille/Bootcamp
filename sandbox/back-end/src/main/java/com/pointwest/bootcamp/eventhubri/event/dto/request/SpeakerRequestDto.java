package com.pointwest.bootcamp.eventhubri.event.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SpeakerRequestDto(
        @NotNull Long organizerId,
        @NotBlank @Size(max = 255) String fullName,
        @Size(max = 255) String title,
        @Size(max = 255) String company,
        String bio,
        @Size(max = 255) String photoUrl,
        @Email @Size(max = 255) String email
) {
}
