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
@XmlType(name = "MessageProcessingResult", propOrder = { "messageDiagnostic", "possibleDuplicate" })
public class MessageProcessingResult {

	@XmlElement(name = "MessageDiagnostic", required = true)
	protected MessageDiagnostic messageDiagnostic;

	@XmlElement(name = "PossibleDuplicate")
	protected boolean possibleDuplicate;

	public MessageDiagnostic getMessageDiagnostic() {
		return this.messageDiagnostic;
	}

	public void setMessageDiagnostic(MessageDiagnostic paramMessageDiagnostic) {
		this.messageDiagnostic = paramMessageDiagnostic;
	}

	public boolean isPossibleDuplicate() {
		return this.possibleDuplicate;
	}

	public void setPossibleDuplicate(boolean paramBoolean) {
		this.possibleDuplicate = paramBoolean;
	}
}
