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
 * MonitoringEventInfo POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class MonitoringEventInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7361745939582823677L;
	private String dateTime;
	private String description;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
