package com.flipfit.client;

import com.flipfit.bean.GymCentre;
import com.flipfit.business.GymOwnerService;
import java.util.List;
/**
 * The Class GymOwnerDemo.
 *
 * @author Ananya
 * @ClassName  "GymOwnerDemo"
 */
public class GymOwnerDemo {
  /**
   * Run demo.
   */
    public static void runDemo() {
        System.out.println("========== Gym Owner Demo ==========\n");

        GymOwnerService gymOwnerService = new GymOwnerService();

        // Test 1: Add a new gym centre
        System.out.println("Test 1: Adding Gym Centre");
        System.out.println("------------------------");
        GymCentre centre = new GymCentre();
        centre.setCentreId(1001);
        centre.setName("FitZone Bangalore");
        centre.setCity("Bangalore");
        centre.setState("Karnataka");
        centre.setPincode(560001);
        centre.setOwnerId(101);
        centre.setApproved(false);

        gymOwnerService.addCentre(centre);
        System.out.println("Gym Centre added successfully!\n");

        // Test 2: View all my centres
        System.out.println("Test 2: View All My Centres");
        System.out.println("--------------------------");
        List<GymCentre> myCentres = gymOwnerService.viewMyCentres();
        if (myCentres.isEmpty()) {
            System.out.println("No centres found.");
        } else {
            for (GymCentre c : myCentres) {
                System.out.println("Centre ID: " + c.getCentreId() + ", Name: " + c.getName() 
                    + ", City: " + c.getCity());
            }
        }

        // Test 3: Request approval
        System.out.println("\nTest 3: Request Approval");
        System.out.println("------------------------");
        gymOwnerService.requestApproval(101);
        System.out.println("Approval request sent!\n");

        // Test 4: Update centre
        System.out.println("Test 4: Update Centre");
        System.out.println("---------------------");
        if (!myCentres.isEmpty()) {
            GymCentre updateCentre = myCentres.get(0);
            updateCentre.setApproved(true);
            gymOwnerService.updateCentre(updateCentre);
            System.out.println("Centre updated successfully!");
        }

        System.out.println("\n========== Demo Complete ==========");
    }
}