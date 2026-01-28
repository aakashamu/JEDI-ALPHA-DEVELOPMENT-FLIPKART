package com.flipfit.dao;

import java.util.List;
import com.flipfit.bean.GymCentre;
import com.flipfit.constants.GymCentreConstants;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;

/**
 * The Class GymCentreDAOImpl.
 *
 * @author Ananya
 * @ClassName  "GymCentreDAOImpl"
 */
public class GymCentreDAOImpl implements GymCentreDAO {
  /**
   * Get Connection.
   *
   * @return the Connection
   * @throws SQLException  
   */
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

  /**
   * Insert Gym Centre.
   *
   * @param centre the centre
   */
    @Override
    public void insertGymCentre(GymCentre centre) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(true); // Ensure autocommit is on
            PreparedStatement pstmt = conn.prepareStatement(GymCentreConstants.INSERT_GYM_CENTRE, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, centre.getName());
            pstmt.setString(2, centre.getCity());
            pstmt.setString(3, centre.getState());
            pstmt.setInt(4, centre.getOwnerId());
            pstmt.setInt(5, centre.isApproved() ? 1 : 0);
            int rows = pstmt.executeUpdate();
            
            // Get the generated centre ID
            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int centreId = generatedKeys.getInt(1);
                    centre.setCentreId(centreId);
                    System.out.println(rows + " gym centre inserted successfully with ID: " + centreId);
                }
                generatedKeys.close();
            }
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error inserting centre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

  /**
   * Select All Gym Centres.
   *
   * @return the List<GymCentre>
   */
    @Override
    public List<GymCentre> selectAllGymCentres() {
        List<GymCentre> centres = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GymCentreConstants.SELECT_ALL_GYM_CENTRES);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                GymCentre centre = new GymCentre();
                centre.setCentreId(rs.getInt("centreId"));
                centre.setName(rs.getString("centreName"));
                centre.setCity(rs.getString("city"));
                centre.setState(rs.getString("state"));
                centre.setOwnerId(rs.getInt("ownerId"));
                centre.setApproved(rs.getInt("isApproved") == 1);
                centres.add(centre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centres;
    }

  /**
   * Update Gym Centre Approval.
   *
   * @param centreId the centreId
   * @param approved the approved
   */
    @Override
    public void updateGymCentreApproval(int centreId, boolean approved) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GymCentreConstants.UPDATE_GYM_CENTRE_APPROVAL)) {
            pstmt.setInt(1, approved ? 1 : 0);
            pstmt.setInt(2, centreId);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " gym centre approval updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  /**
   * Delete Gym Centre.
   *
   * @param centreId the centreId
   */
    @Override
    public void deleteGymCentre(int centreId) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GymCentreConstants.DELETE_GYM_CENTRE)) {
            pstmt.setInt(1, centreId);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " gym centre deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  /**
   * Select Gym Centres By Owner.
   *
   * @param ownerId the ownerId
   * @return the List<GymCentre>
   */
    @Override
    public List<GymCentre> selectGymCentresByOwner(int ownerId) {
        List<GymCentre> centres = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GymCentreConstants.SELECT_GYM_CENTRES_BY_OWNER)) {
            pstmt.setInt(1, ownerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GymCentre centre = new GymCentre();
                    centre.setCentreId(rs.getInt("centreId"));
                    centre.setName(rs.getString("centreName"));
                    centre.setCity(rs.getString("city"));
                    centre.setState(rs.getString("state"));
                    centre.setOwnerId(rs.getInt("ownerId"));
                    centre.setApproved(rs.getInt("isApproved") == 1);
                    centres.add(centre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centres;
    }
}