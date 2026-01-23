package com.flipfit.bean;

import java.time.LocalTime;

public class Slot {

    private int slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;

    public Slot(int slotId, LocalTime startTime, LocalTime endTime, int capacity) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }

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
                " | Capacity: " + capacity;
    }
}
