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
 * BICPerProfile POJO
 * @author EastNets
 * @since September 2, 2012
 */
public class BICPerProfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4243902777872009006L;
	public static Integer SELECTED_BIC_CODE = 1;
	public static Integer AVAILABLE_BIC_CODE = 0;
	
	private Long profileId;
	private String bicCode;
	private Integer isSelectedBIC;
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public String getBicCode() {
		return bicCode;
	}
	public void setBicCode(String bicCode) {
		this.bicCode = bicCode;
	}
	public Integer getIsSelectedBIC() {
		return isSelectedBIC;
	}
	public void setIsSelectedBIC(Integer isSelectedBIC) {
		this.isSelectedBIC = isSelectedBIC;
	}

	
	
}
