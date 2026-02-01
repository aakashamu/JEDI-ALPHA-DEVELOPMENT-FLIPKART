package com.flipfit.client;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymOwner;
import com.flipfit.business.GymAdminService;
import java.util.List;
/**
 * The Class GymAdminDemo.
 *
 * @author Ananya
 * @ClassName  "GymAdminDemo"
 */
public class GymAdminDemo {
  /**
   * Run demo.
   */
    public static void runDemo() {
        System.out.println("========== Gym Admin Demo ==========\n");

        GymAdminService gymAdminService = new GymAdminService();

        // Test 1: View pending approvals
        System.out.println("Test 1: View Pending Approvals");
        System.out.println("------------------------------");
        List<GymCentre> pendingCentres = gymAdminService.getPendingApprovals();
        if (pendingCentres.isEmpty()) {
            System.out.println("No pending approvals.");
        } else {
            for (GymCentre centre : pendingCentres) {
                System.out.println("Centre ID: " + centre.getCentreId() + ", Name: " + centre.getName() 
                    + ", City: " + centre.getCity());
            }
        }

        // Test 2: Approve gym centre
        System.out.println("\nTest 2: Approve Gym Centre");
        System.out.println("--------------------------");
        boolean approved = gymAdminService.approveCentre(1001);
        System.out.println("Centre approved: " + approved);

        // Test 3: View all gym centres
        System.out.println("\nTest 3: View All Gym Centres");
        System.out.println("-----------------------------");
        List<GymCentre> centres = gymAdminService.getAllGymCentres();
        for (GymCentre centre : centres) {
            System.out.println("Centre ID: " + centre.getCentreId() + ", Name: " + centre.getName() 
                + ", Approved: " + centre.isApproved());
        }

        // Test 4: View all gym owners
        System.out.println("\nTest 4: View All Gym Owners");
        System.out.println("---------------------------");
        List<GymOwner> owners = gymAdminService.getAllGymOwners();
        for (GymOwner owner : owners) {
            System.out.println("Owner ID: " + owner.getOwnerId() + ", Name: " + owner.getName());
        }

        // Test 5: Display system statistics
        System.out.println("\nTest 5: System Statistics");
        System.out.println("------------------------");
        gymAdminService.displaySystemStatistics();

        System.out.println("\n========== Demo Complete ==========");
    }
}
