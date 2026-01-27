package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymOwner;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.GymAdminDAOImpl;
import com.flipfit.dao.GymCentreDAOImpl;
import java.util.List;

public class GymAdminService implements GymAdminInterface {
	
	private GymAdminDAOImpl adminDAO = new GymAdminDAOImpl();

	/**
	 * Register a new gym admin - saves to database via DAO
	 */
	public void registerAdmin(String fullName, String email, String password, Long phoneNumber, 
	                          String city, String state, int pincode) {
		adminDAO.registerAdmin(fullName, email, password, phoneNumber, city, state, pincode);
	}
	
	@Override
	public void viewAllGymOwners() {
		// Load owners from database and sync with repository
		java.util.List<GymOwner> dbOwners = adminDAO.getAllOwners();
		
		// Clear and reload the in-memory repository with database data
		FlipFitRepository.owners.clear();
		FlipFitRepository.owners.addAll(dbOwners);
		
		// Also update the users map
		for (GymOwner owner : dbOwners) {
			FlipFitRepository.users.put(owner.getEmail(), owner);
		}
		
		if (FlipFitRepository.owners.isEmpty()) {
			System.out.println("\nNo gym owners registered yet.");
			return;
		}
		
		System.out.println("\n========== ALL GYM OWNERS ==========");
		System.out.println("Total Owners: " + FlipFitRepository.owners.size());
		System.out.println("-----------------------------------------");
		
		for (GymOwner owner : FlipFitRepository.owners) {
			String status = owner.getIsApproved() == 1 ? "✓ Approved" : "⚠ Pending";
			System.out.println("Owner ID: " + owner.getUserId() +
							 " | Name: " + owner.getFullName() +
							 " | Email: " + owner.getEmail() +
							 " | City: " + owner.getCity() +
							 " | PAN: " + owner.getPAN() +
							 " | Status: " + status);
		}
		System.out.println("===================================\n");
	}
	
	public void viewPendingOwners() {
		List<GymOwner> pendingOwners = FlipFitRepository.owners.stream()
			.filter(owner -> owner.getIsApproved() == 0)
			.toList();
		
		if (pendingOwners.isEmpty()) {
			System.out.println("\nNo pending owners to approve.");
			return;
		}
		
		System.out.println("\n========== PENDING OWNERS ==========");
		System.out.println("Total Pending: " + pendingOwners.size());
		System.out.println("-----------------------------------------");
		
		for (GymOwner owner : pendingOwners) {
			System.out.println("Owner ID: " + owner.getUserId() +
							 " | Name: " + owner.getFullName() +
							 " | Email: " + owner.getEmail() +
							 " | City: " + owner.getCity() +
							 " | PAN: " + owner.getPAN());
		}
		System.out.println("===================================\n");
	}
	
	public boolean approveOwner(int ownerId) {
		if (ownerId <= 0) {
			System.out.println("ERROR: Invalid owner ID");
			return false;
		}
		
		// Refresh repository from DB
		viewAllGymOwners();
		
		// First, approve in database
		boolean dbSuccess = adminDAO.approveOwner(ownerId);
		if (!dbSuccess) {
			System.out.println("ERROR: Failed to approve owner in database");
			return false;
		}
		
		// Then update in-memory repository
		for (GymOwner owner : FlipFitRepository.owners) {
			if (owner.getUserId() == ownerId) {
				if (owner.getIsApproved() == 1) {
					System.out.println("Owner " + ownerId + " (" + owner.getFullName() + ") is already approved");
					return true;
				} else {
					owner.setIsApproved(1);
					System.out.println("✓ Gym Owner " + ownerId + " (" + owner.getFullName() + ") approved successfully");
					return true;
				}
			}
		}
		
		System.out.println("ERROR: Gym Owner with ID " + ownerId + " not found");
		return false;
	}
	
	public void viewPendingCentres() {
		// Load centres from database and sync with repository
		GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
		List<GymCentre> dbCentres = centreDAO.selectAllGymCentres();
		
		// Clear and reload the in-memory repository with database data
		FlipFitRepository.gymCentres.clear();
		FlipFitRepository.gymCentres.addAll(dbCentres);
		
		List<GymCentre> pendingCentres = FlipFitRepository.gymCentres.stream()
			.filter(centre -> !centre.isApproved())
			.toList();
		
		if (pendingCentres.isEmpty()) {
			System.out.println("\nNo pending centres.");
			return;
		}
		
		System.out.println("\n========== PENDING CENTRES ==========");
		System.out.println("Total Pending: " + pendingCentres.size());
		System.out.println("-----------------------------------------");
		
		for (GymCentre centre : pendingCentres) {
			int ownerId = centre.getOwnerId();
			String ownerInfo = (ownerId > 0) ? "Owner ID: " + ownerId : "No Owner";
			System.out.println("Centre ID: " + centre.getCentreId() +
							 " | Name: " + centre.getName() +
							 " | City: " + centre.getCity() +
							 " | " + ownerInfo);
		}
		System.out.println("===================================\n");
	}
	
