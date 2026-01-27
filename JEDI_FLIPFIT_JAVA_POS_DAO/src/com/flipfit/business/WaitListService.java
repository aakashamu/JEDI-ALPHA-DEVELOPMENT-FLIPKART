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
 * Service class for managing gym slot wait list
 * Uses DAO (FlipFitRepository) for centralized data management
 */
public class WaitListService implements WaitListInterface {
    
    /**
     * Add a booking to the wait list
     * Uses FIFO order via Queue
     * 
     * @param bookingId Booking ID to add to wait list
     * @return true if successfully added, false otherwise
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
     * Remove a booking from the wait list
     * 
     * @param bookingId Booking ID to remove from wait list
     */
    @Override
    public void removeFromWaitList(int bookingId) {
        System.out.println("\n========== REMOVE FROM WAIT LIST ==========");
        
        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        waitlistDAO.removeFromWaitList(bookingId);
        
        System.out.println("SUCCESS: Booking " + bookingId + " removed from wait list in database");
    }
    
    /**
     * Update positions in wait list after removal
     */
    private void updatePositions() {
        int position = 1;
        for (WaitListEntry entry : FlipFitRepository.allWaitListEntries) {
            entry.setPosition(position++);
        }
    }
    
    /**
     * Update wait list when a slot becomes available
     * Automatically processes the first person in queue
     * 
     * @return true if update successful, false otherwise
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
     * View all wait list entries
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
            
            for (WaitListEntry entry : entries) {
                System.out.println("Position " + entry.getPosition() + 
                                 " | Created: " + entry.getCreatedAt());
            }
        }
        System.out.println("====================================\n");
    }
    
    /**
     * Get wait list position for a booking
     * 
     * @param bookingId Booking ID to check
     * @return position in wait list, or -1 if not found
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
     * Get count of people waiting
     * 
     * @return total wait list count
     */
    public int getWaitListCount() {
        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        return waitlistDAO.getAllWaitListEntries().size();
    }
    
    /**
     * Menu-based wait list operations
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
