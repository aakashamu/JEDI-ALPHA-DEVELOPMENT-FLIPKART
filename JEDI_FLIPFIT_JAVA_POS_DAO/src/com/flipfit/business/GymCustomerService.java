package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Slot;
import com.flipfit.bean.SlotAvailability;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.GymCustomerDAO;
import java.util.ArrayList;
import java.util.List;

public class GymCustomerService implements GymCustomerInterface {

    private GymCustomerDAO customerDAO = new GymCustomerDAO();

    /**
     * Register a new customer - saves to database via DAO
     */
    public void registerCustomer(String fullName, String email, String password, Long phoneNumber, String city, String state, int pincode) {
        customerDAO.registerCustomer(fullName, email, password, phoneNumber, city, state, pincode);
    }

    private static String getCurrentUserEmail() {
        // Get the first logged-in user (simplified session management)
        // In a real implementation, you'd have proper session tracking
        return UserService.getCurrentLoggedInUser();
    }

    @Override
    public List<GymCentre> viewCentres() {
        // REQUIREMENT: Customers should only see APPROVED centres
        List<GymCentre> approvedCentres = FlipFitRepository.gymCentres.stream()
            .filter(GymCentre::isApproved)
            .toList();
        
        if (approvedCentres.isEmpty()) {
            System.out.println("No gym centres available");
            return new ArrayList<>();
        }
        
        System.out.println("\n========== ALL GYM CENTRES ==========");
        System.out.println("Total Centres: " + approvedCentres.size());
        System.out.println("-----------------------------------------");
        
        for (GymCentre centre : approvedCentres) {
            System.out.println("Centre ID: " + centre.getCentreId() +
                             " | Name: " + centre.getName() +
                             " | City: " + centre.getCity());
        }
        System.out.println("===================================\n");
        
        return new ArrayList<>(approvedCentres);
    }

    @Override
    public List<Booking> viewBookedSlots() {
        String currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail == null) {
            System.out.println("No customer is currently logged in");
            return new ArrayList<>();
        }
        
        GymCustomer currentCustomer = FlipFitRepository.customers.get(currentUserEmail);
        if (currentCustomer == null) {
            System.out.println("Customer profile not found");
            return new ArrayList<>();
        }
        
