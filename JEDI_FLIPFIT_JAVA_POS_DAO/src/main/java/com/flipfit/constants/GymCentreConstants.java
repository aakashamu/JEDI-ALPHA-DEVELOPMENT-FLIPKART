package com.flipfit.constants;

/**
 * Constants for Gym Centre DAO SQL Queries.
 *
 * @author Ananya
 * @ClassName "GymCentreConstants"
 */
public class GymCentreConstants {
    public static final String INSERT_GYM_CENTRE = "INSERT INTO GymCentre (centreName, city, state, ownerId, isApproved) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_GYM_CENTRES = "SELECT * FROM GymCentre";
    public static final String UPDATE_GYM_CENTRE_APPROVAL = "UPDATE GymCentre SET isApproved = ? WHERE centreId = ?";
    public static final String DELETE_GYM_CENTRE = "DELETE FROM GymCentre WHERE centreId = ?";
    public static final String SELECT_GYM_CENTRES_BY_OWNER = "SELECT * FROM GymCentre WHERE ownerId = ?";
}