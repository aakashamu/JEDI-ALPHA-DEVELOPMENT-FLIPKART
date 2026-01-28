package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The Class BookingDAOImpl.
 *
 * @author Ananya
 * @ClassName  "BookingDAOImpl"
 */
public class BookingDAOImpl implements BookingDAO {
  /**
   * Create Booking.
   *
   * @param customerId the customerId
   * @param availabilityId the availabilityId
   * @return the Booking
   */
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
  /**
   * Create Pending Booking.
   *
   * @param customerId the customerId
   * @param availabilityId the availabilityId
   * @return the Booking
   */
    public Booking createPendingBooking(int customerId, int availabilityId) {
        System.out.println("[DEBUG] createPendingBooking called with customerId=" + customerId + ", availabilityId=" + availabilityId);
        String query = "INSERT INTO Booking (userId, availabilityId, status, bookingDate, createdAt) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            String status = "PENDING";
            java.util.Date now = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(now.getTime());
            String createdAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, availabilityId);
            stmt.setString(3, status);
            stmt.setDate(4, sqlDate);
            stmt.setString(5, createdAt);
            
            System.out.println("[DEBUG] Executing PENDING booking insert...");
            int affectedRows = stmt.executeUpdate();
            System.out.println("[DEBUG] Affected rows: " + affectedRows);
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int bookingId = generatedKeys.getInt(1);
                        System.out.println("[DEBUG] PENDING Booking created with ID: " + bookingId);
                        Booking booking = new Booking();
                        booking.setBookingId(bookingId);
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
            System.out.println("[ERROR] SQLException in createPendingBooking: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[ERROR] createPendingBooking returning null");
        return null;
    }
  /**
   * Cancel Booking.
   *
   * @param bookingId the bookingId
   * @return the boolean
   */
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
  /**
   * Get Customer Bookings.
   *
   * @param customerId the customerId
   * @return the List<Booking>
   */
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
  /**
   * Check Booking Status.
   *
   * @param bookingId the bookingId
   * @return the boolean
   */
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
  /**
   * Get All Bookings.
   *
   * @return the List<Booking>
   */
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
  /**
   * Confirm Waitlist Booking.
   *
   * @param bookingId the bookingId
   * @return the boolean
   */
    public boolean confirmWaitlistBooking(int bookingId) {
        String query = "UPDATE Booking SET status = 'CONFIRMED' WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
  /**
   * Get Booking By Id.
   *
   * @param bookingId the bookingId
   * @return the Booking
   */
    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM Booking WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("bookingId"));
                    booking.setCustomerId(rs.getInt("userId"));
                    booking.setAvailabilityId(rs.getInt("availabilityId"));
                    booking.setStatus(rs.getString("status"));
                    booking.setBookingDate(rs.getDate("bookingDate"));
                    booking.setCreatedAt(rs.getString("createdAt"));
                    return booking;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
