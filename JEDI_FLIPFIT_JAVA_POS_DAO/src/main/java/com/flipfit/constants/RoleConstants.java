package com.flipfit.constants;

/**
 * Constants for Role DAO SQL Queries.
 */
public class RoleConstants {
    public static final String ADD_ROLE = "INSERT INTO Role (roleName) VALUES (?)";
    public static final String GET_ROLE_BY_ID = "SELECT * FROM Role WHERE roleId = ?";
    public static final String GET_ROLE_BY_NAME = "SELECT * FROM Role WHERE roleName = ?";
    public static final String GET_ALL_ROLES = "SELECT * FROM Role";
    public static final String UPDATE_ROLE = "UPDATE Role SET roleName = ? WHERE roleId = ?";
    public static final String DELETE_ROLE = "DELETE FROM Role WHERE roleId = ?";
}