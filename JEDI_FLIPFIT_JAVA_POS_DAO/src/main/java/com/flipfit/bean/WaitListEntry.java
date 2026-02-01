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
	
	/**
	 * Default constructor.
	 */
	public WaitListEntry() {}

	/**
	 * Constructor with booking, customer, availability and position.
	 *
	 * @param bookingId     the booking id
	 * @param customerId   the customer id
	 * @param availabilityId the availability id
	 * @param position     the position in waitlist
	 */
	public WaitListEntry(int bookingId, int customerId, int availabilityId, int position) {
		this.bookingId = bookingId;
		this.customerId = customerId;
		this.availabilityId = availabilityId;
		this.position = position;
		this.status = "PENDING";
	}
	/**
	 * Gets the waitlist id.
	 *
	 * @return the waitlist id
	 */
	public int getWaitlistid() {
		return waitlistid;
	}
	/**
	 * Sets the waitlist id.
	 *
	 * @param waitlistid the waitlist id
	 */
	public void setWaitlistid(int waitlistid) {
		this.waitlistid = waitlistid;
	}
	/**
	 * Gets the booking id.
	 *
	 * @return the booking id
	 */
	public int getBookingId() {
		return bookingId;
	}
	/**
	 * Sets the booking id.
	 *
	 * @param bookingId the booking id
	 */
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	public int getCustomerId() {
		return customerId;
	}
	/**
	 * Sets the customer id.
	 *
	 * @param customerId the customer id
	 */
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	/**
	 * Gets the availability id.
	 *
	 * @return the availability id
	 */
	public int getAvailabilityId() {
		return availabilityId;
	}
	/**
	 * Sets the availability id.
	 *
	 * @param availabilityId the availability id
	 */
	public void setAvailabilityId(int availabilityId) {
		this.availabilityId = availabilityId;
	}
	/**
	 * Gets the position in waitlist.
	 *
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * Sets the position.
	 *
	 * @param position the position in waitlist
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	/**
	 * Gets the status (PENDING, CONFIRMED, CANCELLED).
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * Sets the status.
	 *
	 * @param status the status (PENDING, CONFIRMED, CANCELLED)
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * Gets the created at timestamp.
	 *
	 * @return the created at timestamp
	 */
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	/**
	 * Sets the created at timestamp.
	 *
	 * @param createdAt the created at timestamp
	 */
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
}
