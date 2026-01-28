package com.flipfit.dao;

import com.flipfit.bean.User;
import com.flipfit.constants.UserConstants;
import com.flipfit.utils.DBConnection;
import java.sql.*;

/**
 * The Class UserDAO.
 *
 * @author Ananya
 * @ClassName  "UserDAO"
 */
public class UserDAO implements UserInterfaceDAO {
  /**
   * Login.
   *
   * @param email the email
   * @param password the password
   * @return the boolean
   */
    @Override
    public boolean login(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserConstants.LOGIN_QUERY)) {
            
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
   * Get User Details.
   *
   * @param email the email
   * @return the User
   */
    @Override
    public User getUserDetails(String email) {
        User user = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserConstants.GET_USER_DETAILS_QUERY)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
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

  /**
   * Update Profile.
   *
   * @param user the user
   */
    @Override
    public void updateProfile(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserConstants.UPDATE_PROFILE_QUERY)) {
            
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

  /**
   * Change Password.
   *
   * @param email the email
   * @param oldPassword the oldPassword
   * @param newPassword the newPassword
   * @return the boolean
   */
    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserConstants.CHANGE_PASSWORD_QUERY)) {
            
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            stmt.setString(3, oldPassword);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}