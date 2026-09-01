package com.pointwest.bootcamp.eventhubri.modules.profile.controller;

import com.pointwest.bootcamp.eventhubri.modules.auth.security.CurrentUser;
import com.pointwest.bootcamp.eventhubri.modules.auth.security.SecurityUser;
import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileUpdateDto;
import com.pointwest.bootcamp.eventhubri.modules.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponseDto> getProfile(@CurrentUser SecurityUser user) {
        return ResponseEntity.ok(profileService.getProfile(user.userId()));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            @CurrentUser SecurityUser user,
            @Valid @RequestBody UserProfileUpdateDto dto) {
        return ResponseEntity.ok(profileService.updateProfile(user.userId(), dto));
    }

    @PostMapping("/me/picture")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> uploadPicture(
            @CurrentUser SecurityUser user,
            @RequestParam("file") MultipartFile file) {
        String url = profileService.uploadProfilePicture(user.userId(), file);
        return ResponseEntity.ok(Map.of("profileImageUrl", url));
    }
}
