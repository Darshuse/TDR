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
@XmlType(name = "MessageGroupIdentifier", propOrder = { "tableId", "messageGroupId", "passiveLockToken" })
public class MessageGroupIdentifier {

	@XmlElement(name = "TableId", required = true)
	protected String tableId;

	@XmlElement(name = "MessageGroupId", required = true)
	protected String messageGroupId;

	@XmlElement(name = "PassiveLockToken")
	protected String passiveLockToken;

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String paramString) {
		this.tableId = paramString;
	}

	public String getMessageGroupId() {
		return this.messageGroupId;
	}

	public void setMessageGroupId(String paramString) {
		this.messageGroupId = paramString;
	}

	public String getPassiveLockToken() {
		return this.passiveLockToken;
	}

	public void setPassiveLockToken(String paramString) {
		this.passiveLockToken = paramString;
	}
}
