package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WDKEYS")
public class WDAppeKey extends WDSuperKey {

	@EmbeddedId
	private AppendixPK appendixPK;

	@Column(name = "LAST_UPDATE")
	private Date lastUpdate;

	@Column(name = "STATUS")
	private Integer status;

	public AppendixPK getAppendixPK() {
		return appendixPK;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
		return "WDAppeKey [appendixPK=" + appendixPK + ", lastUpdate=" + lastUpdate + ", status=" + status + "]";
	}

}
