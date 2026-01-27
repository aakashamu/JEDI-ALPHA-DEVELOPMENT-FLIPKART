package com.flipfit.dao;

import com.flipfit.bean.GymCentre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.flipfit.utils.DBConnection;

public class GymCentreDAOImpl implements GymCentreDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public void insertGymCentre(GymCentre centre) {
        String sql = "INSERT INTO GymCentre (centreName, city, state, ownerId, isApproved) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(true); // Ensure autocommit is on
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

    @Override
    public List<GymCentre> selectAllGymCentres() {
        List<GymCentre> centres = new ArrayList<>();
        String sql = "SELECT * FROM GymCentre";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
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

    @Override
    public void updateGymCentreApproval(int centreId, boolean approved) {
        String sql = "UPDATE GymCentre SET isApproved = ? WHERE centreId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, approved ? 1 : 0);
            pstmt.setInt(2, centreId);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " gym centre approval updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGymCentre(int centreId) {
        String sql = "DELETE FROM GymCentre WHERE centreId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, centreId);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " gym centre deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GymCentre> selectGymCentresByOwner(int ownerId) {
        List<GymCentre> centres = new ArrayList<>();
        String sql = "SELECT * FROM GymCentre WHERE ownerId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
