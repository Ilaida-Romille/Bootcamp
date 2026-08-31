package com.pointwest.bootcamp.eventhubri.modules.auth.security;

public record SecurityUser(Long userId, String email, Long organizationId) {
}
