package com.pointwest.bootcamp.eventhubri.modules.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterOrganizerRequestDto(

        @NotBlank @Email @Size(max = 255) String email,

        @NotBlank @Size(min = 8, max = 100) String password,

        @NotBlank @Size(max = 100) String firstName,

        @NotBlank @Size(max = 100) String lastName,

        @NotBlank @Size(max = 255) String companyName,

        @NotBlank @Email @Size(max = 255) String primaryContactEmail,

        @Size(max = 30) String primaryContactPhone) {
}