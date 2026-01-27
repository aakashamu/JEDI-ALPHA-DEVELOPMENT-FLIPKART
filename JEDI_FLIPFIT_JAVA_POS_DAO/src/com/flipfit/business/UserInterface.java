/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public interface UserInterface {

    public boolean login(String email, String password);
    public void logout();
    public boolean updatePassword(String email, String oldPassword, String newPassword);
}
