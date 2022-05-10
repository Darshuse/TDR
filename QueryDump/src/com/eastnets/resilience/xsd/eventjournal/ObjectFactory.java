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

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
	public EventParameterList createEventParameterList() {
		return new EventParameterList();
	}

	public ApplicationList createApplicationList() {
		return new ApplicationList();
	}

	public SeverityList createSeverityList() {
		return new SeverityList();
	}

	public EventReturn createEventReturn() {
		return new EventReturn();
	}

	public EventCriteria createEventCriteria() {
		return new EventCriteria();
	}

	public EventIdentifier createEventIdentifier() {
		return new EventIdentifier();
	}

	public EventSummary createEventSummary() {
		return new EventSummary();
	}

	public Event createEvent() {
		return new Event();
	}

	public EventSource createEventSource() {
		return new EventSource();
	}

	public ClassList createClassList() {
		return new ClassList();
	}

	public EventExtractCriteria createEventExtractCriteria() {
		return new EventExtractCriteria();
	}
}
