/**
 * User class representing a user in the FlipFit system
 */
package com.flipfit.bean;

/**
 * User bean class with user details and authentication methods
 */
public class User {
    // Attributes as per class diagram
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private long phoneNumber;
    private String city;
    private int pincode;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(int userId, String fullName, String email, String password,
            long phoneNumber, String city, int pincode) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.pincode = pincode;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    // Methods as per class diagram
    /**
     * Login method to authenticate user
     * 
     * @return boolean indicating login success
     */
    public boolean login() {
        return false;
    }

    /**
     * Logout method to end user session
     */
    public void logout() {
    }
}
