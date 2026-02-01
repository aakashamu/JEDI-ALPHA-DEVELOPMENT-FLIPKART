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
 * The Class BookingService.
 *
 * @author Ananya
 * @ClassName  "BookingService"
 */
public class BookingService implements BookingInterface {
  /**
   * Create Booking.
   *
   * @param customerId the customerId
   * @param availabilityId the availabilityId
   * @return the Booking
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
   * Cancel Booking.
   *
   * @param bookingId the bookingId
   * @return true if successful, false otherwise
   */
    @Override
    public boolean cancelBooking(int bookingId) {
        System.out.println("\n========== CANCEL BOOKING ==========");
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        
        // 1. Get the booking to find out the status and availabilityId before cancelling
        com.flipfit.bean.Booking bookingToCancel = bookingDAO.getBookingById(bookingId);
        if (bookingToCancel == null) {
            System.out.println("ERROR: Booking not found with ID: " + bookingId);
            return false;
        }
        
        String originalStatus = bookingToCancel.getStatus();
        
        if ("CANCELLED".equalsIgnoreCase(originalStatus)) {
            System.out.println("ERROR: Booking " + bookingId + " is already CANCELLED.");
            return false;
        }
        
        int availabilityId = bookingToCancel.getAvailabilityId();
        
        // 2. Cancel the booking
        boolean success = bookingDAO.cancelBooking(bookingId);
        
        if (success) {
            // Update in-memory repository if needed
            Booking booking = FlipFitRepository.bookingsMap.get(bookingId);
            if (booking != null) {
                booking.setStatus("CANCELLED");
            }
            System.out.println("SUCCESS: Booking " + bookingId + " cancelled in database!");
            
            // 3. Handle Waitlist Promotion if the cancelled booking was CONFIRMED
            if ("CONFIRMED".equalsIgnoreCase(originalStatus)) {
                WaitListService waitListService = new WaitListService();
                boolean promoted = waitListService.promoteFromWaitList(availabilityId);
                
                if (!promoted) {
                    // No one on waitlist, increment available seats
                    com.flipfit.dao.SlotAvailabilityDAOImpl availabilityDAO = new com.flipfit.dao.SlotAvailabilityDAOImpl();
                    availabilityDAO.incrementSeats(availabilityId);
                    System.out.println("Slot availability incremented as no one was on waitlist.");
                } else {
                    System.out.println("Successfully promoted next person from waitlist into the vacated slot.");
                }
            } else if ("PENDING".equalsIgnoreCase(originalStatus)) {
                // If the booking was PENDING (on waitlist), remove from waitlist table
                com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
                waitlistDAO.removeFromWaitListByBookingId(bookingId);
                waitlistDAO.updateWaitlistPositions(availabilityId);
                System.out.println("Removed from waitlist as booking was pending.");
            }
            
            return true;
        } else {
            System.out.println("ERROR: Failed to cancel booking in database.");
            return false;
        }
    }
    /**
     * Get Customer Bookings.
     *
     * @param customerId the customerId
     * @return the List<Booking>
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
            customerBookingList.forEach(booking -> System.out.println("Booking ID: " + booking.getBookingId() +
                    " | Availability ID: " + booking.getAvailabilityId() +
                    " | Status: " + booking.getStatus() +
                    " | Created: " + booking.getCreatedAt()));
        }
        System.out.println("==========================================\n");

        return customerBookingList;
    }

    /**
     * Check Booking Status.
     *
     * @param bookingId the bookingId
     * @return true if successful, false otherwise
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
     * Get All Bookings.
     *
     * @return the List<Booking>
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(FlipFitRepository.allBookings);
    }

    /**
     * View All Bookings.
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
            allBookings.forEach(booking -> System.out.println("Booking ID: " + booking.getBookingId() +
                    " | Customer ID: " + booking.getCustomerId() +
                    " | Availability ID: " + booking.getAvailabilityId() +
                    " | Status: " + booking.getStatus()));
        }
        System.out.println("===========================\n");
    }

    /**
     * Get Next Booking Id.
     *
     * @return the next booking id
     */
    private static int getNextBookingId() {
        if (FlipFitRepository.bookingsMap.isEmpty()) {
            return 1001;
        }
        return FlipFitRepository.bookingsMap.keySet().stream().max(Integer::compare).orElse(1000) + 1;
    }
  /**
   * Booking Menu.
   *
   * @param scanner the scanner
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
