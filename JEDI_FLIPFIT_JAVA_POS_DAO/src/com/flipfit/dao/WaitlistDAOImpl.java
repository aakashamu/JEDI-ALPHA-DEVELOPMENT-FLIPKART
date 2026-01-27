package com.flipfit.dao;

import com.flipfit.bean.WaitListEntry;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WaitlistDAOImpl implements WaitlistDAO {

    @Override
    public boolean addToWaitList(int bookingId) {
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
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, bookingId);
                insertStmt.setInt(2, currentCount + 1);
                insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
}
