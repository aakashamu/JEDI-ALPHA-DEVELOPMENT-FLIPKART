package com.flipfit.business;

import com.flipfit.bean.Notification;
import com.flipfit.dao.FlipFitNotificationDAO;
import com.flipfit.dao.FlipFitNotificationDAOInterface;
import java.util.List;
/**
 * The Class NotificationService.
 *
 * @author Ananya
 * @ClassName "NotificationService"
 */
public class NotificationService implements NotificationInterface {

    private final FlipFitNotificationDAOInterface notificationDAO;

    /**
     * Instantiates a new notification service.
     */
    public NotificationService() {
        this.notificationDAO = new FlipFitNotificationDAO();
    }

    /**
     * Send notification.
     *
     * @param userId  the user id
     * @param message the message
     */
    @Override
    public void sendNotification(int userId, String message) {
        if (userId <= 0 || message == null || message.isEmpty()) {
            System.out.println("ERROR: Invalid user ID or message");
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setStatus("UNREAD");
        notification.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

        boolean success = notificationDAO.createNotification(notification);

        if (success) {
            System.out.println("✓ Notification sent to User " + userId + ": " + message);
        } else {
            System.out.println("ERROR: Failed to send notification to User " + userId);
        }
    }

    /**
     * Gets the user notifications.
     *
     * @param userId the user id
     * @return the user notifications
     */
    public List<Notification> getUserNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    /**
     * Gets the unread notifications.
     *
     * @param userId the user id
     * @return the unread notifications
     */
    public List<Notification> getUnreadNotifications(int userId) {
        return notificationDAO.getUnreadNotificationsByUserId(userId);
    }

    /**
     * Mark as read.
     *
     * @param notificationId the notification id
     * @return true, if successful
     */
    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    /**
     * Delete notification.
     *
     * @param notificationId the notification id
     * @return true, if successful
     */
    public boolean deleteNotification(int notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }

    /**
     * View user notifications.
     *
     * @param userId the user id
     */
    public void viewUserNotifications(int userId) {
        List<Notification> notifications = getUserNotifications(userId);

        System.out.println("\n========== NOTIFICATIONS FOR USER " + userId + " ==========");
        if (notifications.isEmpty()) {
            System.out.println("No notifications");
        } else {
            System.out.println("Total Notifications: " + notifications.size());
            System.out.println("-----------------------------------------");
            notifications.forEach(notif -> {
                String statusIcon = "UNREAD".equals(notif.getStatus()) ? "🔔" : "✓";
                System.out.println(statusIcon + " [ID: " + notif.getNotificationId() + "] "
                        + notif.getMessage() + " (" + notif.getStatus() + ")");
            });
        }
        System.out.println("==================================================\n");
    }

    /**
     * Gets the all notifications.
     *
     * @return the all notifications
     */
    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }
}