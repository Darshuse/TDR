package com.eastnets.domain.jrnl;

import java.io.Serializable;
import java.util.Date;

public class JournalShowDetailsProcedureParameters implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1112958931475546460L;
	private int aid;
	private long revisionDateTime;
	private long sequenceNumber;
	private Date dateTime;
	
	
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	
	public long getRevisionDateTime() {
		return revisionDateTime;
	}
	public void setRevisionDateTime(long revisionDateTime) {
		this.revisionDateTime = revisionDateTime;
	}
	
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
}
