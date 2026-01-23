/**
 * 
 */
package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.SlotAvailability;
import java.util.List;

/**
 * 
 */
public interface GymCentreInterface {
	
	public List<SlotAvailability> viewAvailableSlots(int centreId);
    public GymCentre getCentreDetails(int centreId);
    public void addGymCentre(GymCentre centre);

}
