package com.flipfit.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * REST API Test Client for FlipFit Application.
 * Tests all endpoints: Registration, Setup, Usage, and Verification phases.
 *
 * @author Ananya
 * @ClassName "RESTAPITestClient"
 */
public class RESTAPITestClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper mapper = new ObjectMapper();

    // Test credentials
    private static String adminId = null;
    private static String ownerId = null;
    private static String customerId = null;
    private static String centerId = null;
    private static String slotAvailabilityId = null;
    private static String bookingId = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   FlipFit REST API Test Client                ║");
        System.out.println("║   Make sure server is running on :8080        ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        while (running) {
            displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    runFullTestSequence();
                    break;
                case 2:
                    runRegistrationPhase();
                    break;
                case 3:
                    runSetupPhase();
                    break;
                case 4:
                    runUsagePhase();
                    break;
                case 5:
                    runVerificationPhase();
                    break;
                case 6:
                    running = false;
                    System.out.println("\nThank you for testing FlipFit! Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n========== FlipFit REST API Test Menu ==========");
        System.out.println("1. Run Full Test Sequence (All Phases)");
        System.out.println("2. Registration Phase Only");
        System.out.println("3. Setup Phase Only");
        System.out.println("4. Usage Phase Only");
        System.out.println("5. Verification Phase Only");
        System.out.println("6. Exit");
        System.out.println("===============================================");
    }

    private static void runFullTestSequence() {
        System.out.println("\n\n╔════════════════════════════════════════════════╗");
        System.out.println("║   Running Full Test Sequence - All Phases      ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        runRegistrationPhase();
        System.out.println("\n[Waiting 2 seconds...]\n");
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        runSetupPhase();
        System.out.println("\n[Waiting 2 seconds...]\n");
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        runUsagePhase();
        System.out.println("\n[Waiting 2 seconds...]\n");
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        runVerificationPhase();

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║   Full Test Sequence Completed!                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    // ========== PHASE 1: REGISTRATION ==========
    private static void runRegistrationPhase() {
        System.out.println("\n┌─── Phase 1: REGISTRATION ───┐\n");

        // A. Register Admin
        System.out.println("A. Registering Admin...");
        String adminJson = """
                {
                  "email": "admin@test.com",
                  "password": "PassP@123",
                  "fullName": "Admin User",
                  "phoneNumber": 9999999999,
                  "city": "Bangalore",
                  "state": "Karnataka",
                  "pincode": 560001
                }
                """;
        String adminResponse = makeRequest("POST", "/admin/register", adminJson);
        System.out.println("Response: " + adminResponse + "\n");

        // B. Register Gym Owner
        System.out.println("B. Registering Gym Owner...");
        String ownerJson = """
                {
                  "email": "owner@gym.com",
                  "password": "PassP@123",
                  "fullName": "Gym Owner",
                  "phoneNumber": 8888888888,
                  "city": "Mumbai",
                  "state": "Maharashtra",
                  "pincode": 400001,
                  "pan": "ABCDE1234F",
                  "aadhaar": "123456789012",
                  "gstin": "29ABCDE1234F1Z5"
                }
                """;
        String ownerResponse = makeRequest("POST", "/owner/register", ownerJson);
        System.out.println("Response: " + ownerResponse + "\n");

        // C. Register Customer
        System.out.println("C. Registering Customer...");
        String customerJson = """
                {
                  "email": "cust@user.com",
                  "password": "PassP@123",
                  "fullName": "John Doe",
                  "phoneNumber": 7777777777,
                  "city": "Delhi",
                  "state": "Delhi",
                  "pincode": 110001
                }
                """;
        String customerResponse = makeRequest("POST", "/customer/register", customerJson);
        System.out.println("Response: " + customerResponse + "\n");

        System.out.println("✓ Registration Phase Complete!\n");
    }

    // ========== PHASE 2: SETUP ==========
    private static void runSetupPhase() {
        System.out.println("\n┌─── Phase 2: SETUP (Owner & Admin) ───┐\n");

        // D. Add Gym Centre (By Owner)
        System.out.println("D. Adding Gym Centre (by Owner)...");
        String centreJson = """
                {
                  "name": "Gold's Gym Indiranagar",
                  "city": "Bangalore",
                  "state": "Karnataka",
                  "pincode": 560038
                }
                """;
        String centreResponse = makeRequest("POST", 
            "/owner/center?email=owner@gym.com&password=PassP@123", centreJson);
        System.out.println("Response: " + centreResponse + "\n");

        // E. View Pending Owners (By Admin)
        System.out.println("E. Viewing Pending Owners (by Admin)...");
        String ownersResponse = makeRequest("GET", 
            "/admin/owners?email=admin@test.com&password=PassP@123", null);
        System.out.println("Response: " + ownersResponse + "\n");
        // Extract ownerId from response if available
        ownerId = "1"; // Default assumption

        // F. Approve Owner (By Admin)
        System.out.println("F. Approving Owner (by Admin)...");
        String approveOwnerResponse = makeRequest("PUT", 
            "/admin/owner/approve/" + ownerId + "?email=admin@test.com&password=PassP@123", null);
        System.out.println("Response: " + approveOwnerResponse + "\n");

        // G. Approve Centre (By Admin)
        System.out.println("G. Approving Centre (by Admin)...");
        centerId = "1"; // Default assumption
        String approveCentreResponse = makeRequest("PUT", 
            "/admin/center/approve/" + centerId + "?email=admin@test.com&password=PassP@123", null);
        System.out.println("Response: " + approveCentreResponse + "\n");

        // H. Add Slots (By Owner)
        System.out.println("H. Adding Slots (by Owner)...");
        String slotsResponse = makeRequest("POST", 
            "/owner/slot?centreId=" + centerId + "&numSlots=5&capacity=10&email=owner@gym.com&password=PassP@123", null);
        System.out.println("Response: " + slotsResponse + "\n");

        System.out.println("✓ Setup Phase Complete!\n");
    }

    // ========== PHASE 3: USAGE ==========
    private static void runUsagePhase() {
        System.out.println("\n┌─── Phase 3: USAGE (Customer) ───┐\n");

        // I. View Available Centres (By Customer)
        System.out.println("I. Viewing Available Centres (by Customer)...");
        String centresResponse = makeRequest("GET", 
            "/customer/centers?email=cust@user.com&password=PassP@123", null);
        System.out.println("Response: " + centresResponse + "\n");

        // J. View Slots in a Centre (By Customer)
        System.out.println("J. Viewing Slots (by Customer)...");
        String slotsResponse = makeRequest("GET", 
            "/customer/slots/" + centerId, null);
        System.out.println("Response: " + slotsResponse + "\n");
        slotAvailabilityId = "1"; // Default assumption

        // K. Book a Slot (By Customer)
        System.out.println("K. Booking a Slot (by Customer)...");
        String bookResponse = makeRequest("POST", 
            "/customer/book?slotId=" + slotAvailabilityId + "&email=cust@user.com&password=PassP@123", null);
        System.out.println("Response: " + bookResponse + "\n");
        bookingId = "1"; // Default assumption

        // L. View My Bookings (By Customer)
        System.out.println("L. Viewing My Bookings (by Customer)...");
        String bookingsResponse = makeRequest("GET", 
            "/customer/bookings?email=cust@user.com&password=PassP@123", null);
        System.out.println("Response: " + bookingsResponse + "\n");

        System.out.println("✓ Usage Phase Complete!\n");
    }

    // ========== PHASE 4: VERIFICATION ==========
    private static void runVerificationPhase() {
        System.out.println("\n┌─── Phase 4: VERIFICATION ───┐\n");

        // M. Cancel Booking (By Customer)
        System.out.println("M. Canceling Booking (by Customer)...");
        String cancelResponse = makeRequest("DELETE", 
            "/customer/cancel/" + bookingId + "?email=cust@user.com&password=PassP@123", null);
        System.out.println("Response: " + cancelResponse + "\n");

        // N. View Metrics (By Admin)
        System.out.println("N. Viewing Metrics (by Admin)...");
        String metricsResponse = makeRequest("GET", 
            "/admin/metrics/customer?email=admin@test.com&password=PassP@123", null);
        System.out.println("Response: " + metricsResponse + "\n");

        System.out.println("✓ Verification Phase Complete!\n");
    }

    // ========== HTTP REQUEST UTILITY ==========
    private static String makeRequest(String method, String endpoint, String body) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (body != null && !body.isEmpty()) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes("utf-8"));
                    os.flush();
                }
            }

            int responseCode = conn.getResponseCode();
            String result;

            if (responseCode >= 200 && responseCode < 300) {
                Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
                result = scanner.hasNext() ? scanner.next() : "Success (No Content)";
                scanner.close();
            } else {
                Scanner scanner = new Scanner(conn.getErrorStream()).useDelimiter("\\A");
                result = "Error " + responseCode + ": " + (scanner.hasNext() ? scanner.next() : "Unknown");
                scanner.close();
            }

            conn.disconnect();
            return result;

        } catch (Exception e) {
            return "Connection Error: " + e.getMessage() + "\n(Is server running on :8080?)";
        }
    }
}
