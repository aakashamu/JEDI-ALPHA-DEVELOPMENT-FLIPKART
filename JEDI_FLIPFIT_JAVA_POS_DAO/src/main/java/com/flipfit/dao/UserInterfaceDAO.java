package com.flipfit.dao;

import com.flipfit.bean.User;

public interface UserInterfaceDAO {
    public boolean login(String email, String password);
    public User getUserDetails(String email);
    public void updateProfile(User user);
    public boolean changePassword(String email, String oldPassword, String newPassword);
}