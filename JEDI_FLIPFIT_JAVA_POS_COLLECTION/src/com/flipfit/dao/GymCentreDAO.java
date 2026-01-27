package com.flipfit.dao;

import java.util.List;
import com.flipfit.bean.GymCentre;

public interface GymCentreDAO {
    // Insert a new GymCentre
    void insertGymCentre(GymCentre centre);

    // Get all GymCentres
    List<GymCentre> selectAllGymCentres();

    // Update GymCentre approval status
    void updateGymCentreApproval(int centreId, boolean approved);

    // Delete a GymCentre by ID
    void deleteGymCentre(int centreId);

    // Get GymCentres by ownerId
    List<GymCentre> selectGymCentresByOwner(int ownerId);
}
