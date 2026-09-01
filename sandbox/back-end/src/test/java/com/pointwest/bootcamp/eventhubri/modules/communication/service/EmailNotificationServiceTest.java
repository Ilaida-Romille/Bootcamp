package com.pointwest.bootcamp.eventhubri.modules.communication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.EmailSendRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.NotificationLogResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.entity.DeliveryStatus;
import com.pointwest.bootcamp.eventhubri.modules.communication.repository.BroadcastNoticeLogRepository;
import com.pointwest.bootcamp.eventhubri.modules.communication.repository.EmailNotificationLogRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.Registration;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.repository.RegistrationRepository;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private BroadcastNoticeLogRepository broadcastNoticeLogRepository;

    @Mock
    private EmailNotificationLogRepository emailNotificationLogRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailNotificationServiceImpl service;

    private Event event;
    private AppUser organizer;

    @BeforeEach
    void setUp() {
        Organization organization = new Organization();
        organization.setId(7L);
        organization.setCompanyName("Acme Events");

        organizer = AppUser.builder()
                .id(11L)
                .email("organizer@example.com")
                .firstName("Dana")
                .lastName("Scott")
                .organization(organization)
                .role(Role.ORGANIZER_ADMIN)
                .build();

        event = Event.builder()
                .id(99L)
                .title("Spring Boot Meetup")
                .description("A meetup for developers")
                .organization(organization)
                .createdBy(organizer)
                .startTime(LocalDateTime.now().plusDays(2))
                .endTime(LocalDateTime.now().plusDays(2).plusHours(3))
                .status(Event.Status.PUBLISHED)
                .build();
    }

    @Test
    void sendBroadcastEmail_shouldSendToEachRegisteredAttendeeAndPersistLogs() {
        AppUser attendeeOne = AppUser.builder().id(21L).email("alice@example.com").firstName("Alice").lastName("Jones")
                .build();
        AppUser attendeeTwo = AppUser.builder().id(22L).email("bob@example.com").firstName("Bob").lastName("Smith")
                .build();

        Registration registrationOne = Registration.builder()
                .id(1L)
                .event(event)
                .attendee(attendeeOne)
                .status(RegistrationStatus.CONFIRMED)
                .build();

        Registration registrationTwo = Registration.builder()
                .id(2L)
                .event(event)
                .attendee(attendeeTwo)
                .status(RegistrationStatus.CONFIRMED)
                .build();

        when(appUserRepository.findByEmail("organizer@example.com")).thenReturn(Optional.of(organizer));
        when(eventRepository.findByIdAndOrganizationId(99L, 7L)).thenReturn(Optional.of(event));
        when(registrationRepository.findByEvent_Id(99L)).thenReturn(List.of(registrationOne, registrationTwo));
        when(templateEngine.process(contains("email/notification"), any(Context.class))).thenReturn("<p>hello</p>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        EmailSendRequestDto request = new EmailSendRequestDto(99L, null, "Event Update", "Hello from the organizer");

        NotificationLogResponseDto response = service.sendBroadcastEmail(request, "organizer@example.com");

        assertEquals("Event Update", response.subject());
        verify(mailSender, times(2)).send(any(MimeMessage.class));
        verify(emailNotificationLogRepository, times(2)).save(any());
        verify(broadcastNoticeLogRepository, times(1)).save(any());
    }

    @Test
    void sendBroadcastEmail_shouldSendOnlyToRequestedRecipientWhenRecipientUserIdIsProvided() {
        AppUser attendee = AppUser.builder().id(21L).email("alice@example.com").firstName("Alice").lastName("Jones")
                .build();

        when(appUserRepository.findByEmail("organizer@example.com")).thenReturn(Optional.of(organizer));
        when(eventRepository.findByIdAndOrganizationId(99L, 7L)).thenReturn(Optional.of(event));
        when(appUserRepository.findById(21L)).thenReturn(Optional.of(attendee));
        when(registrationRepository.findByEvent_IdAndAttendee_Id(99L, 21L))
                .thenReturn(Optional.of(Registration.builder().id(7L).event(event).attendee(attendee)
                        .status(RegistrationStatus.CONFIRMED).build()));
        when(templateEngine.process(contains("email/notification"), any(Context.class))).thenReturn("<p>hello</p>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailNotificationLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        EmailSendRequestDto request = new EmailSendRequestDto(99L, 21L, "Private Update", "Hello Alice");

        NotificationLogResponseDto response = service.sendBroadcastEmail(request, "organizer@example.com");

        assertEquals("Private Update", response.subject());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(emailNotificationLogRepository, times(1)).save(any());
        verify(broadcastNoticeLogRepository, times(0)).save(any());
    }
}
