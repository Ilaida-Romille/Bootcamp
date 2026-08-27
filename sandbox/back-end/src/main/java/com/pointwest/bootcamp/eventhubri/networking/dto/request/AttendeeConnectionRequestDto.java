package com.pointwest.bootcamp.eventhubri.networking.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record AttendeeConnectionRequestDto(
        @NotNull Long eventId,
        @NotNull Long recipientUserId
) {
    // Cross-field business rule expressed right on the DTO -- fails fast
    // before ever reaching the service layer. requesterUserId is intentionally
    // NOT part of this record; it's derived from the authenticated principal,
    // never trusted from client input.
    @AssertTrue(message = "recipientUserId is required")
    public boolean isRecipientProvided() {
        return recipientUserId != null;
    }
}
