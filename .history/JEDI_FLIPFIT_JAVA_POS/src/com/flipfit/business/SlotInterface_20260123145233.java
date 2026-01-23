package com.flipfit.business;

import com.flipfit.bean.Slot;
import java.util.List;

public interface SlotInterface {

    Slot addSlot(Slot slot);

    Slot updateSlot(int slotId, Slot slot);

    boolean deleteSlot(int slotId);

    Slot getSlotById(int slotId);

    List<Slot> getAllSlots();

    String getSlotInfo(int slotId);
}
