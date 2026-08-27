package com.pointwest.bootcamp.eventhubri.modules.account.dto;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequestDto(

                @NotBlank(message = "Email is required") @Email(message = "Email must be a valid address") @Size(max = 255) String email,

                // Raw password only — hashed with BCrypt in the service layer, never persisted
                // as-is.
                @NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters") String password,

                @NotBlank(message = "First name is required") @Size(max = 100) String firstName,

                @NotBlank(message = "Last name is required") @Size(max = 100) String lastName,

                @NotNull(message = "Role is required") Role role,

                // Required for ORGANIZER_ADMIN / ORGANIZER_STAFF, absent for ATTENDEE /
                // PLATFORM_ADMIN.
                Long organizationId) {
}
