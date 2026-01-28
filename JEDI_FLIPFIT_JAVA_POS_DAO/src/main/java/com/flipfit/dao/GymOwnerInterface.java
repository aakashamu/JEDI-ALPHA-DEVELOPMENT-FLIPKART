package com.flipfit.dao;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import java.util.List;

public interface GymOwnerInterface {
    public boolean registerNewCentre(GymCentre centre);
    public List<GymCentre> viewMyCentres(int ownerId);
    public List<GymCustomer> viewCustomers(int gymCentreId);
    public void requestApproval(int gymOwnerId);
    public boolean cancelBooking(int bookingId);
}