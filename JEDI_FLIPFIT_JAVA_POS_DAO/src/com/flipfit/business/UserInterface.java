/**
 * 
 */
package com.flipfit.business;
/**
 * The Interface UserInterface.
 *
 * @author Ananya
 * @ClassName  "UserInterface"
 */
public interface UserInterface {

    public boolean login(String email, String password) throws com.flipfit.exception.UserNotFoundException;

    public void logout();

    public boolean updatePassword(String email, String oldPassword, String newPassword);
}
