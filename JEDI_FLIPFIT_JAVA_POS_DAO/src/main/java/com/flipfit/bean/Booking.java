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
     * Default constructor.
     */
    public Booking() {
    }

    /**
     * Constructor with all booking fields.
     *
     * @param bookingId     the booking id
     * @param bookingDate   the booking date
     * @param status        the status
     * @param createdAt     the created at timestamp
     * @param customerId    the customer id
     * @param availabilityId the availability id
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
     * Gets the booking id.
     *
     * @return the booking id
     */
    public int getBookingId() { return bookingId; }

    /**
     * Sets the booking id.
     *
     * @param bookingId the booking id
     */
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    /**
     * Gets the booking date.
     *
     * @return the booking date
     */
    public Date getBookingDate() { return bookingDate; }

    /**
     * Sets the booking date.
     *
     * @param bookingDate the booking date
     */
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() { return status; }

    /**
     * Sets the status.
     *
     * @param status the status
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Gets the created at timestamp.
     *
     * @return the created at
     */
    public String getCreatedAt() { return createdAt; }

    /**
     * Sets the created at timestamp.
     *
     * @param createdAt the created at
     */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    /**
     * Gets the customer id.
     *
     * @return the customer id
     */
    public int getCustomerId() { return customerId; }

    /**
     * Sets the customer id.
     *
     * @param customerId the customer id
     */
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    /**
     * Gets the availability id.
     *
     * @return the availability id
     */
    public int getAvailabilityId() { return availabilityId; }

    /**
     * Sets the availability id.
     *
     * @param availabilityId the availability id
     */
    public void setAvailabilityId(int availabilityId) { this.availabilityId = availabilityId; }
}