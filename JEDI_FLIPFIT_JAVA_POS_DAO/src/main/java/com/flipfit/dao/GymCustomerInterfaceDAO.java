package com.flipfit.dao;

import com.flipfit.bean.User;
/**
 * The Interface GymCustomerInterfaceDAO.
 *
 * @author Ananya
 * @ClassName  "GymCustomerInterfaceDAO"
 */
public interface GymCustomerInterfaceDAO {
    // We use boolean or void since we aren't using custom exceptions yet
    public void registerCustomer(String fullName, String email, String password, Long phoneNumber, String city, String state, int pincode);

    public boolean isUserValid(String email, String password);

    public User getCustomerById(int userId);
}