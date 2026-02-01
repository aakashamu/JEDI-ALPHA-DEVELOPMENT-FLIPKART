/**
 * User class representing a user in the FlipFit system
 */
package com.flipfit.bean;
/**
 * The Class User.
 *
 * @author Ananya
 * @ClassName  "User"
 */
public class User {
    // Attributes as per class diagram
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private long phoneNumber;
    private String city;
    private String state;
    private int pincode;
    private int roleId;
    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructor with all user fields.
     *
     * @param userId     the user id
     * @param fullName   the full name
     * @param email      the email
     * @param password   the password
     * @param phoneNumber the phone number
     * @param city       the city
     * @param pincode    the pincode
     */
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
    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Sets the user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * Sets the full name.
     *
     * @param fullName the full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public long getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Sets the phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }
    /**
     * Sets the city.
     *
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }
    /**
     * Sets the state.
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }
    /**
     * Gets the pincode.
     *
     * @return the pincode
     */
    public int getPincode() {
        return pincode;
    }
    /**
     * Sets the pincode.
     *
     * @param pincode the pincode
     */
    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
    /**
     * Gets the role id.
     *
     * @return the role id
     */
    public int getRoleId() {
        return roleId;
    }
    /**
     * Sets the role id.
     *
     * @param roleId the role id
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    /**
     * Performs login.
     *
     * @return true if login succeeds
     */
    public boolean login() {
        return false;
    }
    /**
     * Performs logout.
     */
    public void logout() {
    }
}
