package com.flipfit.dao;

import com.flipfit.bean.User;
import com.flipfit.client.TestConnection;
import java.sql.*;

public class UserDAO implements UserInterfaceDAO {

    @Override
    public boolean login(String email, String password) {
        // This checks the User table for a matching email and password
        String query = "SELECT * FROM User WHERE email = ? AND password = ?";
        try (Connection conn = TestConnection.getConnection();
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
    public User getUserDetails(String email) {
        User user = null;
        String query = "SELECT * FROM User WHERE email = ?";
        try (Connection conn = TestConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                // Matching your Bean setters to your DB column names
                user.setUserId(rs.getInt("userId"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getLong("phoneNumber"));
                user.setCity(rs.getString("city"));
                user.setState(rs.getString("state"));
                user.setPincode(rs.getInt("pincode"));
                user.setRoleId(rs.getInt("roleId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateProfile(User user) {
        // Updating common fields based on the email (unique identifier)
        String query = "UPDATE User SET fullName = ?, phoneNumber = ?, city = ?, state = ?, pincode = ? WHERE email = ?";
        try (Connection conn = TestConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getFullName());
            stmt.setLong(2, user.getPhoneNumber());
            stmt.setString(3, user.getCity());
            stmt.setString(4, user.getState());
            stmt.setInt(5, user.getPincode());
            stmt.setString(6, user.getEmail());
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User profile updated successfully in FlipFitDB!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}