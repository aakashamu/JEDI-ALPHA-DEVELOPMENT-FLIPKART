/**
 * 
 */
package com.flipfit.bean;

/**
 * 
 */
public class GymCentre {
	
	private int centreId;
    private String name;
    private String city;
    private String state;
    private int pincode;
    private boolean approved;
    private int ownerId;  // Track which owner registered this centre

    // Getters and Setters
    public int getCentreId() { return centreId; }
    public void setCentreId(int centreId) { this.centreId = centreId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public int getPincode() { return pincode; }
    public void setPincode(int pincode) { this.pincode = pincode; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
}