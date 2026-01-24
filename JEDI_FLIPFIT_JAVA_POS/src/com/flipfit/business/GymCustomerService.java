package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCustomer;
import com.flipfit.dao.FlipFitRepository;
import java.util.List;

public class GymCustomerService implements GymCustomerInterface {

    @Override
    public List<GymCentre> viewCentres() {
        return FlipFitRepository.gymCentres;
    }

    @Override
    public List<Booking> viewBookedSlots() {
        return FlipFitRepository.bookings;
    }

    @Override
    public Booking bookSlot(int slotAvailabilityId) {
        Booking newBooking = new Booking();
        newBooking.setBookingId(FlipFitRepository.bookings.size() + 1);
        newBooking.setAvailabilityId(slotAvailabilityId);
        newBooking.setStatus("CONFIRMED");
        
        FlipFitRepository.bookings.add(newBooking);
        System.out.println("Booking successful! ID: " + newBooking.getBookingId());
        return newBooking;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        return FlipFitRepository.bookings.removeIf(b -> b.getBookingId() == bookingId);
    }

    @Override
    public void editDetails(String fullName, String email, long phoneNumber, String city, int pincode) {
        // Instant O(1) lookup using email as key
        GymCustomer customer = FlipFitRepository.customers.get(email);
        
        if (customer != null) {
            customer.setFullName(fullName);
            customer.setPhoneNumber(phoneNumber);
            customer.setCity(city);
            customer.setPincode(pincode);
            System.out.println("Profile updated successfully for " + email);
        } else {
            System.out.println("Customer profile not found. Please register first.");
        }
    }
}