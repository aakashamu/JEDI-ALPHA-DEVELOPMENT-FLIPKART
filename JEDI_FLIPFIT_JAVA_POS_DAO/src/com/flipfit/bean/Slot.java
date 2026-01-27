package com.flipfit.bean;

import java.time.LocalTime;

public class Slot {

    private int slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;
    private int centreId;

    public Slot(int slotId, LocalTime startTime, LocalTime endTime, int capacity,int centreId) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.centreId=centreId;
    }
    public int getCentreId() { 
        return centreId; 
    }
   public void setCentreId(int centreId) {this.centreId=centreId;}
    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getSlotInfo() {
        return "Slot ID: " + slotId +
                " | " + startTime + " - " + endTime +
                " | Capacity: " + capacity +"|"+"centreId: "+ centreId;
    }
}
