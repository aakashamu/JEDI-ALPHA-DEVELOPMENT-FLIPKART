package com.flipfit.client;

import com.flipfit.business.GymAdminService;
import java.util.List;
import com.flipfit.bean.GymOwner;

/**
 * The Class GymAdminDemo.
 *
 * @author Ananya
 * @ClassName "GymAdminDemo"
 */
public class GymAdminDemo {

    /**
     * Run demo.
     */
    public static void runDemo() {
        System.out.println("\n========== Gym Admin Demo ==========\n");

        GymAdminService gymAdminService = new GymAdminService();

        // Test 1: Register admin
        System.out.println("Test 1: Registering Gym Admin");
        System.out.println("-----------------------------");
        gymAdminService.registerAdmin("Admin User", "admin@test.com", "Pass@123", 9999999999L,
                "Bangalore", "Karnataka", 560001);
        System.out.println("Admin registered successfully!\n");

        // Test 2: View all gym owners
        System.out.println("Test 2: View All Gym Owners");
        System.out.println("---------------------------");
        List<GymOwner> owners = gymAdminService.viewAllGymOwners("admin@test.com", "Pass@123");
        System.out.println("Total owners retrieved: " + owners.size() + "\n");

        System.out.println("========== Demo Complete ==========\n");
    }
}
