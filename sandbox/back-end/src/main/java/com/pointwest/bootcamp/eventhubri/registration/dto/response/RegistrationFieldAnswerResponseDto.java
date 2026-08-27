package com.pointwest.bootcamp.eventhubri.registration.dto.response;

public record RegistrationFieldAnswerResponseDto(
        Long id,
        Long registrationId,
        Long eventCustomFieldId,
        String fieldLabel,
        String answerValue
) {
}
