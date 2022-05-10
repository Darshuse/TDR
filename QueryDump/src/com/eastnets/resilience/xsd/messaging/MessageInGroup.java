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
@XmlType(name = "MessageInGroup", propOrder = { "identifier", "metaData" })
public class MessageInGroup {

	@XmlElement(name = "Identifier", required = true)
	protected MessageIdentifier identifier;

	@XmlElement(name = "MetaData")
	protected String metaData;

	public MessageIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(MessageIdentifier paramMessageIdentifier) {
		this.identifier = paramMessageIdentifier;
	}

	public String getMetaData() {
		return this.metaData;
	}

	public void setMetaData(String paramString) {
		this.metaData = paramString;
	}
}
