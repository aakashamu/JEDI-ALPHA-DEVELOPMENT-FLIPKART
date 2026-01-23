/**
 * 
 */
package com.flipfit.bean;
import java.sql.Timestamp;

/**
 * 
 */
public class WaitListEntry {
	private int waitlistid;
	private int position;
	private Timestamp createdAt;
	
	public int getWaitlistid() {
		return waitlistid;
	}
	public void setWaitlistid(int waitlistid) {
		this.waitlistid = waitlistid;
	} 
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
}
