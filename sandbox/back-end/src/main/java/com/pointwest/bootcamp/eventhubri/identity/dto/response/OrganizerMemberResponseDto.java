package com.pointwest.bootcamp.eventhubri.identity.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.identity.enums.MembershipStatus;

public record OrganizerMemberResponseDto(
        Long id,
        Long organizerId,
        Long userId,
        String userFullName,
        Long roleId,
        MembershipStatus membershipStatus,
        LocalDateTime invitedAt,
        LocalDateTime joinedAt
) {
}
