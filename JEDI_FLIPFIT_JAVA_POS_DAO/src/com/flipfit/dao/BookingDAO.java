package com.flipfit.dao;

import com.flipfit.bean.Booking;
import java.util.List;
/**
 * The Interface BookingDAO.
 *
 * @author Ananya
 * @ClassName  "BookingDAO"
 */
public interface BookingDAO {
    /**
     * Creates a new booking in the database
     * @param customerId ID of the customer
     * @param availabilityId ID of the slot availability
     * @return Created Booking object or null
     */
    Booking createBooking(int customerId, int availabilityId);

    /**
     * Cancels an existing booking
     * @param bookingId ID of the booking to cancel
     * @return true if cancellation was successful
     */
    boolean cancelBooking(int bookingId);

    /**
     * Retrieves all bookings for a specific customer
     * @param customerId ID of the customer
     * @return List of Booking objects
     */
    List<Booking> getCustomerBookings(int customerId);

    /**
     * Checks the status of a booking
     * @param bookingId ID of the booking
     * @return true if booking is active/confirmed
     */
    boolean checkBookingStatus(int bookingId);

    /**
     * Retrieves all bookings in the system
     * @return List of all bookings
     */
    List<Booking> getAllBookings();
}
