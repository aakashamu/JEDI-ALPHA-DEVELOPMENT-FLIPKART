package com.flipfit.dao;

import com.flipfit.bean.WaitListEntry;
import java.util.List;

public interface WaitlistDAO {
    /**
     * Adds a booking to the waitlist
     * @param bookingId ID of the booking
     * @return true if added successfully
     */
    boolean addToWaitList(int bookingId);

    /**
     * Removes a booking from the waitlist
     * @param bookingId ID of the booking
     */
    void removeFromWaitList(int bookingId);

    /**
     * Processes the waitlist and updates statuses when a slot becomes available
     * @return true if update was successful
     */
    boolean updateWaitList();

    /**
     * Retrieves all entries currently in the waitlist
     * @return List of WaitListEntry
     */
    List<WaitListEntry> getAllWaitListEntries();

    /**
     * Gets the position of a booking in the waitlist
     * @param bookingId ID of the booking
     * @return Position as an integer
     */
    int getWaitListPosition(int bookingId);
}
