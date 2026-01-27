package com.flipfit.client;

import com.flipfit.business.NotificationService;
import com.flipfit.business.NotificationInterface;

public class FlipFitApplication {
	public static void main(String[] args) {
		// Creating the service instance
		NotificationInterface notificationService = new NotificationService();

		System.out.println("--- FlipFit Notification System ---");

		// Simulating sending a notification
		notificationService.sendNotification(101, "Welcome to FlipFit! Your registration is successful.");
	}
}