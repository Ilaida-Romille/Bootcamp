package com.pointwest.bootcamp.eventhubri.event.dto.request;

import java.util.List;

import com.pointwest.bootcamp.eventhubri.event.enums.CustomFieldType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventCustomFieldRequestDto(
        @NotNull Long eventId,
        @NotBlank String fieldLabel,
        @NotNull CustomFieldType fieldType,
        List<String> fieldOptions,
        boolean isRequired,
        Integer displayOrder
) {
}
