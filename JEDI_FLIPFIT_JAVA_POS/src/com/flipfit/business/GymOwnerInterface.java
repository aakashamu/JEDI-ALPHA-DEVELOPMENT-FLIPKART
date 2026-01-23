/**
 * 
 */
package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;

/**
 * 
 */
public interface GymOwnerInterface {
	
	
	public void addCentre(GymCentre centre);
	
	public List<GymCustomer> viewCustomers();
	

}
