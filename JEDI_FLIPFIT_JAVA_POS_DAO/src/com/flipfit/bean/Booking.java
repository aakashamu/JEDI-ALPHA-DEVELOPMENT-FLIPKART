/**
 * */
package com.flipfit.bean;

import java.util.Date;
/**
 * The Class Booking.
 *
 * @author Ananya
 * @ClassName  "Booking"
 */
public class Booking {
     
    private int bookingId;
    private Date bookingDate;
    private String status;
    private String createdAt; 
    private int customerId;       
    private int availabilityId;   
  /**
   * Booking.
   *
   * @return the public
   */
    public Booking() {
    }
  /**
   * Booking.
   *
   * @param bookingId the bookingId
   * @param bookingDate the bookingDate
   * @param status the status
   * @param createdAt the createdAt
   * @param customerId the customerId
   * @param availabilityId the availabilityId
   * @return the public
   */
    public Booking(int bookingId, Date bookingDate, String status, String createdAt, int customerId, int availabilityId) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.availabilityId = availabilityId;
    }
  /**
   * Get Booking Id.
   *
   * @return the int
   */
    public int getBookingId() { return bookingId; }
  /**
   * Set Booking Id.
   *
   * @param bookingId the bookingId
   */
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
  /**
   * Get Booking Date.
   *
   * @return the Date
   */
    public Date getBookingDate() { return bookingDate; }
  /**
   * Set Booking Date.
   *
   * @param bookingDate the bookingDate
   */
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
  /**
   * Get Status.
   *
   * @return the String
   */
    public String getStatus() { return status; }
  /**
   * Set Status.
   *
   * @param status the status
   */
    public void setStatus(String status) { this.status = status; }
  /**
   * Get Created At.
   *
   * @return the String
   */
    public String getCreatedAt() { return createdAt; }
  /**
   * Set Created At.
   *
   * @param createdAt the createdAt
   */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
  /**
   * Get Customer Id.
   *
   * @return the int
   */
    public int getCustomerId() { return customerId; }
  /**
   * Set Customer Id.
   *
   * @param customerId the customerId
   */
    public void setCustomerId(int customerId) { this.customerId = customerId; }
  /**
   * Get Availability Id.
   *
   * @return the int
   */
    public int getAvailabilityId() { return availabilityId; }
  /**
   * Set Availability Id.
   *
   * @param availabilityId the availabilityId
   */
    public void setAvailabilityId(int availabilityId) { this.availabilityId = availabilityId; }
}