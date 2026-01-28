package com.flipfit.dao;

import com.flipfit.bean.GymOwner;
import com.flipfit.utils.DBConnection;
import java.sql.*;

public class GymOwnerDAOImpl implements GymOwnerDAO {

    @Override
    public void registerOwner(String fullName, String email, String password, Long phoneNumber, 
                             String city, String state, int pincode, String panCard, 
                             String aadhaarNumber, String gstin) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert into User table with roleId = 3 (Owner)
            String userQuery = "INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) VALUES (?, ?, ?, ?, ?, ?, ?, 3)";
            PreparedStatement userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, fullName);
            userStmt.setString(2, email);
            userStmt.setString(3, password);
            userStmt.setLong(4, phoneNumber);
            userStmt.setString(5, city);
            userStmt.setString(6, state);
            userStmt.setInt(7, pincode);
            userStmt.executeUpdate();

            ResultSet rs = userStmt.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) { 
                userId = rs.getInt(1); 
            }

            // Insert into GymOwner table
            String ownerQuery = "INSERT INTO GymOwner (userId, panCard, aadhaarNumber, gstin, isApproved) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement ownerStmt = conn.prepareStatement(ownerQuery);
            ownerStmt.setInt(1, userId);
            ownerStmt.setString(2, panCard);
            ownerStmt.setString(3, aadhaarNumber);
            ownerStmt.setString(4, gstin);
            ownerStmt.executeUpdate();

            conn.commit();
            System.out.println("Gym Owner registered successfully! Awaiting admin approval.");
        } catch (SQLException e) {
            try { 
                if(conn != null) conn.rollback(); 
            } catch (SQLException se) { 
                se.printStackTrace(); 
            }
            e.printStackTrace();
        } finally {
            try { 
                if(conn != null) conn.close(); 
            } catch (SQLException se) { 
                se.printStackTrace(); 
            }
        }
    }

    @Override
    public boolean isOwnerValid(String email, String password) {
        String query = "SELECT * FROM User WHERE email = ? AND password = ? AND roleId = 3";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
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
    public GymOwner getOwnerById(int userId) {
        GymOwner owner = new GymOwner();
        String query = "SELECT u.*, o.panCard, o.aadhaarNumber, o.gstin, o.isApproved " +
                      "FROM User u JOIN GymOwner o ON u.userId = o.userId WHERE u.userId = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                owner.setUserId(rs.getInt("userId"));
                owner.setFullName(rs.getString("fullName"));
                owner.setEmail(rs.getString("email"));
                owner.setPhoneNumber(rs.getLong("phoneNumber"));
                owner.setCity(rs.getString("city"));
                owner.setState(rs.getString("state"));
                owner.setPincode(rs.getInt("pincode"));
                owner.setRoleId(rs.getInt("roleId"));
                owner.setPAN(rs.getString("panCard"));
                owner.setAadhaar(rs.getString("aadhaarNumber"));
                owner.setGSTIN(rs.getString("gstin"));
                owner.setIsApproved(rs.getInt("isApproved"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }
}
