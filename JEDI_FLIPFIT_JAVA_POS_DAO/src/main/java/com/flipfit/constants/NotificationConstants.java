package com.flipfit.constants;

/**
 * Constants for Notification DAO SQL Queries.
 *
 * @author Ananya
 * @ClassName "NotificationConstants"
 */
public class NotificationConstants {
    public static final String INSERT_NOTIFICATION = "INSERT INTO Notification (userId, message, status, timestamp) VALUES (?, ?, ?, ?)";
    public static final String GET_NOTIFICATION_BY_ID = "SELECT * FROM Notification WHERE notificationId = ?";
    public static final String GET_NOTIFICATIONS_BY_USER_ID = "SELECT * FROM Notification WHERE userId = ? ORDER BY timestamp DESC";
    public static final String GET_UNREAD_NOTIFICATIONS_BY_USER_ID = "SELECT * FROM Notification WHERE userId = ? AND status = 'UNREAD' ORDER BY timestamp DESC";
    public static final String MARK_AS_READ = "UPDATE Notification SET status = 'READ' WHERE notificationId = ?";
    public static final String DELETE_NOTIFICATION = "DELETE FROM Notification WHERE notificationId = ?";
    public static final String DELETE_ALL_NOTIFICATIONS_BY_USER_ID = "DELETE FROM Notification WHERE userId = ?";
    public static final String GET_ALL_NOTIFICATIONS = "SELECT * FROM Notification ORDER BY timestamp DESC";
}