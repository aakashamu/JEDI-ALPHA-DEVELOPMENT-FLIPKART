package com.flipfit.dao;

import com.flipfit.bean.User;
import com.flipfit.constants.GymCustomerConstants;
import com.flipfit.utils.DBConnection;
import java.sql.*;

/**
 * The Class GymCustomerDAO.
 *
 * @author Ananya
 * @ClassName  "GymCustomerDAO"
 */
public class GymCustomerDAO implements GymCustomerInterfaceDAO {
  /**
   * Register Customer.
   *
   * @param fullName the fullName
   * @param email the email
   * @param password the password
   * @param phoneNumber the phoneNumber
   * @param city the city
   * @param state the state
   * @param pincode the pincode
   */
    @Override
    public void registerCustomer(String fullName, String email, String password, Long phoneNumber, String city, String state, int pincode) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            PreparedStatement userStmt = conn.prepareStatement(GymCustomerConstants.REGISTER_USER, Statement.RETURN_GENERATED_KEYS);
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

            PreparedStatement customerStmt = conn.prepareStatement(GymCustomerConstants.REGISTER_CUSTOMER);
            customerStmt.setInt(1, userId);
            customerStmt.executeUpdate();

            conn.commit();
            System.out.println("Customer registered successfully!");
        } catch (SQLException e) {
            try { if(conn != null) conn.rollback(); } catch (SQLException se) { se.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch (SQLException se) { se.printStackTrace(); }
        }
    }
    
  /**
   * Is User Valid.
   *
   * @param email the email
   * @param password the password
   * @return true if successful, false otherwise
   */
    @Override
    public boolean isUserValid(String email, String password) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(GymCustomerConstants.IS_USER_VALID)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
  /**
   * Get Customer By Id.
   *
   * @param userId the userId
   * @return the User
   */
    @Override
    public User getCustomerById(int userId) {
        User user = new User();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(GymCustomerConstants.GET_CUSTOMER_BY_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
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
}