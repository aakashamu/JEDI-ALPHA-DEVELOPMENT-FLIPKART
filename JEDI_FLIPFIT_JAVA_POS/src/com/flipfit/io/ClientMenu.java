package com.flipfit.io;

import java.util.Scanner;

import com.flipfit.bean.GymCentre;
import com.flipfit.business.GymCustomerService;

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
		System.out.print("Username: ");
		String username = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();

		System.out.println("Role: 1. GYM Owner 2. Gym Customer 3. Gym Admin");
		System.out.print("Enter Role Choice: ");
		int role = scanner.nextInt();

		System.out.println("Login Successful for " + username + " as Role " + role);

		if (role == 2) {
			customerDashboard(scanner);
		}
	}

	private static void customerDashboard(Scanner scanner) {
		GymCustomerService customerService = new GymCustomerService();
		boolean session = true;

		while (session) {
			System.out.println("\n--- Customer Dashboard ---");
			System.out.println("1. View Gym Centres\n2. View Bookings\n3. Book Slot\n4. Cancel Booking\n5. Edit Profile\n6. Logout");
			System.out.print("Choice: ");
			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				customerService.viewCentres().forEach(g -> System.out.println("ID: " + g.getCentreId() + " | " + g.getName()));
				break;
			case 2:
				customerService.viewBookedSlots().forEach(b -> System.out.println("Booking ID: " + b.getBookingId() + " Status: " + b.getStatus()));
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
			    scanner.nextLine(); // Clear the buffer after nextInt()
			    System.out.println("\n--- Edit Your Profile ---");
			    
			    // Email is the key used to find the user in the Map
			    System.out.print("Confirm your Email (unique ID): ");
			    String confirmEmail = scanner.next();
			    scanner.nextLine(); // Clear buffer for name input
			    
			    System.out.print("Enter New Full Name: ");
			    String newName = scanner.nextLine();
			    
			    System.out.print("Enter New Phone Number: ");
			    long newPhone = scanner.nextLong();
			    scanner.nextLine(); // Clear buffer for city input
			    
			    System.out.print("Enter New City: ");
			    String newCity = scanner.nextLine();
			    
			    System.out.print("Enter New Pincode: ");
			    int newPin = scanner.nextInt();

			    // Call the service method with all collected data
			    customerService.editDetails(newName, confirmEmail, newPhone, newCity, newPin);
			    break;
			case 6:
				session = false;
				break;
			}
		}
	}

	private static void handleCustomerRegistration(Scanner scanner) {
		scanner.nextLine();
		System.out.println("\n--- Gym Admin Registration ---");

		System.out.print("Full Name: ");
		String name = scanner.nextLine();

		System.out.print("Email: ");
		String email = scanner.next();

		System.out.print("Password: ");
		String pass = scanner.next();

		System.out.print("Phone Number: ");
		long phone = scanner.nextLong();

		System.out.print("City: ");
		String city = scanner.next();

		System.out.print("Pincode: ");
		int pincode = scanner.nextInt();

		System.out.println("Customer account created for: " + name);
	}

	private static void handleOwnerRegistration(Scanner scanner) {
		scanner.nextLine();
		System.out.println("\n--- Gym Owner Registration ---");

		System.out.print("Full Name: ");
		String name = scanner.nextLine();

		System.out.print("Email: ");
		String email = scanner.next();

		System.out.print("Password: ");
		String pass = scanner.next();

		System.out.print("Phone Number: ");
		long phone = scanner.nextLong();

		System.out.print("City: ");
		String city = scanner.next();

		System.out.print("Pincode: ");
		int pincode = scanner.nextInt();

		System.out.println("Admin account created for: " + name);
	}

	private static void handleAdminRegistration(Scanner scanner) {
		scanner.nextLine();
		System.out.println("\n--- Gym Admin Registration ---");

		System.out.print("Full Name: ");
		String name = scanner.nextLine();

		System.out.print("Email: ");
		String email = scanner.next();

		System.out.print("Password: ");
		String pass = scanner.next();

		System.out.print("Phone Number: ");
		long phone = scanner.nextLong();

		System.out.print("City: ");
		String city = scanner.next();

		System.out.print("Pincode: ");
		int pincode = scanner.nextInt();

		System.out.println("Admin account created for: " + name);
	}

	private static void handleChangePassword(Scanner scanner) {
		scanner.nextLine();
		System.out.print("Enter Username: ");
		String user = scanner.nextLine();
		System.out.print("Enter Old Password: ");
		String oldPass = scanner.nextLine();
		System.out.print("Enter New Password: ");
		String newPass = scanner.nextLine();
		System.out.println("Password updated successfully.");
	}
}