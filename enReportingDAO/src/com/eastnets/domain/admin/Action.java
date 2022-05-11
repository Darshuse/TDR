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
 * Action POJO
 * @author EastNets
 * @since August 29, 2012
 */
public class Action implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1160536950579165666L;
	private Long actionId;
	private String actionName;
	private Integer moduleId;
	private Integer profileId;
	private String strActionId;

	
	public String getStrActionId() {
		return strActionId;
	}

	public void setStrActionId(String strActionId) {
		this.strActionId = strActionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
		
		if(actionId != null)
			setStrActionId(actionId.toString());
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
}
