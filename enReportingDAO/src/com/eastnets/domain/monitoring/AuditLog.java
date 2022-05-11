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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AuditLog POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class AuditLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2943090938479863798L;
	private String id;
	private String loginName;
	private String programName;
	private String event;
	private String action;
	private Date timeStamp;
	private String  actionDetails;
	private String ipAddress;
	private boolean showDetails;
	private boolean overflowSize;

	private List<AuditLogDetails> auditLogDetailsList = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}	 

	public String getActionDetails() {
		return actionDetails;
	}

	public boolean isOverflowSize() {
		if(auditLogDetailsList.isEmpty())
			return false;
		overflowSize= (auditLogDetailsList.size() >= 4) ? true : false;
		
		if(overflowSize == false){
			for(AuditLogDetails auditLogDetail:auditLogDetailsList){
				if(auditLogDetail.getNewValue() != null && !auditLogDetail.getNewValue().isEmpty()){
					if(auditLogDetail.getNewValue().length() >=1300){
						overflowSize=true;
						break;
					}
				}
				if(auditLogDetail.getOldValue() != null && !auditLogDetail.getOldValue().isEmpty()){
					if(auditLogDetail.getOldValue().length() >=1300){
						overflowSize=true;
						break;
					}
				}
				
				
				
			}
		}

		return overflowSize;
	} 

	public void setOverflowSize(boolean overflowSize) {
		this.overflowSize = overflowSize;
	}

	public void setActionDetails(String actionDetails) {
		this.actionDetails = actionDetails;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public List<AuditLogDetails> getAuditLogDetailsList() {
		return auditLogDetailsList;
	}

	public void setAuditLogDetailsList(List<AuditLogDetails> auditLogDetailsList) {
		this.auditLogDetailsList = auditLogDetailsList;
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

}
