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
import com.eastnets.resilience.xsd.eventjournal.Severity;
import com.eastnets.resilience.xsd.eventjournal.OperatorIdentifier;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventSummary", propOrder = { "identifier", "severity", "application", "text", "clazz", "security",
		"name", "operator", "location", "alarm", "configurationManagement" })
public class EventSummary {

	@XmlElement(name = "Identifier", required = true)
	protected EventIdentifier identifier;

	@XmlElement(name = "Severity", required = true)
	protected Severity severity;

	@XmlElement(name = "Application", required = true)
	protected String application;

	@XmlElement(name = "Text", required = true)
	protected String text;

	@XmlElement(name = "Class", required = true)
	protected String clazz;

	@XmlElement(name = "Security")
	protected boolean security;

	@XmlElement(name = "Name", required = true)
	protected String name;

	@XmlElement(name = "Operator", required = true)
	protected OperatorIdentifier operator;

	@XmlElement(name = "Location", required = true)
	protected EventLocation location;

	@XmlElement(name = "Alarm", required = true)
	protected Alarm alarm;

	@XmlElement(name = "ConfigurationManagement")
	protected boolean configurationManagement;

	public EventIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(EventIdentifier paramEventIdentifier) {
		this.identifier = paramEventIdentifier;
	}

	public Severity getSeverity() {
		return this.severity;
	}

	public void setSeverity(Severity paramSeverity) {
		this.severity = paramSeverity;
	}

	public String getApplication() {
		return this.application;
	}

	public void setApplication(String paramString) {
		this.application = paramString;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String paramString) {
		this.text = paramString;
	}

	public String getClazz() {
		return this.clazz;
	}

	public void setClazz(String paramString) {
		this.clazz = paramString;
	}

	public boolean isSecurity() {
		return this.security;
	}

	public void setSecurity(boolean paramBoolean) {
		this.security = paramBoolean;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public OperatorIdentifier getOperator() {
		return this.operator;
	}

	public void setOperator(OperatorIdentifier paramOperatorIdentifier) {
		this.operator = paramOperatorIdentifier;
	}

	public EventLocation getLocation() {
		return this.location;
	}

	public void setLocation(EventLocation paramEventLocation) {
		this.location = paramEventLocation;
	}

	public Alarm getAlarm() {
		return this.alarm;
	}

	public void setAlarm(Alarm paramAlarm) {
		this.alarm = paramAlarm;
	}

	public boolean isConfigurationManagement() {
		return this.configurationManagement;
	}

	public void setConfigurationManagement(boolean paramBoolean) {
		this.configurationManagement = paramBoolean;
	}
}
