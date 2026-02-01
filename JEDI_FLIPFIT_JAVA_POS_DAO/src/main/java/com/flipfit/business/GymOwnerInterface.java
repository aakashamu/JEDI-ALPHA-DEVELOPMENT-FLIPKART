package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import java.util.List;

/**
 * The Interface GymOwnerInterface.
 *
 * @author Ananya
 * @ClassName "GymOwnerInterface"
 */
public interface GymOwnerInterface {
    void addCentre(GymCentre centre, String email, String password);

    boolean registerNewCentre(String centreName, String city, int numberOfSlots, int slotCapacity, String email,
            String password);

    List<GymCentre> viewMyCentres(String email, String password);

    List<GymCustomer> viewCustomers(int gymCentreId, String email, String password);

    void requestApproval(int gymOwnerId, String email, String password);

    void setupSlotsForExistingCentre(int centreId, int numSlots, int capacity, String email, String password);
}