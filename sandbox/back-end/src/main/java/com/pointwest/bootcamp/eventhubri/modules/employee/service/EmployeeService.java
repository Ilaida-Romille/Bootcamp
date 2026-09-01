package com.pointwest.bootcamp.eventhubri.modules.employee.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.auth.repository.RefreshTokenRepository;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.EmployeeResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.RegisteredEventSummaryDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.UpdateEmployeeRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.repository.EmployeeRepository;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.repository.RegistrationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RegistrationRepository registrationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployees(Long organizationId) {
        return employeeRepository
                .findByOrganizationIdAndRoleOrderByLastNameAscFirstNameAsc(organizationId, Role.ORGANIZER_STAFF)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployee(Long organizationId, Long id) {
        AppUser staffUser = findEmployeeOrThrow(organizationId, id);
        return toDto(staffUser);
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(Long organizationId, Long id, UpdateEmployeeRequestDto dto) {
        AppUser user = findEmployeeOrThrow(organizationId, id);

        if (dto.firstName() != null)
            user.setFirstName(dto.firstName());
        if (dto.lastName() != null)
            user.setLastName(dto.lastName());
        if (dto.email() != null)
            user.setEmail(dto.email());
        if (dto.company() != null)
            user.setCompany(dto.company());
        if (dto.avatarUrl() != null)
            user.setProfileImageUrl(dto.avatarUrl());

        return toDto(employeeRepository.save(user));
    }

    @Transactional
    public EmployeeResponseDto patchEmployee(Long organizationId, Long id, UpdateEmployeeRequestDto dto) {
        return updateEmployee(organizationId, id, dto);
    }

    @Transactional
    public void deleteEmployee(Long organizationId, Long id) {
        AppUser user = findEmployeeOrThrow(organizationId, id);
        registrationRepository.deleteByAttendee_Id(id);
        refreshTokenRepository.deleteByUser_Id(id);
        employeeRepository.delete(user);
    }

    private AppUser findEmployeeOrThrow(Long organizationId, Long id) {
        return employeeRepository
                .findByIdAndOrganizationIdAndRole(id, organizationId, Role.ORGANIZER_STAFF)
                .orElseThrow(() -> new EntityNotFoundException("Staff member not found: " + id));
    }

    private EmployeeResponseDto toDto(AppUser user) {
        List<RegisteredEventSummaryDto> registeredEvents = registrationRepository
                .findByAttendee_IdAndStatusNot(user.getId(), RegistrationStatus.CANCELLED)
                .stream()
                .sorted(Comparator.comparing(registration -> registration.getEvent().getTitle()))
                .map(registration -> new RegisteredEventSummaryDto(
                        registration.getId(),
                        registration.getEvent().getId(),
                        registration.getEvent().getTitle(),
                        registration.getEvent().getRegistrationEndTime() != null
                                && registration.getEvent().getRegistrationEndTime().isAfter(LocalDateTime.now())))
                .toList();

        return new EmployeeResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCompany(),
                user.getProfileImageUrl(),
                user.getRole().name(),
                registeredEvents);
    }
}