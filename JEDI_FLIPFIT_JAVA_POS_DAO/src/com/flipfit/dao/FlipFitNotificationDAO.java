package com.flipfit.dao;

import com.flipfit.bean.Notification;
import com.flipfit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * The Class FlipFitNotificationDAO.
 *
 * @author Ananya
 * @ClassName  "FlipFitNotificationDAO"
 */
public class FlipFitNotificationDAO implements FlipFitNotificationDAOInterface {
  /**
   * Create Notification.
   *
   * @param notification the notification
   * @return the boolean
   */
    @Override
    public boolean createNotification(Notification notification) {
        if (notification == null) {
            System.out.println("ERROR: Notification object is null");
            return false;
        }

        String sql = "INSERT INTO Notification (userId, message, status, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setString(3, notification.getStatus() != null ? notification.getStatus() : "UNREAD");
            stmt.setTimestamp(4, notification.getTimestamp() != null ? notification.getTimestamp()
                    : new java.sql.Timestamp(System.currentTimeMillis()));

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to create notification - " + e.getMessage());
            return false;
        }
    }
  /**
   * Get Notification By Id.
   *
   * @param notificationId the notificationId
   * @return the Notification
   */
    @Override
    public Notification getNotificationById(int notificationId) {
        String sql = "SELECT * FROM Notification WHERE notificationId = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getInt("notificationId"));
                notification.setUserId(rs.getInt("userId"));
                notification.setMessage(rs.getString("message"));
                notification.setStatus(rs.getString("status"));
                notification.setTimestamp(rs.getTimestamp("timestamp"));
                return notification;
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to get notification - " + e.getMessage());
        }
        return null;
    }
  /**
   * Get Notifications By User Id.
   *
   * @param userId the userId
   * @return the List<Notification>
   */
    @Override
    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notification WHERE userId = ? ORDER BY timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getInt("notificationId"));
                notification.setUserId(rs.getInt("userId"));
                notification.setMessage(rs.getString("message"));
                notification.setStatus(rs.getString("status"));
                notification.setTimestamp(rs.getTimestamp("timestamp"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to get user notifications - " + e.getMessage());
        }
        return notifications;
    }
  /**
   * Get Unread Notifications By User Id.
   *
   * @param userId the userId
   * @return the List<Notification>
   */
    @Override
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notification WHERE userId = ? AND status = 'UNREAD' ORDER BY timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getInt("notificationId"));
                notification.setUserId(rs.getInt("userId"));
                notification.setMessage(rs.getString("message"));
                notification.setStatus(rs.getString("status"));
                notification.setTimestamp(rs.getTimestamp("timestamp"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to get unread notifications - " + e.getMessage());
        }
        return notifications;
    }
  /**
   * Mark As Read.
   *
   * @param notificationId the notificationId
   * @return the boolean
   */
    @Override
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE Notification SET status = 'READ' WHERE notificationId = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to mark notification as read - " + e.getMessage());
            return false;
        }
    }
  /**
   * Delete Notification.
   *
   * @param notificationId the notificationId
   * @return the boolean
   */
    @Override
    public boolean deleteNotification(int notificationId) {
        String sql = "DELETE FROM Notification WHERE notificationId = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to delete notification - " + e.getMessage());
            return false;
        }
    }
  /**
   * Delete All Notifications By User Id.
   *
   * @param userId the userId
   * @return the boolean
   */
    @Override
    public boolean deleteAllNotificationsByUserId(int userId) {
        String sql = "DELETE FROM Notification WHERE userId = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to delete all user notifications - " + e.getMessage());
            return false;
        }
    }
  /**
   * Get All Notifications.
   *
   * @return the List<Notification>
   */
    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notification ORDER BY timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getInt("notificationId"));
                notification.setUserId(rs.getInt("userId"));
                notification.setMessage(rs.getString("message"));
                notification.setStatus(rs.getString("status"));
                notification.setTimestamp(rs.getTimestamp("timestamp"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to get all notifications - " + e.getMessage());
        }
        return notifications;
    }
}
