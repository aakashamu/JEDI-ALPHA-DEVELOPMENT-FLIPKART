package com.flipfit.business;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import java.util.List;

public interface GymOwnerInterface {
    void addCentre(GymCentre centre);
    List<GymCentre> viewMyCentres();
    List<GymCustomer> viewCustomers(int gymCentreId);
    void requestApproval(int gymOwnerId);
    List<GymOwner> getPendingRequests();;
    void approveOwner(int userId); // The contract
    void register(GymOwner owner); // Ensure this is here
}