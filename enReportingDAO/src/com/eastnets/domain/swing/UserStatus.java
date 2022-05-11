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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * UserStatus POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class UserStatus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2758306176981886638L;
	private Integer id;
	private Date internalTimeStamp;
	private String unit;
	private String bic;
	private String standard;
	private String qualifier;
	private String category;
	private String eaiMessageId;
	private String eaiUniqueIdentifier;
	private Timestamp eaiTimeStamp;
	private String eaiStatus;
	private String eaiOperator;
	private String eaiErrorCode;
	private String eaiComment;
	private String eaiMessageSource;
	private String sourceName;
	private String externIdentifier;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInternalTimeStamp() {
		return internalTimeStamp;
	}

	public void setInternalTimeStamp(Date internalTimeStamp) {
		this.internalTimeStamp = internalTimeStamp;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEaiMessageId() {
		return eaiMessageId;
	}

	public void setEaiMessageId(String eaiMessageId) {
		this.eaiMessageId = eaiMessageId;
	}

	public String getEaiUniqueIdentifier() {
		return eaiUniqueIdentifier;
	}

	public void setEaiUniqueIdentifier(String eaiUniqueIdentifier) {
		this.eaiUniqueIdentifier = eaiUniqueIdentifier;
	}

	public Timestamp getEaiTimeStamp() {
		return eaiTimeStamp;
	}

	public void setEaiTimeStamp(Timestamp eaiTimeStamp) {
		this.eaiTimeStamp = eaiTimeStamp;
	}

	public String getEaiStatus() {
		return eaiStatus;
	}

	public void setEaiStatus(String eaiStatus) {
		this.eaiStatus = eaiStatus;
	}

	public String getEaiOperator() {
		return eaiOperator;
	}

	public void setEaiOperator(String eaiOperator) {
		this.eaiOperator = eaiOperator;
	}

	public String getEaiErrorCode() {
		return eaiErrorCode;
	}

	public void setEaiErrorCode(String eaiErrorCode) {
		this.eaiErrorCode = eaiErrorCode;
	}

	public String getEaiComment() {
		return eaiComment;
	}

	public void setEaiComment(String eaiComment) {
		this.eaiComment = eaiComment;
	}

	public String getEaiMessageSource() {
		return eaiMessageSource;
	}

	public void setEaiMessageSource(String eaiMessageSource) {
		this.eaiMessageSource = eaiMessageSource;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getExternIdentifier() {
		return externIdentifier;
	}

	public void setExternIdentifier(String externIdentifier) {
		this.externIdentifier = externIdentifier;
	}

}
