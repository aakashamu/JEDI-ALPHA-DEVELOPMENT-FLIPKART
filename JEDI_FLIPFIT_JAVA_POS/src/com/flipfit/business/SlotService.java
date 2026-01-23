package com.flipfit.business;

import com.flipfit.bean.Slot;

import java.util.*;

public class SlotService implements SlotInterface {

    private static final Map<Integer, Slot> slotDB = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public Slot addSlot(Slot slot) {

        validate(slot);

        slot.setSlotId(idCounter++);
        slotDB.put(slot.getSlotId(), slot);
        return slot;
    }

    @Override
    public Slot updateSlot(int slotId, Slot slot) {

        if (!slotDB.containsKey(slotId))
            throw new IllegalArgumentException("Slot not found");

        validate(slot);

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

    private void validate(Slot slot) {
        if (slot.getStartTime() == null || slot.getEndTime() == null)
            throw new IllegalArgumentException("Time cannot be null");

        if (!slot.getStartTime().isBefore(slot.getEndTime()))
            throw new IllegalArgumentException("Start must be before end");

        if (slot.getCapacity() <= 0)
            throw new IllegalArgumentException("Capacity must be > 0");
    }
}
