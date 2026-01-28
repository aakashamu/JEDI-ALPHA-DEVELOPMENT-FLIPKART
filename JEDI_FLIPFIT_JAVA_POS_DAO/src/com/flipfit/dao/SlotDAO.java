package com.flipfit.dao;

import com.flipfit.bean.Slot;
import java.util.List;
/**
 * The Interface SlotDAO.
 *
 * @author Ananya
 * @ClassName  "SlotDAO"
 */
public interface SlotDAO {
    /**
     * Adds a new slot to the system
     * @param slot Slot object to be added
     */
    void addSlot(Slot slot);

    /**
     * Updates an existing slot
     * @param slotId ID of the slot to update
     * @param slot Updated slot details
     * @return true if update was successful, false otherwise
     */
    boolean updateSlot(int slotId, Slot slot);

    /**
     * Deletes a slot from the system
     * @param slotId ID of the slot to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteSlot(int slotId);

    /**
     * Retrieves a slot by its ID
     * @param slotId ID of the slot
     * @return Slot object if found, null otherwise
     */
    Slot getSlotById(int slotId);

    /**
     * Retrieves all slots in the system
     * @return List of all slots
     */
    List<Slot> getAllSlots();

    /**
     * Retrieves all slots available in a specific gym centre
     * @param centreId ID of the gym centre
     * @return List of slots in the centre
     */
    List<Slot> getSlotsByCentreId(int centreId);
}
