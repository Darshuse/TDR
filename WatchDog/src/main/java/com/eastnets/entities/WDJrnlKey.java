package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WDJRNLKEYS")
public class WDJrnlKey extends WDSuperKey {

	@EmbeddedId
	private JrnlPK jrnlPK;

	@Column(name = "JRNL_COMP_NAME")
	private String jrnlCompName;

	@Column(name = "JRNL_EVENT_NUM")
	private Integer jrnlEventNumber;

	@Column(name = "LAST_UPDATE")
	private Date lastUpdate;

	@Column(name = "PROCESS_STATUS")
	private int processStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "jrnl_date_time", nullable = true)
	private Date jrnlDateTime;

	public Date getJrnlDateTime() {
		return jrnlDateTime;
	}

	public void setJrnlDateTime(Date jrnlDateTime) {
		this.jrnlDateTime = jrnlDateTime;
	}

	public JrnlPK getJrnlPK() {
		return jrnlPK;
	}

	public void setJrnlPK(JrnlPK jrnlPK) {
		this.jrnlPK = jrnlPK;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jrnlCompName == null) ? 0 : jrnlCompName.hashCode());
		result = prime * result + ((jrnlEventNumber == null) ? 0 : jrnlEventNumber.hashCode());
		result = prime * result + ((jrnlPK == null) ? 0 : jrnlPK.hashCode());
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WDJrnlKey other = (WDJrnlKey) obj;
		if (jrnlCompName == null) {
			if (other.jrnlCompName != null)
				return false;
		} else if (!jrnlCompName.equals(other.jrnlCompName))
			return false;
		if (jrnlEventNumber == null) {
			if (other.jrnlEventNumber != null)
				return false;
		} else if (!jrnlEventNumber.equals(other.jrnlEventNumber))
			return false;
		if (jrnlPK == null) {
			if (other.jrnlPK != null)
				return false;
		} else if (!jrnlPK.equals(other.jrnlPK))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WDJrnlKey [jrnlPK=" + jrnlPK + ", jrnlCompName=" + jrnlCompName + ", jrnlEventNumber=" + jrnlEventNumber + ", lastUpdate=" + lastUpdate + "]";
	}

}
