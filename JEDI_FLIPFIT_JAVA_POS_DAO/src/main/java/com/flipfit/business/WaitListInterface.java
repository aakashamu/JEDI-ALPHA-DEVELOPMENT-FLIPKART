/**
 * 
 */
package com.flipfit.business;
/**
 * The Interface WaitListInterface.
 *
 * @author Ananya
 * @ClassName  "WaitListInterface"
 */
public interface WaitListInterface {
	  public boolean addToWaitList(int bookingId);   
	  public void removeFromWaitList(int bookingId); // Added from your UML diagram
	  public boolean updateWaitList();
	  public boolean promoteFromWaitList(int availabilityId);
}