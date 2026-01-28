package com.flipfit.dao;

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
    public boolean registerNewCentre(GymCentre centre);
    public List<GymCentre> viewMyCentres(int ownerId);
    public List<GymCustomer> viewCustomers(int gymCentreId);
    public void requestApproval(int gymOwnerId);
    public boolean cancelBooking(int bookingId);
}