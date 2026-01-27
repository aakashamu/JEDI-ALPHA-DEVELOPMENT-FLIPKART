package com.flipfit.client;

import com.flipfit.bean.GymCentre;
import com.flipfit.business.GymOwnerService;
import java.util.Scanner;

public class GymOwnerFlipfitMenu {
    private GymOwnerService gymOwnerService = new GymOwnerService();
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu(int ownerId) {
        while (true) {
            System.out.println("\n--- Gym Owner Dashboard ---");
            System.out.println("1. Add New Gym Centre");
            System.out.println("2. View All My Centres");
            System.out.println("3. Send Admin Approval Request");
            System.out.println("4. Exit to Main Menu");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Centre ID: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter Centre Name: ");
                    String name = scanner.next();
                    System.out.print("Enter City: ");
                    String city = scanner.next();

                    // 1. Create the empty object (Matches the 'required: no arguments' error)
                    GymCentre newCentre = new GymCentre();

                    // 2. Set the values manually using the setter methods
                    newCentre.setCentreId(id);
                    newCentre.setName(name);
                    newCentre.setCity(city);
                    newCentre.setState("Karnataka");
                    newCentre.setPincode(560001);
                    newCentre.setApproved(true);

                    // 3. Add it to your service
                    gymOwnerService.addCentre(newCentre);
                    break;

                case 2:
                    System.out.println("--- Your Gym Centres ---");
                    for(GymCentre gc : gymOwnerService.viewMyCentres()) {
                        System.out.println("ID: " + gc.getCentreId() + " | Name: " + gc.getName() + " | City: " + gc.getCity());
                    }
                    break;

                case 3:
                    gymOwnerService.requestApproval(ownerId);
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid Option.");
            }
        }
    }
}