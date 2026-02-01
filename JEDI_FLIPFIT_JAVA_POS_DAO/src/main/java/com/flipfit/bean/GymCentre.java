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
     * Gets the centre id.
     *
     * @return the centre id
     */
    public int getCentreId() { return centreId; }

    /**
     * Sets the centre id.
     *
     * @param centreId the centre id
     */
    public void setCentreId(int centreId) { this.centreId = centreId; }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() { return city; }

    /**
     * Sets the city.
     *
     * @param city the city
     */
    public void setCity(String city) { this.city = city; }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() { return state; }

    /**
     * Sets the state.
     *
     * @param state the state
     */
    public void setState(String state) { this.state = state; }

    /**
     * Gets the pincode.
     *
     * @return the pincode
     */
    public int getPincode() { return pincode; }

    /**
     * Sets the pincode.
     *
     * @param pincode the pincode
     */
    public void setPincode(int pincode) { this.pincode = pincode; }

    /**
     * Checks if the centre is approved.
     *
     * @return true if approved
     */
    public boolean isApproved() { return approved; }

    /**
     * Sets the approved flag.
     *
     * @param approved the approved flag
     */
    public void setApproved(boolean approved) { this.approved = approved; }

    /**
     * Gets the owner id.
     *
     * @return the owner id
     */
    public int getOwnerId() { return ownerId; }

    /**
     * Sets the owner id.
     *
     * @param ownerId the owner id
     */
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
}