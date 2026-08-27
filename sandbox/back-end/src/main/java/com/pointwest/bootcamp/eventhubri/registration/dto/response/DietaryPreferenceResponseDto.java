package com.pointwest.bootcamp.eventhubri.registration.dto.response;

import com.pointwest.bootcamp.eventhubri.registration.enums.DietaryRestriction;

public record DietaryPreferenceResponseDto(
        Long id,
        Long registrationId,
        DietaryRestriction dietaryRestriction,
        String allergyNotes
) {
}
