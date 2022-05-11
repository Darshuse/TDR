/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.domain.watchdog;

import com.eastnets.domain.BaseEntity;

/**
 * LastIDs POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class LastIDs extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6592803318423041761L;
	private Long newSysRequest;
    private Long newUserRequest;
    private Long newCalculatedDup;
    private Long newPossibleDup;
    private Long newISN;
    private Long newOSN;
    private Long newNACK;
    private Long newEvent;
    
	public Long getNewSysRequest() {
		return newSysRequest;
	}
	public void setNewSysRequest(Long newSysRequest) {
		this.newSysRequest = newSysRequest;
	}
	public Long getNewUserRequest() {
		return newUserRequest;
	}
	public void setNewUserRequest(Long newUserRequest) {
		this.newUserRequest = newUserRequest;
	}
	public Long getNewCalculatedDup() {
		return newCalculatedDup;
	}
	public void setNewCalculatedDup(Long newCalculatedDup) {
		this.newCalculatedDup = newCalculatedDup;
	}
	public Long getNewPossibleDup() {
		return newPossibleDup;
	}
	public void setNewPossibleDup(Long newPossibleDup) {
		this.newPossibleDup = newPossibleDup;
	}
	public Long getNewISN() {
		return newISN;
	}
	public void setNewISN(Long newISN) {
		this.newISN = newISN;
	}
	public Long getNewOSN() {
		return newOSN;
	}
	public void setNewOSN(Long newOSN) {
		this.newOSN = newOSN;
	}
	public Long getNewNACK() {
		return newNACK;
	}
	public void setNewNACK(Long newNACK) {
		this.newNACK = newNACK;
	}
	public Long getNewEvent() {
		return newEvent;
	}
	public void setNewEvent(Long newEvent) {
		this.newEvent = newEvent;
	}	
	
 
}
