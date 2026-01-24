/**
 * 
 */
package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.dao.FlipFitRepository;
import java.util.HashMap;
import java.util.Map;

public class UserService implements UserInterface {
    
    private static final Map<String, User> loggedInUsers = new HashMap<>();
    
    @Override
    public boolean login(String email, String password) {
        // Check if user is already logged in
        if (loggedInUsers.containsKey(email)) {
            System.out.println("User " + email + " is already logged in");
            return true;
        }
        
        // Validate user credentials using repository
        if (!FlipFitRepository.validateUser(email, password)) {
            System.out.println("Invalid email or password for user: " + email);
            return false;
        }
        
        // Get user from repository
        User user = FlipFitRepository.getUserByEmail(email);
        if (user != null) {
            loggedInUsers.put(email, user);
            System.out.println("User " + email + " logged in successfully");
            return true;
        }
        
        System.out.println("User not found: " + email);
        return false;
    }

    @Override
    public void logout() {
        if (loggedInUsers.isEmpty()) {
            System.out.println("No users are currently logged in");
            return;
        }
        
        // For simplicity, logout the first logged in user
        // In a real implementation, you'd track the current user session
        String userEmail = loggedInUsers.keySet().iterator().next();
        loggedInUsers.remove(userEmail);
        System.out.println("User " + userEmail + " logged out successfully");
    }
    
    public static User getCurrentUser(String email) {
        return loggedInUsers.get(email);
    }
    
    public static boolean isUserLoggedIn(String email) {
        return loggedInUsers.containsKey(email);
    }
}
