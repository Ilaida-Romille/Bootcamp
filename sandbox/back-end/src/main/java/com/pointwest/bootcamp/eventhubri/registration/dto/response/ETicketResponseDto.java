package com.pointwest.bootcamp.eventhubri.registration.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.registration.enums.TicketStatus;

// No request DTO: e-tickets are generated server-side when a registration is
// confirmed, never submitted by a client -- there is nothing to validate on
// the way in, so an input contract for this resource would be meaningless.
public record ETicketResponseDto(
        Long id,
        Long registrationId,
        String qrCodeHash,
        TicketStatus ticketStatus,
        LocalDateTime issuedAt
) {
}
