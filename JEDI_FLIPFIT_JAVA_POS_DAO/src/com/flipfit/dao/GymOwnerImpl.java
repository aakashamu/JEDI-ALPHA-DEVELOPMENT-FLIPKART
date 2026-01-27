package com.flipfit.dao;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymOwnerImpl implements GymOwnerInterface {

    @Override
    public boolean registerNewCentre(GymCentre centre) {
        String query = "INSERT INTO GymCentre (centreName, city, state, ownerId, isApproved) VALUES (?, ?, ?, ?, ?)";
        // Use TestConnection.getConnection() instead of DriverManager
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return false;
            
            stmt.setString(1, centre.getName());
            stmt.setString(2, centre.getCity());
            stmt.setString(3, centre.getState());
            stmt.setInt(4, centre.getOwnerId());
            stmt.setInt(5, 0); // Default to Pending (0)
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<GymCentre> viewMyCentres(int ownerId) {
        List<GymCentre> centres = new ArrayList<>();
        String query = "SELECT * FROM GymCentre WHERE ownerId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return centres;
            
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                GymCentre centre = new GymCentre();
                centre.setCentreId(rs.getInt("centreId"));
                centre.setName(rs.getString("centreName"));
                centre.setCity(rs.getString("city"));
                centre.setState(rs.getString("state")); // Included based on schema
                centre.setApproved(rs.getInt("isApproved") == 1);
                centres.add(centre);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return centres;
    }

    @Override
    public void requestApproval(int gymOwnerId) {
        String query = "UPDATE GymOwner SET isApproved = 0 WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn != null) {
                stmt.setInt(1, gymOwnerId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    @Override
    public List<GymCustomer> viewCustomers(int gymCentreId) {
        List<GymCustomer> customers = new ArrayList<>();
        // Query joins GymCustomer with Booking and User to get names
        String query = "SELECT u.userId, u.fullName FROM User u " +
                       "JOIN GymCustomer gc ON u.userId = gc.userId " +
                       "JOIN Booking b ON gc.userId = b.userId " +
                       "JOIN SlotAvailability sa ON b.availabilityId = sa.availabilityId " +
                       "JOIN Slot s ON sa.slotId = s.slotId " +
                       "WHERE s.centreId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return customers;
            
            stmt.setInt(1, gymCentreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GymCustomer customer = new GymCustomer();
                customer.setUserId(rs.getInt("userId"));
                customer.setFullName(rs.getString("fullName"));
                customers.add(customer);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return customers;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        String query = "UPDATE Booking SET status = 'CANCELLED' WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return false;
            
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}