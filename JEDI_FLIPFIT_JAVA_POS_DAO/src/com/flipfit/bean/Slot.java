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
   * Slot.
   *
   * @return the public
   */
    public Slot() {}
  /**
   * Slot.
   *
   * @param slotId the slotId
   * @param startTime the startTime
   * @param endTime the endTime
   * @param capacity the capacity
   * @param centreId the centreId
   * @return the public
   */
    public Slot(int slotId, LocalTime startTime, LocalTime endTime, int capacity,int centreId) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.centreId=centreId;
    }
  /**
   * Get Centre Id.
   *
   * @return the int
   */
    public int getCentreId() { 
        return centreId; 
    }
  /**
   * Set Centre Id.
   *
   * @param centreId the centreId
   */
   public void setCentreId(int centreId) {this.centreId=centreId;}
  /**
   * Get Slot Id.
   *
   * @return the int
   */
    public int getSlotId() { return slotId; }
  /**
   * Set Slot Id.
   *
   * @param slotId the slotId
   */
    public void setSlotId(int slotId) { this.slotId = slotId; }
  /**
   * Get Start Time.
   *
   * @return the LocalTime
   */
    public LocalTime getStartTime() { return startTime; }
  /**
   * Set Start Time.
   *
   * @param startTime the startTime
   */
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
  /**
   * Get End Time.
   *
   * @return the LocalTime
   */
    public LocalTime getEndTime() { return endTime; }
  /**
   * Set End Time.
   *
   * @param endTime the endTime
   */
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
  /**
   * Get Capacity.
   *
   * @return the int
   */
    public int getCapacity() { return capacity; }
  /**
   * Set Capacity.
   *
   * @param capacity the capacity
   */
    public void setCapacity(int capacity) { this.capacity = capacity; }
  /**
   * Get Slot Info.
   *
   * @return the String
   */
    public String getSlotInfo() {
        return "Slot ID: " + slotId +
                " | " + startTime + " - " + endTime +
                " | Capacity: " + capacity +"|"+"centreId: "+ centreId;
    }
}
