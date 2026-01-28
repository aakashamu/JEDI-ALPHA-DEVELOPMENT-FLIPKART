package com.flipfit.constants;

/**
 * Constants for Gym Owner DAO SQL Queries.
 */
public class GymOwnerConstants {
    public static final String REGISTER_USER = "INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) VALUES (?, ?, ?, ?, ?, ?, ?, 3)";
    public static final String REGISTER_OWNER = "INSERT INTO GymOwner (userId, panCard, aadhaarNumber, gstin, isApproved) VALUES (?, ?, ?, ?, 0)";
    public static final String IS_OWNER_VALID = "SELECT * FROM User WHERE email = ? AND password = ? AND roleId = 3";
    public static final String GET_OWNER_BY_ID = "SELECT u.*, o.panCard, o.aadhaarNumber, o.gstin, o.isApproved " +
                                                "FROM User u JOIN GymOwner o ON u.userId = o.userId WHERE u.userId = ?";
}