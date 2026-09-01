package com.pointwest.bootcamp.eventhubri.modules.communication.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pointwest.bootcamp.eventhubri.core.exception.AccessDeniedOperationException;
import com.pointwest.bootcamp.eventhubri.core.exception.ResourceNotFoundException;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.EmailSendRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.NotificationLogResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.entity.BroadcastNoticeLog;
import com.pointwest.bootcamp.eventhubri.modules.communication.entity.DeliveryStatus;
import com.pointwest.bootcamp.eventhubri.modules.communication.entity.EmailNotificationLog;
import com.pointwest.bootcamp.eventhubri.modules.communication.repository.BroadcastNoticeLogRepository;
import com.pointwest.bootcamp.eventhubri.modules.communication.repository.EmailNotificationLogRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.Registration;
import com.pointwest.bootcamp.eventhubri.modules.registration.repository.RegistrationRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;
    private final RegistrationRepository registrationRepository;
    private final EmailNotificationLogRepository emailNotificationLogRepository;
    private final BroadcastNoticeLogRepository broadcastNoticeLogRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public NotificationLogResponseDto sendBroadcastEmail(EmailSendRequestDto request, String authenticatedUserEmail) {
        AppUser sender = appUserRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        if (sender.getRole() != Role.ORGANIZER_ADMIN && sender.getRole() != Role.ORGANIZER_STAFF) {
            throw new AccessDeniedOperationException("You do not have permission to send event notifications.");
        }

        Event event = eventRepository
                .findByIdAndOrganizationId(request.eventId(),
                        sender.getOrganization() != null ? sender.getOrganization().getId() : null)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found or not managed by your organization: " + request.eventId()));

        LocalDateTime sentAt = LocalDateTime.now();

        if (request.recipientUserId() != null) {
            AppUser recipient = appUserRepository.findById(request.recipientUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Recipient user not found: " + request.recipientUserId()));

            registrationRepository.findByEvent_IdAndAttendee_Id(request.eventId(), recipient.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Recipient is not registered for event: " + request.eventId()));

            return sendSingleEmail(event, sender, recipient, request, sentAt);
        }

        List<Registration> registrations = registrationRepository.findByEvent_Id(request.eventId());
        if (registrations.isEmpty()) {
            throw new ResourceNotFoundException("No registrations found for this event.");
        }

        List<EmailNotificationLog> savedLogs = new ArrayList<>();

        for (Registration registration : registrations) {
            AppUser recipient = registration.getAttendee();
            savedLogs.add(sendSingleEmailLog(event, sender, recipient, request, sentAt));
        }

        BroadcastNoticeLog broadcastLog = BroadcastNoticeLog.builder()
                .event(event)
                .sender(sender)
                .subject(request.subject())
                .messageBody(request.messageBody())
                .sentAt(sentAt)
                .deliveryStatus(DeliveryStatus.SENT)
                .build();
        broadcastNoticeLogRepository.save(broadcastLog);

        EmailNotificationLog firstLog = savedLogs.isEmpty() ? null : savedLogs.getFirst();
        return new NotificationLogResponseDto(
                firstLog != null ? firstLog.getId() : broadcastLog.getId(),
                event.getId(),
                sender.getId(),
                sender.getFirstName() + " " + sender.getLastName(),
                null,
                "EMAIL_BROADCAST",
                request.subject(),
                request.messageBody(),
                sentAt,
                DeliveryStatus.SENT);
    }

    private NotificationLogResponseDto sendSingleEmail(Event event, AppUser sender, AppUser recipient,
            EmailSendRequestDto request, LocalDateTime sentAt) {
        EmailNotificationLog log = sendSingleEmailLog(event, sender, recipient, request, sentAt);
        return new NotificationLogResponseDto(
                log.getId(),
                event.getId(),
                sender.getId(),
                sender.getFirstName() + " " + sender.getLastName(),
                recipient.getId(),
                "EMAIL_NOTIFICATION",
                request.subject(),
                request.messageBody(),
                sentAt,
                log.getDeliveryStatus());
    }

    private EmailNotificationLog sendSingleEmailLog(Event event, AppUser sender, AppUser recipient,
            EmailSendRequestDto request, LocalDateTime sentAt) {
        String renderedHtml = renderEmailBody(event, sender, recipient, request.messageBody());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipient.getEmail());
            helper.setSubject(request.subject());
            helper.setText(renderedHtml, true);
            helper.setFrom("no-reply@eventhub.local");
            mailSender.send(message);

            EmailNotificationLog log = EmailNotificationLog.builder()
                    .event(event)
                    .sender(sender)
                    .recipient(recipient)
                    .subject(request.subject())
                    .messageBody(request.messageBody())
                    .sentAt(sentAt)
                    .deliveryStatus(DeliveryStatus.SENT)
                    .build();
            return emailNotificationLogRepository.save(log);
        } catch (MessagingException e) {
            EmailNotificationLog failedLog = EmailNotificationLog.builder()
                    .event(event)
                    .sender(sender)
                    .recipient(recipient)
                    .subject(request.subject())
                    .messageBody(request.messageBody())
                    .sentAt(sentAt)
                    .deliveryStatus(DeliveryStatus.FAILED)
                    .build();
            return emailNotificationLogRepository.save(failedLog);
        }
    }

    private String renderEmailBody(Event event, AppUser sender, AppUser recipient, String messageBody) {
        Context context = new Context();
        context.setVariable("eventTitle", event.getTitle());
        context.setVariable("eventDate", event.getStartTime());
        context.setVariable("eventLocation",
                event.getLocationAddress() != null ? event.getLocationAddress() : "Virtual");
        context.setVariable("senderName", sender.getFirstName() + " " + sender.getLastName());
        context.setVariable("recipientName", recipient.getFirstName() + " " + recipient.getLastName());
        context.setVariable("messageBody", messageBody);
        return templateEngine.process("email/notification", context);
    }
}
