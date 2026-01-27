package com.flipfit.dao;

import com.flipfit.bean.GymOwner;

public interface GymOwnerDAO {
    void registerOwner(String fullName, String email, String password, Long phoneNumber, 
                      String city, String state, int pincode, String panCard, 
                      String aadhaarNumber, String gstin);
    
    boolean isOwnerValid(String email, String password);
    
    GymOwner getOwnerById(int userId);
}
