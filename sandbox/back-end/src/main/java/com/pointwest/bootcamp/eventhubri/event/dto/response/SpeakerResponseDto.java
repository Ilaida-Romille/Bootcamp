package com.pointwest.bootcamp.eventhubri.event.dto.response;

public record SpeakerResponseDto(
        Long id,
        String fullName,
        String title,
        String company,
        String bio,
        String photoUrl,
        String email
) {
}
