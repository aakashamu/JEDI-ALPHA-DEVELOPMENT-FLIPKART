package com.flipfit.dao;

public interface GymAdminDAO {
    void registerAdmin(String fullName, String email, String password, Long phoneNumber, 
                      String city, String state, int pincode);
    
    boolean isAdminValid(String email, String password);
    
    java.util.List<com.flipfit.bean.GymOwner> getAllOwners();
    
    boolean approveOwner(int ownerId);
    
    boolean deleteOwner(int ownerId);
}
