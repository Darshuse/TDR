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
 * Alliance POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class Alliance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2905798732309418526L;
	private MonitoringMessageInfo lastProcessedMessage = new MonitoringMessageInfo();
	private MonitoringEventInfo lastProcessedEvent = new MonitoringEventInfo();

	private String instanceName;
	private String aid;
	private String jrnlSeqNbr;
	private String jrnlRevDateTime;
	private String lastUmidl;
	private String lastUmidh;

	public MonitoringMessageInfo getLastProcessedMessage() {
		return lastProcessedMessage;
	}

	public void setLastProcessedMessage(MonitoringMessageInfo lastProcessedMessage) {
		this.lastProcessedMessage = lastProcessedMessage;
	}

	public MonitoringEventInfo getLastProcessedEvent() {
		return lastProcessedEvent;
	}

	public void setLastProcessedEvent(MonitoringEventInfo lastProcessedEvent) {
		this.lastProcessedEvent = lastProcessedEvent;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getJrnlSeqNbr() {
		return jrnlSeqNbr;
	}

	public void setJrnlSeqNbr(String jrnlSeqNbr) {
		this.jrnlSeqNbr = jrnlSeqNbr;
	}

	public String getJrnlRevDateTime() {
		return jrnlRevDateTime;
	}

	public void setJrnlRevDateTime(String jrnlRevDateTime) {
		this.jrnlRevDateTime = jrnlRevDateTime;
	}

	public String getLastUmidl() {
		return lastUmidl;
	}

	public void setLastUmidl(String lastUmidl) {
		this.lastUmidl = lastUmidl;
	}

	public String getLastUmidh() {
		return lastUmidh;
	}

	public void setLastUmidh(String lastUmidh) {
		this.lastUmidh = lastUmidh;
	}
}
