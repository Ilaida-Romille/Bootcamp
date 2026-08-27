package com.pointwest.bootcamp.eventhubri.event.dto.response;

import com.pointwest.bootcamp.eventhubri.event.enums.SpeakerRole;

public record SessionSpeakerResponseDto(
        Long id,
        Long sessionId,
        Long speakerId,
        String speakerFullName,
        SpeakerRole speakerRole
) {
}
