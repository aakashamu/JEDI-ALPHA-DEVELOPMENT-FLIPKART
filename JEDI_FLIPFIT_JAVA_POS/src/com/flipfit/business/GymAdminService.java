package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymOwner;
import com.flipfit.dao.FlipFitRepository;

public class GymAdminService implements GymAdminInterface {
	
	@Override
	public boolean validateGymOwner(int ownerId) {
		// Search for the gym owner in the repository
		for (GymOwner owner : FlipFitRepository.owners) {
			if (owner.getUserId() == ownerId) {
				System.out.println("Gym Owner " + ownerId + " (" + owner.getFullName() + ") validated successfully");
				return true;
			}
		}
		
		System.out.println("Gym Owner with ID " + ownerId + " not found");
		return false;
	}
	
	@Override
	public boolean approveCentre(int centreId) {
		// Search for the centre in the repository
		for (GymCentre centre : FlipFitRepository.gymCentres) {
			if (centre.getCentreId() == centreId) {
				if (centre.isApproved()) {
					System.out.println("Centre " + centreId + " (" + centre.getName() + ") is already approved");
					return true;
				} else {
					centre.setApproved(true);
					System.out.println("Centre " + centreId + " (" + centre.getName() + ") approved successfully");
					return true;
				}
			}
		}
		
		System.out.println("Centre with ID " + centreId + " not found");
		return false;
	}
	
	@Override
	public boolean deleteOwner(int ownerId) {
		// Search for and remove the gym owner
		for (int i = 0; i < FlipFitRepository.owners.size(); i++) {
			GymOwner owner = FlipFitRepository.owners.get(i);
			if (owner.getUserId() == ownerId) {
				FlipFitRepository.owners.remove(i);
				// Also remove from users map
				FlipFitRepository.users.remove(owner.getEmail());
				System.out.println("Gym Owner " + ownerId + " (" + owner.getFullName() + ") deleted successfully");
				return true;
			}
		}
		
		System.out.println("Gym Owner with ID " + ownerId + " not found");
		return false;
	}
	
	@Override
	public void viewCustomerMetrics() {
		System.out.println("\n--- Customer Metrics ---");
		System.out.println("Total Customers: " + FlipFitRepository.customers.size());
		System.out.println("Total Bookings: " + FlipFitRepository.bookings.size());
		
		// Show customers by city
		System.out.println("\nCustomers by City:");
		FlipFitRepository.customers.values().stream()
			.collect(java.util.stream.Collectors.groupingBy(
				customer -> customer.getCity(),
				java.util.stream.Collectors.counting()
			))
			.forEach((city, count) -> 
				System.out.println("  " + city + ": " + count + " customers")
			);
	}
	
	@Override
	public void viewGymMetrics() {
		System.out.println("\n--- Gym Metrics ---");
		System.out.println("Total Gym Centres: " + FlipFitRepository.gymCentres.size());
		System.out.println("Total Gym Owners: " + FlipFitRepository.owners.size());
		
		// Show approved vs pending centres
		long approvedCentres = FlipFitRepository.gymCentres.stream()
			.filter(GymCentre::isApproved)
			.count();
		long pendingCentres = FlipFitRepository.gymCentres.size() - approvedCentres;
		
		System.out.println("Approved Centres: " + approvedCentres);
		System.out.println("Pending Centres: " + pendingCentres);
		
		// Show centres by city
		System.out.println("\nCentres by City:");
		FlipFitRepository.gymCentres.stream()
			.collect(java.util.stream.Collectors.groupingBy(
				centre -> centre.getCity(),
				java.util.stream.Collectors.counting()
			))
			.forEach((city, count) -> 
				System.out.println("  " + city + ": " + count + " centres")
			);
	}
}
