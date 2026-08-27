package com.pointwest.bootcamp.eventhubri.modules.networking.dto;

import java.time.LocalDateTime;

public record DigitalBusinessCardResponseDto(
        Long id,
        Long userId,
        String ownerFullName,
        String userProfilePictureUrl,
        String headline,
        String bio,
        String linkedinProfileUrl,
        String phoneNumber,
        String companyName,
        String jobTitle,
        LocalDateTime updatedAt) {
}
