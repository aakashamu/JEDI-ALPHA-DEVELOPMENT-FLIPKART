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
     * Default constructor.
     */
    public GymOwner() {
        super();
    }

    /**
     * Constructor with all owner fields.
     *
     * @param userId     the user id
     * @param fullName   the full name
     * @param email      the email
     * @param password   the password
     * @param phoneNumber the phone number
     * @param city       the city
     * @param pincode    the pincode
     * @param PAN        the PAN
     * @param Aadhaar    the Aadhaar
     * @param GSTIN      the GSTIN
     */
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
     * Gets the PAN.
     *
     * @return the PAN
     */
    public String getPAN() { return PAN; }

    /**
     * Sets the PAN.
     *
     * @param PAN the PAN
     */
    public void setPAN(String PAN) { this.PAN = PAN; }

    /**
     * Gets the Aadhaar.
     *
     * @return the Aadhaar
     */
    public String getAadhaar() { return Aadhaar; }

    /**
     * Sets the Aadhaar.
     *
     * @param Aadhaar the Aadhaar
     */
    public void setAadhaar(String Aadhaar) { this.Aadhaar = Aadhaar; }

    /**
     * Gets the GSTIN.
     *
     * @return the GSTIN
     */
    public String getGSTIN() { return GSTIN; }

    /**
     * Sets the GSTIN.
     *
     * @param GSTIN the GSTIN
     */
    public void setGSTIN(String GSTIN) { this.GSTIN = GSTIN; }

    /**
     * Gets the approval status (0 pending, 1 approved).
     *
     * @return the approval status
     */
    public int getIsApproved() { return isApproved; }

    /**
     * Sets the approval status.
     *
     * @param isApproved the approval status (0 pending, 1 approved)
     */
    public void setIsApproved(int isApproved) { this.isApproved = isApproved; }
}