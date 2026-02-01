/**
 * WaitListService - Implementation with Collection API using DAO
 */
package com.flipfit.business;

import com.flipfit.bean.WaitListEntry;
import com.flipfit.dao.FlipFitRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Class WaitListService.
 *
 * @author Ananya
 * @ClassName "WaitListService"
 */
public class WaitListService implements WaitListInterface {
  /**
   * Add To Wait List.
   *
   * @param bookingId the bookingId
   * @return true if successful, false otherwise
   */
    @Override
    public boolean addToWaitList(int bookingId) {
        System.out.println("\n========== ADD TO WAIT LIST ==========");
        
        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        boolean success = waitlistDAO.addToWaitList(bookingId);
        
        if (success) {
            System.out.println("SUCCESS: Booking " + bookingId + " added to wait list in database");
            return true;
        } else {
            System.out.println("ERROR: Failed to add booking " + bookingId + " to wait list");
            return false;
        }
    }
  /**
   * Remove From Wait List.
   *
   * @param bookingId the bookingId
   */
    @Override
    public void removeFromWaitList(int bookingId) {
        System.out.println("\n========== REMOVE FROM WAIT LIST ==========");
        
        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        waitlistDAO.removeFromWaitList(bookingId);
        
        System.out.println("SUCCESS: Booking " + bookingId + " removed from wait list in database");
    }
  /**
   * Update Positions.
   *
   */
    private void updatePositions() {
        java.util.concurrent.atomic.AtomicInteger position = new java.util.concurrent.atomic.AtomicInteger(1);
        FlipFitRepository.allWaitListEntries.forEach(entry -> entry.setPosition(position.getAndIncrement()));
    }

    /**
     * Update wait list.
     *
     * @return true, if successful
     */
    @Override
    public boolean updateWaitList() {
        System.out.println("\n========== UPDATE WAIT LIST ==========");

        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        boolean success = waitlistDAO.updateWaitList();

        if (success) {
            System.out.println("Wait list updated successfully in database");
            return true;
        } else {
            System.out.println("Wait list is empty or update failed");
            return false;
        }
    }

    /**
     * Promote from wait list.
     *
     * @param availabilityId the availability id
     * @return true, if successful
     */
    @Override
    public boolean promoteFromWaitList(int availabilityId) {
        System.out.println("\n========== PROMOTE FROM WAIT LIST ==========");

        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();

        // 1. Get the first pending entry for this availability
        WaitListEntry entry = waitlistDAO.getFirstPendingWaitlistEntry(availabilityId);

        if (entry != null) {
            System.out.println("Found candidate for promotion: Booking ID " + entry.getBookingId());

            // 2. Confirm the booking
            if (bookingDAO.confirmWaitlistBooking(entry.getBookingId())) {
                System.out.println("SUCCESS: Booking " + entry.getBookingId() + " confirmed!");

                // 3. Remove from waitlist
                waitlistDAO.removeFromWaitListByBookingId(entry.getBookingId());

                // 4. Update remaining positions
                waitlistDAO.updateWaitlistPositions(availabilityId);

                return true;
            } else {
                System.out.println("ERROR: Failed to confirm waitlisted booking " + entry.getBookingId());
            }
        } else {
            System.out.println("No one on waitlist for Availability ID: " + availabilityId);
        }

        return false;
    }

    /**
     * View wait list.
     */
    public void viewWaitList() {
        System.out.println("\n========== WAIT LIST VIEW ==========");

        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        List<WaitListEntry> entries = waitlistDAO.getAllWaitListEntries();

        if (entries.isEmpty()) {
            System.out.println("Wait list is empty");
        } else {
            System.out.println("Total Entries: " + entries.size());
            System.out.println("-----------------------------------------");

            entries.forEach(entry -> System.out
                    .println("Position " + entry.getPosition() + " | Created: " + entry.getCreatedAt()));
        }
        System.out.println("====================================\n");
    }
    /**
     * Gets the wait list position.
     *
     * @param bookingId the booking id
     * @return the wait list position
     */
    public int getWaitListPosition(int bookingId) {
        System.out.println("\n========== CHECK WAIT LIST POSITION ==========");

        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        int position = waitlistDAO.getWaitListPosition(bookingId);

        if (position > 0) {
            System.out.println("Booking " + bookingId + " is at position " + position + " in wait list");
        } else {
            System.out.println("Booking " + bookingId + " is not in wait list");
        }
        System.out.println("=======================================\n");

        return position;
    }

    /**
     * Gets the wait list count.
     *
     * @return the wait list count
     */
    public int getWaitListCount() {
        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        return waitlistDAO.getAllWaitListEntries().size();
    }

    /**
     * Wait list menu.
     *
     * @param scanner the scanner
     */
    public static void waitListMenu(Scanner scanner) {
        WaitListService waitListService = new WaitListService();
        boolean continueWaitList = true;

        while (continueWaitList) {
            System.out.println("\n========== WAIT LIST MANAGEMENT ==========");
            System.out.println("1. Add Booking to Wait List");
            System.out.println("2. Remove Booking from Wait List");
            System.out.println("3. Check Wait List Position");
            System.out.println("4. View Wait List");
            System.out.println("5. Update Wait List (Process Next Entry)");
            System.out.println("6. Check Wait List Count");
            System.out.println("0. Exit Wait List Menu");
            System.out.println("==========================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Booking ID to add to wait list: ");
                    int bookId = scanner.nextInt();
                    if (waitListService.addToWaitList(bookId)) {
                        System.out.println("Added successfully");
                    } else {
                        System.out.println("Failed to add to wait list");
                    }
                    break;

                case 2:
                    System.out.print("Enter Booking ID to remove from wait list: ");
                    int removeBookId = scanner.nextInt();
                    waitListService.removeFromWaitList(removeBookId);
                    break;

                case 3:
                    System.out.print("Enter Booking ID to check position: ");
                    int checkBookId = scanner.nextInt();
                    int position = waitListService.getWaitListPosition(checkBookId);
                    if (position > 0) {
                        System.out.println("Position: " + position);
                    }
                    break;

                case 4:
                    waitListService.viewWaitList();
                    break;

                case 5:
                    if (waitListService.updateWaitList()) {
                        System.out.println("Wait list updated successfully");
                    } else {
                        System.out.println("Failed to update wait list");
                    }
                    break;

                case 6:
                    int count = waitListService.getWaitListCount();
                    System.out.println("Total entries in wait list: " + count);
                    break;

                case 0:
                    continueWaitList = false;
                    System.out.println("Exiting Wait List Menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
