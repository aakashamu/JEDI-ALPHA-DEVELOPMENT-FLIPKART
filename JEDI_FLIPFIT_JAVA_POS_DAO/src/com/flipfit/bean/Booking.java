/**
 * */
package com.flipfit.bean;

import java.util.Date;

/**
 * Represents a gym slot booking entity as defined in the UML diagram.
 */
public class Booking {
     
    private int bookingId;
    private Date bookingDate;
    private String status;
    private String createdAt; 
    private int customerId;       
    private int availabilityId;   

    /**
     * Default Constructor
     */
    public Booking() {
    }

    /**
     * Parameterized Constructor
     */
    public Booking(int bookingId, Date bookingDate, String status, String createdAt, int customerId, int availabilityId) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.availabilityId = availabilityId;
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getAvailabilityId() { return availabilityId; }
    public void setAvailabilityId(int availabilityId) { this.availabilityId = availabilityId; }
}