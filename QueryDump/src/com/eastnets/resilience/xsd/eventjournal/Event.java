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
package com.eastnets.resilience.xsd.eventjournal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event", propOrder = { "identifier" })
public class Event {

	@XmlElement(name = "Identifier", required = true)
	protected EventIdentifier identifier;

	public EventIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(EventIdentifier paramEventIdentifier) {
		this.identifier = paramEventIdentifier;
	}
}
