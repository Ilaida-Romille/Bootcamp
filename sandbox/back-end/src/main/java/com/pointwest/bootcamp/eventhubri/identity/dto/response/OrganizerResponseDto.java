package com.pointwest.bootcamp.eventhubri.identity.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.identity.enums.OrganizerStatus;

public record OrganizerResponseDto(
        Long id,
        String companyName,
        String legalEntityName,
        String billingEmail,
        String city,
        String stateProvince,
        String country,
        OrganizerStatus accountStatus,
        Long approvedById,
        LocalDateTime approvedAt
) {
}
