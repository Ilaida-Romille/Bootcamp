package com.pointwest.bootcamp.eventhubri.modules.account.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pointwest.bootcamp.eventhubri.modules.account.dto.OrganizerResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.account.dto.UpdateOrganizerDto;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.OrganizationRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;

import com.pointwest.bootcamp.eventhubri.core.exception.account.OrganizationNotFoundException;
import com.pointwest.bootcamp.eventhubri.core.exception.account.AccountNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizerManagementService {

    private final AppUserRepository appUserRepository;
    private final OrganizationRepository organizationRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public Page<OrganizerResponseDto> getOrganizers(Pageable pageable) {
        return appUserRepository
                .findByRoleAndOrganization_StatusNot(Role.ORGANIZER_ADMIN, Organization.Status.SUSPENDED, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Page<OrganizerResponseDto> searchOrganizers(String query, Pageable pageable) {
        String normalizedQuery = query == null ? "" : query.trim();
        if (normalizedQuery.isBlank()) {
            return getOrganizers(pageable);
        }

        return appUserRepository
                .searchOrganizers(Role.ORGANIZER_ADMIN, Organization.Status.SUSPENDED, normalizedQuery, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public OrganizerResponseDto getOrganizerById(Long id) {
        AppUser user = findOrganizerOrThrow(id);
        return toDto(user);
    }

    @Transactional
    public OrganizerResponseDto updateOrganizer(Long id, UpdateOrganizerDto dto) {
        AppUser user = findOrganizerOrThrow(id);

        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        if (dto.company() != null) {
            user.setCompany(dto.company());
        }
        if (dto.organizationName() != null && user.getOrganization() != null) {
            user.getOrganization().setCompanyName(dto.organizationName());
            organizationRepository.save(user.getOrganization());
        }

        return toDto(appUserRepository.save(user));
    }

    @Transactional
    public OrganizerResponseDto updateUserStatus(Long id, AppUser.Status status) {
        AppUser user = findOrganizerOrThrow(id);
        user.setStatus(status);
        // Setting INACTIVE is sufficient — JwtAuthenticationFilter checks
        // userDetails.isEnabled() (user.status == ACTIVE) on every request.
        return toDto(appUserRepository.save(user));
    }

    @Transactional
    public void deleteOrganizer(Long id) {
        AppUser user = findOrganizerOrThrow(id);
        // Soft-delete: deactivate user and suspend organization so they no longer
        // appear in lists
        user.setStatus(AppUser.Status.INACTIVE);
        appUserRepository.save(user);

        if (user.getOrganization() != null) {
            user.getOrganization().setStatus(Organization.Status.SUSPENDED);
            organizationRepository.save(user.getOrganization());
        }
    }

    private AppUser findOrganizerOrThrow(Long id) {
        return appUserRepository.findById(id)
                .filter(u -> u.getRole() == Role.ORGANIZER_ADMIN)
                .orElseThrow(() -> new AccountNotFoundException("Organizer not found: " + id));
    }

    private OrganizerResponseDto toDto(AppUser user) {
        Organization org = user.getOrganization();
        long totalEvents = org != null ? eventRepository.countByOrganizationId(org.getId()) : 0L;

        return new OrganizerResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCompany(),
                user.getStatus(),
                org != null ? org.getId() : null,
                org != null ? org.getCompanyName() : null,
                org != null ? org.getPrimaryContactEmail() : null,
                org != null ? org.getPrimaryContactPhone() : null,
                org != null ? org.getStatus() : null,
                totalEvents);
    }
}
