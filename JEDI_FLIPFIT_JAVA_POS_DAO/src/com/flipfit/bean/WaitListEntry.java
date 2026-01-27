/**
 * 
 */
package com.flipfit.bean;
import java.sql.Timestamp;

/**
 * 
 */
public class WaitListEntry {
	private int waitlistid;
	private int bookingId;
	private int customerId;
	private int availabilityId;
	private int position;
	private String status;  // "PENDING", "CONFIRMED", "CANCELLED"
	private Timestamp createdAt;
	
	public WaitListEntry() {}
	
	public WaitListEntry(int bookingId, int customerId, int availabilityId, int position) {
		this.bookingId = bookingId;
		this.customerId = customerId;
		this.availabilityId = availabilityId;
		this.position = position;
		this.status = "PENDING";
	}
	
	public int getWaitlistid() {
		return waitlistid;
	}
	public void setWaitlistid(int waitlistid) {
		this.waitlistid = waitlistid;
	}
	
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	public int getAvailabilityId() {
		return availabilityId;
	}
	public void setAvailabilityId(int availabilityId) {
		this.availabilityId = availabilityId;
	}
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
}
