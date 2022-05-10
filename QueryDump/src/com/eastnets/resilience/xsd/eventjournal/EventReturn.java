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

import com.eastnets.resilience.xmldump.utils.StringUtils;
import com.eastnets.resilience.xsd.InputStartEntry;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventReturn", propOrder = { "event", "description", "dateTime", "operator", "function", "type", "severity", "application", "alarmHistory", "alarmDateTime", "alarmOperator", "name", "clazz", "number", "config_mgmt", "security",
		"jrnl_business_entity_name", "hostname", "browserHostname", "browserIpAddress" })

public class EventReturn implements InputStartEntry {

	@XmlElement(name = "Event", required = true)
	protected Event event;

	@XmlElement(name = "Description", required = true)
	protected String description;

	@XmlElement(name = "DateTime", required = true)
	protected XMLGregorianCalendar dateTime;

	@XmlElement(name = "Operator", required = true)
	protected OperatorIdentifier operator;

	@XmlElement(name = "Function", required = true)
	protected String function;

	@XmlElement(name = "Type", required = true)
	protected String type;

	@XmlElement(name = "Severity", required = true)
	protected Severity severity;

	@XmlElement(name = "Application", required = true)
	protected String application;

	@XmlElement(name = "AlarmHistory")
	protected Alarm alarmHistory;

	@XmlElement(name = "AlarmDateTime")
	protected XMLGregorianCalendar alarmDateTime;

	@XmlElement(name = "AlarmOperator")
	protected OperatorIdentifier alarmOperator;

	@XmlElement(name = "Name", required = true)
	protected String name;

	@XmlElement(name = "Class", required = true)
	protected String clazz;

	@XmlElement(name = "Number")
	protected int number;

	@XmlElement(name = "ConfigurationManagement")
	protected boolean config_mgmt;

	@XmlElement(name = "Security")
	protected boolean security;

	@XmlElement(name = "BusinessEntity")
	protected String jrnl_business_entity_name;

	@XmlElement(name = "Hostname", required = true)
	protected String hostname;

	@XmlElement(name = "BrowserHostname")
	protected String browserHostname;

	@XmlElement(name = "BrowserIpAddress")
	protected String browserIpAddress;

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event paramEvent) {
		this.event = paramEvent;
	}

	public boolean isConfig_mgmt() {
		return config_mgmt;
	}

	public void setConfig_mgmt(boolean config_mgmt) {
		this.config_mgmt = config_mgmt;
	}

	public String getJrnl_business_entity_name() {
		return jrnl_business_entity_name;
	}

	public void setJrnl_business_entity_name(String jrnl_business_entity_name) {
		this.jrnl_business_entity_name = jrnl_business_entity_name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String paramString) {
		this.description = paramString;
	}

	public XMLGregorianCalendar getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.dateTime = paramXMLGregorianCalendar;
	}

	public OperatorIdentifier getOperator() {
		return this.operator;
	}

	public void setOperator(OperatorIdentifier paramOperatorIdentifier) {
		this.operator = paramOperatorIdentifier;
	}

	public String getFunction() {
		return this.function;
	}

	public void setFunction(String paramString) {
		this.function = paramString;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String paramString) {
		this.type = paramString;
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

	public Alarm getAlarmHistory() {
		return this.alarmHistory;
	}

	public void setAlarmHistory(Alarm paramAlarm) {
		this.alarmHistory = paramAlarm;
	}

	public XMLGregorianCalendar getAlarmDateTime() {
		return this.alarmDateTime;
	}

	public String getBrowserHostname() {
		return browserHostname;
	}

	public void setBrowserHostname(String browserHostname) {
		this.browserHostname = browserHostname;
	}

	public String getBrowserIpAddress() {
		return browserIpAddress;
	}

	public void setBrowserIpAddress(String browserIpAddress) {
		this.browserIpAddress = browserIpAddress;
	}

	public void setAlarmDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.alarmDateTime = paramXMLGregorianCalendar;
	}

	public OperatorIdentifier getAlarmOperator() {
		return this.alarmOperator;
	}

	public void setAlarmOperator(OperatorIdentifier paramOperatorIdentifier) {
		this.alarmOperator = paramOperatorIdentifier;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public String getClazz() {
		return this.clazz;
	}

	public void setClazz(String paramString) {
		this.clazz = paramString;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int paramInt) {
		this.number = paramInt;
	}

	public boolean isSecurity() {
		return this.security;
	}

	public void setSecurity(boolean paramBoolean) {
		this.security = paramBoolean;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String paramString) {
		this.hostname = paramString;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Sequence " + this.getEvent().getIdentifier().getSequenceNumber() + "\n");
		builder.append("Text " + this.getDescription());
		return builder.toString();
	}

	public int getObjectCRC() {
		int crc = 0;
		crc = this.hashCode();
		StringUtils.computeCRC(new Integer(crc).toString());
		return crc;
	}
}
