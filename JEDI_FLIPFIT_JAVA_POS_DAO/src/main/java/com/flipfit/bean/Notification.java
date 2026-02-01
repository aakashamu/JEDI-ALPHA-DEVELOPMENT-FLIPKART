package com.flipfit.bean;

import java.sql.Timestamp;
/**
 * The Class Notification.
 *
 * @author Ananya
 * @ClassName  "Notification"
 */
public class Notification {
    private int notificationId;
    private int userId;
    private String message;
    private Timestamp timestamp;
    private String status; // UNREAD, READ
    /**
     * Gets the notification id.
     *
     * @return the notification id
     */
    public int getNotificationId() {
        return notificationId;
    }
    /**
     * Sets the notification id.
     *
     * @param notificationId the notification id
     */
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Sets the user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Sets the message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
    /**
     * Sets the timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * Gets the status (UNREAD, READ).
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * Sets the status.
     *
     * @param status the status (UNREAD, READ)
     */
    public void setStatus(String status) {
        this.status = status;
    }
}