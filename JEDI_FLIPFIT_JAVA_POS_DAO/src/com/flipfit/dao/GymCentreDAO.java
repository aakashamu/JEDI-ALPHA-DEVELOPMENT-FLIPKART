package com.flipfit.dao;

import java.util.List;
import com.flipfit.bean.GymCentre;
/**
 * The Interface GymCentreDAO.
 *
 * @author Ananya
 * @ClassName  "GymCentreDAO"
 */
public interface GymCentreDAO {
    void insertGymCentre(GymCentre centre);
    List<GymCentre> selectAllGymCentres();
    void updateGymCentreApproval(int centreId, boolean approved);
    void deleteGymCentre(int centreId);
    List<GymCentre> selectGymCentresByOwner(int ownerId);
}
