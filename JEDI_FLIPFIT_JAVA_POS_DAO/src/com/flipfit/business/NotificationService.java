package com.flipfit.business;

import com.flipfit.bean.Notification;
import com.flipfit.dao.FlipFitNotificationDAO;
import com.flipfit.dao.FlipFitNotificationDAOInterface;
import java.util.List;

/**
 * Business Service for Notification operations
 * Uses DAO layer for data access
 */
public class NotificationService implements NotificationInterface {

    private final FlipFitNotificationDAOInterface notificationDAO;

    /**
     * Constructor - initializes DAO
     */
    public NotificationService() {
        this.notificationDAO = new FlipFitNotificationDAO();
    }

    /**
     * Sends a notification to a user
     * 
     * @param userId  ID of the user to send notification to
     * @param message Message content
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
     * Get all notifications for a user
     * 
     * @param userId ID of the user
     * @return List of notifications
     */
    public List<Notification> getUserNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    /**
     * Get unread notifications for a user
     * 
     * @param userId ID of the user
     * @return List of unread notifications
     */
    public List<Notification> getUnreadNotifications(int userId) {
        return notificationDAO.getUnreadNotificationsByUserId(userId);
    }

    /**
     * Mark notification as read
     * 
     * @param notificationId ID of the notification
     * @return true if successful
     */
    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    /**
     * Delete a notification
     * 
     * @param notificationId ID of the notification
     * @return true if successful
     */
    public boolean deleteNotification(int notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }

    /**
     * View all user notifications (console output)
     * 
     * @param userId ID of the user
     */
    public void viewUserNotifications(int userId) {
        List<Notification> notifications = getUserNotifications(userId);

        System.out.println("\n========== NOTIFICATIONS FOR USER " + userId + " ==========");
        if (notifications.isEmpty()) {
            System.out.println("No notifications");
        } else {
            System.out.println("Total Notifications: " + notifications.size());
            System.out.println("-----------------------------------------");
            for (Notification notif : notifications) {
                String statusIcon = "UNREAD".equals(notif.getStatus()) ? "🔔" : "✓";
                System.out.println(statusIcon + " [ID: " + notif.getNotificationId() + "] "
                        + notif.getMessage() + " (" + notif.getStatus() + ")");
            }
        }
        System.out.println("==================================================\n");
    }

    /**
     * Get all notifications in the system
     * 
     * @return List of all notifications
     */
    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }
}