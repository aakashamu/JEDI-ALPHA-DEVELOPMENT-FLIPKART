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
   * User.
   *
   * @return the public
   */
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
  /**
   * Get User Id.
   *
   * @return the int
   */
    public int getUserId() {
        return userId;
    }
  /**
   * Set User Id.
   *
   * @param userId the userId
   */
    public void setUserId(int userId) {
        this.userId = userId;
    }
  /**
   * Get Full Name.
   *
   * @return the String
   */
    public String getFullName() {
        return fullName;
    }
  /**
   * Set Full Name.
   *
   * @param fullName the fullName
   */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
  /**
   * Get Email.
   *
   * @return the String
   */
    public String getEmail() {
        return email;
    }
  /**
   * Set Email.
   *
   * @param email the email
   */
    public void setEmail(String email) {
        this.email = email;
    }
  /**
   * Get Password.
   *
   * @return the String
   */
    public String getPassword() {
        return password;
    }
  /**
   * Set Password.
   *
   * @param password the password
   */
    public void setPassword(String password) {
        this.password = password;
    }
  /**
   * Get Phone Number.
   *
   * @return the long
   */
    public long getPhoneNumber() {
        return phoneNumber;
    }
  /**
   * Set Phone Number.
   *
   * @param phoneNumber the phoneNumber
   */
    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
  /**
   * Get City.
   *
   * @return the String
   */
    public String getCity() {
        return city;
    }
  /**
   * Set City.
   *
   * @param city the city
   */
    public void setCity(String city) {
        this.city = city;
    }
  /**
   * Get State.
   *
   * @return the String
   */
    public String getState() {
        return state;
    }
  /**
   * Set State.
   *
   * @param state the state
   */
    public void setState(String state) {
        this.state = state;
    }
  /**
   * Get Pincode.
   *
   * @return the int
   */
    public int getPincode() {
        return pincode;
    }
  /**
   * Set Pincode.
   *
   * @param pincode the pincode
   */
    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
  /**
   * Get Role Id.
   *
   * @return the int
   */
    public int getRoleId() {
        return roleId;
    }
  /**
   * Set Role Id.
   *
   * @param roleId the roleId
   */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
  /**
   * Login.
   *
   * @return the boolean
   */
    public boolean login() {
        return false;
    }
  /**
   * Logout.
   *
   */
    public void logout() {
    }
}
