package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.dao.FlipFitRepository;
import com.flipfit.dao.UserDAO;
import java.util.HashMap;
import java.util.Map;

public class UserService implements UserInterface {
    
    private static String currentLoggedInUserEmail = null;
    private static final Map<String, User> loggedInUsers = new HashMap<>();
    private static UserDAO userDAO = new UserDAO();
    
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
        
        // Validate user credentials using UserDAO (from database)
        if (!userDAO.login(email, password)) {
            System.out.println("ERROR: Invalid email or password for user: " + email);
            return false;
        }
        
        // Get user from database
        User user = userDAO.getUserDetails(email);
        if (user != null) {
            loggedInUsers.put(email, user);
            currentLoggedInUserEmail = email;
            System.out.println("✓ User " + email + " logged in successfully");
            return true;
        }
        
        System.out.println("ERROR: User not found: " + email);
        return false;
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
