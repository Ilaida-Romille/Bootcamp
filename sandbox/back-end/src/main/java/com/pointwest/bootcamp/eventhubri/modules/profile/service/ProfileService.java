package com.pointwest.bootcamp.eventhubri.modules.profile.service;

import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UserProfileResponseDto getProfile(Long userId);

    UserProfileResponseDto updateProfile(Long userId, UserProfileUpdateDto dto);

    String uploadProfilePicture(Long userId, MultipartFile file);
}
