package com.flipfit.dao;

import com.flipfit.bean.GymCentre;
import com.flipfit.bean.GymCustomer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymOwnerImpl implements GymOwnerInterface {
    // Standard JDBC connection details
    private static final String URL = "jdbc:mysql://localhost:3306/flipfit_schema";
    private static final String USER = "root";
    private static final String PASS = "password";

    @Override
    public boolean registerNewCentre(GymCentre centre) {
        String query = "INSERT INTO GymCentre (centreName, city, state, ownerId, isApproved) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, centre.getName());
            stmt.setString(2, centre.getCity());
            stmt.setString(3, centre.getState());
            stmt.setInt(4, centre.getOwnerId());
            stmt.setInt(5, 0); // Default to Pending
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<GymCentre> viewMyCentres(int ownerId) {
        List<GymCentre> centres = new ArrayList<>();
        String query = "SELECT * FROM GymCentre WHERE ownerId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GymCentre centre = new GymCentre();
                centre.setCentreId(rs.getInt("centreId"));
                centre.setName(rs.getString("centreName"));
                centre.setCity(rs.getString("city"));
                centre.setApproved(rs.getInt("isApproved") == 1);
                centres.add(centre);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return centres;
    }

    @Override
    public void requestApproval(int gymOwnerId) {
        String query = "UPDATE GymOwner SET isApproved = 0 WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, gymOwnerId);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<GymCustomer> viewCustomers(int gymCentreId) {
        // Logic to fetch customers who have bookings at this centre
        return new ArrayList<>();
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        String query = "UPDATE Booking SET status = 'CANCELLED' WHERE bookingId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}