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
@XmlType(name = "MessageReturn", propOrder = { "message", "fullFinMessage" })
public class MessageReturn {

	@XmlElement(name = "Message", required = true)
	protected Message message;

	@XmlElement(name = "FullFinMessage")
	protected String fullFinMessage;

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message paramMessage) {
		this.message = paramMessage;
	}

	public String getFullFinMessage() {
		return this.fullFinMessage;
	}

	public void setFullFinMessage(String paramString) {
		this.fullFinMessage = paramString;
	}
}
