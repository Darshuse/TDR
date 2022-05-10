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

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
	public Intervention createIntervention() {
		return new Intervention();
	}

	public InstanceIdentifier createInstanceIdentifier() {
		return new InstanceIdentifier();
	}

	public MessageInGroup createMessageInGroup() {
		return new MessageInGroup();
	}

	public Appendix createAppendix() {
		return new Appendix();
	}

	public Message createMessage() {
		return new Message();
	}

	public MessageReturn createMessageReturn() {
		return new MessageReturn();
	}

	public InterventionIdentifier createInterventionIdentifier() {
		return new InterventionIdentifier();
	}

	public MessageIdentifier createMessageIdentifier() {
		return new MessageIdentifier();
	}

	public MessageProcessingResult createMessageProcessingResult() {
		return new MessageProcessingResult();
	}

	public Instance createInstance() {
		return new Instance();
	}

	public TextIdentifier createTextIdentifier() {
		return new TextIdentifier();
	}

	public AppendixIdentifier createAppendixIdentifier() {
		return new AppendixIdentifier();
	}

	public Text createText() {
		return new Text();
	}

	public MessageGroupIdentifier createMessageGroupIdentifier() {
		return new MessageGroupIdentifier();
	}

	public MessageDiagnostic createMessageDiagnostic() {
		return new MessageDiagnostic();
	}

	public MessageGroup createMessageGroup() {
		return new MessageGroup();
	}

	public MessageCriteria createMessageCriteria() {
		return new MessageCriteria();
	}

	public InstanceSummary createInstanceSummary() {
		return new InstanceSummary();
	}

	public AppendixReturn createAppendixReturn() {
		return new AppendixReturn();
	}

	public MessageGroupReturn createMessageGroupReturn() {
		return new MessageGroupReturn();
	}

	public MessageSummary createMessageSummary() {
		return new MessageSummary();
	}
}
