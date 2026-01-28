package com.flipfit.constants;

/**
 * Constants for Slot DAO SQL Queries.
 */
public class SlotConstants {
    public static final String ADD_SLOT = "INSERT INTO Slot (startTime, endTime, capacity, centreId) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_SLOT = "UPDATE Slot SET startTime = ?, endTime = ?, capacity = ?, centreId = ? WHERE slotId = ?";
    public static final String DELETE_SLOT = "DELETE FROM Slot WHERE slotId = ?";
    public static final String GET_SLOT_BY_ID = "SELECT * FROM Slot WHERE slotId = ?";
    public static final String GET_ALL_SLOTS = "SELECT * FROM Slot";
    public static final String GET_SLOTS_BY_CENTRE_ID = "SELECT * FROM Slot WHERE centreId = ?";
}