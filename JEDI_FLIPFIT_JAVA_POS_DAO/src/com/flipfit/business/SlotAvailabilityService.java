/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
import com.flipfit.bean.SlotAvailability;
import java.time.LocalDate;
import java.util.*;
/**
 * The Class SlotAvailabilityService.
 *
 * @author Ananya
 * @ClassName  "SlotAvailabilityService"
 */
public class SlotAvailabilityService implements SlotAvailabilityInterface {
	private static final Map<Integer, SlotAvailability> slotAvailabilityDB = new HashMap<>();
	private static int idCounter = 2001;  // Start from 2001 to avoid conflicts
  /**
   * Add Slot Availability.
   *
   * @param slotAvailability the slotAvailability
   * @return the SlotAvailability
   */
	@Override
	public SlotAvailability addSlotAvailability(SlotAvailability slotAvailability) {
		slotAvailability.setId(idCounter++);
		slotAvailabilityDB.put(slotAvailability.getId(), slotAvailability);
		return slotAvailability;
	}
  /**
   * Update Slot Availability.
   *
   * @param id the id
   * @param slotAvailability the slotAvailability
   * @return the SlotAvailability
   */
	@Override
	public SlotAvailability updateSlotAvailability(int id, SlotAvailability slotAvailability) {
		if (slotAvailabilityDB.containsKey(id)) {
			slotAvailability.setId(id);
			slotAvailabilityDB.put(id, slotAvailability);
			return slotAvailability;
		}
		return null;
	}
  /**
   * Delete Slot Availability.
   *
   * @param id the id
   * @return the boolean
   */
	@Override
	public boolean deleteSlotAvailability(int id) {
		return slotAvailabilityDB.remove(id) != null;
	}
  /**
   * Get Slot Availability By Id.
   *
   * @param id the id
   * @return the SlotAvailability
   */
	@Override
	public SlotAvailability getSlotAvailabilityById(int id) {
		return slotAvailabilityDB.get(id);
	}
  /**
   * Get All Slot Availabilities.
   *
   * @return the List<SlotAvailability>
   */
	@Override
	public List<SlotAvailability> getAllSlotAvailabilities() {
		return new ArrayList<>(slotAvailabilityDB.values());
	}
  /**
   * Get Available Slots By Date.
   *
   * @param date the date
   * @return the List<SlotAvailability>
   */
	@Override
	public List<SlotAvailability> getAvailableSlotsByDate(LocalDate date) {
		List<SlotAvailability> result = new ArrayList<>();
		for (SlotAvailability sa : slotAvailabilityDB.values()) {
			if (sa.getDate().equals(date) && sa.isAvailable()) {
				result.add(sa);
			}
		}
		return result;
	}
  /**
   * Get Available Slots By Slot Id.
   *
   * @param slotId the slotId
   * @return the List<SlotAvailability>
   */
	@Override
	public List<SlotAvailability> getAvailableSlotsBySlotId(int slotId) {
		List<SlotAvailability> result = new ArrayList<>();
		for (SlotAvailability sa : slotAvailabilityDB.values()) {
			if (sa.getSlotId() == slotId && sa.isAvailable()) {
				result.add(sa);
			}
		}
		return result;
	}
}
