package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.Slot;
import com.flipfit.bean.SlotAvailability;
import com.flipfit.dao.FlipFitRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GymCentreService implements GymCentreInterface {

    private final SlotService slotService = new SlotService();
    private final SlotAvailabilityService availabilityService = new SlotAvailabilityService();

    @Override
    public List<SlotAvailability> viewAvailableSlots(int centreId) {
        List<SlotAvailability> centreSpecificSlots = new ArrayList<>();
        
        // Get every single availability record across the whole system
        List<SlotAvailability> allAvailabilities = availabilityService.getAllSlotAvailabilities();
        
        for (SlotAvailability sa : allAvailabilities) {
            // Use getSlotById method to find the parent Slot details
            Slot parentSlot = slotService.getSlotById(sa.getSlotId());
           
            if (parentSlot != null && parentSlot.getCentreId() == centreId) {
                // Only add if it's currently marked as available
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
            System.out.println("✓ Gym centre added successfully: " + centre.getName());
        }
    }
    
    /**
     * Get all centres in a specific city (REQUIREMENT: View centers for a given city)
     */
    public List<GymCentre> viewCentresByCity(String city) {
        if (city == null || city.isEmpty()) {
            System.out.println("ERROR: City cannot be empty");
            return new ArrayList<>();
        }
        
        List<GymCentre> centresInCity = new ArrayList<>();
        for (GymCentre centre : FlipFitRepository.gymCentres) {
            if (centre.getCity().equalsIgnoreCase(city) && centre.isApproved()) {
                centresInCity.add(centre);
            }
        }
        
        System.out.println("\n========== GYM CENTRES IN " + city.toUpperCase() + " ==========");
        if (centresInCity.isEmpty()) {
            System.out.println("No approved centres found in " + city);
        } else {
            System.out.println("Total Centres: " + centresInCity.size());
            System.out.println("-----------------------------------------");
            for (GymCentre centre : centresInCity) {
                System.out.println("Centre ID: " + centre.getCentreId() +
                                 " | Name: " + centre.getName() +
                                 " | City: " + centre.getCity() +
                                 " | State: " + centre.getState() +
                                 " | Pincode: " + centre.getPincode());
            }
        }
        System.out.println("==================================\n");
        
        return centresInCity;
    }
    
    /**
     * BONUS FEATURE: Get the nearest available time slot for the same centre/date (REQUIREMENT: Nearest time slot)
     */
    public Slot getNearestTimeSlot(int centreId, LocalDate date, Slot referenceSlot) {
        if (referenceSlot == null) {
            System.out.println("ERROR: Reference slot cannot be null");
            return null;
        }
        
        // Get all available slots for this centre on the given date
        List<SlotAvailability> availableOnDate = new ArrayList<>();
        List<SlotAvailability> allAvailabilities = availabilityService.getAllSlotAvailabilities();
        
        for (SlotAvailability sa : allAvailabilities) {
            if (sa.getDate().equals(date) && sa.isAvailable()) {
                Slot slot = slotService.getSlotById(sa.getSlotId());
                if (slot != null && slot.getCentreId() == centreId) {
                    availableOnDate.add(sa);
                }
            }
        }
        
        if (availableOnDate.isEmpty()) {
            System.out.println("No available slots found for centre " + centreId + " on " + date);
            return null;
        }
        
        // Find the slot closest to the reference slot time
        return availableOnDate.stream()
            .map(sa -> slotService.getSlotById(sa.getSlotId()))
            .min(Comparator.comparingLong(slot -> 
                Math.abs(slot.getStartTime().toSecondOfDay() - referenceSlot.getStartTime().toSecondOfDay())))
            .orElse(null);
    }
}