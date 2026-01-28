package com.flipfit.business;

import com.flipfit.bean.Notification;
import com.flipfit.dao.FlipFitNotificationDAO;
import com.flipfit.dao.FlipFitNotificationDAOInterface;
import java.util.List;
/**
 * The Class NotificationService.
 *
 * @author Ananya
 * @ClassName  "NotificationService"
 */
public class NotificationService implements NotificationInterface {

    private final FlipFitNotificationDAOInterface notificationDAO;
  /**
   * Notification Service.
   *
   * @return the public
   */
    public NotificationService() {
        this.notificationDAO = new FlipFitNotificationDAO();
    }
  /**
   * Send Notification.
   *
   * @param userId the userId
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
   * Get User Notifications.
   *
   * @param userId the userId
   * @return the List<Notification>
   */
    public List<Notification> getUserNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }
  /**
   * Get Unread Notifications.
   *
   * @param userId the userId
   * @return the List<Notification>
   */
    public List<Notification> getUnreadNotifications(int userId) {
        return notificationDAO.getUnreadNotificationsByUserId(userId);
    }
  /**
   * Mark As Read.
   *
   * @param notificationId the notificationId
   * @return the boolean
   */
    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }
  /**
   * Delete Notification.
   *
   * @param notificationId the notificationId
   * @return the boolean
   */
    public boolean deleteNotification(int notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }
  /**
   * View User Notifications.
   *
   * @param userId the userId
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
   * Get All Notifications.
   *
   * @return the List<Notification>
   */
    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }
}