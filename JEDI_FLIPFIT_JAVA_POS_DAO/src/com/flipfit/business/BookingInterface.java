/**
 * */
package com.flipfit.business;

import com.flipfit.bean.Booking;
import java.util.List;
/**
 * The Interface BookingInterface.
 *
 * @author Ananya
 * @ClassName  "BookingInterface"
 */
public interface BookingInterface {

   
    public Booking createBooking(int customerId, int availabilityId);

    
    public boolean cancelBooking(int bookingId);

    
    public List<Booking> getCustomerBookings(int customerId);

   
    public boolean checkBookingStatus(int bookingId);

}
