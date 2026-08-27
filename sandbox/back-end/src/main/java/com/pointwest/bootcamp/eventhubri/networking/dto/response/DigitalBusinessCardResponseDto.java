package com.pointwest.bootcamp.eventhubri.networking.dto.response;

public record DigitalBusinessCardResponseDto(
        Long id,
        Long userId,
        String displayName,
        String jobTitle,
        String company,
        String email,
        String linkedinUrl,
        boolean isDirectoryOptIn
) {
}
