/**
 * 
 */
package com.flipfit.bean;

/**
 * 
 */
public class GymOwner extends User{
	public GymOwner()
	{
		super();
	}
	
	private String pan;
	private String aadhaar;
	private String gstin;
	
	public String getAadhaar() {
		return aadhaar;
	}
	public void setAadhaar(String aadhaar) {
		this.aadhaar = aadhaar;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getGstin() {
		return gstin;
	}
	public void setGstin(String gstin) {
		this.gstin = gstin;
	}
	

}
