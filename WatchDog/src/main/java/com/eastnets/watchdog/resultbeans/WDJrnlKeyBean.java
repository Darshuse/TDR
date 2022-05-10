package com.eastnets.watchdog.resultbeans;

import java.util.Date;

public class WDJrnlKeyBean {

	private Integer aid;

	private Integer jrnlRevDateTime;

	private Long jrnlSeqNumber;
	private String jrnlCompName;

	private Integer jrnlEventNumber;

	private Date lastUpdate;

	private int processStatus;

	private Date jrnlDateTime;

	public WDJrnlKeyBean(Integer aid, Integer jrnlRevDateTime, Long jrnlSeqNumber, String jrnlCompName, Integer jrnlEventNumber, Date lastUpdate) {
		super();
		this.aid = aid;
		this.jrnlRevDateTime = jrnlRevDateTime;
		this.jrnlSeqNumber = jrnlSeqNumber;
		this.jrnlCompName = jrnlCompName;
		this.jrnlEventNumber = jrnlEventNumber;
		this.lastUpdate = lastUpdate;
	}

	public WDJrnlKeyBean(Integer aid, Integer jrnlRevDateTime, Long jrnlSeqNumber, String jrnlCompName, Integer jrnlEventNumber, Date lastUpdate, Date jrnlDateTime) {
		super();
		this.aid = aid;
		this.jrnlRevDateTime = jrnlRevDateTime;
		this.jrnlSeqNumber = jrnlSeqNumber;
		this.jrnlCompName = jrnlCompName;
		this.jrnlEventNumber = jrnlEventNumber;
		this.lastUpdate = lastUpdate;
		this.jrnlDateTime = jrnlDateTime;
	}

	public Date getJrnlDateTime() {
		return jrnlDateTime;
	}

	public void setJrnlDateTime(Date jrnlDateTime) {
		this.jrnlDateTime = jrnlDateTime;
	}

	public String getJrnlCompName() {
		return jrnlCompName;
	}

	public void setJrnlCompName(String jrnlCompName) {
		this.jrnlCompName = jrnlCompName;
	}

	public Integer getJrnlEventNumber() {
		return jrnlEventNumber;
	}

	public void setJrnlEventNumber(Integer jrnlEventNumber) {
		this.jrnlEventNumber = jrnlEventNumber;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getJrnlRevDateTime() {
		return jrnlRevDateTime;
	}

	public void setJrnlRevDateTime(Integer jrnlRevDateTime) {
		this.jrnlRevDateTime = jrnlRevDateTime;
	}

	public Long getJrnlSeqNumber() {
		return jrnlSeqNumber;
	}

	public void setJrnlSeqNumber(Long jrnlSeqNumber) {
		this.jrnlSeqNumber = jrnlSeqNumber;
	}

}
