package com.pointwest.bootcamp.eventhubri.modules.communication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// senderUserId is intentionally absent — resolved server-side from the
// authenticated principal, never trusted from the client.
public record EmailSendRequestDto(

        @NotNull Long eventId,

        // Present -> single EmailNotificationLog to this recipient.
        // Absent -> BroadcastNoticeLog fanned out to all event registrants.
        Long recipientUserId,

        @NotBlank @Size(max = 255) String subject,

        @NotBlank String messageBody) {
}
