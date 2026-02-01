package com.flipfit.client;

import com.flipfit.business.GymCustomerService;
import com.flipfit.bean.GymCentre;
import java.util.List;

/**
 * The Class GymCustomerDemo.
 *
 * @author Ananya
 * @ClassName "GymCustomerDemo"
 */
public class GymCustomerDemo {

    /**
     * Run demo.
     */
    public static void runDemo() {
        System.out.println("\n========== Gym Customer Demo ==========\n");

        GymCustomerService gymCustomerService = new GymCustomerService();

        // Test 1: Register customer
        System.out.println("Test 1: Registering Gym Customer");
        System.out.println("--------------------------------");
        gymCustomerService.registerCustomer("John Doe", "cust@user.com", "Pass@123", 7777777777L,
                "Delhi", "Delhi", 110001);
        System.out.println("Customer registered successfully!\n");

        // Test 2: View available centres
        System.out.println("Test 2: View Available Centres");
        System.out.println("------------------------------");
        List<GymCentre> centres = gymCustomerService.viewCentres("cust@user.com", "Pass@123");
        System.out.println("Total centres available: " + centres.size() + "\n");

        System.out.println("========== Demo Complete ==========\n");
    }
}
