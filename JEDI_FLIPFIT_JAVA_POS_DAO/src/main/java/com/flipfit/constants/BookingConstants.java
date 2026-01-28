package com.flipfit.constants;

/**
 * Constants for Booking DAO SQL Queries.
 */
public class BookingConstants {
    public static final String INSERT_BOOKING = "INSERT INTO Booking (userId, availabilityId, status, bookingDate, createdAt) VALUES (?, ?, ?, ?, ?)";
    public static final String CANCEL_BOOKING = "UPDATE Booking SET status = 'CANCELLED' WHERE bookingId = ?";
    public static final String GET_CUSTOMER_BOOKINGS = "SELECT * FROM Booking WHERE userId = ?";
    public static final String CHECK_BOOKING_STATUS = "SELECT status FROM Booking WHERE bookingId = ?";
    public static final String GET_ALL_BOOKINGS = "SELECT * FROM Booking";
    public static final String CONFIRM_WAITLIST_BOOKING = "UPDATE Booking SET status = 'CONFIRMED' WHERE bookingId = ?";
    public static final String GET_BOOKING_BY_ID = "SELECT * FROM Booking WHERE bookingId = ?";
}