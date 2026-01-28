package com.flipfit.dao;

import com.flipfit.bean.SlotAvailability;
import com.flipfit.constants.SlotAvailabilityConstants;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class SlotAvailabilityDAOImpl.
 *
 * @author Ananya
 * @ClassName  "SlotAvailabilityDAOImpl"
 */
public class SlotAvailabilityDAOImpl implements SlotAvailabilityDAO {
  /**
   * Add Slot Availability.
   *
   * @param slotAvailability the slotAvailability
   */
    @Override
    public void addSlotAvailability(SlotAvailability slotAvailability) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.ADD_SLOT_AVAILABILITY)) {
            
            stmt.setInt(1, slotAvailability.getSlotId());
            stmt.setDate(2, Date.valueOf(slotAvailability.getDate()));
            stmt.setInt(3, slotAvailability.getSeatsTotal());
            stmt.setInt(4, slotAvailability.isAvailable() ? slotAvailability.getSeatsTotal() : 0);
            
            stmt.executeUpdate();
            System.out.println("Slot availability added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  /**
   * Update Slot Availability.
   *
   * @param id the id
   * @param slotAvailability the slotAvailability
   * @return the boolean
   */
    @Override
    public boolean updateSlotAvailability(int id, SlotAvailability slotAvailability) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.UPDATE_SLOT_AVAILABILITY)) {
            
            stmt.setInt(1, slotAvailability.getSlotId());
            stmt.setDate(2, Date.valueOf(slotAvailability.getDate()));
            stmt.setInt(3, slotAvailability.isAvailable() ? slotAvailability.getSeatsTotal() : 0);
            stmt.setInt(4, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

  /**
   * Delete Slot Availability.
   *
   * @param id the id
   * @return the boolean
   */
    @Override
    public boolean deleteSlotAvailability(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.DELETE_SLOT_AVAILABILITY)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

  /**
   * Get Slot Availability By Id.
   *
   * @param id the id
   * @return the SlotAvailability
   */
    @Override
    public SlotAvailability getSlotAvailabilityById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.GET_SLOT_AVAILABILITY_BY_ID)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                SlotAvailability sa = new SlotAvailability();
                sa.setId(rs.getInt("availabilityId"));
                sa.setSlotId(rs.getInt("slotId"));
                sa.setDate(rs.getDate("date").toLocalDate());
                sa.setSeatsAvailable(rs.getInt("seatsAvailable"));
                sa.setSeatsTotal(rs.getInt("seatsTotal"));
                sa.setAvailable(rs.getInt("seatsAvailable") > 0);
                return sa;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

  /**
   * Get All Slot Availabilities.
   *
   * @return the List<SlotAvailability>
   */
    @Override
    public List<SlotAvailability> getAllSlotAvailabilities() {
        List<SlotAvailability> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.GET_ALL_SLOT_AVAILABILITIES);
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

  /**
   * Get Available Slots By Date.
   *
   * @param date the date
   * @return the List<SlotAvailability>
   */
    @Override
    public List<SlotAvailability> getAvailableSlotsByDate(LocalDate date) {
        List<SlotAvailability> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.GET_AVAILABLE_SLOTS_BY_DATE)) {
            
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

  /**
   * Get Available Slots By Slot Id.
   *
   * @param slotId the slotId
   * @return the List<SlotAvailability>
   */
    @Override
    public List<SlotAvailability> getAvailableSlotsBySlotId(int slotId) {
        List<SlotAvailability> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.GET_AVAILABLE_SLOTS_BY_SLOT_ID)) {
            
            stmt.setInt(1, slotId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SlotAvailability sa = new SlotAvailability();
                sa.setId(rs.getInt("availabilityId"));
                sa.setSlotId(rs.getInt("slotId"));
                sa.setDate(rs.getDate("date").toLocalDate());
                sa.setSeatsAvailable(rs.getInt("seatsAvailable"));
                sa.setSeatsTotal(rs.getInt("seatsTotal"));
                sa.setAvailable(rs.getInt("seatsAvailable") > 0);
                list.add(sa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

  /**
   * Decrement Seats.
   *
   * @param availabilityId the availabilityId
   * @return the boolean
   */
    public boolean decrementSeats(int availabilityId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.DECREMENT_SEATS)) {
            
            stmt.setInt(1, availabilityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

  /**
   * Increment Seats.
   *
   * @param availabilityId the availabilityId
   * @return the boolean
   */
    public boolean incrementSeats(int availabilityId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SlotAvailabilityConstants.INCREMENT_SEATS)) {
            
            stmt.setInt(1, availabilityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}