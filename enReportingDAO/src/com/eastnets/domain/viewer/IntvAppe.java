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
 * IntvAppe POJO
 * @author EastNets
 * @since October 3, 2012
 */
public class IntvAppe implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6879741449365327860L;
	private String displayText;
	private Date date;
	
	private InterventionExtDetails intv;
	private AppendixExtDetails 	appe;
	private Integer aid;
	private Integer umidl;
	private Integer umidh;
	private boolean intervention;
	private Integer instNum;
	private Long    seqNbr;
	
	private Integer timeZoneOffset;
	
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	public Date getDateOnDB() {
		return date;
	}
	public Date getDate() {
		if ( date == null ){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if ( timeZoneOffset != null ){
			cal.add( Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date( cal.getTime().getTime() );
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public Integer getUmidl() {
		return umidl;
	}
	public void setUmidl(Integer umidl) {
		this.umidl = umidl;
	}
	public Integer getUmidh() {
		return umidh;
	}
	public void setUmidh(Integer umidh) {
		this.umidh = umidh;
	}
	public boolean isIntervention() {
		return intervention;
	}
	public void setIntervention(boolean intervention) {
		this.intervention = intervention;
	}
	public Integer getInstNum() {
		return instNum;
	}
	public void setInstNum(Integer instNum) {
		this.instNum = instNum;
	}
	public Long getSeqNbr() {
		return seqNbr;
	}
	public void setSeqNbr(Long seqNbr) {
		this.seqNbr = seqNbr;
	}
	public void setIntv(InterventionExtDetails intv) {
		this.intv = intv;
	}
	public InterventionExtDetails getIntv() {
		return intv;
	}
	public void setAppe(AppendixExtDetails appe) {
		this.appe = appe;
	}
	public AppendixExtDetails getAppe() {
		return appe;
	}
	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}
	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}
}
