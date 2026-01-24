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

    static {
        // Initialize sample users for testing
        initializeSampleUsers();
        
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
        customers.put("ananya@gmail.com", customer1);
        users.put("ananya@gmail.com", customer1);
        
        GymCustomer customer2 = new GymCustomer();
        customer2.setUserId(102);
        customer2.setFullName("Supriya Nalla");
        customer2.setEmail("surpiya@gmail.com");
        customer2.setPassword("password@456");
        customer2.setPhoneNumber(7094561234L);
        customer2.setCity("Hyderabad");
        customer2.setPincode(500008);
        customers.put("supriya@gmail.com", customer2);
        users.put("supriya@gmail.com", customer2);
        
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
        users.put("sam@gmail.com", owner1);
        
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
        users.put("sujitha@gmail.com", owner2);
    }
    
    // User management methods
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