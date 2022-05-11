package com.eastnets.domain.jrnl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eastnets.domain.Alliance;

public class SearchLookups implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6566209501715620733L;
	private List<String> eventClassLookup = new ArrayList<String>();
	private List<String> eventSeverityLookup = new ArrayList<String>();
	private List<String> applicationServiceLookup = new ArrayList<String>();
	
	private List<String> componentLookup = new ArrayList<String>();
	private List<String> eventTypeLookup = new ArrayList<String>();
	private List<String> hostnameLookup = new ArrayList<String>();
	private List<String> alarmTypeLookup = new ArrayList<String>();
	private List<Alliance> saaListLookup = new ArrayList<Alliance>();
	
	public List<Alliance> getSaaListLookup() {
		return saaListLookup;
	}
	public void setSaaListLookup(List<Alliance> saaListLookup) {
		this.saaListLookup = saaListLookup;
	}
	
	public List<String> getComponentLookup() {
		return componentLookup;
	}
	public void setComponentLookup(List<String> componentLookup) {
		this.componentLookup = componentLookup;
	}
	
	public List<String> getEventTypeLookup() {
		return eventTypeLookup;
	}
	public void setEventTypeLookup(List<String> eventTypeLookup) {
		this.eventTypeLookup = eventTypeLookup;
	}
	
	public List<String> getHostnameLookup() {
		return hostnameLookup;
	}
	public void setHostnameLookup(List<String> hostnameLookup) {
		this.hostnameLookup = hostnameLookup;
	}
	
	public List<String> getAlarmTypeLookup() {
		return alarmTypeLookup;
	}
	public void setAlarmTypeLookup(List<String> alarmTypeLookup) {
		this.alarmTypeLookup = alarmTypeLookup;
	}
	
	public List<String> getApplicationServiceLookup() {
		return applicationServiceLookup;
	}
	public void setApplicationServiceLookup(List<String> applicationServiceLookup) {
		this.applicationServiceLookup = applicationServiceLookup;
	}
	
	public List<String> getEventClassLookup() {
		return eventClassLookup;
	}
	public void setEventClassLookup(List<String> eventClassLookup) {
		this.eventClassLookup = eventClassLookup;
	}
	
	public List<String> getEventSeverityLookup() {
		return eventSeverityLookup;
	}
	public void setEventSeverityLookup(List<String> eventSeverityLookup) {
		this.eventSeverityLookup = eventSeverityLookup;
	}
	
}
