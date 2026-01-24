package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.Slot;
import com.flipfit.bean.SlotAvailability;
import com.flipfit.dao.FlipFitRepository;
import java.util.ArrayList;
import java.util.List;

public class GymCentreService implements GymCentreInterface {

    private final SlotService slotService = new SlotService();
    private final SlotAvailabilityService availabilityService = new SlotAvailabilityService();

    @Override
    public List<SlotAvailability> viewAvailableSlots(int centreId) {
        List<SlotAvailability> centreSpecificSlots = new ArrayList<>();
        
        // 1. Get every single availability record across the whole system
        List<SlotAvailability> allAvailabilities = availabilityService.getAllSlotAvailabilities();
        
        for (SlotAvailability sa : allAvailabilities) {
            // 2. Use your getSlotById method to find the parent Slot details
            Slot parentSlot = slotService.getSlotById(sa.getSlotId());
           
            if (parentSlot != null && parentSlot.getCentreId() == centreId) {
                // 4. Only add if it's currently marked as available
                if (sa.isAvailable()) {
                    centreSpecificSlots.add(sa);
                }
            }
        }
        
        return centreSpecificSlots;
    }

    @Override
    public GymCentre getCentreDetails(int centreId) {
        // Direct search in your FlipFitRepository static list
        for (GymCentre centre : FlipFitRepository.gymCentres) {
            if (centre.getCentreId() == centreId) {
                return centre;
            }
        }
        return null; // Return null if center doesn't exist
    }

    @Override
    public void addGymCentre(GymCentre centre) {
        if (centre != null) {
            FlipFitRepository.gymCentres.add(centre);
        }
    }
}