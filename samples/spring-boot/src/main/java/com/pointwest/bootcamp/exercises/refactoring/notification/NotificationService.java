package com.pointwest.bootcamp.exercises.refactoring.notification;

import java.util.List;

import com.pointwest.bootcamp.exercises.refactoring.SentEmail;

public interface NotificationService {
    void sendConfirmation(String email, String guestName, String stayId, String spaceId);

    List<SentEmail> getSentEmail();
}
