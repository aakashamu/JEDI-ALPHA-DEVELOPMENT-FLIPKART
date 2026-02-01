package com.flipfit.client;

import com.flipfit.bean.GymCentre;
import com.flipfit.business.GymOwnerService;

/**
 * The Class GymOwnerDemo.
 *
 * @author Ananya
 * @ClassName "GymOwnerDemo"
 */
public class GymOwnerDemo {

    /**
     * Run demo.
     */
    public static void runDemo() {
        System.out.println("\n========== Gym Owner Demo ==========\n");

        GymOwnerService gymOwnerService = new GymOwnerService();

        // Test 1: Register a gym owner
        System.out.println("Test 1: Registering Gym Owner");
        System.out.println("-----------------------------");
        gymOwnerService.registerOwner("Raj Kumar", "owner@gym.com", "Pass@123", 8888888888L,
                "Mumbai", "Maharashtra", 400001, "ABCDE1234F", "123456789012", "29ABCDE1234F1Z5");
        System.out.println("Owner registered successfully!\n");

        // Test 2: Add a gym centre
        System.out.println("Test 2: Adding Gym Centre");
        System.out.println("------------------------");
        GymCentre centre = new GymCentre();
        centre.setCentreId(1001);
        centre.setName("FitZone Mumbai");
        centre.setCity("Mumbai");
        centre.setState("Maharashtra");
        centre.setPincode(400001);

        gymOwnerService.addCentre(centre, "owner@gym.com", "Pass@123");
        System.out.println("Gym Centre added successfully!\n");

        System.out.println("========== Demo Complete ==========\n");
    }
}
