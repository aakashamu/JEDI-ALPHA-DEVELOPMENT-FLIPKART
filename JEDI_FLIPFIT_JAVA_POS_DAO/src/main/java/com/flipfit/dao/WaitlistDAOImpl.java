package com.flipfit.dao;

import com.flipfit.bean.WaitListEntry;
import com.flipfit.constants.WaitlistConstants;
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
        
        try (Connection conn = DBConnection.getConnection()) {
            int currentCount = 0;
            try (PreparedStatement posStmt = conn.prepareStatement(WaitlistConstants.GET_WAITLIST_COUNT);
                 ResultSet rs = posStmt.executeQuery()) {
                if (rs.next()) {
                    currentCount = rs.getInt(1);
                }
            }
                                        
            System.out.println("[DEBUG] Current waitlist count: " + currentCount + ", New position will be: " + (currentCount + 1));
                                        
            try (PreparedStatement insertStmt = conn.prepareStatement(WaitlistConstants.INSERT_WAITLIST_ENTRY)) {
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
   * Add To Wait List (Overloaded).
   *
   * @param bookingId the bookingId
   * @param customerId the customerId
   * @param availabilityId the availabilityId
   * @return the int
   */
    public int addToWaitList(int bookingId, int customerId, int availabilityId) {
        try (Connection conn = DBConnection.getConnection()) {
            int position = 1;
            try (PreparedStatement posStmt = conn.prepareStatement(WaitlistConstants.GET_WAITLIST_COUNT_BY_AVAILABILITY)) {
                posStmt.setInt(1, availabilityId);
                try (ResultSet rs = posStmt.executeQuery()) {
                    if (rs.next()) {
                        position = rs.getInt("cnt") + 1;
                    }
                }
            }
                                        
            try (PreparedStatement insertStmt = conn.prepareStatement(WaitlistConstants.INSERT_WAITLIST_ENTRY)) {
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(WaitlistConstants.GET_WAITLIST_COUNT_FOR_PENDING)) {
                                        
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(WaitlistConstants.GET_FIRST_PENDING_ENTRY)) {
                                        
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(WaitlistConstants.UPDATE_WAITLIST_STATUS)) {
                                        
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement deleteStmt = conn.prepareStatement(WaitlistConstants.DELETE_WAITLIST_ENTRY)) {
                                        
            deleteStmt.setInt(1, bookingId);
            deleteStmt.executeUpdate();
                                        
            // Re-ordering positions after removal
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(WaitlistConstants.DELETE_WAITLIST_ENTRY)) {
                                        
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  /**
   * Internal method to re-order positions.
   */
    private void updatePositions(Connection conn) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement selectStmt = conn.prepareStatement(WaitlistConstants.SELECT_WAITLIST_IDS_BY_TIME);
             ResultSet rs = selectStmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("waitlistid"));
            }
        }
                                        
        try (PreparedStatement updateStmt = conn.prepareStatement(WaitlistConstants.UPDATE_WAITLIST_POSITION)) {
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(WaitlistConstants.SELECT_FIRST_ENTRY_FOR_UPDATE);
             ResultSet rs = stmt.executeQuery()) {
                                        
            if (rs.next()) {
                int bookingId = rs.getInt("bookingId");
                removeFromWaitList(bookingId);
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(WaitlistConstants.GET_ALL_WAITLIST_ENTRIES);
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
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(WaitlistConstants.GET_WAITLIST_POSITION)) {
                                        
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
   * Update Waitlist Positions for a specific availability.
   *
   * @param availabilityId the availabilityId
   */
    public void updateWaitlistPositions(int availabilityId) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement selectStmt = conn.prepareStatement(WaitlistConstants.SELECT_WAITLIST_IDS_BY_AVAILABILITY)) {
                                        
            selectStmt.setInt(1, availabilityId);
            List<Integer> waitlistIds = new ArrayList<>();
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    waitlistIds.add(rs.getInt("waitlistId"));
                }
            }
                                        
            try (PreparedStatement updateStmt = conn.prepareStatement(WaitlistConstants.UPDATE_WAITLIST_POSITION)) {
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
    }
}













