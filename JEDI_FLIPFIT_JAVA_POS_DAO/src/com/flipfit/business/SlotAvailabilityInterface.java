/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
import com.flipfit.bean.SlotAvailability;
import java.time.LocalDate;
import java.util.List;
/**
 * The Interface SlotAvailabilityInterface.
 *
 * @author Ananya
 * @ClassName  "SlotAvailabilityInterface"
 */
public interface SlotAvailabilityInterface {
	SlotAvailability addSlotAvailability(SlotAvailability slotAvailability);
	SlotAvailability updateSlotAvailability(int id, SlotAvailability slotAvailability);
	boolean deleteSlotAvailability(int id);
	SlotAvailability getSlotAvailabilityById(int id);
	List<SlotAvailability> getAllSlotAvailabilities();
	List<SlotAvailability> getAvailableSlotsByDate(LocalDate date);
	List<SlotAvailability> getAvailableSlotsBySlotId(int slotId);
}
