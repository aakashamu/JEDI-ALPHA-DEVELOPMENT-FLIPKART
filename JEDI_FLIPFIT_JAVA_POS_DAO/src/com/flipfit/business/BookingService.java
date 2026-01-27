/**
 * BookingService - Implementation with Collection API using DAO
 */
package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.dao.FlipFitRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service class for managing gym slot bookings
 * Uses DAO (FlipFitRepository) for centralized data management
 */
public class BookingService implements BookingInterface {
    
    /**
     * Create a new booking for a customer
     * 
     * @param customerId Customer ID
     * @param availabilityId Slot Availability ID
     * @return Created Booking object or null if creation fails
     */
    @Override
    public Booking createBooking(int customerId, int availabilityId) {
        System.out.println("\n========== CREATE BOOKING ==========");
        
        // Validate customer ID
        if (customerId <= 0) {
            System.out.println("ERROR: Invalid customer ID");
            return null;
        }
        
        // Validate availability ID
        if (availabilityId <= 0) {
            System.out.println("ERROR: Invalid availability ID");
            return null;
        }
        
        // Create new booking
        int newBookingId = getNextBookingId();
        Booking newBooking = new Booking();
        newBooking.setBookingId(newBookingId);
        newBooking.setCustomerId(customerId);
        newBooking.setAvailabilityId(availabilityId);
        newBooking.setStatus("CONFIRMED");
        newBooking.setBookingDate(new Date());
        newBooking.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        // Add to DAO collections
        FlipFitRepository.bookingsMap.put(newBookingId, newBooking);
        FlipFitRepository.allBookings.add(newBooking);
        
        // Add to customer's booking list
        if (!FlipFitRepository.customerBookings.containsKey(customerId)) {
            FlipFitRepository.customerBookings.put(customerId, new ArrayList<>());
        }
        FlipFitRepository.customerBookings.get(customerId).add(newBooking);
        
        System.out.println("SUCCESS: Booking created successfully!");
        System.out.println("Booking ID: " + newBookingId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Status: CONFIRMED");
        System.out.println("====================================\n");
        
        return newBooking;
    }
    
    /**
     * Cancel an existing booking
     * 
     * @param bookingId Booking ID to cancel
     * @return true if cancellation successful, false otherwise
     */
    @Override
    public boolean cancelBooking(int bookingId) {
        System.out.println("\n========== CANCEL BOOKING ==========");
        
        // Check if booking exists in Map
        if (!FlipFitRepository.bookingsMap.containsKey(bookingId)) {
            System.out.println("ERROR: Booking not found with ID: " + bookingId);
            return false;
        }
        
        Booking booking = FlipFitRepository.bookingsMap.get(bookingId);
        
        // Cannot cancel if already cancelled
        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            System.out.println("ERROR: Booking is already cancelled");
            return false;
        }
        
        // Update booking status
        booking.setStatus("CANCELLED");
        
        // Remove from list
        FlipFitRepository.allBookings.remove(booking);
        
        // Remove from customer's booking list
        if (FlipFitRepository.customerBookings.containsKey(booking.getCustomerId())) {
            FlipFitRepository.customerBookings.get(booking.getCustomerId()).remove(booking);
        }
        
        System.out.println("SUCCESS: Booking cancelled successfully!");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Status: CANCELLED");
        System.out.println("====================================\n");
        
        return true;
    }
    
    /**
     * Get all bookings for a specific customer
     * 
     * @param customerId Customer ID
     * @return List of bookings for the customer
     */
    @Override
    public List<Booking> getCustomerBookings(int customerId) {
        System.out.println("\n====== CUSTOMER BOOKING HISTORY ======");
        
        List<Booking> customerBookingList = FlipFitRepository.customerBookings.getOrDefault(customerId, new ArrayList<>());
        
        if (customerBookingList.isEmpty()) {
            System.out.println("No bookings found for Customer ID: " + customerId);
        } else {
            System.out.println("Total Bookings: " + customerBookingList.size());
            System.out.println("-----------------------------------------");
            for (Booking booking : customerBookingList) {
                System.out.println("Booking ID: " + booking.getBookingId() +
                                 " | Availability ID: " + booking.getAvailabilityId() +
                                 " | Status: " + booking.getStatus() +
                                 " | Created: " + booking.getCreatedAt());
            }
        }
        System.out.println("==========================================\n");
        
        return customerBookingList;
    }
    
