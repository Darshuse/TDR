package com.eastnets.domain.jrnl;

import java.util.Date;
import com.eastnets.domain.BaseEntity;

public class SearchResultEntity extends BaseEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6886877168269783168L;
	private Date dateTime;
	private String eventServerity;
	private String applicationName;
	private String journalDisplayName;
	private String eventClass;
	private String eventName;
	private String operatorNickName;
	private String compName;
	private int eventIsSecurity;
	private int eventIsConfigMgmt;
	
	private int aid;
	private Long revDateTime;
	private Long sequenceNumber;
	private String eventType;
	private Long eventNumber;
	private String hostName;
	private String function;
	private String description;
	private String allianceInstance;
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventType() {
		return eventType;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Long getEventNumber() {
		return eventNumber;
	}
	
	public void setEventNumber(Long eventNumber) {
		this.eventNumber = eventNumber;
	}

	public String getHostName() {
		return hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}

	public String getCompName() {
		return compName;
	}
	
	public void setCompName(String compName) {
		this.compName = compName;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public Long getRevDateTime() {
		return revDateTime;
	}

	public void setRevDateTime(Long revDateTime) {
		this.revDateTime = revDateTime;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Date getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getEventServerity() {
		return eventServerity;
	}
	
	public void setEventServerity(String eventServerity) {
		this.eventServerity = eventServerity;
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public String getJournalDisplayName() {
		return journalDisplayName;
	}
	
	public void setJournalDisplayName(String journalDisplayName) {
		this.journalDisplayName = journalDisplayName;
	}
	
	public String getEventClass() {
		return eventClass;
	}
	
	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getOperatorNickName() {
		return operatorNickName;
	}
	
	public void setOperatorNickName(String operatorNickName) {
		this.operatorNickName = operatorNickName;
	}
	
	public int getEventIsSecurity() {
		return eventIsSecurity;
	}
	
	public void setEventIsSecurity(int eventIsSecurity) {
		this.eventIsSecurity = eventIsSecurity;
	}
	
	public int getEventIsConfigMgmt() {
		return eventIsConfigMgmt;
	}

	public void setEventIsConfigMgmt(int eventIsConfigMgmt) {
		this.eventIsConfigMgmt = eventIsConfigMgmt;
	}

	public String getAllianceInstance() {
		return allianceInstance;
	}

	public void setAllianceInstance(String allianceInstance) {
		this.allianceInstance = allianceInstance;
	}
	
	
}
