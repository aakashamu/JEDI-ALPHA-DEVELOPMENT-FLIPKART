package com.flipfit.io;

import com.flipfit.business.BookingService;
import com.flipfit.business.GymAdminService;
import com.flipfit.business.GymCustomerService;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.UserService;
import com.flipfit.business.WaitListService;
import java.util.Scanner;

public class ClientMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println("Welcome to the Flipfit Application for GYM");
            System.out.println("========================================");
            System.out.println("1. Login");
            System.out.println("2. Registration of the GymCustomer");
            System.out.println("3. Registration of the GymOwner");
            System.out.println("4. Registration of the GymAdmin");
            System.out.println("5. Change Password");
            System.out.println("6. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleLogin(scanner);
                    break;
                case 2:
                    handleCustomerRegistration(scanner);
                    break;
                case 3:
                    handleOwnerRegistration(scanner);
                    break;
                case 4:
                    handleAdminRegistration(scanner);
                    break;
                case 5:
                    handleChangePassword(scanner);
                    break;
                case 6:
                    System.out.println("\n✓ Exiting Application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n✗ Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void handleLogin(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserService userService = new UserService();
        boolean loginSuccess = userService.login(email, password);

        if (loginSuccess) {
            com.flipfit.bean.User user = UserService.getCurrentUser(email);
            if (user == null) {
                System.out.println("ERROR: Session error. Please try logging in again.");
                return;
            }
            
            int roleId = user.getRoleId();
            System.out.println("✓ Login Successful. Role detected: " + (roleId == 1 ? "Admin" : (roleId == 2 ? "Customer" : "Owner")));
            
            // DB Roles: 1 = Admin, 2 = Customer, 3 = Owner
            switch (roleId) {
                case 3:
                    ownerDashboard(scanner);
                    break;
                case 2:
                    customerDashboard(scanner);
                    break;
                case 1:
                    adminDashboard(scanner);
                    break;
                default:
                    System.out.println("ERROR: Unknown role ID: " + roleId + ". Contact administration.");
            }
        } else {
            System.out.println("Login Failed. Invalid email or password.");
        }
    }

    // -------------------------------
    // Customer Dashboard
    // -------------------------------
    private static void customerDashboard(Scanner scanner) {
        GymCustomerService customerService = new GymCustomerService();
        boolean session = true;

        while (session) {
            System.out.println("\n--- Customer Dashboard ---");
            System.out.println("1. View Gym Centres");
            System.out.println("2. View Bookings");
            System.out.println("3. Book Slot");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Edit Profile");
            System.out.println("6. Booking Management");
            System.out.println("7. Wait List Management");
            System.out.println("8. Logout");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    customerService.viewCentres();
                    break;

                case 2:
                    customerService.viewBookedSlots();
                    break;

                case 3:
                    customerService.viewCentres();
                    System.out.print("Enter Centre ID to see available slots: ");
                    int centreId = scanner.nextInt();
                    customerService.viewAvailableSlots(centreId);
                    
                    System.out.print("Enter Availability ID to book (or 0 to go back): ");
                    int availabilityId = scanner.nextInt();
                    if (availabilityId > 0) {
                        customerService.bookSlot(availabilityId);
                    }
                    break;

                case 4:
                    System.out.print("Enter Booking ID to cancel: ");
                    customerService.cancelBooking(scanner.nextInt());
                    break;

                case 5:
                    scanner.nextLine();
                    System.out.println("\n--- Edit Your Profile ---");

                    System.out.print("Confirm your Email (unique ID): ");
                    String confirmEmail = scanner.next();
                    scanner.nextLine();

                    System.out.print("Enter New Full Name: ");
                    String newName = scanner.nextLine();

                    System.out.print("Enter New Phone Number: ");
                    long newPhone = scanner.nextLong();
                    scanner.nextLine();

                    System.out.print("Enter New City: ");
                    String newCity = scanner.nextLine();

                    System.out.print("Enter New Pincode: ");
                    int newPin = scanner.nextInt();

                    customerService.editDetails(newName, confirmEmail, newPhone, newCity, newPin);
                    break;

                case 6:
                    BookingService.bookingMenu(scanner);
                    break;

                case 7:
                    WaitListService.waitListMenu(scanner);
                    break;

                case 8:
                    System.out.println("\n✓ Logging out... Returning to main menu.");
                    session = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // -------------------------------
    // Owner Dashboard
    // -------------------------------
    private static void ownerDashboard(Scanner scanner) {
        GymOwnerService ownerService = new GymOwnerService();
        boolean ownerSession = true;

        while (ownerSession) {
            System.out.println("\n--- Gym Owner Dashboard ---");
            System.out.println("1. Add Gym Centre");
            System.out.println("2. View All My Centres");
            System.out.println("3. Booking Management");
            System.out.println("4. View All Bookings");
            System.out.println("5. Wait List Management");
            System.out.println("6. View All Wait List");
            System.out.println("7. Setup Slots for Existing Centre");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Centre Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter City: ");
                    String city = scanner.nextLine();
                    System.out.print("Enter Number of Slots: ");
                    int slots = scanner.nextInt();
                    System.out.print("Enter Capacity per Slot: ");
                    int capacity = scanner.nextInt();
                    
                    ownerService.registerNewCentre(name, city, slots, capacity);
                    break;

                case 2:
                    ownerService.viewMyCentres();
                    break;

                case 3:
                    BookingService.bookingMenu(scanner);
                    break;

                case 4:
                    BookingService bookingService = new BookingService();
                    bookingService.viewAllBookings();
                    break;

                case 5:
                    WaitListService.waitListMenu(scanner);
                    break;

                case 6:
                    WaitListService waitListService = new WaitListService();
                    waitListService.viewWaitList();
                    break;

                case 7:
                    ownerService.viewMyCentres();
                    System.out.print("Enter Centre ID to setup slots: ");
                    int cId = scanner.nextInt();
                    System.out.print("Enter Number of Slots: ");
                    int nSlots = scanner.nextInt();
                    System.out.print("Enter Capacity per Slot: ");
                    int cap = scanner.nextInt();
                    ownerService.setupSlotsForExistingCentre(cId, nSlots, cap);
                    break;

                case 8:
                    System.out.println("\n✓ Logging out... Returning to main menu.");
                    ownerSession = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // -------------------------------
    // Admin Dashboard
    // -------------------------------
    private static void adminDashboard(Scanner scanner) {
        GymAdminService adminService = new GymAdminService();
        boolean adminSession = true;

        while (adminSession) {
            System.out.println("\n--- Gym Admin Dashboard ---");
            System.out.println("1. Validate Gym Owner");
            System.out.println("2. Approve Gym Centre");
            System.out.println("3. Delete Gym Owner");
            System.out.println("4. View Customer Metrics");
            System.out.println("5. View Gym Metrics");
            System.out.println("6. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    adminService.viewAllGymOwners();
                    System.out.print("Enter Gym Owner ID to validate: ");
                    int ownerId = scanner.nextInt();
                    System.out.println(adminService.validateGymOwner(ownerId)
                            ? "Gym Owner validation successful!"
                            : "Gym Owner validation failed!");
                    break;

                case 2:
                    adminService.viewAllCentres();
                    System.out.print("Enter Centre ID to approve: ");
                    int centreId = scanner.nextInt();
                    System.out.println(adminService.approveCentre(centreId)
                            ? "Centre approval successful!"
                            : "Centre approval failed!");
                    break;

                case 3:
                    adminService.viewAllGymOwners();
                    System.out.print("Enter Gym Owner ID to delete: ");
                    int deleteOwnerId = scanner.nextInt();
                    System.out.println(adminService.deleteOwner(deleteOwnerId)
                            ? "Gym Owner deletion successful!"
                            : "Gym Owner deletion failed!");
                    break;

                case 4:
                    adminService.viewCustomerMetrics();
                    break;

                case 5:
                    adminService.viewGymMetrics();
                    break;

                case 6:
                    System.out.println("\n✓ Logging out... Returning to main menu.");
                    adminSession = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // -------------------------------
    // Registration / Password
    // -------------------------------
    private static void handleCustomerRegistration(Scanner scanner) {
        scanner.nextLine(); // consume leftover newline
        System.out.println("\n--- Gym Customer Registration ---");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Phone Number: ");
        long phoneNumber = scanner.nextLong();
        scanner.nextLine(); // consume newline
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("State: ");
        String state = scanner.nextLine();
        System.out.print("Pincode: ");
        int pincode = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Actually register the customer in the database
        GymCustomerService customerService = new GymCustomerService();
        customerService.registerCustomer(fullName, email, password, phoneNumber, city, state, pincode);
    }

    private static void handleOwnerRegistration(Scanner scanner) {
        scanner.nextLine(); // consume leftover newline
        System.out.println("\n--- Gym Owner Registration ---");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Phone Number: ");
        long phoneNumber = scanner.nextLong();
        scanner.nextLine(); // consume newline
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("State: ");
        String state = scanner.nextLine();
        System.out.print("Pincode: ");
        int pincode = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("PAN Card Number: ");
        String panCard = scanner.nextLine();
        System.out.print("Aadhaar Number: ");
        String aadhaarNumber = scanner.nextLine();
        System.out.print("GSTIN: ");
        String gstin = scanner.nextLine();

        // Actually register the owner in the database
        GymOwnerService ownerService = new GymOwnerService();
        ownerService.registerOwner(fullName, email, password, phoneNumber, city, state, pincode, panCard, aadhaarNumber, gstin);
    }

    private static void handleAdminRegistration(Scanner scanner) {
        scanner.nextLine(); // consume leftover newline
        System.out.println("\n--- Gym Admin Registration ---");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Phone Number: ");
        long phoneNumber = scanner.nextLong();
        scanner.nextLine(); // consume newline
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("State: ");
        String state = scanner.nextLine();
        System.out.print("Pincode: ");
        int pincode = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Actually register the admin in the database
        GymAdminService adminService = new GymAdminService();
        adminService.registerAdmin(fullName, email, password, phoneNumber, city, state, pincode);
    }

    private static void handleChangePassword(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Old Password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();
        
        UserService userService = new UserService();
        if (userService.updatePassword(email, oldPassword, newPassword)) {
            System.out.println("✓ Password updated successfully.");
        } else {
            System.out.println("❌ Failed to update password. Please check your email and old password.");
        }
    }
}