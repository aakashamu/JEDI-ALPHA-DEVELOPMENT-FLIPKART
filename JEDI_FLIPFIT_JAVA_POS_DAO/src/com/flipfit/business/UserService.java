package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.UserDAO;
import java.util.HashMap;
import java.util.Map;

public class UserService implements UserInterface {
    
    private static String currentLoggedInUserEmail = null;
    private static final Map<String, User> loggedInUsers = new HashMap<>();
    
    @Override
    public boolean login(String email, String password) {
        // Validate email
        if (email == null || email.isEmpty()) {
            System.out.println("ERROR: Email cannot be empty");
            return false;
        }
        
        // Validate password
        if (password == null || password.isEmpty()) {
            System.out.println("ERROR: Password cannot be empty");
            return false;
        }
        
        // Check if user is already logged in
        if (loggedInUsers.containsKey(email)) {
            System.out.println("User " + email + " is already logged in");
            return true;
        }
        
        // Validate user credentials using database
        UserDAO userDAO = new UserDAO();
        if (!userDAO.login(email, password)) {
            System.out.println("ERROR: Invalid email or password for user: " + email);
            return false;
        }
        
        // Get user details from database
        User user = userDAO.getUserDetails(email);
        if (user != null) {
            // Load role-specific user object based on roleId
            User roleSpecificUser = loadRoleSpecificUser(user);
            
            // Add to in-memory repository for current session
            FlipFitRepository.users.put(email, roleSpecificUser);
            
            // Add to logged in users
            loggedInUsers.put(email, roleSpecificUser);
            currentLoggedInUserEmail = email;
            System.out.println("✓ User " + email + " logged in successfully");
            return true;
        }
        
        System.out.println("ERROR: User not found: " + email);
        return false;
    }
    
    /**
     * Load role-specific user object (GymOwner, GymCustomer, GymAdmin) from database
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
    
    public static User getCurrentUser(String email) {
        return loggedInUsers.get(email);
    }
    
    public static boolean isUserLoggedIn(String email) {
        return loggedInUsers.containsKey(email);
    }
    
    public static String getCurrentLoggedInUser() {
        return currentLoggedInUserEmail;
    }
    
    public static void setCurrentLoggedInUser(String email) {
        currentLoggedInUserEmail = email;
    }
}
