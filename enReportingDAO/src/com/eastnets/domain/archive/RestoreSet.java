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

package com.eastnets.domain.archive;

import java.io.Serializable;
import java.util.Date;

/**
 * RestoreSet POJO
 * @author EastNets
 * @since October 4, 2012
 */
public class RestoreSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6332062712849959965L;
	private Long aid;
	private Long setType;
	private Long mesgCount;
	private Long totalCount;
	private Long setId;
	
	private String version;
	private String size;
	
	private String name;
	
	private Date restoredAt;
	private Date minDate;
	private Date maxDate;
	
	public RestoreSet()
	{
		this.name = " ";
		this.size = "nf";
		this.version = " ";
	}
	public Long getAid() {
		return aid;
	}
	public void setAid(Long aid) {
		this.aid = aid;
	}
	public Long getSetType() {
		return setType;
	}
	public void setSetType(Long setType) {
		this.setType = setType;
	}
	public Long getMesgCount() {
		return mesgCount;
	}
	public void setMesgCount(Long mesgCount) {
		this.mesgCount = mesgCount;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public Long getSetId() {
		return setId;
	}
	public void setSetId(Long setId) {
		this.setId = setId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRestoredAt() {
		return this.restoredAt;
	}
	public void setRestoredAt(Date restoredAt) {
		this.restoredAt = restoredAt;
	}
	public Date getMinDate() {
		return minDate;
	}
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSize() {
		
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
}
