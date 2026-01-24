package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.Booking;
import java.util.ArrayList;
import java.util.List;

public class GymCustomerService implements GymCustomerInterface {

    // Static collections to store hard-coded data
    private static List<GymCentre> gymCentres = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();

    // Static block to initialize hard-coded data
    // just for testing
    static {
        GymCentre center1 = new GymCentre();
        center1.setCentreId(1);
        center1.setName("FlipFit Elite - Koramangala");
        center1.setCity("Bengaluru");
        center1.setApproved(true);
        
        GymCentre center2 = new GymCentre();
        center2.setCentreId(2);
        center2.setName("FlipFit Pro - Indiranagar");
        center2.setCity("Bengaluru");
        center2.setApproved(true);

        gymCentres.add(center1);
        gymCentres.add(center2);
    }

    @Override
    public List<GymCentre> viewCentres() {
        // Returns the list of all available gym centres
        return gymCentres;
    }

    @Override
    public List<Booking> viewBookedSlots() {
        // Returns the list of slots already booked
        return bookings;
    }

    @Override
    public Booking bookSlot(int slotAvailabilityId) {
        // Creates a new booking object and adds it to the collection
        Booking newBooking = new Booking();
        newBooking.setBookingId(bookings.size() + 1);
        newBooking.setAvailabilityId(slotAvailabilityId);
        newBooking.setStatus("CONFIRMED");
        
        bookings.add(newBooking);
        return newBooking;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        // Removes the booking from the collection if the ID matches
        return bookings.removeIf(b -> b.getBookingId() == bookingId);
    }

    @Override
    public void editDetails(String fullName, String email, long phoneNumber, String city, int pincode) {
        // Logic to update customer profile details
        System.out.println("Profile updated for: " + fullName);
    }
}