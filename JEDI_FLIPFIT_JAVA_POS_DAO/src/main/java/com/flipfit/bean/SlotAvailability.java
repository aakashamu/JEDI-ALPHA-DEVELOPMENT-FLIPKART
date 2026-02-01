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

	/**
	 * Default constructor.
	 */
	public SlotAvailability() {}

	/**
	 * Constructor with id, slot, date and availability flag.
	 *
	 * @param id          the availability id
	 * @param slotId      the slot id
	 * @param date        the date
	 * @param isAvailable the availability flag
	 */
	public SlotAvailability(int id, int slotId, LocalDate date, boolean isAvailable) {
		this.id = id;
		this.slotId = slotId;
		this.date = date;
		this.isAvailable = isAvailable;
	}

	/**
	 * Constructor with id, slot, date and seat counts.
	 *
	 * @param id             the availability id
	 * @param slotId         the slot id
	 * @param date           the date
	 * @param seatsAvailable the seats available
	 * @param seatsTotal     the total seats
	 */
	public SlotAvailability(int id, int slotId, LocalDate date, int seatsAvailable, int seatsTotal) {
		this.id = id;
		this.slotId = slotId;
		this.date = date;
		this.seatsAvailable = seatsAvailable;
		this.seatsTotal = seatsTotal;
		this.isAvailable = seatsAvailable > 0;
	}
	/**
	 * Gets the availability id.
	 *
	 * @return the availability id
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets the availability id.
	 *
	 * @param id the availability id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Gets the slot id.
	 *
	 * @return the slot id
	 */
	public int getSlotId() {
		return slotId;
	}
	/**
	 * Sets the slot id.
	 *
	 * @param slotId the slot id
	 */
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
	/**
	 * Sets the date.
	 *
	 * @param date the date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	/**
	 * Checks if the slot is available.
	 *
	 * @return true if available
	 */
	public boolean isAvailable() {
		return isAvailable;
	}
	/**
	 * Sets the availability flag.
	 *
	 * @param available the availability flag
	 */
	public void setAvailable(boolean available) {
		isAvailable = available;
	}
	/**
	 * Gets the seats available.
	 *
	 * @return the seats available
	 */
	public int getSeatsAvailable() {
		return seatsAvailable;
	}
	/**
	 * Sets the seats available.
	 *
	 * @param seatsAvailable the seats available
	 */
	public void setSeatsAvailable(int seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
	/**
	 * Gets the total seats.
	 *
	 * @return the total seats
	 */
	public int getSeatsTotal() {
		return seatsTotal;
	}
	/**
	 * Sets the total seats.
	 *
	 * @param seatsTotal the total seats
	 */
	public void setSeatsTotal(int seatsTotal) {
		this.seatsTotal = seatsTotal;
	}
}
