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

package com.eastnets.domain.watchdog;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.BaseEntity;

/**
 * EventNotification POJO
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class EventNotification extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1347243910042563343L;
	private DateFormat dateFormat;
	private int iSecurity;
	private int iAlarm;
	private Double jrnlSeqNbr;
	private Long aid;
	private Long jrnlRevDatetime;
	private Long notificationId;
	
	private String description;
	private String componentName;
	private String eventNbr;
	private String eventName;
	private String eventClass;
	private String eventSeverity;
	private String eventType;
	private String jrnlApplServName;
	private String jrnlFuncName;
	private String jrnlOperNickname;
	private String jrnlHostname;
	private String jrnlMergedText;
	private Timestamp jrnlDatetime;
	private Timestamp receptionDate;

	public EventNotification(){
		
		dateFormat = new SimpleDateFormat( Constants.DATE_TIME_FORMAT);
	}
	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Long getJrnlRevDatetime() {
		return jrnlRevDatetime;
	}

	public void setJrnlRevDatetime(Long jrnlRevDatetime) {
		this.jrnlRevDatetime = jrnlRevDatetime;
	}

	public Double getJrnlSeqNbr() {
		return jrnlSeqNbr;
	}

	public void setJrnlSeqNbr(Double jrnlSeqNbr) {
		this.jrnlSeqNbr = jrnlSeqNbr;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getEventNbr() {
		return eventNbr;
	}

	public void setEventNbr(String eventNbr) {
		this.eventNbr = eventNbr;
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

	public String getEventSeverity() {
		return eventSeverity;
	}

	public void setEventSeverity(String eventSeverity) {
		this.eventSeverity = eventSeverity;
	}

	public int getiSecurity() {
		return iSecurity;
	}

	public void setiSecurity(int iSecurity) {
		this.iSecurity = iSecurity;
	}

	public int getiAlarm() {
		return iAlarm;
	}

	public void setiAlarm(int iAlarm) {
		this.iAlarm = iAlarm;
	}

	public String getJrnlApplServName() {
		return jrnlApplServName;
	}

	public void setJrnlApplServName(String jrnlApplServName) {
		this.jrnlApplServName = jrnlApplServName;
	}

	public String getJrnlFuncName() {
		return jrnlFuncName;
	}

	public void setJrnlFuncName(String jrnlFuncName) {
		this.jrnlFuncName = jrnlFuncName;
	}

	public String getJrnlOperNickname() {
		return jrnlOperNickname;
	}

	public void setJrnlOperNickname(String jrnlOperNickname) {
		this.jrnlOperNickname = jrnlOperNickname;
	}

	public String getJrnlHostname() {
		return jrnlHostname;
	}

	public void setJrnlHostname(String jrnlHostname) {
		this.jrnlHostname = jrnlHostname;
	}

	public String getJrnlMergedText() {
		return jrnlMergedText;
	}

	public void setJrnlMergedText(String jrnlMergedText) {
		this.jrnlMergedText = jrnlMergedText;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getJrnlDatetime() {
		if(jrnlDatetime == null){
			return null;
		}
		return dateFormat.format(jrnlDatetime);
	}

	public void setJrnlDatetime(Timestamp jrnlDatetime) {
		this.jrnlDatetime = jrnlDatetime;
	}
	public String getReceptionDate() {
		if(receptionDate == null){
			return null;
		}
		return dateFormat.format(receptionDate);
	}

	public void setReceptionDate(Timestamp receptionDate) {
		this.receptionDate = receptionDate;
	}
	
	

}
