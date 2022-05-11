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

package com.eastnets.domain.swing;

import java.sql.Clob;
import java.util.Date;

import com.eastnets.domain.BaseEntity;

/**
 * SwingEntity POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class SwingEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8366274768024179549L;
	// USED IN SERVICE, PRESENTATION.... TODO, RE-THINK ABOUT BETTER PLACE TO THEM, IT COULD BE BETTER TO PUT THEM IN BACKING BEAN!
	private String fromFinValueTimeHours = "00";
	private String fromFinValueTimeMinutes = "00";
	private String fromFinValueTimeSeconds = "00";

	private String toFinValueTimeHours = "23";
	private String toFinValueTimeMinutes = "59";
	private String toFinValueTimeSeconds = "59";

	private Date fromEAITimeStamp = new Date();
	private Date toEAITimeStamp = new Date();

	private Date fromFinValueDate ;
	private Date toFinValueDate;
	private Integer fromFinAmount;
	private Integer toFinAmount;
	private String isn;
	private String osn;
	private String mesgFormat;

	// USED IN DAO, SERVICE , PRESENTATION
	private UserLink userLink = new UserLink();
	private UserFin userFin = new UserFin();
	private UserStatus userStatus = new UserStatus();


	public UserLink getUserLink() {
		return userLink;
	}

	public void setUserLink(UserLink userLink) {
		this.userLink = userLink;
	}

	public UserFin getUserFin() {
		return userFin;
	}

	public void setUserFin(UserFin userFin) {
		this.userFin = userFin;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public Date getFromFinValueDate() {
		return fromFinValueDate;
	}

	public void setFromFinValueDate(Date fromFinValueDate) {
		this.fromFinValueDate = fromFinValueDate;
	}

	public Date getToFinValueDate() {
		return toFinValueDate;
	}

	public void setToFinValueDate(Date toFinValueDate) {
		this.toFinValueDate = toFinValueDate;
	}

	public Integer getFromFinAmount() {
		return fromFinAmount;
	}

	public void setFromFinAmount(Integer fromFinAmount) {
		this.fromFinAmount = fromFinAmount;
	}

	public Integer getToFinAmount() {
		return toFinAmount;
	}

	public void setToFinAmount(Integer toFinAmount) {
		this.toFinAmount = toFinAmount;
	}

	public Date getFromEAITimeStamp() {
		return fromEAITimeStamp;
	}

	public void setFromEAITimeStamp(Date fromEAITimeStamp) {
		this.fromEAITimeStamp = fromEAITimeStamp;
	}

	public Date getToEAITimeStamp() {
		return toEAITimeStamp;
	}

	public void setToEAITimeStamp(Date toEAITimeStamp) {
		this.toEAITimeStamp = toEAITimeStamp;
	}

	public String getIsn() {
		return isn;
	}

	public void setIsn(String isn) {
		this.isn = isn;
	}

	public String getOsn() {
		return osn;
	}

	public void setOsn(String osn) {
		this.osn = osn;
	}

	public String getMesgFormat() {
		return mesgFormat;
	}

	public void setMesgFormat(String mesgFormat) {
		this.mesgFormat = mesgFormat;
	}

	public String getFromFinValueTimeHours() {
		return fromFinValueTimeHours;
	}

	public void setFromFinValueTimeHours(String fromFinValueTimeHours) {
		this.fromFinValueTimeHours = fromFinValueTimeHours;
	}

	public String getFromFinValueTimeMinutes() {
		return fromFinValueTimeMinutes;
	}

	public void setFromFinValueTimeMinutes(String fromFinValueTimeMinutes) {
		this.fromFinValueTimeMinutes = fromFinValueTimeMinutes;
	}

	public String getFromFinValueTimeSeconds() {
		return fromFinValueTimeSeconds;
	}

	public void setFromFinValueTimeSeconds(String fromFinValueTimeSeconds) {
		this.fromFinValueTimeSeconds = fromFinValueTimeSeconds;
	}

	public String getToFinValueTimeHours() {
		return toFinValueTimeHours;
	}

	public void setToFinValueTimeHours(String toFinValueTimeHours) {
		this.toFinValueTimeHours = toFinValueTimeHours;
	}

	public String getToFinValueTimeMinutes() {
		return toFinValueTimeMinutes;
	}

	public void setToFinValueTimeMinutes(String toFinValueTimeMinutes) {
		this.toFinValueTimeMinutes = toFinValueTimeMinutes;
	}

	public String getToFinValueTimeSeconds() {
		return toFinValueTimeSeconds;
	}

	public void setToFinValueTimeSeconds(String toFinValueTimeSeconds) {
		this.toFinValueTimeSeconds = toFinValueTimeSeconds;
	}

	public String getDataStr() {
		Clob data = userFin.getData();
		String stringClob = null;
		if (data != null) {
			try {
				long i = 1;
				int clobLength = (int) data.length();
				stringClob = data.getSubString(i, clobLength);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return stringClob;
	}

}
