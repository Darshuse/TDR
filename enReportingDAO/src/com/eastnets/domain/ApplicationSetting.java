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

package com.eastnets.domain;

import java.io.Serializable;

/**
 * ArchiveSetting POJO
 * 
 * @author EastNets
 * @since September 20, 2012
 */
public class ApplicationSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7216321991563424887L;
	private Long id;
	private Long userID;
	private Long allianceID;
	private String fieldName;
	private String FieldValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Long getAllianceID() {
		return this.allianceID;
	}

	public void setAllianceID(Long allianceID) {
		this.allianceID = allianceID;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return FieldValue;
	}

	public void setFieldValue(String fieldValue) {
		FieldValue = fieldValue;
	}

}
