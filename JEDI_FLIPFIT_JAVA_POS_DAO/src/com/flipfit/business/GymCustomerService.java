package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.Slot;
import com.flipfit.bean.SlotAvailability;
import com.flipfit.bean.WaitListEntry;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.GymCustomerDAO;
import java.util.ArrayList;
import java.util.List;

public class GymCustomerService implements GymCustomerInterface {

    private GymCustomerDAO customerDAO = new GymCustomerDAO();

    /**
     * Register a new customer - saves to database via DAO
     */
    public void registerCustomer(String fullName, String email, String password, Long phoneNumber, String city,
            String state, int pincode) {
        customerDAO.registerCustomer(fullName, email, password, phoneNumber, city, state, pincode);
    }

    private static String getCurrentUserEmail() {
        // Get the first logged-in user (simplified session management)
        // In a real implementation, you'd have proper session tracking
        return UserService.getCurrentLoggedInUser();
    }

    @Override
    public List<GymCentre> viewCentres() {
        // Load from DB
        com.flipfit.dao.GymCentreDAOImpl centreDAO = new com.flipfit.dao.GymCentreDAOImpl();
        List<GymCentre> allCentres = centreDAO.selectAllGymCentres();

        // Sync repository
        FlipFitRepository.gymCentres.clear();
        FlipFitRepository.gymCentres.addAll(allCentres);

        // REQUIREMENT: Customers should only see APPROVED centres
        List<GymCentre> approvedCentres = allCentres.stream()
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

        // Load from DB
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        List<Booking> dbBookings = bookingDAO.getCustomerBookings(currentCustomer.getUserId());

        // Sync repository
        FlipFitRepository.customerBookings.put(currentCustomer.getUserId(), new ArrayList<>(dbBookings));

        System.out.println("\n========== YOUR BOOKINGS ==========");
        if (dbBookings.isEmpty()) {
            System.out.println("You have no bookings yet.");
        } else {
            for (Booking b : dbBookings) {
                System.out.println("Booking ID: " + b.getBookingId() +
                        " | Availability ID: " + b.getAvailabilityId() +
                        " | Status: " + b.getStatus() +
                        " | Date: " + b.getCreatedAt());
            }
        }
        System.out.println("===================================\n");

        return dbBookings;
    }

    @Override
    public Booking bookSlot(int slotAvailabilityId)
            throws com.flipfit.exception.SlotNotAvailableException, com.flipfit.exception.BookingNotDoneException {
        if (slotAvailabilityId <= 0) {
            throw new com.flipfit.exception.SlotNotAvailableException("ERROR: Invalid slot availability ID");
        }

        String currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail == null) {
            throw new com.flipfit.exception.BookingNotDoneException("No customer is currently logged in");
        }

        GymCustomer currentCustomer = FlipFitRepository.customers.get(currentUserEmail);
        if (currentCustomer == null) {
            throw new com.flipfit.exception.BookingNotDoneException("Customer profile not found");
        }

        com.flipfit.dao.SlotAvailabilityDAOImpl availabilityDAO = new com.flipfit.dao.SlotAvailabilityDAOImpl();
        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();

        SlotAvailability availability = availabilityDAO.getSlotAvailabilityById(slotAvailabilityId);
        if (availability == null) {
            throw new com.flipfit.exception.SlotNotAvailableException(
                    "ERROR: Slot Availability not found with ID: " + slotAvailabilityId);
        }

        Slot slot = slotDAO.getSlotById(availability.getSlotId());
        if (slot == null) {
            throw new com.flipfit.exception.SlotNotAvailableException(
                    "ERROR: Slot not found with ID: " + availability.getSlotId());
        }

        // Check if seats are available for this specific availability
        if (availability.getSeatsAvailable() <= 0) {
            System.out.println("⚠ Slot is FULL for this availability (ID: " + slotAvailabilityId + ")");
            System.out.println("Adding you to the waitlist...");

            // Add to waitlist
            com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
            com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();

            Booking pendingBooking = bookingDAO.createPendingBooking(currentCustomer.getUserId(), slotAvailabilityId);
            if (pendingBooking != null) {
                int waitlistPosition = waitlistDAO.addToWaitList(pendingBooking.getBookingId(),
                        currentCustomer.getUserId(), slotAvailabilityId);
                System.out.println("✓ Added to waitlist at position: " + waitlistPosition);
                return pendingBooking;
            } else {
                throw new com.flipfit.exception.BookingNotDoneException("ERROR: Failed to add to waitlist.");
            }
        }