	public void viewAllCentres() {
		// Load centres from database and sync with repository
		GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
		List<GymCentre> dbCentres = centreDAO.selectAllGymCentres();
		
		// Clear and reload the in-memory repository with database data
		FlipFitRepository.gymCentres.clear();
		FlipFitRepository.gymCentres.addAll(dbCentres);
		
		System.out.println("\n========== ALL GYM CENTRES ==========");
		System.out.println("Total Centres: " + FlipFitRepository.gymCentres.size());
		System.out.println("-----------------------------------------");
		
		for (GymCentre centre : FlipFitRepository.gymCentres) {
			String status = centre.isApproved() ? "✓ Approved" : "⚠ Pending";
			System.out.println("Centre ID: " + centre.getCentreId() +
							 " | Name: " + centre.getName() +
							 " | City: " + centre.getCity() +
							 " | Status: " + status);
		}
		System.out.println("===================================\n");
	}
	
	@Override
	public boolean validateGymOwner(int ownerId) {
		if (ownerId <= 0) {
			System.out.println("ERROR: Invalid owner ID");
			return false;
		}
		
		for (GymOwner owner : FlipFitRepository.owners) {
			if (owner.getUserId() == ownerId) {
				System.out.println("✓ Gym Owner " + ownerId + " (" + owner.getFullName() + ") validated successfully");
				return true;
			}
		}
		
		System.out.println("ERROR: Gym Owner with ID " + ownerId + " not found");
		return false;
	}
	
	@Override
	public boolean approveCentre(int centreId) {
		if (centreId <= 0) {
			System.out.println("ERROR: Invalid centre ID");
			return false;
		}
		
		// Refresh repository from DB
		viewAllCentres();
		
		// First, approve in database
		GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
		centreDAO.updateGymCentreApproval(centreId, true);
		
		// Then update in-memory repository
		for (GymCentre centre : FlipFitRepository.gymCentres) {
			if (centre.getCentreId() == centreId) {
				if (centre.isApproved()) {
					System.out.println("Centre " + centreId + " (" + centre.getName() + ") is already approved");
					return true;
				} else {
					centre.setApproved(true);
					System.out.println("✓ Centre " + centreId + " (" + centre.getName() + ") approved successfully");
					return true;
				}
			}
		}
		
		System.out.println("ERROR: Centre with ID " + centreId + " not found");
		return false;
	}
	
	@Override
	public boolean deleteOwner(int ownerId) {
		if (ownerId <= 0) {
			System.out.println("ERROR: Invalid owner ID");
			return false;
		}
		
		// First, delete from database
		boolean dbSuccess = adminDAO.deleteOwner(ownerId);
		if (!dbSuccess) {
			System.out.println("ERROR: Failed to delete owner from database");
			return false;
		}
		
		// Then remove from in-memory repository
		for (int i = 0; i < FlipFitRepository.owners.size(); i++) {
			GymOwner owner = FlipFitRepository.owners.get(i);
			if (owner.getUserId() == ownerId) {
				FlipFitRepository.owners.remove(i);
				FlipFitRepository.users.remove(owner.getEmail());
				System.out.println("✓ Gym Owner " + ownerId + " (" + owner.getFullName() + ") deleted successfully");
				return true;
			}
		}
		
		System.out.println("ERROR: Gym Owner with ID " + ownerId + " not found");
		return false;
	}
	
	@Override
	public void viewCustomerMetrics() {
		System.out.println("\n--- Customer Metrics ---");
		System.out.println("Total Customers: " + FlipFitRepository.customers.size());
		System.out.println("Total Bookings: " + FlipFitRepository.allBookings.size());
		
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
		
		// Show all centres with their status
		System.out.println("\nAll Centres:");
		FlipFitRepository.gymCentres.forEach(centre -> {
			String status = centre.isApproved() ? "✓ Approved" : "⚠ Pending";
			int ownerId = centre.getOwnerId();
			String ownerInfo = (ownerId > 0) ? "Owner ID: " + ownerId : "No Owner";
			System.out.println("  " + centre.getCentreId() + ". " + centre.getName() + 
							   " | City: " + centre.getCity() + 
							   " | " + ownerInfo + " | " + status);
		});
		
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
