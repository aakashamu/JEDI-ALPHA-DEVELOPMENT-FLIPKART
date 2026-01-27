package com.flipfit.io;

import com.flipfit.business.BookingService;
import com.flipfit.business.GymAdminService;
import com.flipfit.business.GymCustomerService;
import com.flipfit.business.UserService;
import com.flipfit.business.WaitListService;
import java.util.Scanner;

public class ClientMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("\nWelcome to the Flipfit Application for GYM");
        System.out.println("1. Login");
        System.out.println("2. Registration of the GymCustomer");
        System.out.println("3. Registration of the GymOwner");
        System.out.println("4. Registration of the GymAdmin");
        System.out.println("5. Change Password");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");

        choice = scanner.nextInt();

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
                System.out.println("Exiting Application. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
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
            System.out.println("Login Successful for " + email);

            System.out.println("Role: 1. GYM Owner 2. Gym Customer 3. Gym Admin");
            System.out.print("Enter Role Choice: ");
            int role = scanner.nextInt();

            switch (role) {
                case 1:
                    ownerDashboard(scanner);
                    break;
                case 2:
                    customerDashboard(scanner);
                    break;
                case 3:
                    adminDashboard(scanner);
                    break;
                default:
                    System.out.println("Invalid role choice.");
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
                    customerService.viewCentres()
                            .forEach(g -> System.out.println("ID: " + g.getCentreId() + " | " + g.getName()));
                    break;

                case 2:
                    customerService.viewBookedSlots()
                            .forEach(b -> System.out.println("Booking ID: " + b.getBookingId() +
                                    " Status: " + b.getStatus()));
                    break;

                case 3:
                    System.out.print("Enter Availability ID: ");
                    customerService.bookSlot(scanner.nextInt());
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
        boolean ownerSession = true;

        while (ownerSession) {
            System.out.println("\n--- Gym Owner Dashboard ---");
            System.out.println("1. Booking Management");
            System.out.println("2. View All Bookings");
            System.out.println("3. Wait List Management");
            System.out.println("4. View All Wait List");
            System.out.println("5. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    BookingService.bookingMenu(scanner);
                    break;

                case 2:
                    BookingService bookingService = new BookingService();
                    bookingService.viewAllBookings();
                    break;

                case 3:
                    WaitListService.waitListMenu(scanner);
                    break;

                case 4:
                    WaitListService waitListService = new WaitListService();
                    waitListService.viewWaitList();
                    break;

                case 5:
                    ownerSession = false;
                    System.out.println("Exiting Owner Dashboard...");
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
                    System.out.print("Enter Gym Owner ID to validate: ");
                    int ownerId = scanner.nextInt();
                    System.out.println(adminService.validateGymOwner(ownerId)
                            ? "Gym Owner validation successful!"
                            : "Gym Owner validation failed!");
                    break;

                case 2:
                    System.out.print("Enter Centre ID to approve: ");
                    int centreId = scanner.nextInt();
                    System.out.println(adminService.approveCentre(centreId)
                            ? "Centre approval successful!"
                            : "Centre approval failed!");
                    break;

                case 3:
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
                    adminSession = false;
                    System.out.println("Exiting Admin Dashboard...");
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
        scanner.nextLine();
        System.out.println("\n--- Gym Customer Registration ---");

        System.out.print("Full Name: ");
        scanner.nextLine();
        System.out.print("Email: ");
        scanner.next();
        System.out.print("Password: ");
        scanner.next();
        System.out.print("Phone Number: ");
        scanner.nextLong();
        System.out.print("City: ");
        scanner.next();
        System.out.print("Pincode: ");
        scanner.nextInt();

        System.out.println("Customer account created successfully.");
    }

    private static void handleOwnerRegistration(Scanner scanner) {
        scanner.nextLine();
        System.out.println("\n--- Gym Owner Registration ---");

        System.out.print("Full Name: ");
        scanner.nextLine();
        System.out.print("Email: ");
        scanner.next();
        System.out.print("Password: ");
        scanner.next();
        System.out.print("Phone Number: ");
        scanner.nextLong();
        System.out.print("City: ");
        scanner.next();
        System.out.print("Pincode: ");
        scanner.nextInt();

        System.out.println("Owner account created successfully.");
    }

    private static void handleAdminRegistration(Scanner scanner) {
        scanner.nextLine();
        System.out.println("\n--- Gym Admin Registration ---");

        System.out.print("Full Name: ");
        scanner.nextLine();
        System.out.print("Email: ");
        scanner.next();
        System.out.print("Password: ");
        scanner.next();
        System.out.print("Phone Number: ");
        scanner.nextLong();
        System.out.print("City: ");
        scanner.next();
        System.out.print("Pincode: ");
        scanner.nextInt();

        System.out.println("Admin account created successfully.");
    }

    private static void handleChangePassword(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter Username: ");
        scanner.nextLine();
        System.out.print("Enter Old Password: ");
        scanner.nextLine();
        System.out.print("Enter New Password: ");
        scanner.nextLine();
        System.out.println("Password updated successfully.");
    }
}