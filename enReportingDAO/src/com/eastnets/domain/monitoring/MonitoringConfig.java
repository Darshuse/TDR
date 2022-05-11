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

package com.eastnets.domain.monitoring;

import java.io.Serializable;

/**
 * MonitoringConfig POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class MonitoringConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2245742794656752721L;
	private int dayHistory;
	private boolean displayJournals;
	private boolean displayWarnings;

	public int getDayHistory() {
		return dayHistory;
	}

	public void setDayHistory(int dayHistory) {
		this.dayHistory = dayHistory;
	}

	public boolean isDisplayJournals() {
		return displayJournals;
	}

	public void setDisplayJournals(boolean displayJournals) {
		this.displayJournals = displayJournals;
	}

	public boolean isDisplayWarnings() {
		return displayWarnings;
	}

	public void setDisplayWarnings(boolean displayWarnings) {
		this.displayWarnings = displayWarnings;
	}

}