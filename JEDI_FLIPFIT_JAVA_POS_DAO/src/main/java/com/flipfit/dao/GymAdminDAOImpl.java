package com.flipfit.dao;

import com.flipfit.constants.GymAdminConstants;
import com.flipfit.utils.DBConnection;
import java.sql.*;

/**
 * The Class GymAdminDAOImpl.
 *
 * @author Ananya
 * @ClassName  "GymAdminDAOImpl"
 */
public class GymAdminDAOImpl implements GymAdminDAO {

    @Override
    public void registerAdmin(String fullName, String email, String password, Long phoneNumber, 
                             String city, String state, int pincode) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            
            PreparedStatement userStmt = conn.prepareStatement(GymAdminConstants.REGISTER_ADMIN, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, fullName);
            userStmt.setString(2, email);
            userStmt.setString(3, password);
            userStmt.setLong(4, phoneNumber);
            userStmt.setString(5, city);
            userStmt.setString(6, state);
            userStmt.setInt(7, pincode);
            userStmt.executeUpdate();

            System.out.println("Gym Admin registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if(conn != null) conn.close(); 
            } catch (SQLException se) { 
                se.printStackTrace(); 
            }
        }
    }

  /**
   * Is Admin Valid.
   *
   * @param email the email
   * @param password the password
   * @return the boolean
   */
    @Override
    public boolean isAdminValid(String email, String password) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(GymAdminConstants.IS_ADMIN_VALID)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public java.util.List<com.flipfit.bean.GymOwner> getAllOwners() {
        java.util.List<com.flipfit.bean.GymOwner> owners = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(GymAdminConstants.GET_ALL_OWNERS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                com.flipfit.bean.GymOwner owner = new com.flipfit.bean.GymOwner();
                owner.setUserId(rs.getInt("userId"));
                owner.setFullName(rs.getString("fullName"));
                owner.setEmail(rs.getString("email"));
                owner.setPassword(rs.getString("password"));
                owner.setPhoneNumber(rs.getLong("phoneNumber"));
                owner.setCity(rs.getString("city"));
                owner.setState(rs.getString("state"));
                owner.setPincode(rs.getInt("pincode"));
                owner.setPAN(rs.getString("panCard"));
                owner.setAadhaar(rs.getString("aadhaarNumber"));
                owner.setGSTIN(rs.getString("gstin"));
                owner.setIsApproved(rs.getInt("isApproved"));
                owners.add(owner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
    }

  /**
   * Approve Owner.
   *
   * @param ownerId the ownerId
   * @return the boolean
   */
    @Override
    public boolean approveOwner(int ownerId) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(GymAdminConstants.APPROVE_OWNER)) {
            stmt.setInt(1, ownerId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

  /**
   * Delete Owner.
   *
   * @param ownerId the ownerId
   * @return the boolean
   */
    @Override
    public boolean deleteOwner(int ownerId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Delete from GymOwner table first
            PreparedStatement ownerStmt = conn.prepareStatement(GymAdminConstants.DELETE_OWNER_FROM_GYM_OWNER);
            ownerStmt.setInt(1, ownerId);
            ownerStmt.executeUpdate();
            
            // Delete from User table
            PreparedStatement userStmt = conn.prepareStatement(GymAdminConstants.DELETE_OWNER_FROM_USER);
            userStmt.setInt(1, ownerId);
            int rowsAffected = userStmt.executeUpdate();
            
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try { 
                if(conn != null) conn.rollback(); 
            } catch (SQLException se) { 
                se.printStackTrace(); 
            }
            e.printStackTrace();
            return false;
        } finally {
            try { 
                if(conn != null) conn.close(); 
            } catch (SQLException se) { 
                se.printStackTrace(); 
            }
        }
    }

    @Override
    public java.util.List<com.flipfit.bean.GymCustomer> getAllCustomers() {
        java.util.List<com.flipfit.bean.GymCustomer> customers = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(GymAdminConstants.GET_ALL_CUSTOMERS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                com.flipfit.bean.GymCustomer customer = new com.flipfit.bean.GymCustomer();
                customer.setUserId(rs.getInt("userId"));
                customer.setFullName(rs.getString("fullName"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getLong("phoneNumber"));
                customer.setCity(rs.getString("city"));
                customer.setState(rs.getString("state"));
                customer.setPincode(rs.getInt("pincode"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public java.util.List<com.flipfit.bean.Booking> getAllBookings() {
        java.util.List<com.flipfit.bean.Booking> bookings = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(GymAdminConstants.GET_ALL_BOOKINGS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                com.flipfit.bean.Booking booking = new com.flipfit.bean.Booking();
                booking.setBookingId(rs.getInt("bookingId"));
                booking.setCustomerId(rs.getInt("userId"));
                booking.setAvailabilityId(rs.getInt("availabilityId"));
                booking.setStatus(rs.getString("status"));
                booking.setBookingDate(rs.getDate("bookingDate"));
                booking.setCreatedAt(rs.getString("createdAt"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}