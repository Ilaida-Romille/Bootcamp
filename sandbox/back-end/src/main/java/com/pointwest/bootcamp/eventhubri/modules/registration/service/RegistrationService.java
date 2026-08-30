package com.pointwest.bootcamp.eventhubri.modules.registration.service;

import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegistrationService {

    RegistrationResponseDto register(Long eventId, String attendeeEmail);

    RegistrationResponseDto getOwnRegistration(Long registrationId, String attendeeEmail);

    Page<RegistrationResponseDto> listOwnRegistrations(String attendeeEmail, Pageable pageable);

    void cancelOwnRegistration(Long registrationId, String attendeeEmail);

    Page<RegistrationResponseDto> listRegistrationsForEvent(
            Long eventId, RegistrationStatus statusFilter, String staffEmail, Pageable pageable);

    RegistrationResponseDto checkInAttendee(Long registrationId, String staffEmail);

    RegistrationResponseDto updateStatus(Long registrationId, RegistrationStatus newStatus, String staffEmail);
}
