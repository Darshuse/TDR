package com.eastnets.domain.jrnl;

import java.util.ArrayList;
import java.util.List;
import com.eastnets.domain.BaseEntity;

public class JournalSearchProcedureParameters extends BaseEntity {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 7385266688677878799L;
	private String fromDate;
	private String toDate;
	private List<String> eventClasses = new ArrayList<String>();
	private List<String> eventSeverities = new ArrayList<String>();
	private String operator;
	private String component;
	private String searchText;
	private String function;
	private String eventType;
	private String hostname;
	private String alarmType;
	private long eventNumber;
	private List<String> appsServices = new ArrayList<String>();
	private long listMax;
	
	
	public long getListMax() {
		return listMax;
	}
	public void setListMax(long listMax) {
		this.listMax = listMax;
	}
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public List<String> getEventClasses() {
		return eventClasses;
	}
	public void setEventClasses(List<String> eventClasses) {
		this.eventClasses = eventClasses;
	}
	
	public List<String> getEventSeverities() {
		return eventSeverities;
	}
	public void setEventSeverities(List<String> eventSeverities) {
		this.eventSeverities = eventSeverities;
	}
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	public long getEventNumber() {
		return eventNumber;
	}
	public void setEventNumber(long eventNumber) {
		this.eventNumber = eventNumber;
	}
	
	public List<String> getAppsServices() {
		return appsServices;
	}
	public void setAppsServices(List<String> appsServices) {
		this.appsServices = appsServices;
	}
	
}
