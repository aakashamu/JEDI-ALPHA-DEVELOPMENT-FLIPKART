package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymOwner;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.GymAdminDAOImpl;
import com.flipfit.dao.GymCentreDAOImpl;
import java.util.List;
import java.util.ArrayList;

/**
 * The Class GymAdminService.
 *
 * @author Ananya
 * @ClassName "GymAdminService"
 */
public class GymAdminService implements GymAdminInterface {

    private GymAdminDAOImpl adminDAO = new GymAdminDAOImpl();
    private UserService userService = new UserService();

    public void registerAdmin(String fullName, String email, String password, Long phoneNumber,
            String city, String state, int pincode) {
        adminDAO.registerAdmin(fullName, email, password, phoneNumber, city, state, pincode);
    }

    @Override
    public List<GymOwner> viewAllGymOwners(String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("Authentication Failed");
            return new ArrayList<>();
        }

        List<GymOwner> dbOwners = adminDAO.getAllOwners();
        FlipFitRepository.owners.clear();
        FlipFitRepository.owners.addAll(dbOwners);

        if (dbOwners.isEmpty()) {
            System.out.println("\nNo gym owners registered yet.");
        } else {
            System.out.println("\n========== ALL GYM OWNERS ==========");
            System.out.println("Total Owners: " + dbOwners.size());
            System.out.println("-----------------------------------------");
            dbOwners.forEach(owner -> {
                String status = (owner.getIsApproved() == 1) ? "✓ Approved" : "⚠ Pending";
                System.out.println("Owner ID: " + owner.getUserId() +
                        " | Name: " + owner.getFullName() +
                        " | Email: " + owner.getEmail() +
                        " | Status: " + status);
            });
            System.out.println("=====================================\n");
        }
        return dbOwners;
    }

    @Override
    public List<GymCentre> viewAllCentres(String email, String password) {
        if (!userService.validateUser(email, password)) {
            System.out.println("Authentication Failed");
            return new ArrayList<>();
        }

        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        List<GymCentre> dbCentres = centreDAO.selectAllGymCentres();

        FlipFitRepository.gymCentres.clear();
        FlipFitRepository.gymCentres.addAll(dbCentres);

        if (dbCentres.isEmpty()) {
            System.out.println("\nNo gym centres registered yet.");
        } else {
            System.out.println("\n========== ALL GYM CENTRES ==========");
            System.out.println("Total Centres: " + dbCentres.size());
            System.out.println("-----------------------------------------");
            dbCentres.forEach(centre -> {
                String status = centre.isApproved() ? "✓ Approved" : "⚠ Pending";
                System.out.println("Centre ID: " + centre.getCentreId() +
                        " | Name: " + centre.getName() +
                        " | City: " + centre.getCity() +
                        " | Status: " + status);
            });
            System.out.println("=====================================\n");
        }

        return dbCentres;
    }

    @Override
    public boolean validateGymOwner(int ownerId, String email, String password)
            throws com.flipfit.exception.UserNotFoundException {
        if (!userService.validateUser(email, password)) {
            throw new com.flipfit.exception.UserNotFoundException("Authentication Failed");
        }
        if (ownerId <= 0) {
            throw new com.flipfit.exception.UserNotFoundException("ERROR: Invalid owner ID");
        }

        // Call DAO to approve the owner in database
        boolean dbSuccess = adminDAO.approveOwner(ownerId);
        if (!dbSuccess) {
            throw new com.flipfit.exception.UserNotFoundException("ERROR: Failed to approve Gym Owner in database");
        }

        viewAllGymOwners(email, password); // refresh repository from DB

        boolean exists = FlipFitRepository.owners.stream()
                .anyMatch(owner -> owner.getUserId() == ownerId);

        if (exists) {
            System.out.println("✓ Gym Owner " + ownerId + " validated and approved successfully");
            return true;
        }

        throw new com.flipfit.exception.UserNotFoundException("ERROR: Gym Owner with ID " + ownerId + " not found");
    }

    @Override
    public boolean approveCentre(int centreId, String email, String password)
            throws com.flipfit.exception.IssueWithApprovalException {
        if (!userService.validateUser(email, password)) {
            throw new com.flipfit.exception.IssueWithApprovalException("Authentication Failed");
        }

        if (centreId <= 0) {
            throw new com.flipfit.exception.IssueWithApprovalException("ERROR: Invalid centre ID");
        }

        GymCentreDAOImpl centreDAO = new GymCentreDAOImpl();
        centreDAO.updateGymCentreApproval(centreId, true);

        // Refresh the repository from database after update
        viewAllCentres(email, password);

        return FlipFitRepository.gymCentres.stream()
                .filter(c -> c.getCentreId() == centreId)
                .findFirst()
                .map(centre -> {
                    // It should already be approved from viewAllCentres() if DB update was
                    // successful
                    System.out.println("✓ Centre " + centreId + " approved successfully");
                    return true;
                }).orElseThrow(() -> new com.flipfit.exception.IssueWithApprovalException(
                        "ERROR: Centre with ID " + centreId + " not found"));
    }

    @Override
    public boolean deleteOwner(int ownerId, String email, String password)
            throws com.flipfit.exception.UserNotFoundException {
        if (!userService.validateUser(email, password)) {
            throw new com.flipfit.exception.UserNotFoundException("Authentication Failed");
        }

        boolean dbSuccess = adminDAO.deleteOwner(ownerId);
        if (dbSuccess) {
            FlipFitRepository.owners.removeIf(owner -> owner.getUserId() == ownerId);
            System.out.println("✓ Owner deleted");
            return true;
        }
        throw new com.flipfit.exception.UserNotFoundException("Failed to delete owner");
    }

    @Override
    public void viewCustomerMetrics(String email, String password) {
        if (!userService.validateUser(email, password))
            return;
        List<com.flipfit.bean.GymCustomer> allCustomers = adminDAO.getAllCustomers();
        System.out.println("Total Customers: " + allCustomers.size());
    }

    @Override
    public void viewGymMetrics(String email, String password) {
        if (!userService.validateUser(email, password))
            return;
        // Logic simplication
        System.out.println("Gym Metrics displayed");
    }
}
