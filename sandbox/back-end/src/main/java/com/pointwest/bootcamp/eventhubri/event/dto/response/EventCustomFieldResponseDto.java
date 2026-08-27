package com.pointwest.bootcamp.eventhubri.event.dto.response;

import java.util.List;

import com.pointwest.bootcamp.eventhubri.event.enums.CustomFieldType;

public record EventCustomFieldResponseDto(
        Long id,
        Long eventId,
        String fieldLabel,
        CustomFieldType fieldType,
        List<String> fieldOptions,
        boolean isRequired,
        Integer displayOrder
) {
}
