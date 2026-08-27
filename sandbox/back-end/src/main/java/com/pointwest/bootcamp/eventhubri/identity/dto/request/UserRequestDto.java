package com.pointwest.bootcamp.eventhubri.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Inbound contract for creating/updating a User. Never accept the User
 * entity itself at the controller boundary -- decouples the DB schema
 * (e.g. passwordHash storage) from what a client is allowed to send
 * (a raw `password`, never a hash).
 */
public record UserRequestDto(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Pattern(regexp = "^[+0-9()\\-\\s]{0,30}$", message = "invalid phone number format")
        String phoneNumber
) {
}
