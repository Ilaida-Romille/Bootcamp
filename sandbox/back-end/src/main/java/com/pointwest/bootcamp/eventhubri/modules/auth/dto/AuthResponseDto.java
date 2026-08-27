package com.pointwest.bootcamp.eventhubri.modules.auth.dto;

import com.pointwest.bootcamp.eventhubri.modules.account.dto.UserResponseDto;

public record AuthResponseDto(

        String accessToken,

        String tokenType,

        long expiresIn,

        UserResponseDto user) {
}