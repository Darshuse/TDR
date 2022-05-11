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

package com.eastnets.domain.viewer;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * AppendixDetails POJO
 * @author EastNets
 * @since September 25, 2012
 */
public class AppendixDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7329333042237759455L;
	
	private Long appeSeqNbr;
	private Date appeDateTime;
	private String appeType;
	private String appeIAppName;
	private String appeSessionHolder;
	private Integer appeSessionNbr;
	private Integer appeSequenceNbr;
	private String appeNetworkDeliveryStatus;
	private String appeAckNackText;
	private String appePkiAuthResult;
	private String appePkiAuthorisationRes;
	private String appePkiPac2Result;
	private String appeRmaCheckResult;
	private Integer timeZoneOffset;
	
	public Long getAppeSeqNbr() {
		return appeSeqNbr;
	}
	public void setAppeSeqNbr(Long appeSeqNbr) {
		this.appeSeqNbr = appeSeqNbr;
	}
	public Date getAppeDateTimeOnDB() {
		return appeDateTime;
	}
	public Date getAppeDateTime() {
		if ( appeDateTime == null ){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(appeDateTime);
		if ( timeZoneOffset != null ){
			cal.add( Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date( cal.getTime().getTime() );
	}
	public void setAppeDateTime(Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}
	public String getAppeType() {
		return appeType;
	}
	public void setAppeType(String appeType) {
		this.appeType = appeType;
	}
	public String getAppeIAppName() {
		return appeIAppName;
	}
	public void setAppeIAppName(String appeIAppName) {
		this.appeIAppName = appeIAppName;
	}
	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}
	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}
	public Integer getAppeSessionNbr() {
		return appeSessionNbr;
	}
	public void setAppeSessionNbr(Integer appeSessionNbr) {
		this.appeSessionNbr = appeSessionNbr;
	}
	public Integer getAppeSequenceNbr() {
		return appeSequenceNbr;
	}
	public void setAppeSequenceNbr(Integer appeSequenceNbr) {
		this.appeSequenceNbr = appeSequenceNbr;
	}
	public String getAppeNetworkDeliveryStatus() {
		return appeNetworkDeliveryStatus;
	}
	public void setAppeNetworkDeliveryStatus(String appeNetworkDeliveryStatus) {
		this.appeNetworkDeliveryStatus = appeNetworkDeliveryStatus;
	}
	public String getAppeAckNackText() {
		return appeAckNackText;
	}
	public void setAppeAckNackText(String appeAckNackText) {
		this.appeAckNackText = appeAckNackText;
	}
	public String getAppePkiAuthResult() {
		return appePkiAuthResult;
	}
	public void setAppePkiAuthResult(String appePkiAuthResult) {
		this.appePkiAuthResult = appePkiAuthResult;
	}
	public String getAppePkiAuthorisationRes() {
		return appePkiAuthorisationRes;
	}
	public void setAppePkiAuthorisationRes(String appePkiAuthorisationRes) {
		this.appePkiAuthorisationRes = appePkiAuthorisationRes;
	}
	public String getAppePkiPac2Result() {
		return appePkiPac2Result;
	}
	public void setAppePkiPac2Result(String appePkiPac2Result) {
		this.appePkiPac2Result = appePkiPac2Result;
	}
	public String getAppeRmaCheckResult() {
		return appeRmaCheckResult;
	}
	public void setAppeRmaCheckResult(String appeRmaCheckResult) {
		this.appeRmaCheckResult = appeRmaCheckResult;
	}
	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}
	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}
	
	
}
