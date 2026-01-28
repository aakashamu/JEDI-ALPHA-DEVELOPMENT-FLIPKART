package com.flipfit.constants;

/**
 * Constants for User DAO SQL Queries.
 */
public class UserConstants {
    public static final String LOGIN_QUERY = "SELECT * FROM User WHERE email = ? AND password = ?";
    public static final String GET_USER_DETAILS_QUERY = "SELECT * FROM User WHERE email = ?";
    public static final String UPDATE_PROFILE_QUERY = "UPDATE User SET fullName = ?, phoneNumber = ?, city = ?, state = ?, pincode = ? WHERE email = ?";
    public static final String CHANGE_PASSWORD_QUERY = "UPDATE User SET password = ? WHERE email = ? AND password = ?";
}