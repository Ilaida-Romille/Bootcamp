package com.pointwest.bootcamp.eventhubri.registration.dto.request;

import com.pointwest.bootcamp.eventhubri.registration.enums.DietaryRestriction;

import jakarta.validation.constraints.NotNull;

public record DietaryPreferenceRequestDto(
        @NotNull Long registrationId,
        @NotNull DietaryRestriction dietaryRestriction,
        String allergyNotes
) {
}
