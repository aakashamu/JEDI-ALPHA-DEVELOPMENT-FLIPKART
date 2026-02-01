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
   * @return true if successful, false otherwise
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
     * Gets the available slots by date.
     *
     * @param date the date
     * @return the available slots by date
     */
    @Override
    public List<SlotAvailability> getAvailableSlotsByDate(LocalDate date) {
        return slotAvailabilityDB.values().stream()
                .filter(sa -> sa.getDate().equals(date) && sa.isAvailable())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Gets the available slots by slot id.
     *
     * @param slotId the slot id
     * @return the available slots by slot id
     */
    @Override
    public List<SlotAvailability> getAvailableSlotsBySlotId(int slotId) {
        return slotAvailabilityDB.values().stream()
                .filter(sa -> sa.getSlotId() == slotId && sa.isAvailable())
                .collect(java.util.stream.Collectors.toList());
    }
}
