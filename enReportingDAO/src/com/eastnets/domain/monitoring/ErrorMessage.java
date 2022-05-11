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

package com.eastnets.domain.monitoring;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * ErrorMessage POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class ErrorMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4406014043202068901L;
	private String applicationName;
	private String errID;
	private Date timeStamp;
	private String errLevel;
	private String errModule;
	private String errMsg1;
	private String errMsg2;
	private String description;

	public String getApplicationName() {
		return applicationName;
	}

	public String getApplicationNameFormatted() {
		return StringUtils.capitalize(applicationName.toLowerCase());
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getErrID() {
		return errID;
	}

	public void setErrID(String errID) {
		this.errID = errID;
	}

	public String getErrLevel() {
		return errLevel;
	}

	public void setErrLevel(String errLevel) {
		this.errLevel = errLevel;
	}

	public String getErrModule() {
		return errModule;
	}

	public void setErrModule(String errModule) {
		this.errModule = errModule;
	}

	public String getErrMsg1() {
		return errMsg1;
	}

	public void setErrMsg1(String errMsg1) {
		this.errMsg1 = errMsg1;
	}

	public String getErrMsg2() {
		return errMsg2;
	}

	public void setErrMsg2(String errMsg2) {
		this.errMsg2 = errMsg2;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
