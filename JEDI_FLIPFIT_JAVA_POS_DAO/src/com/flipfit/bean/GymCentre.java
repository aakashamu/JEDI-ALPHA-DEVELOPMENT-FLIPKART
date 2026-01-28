/**
 * 
 */
package com.flipfit.bean;
/**
 * The Class GymCentre.
 *
 * @author Ananya
 * @ClassName  "GymCentre"
 */
public class GymCentre {
	
	private int centreId;
    private String name;
    private String city;
    private String state;
    private int pincode;
    private boolean approved;
    private int ownerId;  // Track which owner registered this centre
  /**
   * Get Centre Id.
   *
   * @return the int
   */
    public int getCentreId() { return centreId; }
  /**
   * Set Centre Id.
   *
   * @param centreId the centreId
   */
    public void setCentreId(int centreId) { this.centreId = centreId; }
  /**
   * Get Name.
   *
   * @return the String
   */
    public String getName() { return name; }
  /**
   * Set Name.
   *
   * @param name the name
   */
    public void setName(String name) { this.name = name; }
  /**
   * Get City.
   *
   * @return the String
   */
    public String getCity() { return city; }
  /**
   * Set City.
   *
   * @param city the city
   */
    public void setCity(String city) { this.city = city; }
  /**
   * Get State.
   *
   * @return the String
   */
    public String getState() { return state; }
  /**
   * Set State.
   *
   * @param state the state
   */
    public void setState(String state) { this.state = state; }
  /**
   * Get Pincode.
   *
   * @return the int
   */
    public int getPincode() { return pincode; }
  /**
   * Set Pincode.
   *
   * @param pincode the pincode
   */
    public void setPincode(int pincode) { this.pincode = pincode; }
  /**
   * Is Approved.
   *
   * @return the boolean
   */
    public boolean isApproved() { return approved; }
  /**
   * Set Approved.
   *
   * @param approved the approved
   */
    public void setApproved(boolean approved) { this.approved = approved; }
  /**
   * Get Owner Id.
   *
   * @return the int
   */
    public int getOwnerId() { return ownerId; }
  /**
   * Set Owner Id.
   *
   * @param ownerId the ownerId
   */
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
}