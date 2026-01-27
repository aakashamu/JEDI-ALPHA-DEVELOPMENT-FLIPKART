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
        
        List<GymCentre> myCentres = FlipFitRepository.gymCentres.stream()
            .filter(centre -> centre.getOwnerId() == ownerId)
            .toList();
        
        if (myCentres.isEmpty()) {
            System.out.println("You have no registered centres");
            return;
        }
        
        List<Booking> myBookings = new ArrayList<>();
        for (GymCentre centre : myCentres) {
            for (Slot slot : FlipFitRepository.slots) {
                if (slot.getCentreId() == centre.getCentreId()) {
                    List<Booking> slotBookings = FlipFitRepository.slotBookings.getOrDefault(slot.getSlotId(), new ArrayList<>());
                    myBookings.addAll(slotBookings);
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
                Slot slot = FlipFitRepository.slotMap.get(booking.getAvailabilityId());
                String slotInfo = (slot != null) ? 
                    slot.getStartTime() + " - " + slot.getEndTime() : "Unknown";
                
                System.out.println("Booking ID: " + booking.getBookingId() +
                                 " | Customer ID: " + booking.getCustomerId() +
                                 " | Slot Time: " + slotInfo +
                                 " | Status: " + booking.getStatus());
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
        
        String currentUserEmail = UserService.getCurrentLoggedInUser();
        if (currentUserEmail == null) {
            System.out.println("No owner is logged in");
            return false;
        }
        
        Object owner = FlipFitRepository.users.get(currentUserEmail);
        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can perform this action");
            return false;
        }
        int ownerId = ((GymOwner) owner).getUserId();
        
        Booking booking = FlipFitRepository.bookingsMap.get(bookingId);
        if (booking == null) {
            System.out.println("ERROR: Booking not found with ID: " + bookingId);
            return false;
        }
        
        Slot bookingSlot = FlipFitRepository.slotMap.get(booking.getAvailabilityId());
        if (bookingSlot == null) {
            System.out.println("ERROR: Slot not found for this booking");
            return false;
        }
        
        GymCentre centre = FlipFitRepository.gymCentres.stream()
            .filter(c -> c.getCentreId() == bookingSlot.getCentreId())
            .findFirst()
            .orElse(null);
        
        if (centre == null || centre.getOwnerId() != ownerId) {
            System.out.println("ERROR: This booking does not belong to your centre");
            return false;
        }
        
        if ("CANCELLED".equals(booking.getStatus())) {
            System.out.println("ERROR: Booking is already cancelled");
            return false;
        }
        
        booking.setStatus("CANCELLED");
        System.out.println("✓ Booking " + bookingId + " cancelled successfully");
        System.out.println("====================================\n");
        return true;
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
        
        List<GymCentre> myCentres = FlipFitRepository.gymCentres.stream()
            .filter(centre -> centre.getOwnerId() == ownerId)
            .toList();
        
        if (myCentres.isEmpty()) {
            System.out.println("You have no registered centres");
            return;
        }
        
        List<Integer> myWaitList = new ArrayList<>();
        for (GymCentre centre : myCentres) {
            for (Slot slot : FlipFitRepository.slots) {
                if (slot.getCentreId() == centre.getCentreId()) {
                    List<Integer> slotWaitList = FlipFitRepository.slotWaitList.getOrDefault(slot.getSlotId(), new ArrayList<>());
                    myWaitList.addAll(slotWaitList);
                }
            }
        }
        
        if (myWaitList.isEmpty()) {
            System.out.println("No wait list entries for your centres");
        } else {
            System.out.println("Total Wait List Entries: " + myWaitList.size());
            System.out.println("-----------------------------------------");
            int position = 1;
            for (Integer bookingId : myWaitList) {
                WaitListEntry entry = FlipFitRepository.waitListMap.get(bookingId);
                if (entry != null) {
                    System.out.println("Position " + position + ": Booking ID " + bookingId + 
                                     " | Added: " + entry.getCreatedAt());
                }
                position++;
            }
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
        
        // Get all centres owned by this owner
        List<GymCentre> myCentres = FlipFitRepository.gymCentres.stream()
            .filter(centre -> centre.getOwnerId() == ownerId)
            .toList();
        
        if (myCentres.isEmpty()) {
            System.out.println("You have no registered centres");
            return;
        }
        
        // Calculate metrics
        List<Booking> myBookings = new ArrayList<>();
        for (GymCentre centre : myCentres) {
            for (Slot slot : FlipFitRepository.slots) {
                if (slot.getCentreId() == centre.getCentreId()) {
                    List<Booking> slotBookings = FlipFitRepository.slotBookings.getOrDefault(slot.getSlotId(), new ArrayList<>());
                    myBookings.addAll(slotBookings);
                }
            }
        }
        
        long confirmedCount = myBookings.stream().filter(b -> "CONFIRMED".equals(b.getStatus())).count();
        long cancelledCount = myBookings.stream().filter(b -> "CANCELLED".equals(b.getStatus())).count();
        
        System.out.println("\n========== BOOKING METRICS ==========");
        System.out.println("Total Centres: " + myCentres.size());
        System.out.println("Total Bookings: " + myBookings.size());
        System.out.println("Confirmed: " + confirmedCount);
        System.out.println("Cancelled: " + cancelledCount);
        System.out.println("Occupancy Rate: " + 
            (myBookings.isEmpty() ? "0%" : String.format("%.1f%%", (confirmedCount * 100.0 / myBookings.size()))));
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
        
        // Add to in-memory repository for current session
        FlipFitRepository.gymCentres.add(newCentre);
        
        // REQUIREMENT: Create slots for this centre
        createSlotsForCentre(newCentreId, numberOfSlots, slotCapacity);
        
        System.out.println("\n========== GYM CENTRE REGISTRATION ==========");
        System.out.println("✓ Centre registered successfully!");
        System.out.println("Centre ID: " + newCentreId);
        System.out.println("Name: " + centreName);
        System.out.println("City: " + city);
        System.out.println("Owner ID: " + ownerId);
        System.out.println("Slots created: " + numberOfSlots);
        System.out.println("Capacity per slot: " + slotCapacity);
        System.out.println("Status: ⚠ PENDING ADMIN APPROVAL");
        System.out.println("============================================\n");
        
        // Display created slots
        displayCreatedSlots(newCentreId);
        
        return true;
    }
    
    /**
     * Helper method to create time slots for a newly registered centre
     */
    private void createSlotsForCentre(int centreId, int numberOfSlots, int slotCapacity) {
        int slotId = FlipFitRepository.slots.stream()
            .mapToInt(Slot::getSlotId)
            .max()
            .orElse(512) + 1;
        
        LocalTime[][] timeSlots = {
            {LocalTime.of(6, 0), LocalTime.of(7, 0)},
            {LocalTime.of(7, 0), LocalTime.of(8, 0)},
            {LocalTime.of(8, 0), LocalTime.of(9, 0)},
            {LocalTime.of(18, 0), LocalTime.of(19, 0)},
            {LocalTime.of(19, 0), LocalTime.of(20, 0)},
            {LocalTime.of(20, 0), LocalTime.of(21, 0)},
            {LocalTime.of(5, 0), LocalTime.of(6, 0)},
            {LocalTime.of(9, 0), LocalTime.of(10, 0)},
            {LocalTime.of(10, 0), LocalTime.of(11, 0)},
            {LocalTime.of(11, 0), LocalTime.of(12, 0)},
            {LocalTime.of(12, 0), LocalTime.of(13, 0)},
            {LocalTime.of(13, 0), LocalTime.of(14, 0)},
            {LocalTime.of(14, 0), LocalTime.of(15, 0)},
            {LocalTime.of(15, 0), LocalTime.of(16, 0)},
            {LocalTime.of(16, 0), LocalTime.of(17, 0)},
            {LocalTime.of(17, 0), LocalTime.of(18, 0)},
            {LocalTime.of(21, 0), LocalTime.of(22, 0)},
            {LocalTime.of(22, 0), LocalTime.of(23, 0)},
            {LocalTime.of(23, 0), LocalTime.of(23, 59)},
            {LocalTime.of(4, 0), LocalTime.of(5, 0)}
        };
        
        for (int i = 0; i < numberOfSlots && i < timeSlots.length; i++) {
            Slot slot = new Slot(slotId++, timeSlots[i][0], timeSlots[i][1], slotCapacity, centreId);
            FlipFitRepository.slots.add(slot);
            FlipFitRepository.slotMap.put(slot.getSlotId(), slot);
            FlipFitRepository.slotBookings.put(slot.getSlotId(), new ArrayList<>());
        }
        
        LocalDate today = LocalDate.now();
        int availabilityId = FlipFitRepository.slotAvailabilityMap.keySet().stream()
            .mapToInt(Integer::intValue)
            .max()
            .orElse(2999) + 1;
        
        for (Slot slot : FlipFitRepository.slots) {
            if (slot.getCentreId() == centreId) {
                for (int day = 0; day < 7; day++) {
                    LocalDate date = today.plusDays(day);
                    SlotAvailability availability = new SlotAvailability();
                    availability.setId(availabilityId++);
                    availability.setSlotId(slot.getSlotId());
                    availability.setDate(date);
                    availability.setAvailable(true);
                    FlipFitRepository.slotAvailabilityMap.put(availability.getId(), availability);
                }
            }
        }
    }
    
    /**
     * Helper method to display created slots for a newly registered centre
     */
    private void displayCreatedSlots(int centreId) {
        System.out.println("\n========== CREATED SLOTS FOR CENTRE " + centreId + " ==========");
        List<Slot> centreSlots = FlipFitRepository.slots.stream()
            .filter(slot -> slot.getCentreId() == centreId)
            .toList();
        
        if (centreSlots.isEmpty()) {
            System.out.println("No slots created");
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
}