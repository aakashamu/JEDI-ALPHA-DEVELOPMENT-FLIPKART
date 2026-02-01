package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.UserDAO;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class UserService.
 *
 * @author Ananya
 * @ClassName "UserService"
 */
public class UserService implements UserInterface {

    private static String currentLoggedInUserEmail = null;
    private static final Map<String, User> loggedInUsers = new HashMap<>();

    /**
     * Login.
     *
     * @param email    the email
     * @param password the password
     * @return true if successful, false otherwise
     * @throws com.flipfit.exception.UserNotFoundException
     */
    @Override
    public boolean login(String email, String password) throws com.flipfit.exception.UserNotFoundException {
        // Validate email
        if (email == null || email.isEmpty()) {
            throw new com.flipfit.exception.UserNotFoundException("ERROR: Email cannot be empty");
        }

        // Validate password
        if (password == null || password.isEmpty()) {
            throw new com.flipfit.exception.UserNotFoundException("ERROR: Password cannot be empty");
        }

        // Validate user credentials using database
        UserDAO userDAO = new UserDAO();
        if (!userDAO.login(email, password)) {
            throw new com.flipfit.exception.UserNotFoundException(
                    "ERROR: Invalid email or password for user: " + email);
        }

        // Always reload user from database to ensure proper role-specific object
        User user = userDAO.getUserDetails(email);
        if (user != null) {
            // Load role-specific user object based on roleId
            User roleSpecificUser = loadRoleSpecificUser(user);

            // Update in-memory repository for current session (remove old, add new)
            FlipFitRepository.users.put(email, roleSpecificUser);
            loggedInUsers.put(email, roleSpecificUser);
            currentLoggedInUserEmail = email;
            System.out.println("✓ User " + email + " logged in successfully");
            return true;
        }

        throw new com.flipfit.exception.UserNotFoundException("ERROR: User not found: " + email);
    }

    /**
     * Validate User Credentials (Stateless).
     * 
     * @param email
     * @param password
     * @return true if valid
     */
    public boolean validateUser(String email, String password) {
        if (email == null || password == null)
            return false;
        return new UserDAO().login(email, password);
    }

    /**
     * Get user details (stateless fetch).
     * 
     * @param email
     * @return User
     */
    public User getUser(String email) {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserDetails(email);
        if (user != null) {
            return loadRoleSpecificUser(user);
        }
        return null; // or throw exception
    }

    /**
     * Load Role Specific User.
     *
     * @param baseUser the baseUser
     * @return the User
     */
    private User loadRoleSpecificUser(User baseUser) {
        int roleId = baseUser.getRoleId();

        // Role 1 = Admin, Role 2 = Customer, Role 3 = Owner
        if (roleId == 3) {
            // Load GymOwner details
            com.flipfit.dao.GymOwnerDAOImpl ownerDAO = new com.flipfit.dao.GymOwnerDAOImpl();
            com.flipfit.bean.GymOwner owner = ownerDAO.getOwnerById(baseUser.getUserId());
            if (owner != null && owner.getUserId() > 0) {
                // Ensure all base fields are preserved
                owner.setRoleId(roleId);
                owner.setPassword(baseUser.getPassword());
                owner.setCity(baseUser.getCity());
                owner.setState(baseUser.getState());
                owner.setPincode(baseUser.getPincode());
                owner.setPhoneNumber(baseUser.getPhoneNumber());

                FlipFitRepository.owners.add(owner);
                return owner;
            }
        } else if (roleId == 2) {
            // Load GymCustomer details
            com.flipfit.dao.GymCustomerDAO customerDAO = new com.flipfit.dao.GymCustomerDAO();
            com.flipfit.bean.User customerUser = customerDAO.getCustomerById(baseUser.getUserId());
            if (customerUser != null && customerUser.getUserId() > 0) {
                com.flipfit.bean.GymCustomer customer = new com.flipfit.bean.GymCustomer();
                customer.setUserId(customerUser.getUserId());
                customer.setFullName(customerUser.getFullName());
                customer.setEmail(customerUser.getEmail());

                // Copy base fields
                customer.setRoleId(roleId);
                customer.setPassword(baseUser.getPassword());
                customer.setPhoneNumber(baseUser.getPhoneNumber());
                customer.setCity(baseUser.getCity());
                customer.setState(baseUser.getState());
                customer.setPincode(baseUser.getPincode());

                FlipFitRepository.customers.put(customer.getEmail(), customer);
                return customer;
            }
        }

        // For admin or if role-specific loading fails, return base user
        return baseUser;
    }

    /**
     * Logout.
     *
     */
    @Override
    public void logout() {
        if (currentLoggedInUserEmail == null || currentLoggedInUserEmail.isEmpty()) {
            System.out.println("ERROR: No user is currently logged in");
            return;
        }

        String userEmail = currentLoggedInUserEmail;
        loggedInUsers.remove(userEmail);
        currentLoggedInUserEmail = null;
        System.out.println("✓ User " + userEmail + " logged out successfully");
    }

    /**
     * Get Current User.
     *
     * @param email the email
     * @return the User
     */
    public static User getCurrentUser(String email) {
        return loggedInUsers.get(email);
    }

    /**
     * Is User Logged In.
     *
     * @param email the email
     * @return true if successful, false otherwise
     */
    public static boolean isUserLoggedIn(String email) {
        return loggedInUsers.containsKey(email);
    }

    /**
 * Gets the current logged-in user for the given email.
   *
   * @return the user object, or null
     */
    public static String getCurrentLoggedInUser() {
        return currentLoggedInUserEmail;
    }

    /**
     * Set Current Logged In User.
     *
     * @param email the email
     */
    public static void setCurrentLoggedInUser(String email) {
        currentLoggedInUserEmail = email;
    }

    /**
     * Update Password.
     *
     * @param email       the email
     * @param oldPassword the oldPassword
     * @param newPassword the newPassword
     * @return true if successful, false otherwise
     */
    @Override
    public boolean updatePassword(String email, String oldPassword, String newPassword) {
        UserDAO userDAO = new UserDAO();
        return userDAO.changePassword(email, oldPassword, newPassword);
    }
}
