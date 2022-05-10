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
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventExtractCriteria", propOrder = { "eventLocation", "startDateTime", "endDateTime", "filter" })
public class EventExtractCriteria {

	@XmlElement(name = "EventLocation", required = true)
	protected EventExtractLocation eventLocation;

	@XmlElement(name = "StartDateTime")
	protected XMLGregorianCalendar startDateTime;

	@XmlElement(name = "EndDateTime")
	protected XMLGregorianCalendar endDateTime;

	@XmlElement(name = "Filter")
	protected String filter;

	public EventExtractLocation getEventLocation() {
		return this.eventLocation;
	}

	public void setEventLocation(EventExtractLocation paramEventExtractLocation) {
		this.eventLocation = paramEventExtractLocation;
	}

	public XMLGregorianCalendar getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.startDateTime = paramXMLGregorianCalendar;
	}

	public XMLGregorianCalendar getEndDateTime() {
		return this.endDateTime;
	}

	public void setEndDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.endDateTime = paramXMLGregorianCalendar;
	}

	public String getFilter() {
		return this.filter;
	}

	public void setFilter(String paramString) {
		this.filter = paramString;
	}
}
