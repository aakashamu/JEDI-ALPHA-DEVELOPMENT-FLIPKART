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
   * Get Notification Id.
   *
   * @return the int
   */
    public int getNotificationId() {
        return notificationId;
    }
  /**
   * Set Notification Id.
   *
   * @param notificationId the notificationId
   */
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
  /**
   * Get User Id.
   *
   * @return the int
   */
    public int getUserId() {
        return userId;
    }
  /**
   * Set User Id.
   *
   * @param userId the userId
   */
    public void setUserId(int userId) {
        this.userId = userId;
    }
  /**
   * Get Message.
   *
   * @return the String
   */
    public String getMessage() {
        return message;
    }
  /**
   * Set Message.
   *
   * @param message the message
   */
    public void setMessage(String message) {
        this.message = message;
    }
  /**
   * Get Timestamp.
   *
   * @return the Timestamp
   */
    public Timestamp getTimestamp() {
        return timestamp;
    }
  /**
   * Set Timestamp.
   *
   * @param timestamp the timestamp
   */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
  /**
   * Get Status.
   *
   * @return the String
   */
    public String getStatus() {
        return status;
    }
  /**
   * Set Status.
   *
   * @param status the status
   */
    public void setStatus(String status) {
        this.status = status;
    }
}