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

import com.eastnets.dao.common.Constants;

/**
 * ArchiveLog POJO
 * @author EastNets
 * @since October 7, 2012
 */
public class ArchiveLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6617604836382869626L;
	private Long id;
	private Long moduleId;
	private Date creationTime;
	private Long aid;
	private Long status;
	private String logPath;
	private String logName;
	private Date dateFrom;
	private Date dateTo;
	private String statusDescription;
	private String logText;
	
	public Long getId() {
		return id;
	}
	
	public String getIdString() {
		if(id == null)
			return "No Records";
		return id.toString();
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Long getAid() {
		return aid;
	}
	public String getAidString() {
		if(aid == null){
			return " ";
		}
			
		return aid.toString();
	}
	
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public void setAid(Long aid) {
		this.aid = aid;
	}
	public Long getStatus() {
		if(status == null){
			return -1L;
		}
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public String getLogName(){
		return logName;
	}
	
	public void setLogName(String logName){
		this.logName =  logName;
	}

	public Date getDateFrom() {
		return dateFrom;
	}
	
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}	
	public String getLogText() {
		return logText;
	}
	public void setLogText(String logText) {
		this.logText = logText;
	}
	public String getStatusDescription() {
		
		if(status == null){
			return "None";
		}
		switch(Integer.parseInt(status.toString()))
		{
		case (int)Constants.ARCHIVE_STATUS_RUNNING:
			return "Running";
			//TODO Sameer: return Utils.getKeyValueInBundle("i18n", "Archive_Log_Running");
		case (int)Constants.ARCHIVE_STATUS_SUCCESS:
			return "Success";
			//TODO Sameer: return Utils.getKeyValueInBundle("i18n", "Archive_Log_Success");
		case (int)Constants.ARCHIVE_STATUS_FAILED:
			return "Failed";
			//TODO Sameer: return Utils.getKeyValueInBundle("i18n", "Archive_Log_Failed");
		}
		return statusDescription;
	}
}
