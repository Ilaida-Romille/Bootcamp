package com.pointwest.bootcamp.eventhubri.modules.profile.dto;

public record UserProfileResponseDto(
        Long id,
        String email,
        String fullName,
        String company,
        String dietary,
        String profileImageUrl,
        String organizationName) {
}