        return FlipFitRepository.customerBookings.getOrDefault(currentCustomer.getUserId(), new ArrayList<>());
    }

    @Override
    public Booking bookSlot(int slotAvailabilityId) {
        if (slotAvailabilityId <= 0) {
            System.out.println("ERROR: Invalid slot availability ID");
            return null;
        }
        
        String currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail == null) {
            System.out.println("No customer is currently logged in");
            return null;
        }
        
        GymCustomer currentCustomer = FlipFitRepository.customers.get(currentUserEmail);
        if (currentCustomer == null) {
            System.out.println("Customer profile not found");
            return null;
        }
        
        Slot slot = FlipFitRepository.slotMap.get(slotAvailabilityId);
        if (slot == null) {
            System.out.println("ERROR: Slot not found with ID: " + slotAvailabilityId);
            return null;
        }
        
        SlotAvailability availability = FlipFitRepository.slotAvailabilityMap.get(slotAvailabilityId);
        if (availability != null && !availability.isAvailable()) {
            System.out.println("ERROR: Slot is not available on " + availability.getDate());
            return null;
        }
        
        List<Booking> customerExistingBookings = FlipFitRepository.customerBookings.getOrDefault(currentCustomer.getUserId(), new ArrayList<>());
        
        List<Booking> conflictingBookings = new ArrayList<>();
        for (Booking existingBooking : customerExistingBookings) {
            if ("CONFIRMED".equals(existingBooking.getStatus())) {
                Slot existingSlot = FlipFitRepository.slotMap.get(existingBooking.getAvailabilityId());
                if (existingSlot != null && slotsOverlap(slot, existingSlot)) {
                    conflictingBookings.add(existingBooking);
                }
            }
        }
        
        if (!conflictingBookings.isEmpty()) {
            System.out.println("INFO: Found conflicting bookings. Removing old bookings...");
            for (Booking conflictingBooking : conflictingBookings) {
                removeBookingCompletely(conflictingBooking.getBookingId());
                System.out.println("Cancelled conflicting booking ID: " + conflictingBooking.getBookingId());
            }
        }
        
        List<Booking> slotExistingBookings = FlipFitRepository.slotBookings.getOrDefault(slotAvailabilityId, new ArrayList<>());
        long confirmedBookingsInSlot = slotExistingBookings.stream()
            .filter(booking -> "CONFIRMED".equals(booking.getStatus()))
            .count();
        
        if (confirmedBookingsInSlot >= slot.getCapacity()) {
            System.out.println("ERROR: Slot is full. Capacity: " + slot.getCapacity() + ", Booked: " + confirmedBookingsInSlot);
            System.out.println("Adding you to the waitlist...");
            
            WaitListService waitListService = new WaitListService();
            int tempBookingId = 9000 + (int)(System.currentTimeMillis() % 1000);
            waitListService.addToWaitList(tempBookingId);
            
            return null;
        }
        
        int maxId = FlipFitRepository.allBookings.stream()
            .mapToInt(Booking::getBookingId)
            .max()
            .orElse(1000);
        
        Booking newBooking = new Booking();
        newBooking.setBookingId(maxId + 1);
        newBooking.setCustomerId(currentCustomer.getUserId());
        newBooking.setAvailabilityId(slotAvailabilityId);
        newBooking.setStatus("CONFIRMED");
        newBooking.setBookingDate(new java.util.Date());
        newBooking.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        
        FlipFitRepository.allBookings.add(newBooking);
        FlipFitRepository.bookingsMap.put(newBooking.getBookingId(), newBooking);
        
        FlipFitRepository.customerBookings.computeIfAbsent(currentCustomer.getUserId(), k -> new ArrayList<>()).add(newBooking);
        
        FlipFitRepository.slotBookings.computeIfAbsent(slotAvailabilityId, k -> new ArrayList<>()).add(newBooking);
        
        System.out.println("✓ Booking successful! ID: " + newBooking.getBookingId());
        System.out.println("Slot: " + slot.getStartTime() + " - " + slot.getEndTime());
        System.out.println("Remaining capacity: " + (slot.getCapacity() - confirmedBookingsInSlot - 1) + "/" + slot.getCapacity());
        return newBooking;
    }
    
    /**
     * Check if two slots overlap in time - MUST be in same centre
     * REQUIREMENT: Prevent double booking of overlapping time slots
     */
    private boolean slotsOverlap(Slot slot1, Slot slot2) {
        boolean sameCentre = slot1.getCentreId() == slot2.getCentreId();
        boolean timeOverlap = !(slot1.getEndTime().isBefore(slot2.getStartTime()) || 
                                slot2.getEndTime().isBefore(slot1.getStartTime()));
        
        return sameCentre && timeOverlap;
    }
    
    /**
     * Completely remove a booking from all collections
     */
    private void removeBookingCompletely(int bookingId) {
        Booking booking = FlipFitRepository.bookingsMap.remove(bookingId);
        if (booking != null) {
            FlipFitRepository.allBookings.removeIf(b -> b.getBookingId() == bookingId);
            FlipFitRepository.customerBookings.values().forEach(bookings -> 
                bookings.removeIf(b -> b.getBookingId() == bookingId));
            FlipFitRepository.slotBookings.values().forEach(bookings ->
                bookings.removeIf(b -> b.getBookingId() == bookingId));
        }
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        if (bookingId <= 0) {
            System.out.println("ERROR: Invalid booking ID");
            return false;
        }
        
        Booking booking = FlipFitRepository.bookingsMap.get(bookingId);
        if (booking == null) {
            System.out.println("ERROR: Booking not found with ID: " + bookingId);
            return false;
        }
        
        if ("CANCELLED".equals(booking.getStatus())) {
            System.out.println("ERROR: Booking is already cancelled");
            return false;
        }
        
        booking.setStatus("CANCELLED");
        System.out.println("✓ Booking cancelled successfully! ID: " + bookingId);
        return true;
    }

    @Override
    public void editDetails(String fullName, String email, long phoneNumber, String city, int pincode) {
        // Input validation
        if (email == null || email.isEmpty()) {
            System.out.println("ERROR: Email cannot be empty");
            return;
        }
        
        if (fullName == null || fullName.isEmpty()) {
            System.out.println("ERROR: Full name cannot be empty");
            return;
        }
        
        if (phoneNumber <= 0 || String.valueOf(phoneNumber).length() < 10) {
            System.out.println("ERROR: Invalid phone number");
            return;
        }
        
        if (city == null || city.isEmpty()) {
            System.out.println("ERROR: City cannot be empty");
            return;
        }
        
        if (pincode <= 0 || String.valueOf(pincode).length() != 6) {
            System.out.println("ERROR: Invalid pincode (must be 6 digits)");
            return;
        }
        
        // Instant O(1) lookup using email as key
        GymCustomer customer = FlipFitRepository.customers.get(email);
        
        if (customer != null) {
            customer.setFullName(fullName);
            customer.setPhoneNumber(phoneNumber);
            customer.setCity(city);
            customer.setPincode(pincode);
            System.out.println("✓ Profile updated successfully for " + email);
        } else {
            System.out.println("ERROR: Customer profile not found. Please register first.");
        }
    }

    @Override
    public void viewProfile() {
        // Get current logged-in customer
        String currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail == null) {
            System.out.println("No customer is currently logged in");
            return;
        }
        
        GymCustomer customer = FlipFitRepository.customers.get(currentUserEmail);
        if (customer == null) {
            System.out.println("ERROR: Customer profile not found");
            return;
        }
        
        System.out.println("\n========== YOUR PROFILE ==========");
        System.out.println("User ID: " + customer.getUserId());
        System.out.println("Full Name: " + customer.getFullName());
        System.out.println("Email: " + currentUserEmail);
        System.out.println("Phone Number: " + customer.getPhoneNumber());
        System.out.println("City: " + customer.getCity());
        System.out.println("Pincode: " + customer.getPincode());
        System.out.println("==================================\n");
    }
}