package com.pointwest.bootcamp.eventhubri.modules.networking.dto;

import jakarta.validation.constraints.Size;

// userId is intentionally absent — resolved server-side from the authenticated
// principal so one user can never overwrite another user's card.
public record DigitalBusinessCardRequestDto(

        String userProfilePictureUrl,

        @Size(max = 255) String headline,

        String bio,

        String linkedinProfileUrl,

        @Size(max = 30) String phoneNumber,

        @Size(max = 255) String companyName,

        @Size(max = 255) String jobTitle) {
}
