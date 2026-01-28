package com.flipfit.bean;
/**
 * The Class GymOwner.
 *
 * @author Ananya
 * @ClassName  "GymOwner"
 */
public class GymOwner extends User {
    private String PAN;
    private String Aadhaar;
    private String GSTIN;
    private int isApproved; // 0 for pending, 1 for approved
  /**
   * Gym Owner.
   *
   * @return the public
   */
    public GymOwner() {
        super();
    }

    // Parametrized Constructor (Matches your UML and terminal run requirements)
    public GymOwner(int userId, String fullName, String email, String password,
                    long phoneNumber, String city, int pincode,
                    String PAN, String Aadhaar, String GSTIN) {
        super(userId, fullName, email, password, phoneNumber, city, pincode);
        this.PAN = PAN;
        this.Aadhaar = Aadhaar;
        this.GSTIN = GSTIN;
        this.isApproved = 0;
    }
  /**
   * Get Pan.
   *
   * @return the String
   */
    public String getPAN() { return PAN; }
  /**
   * Set Pan.
   *
   * @param PAN the PAN
   */
    public void setPAN(String PAN) { this.PAN = PAN; }
  /**
   * Get Aadhaar.
   *
   * @return the String
   */
    public String getAadhaar() { return Aadhaar; }
  /**
   * Set Aadhaar.
   *
   * @param Aadhaar the Aadhaar
   */
    public void setAadhaar(String Aadhaar) { this.Aadhaar = Aadhaar; }
  /**
   * Get Gstin.
   *
   * @return the String
   */
    public String getGSTIN() { return GSTIN; }
  /**
   * Set Gstin.
   *
   * @param GSTIN the GSTIN
   */
    public void setGSTIN(String GSTIN) { this.GSTIN = GSTIN; }
  /**
   * Get Is Approved.
   *
   * @return the int
   */
    public int getIsApproved() { return isApproved; }
  /**
   * Set Is Approved.
   *
   * @param isApproved the isApproved
   */
    public void setIsApproved(int isApproved) { this.isApproved = isApproved; }
}