package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.Booking;
import java.util.List;
/**
 * The Interface GymCustomerInterface.
 *
 * @author Ananya
 * @ClassName  "GymCustomerInterface"
 */
public interface GymCustomerInterface {

    /**
     * Retrieves a list of all available gym centres.
     */
    public List<GymCentre> viewCentres();

    /**
     * Retrieves a list of slots already booked by the customer.
     */
    public List<Booking> viewBookedSlots();

    /**
     * Books a specific slot based on the availability ID.
     */
    public Booking bookSlot(int slotAvailabilityId)
            throws com.flipfit.exception.SlotNotAvailableException, com.flipfit.exception.BookingNotDoneException;

    /**
     * Cancels an existing booking.
     */
    public boolean cancelBooking(int bookingId) throws com.flipfit.exception.BookingNotDoneException;

    /**
     * Updates the customer's personal profile information.
     */
    public void editDetails(String fullName, String email, long phoneNumber, String city, int pincode);

    /**
     * Displays the customer's current profile information.
     */
    public void viewProfile();

    /**
     * Lists all available slots for a given gym centre.
     */
    public void viewAvailableSlots(int centreId);
}