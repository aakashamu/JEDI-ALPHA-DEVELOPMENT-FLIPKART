package com.flipfit.bean;

import java.time.LocalTime;
/**
 * The Class Slot.
 *
 * @author Ananya
 * @ClassName  "Slot"
 */
public class Slot {

    private int slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;
    private int centreId;
    /**
     * Default constructor.
     */
    public Slot() {}

    /**
     * Constructor with all slot fields.
     *
     * @param slotId    the slot id
     * @param startTime the start time
     * @param endTime   the end time
     * @param capacity  the capacity
     * @param centreId  the centre id
     */
    public Slot(int slotId, LocalTime startTime, LocalTime endTime, int capacity,int centreId) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.centreId=centreId;
    }
    /**
     * Gets the centre id.
     *
     * @return the centre id
     */
    public int getCentreId() { 
        return centreId; 
    }
    /**
     * Sets the centre id.
     *
     * @param centreId the centre id
     */
    public void setCentreId(int centreId) {this.centreId=centreId;}

    /**
     * Gets the slot id.
     *
     * @return the slot id
     */
    public int getSlotId() { return slotId; }

    /**
     * Sets the slot id.
     *
     * @param slotId the slot id
     */
    public void setSlotId(int slotId) { this.slotId = slotId; }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public LocalTime getStartTime() { return startTime; }

    /**
     * Sets the start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public LocalTime getEndTime() { return endTime; }

    /**
     * Sets the end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    /**
     * Gets the capacity.
     *
     * @return the capacity
     */
    public int getCapacity() { return capacity; }

    /**
     * Sets the capacity.
     *
     * @param capacity the capacity
     */
    public void setCapacity(int capacity) { this.capacity = capacity; }

    /**
     * Gets slot info as a formatted string.
     *
     * @return the slot info string
     */
    public String getSlotInfo() {
        return "Slot ID: " + slotId +
                " | " + startTime + " - " + endTime +
                " | Capacity: " + capacity +"|"+"centreId: "+ centreId;
    }
}
