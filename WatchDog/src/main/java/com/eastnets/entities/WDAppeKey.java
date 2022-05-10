package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WDKEYS")
public class WDAppeKey extends WDSuperKey {

	@EmbeddedId
	private AppendixPK appendixPK;

	@Column(name = "LAST_UPDATE")
	private Date lastUpdate;

	@Column(name = "PROCESS_STATUS")
	private int processStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG", nullable = true)
	private Date mesgCreaDateTime;

	public AppendixPK getAppendixPK() {
		return appendixPK;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public void setAppendixPK(AppendixPK appendixPK) {
		this.appendixPK = appendixPK;
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
		result = prime * result + ((appendixPK == null) ? 0 : appendixPK.hashCode());
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
		WDAppeKey other = (WDAppeKey) obj;
		if (appendixPK == null) {
			if (other.appendixPK != null)
				return false;
		} else if (!appendixPK.equals(other.appendixPK)) {
			return false;
		}
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WDAppeKey [appendixPK=" + appendixPK + ", lastUpdate=" + lastUpdate + "]";
	}

}
