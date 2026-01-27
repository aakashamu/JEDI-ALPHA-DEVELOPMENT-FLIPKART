package com.flipfit.client;

import com.flipfit.bean.GymOwner;
import com.flipfit.business.GymOwnerService; // Import the service
import java.util.Scanner;

public class FlipFitApplication {
	// 1. Declare the service at the class level so it's accessible
	private static GymOwnerService gymOwnerService = new GymOwnerService();

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		FlipFitApplication app = new FlipFitApplication();
		GymOwnerFlipfitMenu ownerMenu = new GymOwnerFlipfitMenu();

		while (true) {
			System.out.println("\n=== WELCOME TO FLIPFIT APPLICATION ===");
			System.out.println("1. Login ");
			System.out.println("2. Registration (Gym Owner)");
			System.out.println("3. Exit");

			int choice = sc.nextInt();
			switch (choice) {
				case 1:
					System.out.print("Enter your Owner ID: ");
					int loginId = sc.nextInt();

					// 2. This now works because gymOwnerService is defined
					if (gymOwnerService.isValidOwner(loginId)) {
						System.out.println("Login Successful!");
						ownerMenu.displayMenu(loginId);
					} else {
						System.out.println("Error: ID " + loginId + " not found. Please register first.");
					}
					break;

				case 2:
					// 3. Call the actual registration method you wrote below
					app.registerGymOwner();
					break;

				case 3:
					System.out.println("Exiting... Goodbye!");
					System.exit(0);
				default:
					System.out.println("Invalid choice.");
			}
		}
	}

	public void registerGymOwner() {
		Scanner sc = new Scanner(System.in);

		System.out.println("\n--- Gym Owner Registration ---");

		System.out.print("Enter User ID: ");
		int userId = sc.nextInt();
		System.out.print("Enter Full Name: ");
		sc.nextLine();
		String name = sc.nextLine();
		System.out.print("Enter Email: ");
		String email = sc.next();
		System.out.print("Enter Password: ");
		String password = sc.next();
		System.out.print("Enter Phone Number: ");
		long phone = sc.nextLong();
		System.out.print("Enter City: ");
		String city = sc.next();
		System.out.print("Enter Pincode: ");
		int pincode = sc.nextInt();

		System.out.print("Enter PAN: ");
		String pan = sc.next();
		System.out.print("Enter Aadhaar: ");
		String aadhaar = sc.next();
		System.out.print("Enter GSTIN: ");
		String gstin = sc.next();

		GymOwner newOwner = new GymOwner(
				userId, name, email, password, phone, city, pincode,
				pan, aadhaar, gstin
		);

		newOwner.setIsApproved(0);

		// 4. Send the created owner to the service list for storage
		gymOwnerService.register(newOwner);

		System.out.println("\nRegistration successful for " + name + "!");
		System.out.println("Status: PENDING ADMIN APPROVAL.");
	}
}