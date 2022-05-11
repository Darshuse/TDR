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
import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;

/**
 * InterventionDetails POJO
 * @author EastNets
 * @since September 25, 2012
 */
public class InterventionDetails implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8265133867970896512L;
	private Long intvSeqNbr;
	private Date intvDateTime;
	private Clob intvText;
	private String intvOperNickname;
	private String intvIntyCategory;
	private String intvIntyName;
	private Integer timeZoneOffset;
	
	
	public Long getIntvSeqNbr() {
		return intvSeqNbr;
	}
	public void setIntvSeqNbr(Long intvSeqNbr) {
		this.intvSeqNbr = intvSeqNbr;
	}
	public Date getIntvDateTimeOnDB() {
		return intvDateTime;
	}
	public Date getIntvDateTime() {
		if ( intvDateTime == null ){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(intvDateTime);
		if ( timeZoneOffset != null ){
			cal.add( Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date( cal.getTime().getTime() );
	}
	public void setIntvDateTime(Date intvDateTime) {
		this.intvDateTime = intvDateTime;
	}
	public Clob getIntvText() {
		return intvText;
	}
	public void setIntvText(Clob intvText) {
		this.intvText = intvText;
	}
	public String getIntvOperNickname() {
		return intvOperNickname;
	}
	public void setIntvOperNickname(String intvOperNickname) {
		this.intvOperNickname = intvOperNickname;
	}
	public String getIntvIntyCategory() {
		return intvIntyCategory;
	}
	public void setIntvIntyCategory(String intvIntyCategory) {
		this.intvIntyCategory = intvIntyCategory;
	}
	public String getIntvIntyName() {
		return intvIntyName;
	}
	public void setIntvIntyName(String intvIntyName) {
		this.intvIntyName = intvIntyName;
	}
	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}
	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

}
