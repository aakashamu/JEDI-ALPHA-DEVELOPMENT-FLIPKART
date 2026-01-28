package com.flipfit.client;

import com.flipfit.business.NotificationService;
import com.flipfit.business.NotificationInterface;
/**
 * The Class FlipFitApplication.
 *
 * @author Ananya
 * @ClassName  "FlipFitApplication"
 */
public class FlipFitApplication {
  /**
   * Main.
   *
   * @param args the args
   */
	public static void main(String[] args) {
		// Creating the service instance
		NotificationInterface notificationService = new NotificationService();

		System.out.println("--- FlipFit Notification System ---");

		// Simulating sending a notification
		notificationService.sendNotification(101, "Welcome to FlipFit! Your registration is successful.");
	}
}