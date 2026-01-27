/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public interface GymAdminInterface {
	/**
	 * Views all gym owners
	 */
	public void viewAllGymOwners();
	
	/**
	 * Validates a Gym Owner
	 */
	public boolean validateGymOwner(int ownerId);
	
	/**
	 * Approves a Gym Centre
	 */
	public boolean approveCentre(int centreId);
	
	/**
	 * Deletes a Gym Owner
	 */
	public boolean deleteOwner(int ownerId);
	
	/**
	 * Views customer metrics
	 */
	public void viewCustomerMetrics();
	
	/**
	 * Views gym metrics
	 */
	public void viewGymMetrics();
}
