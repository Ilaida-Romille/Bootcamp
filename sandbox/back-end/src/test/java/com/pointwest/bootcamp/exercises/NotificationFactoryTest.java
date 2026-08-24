package com.pointwest.bootcamp.exercises;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pointwest.bootcamp.exercises.notification.NotificationFactory;
import com.pointwest.bootcamp.exercises.notification.NotificationStrategy;
import com.pointwest.bootcamp.exercises.notification.RegistrationService;
import com.pointwest.bootcamp.exercises.notification.Registration;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationFactoryTest {
    @Mock
    private NotificationFactory notificationFactory;

    @Mock
    private NotificationStrategy notificationStrategy;

    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService(notificationFactory);
    }

    @Test
    @DisplayName("Should execute notification strategy without altering RegistrationService logic")
    void shouldProcessRegistrationWithoutChangingServiceCode() {
        // Arrange: Mock factory to return the strategy regardless of channel[cite: 1]
        when(notificationFactory.createNotification(anyString())).thenReturn(notificationStrategy);

        Registration registration = new Registration("user@example.com", "123456789", "EMAIL");

        // Act: Process registration[cite: 1]
        registrationService.processRegistration(registration);

        // Assert: Verify Service delegates to factory and strategy dynamically[cite: 1]
        verify(notificationFactory, times(1)).createNotification("EMAIL");
        verify(notificationStrategy, times(1)).send(registration);
    }

    @Test
    @DisplayName("Proves Open/Closed Principle: Handles new notification channels smoothly")
    void shouldHandleNewChannelsWithoutModifyingRegistrationService() {
        // Arrange: Mock factory handling a hypothetically new channel (e.g., WHATSAPP)
        when(notificationFactory.createNotification("WHATSAPP")).thenReturn(notificationStrategy);

        Registration newChannelRegistration = new Registration("user@example.com", "123456789", "WHATSAPP");

        // Act: Process registration with the new channel
        registrationService.processRegistration(newChannelRegistration);

        // Assert: RegistrationService handles new channel without code
        // modifications[cite: 1]
        verify(notificationFactory, times(1)).createNotification("WHATSAPP");
        verify(notificationStrategy, times(1)).send(newChannelRegistration);
    }
}
