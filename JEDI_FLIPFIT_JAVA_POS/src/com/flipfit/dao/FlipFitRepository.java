package com.flipfit.dao;

import com.flipfit.bean.*;
import java.util.*;

/**
 * Centralized Mock Database using Collections
 */
public class FlipFitRepository {
    // Global static lists reachable by all services
	public static Map<String, GymCustomer> customers = new HashMap<>();
	public static List<GymCentre> gymCentres = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();
    public static List<GymOwner> owners = new ArrayList<>();

    static {
        // Initial Hard-coded data for testing centers
        GymCentre center1 = new GymCentre();
        center1.setCentreId(1);
        center1.setName("FlipFit Elite - Koramangala");
        center1.setCity("Bengaluru");
        center1.setApproved(true);
        
        GymCentre center2 = new GymCentre();
        center2.setCentreId(2);
        center2.setName("FlipFit Pro - Indiranagar");
        center2.setCity("Bengaluru");
        center2.setApproved(true);

        gymCentres.add(center1);
        gymCentres.add(center2);
    }
}