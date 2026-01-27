package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public Booking createBooking(int customerId, int availabilityId) {
        String query = "INSERT INTO Booking (userId, availabilityId, status, bookingDate, createdAt) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            String status = "CONFIRMED";
            java.util.Date now = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(now.getTime());
            String createdAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, availabilityId);
            stmt.setString(3, status);
            stmt.setDate(4, sqlDate);
            stmt.setString(5, createdAt);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Booking booking = new Booking();
                        booking.setBookingId(generatedKeys.getInt(1));
                        booking.setCustomerId(customerId);
                        booking.setAvailabilityId(availabilityId);
                        booking.setStatus(status);
                        booking.setBookingDate(now);
                        booking.setCreatedAt(createdAt);
                        return booking;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        String query = "UPDATE Booking SET status = 'CANCELLED' WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Booking> getCustomerBookings(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Booking WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("bookingId"));
                booking.setCustomerId(rs.getInt("userId"));
                booking.setAvailabilityId(rs.getInt("availabilityId"));
                booking.setStatus(rs.getString("status"));
                booking.setBookingDate(rs.getDate("bookingDate"));
                booking.setCreatedAt(rs.getString("createdAt"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean checkBookingStatus(int bookingId) {
        String query = "SELECT status FROM Booking WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return "CONFIRMED".equalsIgnoreCase(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Booking";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("bookingId"));
                booking.setCustomerId(rs.getInt("userId"));
                booking.setAvailabilityId(rs.getInt("availabilityId"));
                booking.setStatus(rs.getString("status"));
                booking.setBookingDate(rs.getDate("bookingDate"));
                booking.setCreatedAt(rs.getString("createdAt"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
