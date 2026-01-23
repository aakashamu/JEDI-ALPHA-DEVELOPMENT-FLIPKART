/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public class UserService implements UserInterface {

    @Override
    public boolean login() {
        // TODO Auto-generated method stub
        System.out.println("User logged in");
        return true;
    }

    @Override
    public void logout() {
        // TODO Auto-generated method stub
        System.out.println("User logged out");
    }

}
