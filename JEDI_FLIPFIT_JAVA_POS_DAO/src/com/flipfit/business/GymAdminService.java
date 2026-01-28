package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymOwner;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.GymAdminDAOImpl;
import com.flipfit.dao.GymCentreDAOImpl;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class GymAdminService.
 *
 * @author Ananya
 * @ClassName "GymAdminService"
 */
public class GymAdminService implements GymAdminInterface {

    private GymAdminDAOImpl adminDAO = new GymAdminDAOImpl();

    /**
     * Register admin.
     *
     * @param fullName    the full name
     * @param email       the email
     * @param password    the password
     * @param phoneNumber the phone number
     * @param city        the city
     * @param state       the state
     * @param pincode     the pincode
     */
    public void registerAdmin(String fullName, String email, String password, Long phoneNumber,
            String city, String state, int pincode) {
        adminDAO.registerAdmin(fullName, email, password, phoneNumber, city, state, pincode);
    }

    /**
     * View all gym owners.
     */
    @Override
    public void viewAllGymOwners() {
        java.util.List<GymOwner> dbOwners = adminDAO.getAllOwners();

        FlipFitRepository.owners.clear();
        FlipFitRepository.owners.addAll(dbOwners);

        dbOwners.forEach(owner -> FlipFitRepository.users.put(owner.getEmail(), owner));

        if (FlipFitRepository.owners.isEmpty()) {
            System.out.println("\nNo gym owners registered yet.");
            return;
        }

        System.out.println("\n========== ALL GYM OWNERS ==========");
        System.out.println("Total Owners: " + FlipFitRepository.owners.size());
        System.out.println("-----------------------------------------");

        System.out.println("\n--- APPROVED OWNERS ---");
        getOwnersByStatus(true).forEach(this::printOwnerDetails);

        System.out.println("\n--- PENDING OWNERS ---");
        getOwnersByStatus(false).forEach(this::printOwnerDetails);

        System.out.println("===================================\n");
    }

    /**
     * Get owners by status.
     * 
     * @param isApproved the approval status to filter by
     * @return list of gym owners matching the status
     */
    public List<GymOwner> getOwnersByStatus(boolean isApproved) {
        return FlipFitRepository.owners.stream()
                .filter(owner -> (owner.getIsApproved() == 1) == isApproved)
                .collect(Collectors.toList());
    }

    /**
     * Print owner details.
     * 
     * @param owner the gym owner
     */
    public void printOwnerDetails(GymOwner owner) {
        String status = owner.getIsApproved() == 1 ? "✓ Approved" : "⚠ Pending";
        System.out.println("Owner ID: " + owner.getUserId() +
                " | Name: " + owner.getFullName() +
                " | Email: " + owner.getEmail() +
                " | City: " + owner.getCity() +
                " | PAN: " + owner.getPAN() +
                " | Status: " + status);
    }
    /**
     * Print centre details.
     * 
     * @param centre the gym centre
     */
    public void printCentreDetails(GymCentre centre) {
        String status = centre.isApproved() ? "✓ Approved" : "⚠ Pending";
        int ownerId = centre.getOwnerId();
        String ownerInfo = (ownerId > 0) ? "Owner ID: " + ownerId : "No Owner";
        System.out.println("Centre ID: " + centre.getCentreId() +
                " | Name: " + centre.getName() +
                " | City: " + centre.getCity() +
                " | " + ownerInfo +
                " | Status: " + status);
    }

    /**
     * View pending owners.
     */
    public void viewPendingOwners() {
        List<GymOwner> pendingOwners = getOwnersByStatus(false);

        if (pendingOwners.isEmpty()) {
            System.out.println("\nNo pending owners to approve.");
            return;
        }

        System.out.println("\n========== PENDING OWNERS ==========");
        System.out.println("Total Pending: " + pendingOwners.size());
        System.out.println("-----------------------------------------");
        pendingOwners.forEach(this::printOwnerDetails); // Method reference in forEach
        System.out.println("===================================\n");
    }

