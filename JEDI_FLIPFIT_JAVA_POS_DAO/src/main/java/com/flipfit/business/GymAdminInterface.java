package com.flipfit.business;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymCentre;
import java.util.List;

/**
 * The Interface GymAdminInterface.
 *
 * @author Ananya
 * @ClassName "GymAdminInterface"
 */
public interface GymAdminInterface {
	/**
	 * Views all gym owners
	 */
	public List<GymOwner> viewAllGymOwners(String email, String password);

	/**
	 * Validates a Gym Owner
	 */
	public boolean validateGymOwner(int ownerId, String email, String password)
			throws com.flipfit.exception.UserNotFoundException;

	/**
	 * Approves a Gym Centre
	 */
	public boolean approveCentre(int centreId, String email, String password)
			throws com.flipfit.exception.IssueWithApprovalException;

	/**
	 * Deletes a Gym Owner
	 */
	public boolean deleteOwner(int ownerId, String email, String password)
			throws com.flipfit.exception.UserNotFoundException;

	/**
	 * Views customer metrics
	 */
	public void viewCustomerMetrics(String email, String password);

	/**
	 * Views gym metrics
	 */
	public void viewGymMetrics(String email, String password);

	/**
	 * Views all gym centres
	 */
	public List<GymCentre> viewAllCentres(String email, String password);
}
