package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.bean.SlotAvailability;
import com.flipfit.bean.Booking;
import com.flipfit.bean.WaitListEntry;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.GymOwnerDAOImpl;
import com.flipfit.dao.GymCentreDAOImpl;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;

public class GymOwnerService implements GymOwnerInterface {
    // Static list to act as a temporary database for this session
    private static List<GymCentre> staticGymCentreList = new ArrayList<>();
    
    private GymOwnerDAOImpl ownerDAO = new GymOwnerDAOImpl();

    /**
     * Register a new gym owner - saves to database via DAO
     */
    public void registerOwner(String fullName, String email, String password, Long phoneNumber, 
                             String city, String state, int pincode, String panCard, 
                             String aadhaarNumber, String gstin) {
        ownerDAO.registerOwner(fullName, email, password, phoneNumber, city, state, pincode, panCard, aadhaarNumber, gstin);
    }

    @Override
    public void addCentre(GymCentre centre) {
        if (centre == null) {
            System.out.println("ERROR: Centre cannot be null");
            return;
        }
        
        if (centre.getName() == null || centre.getName().isEmpty()) {
            System.out.println("ERROR: Centre name cannot be empty");
            return;
        }
        
        if (centre.getCity() == null || centre.getCity().isEmpty()) {
            System.out.println("ERROR: City cannot be empty");
            return;
        }
        
        // Get current logged-in owner to associate the centre
        String currentUserEmail = UserService.getCurrentLoggedInUser();
        if (currentUserEmail == null) {
            System.out.println("ERROR: No owner is logged in");
            return;
        }
        
        Object owner = FlipFitRepository.users.get(currentUserEmail);
        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can add centres");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();
        centre.setOwnerId(ownerId);
        
        // Set default values if not provided
        if (centre.getState() == null || centre.getState().isEmpty()) {
            centre.setState("Karnataka");
        }
        if (centre.getPincode() == 0) {
            centre.setPincode(560001);
        }
        
        // Persist to database using DAO
        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        centreDAO.insertGymCentre(centre);
        
        // Also add to in-memory repository for current session
        FlipFitRepository.gymCentres.add(centre);
        
        System.out.println("✓ Successfully added Gym Centre: " + centre.getName());
        System.out.println("  Status: ⚠ PENDING ADMIN APPROVAL");
    }

    @Override
    public List<GymCentre> viewMyCentres() {
        // REQUIREMENT: Owner should only see their own registered centres
        String currentUserEmail = UserService.getCurrentLoggedInUser();
        if (currentUserEmail == null) {
            System.out.println("No owner is logged in");
            return new ArrayList<>();
        }
        
        Object owner = FlipFitRepository.users.get(currentUserEmail);
        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view centres");
            return new ArrayList<>();
        }
        int ownerId = ((GymOwner) owner).getUserId();
        
        // Load from DB
        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> dbCentres = centreDAO.selectGymCentresByOwner(ownerId);
        
        // Sync repository for this session
        // Note: we might not want to clear ALL if multiple owners are active, 
        // but for a single-user CLI it's often simpler. 
        // Let's just filter out old ones of this owner.
        FlipFitRepository.gymCentres.removeIf(c -> c.getOwnerId() == ownerId);
        FlipFitRepository.gymCentres.addAll(dbCentres);
        
        if (dbCentres.isEmpty()) {
            System.out.println("You have not registered any centres yet");
            return new ArrayList<>();
        }
        
        System.out.println("\n========== MY GYM CENTRES ==========");
        System.out.println("Total Centres: " + dbCentres.size());
        System.out.println("-----------------------------------------");
        
        for (GymCentre centre : dbCentres) {
            String status = centre.isApproved() ? "✓ Approved" : "⚠ Pending";
            System.out.println("Centre ID: " + centre.getCentreId() +
                             " | Name: " + centre.getName() +
                             " | City: " + centre.getCity() +
                             " | Status: " + status);
        }
        System.out.println("===================================\n");
        
