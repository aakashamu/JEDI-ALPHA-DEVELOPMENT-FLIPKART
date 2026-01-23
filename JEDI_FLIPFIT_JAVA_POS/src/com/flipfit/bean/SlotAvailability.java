/**
 * 
 */
package com.flipfit.bean;

/**
 * 
 */
import java.time.LocalDate;

public class SlotAvailability {
	private int id;
	private int slotId;
	private LocalDate date;
	private boolean isAvailable;

	public SlotAvailability() {}

	public SlotAvailability(int id, int slotId, LocalDate date, boolean isAvailable) {
		this.id = id;
		this.slotId = slotId;
		this.date = date;
		this.isAvailable = isAvailable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSlotId() {
		return slotId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean available) {
		isAvailable = available;
	}
}
