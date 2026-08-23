package com.pointwest.bootcamp.exercises.notification;

public class NotificationFactory {
    public NotificationStrategy createNotification(String channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }

        return switch (channel.toUpperCase()) {
            case "EMAIL" -> new EmailNotificationStrategy();
            case "SMS" -> new SmsNotificationStrategy();
            case "PUSH" -> new PushNotificationStrategy();
            default -> throw new IllegalArgumentException(
                    "Unsupported channel: " + channel + "(can only be: Email, Sms, or Push)");
        };
    }
}
