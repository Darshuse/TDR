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

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

/**
 * InterventionExtDetails POJO
 * @author EastNets
 * @since September 26, 2012
 */
public class InterventionExtDetails implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3258985272666955977L;
	private String instUnitName;
	private String intvIntyName;
	private String intvIntyCategory;
	private String intvOperNickname;
	private String intvApplServName;
	private String intvMpfnName;
	private Date intvDateTime;
	private Clob intvText;
	private Integer timeZoneOffset;
	
	public String getInstUnitName() {
		return instUnitName;
	}
	public void setInstUnitName(String instUnitName) {
		this.instUnitName = instUnitName;
	}
	public String getIntvIntyName() {
		return intvIntyName;
	}
	public void setIntvIntyName(String intvIntyName) {
		this.intvIntyName = intvIntyName;
	}
	public String getIntvIntyCategory() {
		return intvIntyCategory;
	}
	public void   setIntvIntyCategoryFormatted(String val){
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getIntvIntyCategoryFormatted() {
		String val= StringUtils.trim(intvIntyCategory);
		val= StringUtils.capitalize(StringUtils.lowerCase((StringUtils.substring(val, 5))));		
		if (StringUtils.equalsIgnoreCase( val, "Mesg_modified")){
			return "Message modified";
		}
		return val;
	}
	public void setIntvIntyCategory(String intvIntyCategory) {
		this.intvIntyCategory = intvIntyCategory;
	}
	public String getIntvOperNickname() {
		return intvOperNickname;
	}
	public void setIntvOperNickname(String intvOperNickname) {
		this.intvOperNickname = intvOperNickname;
	}
	public String getIntvApplServName() {
		return intvApplServName;
	}
	public void setIntvApplServName(String intvApplServName) {
		this.intvApplServName = intvApplServName;
	}
	public String getIntvMpfnName() {
		return intvMpfnName;
	}
	public void setIntvMpfnName(String intvMpfnName) {
		this.intvMpfnName = intvMpfnName;
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
	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}
	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}
	

}
