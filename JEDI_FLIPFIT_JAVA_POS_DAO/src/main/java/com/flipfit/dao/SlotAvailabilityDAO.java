package com.flipfit.dao;

import com.flipfit.bean.SlotAvailability;
import java.time.LocalDate;
import java.util.List;
/**
 * The Interface SlotAvailabilityDAO.
 *
 * @author Ananya
 * @ClassName  "SlotAvailabilityDAO"
 */
public interface SlotAvailabilityDAO {
    /**
     * Adds availability for a slot
     * @param slotAvailability SlotAvailability object
     */
    void addSlotAvailability(SlotAvailability slotAvailability);

    /**
     * Updates slot availability details
     * @param id ID of the availability record
     * @param slotAvailability Updated details
     * @return true if update was successful
     */
    boolean updateSlotAvailability(int id, SlotAvailability slotAvailability);

    /**
     * Deletes availability for a slot
     * @param id ID of the availability record
     * @return true if deletion was successful
     */
    boolean deleteSlotAvailability(int id);

    /**
     * Retrieves availability by ID
     * @param id ID of the availability record
     * @return SlotAvailability object
     */
    SlotAvailability getSlotAvailabilityById(int id);

    /**
     * Retrieves all availability records
     * @return List of SlotAvailability
     */
    List<SlotAvailability> getAllSlotAvailabilities();

    /**
     * Retrieves available slots for a specific date
     * @param date LocalDate
     * @return List of SlotAvailability
     */
    List<SlotAvailability> getAvailableSlotsByDate(LocalDate date);

    /**
     * Retrieves availability for a specific slot ID
     * @param slotId Slot ID
     * @return List of SlotAvailability
     */
    List<SlotAvailability> getAvailableSlotsBySlotId(int slotId);
}
