package com.flipfit.dao;

import com.flipfit.bean.Slot;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 * The Class SlotDAOImpl.
 *
 * @author Ananya
 * @ClassName  "SlotDAOImpl"
 */
public class SlotDAOImpl implements SlotDAO {
  /**
   * Add Slot.
   *
   * @param slot the slot
   */
    @Override
    public void addSlot(Slot slot) {
        String query = "INSERT INTO Slot (startTime, endTime, capacity, centreId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTime(1, Time.valueOf(slot.getStartTime()));
            stmt.setTime(2, Time.valueOf(slot.getEndTime()));
            stmt.setInt(3, slot.getCapacity());
            stmt.setInt(4, slot.getCentreId());
            
            stmt.executeUpdate();
            System.out.println("Slot added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  /**
   * Update Slot.
   *
   * @param slotId the slotId
   * @param slot the slot
   * @return the boolean
   */
    @Override
    public boolean updateSlot(int slotId, Slot slot) {
        String query = "UPDATE Slot SET startTime = ?, endTime = ?, capacity = ?, centreId = ? WHERE slotId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTime(1, Time.valueOf(slot.getStartTime()));
            stmt.setTime(2, Time.valueOf(slot.getEndTime()));
            stmt.setInt(3, slot.getCapacity());
            stmt.setInt(4, slot.getCentreId());
            stmt.setInt(5, slotId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
  /**
   * Delete Slot.
   *
   * @param slotId the slotId
   * @return the boolean
   */
    @Override
    public boolean deleteSlot(int slotId) {
        String query = "DELETE FROM Slot WHERE slotId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, slotId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
  /**
   * Get Slot By Id.
   *
   * @param slotId the slotId
   * @return the Slot
   */
    @Override
    public Slot getSlotById(int slotId) {
        String query = "SELECT * FROM Slot WHERE slotId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, slotId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotId(rs.getInt("slotId"));
                slot.setStartTime(rs.getTime("startTime").toLocalTime());
                slot.setEndTime(rs.getTime("endTime").toLocalTime());
                slot.setCapacity(rs.getInt("capacity"));
                slot.setCentreId(rs.getInt("centreId"));
                return slot;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
  /**
   * Get All Slots.
   *
   * @return the List<Slot>
   */
    @Override
    public List<Slot> getAllSlots() {
        List<Slot> slots = new ArrayList<>();
        String query = "SELECT * FROM Slot";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotId(rs.getInt("slotId"));
                slot.setStartTime(rs.getTime("startTime").toLocalTime());
                slot.setEndTime(rs.getTime("endTime").toLocalTime());
                slot.setCapacity(rs.getInt("capacity"));
                slot.setCentreId(rs.getInt("centreId"));
                slots.add(slot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }
  /**
   * Get Slots By Centre Id.
   *
   * @param centreId the centreId
   * @return the List<Slot>
   */
    @Override
    public List<Slot> getSlotsByCentreId(int centreId) {
        List<Slot> slots = new ArrayList<>();
        String query = "SELECT * FROM Slot WHERE centreId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, centreId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotId(rs.getInt("slotId"));
                slot.setStartTime(rs.getTime("startTime").toLocalTime());
                slot.setEndTime(rs.getTime("endTime").toLocalTime());
                slot.setCapacity(rs.getInt("capacity"));
                slot.setCentreId(rs.getInt("centreId"));
                slots.add(slot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }
}
