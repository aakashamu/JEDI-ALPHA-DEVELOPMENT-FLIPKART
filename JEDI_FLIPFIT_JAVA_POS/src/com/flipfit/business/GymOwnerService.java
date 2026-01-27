package com.flipfit.business;

import com.flipfit.bean.GymOwner; // Essential import
import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import java.util.ArrayList;
import java.util.List;

public class GymOwnerService implements GymOwnerInterface {
    private static List<GymCentre> staticGymCentreList = new ArrayList<>();
    // Static list for registration and approval tracking
    private static List<GymOwner> registeredOwners = new ArrayList<>();

    @Override
    public void register(GymOwner owner) {
        registeredOwners.add(owner); // Starts with isApproved = 0
        System.out.println("Registration request received for: " + owner.getFullName());
    }

    // Method to provide the list of pending requests for the Admin
    @Override
    public List<GymOwner> getPendingRequests() {
        List<GymOwner> pending = new ArrayList<>();
        for (GymOwner owner : registeredOwners) {
            if (owner.getIsApproved() == 0) { // Check UML status
                pending.add(owner);
            }
        }
        return pending;
    }

    public boolean isValidOwner(int id) {
        for (GymOwner owner : registeredOwners) {
            if (owner.getUserId() == id) return true;
        }
        return false;
    }

    @Override
    public void addCentre(GymCentre centre) {
        staticGymCentreList.add(centre);
        System.out.println("Successfully added Gym Centre: " + centre.getName());
    }

    @Override
    public List<GymCentre> viewMyCentres() {
        return staticGymCentreList;
    }

    @Override
    public List<GymCustomer> viewCustomers(int gymCentreId) {
        return new ArrayList<>();
    }

    @Override
    public void requestApproval(int gymOwnerId) {
        System.out.println("Approval request for Gym Owner " + gymOwnerId + " is now PENDING.");
    }

    @Override
    public void approveOwner(int userId) { // Signature must match Interface
        for (GymOwner owner : registeredOwners) {
            if (owner.getUserId() == userId) {
                owner.setIsApproved(1);
                System.out.println("Owner " + owner.getFullName() + " is now APPROVED.");
                return;
            }
        }
        System.out.println("Owner ID not found.");
    }
}