package com.flipfit.dao;

import com.flipfit.bean.*;
import java.util.*;

/**
 * Centralized Mock Database using Collections
 */
public class FlipFitRepository {

    // Global static lists reachable by all services
    public static Map<String, GymCustomer> customers = new HashMap<>();
    public static List<GymCentre> gymCentres = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();
    public static List<GymOwner> owners = new ArrayList<>();
    public static Map<String, User> users = new HashMap<>();

    // Slot Collections (DAO for Slot Management)
    public static List<Slot> slots = new ArrayList<>();
    public static Map<Integer, Slot> slotMap = new HashMap<>(); // slotId -> Slot
    public static Map<Integer, List<Booking>> slotBookings = new HashMap<>(); // slotId -> List of bookings
    
    // SlotAvailability Collections (DAO for Date-aware availability)
    public static Map<Integer, SlotAvailability> slotAvailabilityMap = new HashMap<>(); // availabilityId -> SlotAvailability

    // WaitList Collections (DAO for WaitListService)
    public static Queue<Integer> waitListQueue = new LinkedList<>();
    public static Map<Integer, WaitListEntry> waitListMap = new HashMap<>();
    public static List<WaitListEntry> allWaitListEntries = new ArrayList<>();
    public static Map<Integer, List<Integer>> slotWaitList = new HashMap<>();

    // Booking Collections (DAO for BookingService)
    public static Map<Integer, Booking> bookingsMap = new HashMap<>();
    public static List<Booking> allBookings = new ArrayList<>();
    public static Map<Integer, List<Booking>> customerBookings = new HashMap<>();

    static {
        // Initialize sample users for testing
        initializeSampleUsers();

        // Initialize all hardcoded data
        initializeHardcodedWaitList();
        initializeSampleCentres();
        initializeHardcodedSlots();
        initializeSlotAvailability();
        initializeHardcodedBookings();
    }

    // -------------------------------
    // Sample Centres Initialization
    // -------------------------------
    private static void initializeSampleCentres() {
        // Initial Hard-coded data for testing centers
        GymCentre center1 = new GymCentre();
        center1.setCentreId(1);
        center1.setName("FlipFit Elite - Koramangala");
        center1.setCity("Bengaluru");
        center1.setApproved(true);

        GymCentre center2 = new GymCentre();
        center2.setCentreId(2);
        center2.setName("FlipFit Pro - Indiranagar");
        center2.setCity("Bengaluru");
        center2.setApproved(true);

        gymCentres.add(center1);
        gymCentres.add(center2);
    }

    // -------------------------------
    // Slot Initialization
    // -------------------------------
    private static void initializeHardcodedSlots() {
        // Create sample slots for testing
        Slot slot1 = new Slot(501, java.time.LocalTime.of(6, 0), java.time.LocalTime.of(7, 0), 10, 1);
        Slot slot2 = new Slot(502, java.time.LocalTime.of(7, 0), java.time.LocalTime.of(8, 0), 10, 1);
        Slot slot3 = new Slot(503, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(9, 0), 10, 1);
        
        slots.addAll(List.of(slot1, slot2, slot3));
        
        slotMap.put(501, slot1);
        slotMap.put(502, slot2);
        slotMap.put(503, slot3);
        
        for (Slot slot : slots) {
            slotBookings.put(slot.getSlotId(), new ArrayList<>());
        }
    }

    // -------------------------------
    // SlotAvailability Initialization
    // -------------------------------
    private static void initializeSlotAvailability() {
        java.time.LocalDate today = java.time.LocalDate.now();
        int availabilityId = 3000;
        
        for (Slot slot : slots) {
            for (int day = 0; day < 7; day++) {
                java.time.LocalDate date = today.plusDays(day);
                
                SlotAvailability availability = new SlotAvailability();
                availability.setId(availabilityId++);
                availability.setSlotId(slot.getSlotId());
                availability.setDate(date);
                availability.setAvailable(true);
                
                slotAvailabilityMap.put(availability.getId(), availability);
            }
        }
    }

    // -------------------------------
    // WaitList Initialization
    // -------------------------------
    private static void initializeHardcodedWaitList() {

        // Sample WaitList Entry 1
        WaitListEntry entry1 = new WaitListEntry();
        entry1.setWaitlistid(2001);
        entry1.setPosition(1);
        entry1.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        // Sample WaitList Entry 2
        WaitListEntry entry2 = new WaitListEntry();
        entry2.setWaitlistid(2002);
        entry2.setPosition(2);
        entry2.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        // Sample WaitList Entry 3
        WaitListEntry entry3 = new WaitListEntry();
        entry3.setWaitlistid(2003);
        entry3.setPosition(3);
        entry3.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        // Add to Map
        waitListMap.put(1005, entry1);
        waitListMap.put(1006, entry2);
        waitListMap.put(1007, entry3);

        // Add to List
        allWaitListEntries.add(entry1);
        allWaitListEntries.add(entry2);
        allWaitListEntries.add(entry3);

        // Add to Queue (FIFO order)
        waitListQueue.offer(1005);
        waitListQueue.offer(1006);
        waitListQueue.offer(1007);

        // Organize by availability / slot
        slotWaitList.put(501, new ArrayList<>());
        slotWaitList.get(501).add(1005);
        slotWaitList.get(501).add(1006);

        slotWaitList.put(502, new ArrayList<>());
        slotWaitList.get(502).add(1007);
    }

