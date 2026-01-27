package com.flipfit.client;

import com.flipfit.bean.GymCentre;
import com.flipfit.business.GymCentreService;
import java.util.Scanner;

public class GymCentreFlipfitMenu {
    private GymCentreService gymCentreService = new GymCentreService();
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- Gym Centre Dashboard ---");
            System.out.println("1. Add New Gym Centre");
            System.out.println("2. View Centre Details");
            System.out.println("3. Exit to Main Menu");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Centre ID: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter Centre Name: ");
                    String name = scanner.next();
                    System.out.print("Enter City: ");
                    String city = scanner.next();
                    System.out.print("Enter State: ");
                    String state = scanner.next();
                    System.out.print("Enter Pincode: ");
                    int pincode = scanner.nextInt();
                    System.out.print("Enter Owner ID: ");
                    int ownerId = scanner.nextInt();

                    GymCentre newCentre = new GymCentre();
                    newCentre.setCentreId(id);
                    newCentre.setName(name);
                    newCentre.setCity(city);
                    newCentre.setState(state);
                    newCentre.setPincode(pincode);
                    newCentre.setOwnerId(ownerId);
                    newCentre.setApproved(false);

                    gymCentreService.addGymCentre(newCentre);
                    break;
                case 2:
                    System.out.print("Enter Centre ID to view details: ");
                    int centreId = scanner.nextInt();
                    GymCentre centre = gymCentreService.getCentreDetails(centreId);
                    if (centre != null) {
                        System.out.println("Centre ID: " + centre.getCentreId());
                        System.out.println("Name: " + centre.getName());
                        System.out.println("City: " + centre.getCity());
                        System.out.println("State: " + centre.getState());
                        System.out.println("Pincode: " + centre.getPincode());
                        System.out.println("Owner ID: " + centre.getOwnerId());
                        System.out.println("Approved: " + (centre.isApproved() ? "Yes" : "No"));
                    } else {
                        System.out.println("No centre found with ID: " + centreId);
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid Option.");
            }
        }
    }
}
