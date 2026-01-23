package com.flipfit.business;

public class NotificationService implements NotificationInterface {
    @Override
    public void sendNotification(int userId, String message) {
        System.out.println("Notification sent to " + userId + ": " + message);
    }
}