package com.pointwest.bootcamp.eventhubri.auth.dto;

public record AuthResponse(
    String tokenType,
    String accessToken,
    long accessTokenExpiresIn,
    String refreshToken
) {}
