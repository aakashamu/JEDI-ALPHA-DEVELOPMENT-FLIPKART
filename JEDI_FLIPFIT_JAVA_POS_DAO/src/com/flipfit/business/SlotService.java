package com.flipfit.business;

import com.flipfit.bean.Slot;

import java.util.*;
/**
 * The Class SlotService.
 *
 * @author Ananya
 * @ClassName  "SlotService"
 */
public class SlotService implements SlotInterface {

    private static final Map<Integer, Slot> slotDB = new HashMap<>();
    private static int idCounter = 513;  // Start after hardcoded slots (501-512)
  /**
   * Add Slot.
   *
   * @param slot the slot
   * @return the Slot
   */
    @Override
    public Slot addSlot(Slot slot) {

        validate(slot);

        slot.setSlotId(idCounter++);
        slotDB.put(slot.getSlotId(), slot);
        return slot;
    }
  /**
   * Update Slot.
   *
   * @param slotId the slotId
   * @param slot the slot
   * @return the Slot
   */
    @Override
    public Slot updateSlot(int slotId, Slot slot) {

        if (!slotDB.containsKey(slotId))
            throw new IllegalArgumentException("Slot not found");

        validate(slot);

        slot.setSlotId(slotId);
        slotDB.put(slotId, slot);
        return slot;
    }
  /**
   * Delete Slot.
   *
   * @param slotId the slotId
   * @return the boolean
   */
    @Override
    public boolean deleteSlot(int slotId) {
        return slotDB.remove(slotId) != null;
    }
  /**
   * Get Slot By Id.
   *
   * @param slotId the slotId
   * @return the Slot
   */
    @Override
    public Slot getSlotById(int slotId) {
        return slotDB.get(slotId);
    }
  /**
   * Get All Slots.
   *
   * @return the List<Slot>
   */
    @Override
    public List<Slot> getAllSlots() {
        List<Slot> list = new ArrayList<>(slotDB.values());
        list.sort(Comparator.comparing(Slot::getStartTime));
        return list;
    }
  /**
   * Get Slot Info.
   *
   * @param slotId the slotId
   * @return the String
   */
    @Override
    public String getSlotInfo(int slotId) {
        Slot slot = slotDB.get(slotId);
        return (slot != null) ? slot.getSlotInfo() : "Slot not found";
    }
  /**
   * Validate.
   *
   * @param slot the slot
   */
    private void validate(Slot slot) {
        if (slot.getStartTime() == null || slot.getEndTime() == null)
            throw new IllegalArgumentException("Time cannot be null");

        if (!slot.getStartTime().isBefore(slot.getEndTime()))
            throw new IllegalArgumentException("Start must be before end");

        if (slot.getCapacity() <= 0)
            throw new IllegalArgumentException("Capacity must be > 0");
    }
}
