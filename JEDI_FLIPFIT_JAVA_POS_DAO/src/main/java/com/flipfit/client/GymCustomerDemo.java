package com.flipfit.client;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCustomer;
import com.flipfit.business.GymCustomerService;
import java.util.List;
/**
 * The Class GymCustomerDemo.
 *
 * @author Ananya
 * @ClassName  "GymCustomerDemo"
 */
public class GymCustomerDemo {
  /**
   * Run demo.
   */
    public static void runDemo() {
        System.out.println("========== Gym Customer Demo ==========\n");

        GymCustomerService gymCustomerService = new GymCustomerService();

        // Test 1: Create a new customer
        System.out.println("Test 1: Creating Gym Customer");
        System.out.println("------------------------------");
        GymCustomer customer = new GymCustomer();
        customer.setCustomerId(201);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhoneNumber("9876543210");
        customer.setAge(28);
        customer.setGender("Male");

        gymCustomerService.registerCustomer(customer);
        System.out.println("Customer registered successfully!\n");

        // Test 2: View all customers
        System.out.println("Test 2: View All Customers");
        System.out.println("--------------------------");
        List<GymCustomer> customers = gymCustomerService.getAllCustomers();
        for (GymCustomer cust : customers) {
            System.out.println("Customer ID: " + cust.getCustomerId() + ", Name: " + cust.getName() 
                + ", Email: " + cust.getEmail());
        }

        // Test 3: Get customer by ID
        System.out.println("\nTest 3: Get Customer by ID");
        System.out.println("---------------------------");
        GymCustomer fetchedCustomer = gymCustomerService.getCustomerById(201);
        if (fetchedCustomer != null) {
            System.out.println("Found Customer: " + fetchedCustomer.getName() + " - " + fetchedCustomer.getEmail());
        } else {
            System.out.println("Customer not found.");
        }

        // Test 4: Update customer
        System.out.println("\nTest 4: Update Customer");
        System.out.println("------------------------");
        if (fetchedCustomer != null) {
            fetchedCustomer.setPhoneNumber("9876543211");
            gymCustomerService.updateCustomer(fetchedCustomer);
            System.out.println("Customer updated successfully!");
        }

        // Test 5: Book a slot
        System.out.println("\nTest 5: Book a Slot");
        System.out.println("-------------------");
        Booking booking = new Booking();
        booking.setBookingId(501);
        booking.setCustomerId(201);
        booking.setSlotId(101);
        booking.setBookingDate(new java.util.Date());
        booking.setStatus("CONFIRMED");

        gymCustomerService.bookSlot(booking);
        System.out.println("Slot booked successfully!\n");

        // Test 6: Get customer bookings
        System.out.println("Test 6: Get Customer Bookings");
        System.out.println("-----------------------------");
        List<Booking> bookings = gymCustomerService.getMyBookings(201);
        for (Booking book : bookings) {
            System.out.println("Booking ID: " + book.getBookingId() + ", Slot ID: " + book.getSlotId() 
                + ", Status: " + book.getStatus());
        }

        // Test 7: Cancel booking
        System.out.println("\nTest 7: Cancel Booking");
        System.out.println("----------------------");
        boolean cancelled = gymCustomerService.cancelBooking(501);
        System.out.println("Booking cancelled: " + cancelled);

        // Test 8: Delete customer
        System.out.println("\nTest 8: Delete Customer");
        System.out.println("------------------------");
        boolean deleted = gymCustomerService.deleteCustomer(201);
        System.out.println("Customer deleted: " + deleted);

        System.out.println("\n========== Demo Complete ==========");
    }
}
