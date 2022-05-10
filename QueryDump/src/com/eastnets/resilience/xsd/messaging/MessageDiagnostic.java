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
@XmlType(name = "MessageDiagnostic", propOrder = { "errorOffset", "errorReason" })
public class MessageDiagnostic {

	@XmlElement(name = "ErrorOffset")
	protected int errorOffset;

	@XmlElement(name = "ErrorReason", required = true)
	protected ErrorReason errorReason;

	public int getErrorOffset() {
		return this.errorOffset;
	}

	public void setErrorOffset(int paramInt) {
		this.errorOffset = paramInt;
	}

	public ErrorReason getErrorReason() {
		return this.errorReason;
	}

	public void setErrorReason(ErrorReason paramErrorReason) {
		this.errorReason = paramErrorReason;
	}
}
