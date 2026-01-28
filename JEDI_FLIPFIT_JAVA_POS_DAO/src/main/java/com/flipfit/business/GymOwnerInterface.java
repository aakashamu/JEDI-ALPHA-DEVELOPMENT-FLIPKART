package com.flipfit.business;
import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import java.util.List;
/**
 * The Interface GymOwnerInterface.
 *
 * @author Ananya
 * @ClassName  "GymOwnerInterface"
 */
public interface GymOwnerInterface {
    void addCentre(GymCentre centre);
    boolean registerNewCentre(String centreName, String city, int numberOfSlots, int slotCapacity);
    List<GymCentre> viewMyCentres();
    List<GymCustomer> viewCustomers(int gymCentreId);
    void requestApproval(int gymOwnerId);
    void setupSlotsForExistingCentre(int centreId, int numSlots, int capacity);
}