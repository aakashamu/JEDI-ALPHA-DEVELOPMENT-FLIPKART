package com.flipfit.dao;

import com.flipfit.bean.GymAdmin;
import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymOwner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for GymAdmin-related DB operations. Minimal implementation — update SQL/schema as needed.
 */
public class GymAdminDAO {

    // Database connection constants
    private static final String URL = "jdbc:mysql://localhost:3306/FlipFit_schema";
    private static final String USER = "root";
    private static final String PASS = "password";

    /**
     * Insert a GymAdmin into DB. Assumes a table `gym_admin` exists.
     */
    public void insertAdmin(GymAdmin admin) {
        String sql = "INSERT INTO gym_admin (user_id, full_name, email, password, phone_number, city, pincode) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, admin.getUserId());
            ps.setString(2, admin.getFullName());
            ps.setString(3, admin.getEmail());
            ps.setString(4, admin.getPassword());
            ps.setLong(5, admin.getPhoneNumber());
            ps.setString(6, admin.getCity());
            ps.setInt(7, admin.getPincode());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERROR inserting admin: " + e.getMessage());
        }
    }

    /**
     * Example DB-backed retrieval of pending gym owners.
     * If you don't have a DB table for owners, this can be implemented to fall back to in-memory data.
     */
    public List<GymOwner> getPendingOwnersFromDB() {
        List<GymOwner> pending = new ArrayList<>();
        String sql = "SELECT user_id, full_name, email, phone_number, city, pincode, pan, aadhaar, gstin FROM gym_owner WHERE is_approved = 0";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                GymOwner owner = new GymOwner();
                owner.setUserId(rs.getInt("user_id"));
                owner.setFullName(rs.getString("full_name"));
                owner.setEmail(rs.getString("email"));
                owner.setPhoneNumber(rs.getLong("phone_number"));
                owner.setCity(rs.getString("city"));
                owner.setPincode(rs.getInt("pincode"));
                owner.setPAN(rs.getString("pan"));
                owner.setAadhaar(rs.getString("aadhaar"));
                owner.setGSTIN(rs.getString("gstin"));
                pending.add(owner);
            }
        } catch (SQLException e) {
            // If DB not available or table missing, caller can fallback to in-memory
            System.out.println("DB fetch pending owners failed: " + e.getMessage());
        }
        return pending;
    }

    /**
     * Example DB-backed retrieval of pending gym centres.
     */
    public List<GymCentre> getPendingCentresFromDB() {
        List<GymCentre> pending = new ArrayList<>();
        String sql = "SELECT centre_id, name, city, owner_id FROM gym_centre WHERE is_approved = 0";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                GymCentre centre = new GymCentre();
                centre.setCentreId(rs.getInt("centre_id"));
                centre.setName(rs.getString("name"));
                centre.setCity(rs.getString("city"));
                centre.setOwnerId(rs.getInt("owner_id"));
                centre.setApproved(false);
                pending.add(centre);
            }
        } catch (SQLException e) {
            System.out.println("DB fetch pending centres failed: " + e.getMessage());
        }
        return pending;
    }
}
