package com.eastnets.domain.jrnl;

import java.io.Serializable;
import java.util.Date;

public class DetailsEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6484791679743097934L;
	StringBuffer description;
	Date dateTime;
	String operator;
	String function;
	String type;
	String severity;
	String application;
	String compName;
	String eventName;
	String eventClass;
	String eventNumber;
	String hostname;
	String sequenceNumber;
	String saaName;
	String browserAddress;
	String browserHostname;
	
	public String getSaaName() {
		return saaName;
	}
	public void setSaaName(String saaName) {
		this.saaName = saaName;
	}
	
	public StringBuffer getDescription() {
		return description;
	}
	public void setDescription(StringBuffer description) {
		this.description = description;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getEventClass() {
		return eventClass;
	}
	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}
	
	public String getEventNumber() {
		return eventNumber;
	}
	public void setEventNumber(String eventNumber) {
		this.eventNumber = eventNumber;
	}
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getBrowserAddress() {
		return browserAddress;
	}
	public void setBrowserAddress(String browserAddress) {
		this.browserAddress = browserAddress;
	}
	public String getBrowserHostname() {
		return browserHostname;
	}
	public void setBrowserHostname(String browserHostname) {
		this.browserHostname = browserHostname;
	}
	
	
	
}
