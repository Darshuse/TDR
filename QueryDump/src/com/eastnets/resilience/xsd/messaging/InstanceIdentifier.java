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
@XmlType(name = "InstanceIdentifier", propOrder = { "sUmid", "number", "passiveLockToken" })
public class InstanceIdentifier {

	@XmlElement(name = "SUmid", required = true)
	protected String sUmid;

	@XmlElement(name = "Number")
	protected int number;

	@XmlElement(name = "PassiveLockToken")
	protected String passiveLockToken;

	public String getSUmid() {
		return this.sUmid;
	}

	public void setSUmid(String paramString) {
		this.sUmid = paramString;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int paramInt) {
		this.number = paramInt;
	}

	public String getPassiveLockToken() {
		return this.passiveLockToken;
	}

	public void setPassiveLockToken(String paramString) {
		this.passiveLockToken = paramString;
	}
}
