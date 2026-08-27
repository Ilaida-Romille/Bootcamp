package com.pointwest.bootcamp.eventhubri.event.dto.request;

import com.pointwest.bootcamp.eventhubri.event.enums.SpeakerRole;

import jakarta.validation.constraints.NotNull;

public record SessionSpeakerRequestDto(
        @NotNull Long sessionId,
        @NotNull Long speakerId,
        @NotNull SpeakerRole speakerRole
) {
}
