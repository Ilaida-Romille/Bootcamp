package com.pointwest.bootcamp.eventhubri.modules.profile.service;

import com.pointwest.bootcamp.eventhubri.core.exception.ResourceNotFoundException;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final AppUserRepository appUserRepository;
    private final CloudflareR2Service r2Service;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDto getProfile(Long userId) {
        return toDto(findUser(userId));
    }

    @Override
    @Transactional
    public UserProfileResponseDto updateProfile(Long userId, UserProfileUpdateDto dto) {
        AppUser user = findUser(userId);
        if (dto.firstName() != null) user.setFirstName(dto.firstName());
        if (dto.lastName() != null) user.setLastName(dto.lastName());
        if (dto.company() != null) user.setCompany(dto.company());
        if (dto.dietary() != null) user.setDietary(dto.dietary());
        return toDto(appUserRepository.save(user));
    }

    @Override
    @Transactional
    public String uploadProfilePicture(Long userId, MultipartFile file) {
        AppUser user = findUser(userId);

        // Remove the previous image from R2 before uploading the replacement
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isBlank()) {
            r2Service.deleteProfileImage(user.getProfileImageUrl());
        }

        String url = r2Service.uploadProfileImage(file, userId);
        user.setProfileImageUrl(url);
        appUserRepository.save(user);
        return url;
    }

    private AppUser findUser(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private UserProfileResponseDto toDto(AppUser user) {
        String orgName = (user.getOrganization() != null)
                ? user.getOrganization().getCompanyName()
                : null;

        return new UserProfileResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getCompany(),
                user.getDietary(),
                user.getProfileImageUrl(),
                orgName);
    }
}
