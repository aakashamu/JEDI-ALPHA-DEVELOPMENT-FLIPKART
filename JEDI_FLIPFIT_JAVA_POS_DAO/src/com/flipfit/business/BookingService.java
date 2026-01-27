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
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        Booking newBooking = bookingDAO.createBooking(customerId, availabilityId);
        
        if (newBooking != null) {
            // Also update in-memory repository for current session
            FlipFitRepository.allBookings.add(newBooking);
            FlipFitRepository.bookingsMap.put(newBooking.getBookingId(), newBooking);
            System.out.println("SUCCESS: Booking created successfully in database!");
            return newBooking;
        } else {
            System.out.println("ERROR: Failed to create booking in database.");
            return null;
        }
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
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        boolean success = bookingDAO.cancelBooking(bookingId);
        
        if (success) {
            // Update in-memory repository if needed
            Booking booking = FlipFitRepository.bookingsMap.get(bookingId);
            if (booking != null) {
                booking.setStatus("CANCELLED");
            }
            System.out.println("SUCCESS: Booking " + bookingId + " cancelled in database!");
            return true;
        } else {
            System.out.println("ERROR: Failed to cancel booking in database.");
            return false;
        }
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
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        List<Booking> customerBookingList = bookingDAO.getCustomerBookings(customerId);
        
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
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        boolean confirmed = bookingDAO.checkBookingStatus(bookingId);
        
        // To show details, we still need to load the booking
        List<Booking> all = bookingDAO.getAllBookings();
        Booking booking = all.stream().filter(b -> b.getBookingId() == bookingId).findFirst().orElse(null);
        
        if (booking == null) {
            System.out.println("ERROR: Booking not found with ID: " + bookingId);
            return false;
        }
        
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Customer ID: " + booking.getCustomerId());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("====================================\n");
        
        return confirmed;
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
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        List<Booking> allBookings = bookingDAO.getAllBookings();
        
        if (allBookings.isEmpty()) {
            System.out.println("No bookings available in the database.");
        } else {
            System.out.println("Total Bookings: " + allBookings.size());
            System.out.println("-----------------------------------------");
            for (Booking booking : allBookings) {
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
