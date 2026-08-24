package com.pointwest.bootcamp.exercises.notification;

public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void send(Registration r) {
        System.out.println("Sending confirmation email to: " + r.getUserEmail());
    }
}
