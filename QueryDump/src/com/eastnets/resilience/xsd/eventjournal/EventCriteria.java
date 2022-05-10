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
@XmlType(name = "EventCriteria", propOrder = { "source", "createdFrom", "createdTo", "classList", "severityList",
		"operator", "text", "eventType", "alarmType", "applicationList" })
public class EventCriteria {

	@XmlElement(name = "Source", required = true)
	protected EventSource source;

	@XmlElement(name = "CreatedFrom")
	protected XMLGregorianCalendar createdFrom;

	@XmlElement(name = "CreatedTo")
	protected XMLGregorianCalendar createdTo;

	@XmlElement(name = "ClassList", required = true)
	protected ClassList classList;

	@XmlElement(name = "SeverityList", required = true)
	protected SeverityList severityList;

	@XmlElement(name = "Operator")
	protected OperatorIdentifier operator;

	@XmlElement(name = "Text", required = true)
	protected String text;

	@XmlElement(name = "EventType", required = true)
	protected EventType eventType;

	@XmlElement(name = "AlarmType")
	protected AlarmType alarmType;

	@XmlElement(name = "ApplicationList", required = true)
	protected ApplicationList applicationList;

	public EventSource getSource() {
		return this.source;
	}

	public void setSource(EventSource paramEventSource) {
		this.source = paramEventSource;
	}

	public XMLGregorianCalendar getCreatedFrom() {
		return this.createdFrom;
	}

	public void setCreatedFrom(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.createdFrom = paramXMLGregorianCalendar;
	}

	public XMLGregorianCalendar getCreatedTo() {
		return this.createdTo;
	}

	public void setCreatedTo(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.createdTo = paramXMLGregorianCalendar;
	}

	public ClassList getClassList() {
		return this.classList;
	}

	public void setClassList(ClassList paramClassList) {
		this.classList = paramClassList;
	}

	public SeverityList getSeverityList() {
		return this.severityList;
	}

	public void setSeverityList(SeverityList paramSeverityList) {
		this.severityList = paramSeverityList;
	}

	public OperatorIdentifier getOperator() {
		return this.operator;
	}

	public void setOperator(OperatorIdentifier paramOperatorIdentifier) {
		this.operator = paramOperatorIdentifier;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String paramString) {
		this.text = paramString;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public void setEventType(EventType paramEventType) {
		this.eventType = paramEventType;
	}

	public AlarmType getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(AlarmType paramAlarmType) {
		this.alarmType = paramAlarmType;
	}

	public ApplicationList getApplicationList() {
		return this.applicationList;
	}

	public void setApplicationList(ApplicationList paramApplicationList) {
		this.applicationList = paramApplicationList;
	}
}
