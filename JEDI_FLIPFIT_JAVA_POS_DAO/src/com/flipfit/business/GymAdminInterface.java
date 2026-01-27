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
	public boolean validateGymOwner(int ownerId) throws com.flipfit.exception.UserNotFoundException;

	/**
	 * Approves a Gym Centre
	 */
	public boolean approveCentre(int centreId) throws com.flipfit.exception.IssueWithApprovalException;

	/**
	 * Deletes a Gym Owner
	 */
	public boolean deleteOwner(int ownerId) throws com.flipfit.exception.UserNotFoundException;

	/**
	 * Views customer metrics
	 */
	public void viewCustomerMetrics();

	/**
	 * Views gym metrics
	 */
	public void viewGymMetrics();
}
