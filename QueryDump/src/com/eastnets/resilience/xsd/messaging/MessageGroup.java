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
package com.eastnets.resilience.xsd.messaging;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageGroup", propOrder = { "identifier", "messageCount", "oldestMessageTableId",
		"newestMessageTableId", "metaData" })
public class MessageGroup {

	@XmlElement(name = "Identifier")
	protected MessageGroupIdentifier identifier;

	@XmlElement(name = "MessageCount")
	protected Integer messageCount;

	@XmlElement(name = "OldestMessageTableId")
	protected String oldestMessageTableId;

	@XmlElement(name = "NewestMessageTableId")
	protected String newestMessageTableId;

	@XmlElement(name = "MetaData")
	protected String metaData;

	public MessageGroupIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(MessageGroupIdentifier paramMessageGroupIdentifier) {
		this.identifier = paramMessageGroupIdentifier;
	}

	public Integer getMessageCount() {
		return this.messageCount;
	}

	public void setMessageCount(Integer paramInteger) {
		this.messageCount = paramInteger;
	}

	public String getOldestMessageTableId() {
		return this.oldestMessageTableId;
	}

	public void setOldestMessageTableId(String paramString) {
		this.oldestMessageTableId = paramString;
	}

	public String getNewestMessageTableId() {
		return this.newestMessageTableId;
	}

	public void setNewestMessageTableId(String paramString) {
		this.newestMessageTableId = paramString;
	}

	public String getMetaData() {
		return this.metaData;
	}

	public void setMetaData(String paramString) {
		this.metaData = paramString;
	}
}
