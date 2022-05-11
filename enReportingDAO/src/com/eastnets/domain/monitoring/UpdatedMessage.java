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

/**
 * UpdatedMessage POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class UpdatedMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6222050408703493481L;
	private String lastFileKeyID;
	private String ID;
	private String insertTime;
	private Date timeStamp;
	private String elapsedTime;
	private String newMsgCount;
	private String updateMsgCount;
	private String onTimeCount;
	private String notifiedMsgCount;
	private String refreshCount;
	private String overrun;
	private String status;
	private String errorCount;
	private String failedCount;
	private String warningCount;
	private String jrnlMsgCount;
	private String origin;
	private int totalTime;

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getNewMsgCount() {
		return newMsgCount;
	}

	public void setNewMsgCount(String newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	public String getUpdateMsgCount() {
		return updateMsgCount;
	}

	public void setUpdateMsgCount(String updateMsgCount) {
		this.updateMsgCount = updateMsgCount;
	}

	public String getOnTimeCount() {
		return onTimeCount;
	}

	public void setOnTimeCount(String onTimeCount) {
		this.onTimeCount = onTimeCount;
	}

	public String getNotifiedMsgCount() {
		return notifiedMsgCount;
	}

	public void setNotifiedMsgCount(String notifiedMsgCount) {
		this.notifiedMsgCount = notifiedMsgCount;
	}

	public String getRefreshCount() {
		return refreshCount;
	}

	public void setRefreshCount(String refreshCount) {
		this.refreshCount = refreshCount;
	}

	public String getOverrun() {
		return overrun;
	}

	public void setOverrun(String overrun) {
		this.overrun = overrun;
	}

	public String getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
	}

	public String getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(String failedCount) {
		this.failedCount = failedCount;
	}

	public String getWarningCount() {
		return warningCount;
	}

	public void setWarningCount(String warningCount) {
		this.warningCount = warningCount;
	}

	public String getJrnlMsgCount() {
		return jrnlMsgCount;
	}

	public void setJrnlMsgCount(String jrnlMsgCount) {
		this.jrnlMsgCount = jrnlMsgCount;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getLastFileKeyID() {
		return lastFileKeyID;
	}

	public void setLastFileKeyID(String lastFileKeyID) {
		this.lastFileKeyID = lastFileKeyID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	
}