        List<Booking> customerExistingBookings = FlipFitRepository.customerBookings
                .getOrDefault(currentCustomer.getUserId(), new ArrayList<>());

        List<Booking> conflictingBookings = new ArrayList<>();
        for (Booking existingBooking : customerExistingBookings) {
            if ("CONFIRMED".equals(existingBooking.getStatus())) {
                SlotAvailability existingSA = availabilityDAO
                        .getSlotAvailabilityById(existingBooking.getAvailabilityId());
                if (existingSA != null) {
                    Slot existingSlot = slotDAO.getSlotById(existingSA.getSlotId());
                    if (existingSlot != null && slotsOverlap(slot, existingSlot)) {
                        conflictingBookings.add(existingBooking);
                    }
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

        // Persist to database
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        Booking newBooking = bookingDAO.createBooking(currentCustomer.getUserId(), slotAvailabilityId);

        if (newBooking != null) {
            // Decrement available seats by 1 in database
            availabilityDAO.decrementSeats(slotAvailabilityId);

            // Also sync in-memory repository for current session
            FlipFitRepository.allBookings.add(newBooking);
            FlipFitRepository.bookingsMap.put(newBooking.getBookingId(), newBooking);
            FlipFitRepository.customerBookings.computeIfAbsent(currentCustomer.getUserId(), k -> new ArrayList<>())
                    .add(newBooking);
            FlipFitRepository.slotBookings.computeIfAbsent(slotAvailabilityId, k -> new ArrayList<>()).add(newBooking);

            System.out.println("✓ Booking successful! ID: " + newBooking.getBookingId());
            System.out.println("Slot Time: " + slot.getStartTime() + " - " + slot.getEndTime());
            return newBooking;
        } else {
            throw new com.flipfit.exception.BookingNotDoneException("ERROR: Failed to create booking in database.");
        }
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
            FlipFitRepository.customerBookings.values()
                    .forEach(bookings -> bookings.removeIf(b -> b.getBookingId() == bookingId));
            FlipFitRepository.slotBookings.values()
                    .forEach(bookings -> bookings.removeIf(b -> b.getBookingId() == bookingId));
        }
    }

    @Override
    public boolean cancelBooking(int bookingId) throws com.flipfit.exception.BookingNotDoneException {
        if (bookingId <= 0) {
            throw new com.flipfit.exception.BookingNotDoneException("ERROR: Invalid booking ID");
        }

        // Persist to database
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        boolean dbSuccess = bookingDAO.cancelBooking(bookingId);

        if (dbSuccess) {
            // Fetch booking from database to get its details
            Booking booking = bookingDAO.getBookingById(bookingId);
            System.out.println("[DEBUG] Fetched booking from DB: " + booking);

            if (booking != null) {
                String originalStatus = booking.getStatus();
                System.out.println("[DEBUG] Original status from database: " + originalStatus);

                // If it was a CONFIRMED booking, restore slot availability and promote from
                // waitlist
                if ("CONFIRMED".equals(originalStatus)) {
                    System.out.println("[DEBUG] Entered CONFIRMED branch");
                    int availabilityId = booking.getAvailabilityId();
                    System.out.println("[DEBUG] Cancelled booking from availability: " + availabilityId);
                    com.flipfit.dao.SlotAvailabilityDAOImpl availabilityDAO = new com.flipfit.dao.SlotAvailabilityDAOImpl();
                    availabilityDAO.incrementSeats(availabilityId);

                    // Promote first customer from waitlist
                    com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
                    WaitListEntry nextInLine = waitlistDAO.getFirstPendingWaitlistEntry(availabilityId);
                    System.out.println("[DEBUG] nextInLine result: " + nextInLine);
                    if (nextInLine != null) {
                        System.out.println("[DEBUG] Promoting booking ID: " + nextInLine.getBookingId());
                        // Update booking status to CONFIRMED
                        boolean promoted = bookingDAO.confirmWaitlistBooking(nextInLine.getBookingId());
                        System.out.println("[DEBUG] Promotion result: " + promoted);

                        if (promoted) {
                            // Remove the promoted booking from waitlist
                            waitlistDAO.removeFromWaitList(nextInLine.getBookingId());
                            System.out
                                    .println("[DEBUG] Removed booking " + nextInLine.getBookingId() + " from waitlist");

                            // Update positions for remaining waitlist entries
                            waitlistDAO.updateWaitlistPositions(availabilityId);
                            System.out
                                    .println("[DEBUG] Updated waitlist positions for availability: " + availabilityId);

                            System.out.println("✓ Customer at position #1 has been promoted from waitlist!");
                        }
                    } else {
                        System.out.println("[DEBUG] No pending bookings found for availability: " + availabilityId);
                    }
                } else {
                    System.out.println("[DEBUG] Original status was: " + originalStatus + ", not CONFIRMED");
                }

                // If it was a PENDING booking (waitlist), remove from waitlist
                if ("PENDING".equals(originalStatus)) {
                    com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();
                    waitlistDAO.removeFromWaitList(bookingId);
                    System.out.println("✓ Removed from waitlist.");
                }
            } else {
                System.out.println("[DEBUG] Booking not found in database!");
            }
            System.out.println("✓ Booking cancelled successfully! ID: " + bookingId);
            return true;
        } else {
            throw new com.flipfit.exception.BookingNotDoneException("ERROR: Failed to cancel booking in database.");
        }
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

        // Sync with DB
        com.flipfit.dao.UserDAO userDAO = new com.flipfit.dao.UserDAO();
        com.flipfit.bean.User user = userDAO.getUserDetails(email);

        if (user != null) {
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            user.setCity(city);
            user.setPincode(pincode);
            userDAO.updateProfile(user);

            // Also update in-memory repository
            GymCustomer customer = FlipFitRepository.customers.get(email);
            if (customer != null) {
                customer.setFullName(fullName);
                customer.setPhoneNumber(phoneNumber);
                customer.setCity(city);
                customer.setPincode(pincode);
            }
            System.out.println("✓ Profile updated successfully for " + email);
        } else {
            System.out.println("ERROR: Customer profile not found in database.");
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

    public void viewAvailableSlots(int centreId) {
        if (centreId <= 0) {
            System.out.println("ERROR: Invalid centre ID");
            return;
        }

        com.flipfit.dao.SlotDAOImpl slotDAO = new com.flipfit.dao.SlotDAOImpl();
        com.flipfit.dao.SlotAvailabilityDAOImpl availabilityDAO = new com.flipfit.dao.SlotAvailabilityDAOImpl();
        com.flipfit.dao.WaitlistDAOImpl waitlistDAO = new com.flipfit.dao.WaitlistDAOImpl();

        List<Slot> slots = slotDAO.getSlotsByCentreId(centreId);
        if (slots.isEmpty()) {
            System.out.println("No slots found for this centre.");
            return;
        }

        System.out.println("\n--- AVAILABLE SLOTS ---");
        System.out.println(String.format("%-15s | %-8s | %-20s | %-12s | %-15s | %-12s",
                "Availability ID", "Slot ID", "Time", "Date", "Seats Available", "Waitlist #"));
        System.out.println("-".repeat(90));

        boolean foundAny = false;
        for (Slot slot : slots) {
            List<SlotAvailability> availabilities = availabilityDAO.getAvailableSlotsBySlotId(slot.getSlotId());
            for (SlotAvailability sa : availabilities) {
                // Show all slots including those with 0 seats
                int seatsAvailable = sa.getSeatsAvailable();
                int waitlistCount = waitlistDAO.getWaitlistCountByAvailabilityId(sa.getId());

                String seatDisplay = seatsAvailable > 0 ? String.valueOf(seatsAvailable) : "FULL";
                String waitlistDisplay = waitlistCount > 0 ? String.valueOf(waitlistCount) : "-";

                System.out.println(String.format("%-15d | %-8d | %-20s | %-12s | %-15s | %-12s",
                        sa.getId(), slot.getSlotId(),
                        slot.getStartTime() + " - " + slot.getEndTime(),
                        sa.getDate(),
                        seatDisplay,
                        waitlistDisplay));
                foundAny = true;
            }
        }

        if (!foundAny) {
            System.out.println("No slots found for this centre.");
        }
        System.out.println("-".repeat(90) + "\n");
    }
}