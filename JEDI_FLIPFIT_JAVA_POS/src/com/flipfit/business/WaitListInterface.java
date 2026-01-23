/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public interface WaitListInterface {
	  public boolean addToWaitList(int bookingId);   
	  public boolean removeFromWaitList(int bookingId); // Added from your UML diagram
}