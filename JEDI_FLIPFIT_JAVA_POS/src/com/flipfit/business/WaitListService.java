/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public class WaitListService implements WaitListInterface{

	@Override
	public boolean addToWaitList(int bookingId) {
		// TODO Auto-generated method stub
		System.out.println("Booking Id added to waitlist-->" + bookingId);
		return true;
	}

	@Override
	public boolean removeFromWaitList(int bookingId) {
		// TODO Auto-generated method stub
		System.out.println("Booking Id removed from waitlist-->" + bookingId);
		return true;
	}
	
}
