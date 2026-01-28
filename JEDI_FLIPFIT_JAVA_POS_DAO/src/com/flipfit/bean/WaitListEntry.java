/**
 * 
 */
package com.flipfit.bean;
import java.sql.Timestamp;
/**
 * The Class WaitListEntry.
 *
 * @author Ananya
 * @ClassName  "WaitListEntry"
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
  /**
   * Get Waitlistid.
   *
   * @return the int
   */
	public int getWaitlistid() {
		return waitlistid;
	}
  /**
   * Set Waitlistid.
   *
   * @param waitlistid the waitlistid
   */
	public void setWaitlistid(int waitlistid) {
		this.waitlistid = waitlistid;
	}
  /**
   * Get Booking Id.
   *
   * @return the int
   */
	public int getBookingId() {
		return bookingId;
	}
  /**
   * Set Booking Id.
   *
   * @param bookingId the bookingId
   */
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
  /**
   * Get Customer Id.
   *
   * @return the int
   */
	public int getCustomerId() {
		return customerId;
	}
  /**
   * Set Customer Id.
   *
   * @param customerId the customerId
   */
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
  /**
   * Get Availability Id.
   *
   * @return the int
   */
	public int getAvailabilityId() {
		return availabilityId;
	}
  /**
   * Set Availability Id.
   *
   * @param availabilityId the availabilityId
   */
	public void setAvailabilityId(int availabilityId) {
		this.availabilityId = availabilityId;
	}
  /**
   * Get Position.
   *
   * @return the int
   */
	public int getPosition() {
		return position;
	}
  /**
   * Set Position.
   *
   * @param position the position
   */
	public void setPosition(int position) {
		this.position = position;
	}
  /**
   * Get Status.
   *
   * @return the String
   */
	public String getStatus() {
		return status;
	}
  /**
   * Set Status.
   *
   * @param status the status
   */
	public void setStatus(String status) {
		this.status = status;
	}
  /**
   * Get Created At.
   *
   * @return the Timestamp
   */
	public Timestamp getCreatedAt() {
		return createdAt;
	}
  /**
   * Set Created At.
   *
   * @param createdAt the createdAt
   */
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
}
