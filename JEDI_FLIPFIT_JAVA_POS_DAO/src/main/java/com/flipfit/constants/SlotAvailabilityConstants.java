package com.flipfit.constants;

/**
 * Constants for Slot Availability DAO SQL Queries.
 */
public class SlotAvailabilityConstants {
    public static final String ADD_SLOT_AVAILABILITY = "INSERT INTO SlotAvailability (slotId, date, seatsTotal, seatsAvailable) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_SLOT_AVAILABILITY = "UPDATE SlotAvailability SET slotId = ?, date = ?, seatsAvailable = ? WHERE availabilityId = ?";
    public static final String DELETE_SLOT_AVAILABILITY = "DELETE FROM SlotAvailability WHERE availabilityId = ?";
    public static final String GET_SLOT_AVAILABILITY_BY_ID = "SELECT * FROM SlotAvailability WHERE availabilityId = ?";
    public static final String GET_ALL_SLOT_AVAILABILITIES = "SELECT * FROM SlotAvailability";
    public static final String GET_AVAILABLE_SLOTS_BY_DATE = "SELECT * FROM SlotAvailability WHERE date = ? AND seatsAvailable > 0";
    public static final String GET_AVAILABLE_SLOTS_BY_SLOT_ID = "SELECT * FROM SlotAvailability WHERE slotId = ?";
    public static final String DECREMENT_SEATS = "UPDATE SlotAvailability SET seatsAvailable = seatsAvailable - 1 WHERE availabilityId = ?";
    public static final String INCREMENT_SEATS = "UPDATE SlotAvailability SET seatsAvailable = seatsAvailable + 1 WHERE availabilityId = ?";
}