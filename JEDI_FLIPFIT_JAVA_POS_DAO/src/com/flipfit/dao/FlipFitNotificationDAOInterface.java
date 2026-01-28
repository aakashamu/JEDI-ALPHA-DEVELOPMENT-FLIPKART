package com.flipfit.dao;

import com.flipfit.bean.Notification;
import java.util.List;
/**
 * The Interface FlipFitNotificationDAOInterface.
 *
 * @author Ananya
 * @ClassName  "FlipFitNotificationDAOInterface"
 */
public interface FlipFitNotificationDAOInterface {
    
    /**
     * Create a new notification
     * @param notification Notification object to create
     * @return true if notification is created successfully
     */
    boolean createNotification(Notification notification);
    
    /**
     * Get notification by ID
     * @param notificationId ID of the notification
     * @return Notification object if found, null otherwise
     */
    Notification getNotificationById(int notificationId);
    
    /**
     * Get all notifications for a specific user
     * @param userId ID of the user
     * @return List of notifications for the user
     */
    List<Notification> getNotificationsByUserId(int userId);
    
    /**
     * Get all unread notifications for a specific user
     * @param userId ID of the user
     * @return List of unread notifications
     */
    List<Notification> getUnreadNotificationsByUserId(int userId);
    
    /**
     * Mark notification as read
     * @param notificationId ID of the notification to mark as read
     * @return true if notification status is updated successfully
     */
    boolean markAsRead(int notificationId);
    
    /**
     * Delete a notification by ID
     * @param notificationId ID of the notification to delete
     * @return true if notification is deleted successfully
     */
    boolean deleteNotification(int notificationId);
    
    /**
     * Delete all notifications for a specific user
     * @param userId ID of the user
     * @return true if all notifications are deleted successfully
     */
    boolean deleteAllNotificationsByUserId(int userId);
    
    /**
     * Get all notifications in the system
     * @return List of all notifications
     */
    List<Notification> getAllNotifications();
}
