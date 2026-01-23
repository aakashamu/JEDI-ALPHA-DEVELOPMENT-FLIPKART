/**
 * 
 */
package com.flipflit.business;
import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCentre;
import java.util.List;
/**
 * 
 */
public class GymCustomerService implements GymCustomerInterface {

    @Override
    public List<GymCentre> viewCentres() {
        System.out.println("this is service: viewCentres");
        return null; 
    }

    @Override
    public List<Booking> viewBookedSlots() {
        System.out.println("this is service: viewBookedSlots");
        return null;
    }

    @Override
    public Booking bookSlot(int slotAvailabilityId) {
        System.out.println("this is service: bookSlot for slotId: " + slotAvailabilityId);
        return null;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        System.out.println("this is service: cancelBooking for bookingId: " + bookingId);
        return false;
    }

    @Override
    public void editDetails(String fullName, String email, long phoneNumber, String city, int pincode) {
        System.out.println("this is service: editDetails for user: " + fullName);
    }
}