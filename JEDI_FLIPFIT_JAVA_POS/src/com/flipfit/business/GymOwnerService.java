package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import java.util.ArrayList;
import java.util.List;

public class GymOwnerService implements GymOwnerInterface {
    // Static list to act as a temporary database for this session
    private static List<GymCentre> staticGymCentreList = new ArrayList<>();

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
        System.out.println("Displaying static customer list for Centre ID: " + gymCentreId);
        return new ArrayList<>(); // Return empty list for now
    }

    @Override
    public void requestApproval(int gymOwnerId) {
        System.out.println("Approval request for Gym Owner " + gymOwnerId + " is now PENDING.");
    }
}