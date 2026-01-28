package com.flipfit.business;
import com.flipfit.bean.Notification;
/**
 * The Interface NotificationInterface.
 *
 * @author Ananya
 * @ClassName  "NotificationInterface"
 */
public interface NotificationInterface {
    void sendNotification(int userId, String message);
}