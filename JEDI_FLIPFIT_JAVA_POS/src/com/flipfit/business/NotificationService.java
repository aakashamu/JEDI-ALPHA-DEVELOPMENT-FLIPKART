package com.flipfit.business;

import com.flipfit.bean.Notification;
import com.flipfit.dao.FlipFitRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationService implements NotificationInterface {
    
    private static int notificationIdCounter = 5001;
    private static final Map<Integer, List<Notification>> userNotifications = new java.util.HashMap<>();
    
    @Override
    public void sendNotification(int userId, String message) {
        if (userId <= 0 || message == null || message.isEmpty()) {
            System.out.println("ERROR: Invalid user ID or message");
            return;
        }
        
        Notification notification = new Notification();
        notification.setNotificationId(notificationIdCounter++);
        notification.setUserId(userId);
        notification.setMessage(message);
        
        // Store in user's notification list
        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(notification);
        
        System.out.println("✓ Notification sent to User " + userId + ": " + message);
    }
    
    /**
     * Get all notifications for a user
     */
    public static List<Notification> getUserNotifications(int userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>());
    }
    
    /**
     * View all user notifications
     */
    public static void viewUserNotifications(int userId) {
        List<Notification> notifications = getUserNotifications(userId);
        
        System.out.println("\n========== NOTIFICATIONS FOR USER " + userId + " ==========");
        if (notifications.isEmpty()) {
            System.out.println("No notifications");
        } else {
            System.out.println("Total Notifications: " + notifications.size());
            System.out.println("-----------------------------------------");
            for (Notification notif : notifications) {
                System.out.println("[ID: " + notif.getNotificationId() + "] " + notif.getMessage());
            }
        }
        System.out.println("==================================================\n");
    }
}