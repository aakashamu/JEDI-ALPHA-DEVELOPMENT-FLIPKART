package com.flipfit.client;

import com.flipfit.bean.Notification;
import com.flipfit.business.NotificationService;
import java.util.List;
/**
 * The Class NotificationDemo.
 *
 * @author Ananya
 * @ClassName  "NotificationDemo"
 */
public class NotificationDemo {
  /**
   * Run demo.
   */
    public static void runDemo() {
        System.out.println("========== Notification Demo ==========\n");

        // Create notification service
        NotificationService notificationService = new NotificationService();

        // Test 1: Send notifications to a user
        System.out.println("Test 1: Sending Notifications");
        System.out.println("------------------------------");
        notificationService.sendNotification(101, "Welcome to FlipFit!");
        notificationService.sendNotification(101, "Your booking for slot 501 is confirmed");
        notificationService.sendNotification(101, "Reminder: Your session starts in 1 hour");
        notificationService.sendNotification(102, "Welcome to FlipFit!");

        // Test 2: View all notifications for user 101
        System.out.println("\nTest 2: View All Notifications");
        System.out.println("-------------------------------");
        notificationService.viewUserNotifications(101);

        // Test 3: Get unread notifications
        System.out.println("Test 3: Get Unread Notifications");
        System.out.println("---------------------------------");
        List<Notification> unreadNotifs = notificationService.getUnreadNotifications(101);
        System.out.println("User 101 has " + unreadNotifs.size() + " unread notifications");

        // Test 4: Mark a notification as read
        System.out.println("\nTest 4: Mark Notification as Read");
        System.out.println("----------------------------------");
        List<Notification> user101Notifs = notificationService.getUserNotifications(101);
        if (!user101Notifs.isEmpty()) {
            int firstNotifId = user101Notifs.get(0).getNotificationId();
            boolean marked = notificationService.markAsRead(firstNotifId);
            System.out.println("Marked notification " + firstNotifId + " as read: " + marked);
        }

        // Test 5: View notifications again to see status change
        System.out.println("\nTest 5: View Notifications After Marking as Read");
        System.out.println("------------------------------------------------");
        notificationService.viewUserNotifications(101);

        // Test 6: Get unread count again
        System.out.println("Test 6: Unread Count After Marking");
        System.out.println("----------------------------------");
        unreadNotifs = notificationService.getUnreadNotifications(101);
        System.out.println("User 101 now has " + unreadNotifs.size() + " unread notifications\n");

        // Test 7: Delete a notification
        System.out.println("Test 7: Delete a Notification");
        System.out.println("-----------------------------");
        if (!user101Notifs.isEmpty() && user101Notifs.size() > 1) {
            int secondNotifId = user101Notifs.get(1).getNotificationId();
            boolean deleted = notificationService.deleteNotification(secondNotifId);
            System.out.println("Deleted notification " + secondNotifId + ": " + deleted);
        }

        // Test 8: View final state
        System.out.println("\nTest 8: Final State");
        System.out.println("-------------------");
        notificationService.viewUserNotifications(101);

        // Test 9: Get all system notifications
        System.out.println("Test 9: All System Notifications");
        System.out.println("--------------------------------");
        List<Notification> allNotifs = notificationService.getAllNotifications();
        System.out.println("Total notifications in system: " + allNotifs.size());

        System.out.println("\n========== Demo Complete ==========");
    }
}
