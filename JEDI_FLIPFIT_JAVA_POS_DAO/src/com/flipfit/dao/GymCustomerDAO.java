package com.flipfit.dao;

import com.flipfit.bean.User;
import com.flipfit.client.TestConnection; // Import the class that has the connection logic
import java.sql.*;

public class GymCustomerDAO implements GymCustomerInterfaceDAO {

    @Override
    public void registerCustomer(String fullName, String email, String password, Long phoneNumber, String city, String state, int pincode) {
        Connection conn = TestConnection.getConnection(); // Changed to TestConnection
        try {
            conn.setAutoCommit(false);
            String userQuery = "INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) VALUES (?, ?, ?, ?, ?, ?, ?, 2)";
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
            if (rs.next()) { userId = rs.getInt(1); }

            String customerQuery = "INSERT INTO GymCustomer (userId) VALUES (?)";
            PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
            customerStmt.setInt(1, userId);
            customerStmt.executeUpdate();

            conn.commit();
            System.out.println("Customer registered successfully!");
        } catch (SQLException e) {
            try { if(conn != null) conn.rollback(); } catch (SQLException se) { se.printStackTrace(); }
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUserValid(String email, String password) {
        String query = "SELECT * FROM User WHERE email = ? AND password = ? AND roleId = 2";
        // Fixed: Use TestConnection.getConnection() instead of DBConnection
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
    public User getCustomerById(int userId) {
        User user = new User();
        String query = "SELECT * FROM User WHERE userId = ?";
        // Fixed: Use TestConnection.getConnection() instead of DBConnection
        try (Connection conn = TestConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}