package com.pointwest.bootcamp.eventhubri.modules.account.dto;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;

public record OrganizerResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String company,
        AppUser.Status userStatus,
        Long organizationId,
        String organizationName,
        String primaryContactEmail,
        String primaryContactPhone,
        Organization.Status organizationStatus,
        long totalEvents) {
}
