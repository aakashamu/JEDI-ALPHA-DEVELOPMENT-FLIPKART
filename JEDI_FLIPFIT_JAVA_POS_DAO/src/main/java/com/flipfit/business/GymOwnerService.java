package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.bean.SlotAvailability;
import com.flipfit.bean.WaitListEntry;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.GymCentreDAOImpl;
import com.flipfit.dao.GymOwnerDAOImpl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class GymOwnerService.
 *
 * @author Ananya
 * @ClassName "GymOwnerService"
 */
public class GymOwnerService implements GymOwnerInterface {

    private GymOwnerDAOImpl ownerDAO = new GymOwnerDAOImpl();
    private UserService userService = new UserService();

    public void registerOwner(String fullName, String email, String password, Long phoneNumber,
            String city, String state, int pincode, String panCard,
            String aadhaarNumber, String gstin) {
        ownerDAO.registerOwner(fullName, email, password, phoneNumber, city, state, pincode, panCard, aadhaarNumber,
                gstin);
    }

    @Override
    public void addCentre(GymCentre centre, String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return;
        }
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

        Object owner = FlipFitRepository.users.get(email);
        // If not in cache, fetch from DAO (stateless fallback)
        if (owner == null) {
            owner = userService.getUser(email);
        }

        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can add centres");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();
        centre.setOwnerId(ownerId);

        if (centre.getState() == null || centre.getState().isEmpty()) {
            centre.setState("Karnataka");
        }
        if (centre.getPincode() == 0) {
            centre.setPincode(560001);
        }

        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        centreDAO.insertGymCentre(centre);
        FlipFitRepository.gymCentres.add(centre);

