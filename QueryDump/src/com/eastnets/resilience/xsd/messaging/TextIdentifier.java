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
@XmlType(name = "TextIdentifier", propOrder = { "sUmid", "passiveLockToken" })
public class TextIdentifier {

	@XmlElement(name = "SUmid", required = true)
	protected String sUmid;

	@XmlElement(name = "PassiveLockToken")
	protected String passiveLockToken;

	public String getSUmid() {
		return this.sUmid;
	}

	public void setSUmid(String paramString) {
		this.sUmid = paramString;
	}

	public String getPassiveLockToken() {
		return this.passiveLockToken;
	}

	public void setPassiveLockToken(String paramString) {
		this.passiveLockToken = paramString;
	}
}