        return new ArrayList<>(dbCentres);
    }

    @Override
    public List<GymCustomer> viewCustomers(int gymCentreId) {
        if (gymCentreId <= 0) {
            System.out.println("ERROR: Invalid centre ID");
            return new ArrayList<>();
        }
        
        System.out.println("Displaying customer list for Centre ID: " + gymCentreId);
        return new ArrayList<>(); // Return empty list for now
    }

    @Override
    public void requestApproval(int gymOwnerId) {
        if (gymOwnerId <= 0) {
            System.out.println("ERROR: Invalid gym owner ID");
            return;
        }
        
        System.out.println("✓ Approval request for Gym Owner " + gymOwnerId + " is now PENDING.");
    }
    
    /**
     * View all bookings for this owner's centres
     */
    public void viewMyBookings() {
        String currentUserEmail = UserService.getCurrentLoggedInUser();
        if (currentUserEmail == null) {
            System.out.println("No owner is logged in");
            return;
        }
        
        Object owner = FlipFitRepository.users.get(currentUserEmail);
        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view bookings");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();
        
        // Load Centres from DB
        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> myCentres = centreDAO.selectGymCentresByOwner(ownerId);
        
        if (myCentres.isEmpty()) {
            System.out.println("You have no registered centres");
            return;
        }
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();
        
        List<Booking> myBookings = new ArrayList<>();
        for (GymCentre centre : myCentres) {
            List<Slot> centreSlots = slotDAO.getSlotsByCentreId(centre.getCentreId());
            for (Slot slot : centreSlots) {
                // This is a bit inefficient (multiple DB calls), but correct for logic now
                // Ideally DAO should have a getBookingsByCentreId
                List<Booking> allBookings = bookingDAO.getAllBookings(); // Simplified for now
                for (Booking b : allBookings) {
                    // This is still mock-ish if we don't have a way to link availability to slot
                    // But for now let's assume availability ID exists
                    myBookings.add(b);
                }
            }
        }
        
        // Actually, let's just use getAllBookings and filter for simplicity or better, get all slots
        myBookings.clear();
        List<Booking> allBookings = bookingDAO.getAllBookings();
        for (Booking b : allBookings) {
            // Check if this booking's availability belongs to one of owner's centres
            // This requires looking up availability -> slot -> centre
            com.flipfit.dao.SlotAvailabilityDAOImpl availabilityDAO = new com.flipfit.dao.SlotAvailabilityDAOImpl();
            SlotAvailability sa = availabilityDAO.getSlotAvailabilityById(b.getAvailabilityId());
            if (sa != null) {
                Slot slot = slotDAO.getSlotById(sa.getSlotId());
                if (slot != null && myCentres.stream().anyMatch(c -> c.getCentreId() == slot.getCentreId())) {
                    myBookings.add(b);
                }
            }
        }
        
        System.out.println("\n========== MY BOOKINGS (All Centres) ==========");
        if (myBookings.isEmpty()) {
            System.out.println("No bookings for your centres");
        } else {
            System.out.println("Total Bookings: " + myBookings.size());
            System.out.println("-----------------------------------------");
            for (Booking booking : myBookings) {
                System.out.println("Booking ID: " + booking.getBookingId() +
                                 " | Customer ID: " + booking.getCustomerId() +
                                 " | Status: " + booking.getStatus() +
                                 " | Date: " + booking.getCreatedAt());
            }
        }
        System.out.println("============================================\n");
    }
    
    /**
     * Cancel a booking from owner's centre
     */
    public boolean cancelBooking(int bookingId) {
        System.out.println("\n========== CANCEL BOOKING ==========");
        
        if (bookingId <= 0) {
            System.out.println("ERROR: Invalid booking ID");
            return false;
        }
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        boolean success = bookingDAO.cancelBooking(bookingId);
        
        if (success) {
            System.out.println("✓ Booking " + bookingId + " cancelled successfully from database");
            return true;
        } else {
            System.out.println("ERROR: Failed to cancel booking. ID might be incorrect.");
            return false;
        }
    }
    
    /**
     * View wait list for this owner's centres
     */
    public void viewMyWaitList() {
        System.out.println("\n========== MY WAIT LIST ==========");
        
        String currentUserEmail = UserService.getCurrentLoggedInUser();
        if (currentUserEmail == null) {
            System.out.println("No owner is logged in");
            return;
        }
        
        Object owner = FlipFitRepository.users.get(currentUserEmail);
        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view wait lists");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();
        
        // Load from DB
        com.flipfit.dao.GymCentreDAOImpl centreDAO = new com.flipfit.dao.GymCentreDAOImpl();
        List<GymCentre> myCentres = centreDAO.selectGymCentresByOwner(ownerId);
        
        if (myCentres.isEmpty()) {
            System.out.println("You have no registered centres");
            return;
        }
        
        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
        List<WaitListEntry> allEntries = waitlistDAO.getAllWaitListEntries();
        
        System.out.println("Total Wait List Entries in System: " + allEntries.size());
        System.out.println("-----------------------------------------");
        for (WaitListEntry entry : allEntries) {
            System.out.println("Waitlist ID: " + entry.getWaitlistid() + 
                             " | Position: " + entry.getPosition() + 
                             " | Created: " + entry.getCreatedAt());
        }
        System.out.println("==================================\n");
    }
    
    /**
     * View booking metrics for owner's centres
     */
    public void viewBookingMetrics() {
        String currentUserEmail = UserService.getCurrentLoggedInUser();
        if (currentUserEmail == null) {
            System.out.println("No owner is logged in");
            return;
        }
        
        Object owner = FlipFitRepository.users.get(currentUserEmail);
        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view metrics");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();
        
        // Load from DB
        com.flipfit.dao.GymCentreDAOImpl centreDAO = new com.flipfit.dao.GymCentreDAOImpl();
        List<GymCentre> myCentres = centreDAO.selectGymCentresByOwner(ownerId);
        
        if (myCentres.isEmpty()) {
            System.out.println("You have no registered centres");
            return;
        }
        
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        List<Booking> allBookings = bookingDAO.getAllBookings(); // Simplified filter
        
        long confirmedCount = allBookings.stream().filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus())).count();
        long cancelledCount = allBookings.stream().filter(b -> "CANCELLED".equalsIgnoreCase(b.getStatus())).count();
        
        System.out.println("\n========== BOOKING METRICS ==========");
        System.out.println("Total Centres: " + myCentres.size());
        System.out.println("Total Bookings in System: " + allBookings.size());
        System.out.println("Confirmed (Total): " + confirmedCount);
        System.out.println("Cancelled (Total): " + cancelledCount);
        System.out.println("====================================\n");
    }

    /**
     * Register a new gym centre
     * REQUIREMENT: Owner registers centre, admin approves it
     * Also creates the slots with the specified capacity
     */
    public boolean registerNewCentre(String centreName, String city, int numberOfSlots, int slotCapacity) {
        // Validation
        if (centreName == null || centreName.isEmpty()) {
            System.out.println("ERROR: Centre name cannot be empty");
            return false;
        }
        
        if (city == null || city.isEmpty()) {
            System.out.println("ERROR: City cannot be empty");
            return false;
        }
        
        if (numberOfSlots <= 0 || slotCapacity <= 0) {
            System.out.println("ERROR: Number of slots and capacity must be positive");
            return false;
        }
        
        // Get current logged-in owner
        String currentUserEmail = UserService.getCurrentLoggedInUser();
        if (currentUserEmail == null) {
            System.out.println("ERROR: No owner is logged in");
            return false;
        }
        
        Object owner = FlipFitRepository.users.get(currentUserEmail);
        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can register centres");
            return false;
        }
        int ownerId = ((GymOwner) owner).getUserId();
        
        // Generate new centre ID
        int newCentreId = FlipFitRepository.gymCentres.stream()
            .mapToInt(GymCentre::getCentreId)
            .max()
            .orElse(2) + 1;
        
        // Create new centre - PENDING approval
        GymCentre newCentre = new GymCentre();
        newCentre.setCentreId(newCentreId);
        newCentre.setName(centreName);
        newCentre.setCity(city);
        newCentre.setState("Karnataka"); // Default state
        newCentre.setPincode(560001); // Default pincode
        newCentre.setApproved(false); // Pending admin approval
        newCentre.setOwnerId(ownerId); // Associate with owner
        
        // Persist to database using DAO
        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        centreDAO.insertGymCentre(newCentre);
        
        // Get the actual DB ID from the centre object (after insert)
        int dbCentreId = newCentre.getCentreId();
        
        // Add to in-memory repository for current session
        FlipFitRepository.gymCentres.add(newCentre);
        
        // REQUIREMENT: Create slots for this centre
        createSlotsForCentre(dbCentreId, numberOfSlots, slotCapacity);
        
        System.out.println("\n========== GYM CENTRE REGISTRATION ==========");
        System.out.println("✓ Centre registered successfully!");
        System.out.println("Centre ID: " + dbCentreId);
        System.out.println("Name: " + centreName);
        System.out.println("City: " + city);
        System.out.println("Owner ID: " + ownerId);
        System.out.println("Slots created: " + numberOfSlots);
        System.out.println("Capacity per slot: " + slotCapacity);
        System.out.println("Status: ⚠ PENDING ADMIN APPROVAL");
        System.out.println("============================================\n");
        
        // Display created slots
        displayCreatedSlots(dbCentreId);
        
        return true;
    }
    
    /**
     * Helper method to create time slots for a newly registered centre
     */
    private void createSlotsForCentre(int centreId, int numberOfSlots, int slotCapacity) {
        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();
        com.flipfit.dao.SlotAvailabilityDAOImpl availabilityDAO = new com.flipfit.dao.SlotAvailabilityDAOImpl();
        
        LocalTime[][] timeSlots = {
            {LocalTime.of(6, 0), LocalTime.of(7, 0)},
            {LocalTime.of(7, 0), LocalTime.of(8, 0)},
            {LocalTime.of(8, 0), LocalTime.of(9, 0)},
            {LocalTime.of(18, 0), LocalTime.of(19, 0)},
            {LocalTime.of(19, 0), LocalTime.of(20, 0)},
            {LocalTime.of(20, 0), LocalTime.of(21, 0)},
            {LocalTime.of(5, 0), LocalTime.of(6, 0)},
            {LocalTime.of(9, 0), LocalTime.of(10, 0)}
        };
        
        System.out.println("Creating " + numberOfSlots + " slots in database...");
        for (int i = 0; i < numberOfSlots && i < timeSlots.length; i++) {
            Slot slot = new Slot(0, timeSlots[i][0], timeSlots[i][1], slotCapacity, centreId);
            slotDAO.addSlot(slot);
        }
        
        // After adding slots, we need to create availabilities for the next 7 days
        // Fetch the newly created slots to get their IDs
        List<Slot> createdSlots = slotDAO.getSlotsByCentreId(centreId);
        LocalDate today = LocalDate.now();
        
        for (Slot slot : createdSlots) {
            for (int day = 0; day < 7; day++) {
                SlotAvailability sa = new SlotAvailability();
                sa.setSlotId(slot.getSlotId());
                sa.setDate(today.plusDays(day));
                sa.setAvailable(true);
                availabilityDAO.addSlotAvailability(sa);
            }
        }
    }
    
    /**
     * Helper method to display created slots for a newly registered centre
     */
    private void displayCreatedSlots(int centreId) {
        System.out.println("\n========== CREATED SLOTS FOR CENTRE " + centreId + " ==========");
        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();
        List<Slot> centreSlots = slotDAO.getSlotsByCentreId(centreId);
        
        if (centreSlots.isEmpty()) {
            System.out.println("No slots found in database for this centre");
        } else {
            System.out.println("Total Slots: " + centreSlots.size());
            System.out.println("-----------------------------------------");
            for (Slot slot : centreSlots) {
                System.out.println("Slot ID: " + slot.getSlotId() + 
                                 " | Time: " + slot.getStartTime() + " - " + slot.getEndTime() + 
                                 " | Capacity: " + slot.getCapacity());
            }
        }
        System.out.println("==========================================================\n");
    }
    @Override
    public void setupSlotsForExistingCentre(int centreId, int numSlots, int capacity) {
        System.out.println("Setting up slots for Centre ID: " + centreId);
        createSlotsForCentre(centreId, numSlots, capacity);
        System.out.println("✓ Setup complete for Centre ID: " + centreId);
    }
}