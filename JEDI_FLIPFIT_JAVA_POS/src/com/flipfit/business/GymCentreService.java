/**
 * 
 */
package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.SlotAvailability;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

public class GymCentreService implements GymCentreInterface {
    
	@Override
    public List<SlotAvailability> viewAvailableSlots(int centreId) {
        return null; 
    }

    @Override
    public GymCentre getCentreDetails(int centreId) {
        return null;
    }

    @Override
    public void addGymCentre(GymCentre centre) {
    }
}