    // -------------------------------
    // Booking Initialization
    // -------------------------------
    private static void initializeHardcodedBookings() {

        // Sample Booking 1 - Confirmed
        Booking booking1 = new Booking();
        booking1.setBookingId(1001);
        booking1.setCustomerId(101);
        booking1.setAvailabilityId(501);
        booking1.setStatus("CONFIRMED");
        booking1.setBookingDate(new java.util.Date());
        booking1.setCreatedAt("2026-01-20 10:30:00");

        bookingsMap.put(1001, booking1);
        allBookings.add(booking1);
        customerBookings.computeIfAbsent(101, k -> new ArrayList<>()).add(booking1);

        // Sample Booking 2 - Confirmed
        Booking booking2 = new Booking();
        booking2.setBookingId(1002);
        booking2.setCustomerId(102);
        booking2.setAvailabilityId(502);
        booking2.setStatus("CONFIRMED");
        booking2.setBookingDate(new java.util.Date());
        booking2.setCreatedAt("2026-01-21 11:00:00");

        bookingsMap.put(1002, booking2);
        allBookings.add(booking2);
        customerBookings.computeIfAbsent(102, k -> new ArrayList<>()).add(booking2);

        // Sample Booking 3 - Cancelled
        Booking booking3 = new Booking();
        booking3.setBookingId(1003);
        booking3.setCustomerId(101);
        booking3.setAvailabilityId(503);
        booking3.setStatus("CANCELLED");
        booking3.setBookingDate(new java.util.Date());
        booking3.setCreatedAt("2026-01-22 09:15:00");

        bookingsMap.put(1003, booking3);
        allBookings.add(booking3);
        customerBookings.computeIfAbsent(101, k -> new ArrayList<>()).add(booking3);

        // Sample Booking 4 - Confirmed
        Booking booking4 = new Booking();
        booking4.setBookingId(1004);
        booking4.setCustomerId(101);
        booking4.setAvailabilityId(504);
        booking4.setStatus("CONFIRMED");
        booking4.setBookingDate(new java.util.Date());
        booking4.setCreatedAt("2026-01-23 14:45:00");

        bookingsMap.put(1004, booking4);
        allBookings.add(booking4);
        customerBookings.computeIfAbsent(101, k -> new ArrayList<>()).add(booking4);
    }

    // -------------------------------
    // Users Initialization
    // -------------------------------
    private static void initializeSampleUsers() {

        // Sample customers
        GymCustomer customer1 = new GymCustomer();
        customer1.setUserId(101);
        customer1.setFullName("Ananya");
        customer1.setEmail("ananya@gmail.com");
        customer1.setPassword("password@123");
        customer1.setPhoneNumber(7091234567L);
        customer1.setCity("Bengaluru");
        customer1.setPincode(560001);
        customers.put(customer1.getEmail(), customer1);
        users.put(customer1.getEmail(), customer1);

        GymCustomer customer2 = new GymCustomer();
        customer2.setUserId(102);
        customer2.setFullName("Supriya Nalla");
        customer2.setEmail("supriya@gmail.com");
        customer2.setPassword("password@456");
        customer2.setPhoneNumber(7094561234L);
        customer2.setCity("Hyderabad");
        customer2.setPincode(500008);
        customers.put(customer2.getEmail(), customer2);
        users.put(customer2.getEmail(), customer2);

        // Sample owners
        GymOwner owner1 = new GymOwner();
        owner1.setUserId(201);
        owner1.setFullName("Mekala Samiksha");
        owner1.setEmail("sam@gmail.com");
        owner1.setPassword("owner@123");
        owner1.setPhoneNumber(9876543212L);
        owner1.setCity("Pune");
        owner1.setPincode(560034);
        owner1.setPAN("ABCDE1234F");
        owner1.setAadhaar("123456789012");
        owner1.setGSTIN("29ABCDE1234F1ZV");
        owners.add(owner1);
        users.put(owner1.getEmail(), owner1);

        GymOwner owner2 = new GymOwner();
        owner2.setUserId(202);
        owner2.setFullName("Sujitha");
        owner2.setEmail("sujitha@gmail.com");
        owner2.setPassword("owner@456");
        owner2.setPhoneNumber(9876543343L);
        owner2.setCity("Delhi");
        owner2.setPincode(110001);
        owner2.setPAN("FGHIJ5678K");
        owner2.setAadhaar("345678901234");
        owner2.setGSTIN("07FGHIJ5678K2ZL");
        owners.add(owner2);
        users.put(owner2.getEmail(), owner2);

        // Sample admin
        GymAdmin admin1 = new GymAdmin();
        admin1.setUserId(301);
        admin1.setFullName("Default Admin");
        admin1.setEmail("admin@flipfit.com");
        admin1.setPassword("admin@123");
        admin1.setPhoneNumber(9000000000L);
        admin1.setCity("Bengaluru");
        admin1.setPincode(560001);
        users.put(admin1.getEmail(), admin1);
    }

    // -------------------------------
    // User Management Methods
    // -------------------------------
    public static User getUserByEmail(String email) {
        return users.get(email);
    }

    public static boolean validateUser(String email, String password) {
        User user = users.get(email);
        return user != null && user.getPassword().equals(password);
    }

    public static void addUser(User user) {
        users.put(user.getEmail(), user);
        if (user instanceof GymCustomer customer) {
            customers.put(user.getEmail(), customer);
        } else if (user instanceof GymOwner owner) {
            owners.add(owner);
        }
    }

    public static boolean userExists(String email) {
        return users.containsKey(email);
    }
}