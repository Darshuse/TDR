package com.eastnets.domain.watchdog;

import java.io.Serializable;
import java.sql.Timestamp;



public class Annotaion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3182718858261948010L;
	
	private Long id;
	private Long sysID;
	private Integer requestType;
	private Long processed;
	private String processedBy;
	private Timestamp processedAt;
	private String userComment;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSysID() {
		return sysID;
	}
	public void setSysID(Long sysID) {
		this.sysID = sysID;
	}
	public Integer getRequestType() {
		return requestType;
	}
	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}
	public Long getProcessed() {
		return processed;
	}
	public void setProcessed(Long processed) {
		this.processed = processed;
	}
	public String getProcessedBy() {
		return processedBy;
	}
	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}
	public Timestamp getProcessedAt() {
		return processedAt;
	}
	public void setProcessedAt(Timestamp processedAt) {
		this.processedAt = processedAt;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	
	@Override
	public String toString(){
		String retValue="";
		
		retValue = String.format("By: %s, At: %s, \n%s", this.processedBy,this.processedAt.toString(),this.userComment);
		return retValue;
	}
	
}
