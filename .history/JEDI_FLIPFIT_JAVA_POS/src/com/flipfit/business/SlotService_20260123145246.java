package com.flipfit.business;

import com.flipfit.bean.Slot;

import java.time.LocalTime;
import java.util.*;

public class SlotService implements SlotInterface {

    private static final Map<Integer, Slot> slotDB = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public Slot addSlot(Slot slot) {

        validateSlot(slot);

        // Overlap check
        for (Slot s : slotDB.values()) {
            if (isOverlapping(s.getStartTime(), s.getEndTime(),
                    slot.getStartTime(), slot.getEndTime())) {
                throw new IllegalArgumentException("Overlapping slot exists.");
            }
        }

        slot.setSlotId(idCounter++);
        slotDB.put(slot.getSlotId(), slot);
        return slot;
    }

    @Override
    public Slot updateSlot(int slotId, Slot slot) {

        if (!slotDB.containsKey(slotId))
            throw new IllegalArgumentException("Slot not found");

        validateSlot(slot);

        for (Slot s : slotDB.values()) {
            if (s.getSlotId() != slotId &&
                isOverlapping(s.getStartTime(), s.getEndTime(),
                        slot.getStartTime(), slot.getEndTime())) {
                throw new IllegalArgumentException("Update causes overlapping slot.");
            }
        }

        slot.setSlotId(slotId);
        slotDB.put(slotId, slot);
        return slot;
    }

    @Override
    public boolean deleteSlot(int slotId) {
        return slotDB.remove(slotId) != null;
    }

    @Override
    public Slot getSlotById(int slotId) {
        return slotDB.get(slotId);
    }

    @Override
    public List<Slot> getAllSlots() {
        List<Slot> list = new ArrayList<>(slotDB.values());
        list.sort(Comparator.comparing(Slot::getStartTime));
        return list;
    }

    @Override
    public String getSlotInfo(int slotId) {
        Slot slot = slotDB.get(slotId);
        return (slot != null) ? slot.getSlotInfo() : "Slot not found";
    }

    // ---------- helpers ----------

    private void validateSlot(Slot slot) {
        if (slot.getStartTime() == null || slot.getEndTime() == null)
            throw new IllegalArgumentException("Invalid time.");

        if (!slot.getStartTime().isBefore(slot.getEndTime()))
            throw new IllegalArgumentException("Start must be before end.");

        if (slot.getCapacity() <= 0)
            throw new IllegalArgumentException("Capacity must be > 0.");
    }

    private boolean isOverlapping(LocalTime s1, LocalTime e1,
                                  LocalTime s2, LocalTime e2) {
        return s1.isBefore(e2) && e1.isAfter(s2);
    }
}
