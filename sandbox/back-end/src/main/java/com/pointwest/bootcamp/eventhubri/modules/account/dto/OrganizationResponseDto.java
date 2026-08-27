package com.pointwest.bootcamp.eventhubri.modules.account.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;

public record OrganizationResponseDto(
        Long id,
        String companyName,
        String primaryContactEmail,
        String primaryContactPhone,
        Organization.Status status,
        LocalDateTime createdAt) {
}
