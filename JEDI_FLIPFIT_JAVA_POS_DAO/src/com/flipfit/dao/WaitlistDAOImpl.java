package com.flipfit.dao;

import com.flipfit.bean.WaitListEntry;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The Class WaitlistDAOImpl.
 *
 * @author Ananya
 * @ClassName  "WaitlistDAOImpl"
 */
public class WaitlistDAOImpl implements WaitlistDAO {
  /**
   * Add To Wait List.
   *
   * @param bookingId the bookingId
   * @return the boolean
   */
    @Override
    public boolean addToWaitList(int bookingId) {
        System.out.println("[DEBUG] addToWaitList called with bookingId=" + bookingId);
        // Need to calculate position: next available position
        String posQuery = "SELECT COUNT(*) FROM Waitlist";
        String insertQuery = "INSERT INTO Waitlist (bookingId, position, createdAt) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            int currentCount = 0;
            try (PreparedStatement posStmt = conn.prepareStatement(posQuery);
                 ResultSet rs = posStmt.executeQuery()) {
                if (rs.next()) {
                    currentCount = rs.getInt(1);
                }
            }
            
            System.out.println("[DEBUG] Current waitlist count: " + currentCount + ", New position will be: " + (currentCount + 1));
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, bookingId);
                insertStmt.setInt(2, currentCount + 1);
                insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                
                System.out.println("[DEBUG] Executing waitlist insert for bookingId=" + bookingId);
                int affectedRows = insertStmt.executeUpdate();
                System.out.println("[DEBUG] Waitlist insert affected rows: " + affectedRows);
                
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException in addToWaitList: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
  /**
   * Add To Wait List.
   *
   * @param bookingId the bookingId
   * @param customerId the customerId
   * @param availabilityId the availabilityId
   * @return the int
   */
    public int addToWaitList(int bookingId, int customerId, int availabilityId) {
        String posQuery = "SELECT COUNT(*) as cnt FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ?";
        String insertQuery = "INSERT INTO Waitlist (bookingId, position, createdAt) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            int position = 1;
            try (PreparedStatement posStmt = conn.prepareStatement(posQuery)) {
                posStmt.setInt(1, availabilityId);
                try (ResultSet rs = posStmt.executeQuery()) {
                    if (rs.next()) {
                        position = rs.getInt("cnt") + 1;
                    }
                }
            }
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, bookingId);
                insertStmt.setInt(2, position);
                insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                
                insertStmt.executeUpdate();
                return position;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
  /**
   * Get Waitlist Count By Availability Id.
   *
   * @param availabilityId the availabilityId
   * @return the int
   */
    public int getWaitlistCountByAvailabilityId(int availabilityId) {
        String query = "SELECT COUNT(*) as cnt FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ? AND b.status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, availabilityId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
  /**
   * Get First Pending Waitlist Entry.
   *
   * @param availabilityId the availabilityId
   * @return the WaitListEntry
   */
    public WaitListEntry getFirstPendingWaitlistEntry(int availabilityId) {
        String query = "SELECT w.*, b.userId, b.availabilityId FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ? AND b.status = 'PENDING' ORDER BY w.position ASC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, availabilityId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    WaitListEntry entry = new WaitListEntry();
                    entry.setWaitlistid(rs.getInt("waitlistId"));
                    entry.setBookingId(rs.getInt("bookingId"));
                    entry.setCustomerId(rs.getInt("userId"));
                    entry.setAvailabilityId(rs.getInt("availabilityId"));
                    entry.setPosition(rs.getInt("position"));
                    entry.setStatus("PENDING");
                    entry.setCreatedAt(rs.getTimestamp("createdAt"));
                    return entry;
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException in getFirstPendingWaitlistEntry: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
  /**
   * Update Waitlist Status.
   *
   * @param waitlistId the waitlistId
   * @param status the status
   * @return the boolean
   */
    public boolean updateWaitlistStatus(int waitlistId, String status) {
        String query = "UPDATE Waitlist SET status = ? WHERE waitlistId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, waitlistId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
  /**
   * Remove From Wait List.
   *
   * @param bookingId the bookingId
   */
    @Override
    public void removeFromWaitList(int bookingId) {
        String deleteQuery = "DELETE FROM Waitlist WHERE bookingId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            
            deleteStmt.setInt(1, bookingId);
            deleteStmt.executeUpdate();
            
            // Re-ordering positions after removal
            // Note: Multiple statements might require session handling or a simpler loop
            updatePositions(conn);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  /**
   * Remove From Wait List By Booking Id.
   *
   * @param bookingId the bookingId
   */
    public void removeFromWaitListByBookingId(int bookingId) {
        String deleteQuery = "DELETE FROM Waitlist WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  /**
   * Update Positions.
   *
   * @param conn the conn
 * @throws SQLException 
   */
    private void updatePositions(Connection conn) throws SQLException {
        String selectQuery = "SELECT waitlistId FROM Waitlist ORDER BY createdAt ASC";
        String updateQuery = "UPDATE Waitlist SET position = ? WHERE waitlistId = ?";
        
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             ResultSet rs = selectStmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("waitlistid"));
            }
        }
        
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            for (int i = 0; i < ids.size(); i++) {
                updateStmt.setInt(1, i + 1);
                updateStmt.setInt(2, ids.get(i));
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();
        }
    }
  /**
   * Update Wait List.
   *
   * @return the boolean
   */
    @Override
    public boolean updateWaitList() {
        // This usually means processing the first person in the waitlist when a slot opens up
        String firstEntryQuery = "SELECT bookingId FROM Waitlist ORDER BY position ASC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(firstEntryQuery);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int bookingId = rs.getInt("bookingId");
                removeFromWaitList(bookingId);
                // In a real app, you'd then update the Booking status to CONFIRMED
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
  /**
   * Get All Wait List Entries.
   *
   * @return the List<WaitListEntry>
   */
    @Override
    public List<WaitListEntry> getAllWaitListEntries() {
        List<WaitListEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM Waitlist ORDER BY position ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                WaitListEntry entry = new WaitListEntry();
                entry.setWaitlistid(rs.getInt("waitlistId"));
                entry.setPosition(rs.getInt("position"));
                entry.setCreatedAt(rs.getTimestamp("createdAt"));
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
  /**
   * Get Wait List Position.
   *
   * @param bookingId the bookingId
   * @return the int
   */
    @Override
    public int getWaitListPosition(int bookingId) {
        String query = "SELECT position FROM Waitlist WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("position");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
  /**
   * Update Waitlist Positions.
   *
   * @param availabilityId the availabilityId
   */
    public void updateWaitlistPositions(int availabilityId) {
        String selectQuery = "SELECT w.waitlistId FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ? AND b.status = 'PENDING' ORDER BY w.position ASC";
        String updateQuery = "UPDATE Waitlist SET position = ? WHERE waitlistId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            
            selectStmt.setInt(1, availabilityId);
            List<Integer> waitlistIds = new ArrayList<>();
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    waitlistIds.add(rs.getInt("waitlistId"));
                }
            }
            
            // Update each position sequentially
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                for (int i = 0; i < waitlistIds.size(); i++) {
                    updateStmt.setInt(1, i + 1);
                    updateStmt.setInt(2, waitlistIds.get(i));
                    updateStmt.addBatch();
                }
                updateStmt.executeBatch();
                System.out.println("[DEBUG] Updated " + waitlistIds.size() + " waitlist positions for availability: " + availabilityId);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException in updateWaitlistPositions: " + e.getMessage());
            e.printStackTrace();
        }
    }}