package com.flipfit.business;

import com.flipfit.bean.Slot;
import java.util.List;
/**
 * The Interface SlotInterface.
 *
 * @author Ananya
 * @ClassName  "SlotInterface"
 */
public interface SlotInterface {

    Slot addSlot(Slot slot);

    Slot updateSlot(int slotId, Slot slot);

    boolean deleteSlot(int slotId);

    Slot getSlotById(int slotId);

    List<Slot> getAllSlots();

    String getSlotInfo(int slotId);
}
