package com.flipfit.dao;

import com.flipfit.bean.SlotAvailability;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SlotAvailabilityDAOImpl implements SlotAvailabilityDAO {

    @Override
    public void addSlotAvailability(SlotAvailability slotAvailability) {
        String query = "INSERT INTO SlotAvailability (slotId, date, seatsTotal, seatsAvailable) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, slotAvailability.getSlotId());
            stmt.setDate(2, Date.valueOf(slotAvailability.getDate()));
            stmt.setInt(3, 10); // Default total capacity
            stmt.setInt(4, slotAvailability.isAvailable() ? 10 : 0);
            
            stmt.executeUpdate();
            System.out.println("Slot availability added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateSlotAvailability(int id, SlotAvailability slotAvailability) {
        String query = "UPDATE SlotAvailability SET slotId = ?, date = ?, seatsAvailable = ? WHERE availabilityId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, slotAvailability.getSlotId());
            stmt.setDate(2, Date.valueOf(slotAvailability.getDate()));
            stmt.setInt(3, slotAvailability.isAvailable() ? 10 : 0);
            stmt.setInt(4, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSlotAvailability(int id) {
        String query = "DELETE FROM SlotAvailability WHERE availabilityId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public SlotAvailability getSlotAvailabilityById(int id) {
        String query = "SELECT * FROM SlotAvailability WHERE availabilityId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                SlotAvailability sa = new SlotAvailability();
                sa.setId(rs.getInt("availabilityId"));
                sa.setSlotId(rs.getInt("slotId"));
                sa.setDate(rs.getDate("date").toLocalDate());
                sa.setAvailable(rs.getInt("seatsAvailable") > 0);
                return sa;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SlotAvailability> getAllSlotAvailabilities() {
        List<SlotAvailability> list = new ArrayList<>();
        String query = "SELECT * FROM SlotAvailability";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                SlotAvailability sa = new SlotAvailability();
                sa.setId(rs.getInt("availabilityId"));
                sa.setSlotId(rs.getInt("slotId"));
                sa.setDate(rs.getDate("date").toLocalDate());
                sa.setAvailable(rs.getInt("seatsAvailable") > 0);
                list.add(sa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<SlotAvailability> getAvailableSlotsByDate(LocalDate date) {
        List<SlotAvailability> list = new ArrayList<>();
        String query = "SELECT * FROM SlotAvailability WHERE date = ? AND seatsAvailable > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SlotAvailability sa = new SlotAvailability();
                sa.setId(rs.getInt("availabilityId"));
                sa.setSlotId(rs.getInt("slotId"));
                sa.setDate(rs.getDate("date").toLocalDate());
                sa.setAvailable(true);
                list.add(sa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<SlotAvailability> getAvailableSlotsBySlotId(int slotId) {
        List<SlotAvailability> list = new ArrayList<>();
        String query = "SELECT * FROM SlotAvailability WHERE slotId = ? AND seatsAvailable > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, slotId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SlotAvailability sa = new SlotAvailability();
                sa.setId(rs.getInt("availabilityId"));
                sa.setSlotId(rs.getInt("slotId"));
                sa.setDate(rs.getDate("date").toLocalDate());
                sa.setAvailable(true);
                list.add(sa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Decrement seats available by 1 when booking is made
     */
    public boolean decrementSeats(int availabilityId) {
        String query = "UPDATE SlotAvailability SET seatsAvailable = seatsAvailable - 1 WHERE availabilityId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, availabilityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Increment seats available by 1 when booking is cancelled
     */
    public boolean incrementSeats(int availabilityId) {
        String query = "UPDATE SlotAvailability SET seatsAvailable = seatsAvailable + 1 WHERE availabilityId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, availabilityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
