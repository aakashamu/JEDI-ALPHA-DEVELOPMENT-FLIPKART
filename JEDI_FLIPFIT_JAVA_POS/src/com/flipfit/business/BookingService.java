/**
 * */
package com.flipfit.business;

import com.flipfit.bean.Booking;
import java.util.List;
import java.util.ArrayList;

package com.flipfit.business;

/**
 * Implementation of the BookingInterface handling the business logic 
 * for gym slot reservations.`
 */
public class BookingService implements BookingInterface {

    @Override
    public Booking createBooking(int customerId, int availabilityId) {
        
        return null; 
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        
        return false;
    }

    @Override
    public List<Booking> getCustomerBookings(int customerId) {
       
        return new ArrayList<>();
    }

    @Override
    public boolean checkBookingStatus(int bookingId) {
      
        return false;
    }
}
