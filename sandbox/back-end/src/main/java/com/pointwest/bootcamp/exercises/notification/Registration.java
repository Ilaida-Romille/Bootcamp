package com.pointwest.bootcamp.exercises.notification;

public class Registration {
    private String userEmail;
    private String phoneNumber;
    private String preferredChannel;

    public Registration(String userEmail, String phoneNumber, String preferredChannel) {
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        this.preferredChannel = preferredChannel;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPreferredChannel() {
        return preferredChannel;
    }
}
