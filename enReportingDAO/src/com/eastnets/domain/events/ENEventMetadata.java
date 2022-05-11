package com.eastnets.domain.events;

import java.util.Date;

public class ENEventMetadata {

	private String sequenceNumber;
	
	private int aid;
	
	private String umidh;
	
	private String umidl;
	
	private String instNumber;
	
	private Date dateTime;
	
	private Date creationDate;
	
	private String seqNBR;
	
	private String tableName;
	
	private int operationMode;

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getUmidh() {
		return umidh;
	}

	public void setUmidh(String umidh) {
		this.umidh = umidh;
	}

	public String getUmidl() {
		return umidl;
	}

	public void setUmidl(String umidl) {
		this.umidl = umidl;
	}

	public String getInstNumber() {
		return instNumber;
	}

	public void setInstNumber(String instNumber) {
		this.instNumber = instNumber;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getSeqNBR() {
		return seqNBR;
	}

	public void setSeqNBR(String seqNBR) {
		this.seqNBR = seqNBR;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(int operationMode) {
		this.operationMode = operationMode;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