        System.out.println("✓ Successfully added Gym Centre: " + centre.getName());
        System.out.println("  Status: ⚠ PENDING ADMIN APPROVAL");
    }

    @Override
    public List<GymCentre> viewMyCentres(String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return new ArrayList<>();
        }

        Object owner = FlipFitRepository.users.get(email);
        if (owner == null)
            owner = userService.getUser(email);

        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view centres");
            return new ArrayList<>();
        }
        int ownerId = ((GymOwner) owner).getUserId();

        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> dbCentres = centreDAO.selectGymCentresByOwner(ownerId);

        FlipFitRepository.gymCentres.removeIf(c -> c.getOwnerId() == ownerId);
        FlipFitRepository.gymCentres.addAll(dbCentres);

        if (dbCentres.isEmpty()) {
            System.out.println("You have not registered any centres yet");
            return new ArrayList<>();
        }

        System.out.println("\n========== MY GYM CENTRES ==========");
        System.out.println("Total Centres: " + dbCentres.size());
        System.out.println("-----------------------------------------");

        dbCentres.forEach(centre -> {
            String status = centre.isApproved() ? "✓ Approved" : "⚠ Pending";
            System.out.println("Centre ID: " + centre.getCentreId() +
                    " | Name: " + centre.getName() +
                    " | City: " + centre.getCity() +
                    " | Status: " + status);
        });
        System.out.println("===================================\n");

        return new ArrayList<>(dbCentres);
    }

    @Override
    public List<GymCustomer> viewCustomers(int gymCentreId, String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return new ArrayList<>();
        }
        if (gymCentreId <= 0) {
            System.out.println("ERROR: Invalid centre ID");
            return new ArrayList<>();
        }

        System.out.println("Displaying customer list for Centre ID: " + gymCentreId);
        return new ArrayList<>();
    }

    @Override
    public void requestApproval(int gymOwnerId, String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return;
        }
        if (gymOwnerId <= 0) {
            System.out.println("ERROR: Invalid gym owner ID");
            return;
        }

        System.out.println("✓ Approval request for Gym Owner " + gymOwnerId + " is now PENDING.");
    }

    public void viewMyBookings(String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return;
        }

        Object owner = FlipFitRepository.users.get(email);
        if (owner == null)
            owner = userService.getUser(email);

        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view bookings");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();

        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> myCentres = centreDAO.selectGymCentresByOwner(ownerId);

        if (myCentres.isEmpty()) {
            System.out.println("You have no registered centres");
            return;
        }

        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();

        List<Booking> myBookings = new ArrayList<>();
        List<Booking> allBookings = bookingDAO.getAllBookings();
        for (Booking b : allBookings) {
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
            myBookings.forEach(booking -> System.out.println("Booking ID: " + booking.getBookingId() +
                    " | Customer ID: " + booking.getCustomerId() +
                    " | Status: " + booking.getStatus() +
                    " | Date: " + booking.getCreatedAt()));
        }
        System.out.println("============================================\n");
    }

    public boolean cancelBooking(int bookingId, String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return false;
        }
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

    public void viewMyWaitList(String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return;
        }
        System.out.println("\n========== MY WAIT LIST ==========");

        Object owner = FlipFitRepository.users.get(email);
        if (owner == null)
            owner = userService.getUser(email);

        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view wait lists");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();

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
        allEntries.forEach(entry -> System.out.println("Waitlist ID: " + entry.getWaitlistid() +
                " | Position: " + entry.getPosition() +
                " | Created: " + entry.getCreatedAt()));
        System.out.println("==================================\n");
    }

    public void viewBookingMetrics(String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return;
        }
        Object owner = FlipFitRepository.users.get(email);
        if (owner == null)
            owner = userService.getUser(email);

        if (!(owner instanceof GymOwner)) {
            System.out.println("ERROR: Only gym owners can view metrics");
            return;
        }
        int ownerId = ((GymOwner) owner).getUserId();

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

    public boolean registerNewCentre(String centreName, String city, int numberOfSlots, int slotCapacity, String email,
            String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return false;
        }
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

        Object owner = FlipFitRepository.users.get(email);
        if (owner == null)
            owner = userService.getUser(email);

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

        GymCentre newCentre = new GymCentre();
        newCentre.setCentreId(newCentreId);
        newCentre.setName(centreName);
        newCentre.setCity(city);
        newCentre.setState("Karnataka"); // Default state
        newCentre.setPincode(560001); // Default pincode
        newCentre.setApproved(false); // Pending admin approval
        newCentre.setOwnerId(ownerId); // Associate with owner

        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        centreDAO.insertGymCentre(newCentre);
        int dbCentreId = newCentre.getCentreId();

        FlipFitRepository.gymCentres.add(newCentre);

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

        displayCreatedSlots(dbCentreId);

        return true;
    }

    private void createSlotsForCentre(int centreId, int numberOfSlots, int slotCapacity) {
        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();
        com.flipfit.dao.SlotAvailabilityDAOImpl availabilityDAO = new com.flipfit.dao.SlotAvailabilityDAOImpl();

        LocalTime[][] timeSlots = {
                { LocalTime.of(6, 0), LocalTime.of(7, 0) },
                { LocalTime.of(7, 0), LocalTime.of(8, 0) },
                { LocalTime.of(8, 0), LocalTime.of(9, 0) },
                { LocalTime.of(18, 0), LocalTime.of(19, 0) },
                { LocalTime.of(19, 0), LocalTime.of(20, 0) },
                { LocalTime.of(20, 0), LocalTime.of(21, 0) },
                { LocalTime.of(5, 0), LocalTime.of(6, 0) },
                { LocalTime.of(9, 0), LocalTime.of(10, 0) }
        };

        System.out.println("Creating " + numberOfSlots + " slots in database...");
        for (int i = 0; i < numberOfSlots && i < timeSlots.length; i++) {
            Slot slot = new Slot(0, timeSlots[i][0], timeSlots[i][1], slotCapacity, centreId);
            slotDAO.addSlot(slot);
        }

        List<Slot> createdSlots = slotDAO.getSlotsByCentreId(centreId);
        LocalDate today = LocalDate.now();

        for (Slot slot : createdSlots) {
            for (int day = 0; day < 7; day++) {
                SlotAvailability sa = new SlotAvailability();
                sa.setSlotId(slot.getSlotId());
                sa.setDate(today.plusDays(day));
                sa.setSeatsTotal(slot.getCapacity());
                sa.setSeatsAvailable(slot.getCapacity());
                sa.setAvailable(true);
                // debug logs...
                availabilityDAO.addSlotAvailability(sa);
            }
        }
    }

    private void displayCreatedSlots(int centreId) {
        System.out.println("\n========== CREATED SLOTS FOR CENTRE " + centreId + " ==========");
        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();
        List<Slot> centreSlots = slotDAO.getSlotsByCentreId(centreId);

        if (centreSlots.isEmpty()) {
            System.out.println("No slots found in database for this centre");
        } else {
            System.out.println("Total Slots: " + centreSlots.size());
            System.out.println("-----------------------------------------");
            centreSlots.forEach(slot -> System.out
                    .println("Slot ID: " + slot.getSlotId() +
                            " | Time: " + slot.getStartTime() + " - " + slot.getEndTime() +
                            " | Capacity: " + slot.getCapacity()));
        }
        System.out.println("==========================================================\n");
    }

    @Override
    public void setupSlotsForExistingCentre(int centreId, int numSlots, int capacity, String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("ERROR: Authentication failed.");
            return;
        }
        System.out.println("Setting up slots for Centre ID: " + centreId);
        createSlotsForCentre(centreId, numSlots, capacity);
        System.out.println("✓ Setup complete for Centre ID: " + centreId);
    }
}