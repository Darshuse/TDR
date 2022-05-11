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

package com.eastnets.domain;

import java.io.Serializable;

/**
 * UserSettings POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class UserSettings implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -6129013028197941904L;
	private String rpDirectory;
	private String wdNbDayHistory;
	private String vwListDepth;
	private String email;
	public String getRpDirectory() {
		return rpDirectory;
	}
	public void setRpDirectory(String rpDirectory) {
		this.rpDirectory = rpDirectory;
	}
	public String getWdNbDayHistory() {
		return wdNbDayHistory;
	}
	public void setWdNbDayHistory(String wdNbDayHistory) {
		this.wdNbDayHistory = wdNbDayHistory;
	}
	public String getVwListDepth() {
		return vwListDepth;
	}
	public void setVwListDepth(String vwListDepth) {
		this.vwListDepth = vwListDepth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


}
