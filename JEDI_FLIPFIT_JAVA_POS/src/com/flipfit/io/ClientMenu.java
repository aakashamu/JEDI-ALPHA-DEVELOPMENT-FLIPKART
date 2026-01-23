package com.flipfit.io;

import java.util.Scanner;

public class ClientMenu {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int choice;

		System.out.println("Welcome to the Flipfit Application for GYM");
		System.out.println("1. Login");
		System.out.println("2. Registration of the GymCustomer");
		System.out.println("3. Registration of the GymOwner");
		System.out.println("4. Change Password");
		System.out.println("5. Exit");
		System.out.print("Enter your choice: ");

		choice = scanner.nextInt();

		switch (choice) {
		case 1:
			handleLogin(scanner);
			break;
		case 2:
			System.out.println("Redirecting to GymCustomer Registration...");
			break;
		case 3:
			System.out.println("Redirecting to GymOwner Registration...");
			break;
		case 4:
			System.out.println("Redirecting to Password Change...");
			break;
		case 5:
			System.out.println("Exiting Application. Goodbye!");
			break;
		default:
			System.out.println("Invalid choice. Please try again.");
		}

		scanner.close();
	}

	private static void handleLogin(Scanner scanner) {
	    scanner.nextLine(); // Clear the buffer after scanner.nextInt() from main menu
	    
	    System.out.print("Username: ");
	    String username = scanner.nextLine(); // Use nextLine to support spaces
	    
	    System.out.print("Password: ");
	    String password = scanner.nextLine(); 
	    
	    System.out.println("Role: 1. GYM Owner 2. Gym Customer 3. Gym Admin");
	    System.out.print("Enter Role Choice: ");
	    int role = scanner.nextInt();

	    System.out.println("Login Successful for " + username + " as Role " + role);
	}
}