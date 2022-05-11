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

package com.eastnets.domain.admin;

import java.io.Serializable;

/**
 * UnitPerProfile POJO
 * @author EastNets
 * @since September 3, 2012
 */
public class UnitPerProfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4550754526648610777L;
	public static Integer SELECTED_UNIT = 1;
	public static Integer AVAILABLE_UNIT = 0;
	
	private Long profileId;
	private String unit;
	private Integer isSelectedBIC;	

	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getIsSelectedBIC() {
		return isSelectedBIC;
	}
	public void setIsSelectedBIC(Integer isSelectedBIC) {
		this.isSelectedBIC = isSelectedBIC;
	}




}
