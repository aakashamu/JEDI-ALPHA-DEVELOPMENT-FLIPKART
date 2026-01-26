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

public class SlotAvailabilityService implements SlotAvailabilityInterface {
	private static final Map<Integer, SlotAvailability> slotAvailabilityDB = new HashMap<>();
	private static int idCounter = 2001;  // Start from 2001 to avoid conflicts

	@Override
	public SlotAvailability addSlotAvailability(SlotAvailability slotAvailability) {
		slotAvailability.setId(idCounter++);
		slotAvailabilityDB.put(slotAvailability.getId(), slotAvailability);
		return slotAvailability;
	}

	@Override
	public SlotAvailability updateSlotAvailability(int id, SlotAvailability slotAvailability) {
		if (slotAvailabilityDB.containsKey(id)) {
			slotAvailability.setId(id);
			slotAvailabilityDB.put(id, slotAvailability);
			return slotAvailability;
		}
		return null;
	}

	@Override
	public boolean deleteSlotAvailability(int id) {
		return slotAvailabilityDB.remove(id) != null;
	}

	@Override
	public SlotAvailability getSlotAvailabilityById(int id) {
		return slotAvailabilityDB.get(id);
	}

	@Override
	public List<SlotAvailability> getAllSlotAvailabilities() {
		return new ArrayList<>(slotAvailabilityDB.values());
	}

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
