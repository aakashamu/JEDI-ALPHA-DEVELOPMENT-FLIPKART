package com.flipfit.constants;

/**
 * Constants for Waitlist DAO SQL Queries.
 */
public class WaitlistConstants {
    public static final String GET_WAITLIST_COUNT = "SELECT COUNT(*) FROM Waitlist";
    public static final String INSERT_WAITLIST_ENTRY = "INSERT INTO Waitlist (bookingId, position, createdAt) VALUES (?, ?, ?)";
    public static final String GET_WAITLIST_COUNT_BY_AVAILABILITY = "SELECT COUNT(*) as cnt FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ?";
    public static final String GET_WAITLIST_COUNT_FOR_PENDING = "SELECT COUNT(*) as cnt FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ? AND b.status = 'PENDING'";
    public static final String GET_FIRST_PENDING_ENTRY = "SELECT w.*, b.userId, b.availabilityId FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ? AND b.status = 'PENDING' ORDER BY w.position ASC LIMIT 1";
    public static final String UPDATE_WAITLIST_STATUS = "UPDATE Waitlist SET status = ? WHERE waitlistId = ?";
    public static final String DELETE_WAITLIST_ENTRY = "DELETE FROM Waitlist WHERE bookingId = ?";
    public static final String SELECT_WAITLIST_IDS_BY_TIME = "SELECT waitlistId FROM Waitlist ORDER BY createdAt ASC";
    public static final String UPDATE_WAITLIST_POSITION = "UPDATE Waitlist SET position = ? WHERE waitlistId = ?";
    public static final String SELECT_FIRST_ENTRY_FOR_UPDATE = "SELECT bookingId FROM Waitlist ORDER BY position ASC LIMIT 1";
    public static final String GET_ALL_WAITLIST_ENTRIES = "SELECT * FROM Waitlist ORDER BY position ASC";
    public static final String GET_WAITLIST_POSITION = "SELECT position FROM Waitlist WHERE bookingId = ?";
    public static final String SELECT_WAITLIST_IDS_BY_AVAILABILITY = "SELECT w.waitlistId FROM Waitlist w JOIN Booking b ON w.bookingId = b.bookingId WHERE b.availabilityId = ? AND b.status = 'PENDING' ORDER BY w.position ASC";
}