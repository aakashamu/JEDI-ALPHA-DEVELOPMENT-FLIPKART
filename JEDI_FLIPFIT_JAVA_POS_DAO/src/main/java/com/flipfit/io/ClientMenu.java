package com.flipfit.io;

import com.flipfit.business.BookingService;
import com.flipfit.business.GymAdminService;
import com.flipfit.business.GymCustomerService;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.UserService;
import com.flipfit.business.WaitListService;
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

            int choice = 0;
            try {
                String choiceStr = scanner.next();
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

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
                    // Owner
                    ownerDashboard(scanner, email, password);
                    break;
                case 2:
                    customerDashboard(scanner, email, password);
                    break;
                case 1:
                    adminDashboard(scanner, email, password);
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
     * @param scanner  the scanner
     * @param email    the email
     * @param password the password
     */
    private static void customerDashboard(Scanner scanner, String email, String password) {
        GymCustomerService customerService = new GymCustomerService();
        boolean session = true;

        while (session) {
            System.out.println("\n--- Gym Customer Dashboard ---");
            System.out.println("1. View Gym Centres");
            System.out.println("2. View My Bookings");
            System.out.println("3. Book a Slot");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Edit Profile");
            System.out.println("6. View Profile");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try {
                String choiceStr = scanner.next();
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    customerService.viewCentres(email, password);
                    break;

                case 2:
                    customerService.viewBookedSlots(email, password);
                    break;

                case 3:
                    // Logic slightly modified to match new signature if needed, or keeping
                    // interactions
                    System.out.print("Enter Slot Availability ID: ");
                    try {
                        String sIdStr = scanner.next();
                        int sId = InputValidator.validateId(sIdStr, "Availability ID");
                        customerService.bookSlot(sId, email, password);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter Booking ID: ");
                    try {
                        String bIdStr = scanner.next();
                        int bId = InputValidator.validateId(bIdStr, "Booking ID");
                        customerService.cancelBooking(bId, email, password);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    scanner.nextLine();
                    System.out.print("Enter Full Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    long phone = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Enter City: ");
                    String city = scanner.nextLine();
                    System.out.print("Enter Pincode: ");
                    int pin = scanner.nextInt();
                    customerService.editDetails(name, email, phone, city, pin, password);
                    break;

                case 6:
                    customerService.viewProfile(email, password);
                    break;

                case 7:
                    System.out.println("\n✓ Logging out... Returning to main menu.");
                    session = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Owner Dashboard.
     *
     * @param scanner  the scanner
     * @param email    the email
     * @param password the password
     */
    private static void ownerDashboard(Scanner scanner, String email, String password) {
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

            int choice = 0;
            try {
                String choiceStr = scanner.next();
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Enter Centre Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter City: ");
                    String city = scanner.nextLine();
                    System.out.print("Enter Number of Slots: ");
                    try {
                        String slotsStr = scanner.next();
                        int slots = InputValidator.validateId(slotsStr, "Slots");

                        System.out.print("Enter Capacity per Slot: ");
                        String capacityStr = scanner.next();
                        int capacity = InputValidator.validateId(capacityStr, "Capacity");

                        ownerService.registerNewCentre(name, city, slots, capacity, email, password);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    ownerService.viewMyCentres(email, password);
                    break;

                case 3:
                    BookingService.bookingMenu(scanner);
                    break;

                case 4:
                    BookingService bookingService = new BookingService();
                    bookingService.viewAllBookings(); // Still global for now
                    break;

                case 5:
                    WaitListService.waitListMenu(scanner);
                    break;

                case 6:
                    WaitListService waitListService = new WaitListService();
                    waitListService.viewWaitList(); // Still global
                    break;

                case 7:
                    ownerService.viewMyCentres(email, password);
                    System.out.print("Enter Centre ID to setup slots: ");
                    try {
                        String cIdStr = scanner.next();
                        int cId = InputValidator.validateId(cIdStr, "Centre ID");

                        System.out.print("Enter Number of Slots: ");
                        String nSlotsStr = scanner.next();
                        int nSlots = InputValidator.validateId(nSlotsStr, "Slots");

                        System.out.print("Enter Capacity per Slot: ");
                        String capStr = scanner.next();
                        int cap = InputValidator.validateId(capStr, "Capacity");

                        ownerService.setupSlotsForExistingCentre(cId, nSlots, cap, email, password);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
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
     * @param scanner  the scanner
     * @param email    the email
     * @param password the password
     */
    private static void adminDashboard(Scanner scanner, String email, String password) {
        GymAdminService adminService = new GymAdminService();
        boolean adminSession = true;

        while (adminSession) {
            System.out.println("\n--- Gym Admin Dashboard ---");
            System.out.println("1. View All Gym Owners");
            System.out.println("2. View All Gym Centres");
            System.out.println("3. View Pending Owners");
            System.out.println("4. Approve Gym Owner");
            System.out.println("5. View Pending Centres");
            System.out.println("6. Approve Gym Centre");
            System.out.println("7. Delete Gym Owner");
            System.out.println("8. View Customer Metrics");
            System.out.println("9. View Gym Metrics");
            System.out.println("10. Logout");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try {
                String choiceStr = scanner.next();
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
                continue;
            }

            switch (choice) {
                case 1:
                    adminService.viewAllGymOwners(email, password);
                    break;

                case 2:
                    adminService.viewAllCentres(email, password);
                    break;

                case 3:
                    // viewPendingOwners assumed present in service or helper
                    // adminService.viewPendingOwners(); // If missing in interface update, omit or
                    // fix
                    System.out.println("Pending Owners View Not Implemented in Refactor (Use View All)");
                    break;

                case 4:
                    System.out.print("Enter Gym Owner ID to approve: ");
                    try {
                        String idStr = scanner.next();
                        int ownerId = InputValidator.validateId(idStr, "Owner ID");
                        adminService.validateGymOwner(ownerId, email, password);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("Pending Centres View Not Implemented in Refactor (Use View All)");
                    break;

                case 6:
                    System.out.print("Enter Gym Centre ID to approve: ");
                    try {
                        String cIdStr = scanner.next();
                        int centreId = InputValidator.validateId(cIdStr, "Centre ID");
                        adminService.approveCentre(centreId, email, password);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 7:
                    System.out.print("Enter Gym Owner ID to delete: ");
                    try {
                        String dIdStr = scanner.next();
                        int dOwnerId = InputValidator.validateId(dIdStr, "Owner ID");
                        adminService.deleteOwner(dOwnerId, email, password);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 8:
                    adminService.viewCustomerMetrics(email, password);
                    break;

                case 9:
                    adminService.viewGymMetrics(email, password);
                    break;

                case 10:
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
        try {
            System.out.print("Phone Number: ");
            String phoneStr = scanner.next();
            long phoneNumber = 0;
            try {
                phoneNumber = Long.parseLong(phoneStr);
            } catch (NumberFormatException e) {
                throw new Exception("Phone number must be a valid number");
            }
            if (String.valueOf(phoneNumber).length() != 10) {
                throw new Exception("Phone number must be 10 digits");
            }
            scanner.nextLine(); // consume newline

            System.out.print("City: ");
            String city = scanner.nextLine();
            System.out.print("State: ");
            String state = scanner.nextLine();
            System.out.print("Pincode: ");
            String pinStr = scanner.next();
            int pincode = 0;
            try {
                pincode = Integer.parseInt(pinStr);
            } catch (NumberFormatException e) {
                throw new Exception("Pincode must be a valid number");
            }
            if (String.valueOf(pincode).length() != 6) {
                throw new Exception("Pincode must be 6 digits");
            }
            scanner.nextLine(); // consume newline

            GymCustomerService customerService = new GymCustomerService();
            customerService.registerCustomer(fullName, email, password, phoneNumber, city, state, pincode);

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return;
        }
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

        try {
            System.out.print("Phone Number: ");
            String phoneStr = scanner.next();
            long phoneNumber = 0;
            try {
                phoneNumber = Long.parseLong(phoneStr);
            } catch (NumberFormatException e) {
                throw new Exception("Phone number must be a valid number");
            }
            if (String.valueOf(phoneNumber).length() != 10) {
                throw new Exception("Phone number must be 10 digits");
            }
            scanner.nextLine(); // consume newline

            System.out.print("City: ");
            String city = scanner.nextLine();
            System.out.print("State: ");
            String state = scanner.nextLine();
            System.out.print("Pincode: ");
            String pinStr = scanner.next();
            int pincode = 0;
            try {
                pincode = Integer.parseInt(pinStr);
            } catch (NumberFormatException e) {
                throw new Exception("Pincode must be a valid number");
            }
            if (String.valueOf(pincode).length() != 6) {
                throw new Exception("Pincode must be 6 digits");
            }
            scanner.nextLine(); // consume newline

            System.out.print("PAN Card Number: ");
            String panCard = scanner.nextLine();
            try {
                InputValidator.validatePan(panCard);
            } catch (InvalidCardDetailsException e) {
                System.out.println(e.getMessage());
                return;
            }

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
            if (gstin.length() != 15) {
                throw new Exception("GSTIN must be 15 characters long");
            }

            // Actually register the owner in the database
            GymOwnerService ownerService = new GymOwnerService();
            ownerService.registerOwner(fullName, email, password, phoneNumber, city, state, pincode, panCard,
                    aadhaarNumber, gstin);

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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

        try {
            System.out.print("Phone Number: ");
            String phoneStr = scanner.next();
            long phoneNumber = 0;
            try {
                phoneNumber = Long.parseLong(phoneStr);
            } catch (NumberFormatException e) {
                throw new Exception("Phone number must be a valid number");
            }
            if (String.valueOf(phoneNumber).length() != 10) {
                throw new Exception("Phone number must be 10 digits");
            }
            scanner.nextLine(); // consume newline

            System.out.print("City: ");
            String city = scanner.nextLine();
            System.out.print("State: ");
            String state = scanner.nextLine();
            System.out.print("Pincode: ");
            String pinStr = scanner.next();
            int pincode = 0;
            try {
                pincode = Integer.parseInt(pinStr);
            } catch (NumberFormatException e) {
                throw new Exception("Pincode must be a valid number");
            }
            if (String.valueOf(pincode).length() != 6) {
                throw new Exception("Pincode must be 6 digits");
            }
            scanner.nextLine(); // consume newline

            // Actually register the admin in the database
            GymAdminService adminService = new GymAdminService();
            adminService.registerAdmin(fullName, email, password, phoneNumber, city, state, pincode);

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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