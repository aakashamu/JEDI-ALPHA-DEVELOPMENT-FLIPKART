/**
 * 
 */
package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.SlotAvailability;

/**
 * 
 */
public interface GymCentreInterface {
	
	public List<SlotAvailability> viewAvailableSlots(int centreId);
    public GymCentre getCentreDetails(int centreId);
    public void addGymCentre(GymCentre centre);

}
