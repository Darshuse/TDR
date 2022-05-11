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
 * MonitoringMessageInfo POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class MonitoringMessageInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2912165436995104876L;
	private String mesgCreaDateTime;
	private String mesgUUMID;
	private String mesgUUMIDSuffix;

	public String getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(String mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getMesgUUMID() {
		return mesgUUMID;
	}

	public void setMesgUUMID(String mesgUUMID) {
		this.mesgUUMID = mesgUUMID;
	}

	public String getMesgUUMIDSuffix() {
		return mesgUUMIDSuffix;
	}

	public void setMesgUUMIDSuffix(String mesgUUMIDSuffix) {
		this.mesgUUMIDSuffix = mesgUUMIDSuffix;
	}

}
