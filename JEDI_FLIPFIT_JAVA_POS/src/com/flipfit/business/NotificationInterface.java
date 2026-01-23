package com.flipfit.business;
import com.flipfit.bean.Notification;

public interface NotificationInterface {
    void sendNotification(int userId, String message);
}