    /**
     * Approve owner.
     *
     * @param ownerId the owner id
     * @return true, if successful
     */
    public boolean approveOwner(int ownerId) {
        if (ownerId <= 0) {
            System.out.println("ERROR: Invalid owner ID");
            return false;
        }

        viewAllGymOwners(); // Refresh from DB

        boolean dbSuccess = adminDAO.approveOwner(ownerId);
        if (!dbSuccess) {
            System.out.println("ERROR: Failed to approve owner in database");
            return false;
        }

        return FlipFitRepository.owners.stream()
                .filter(o -> o.getUserId() == ownerId)
                .findFirst()
                .map(owner -> {
                    if (owner.getIsApproved() == 1) {
                        System.out.println(
                                "Owner " + ownerId + " (" + owner.getFullName() + ") is already approved");
                    } else {
                        owner.setIsApproved(1);
                        System.out.println("✓ Gym Owner " + ownerId + " (" + owner.getFullName()
                                + ") approved successfully");
                    }
                    return true;
                }).orElseGet(() -> {
                    System.out.println("ERROR: Gym Owner with ID " + ownerId + " not found");
                    return false;
                });
    }

    /**
     * View pending centres.
     */
    public void viewPendingCentres() {
        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> dbCentres = centreDAO.selectAllGymCentres();

        FlipFitRepository.gymCentres.clear();
        FlipFitRepository.gymCentres.addAll(dbCentres);

        // Filter using Stream API 
        List<GymCentre> pendingCentres = getCentresByStatus(false);

        if (pendingCentres.isEmpty()) {
            System.out.println("\nNo pending centres.");
            return;
        }

        System.out.println("\n========== PENDING CENTRES ==========");
        System.out.println("Total Pending: " + pendingCentres.size());
        System.out.println("-----------------------------------------");

        pendingCentres.forEach(this::printCentreDetails); // Method reference iteration
        System.out.println("===================================\n");
    }

    /**
     * View all centres.
     */
    @Override
    public void viewAllCentres() {
        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> dbCentres = centreDAO.selectAllGymCentres();

        FlipFitRepository.gymCentres.clear();
        FlipFitRepository.gymCentres.addAll(dbCentres);

        System.out.println("\n========== ALL GYM CENTRES ==========");
        System.out.println("Total Centres: " + FlipFitRepository.gymCentres.size());
        System.out.println("-----------------------------------------");

        // Filtered Views 
        System.out.println("\n--- APPROVED CENTRES ---");
        getCentresByStatus(true).forEach(this::printCentreDetails);

        System.out.println("\n--- PENDING CENTRES ---");
        getCentresByStatus(false).forEach(this::printCentreDetails);

        System.out.println("===================================\n");
    }

    /**
     * Get centres by status.
     * 
     * @param isApproved the approval status to filter by
     * @return list of gym centres matching the status
     */
    public List<GymCentre> getCentresByStatus(boolean isApproved) {
        return FlipFitRepository.gymCentres.stream()
                .filter(centre -> centre.isApproved() == isApproved)
                .collect(Collectors.toList());
    }



    /**
     * Validate gym owner.
     *
     * @param ownerId the owner id
     * @return true, if successful
     * @throws com.flipfit.exception.UserNotFoundException the user not found exception
     */
    @Override
    public boolean validateGymOwner(int ownerId) throws com.flipfit.exception.UserNotFoundException {
        if (ownerId <= 0) {
            throw new com.flipfit.exception.UserNotFoundException("ERROR: Invalid owner ID");
        }

        viewAllGymOwners();

        // Stream implementation to find owner
        boolean exists = FlipFitRepository.owners.stream()
                .anyMatch(owner -> owner.getUserId() == ownerId);

        if (exists) {
            System.out.println("✓ Gym Owner " + ownerId + " validated successfully");
            return true;
        }

        throw new com.flipfit.exception.UserNotFoundException("ERROR: Gym Owner with ID " + ownerId + " not found");
    }

