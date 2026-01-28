package com.flipfit.constants;

/**
 * Constants for Gym Admin DAO SQL Queries.
 */
public class GymAdminConstants {
    public static final String REGISTER_ADMIN = "INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
    public static final String IS_ADMIN_VALID = "SELECT * FROM User WHERE email = ? AND password = ? AND roleId = 1";
    public static final String GET_ALL_OWNERS = "SELECT u.*, o.panCard, o.aadhaarNumber, o.gstin, o.isApproved FROM User u JOIN GymOwner o ON u.userId = o.userId WHERE u.roleId = 3";
    public static final String APPROVE_OWNER = "UPDATE GymOwner SET isApproved = 1 WHERE userId = ?";
    public static final String DELETE_OWNER_FROM_GYM_OWNER = "DELETE FROM GymOwner WHERE userId = ?";
    public static final String DELETE_OWNER_FROM_USER = "DELETE FROM User WHERE userId = ?";
    public static final String GET_ALL_CUSTOMERS = "SELECT u.* FROM User u JOIN GymCustomer c ON u.userId = c.userId WHERE u.roleId = 2";
    public static final String GET_ALL_BOOKINGS = "SELECT * FROM Booking";
}