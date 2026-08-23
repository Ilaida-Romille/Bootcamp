package com.pointwest.bootcamp.exercises.notification;

public class RegistrationService {
    private final NotificationFactory notificationFactory;

    public RegistrationService(NotificationFactory notificationFactory) {
        this.notificationFactory = notificationFactory;
    }

    public void processRegistration(Registration registration) {
        NotificationStrategy strategy = notificationFactory.createNotification(registration.getPreferredChannel());
        strategy.send(registration);
    }
}
