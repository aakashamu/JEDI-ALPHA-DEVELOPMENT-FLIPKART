package com.flipfit.io;

import com.flipfit.business.GymAdminService;
import com.flipfit.business.GymCustomerService;
import com.flipfit.business.UserService;
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

		// Use UserService for authentication
		UserService userService = new UserService();
		boolean loginSuccess = userService.login(email, password);

		if (loginSuccess) {
			System.out.println("Login Successful for " + email);
			
			// Show role selection after successful login
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
	
	private static void ownerDashboard(Scanner scanner) {
		System.out.println("\n--- Owner Dashboard ---");
		System.out.println("Owner dashboard functionality not yet implemented.");
		// TODO: Implement owner dashboard features
	}
	
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
					boolean validated = adminService.validateGymOwner(ownerId);
					if (validated) {
						System.out.println("Gym Owner validation successful!");
					} else {
						System.out.println("Gym Owner validation failed!");
					}
					break;
					
				case 2:
					System.out.print("Enter Centre ID to approve: ");
					int centreId = scanner.nextInt();
					boolean approved = adminService.approveCentre(centreId);
					if (approved) {
						System.out.println("Centre approval successful!");
					} else {
						System.out.println("Centre approval failed!");
					}
					break;
					
				case 3:
					System.out.print("Enter Gym Owner ID to delete: ");
					int deleteOwnerId = scanner.nextInt();
					boolean deleted = adminService.deleteOwner(deleteOwnerId);
					if (deleted) {
						System.out.println("Gym Owner deletion successful!");
					} else {
						System.out.println("Gym Owner deletion failed!");
					}
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
}