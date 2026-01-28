package com.flipfit.constants;

/**
 * Constants for Gym Customer DAO SQL Queries.
 */
public class GymCustomerConstants {
    public static final String REGISTER_USER = "INSERT INTO User (fullName, email, password, phoneNumber, city, state, pincode, roleId) VALUES (?, ?, ?, ?, ?, ?, ?, 2)";
    public static final String REGISTER_CUSTOMER = "INSERT INTO GymCustomer (userId) VALUES (?)";
    public static final String IS_USER_VALID = "SELECT * FROM User WHERE email = ? AND password = ? AND roleId = 2";
    public static final String GET_CUSTOMER_BY_ID = "SELECT * FROM User WHERE userId = ?";
}