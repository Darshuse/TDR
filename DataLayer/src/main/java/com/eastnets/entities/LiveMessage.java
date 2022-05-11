package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LDLIVELIST")
public class LiveMessage {

	@EmbeddedId
	private MesgPK mesgPK;

	@Column(name = "LAST_REQUEST")
	private Date lastRequest;

	@Column(name = "RP_NAME")
	private String rpName;

	public LiveMessage() {
	}

	public MesgPK getMesgPK() {
		return mesgPK;
	}

	public void setMesgPK(MesgPK mesgPK) {
		this.mesgPK = mesgPK;
	}

	public Date getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(Date lastRequest) {
		this.lastRequest = lastRequest;
	}

	public String getRpName() {
		return rpName;
	}

	public void setRpName(String rpName) {
		this.rpName = rpName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastRequest == null) ? 0 : lastRequest.hashCode());
		result = prime * result + ((mesgPK == null) ? 0 : mesgPK.hashCode());
		result = prime * result + ((rpName == null) ? 0 : rpName.hashCode());
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
		LiveMessage other = (LiveMessage) obj;
		if (lastRequest == null) {
			if (other.lastRequest != null)
				return false;
		} else if (!lastRequest.equals(other.lastRequest))
			return false;
		if (mesgPK == null) {
			if (other.mesgPK != null)
				return false;
		} else if (!mesgPK.equals(other.mesgPK))
			return false;
		if (rpName == null) {
			if (other.rpName != null)
				return false;
		} else if (!rpName.equals(other.rpName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LiveMessage [mesgPK=" + mesgPK + ", lastRequest=" + lastRequest + ", rpName=" + rpName + "]";
	}

}