    /**
     * Approve centre.
     *
     * @param centreId the centre id
     * @return true, if successful
     * @throws com.flipfit.exception.IssueWithApprovalException the issue with approval exception
     */
    @Override
    public boolean approveCentre(int centreId) throws com.flipfit.exception.IssueWithApprovalException {
        if (centreId <= 0) {
            throw new com.flipfit.exception.IssueWithApprovalException("ERROR: Invalid centre ID");
        }

        viewAllCentres();

        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        centreDAO.updateGymCentreApproval(centreId, true);

        return FlipFitRepository.gymCentres.stream()
                .filter(c -> c.getCentreId() == centreId)
                .findFirst()
                .map(centre -> {
                    if (centre.isApproved()) {
                        System.out.println(
                                "Centre " + centreId + " (" + centre.getName() + ") is already approved");
                    } else {
                        centre.setApproved(true);
                        System.out.println(
                                "✓ Centre " + centreId + " (" + centre.getName() + ") approved successfully");
                    }
                    return true;
                }).orElseThrow(() -> new com.flipfit.exception.IssueWithApprovalException(
                        "ERROR: Centre with ID " + centreId + " not found"));
    }

    /**
     * Delete owner.
     *
     * @param ownerId the owner id
     * @return true, if successful
     * @throws com.flipfit.exception.UserNotFoundException the user not found exception
     */
    @Override
    public boolean deleteOwner(int ownerId) throws com.flipfit.exception.UserNotFoundException {
        if (ownerId <= 0) {
            throw new com.flipfit.exception.UserNotFoundException("ERROR: Invalid owner ID");
        }

        boolean dbSuccess = adminDAO.deleteOwner(ownerId);
        if (!dbSuccess) {
            throw new com.flipfit.exception.UserNotFoundException(
                    "ERROR: Failed to delete owner from database (Owner not found or DB error)");
        }

        // Implementation using removeIf with Functional Interface
        boolean removed = FlipFitRepository.owners.removeIf(owner -> {
            if (owner.getUserId() == ownerId) {
                FlipFitRepository.users.remove(owner.getEmail());
                System.out.println(
                        "✓ Gym Owner " + ownerId + " (" + owner.getFullName() + ") deleted successfully");
                return true;
            }
            return false;
        });

        if (!removed) {
            throw new com.flipfit.exception.UserNotFoundException("ERROR: Gym Owner with ID " + ownerId + " not found");
        }
        return true;
    }

    /**
     * View customer metrics.
     */
    @Override
    public void viewCustomerMetrics() {
        List<com.flipfit.bean.GymCustomer> allCustomers = adminDAO.getAllCustomers();
        List<com.flipfit.bean.Booking> allBookings = adminDAO.getAllBookings();

        // Sync with forEach
        FlipFitRepository.customers.clear();
        allCustomers.forEach(c -> FlipFitRepository.customers.put(c.getEmail(), c));

        FlipFitRepository.allBookings.clear();
        FlipFitRepository.allBookings.addAll(allBookings);

        System.out.println("\n--- Customer Metrics ---");
        System.out.println("Total Customers: " + allCustomers.size());
        System.out.println("Total Bookings: " + allBookings.size());

        System.out.println("\nCustomers by City:");
        allCustomers.stream()
                .collect(Collectors.groupingBy(
                        com.flipfit.bean.GymCustomer::getCity,
                        Collectors.counting()))
                .forEach((city, count) -> System.out.println("  " + city + ": " + count + " customers"));
    }

    /**
     * View gym metrics.
     */
    @Override
    public void viewGymMetrics() {
        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> allCentres = centreDAO.selectAllGymCentres();
        List<GymOwner> allOwners = adminDAO.getAllOwners();

        FlipFitRepository.gymCentres.clear();
        FlipFitRepository.gymCentres.addAll(allCentres);
        FlipFitRepository.owners.clear();
        FlipFitRepository.owners.addAll(allOwners);

        System.out.println("\n--- Gym Metrics ---");
        System.out.println("Total Gym Centres: " + allCentres.size());
        System.out.println("Total Gym Owners: " + allOwners.size());

        long approvedCentres = getCentresByStatus(true).size(); // Reusing our stream method
        long pendingCentres = getCentresByStatus(false).size();

        System.out.println("Approved Centres: " + approvedCentres);
        System.out.println("Pending Centres: " + pendingCentres);

        System.out.println("\nAll Centres:");
        allCentres.forEach(this::printCentreDetails);

        System.out.println("\nCentres by City:");
        allCentres.stream()
                .collect(Collectors.groupingBy(
                        GymCentre::getCity,
                        Collectors.counting()))
                .forEach((city, count) -> System.out.println("  " + city + ": " + count + " centres"));
    }
}
