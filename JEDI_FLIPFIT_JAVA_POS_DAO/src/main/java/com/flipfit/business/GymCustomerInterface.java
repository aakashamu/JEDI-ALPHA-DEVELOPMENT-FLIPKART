package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.Booking;
import java.util.List;

/**
 * The Interface GymCustomerInterface.
 *
 * @author Ananya
 * @ClassName "GymCustomerInterface"
 */
public interface GymCustomerInterface {

    public List<GymCentre> viewCentres(String email, String password);

    public List<Booking> viewBookedSlots(String email, String password);

    public Booking bookSlot(int slotAvailabilityId, String email, String password)
            throws com.flipfit.exception.SlotNotAvailableException, com.flipfit.exception.BookingNotDoneException;

    public boolean cancelBooking(int bookingId, String email, String password)
            throws com.flipfit.exception.BookingNotDoneException;

    public void editDetails(String fullName, String email, long phoneNumber, String city, int pincode, String password);

    public void viewProfile(String email, String password);
}