package com.flipfit.client;

import java.util.Scanner;

/**
 * The Class FlipFitApplication.
 * Main entry point for the FlipFit application.
 *
 * @author Ananya
 * @ClassName  "FlipFitApplication"
 */
public class FlipFitApplication {

    /**
     * Main method - Entry point of the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("========================================");
        System.out.println("   Welcome to FlipFit Application");
        System.out.println("========================================\n");

        while (running) {
            displayMainMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Running Gym Owner Demo ---");
                    GymOwnerDemo.runDemo();
                    break;

                case 2:
                    System.out.println("\n--- Running Gym Admin Demo ---");
                    GymAdminDemo.runDemo();
                    break;

                case 3:
                    System.out.println("\n--- Running Gym Customer Demo ---");
                    GymCustomerDemo.runDemo();
                    break;

                case 4:
                    System.out.println("\n--- Running All Demos ---");
                    runAllDemos();
                    break;

                case 5:
                    running = false;
                    System.out.println("\nThank you for using FlipFit! Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        }

        scanner.close();
    }

    /**
     * Display main menu.
     */
    private static void displayMainMenu() {
        System.out.println("\n========== FlipFit Main Menu ==========");
        System.out.println("1. Gym Owner Demo");
        System.out.println("2. Gym Admin Demo");
        System.out.println("3. Gym Customer Demo");
        System.out.println("4. Run All Demos");
        System.out.println("5. Exit");
        System.out.println("=====================================");
    }

    /**
     * Run all demos sequentially.
     */
    private static void runAllDemos() {
        System.out.println("\n\n");
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║      Running All FlipFit Service Demos         ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        GymOwnerDemo.runDemo();
        System.out.println("\n");

        GymAdminDemo.runDemo();
        System.out.println("\n");

        GymCustomerDemo.runDemo();
        System.out.println("\n");

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         All Demos Completed Successfully       ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
    }
}