    /**
     * Check the status of a specific booking
     * 
     * @param bookingId Booking ID to check
     * @return true if booking exists and is confirmed, false otherwise
     */
    @Override
    public boolean checkBookingStatus(int bookingId) {
        System.out.println("\n====== CHECK BOOKING STATUS ======");
        
        if (!FlipFitRepository.bookingsMap.containsKey(bookingId)) {
            System.out.println("ERROR: Booking not found with ID: " + bookingId);
            return false;
        }
        
        Booking booking = FlipFitRepository.bookingsMap.get(bookingId);
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Customer ID: " + booking.getCustomerId());
        System.out.println("Availability ID: " + booking.getAvailabilityId());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("Created At: " + booking.getCreatedAt());
        System.out.println("====================================\n");
        
        return "CONFIRMED".equalsIgnoreCase(booking.getStatus());
    }
    
    /**
     * Get all bookings
     * 
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(FlipFitRepository.allBookings);
    }
    
    /**
     * View all bookings (utility method)
     */
    public void viewAllBookings() {
        System.out.println("\n====== ALL BOOKINGS ======");
        
        if (FlipFitRepository.allBookings.isEmpty()) {
            System.out.println("No bookings available");
        } else {
            System.out.println("Total Bookings: " + FlipFitRepository.allBookings.size());
            System.out.println("-----------------------------------------");
            for (Booking booking : FlipFitRepository.allBookings) {
                System.out.println("Booking ID: " + booking.getBookingId() +
                                 " | Customer ID: " + booking.getCustomerId() +
                                 " | Availability ID: " + booking.getAvailabilityId() +
                                 " | Status: " + booking.getStatus());
            }
        }
        System.out.println("===========================\n");
    }
    
    /**
     * Generate next booking ID
     */
    private static int getNextBookingId() {
        if (FlipFitRepository.bookingsMap.isEmpty()) {
            return 1001;
        }
        return FlipFitRepository.bookingsMap.keySet().stream().max(Integer::compare).orElse(1000) + 1;
    }
    
    /**
     * Menu-based booking operations
     */
    public static void bookingMenu(java.util.Scanner scanner) {
        BookingService bookingService = new BookingService();
        boolean continueBooking = true;
        
        while (continueBooking) {
            System.out.println("\n========== BOOKING MANAGEMENT ==========");
            System.out.println("1. Create New Booking");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Customer Bookings");
            System.out.println("4. Check Booking Status");
            System.out.println("5. View All Bookings");
            System.out.println("0. Exit Booking Menu");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    int custId = scanner.nextInt();
                    System.out.print("Enter Availability ID: ");
                    int availId = scanner.nextInt();
                    bookingService.createBooking(custId, availId);
                    break;
                    
                case 2:
                    System.out.print("Enter Booking ID to cancel: ");
                    int bookId = scanner.nextInt();
                    if (bookingService.cancelBooking(bookId)) {
                        System.out.println("Booking cancelled successfully");
                    } else {
                        System.out.println("Failed to cancel booking");
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Customer ID: ");
                    int cId = scanner.nextInt();
                    bookingService.getCustomerBookings(cId);
                    break;
                    
                case 4:
                    System.out.print("Enter Booking ID: ");
                    int bId = scanner.nextInt();
                    bookingService.checkBookingStatus(bId);
                    break;
                    
                case 5:
                    bookingService.viewAllBookings();
                    break;
                    
                case 0:
                    continueBooking = false;
                    System.out.println("Exiting Booking Menu...");
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
