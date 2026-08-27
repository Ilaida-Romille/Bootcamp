package com.pointwest.bootcamp.eventhubri.identity.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrganizerMemberRequestDto(
        @NotNull Long organizerId,
        @NotNull Long userId,
        @NotNull Long roleId
) {
}
