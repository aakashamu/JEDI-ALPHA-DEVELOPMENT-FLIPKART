package com.flipfit.business;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.Slot;
import com.flipfit.bean.SlotAvailability;
import com.flipfit.dao.FlipFitRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * The Class GymCentreService.
 *
 * @author Ananya
 * @ClassName  "GymCentreService"
 */
public class GymCentreService implements GymCentreInterface {

    private final SlotService slotService = new SlotService();
    private final SlotAvailabilityService availabilityService = new SlotAvailabilityService();
    /**
     * View available slots.
     *
     * @param centreId the centre id
     * @return the list
     */
    @Override
    public List<SlotAvailability> viewAvailableSlots(int centreId) {
        List<SlotAvailability> allAvailabilities = availabilityService.getAllSlotAvailabilities();

        return allAvailabilities.stream()
                .filter(sa -> {
                    Slot parentSlot = slotService.getSlotById(sa.getSlotId());
                    return parentSlot != null && parentSlot.getCentreId() == centreId && sa.isAvailable();
                })
                .toList(); // Using Java 16+ toList() or collect(Collectors.toList()) for Java 8
        // Assuming Java 8 compatibility is safer: .collect(java.util.stream.Collectors.toList());
        // Since I can't confirm JDK version, I'll use collect(Collectors.toList()) to be safe.
    }

    /**
     * Gets the centre details.
     *
     * @param centreId the centre id
     * @return the centre details
     */
    @Override
    public GymCentre getCentreDetails(int centreId) {
        return FlipFitRepository.gymCentres.stream()
                .filter(centre -> centre.getCentreId() == centreId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds the gym centre.
     *
     * @param centre the centre
     */
    @Override
    public void addGymCentre(GymCentre centre) {
        if (centre != null) {
            FlipFitRepository.gymCentres.add(centre);
            System.out.println("✓ Gym centre added successfully: " + centre.getName());
        }
    }

    /**
     * View centres by city.
     *
     * @param city the city
     * @return the list
     */
    public List<GymCentre> viewCentresByCity(String city) {
        if (city == null || city.isEmpty()) {
            System.out.println("ERROR: City cannot be empty");
            return new ArrayList<>();
        }

        List<GymCentre> centresInCity = FlipFitRepository.gymCentres.stream()
                .filter(centre -> centre.getCity().equalsIgnoreCase(city) && centre.isApproved())
                .collect(java.util.stream.Collectors.toList());

        System.out.println("\n========== GYM CENTRES IN " + city.toUpperCase() + " ==========");
        if (centresInCity.isEmpty()) {
            System.out.println("No approved centres found in " + city);
        } else {
            System.out.println("Total Centres: " + centresInCity.size());
            System.out.println("-----------------------------------------");
            centresInCity.forEach(centre -> System.out.println("Centre ID: " + centre.getCentreId() +
                    " | Name: " + centre.getName() +
                    " | City: " + centre.getCity() +
                    " | State: " + centre.getState() +
                    " | Pincode: " + centre.getPincode()));
        }
        System.out.println("==================================\n");

        return centresInCity;
    }

    /**
     * Gets the nearest time slot.
     *
     * @param centreId      the centre id
     * @param date          the date
     * @param referenceSlot the reference slot
     * @return the nearest time slot
     */
    public Slot getNearestTimeSlot(int centreId, LocalDate date, Slot referenceSlot) {
        if (referenceSlot == null) {
            System.out.println("ERROR: Reference slot cannot be null");
            return null;
        }

        List<SlotAvailability> allAvailabilities = availabilityService.getAllSlotAvailabilities();

        List<SlotAvailability> availableOnDate = allAvailabilities.stream()
                .filter(sa -> sa.getDate().equals(date) && sa.isAvailable())
                .filter(sa -> {
                    Slot slot = slotService.getSlotById(sa.getSlotId());
                    return slot != null && slot.getCentreId() == centreId;
                })
                .collect(java.util.stream.Collectors.toList());

        if (availableOnDate.isEmpty()) {
            System.out.println("No available slots found for centre " + centreId + " on " + date);
            return null;
        }

        return availableOnDate.stream()
                .map(sa -> slotService.getSlotById(sa.getSlotId()))
                .min(Comparator.comparingLong(slot -> Math.abs(
                        slot.getStartTime().toSecondOfDay() - referenceSlot.getStartTime().toSecondOfDay())))
                .orElse(null);
    }
}