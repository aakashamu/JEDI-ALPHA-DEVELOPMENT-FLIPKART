package com.flipfit.bean;

/**
 * Represents a Gym Owner entity, extending the base User class.
 * Based on UML specifications for PAN, Aadhaar, and GSTIN.
 */
public class GymOwner extends User {
    private String PAN;
    private String Aadhaar;
    private String GSTIN;
    private int isApproved; // 0 for pending, 1 for approved

    public GymOwner(int userId, String fullName, String email, String password,
                    long phoneNumber, String city, int pincode,
                    String PAN, String Aadhaar, String GSTIN) {
        super(userId, fullName, email, password, phoneNumber, city, pincode); // Now types match
        this.PAN = PAN;
        this.Aadhaar = Aadhaar;
        this.GSTIN = GSTIN;
        this.isApproved = 0;
    }

    // Getters and Setters
    public String getPAN() { return PAN; }
    public void setPAN(String PAN) { this.PAN = PAN; }

    public String getAadhaar() { return Aadhaar; }
    public void setAadhaar(String Aadhaar) { this.Aadhaar = Aadhaar; }

    public String getGSTIN() { return GSTIN; }
    public void setGSTIN(String GSTIN) { this.GSTIN = GSTIN; }

    public int getIsApproved() { return isApproved; }
    public void setIsApproved(int isApproved) { this.isApproved = isApproved; }
}