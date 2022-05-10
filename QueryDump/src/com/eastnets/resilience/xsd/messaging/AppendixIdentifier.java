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
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppendixIdentifier", propOrder = { "sUmid", "instanceNumber", "dateTime", "internalSequenceNumber",
		"passiveLockToken" })
public class AppendixIdentifier {

	@XmlElement(name = "SUmid", required = true)
	protected String sUmid;

	@XmlElement(name = "InstanceNumber")
	protected int instanceNumber;

	@XmlElement(name = "DateTime")
	protected XMLGregorianCalendar dateTime;

	@XmlElement(name = "InternalSequenceNumber")
	protected int internalSequenceNumber;

	@XmlElement(name = "PassiveLockToken")
	protected String passiveLockToken;

	public String getSUmid() {
		return this.sUmid;
	}

	public void setSUmid(String paramString) {
		this.sUmid = paramString;
	}

	public int getInstanceNumber() {
		return this.instanceNumber;
	}

	public void setInstanceNumber(int paramInt) {
		this.instanceNumber = paramInt;
	}

	public XMLGregorianCalendar getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.dateTime = paramXMLGregorianCalendar;
	}

	public int getInternalSequenceNumber() {
		return this.internalSequenceNumber;
	}

	public void setInternalSequenceNumber(int paramInt) {
		this.internalSequenceNumber = paramInt;
	}

	public String getPassiveLockToken() {
		return this.passiveLockToken;
	}

	public void setPassiveLockToken(String paramString) {
		this.passiveLockToken = paramString;
	}
}
