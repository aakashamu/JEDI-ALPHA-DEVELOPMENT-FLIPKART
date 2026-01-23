/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public interface WaitListInterface {
	  public boolean addToWaitList(int bookingId);   
	  public void removeFromWaitList(int bookingId); // Added from your UML diagram
	  public boolean updateWaitList();
}