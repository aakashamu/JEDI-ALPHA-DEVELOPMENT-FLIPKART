package com.flipfit.io;

import com.flipfit.business.BookingService;
import com.flipfit.business.GymAdminService;
import com.flipfit.business.GymCustomerService;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.UserService;
import com.flipfit.business.WaitListService;
import com.flipfit.business.NotificationService;
import com.flipfit.exception.*;
import com.flipfit.utils.InputValidator;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * The Class ClientMenu.
 *
 * @author Ananya
 * @ClassName "ClientMenu"
 */
public class ClientMenu {
    /**
     * Main.
     *
     * @param args the args
     */
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

    /**
     * Handle Login.
     *
     * @param scanner the scanner
     */
    private static void handleLogin(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserService userService = new UserService();
        boolean loginSuccess = false;
        try {
            loginSuccess = userService.login(email, password);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (loginSuccess) {
            com.flipfit.bean.User user = UserService.getCurrentUser(email);
            if (user == null) {
                System.out.println("ERROR: Session error. Please try logging in again.");
                return;
            }

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            // Define a friendly format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formatDateTime = now.format(formatter);

            // Print Login Greeting with Username on left and Time on right
            System.out.println("\nWelcome " + user.getFullName() + "\t\t\t" + formatDateTime);

            int roleId = user.getRoleId();
            System.out.println("✓ Login Successful. Role detected: "
                    + (roleId == 1 ? "Admin" : (roleId == 2 ? "Customer" : "Owner")));

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

    /**
     * Customer Dashboard.
     *
     * @param scanner the scanner
     */
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
            // System.out.println("6. Booking Management");
            // System.out.println("7. Wait List Management");
            System.out.println("6. Logout");
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

                    System.out.println("Enter Availability ID to book (or 0 to go back): ");
                    String availIdStr = scanner.next();
                    try {
                        int availabilityId = InputValidator.validateId(availIdStr, "Availability ID");
                        if (availabilityId > 0) {
                            try {
                                customerService.bookSlot(availabilityId);
                                NotificationService notificationService = new NotificationService();
                                String currentUserEmail = UserService.getCurrentLoggedInUser();
                                com.flipfit.bean.User currentUser = com.flipfit.dao.FlipFitRepository.users
                                        .get(currentUserEmail);
                                if (currentUser != null) {
                                    notificationService.sendNotification(currentUser.getUserId(),
                                            "You have successfully booked a slot.");
                                }
                            } catch (SlotNotAvailableException | BookingNotDoneException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } catch (InvalidCentreIdException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter Booking ID to cancel: ");
                    int bookingIdToCancel = scanner.nextInt();
                    try {
                        customerService.cancelBooking(bookingIdToCancel);
                        NotificationService notificationService = new NotificationService();
                        String currentUserEmail = UserService.getCurrentLoggedInUser();
                        com.flipfit.bean.User currentUser = com.flipfit.dao.FlipFitRepository.users
                                .get(currentUserEmail);
                        if (currentUser != null) {
                            notificationService.sendNotification(currentUser.getUserId(),
                                    "You have cancelled your booking.");
                        }
                    } catch (BookingNotDoneException e) {
                        System.out.println(e.getMessage());
                    }
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

                // case 6:
                // BookingService.bookingMenu(scanner);
                // break;

                // case 7:
                // WaitListService.waitListMenu(scanner);
                // break;

                case 6:
                    System.out.println("\n✓ Logging out... Returning to main menu.");
                    session = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Owner Dashboard.
     *
     * @param scanner the scanner
     */
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

    /**
     * Admin Dashboard.
     *
     * @param scanner the scanner
     */
    private static void adminDashboard(Scanner scanner) {
        GymAdminService adminService = new GymAdminService();
        boolean adminSession = true;

        while (adminSession) {
            System.out.println("\n--- Gym Admin Dashboard ---");
            System.out.println("1. View Gym Owners");
            System.out.println("2. View Gym Centres");
            System.out.println("3. Approve Pending Gym Owner");
            System.out.println("4. Approve Pending Gym Centre");
            System.out.println("5. Delete Gym Owner");
            System.out.println("6. View Customer Metrics");
            System.out.println("7. View Gym Metrics");
            System.out.println("8. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    adminService.viewAllGymOwners();
                    break;

                case 2:
                    adminService.viewAllCentres();
                    break;

                case 3:
                    adminService.viewPendingOwners();
                    System.out.print("Enter Gym Owner ID to Approve: ");
                    int ownerId = scanner.nextInt();
                    adminService.approveOwner(ownerId);
                    break;

                case 4:
                    adminService.viewPendingCentres();
                    System.out.print("Enter Centre ID to approve: ");
                    String centreIdStr = scanner.next();
                    try {
                        int centreId = InputValidator.validateId(centreIdStr, "Centre ID");
                        adminService.approveCentre(centreId);
                    } catch (InvalidCentreIdException e) {
                        System.out.println(e.getMessage());
                    } catch (IssueWithApprovalException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    adminService.viewAllGymOwners();
                    System.out.print("Enter Gym Owner ID to delete: ");
                    String delIdStr = scanner.next();
                    try {
                        int deleteOwnerId = InputValidator.validateId(delIdStr, "Owner ID");
                        adminService.deleteOwner(deleteOwnerId);
                    } catch (InvalidCentreIdException e) {
                        System.out.println(e.getMessage());
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    adminService.viewCustomerMetrics();
                    break;

                case 7:
                    adminService.viewGymMetrics();
                    break;

                case 8:
                    System.out.println("\n✓ Logging out... Returning to main menu.");
                    adminSession = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handle Customer Registration.
     *
     * @param scanner the scanner
     */
    private static void handleCustomerRegistration(Scanner scanner) {
        scanner.nextLine(); // consume leftover newline
        System.out.println("\n--- Gym Customer Registration ---");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        try {
            InputValidator.validateEmail(email);
        } catch (InvalidEmailException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            InputValidator.validatePassword(password);
        } catch (WeakPasswordException e) {
            System.out.println(e.getMessage());
            return;
        }
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

    /**
     * Handle Owner Registration.
     *
     * @param scanner the scanner
     */
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
        try {
            InputValidator.validateCard(aadhaarNumber, "Aadhaar");
        } catch (InvalidCardDetailsException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("GSTIN: ");
        String gstin = scanner.nextLine();

        // Actually register the owner in the database
        GymOwnerService ownerService = new GymOwnerService();
        ownerService.registerOwner(fullName, email, password, phoneNumber, city, state, pincode, panCard, aadhaarNumber,
                gstin);
    }

    /**
     * Handle Admin Registration.
     *
     * @param scanner the scanner
     */
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

    /**
     * Handle Change Password.
     *
     * @param scanner the scanner
     */
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