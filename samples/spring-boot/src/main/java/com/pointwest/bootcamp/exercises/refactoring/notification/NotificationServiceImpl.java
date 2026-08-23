package com.pointwest.bootcamp.exercises.refactoring.notification;

import java.util.ArrayList;
import java.util.List;

import com.pointwest.bootcamp.exercises.refactoring.SentEmail;

public class NotificationServiceImpl implements NotificationService {
    private final List<SentEmail> sentEmails = new ArrayList<>();

    @Override
    public void sendConfirmation(String email, String guestName, String stayId, String spaceId) {
        String subject = "Check-in confirmed: Room " + spaceId;
        String body = "Welcome, %s. Stay %s is checked in to Room %s."
                .formatted(guestName, stayId, spaceId);
        sentEmails.add(new SentEmail(email, subject, body));
    }

    @Override
    public List<SentEmail> getSentEmail() {
        return List.copyOf(sentEmails);
    }
}
