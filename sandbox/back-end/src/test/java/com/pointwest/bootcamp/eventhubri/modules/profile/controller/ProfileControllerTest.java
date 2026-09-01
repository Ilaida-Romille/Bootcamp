package com.pointwest.bootcamp.eventhubri.modules.profile.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.auth.security.CustomUserDetails;
import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.profile.dto.UserProfileUpdateDto;
import com.pointwest.bootcamp.eventhubri.modules.profile.service.ProfileService;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getProfile_returnsProfile() throws Exception {
        UserProfileResponseDto response = new UserProfileResponseDto(
                7L,
                "user@example.com",
                "Jane Doe",
                "Acme Events",
                "Vegan",
                "avatar.png",
                "Acme Events");

        when(profileService.getProfile(7L)).thenReturn(response);
        setAuthenticatedUser(7L, 10L, "user@example.com");

        mockMvc.perform(get("/api/profile/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));

        verify(profileService).getProfile(7L);
    }

    @Test
    void updateProfile_returnsUpdatedProfile() throws Exception {
        UserProfileUpdateDto request = new UserProfileUpdateDto("Jane", "Doe", "Acme Events", "Vegan");
        UserProfileResponseDto response = new UserProfileResponseDto(
                7L,
                "user@example.com",
                "Jane Doe",
                "Acme Events",
                "Vegan",
                "avatar.png",
                "Acme Events");

        when(profileService.updateProfile(eq(7L), any(UserProfileUpdateDto.class))).thenReturn(response);
        setAuthenticatedUser(7L, 10L, "user@example.com");

        mockMvc.perform(put("/api/profile/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"));

        verify(profileService).updateProfile(eq(7L), any(UserProfileUpdateDto.class));
    }

    @Test
    void uploadPicture_returnsUrl() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", "image-data".getBytes());
        when(profileService.uploadProfilePicture(eq(7L), any())).thenReturn("https://cdn.example.com/photo.png");
        setAuthenticatedUser(7L, 10L, "user@example.com");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/api/profile/me/picture")
                        .file(file)
                        .principal(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                new CustomUserDetails(AppUser.builder().id(7L).email("user@example.com").organization(Organization.builder().id(10L).companyName("Acme Events").primaryContactEmail("primary@acme.com").status(Organization.Status.ACTIVE).build()).firstName("Jane").lastName("Doe").role(Role.ORGANIZER_STAFF).status(AppUser.Status.ACTIVE).passwordHash("hashed").company("Acme Events").build()), null, java.util.List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileImageUrl").value("https://cdn.example.com/photo.png"));

        verify(profileService).uploadProfilePicture(eq(7L), any());
    }

    private void setAuthenticatedUser(Long userId, Long organizationId, String email) {
        Organization organization = Organization.builder()
                .id(organizationId)
                .companyName("Acme Events")
                .primaryContactEmail("primary@acme.com")
                .status(Organization.Status.ACTIVE)
                .build();

        AppUser user = AppUser.builder()
                .id(userId)
                .email(email)
                .organization(organization)
                .firstName("Jane")
                .lastName("Doe")
                .role(Role.ORGANIZER_STAFF)
                .status(AppUser.Status.ACTIVE)
                .passwordHash("hashed")
                .company("Acme Events")
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        new CustomUserDetails(user), null, java.util.List.of()));
    }
}
