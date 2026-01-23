/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public class GymAdminService implements GymAdminInterface {
	
	@Override
	public boolean validateGymOwner(int ownerId) {
		System.out.println("this is service: validateGymOwner for ownerId: " + ownerId);
		return false;
	}
	
	@Override
	public boolean approveCentre(int centreId) {
		System.out.println("this is service: approveCentre for centreId: " + centreId);
		return false;
	}
	
	@Override
	public boolean deleteOwner(int ownerId) {
		System.out.println("this is service: deleteOwner for ownerId: " + ownerId);
		return false;
	}
	
	@Override
	public void viewCustomerMetrics() {
		System.out.println("this is service: viewCustomerMetrics");
	}
	
	@Override
	public void viewGymMetrics() {
		System.out.println("this is service: viewGymMetrics");
	}
}
