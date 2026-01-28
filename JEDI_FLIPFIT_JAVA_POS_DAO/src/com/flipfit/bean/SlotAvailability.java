/**
 * 
 */
package com.flipfit.bean;

/**
 * 
 */
import java.time.LocalDate;
/**
 * The Class SlotAvailability.
 *
 * @author Ananya
 * @ClassName  "SlotAvailability"
 */
public class SlotAvailability {
	private int id;
	private int slotId;
	private LocalDate date;
	private boolean isAvailable;
	private int seatsAvailable;
	private int seatsTotal;

	public SlotAvailability() {}

	public SlotAvailability(int id, int slotId, LocalDate date, boolean isAvailable) {
		this.id = id;
		this.slotId = slotId;
		this.date = date;
		this.isAvailable = isAvailable;
	}

	public SlotAvailability(int id, int slotId, LocalDate date, int seatsAvailable, int seatsTotal) {
		this.id = id;
		this.slotId = slotId;
		this.date = date;
		this.seatsAvailable = seatsAvailable;
		this.seatsTotal = seatsTotal;
		this.isAvailable = seatsAvailable > 0;
	}
  /**
   * Get Id.
   *
   * @return the int
   */
	public int getId() {
		return id;
	}
  /**
   * Set Id.
   *
   * @param id the id
   */
	public void setId(int id) {
		this.id = id;
	}
  /**
   * Get Slot Id.
   *
   * @return the int
   */
	public int getSlotId() {
		return slotId;
	}
  /**
   * Set Slot Id.
   *
   * @param slotId the slotId
   */
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}
  /**
   * Get Date.
   *
   * @return the LocalDate
   */
	public LocalDate getDate() {
		return date;
	}
  /**
   * Set Date.
   *
   * @param date the date
   */
	public void setDate(LocalDate date) {
		this.date = date;
	}
  /**
   * Is Available.
   *
   * @return the boolean
   */
	public boolean isAvailable() {
		return isAvailable;
	}
  /**
   * Set Available.
   *
   * @param available the available
   */
	public void setAvailable(boolean available) {
		isAvailable = available;
	}
  /**
   * Get Seats Available.
   *
   * @return the int
   */
	public int getSeatsAvailable() {
		return seatsAvailable;
	}
  /**
   * Set Seats Available.
   *
   * @param seatsAvailable the seatsAvailable
   */
	public void setSeatsAvailable(int seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
  /**
   * Get Seats Total.
   *
   * @return the int
   */
	public int getSeatsTotal() {
		return seatsTotal;
	}
  /**
   * Set Seats Total.
   *
   * @param seatsTotal the seatsTotal
   */
	public void setSeatsTotal(int seatsTotal) {
		this.seatsTotal = seatsTotal;
	}
}
