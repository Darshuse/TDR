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
	private PKMesg pKMesg;

	@Column(name = "LAST_REQUEST")
	private Date lastRequest;

	@Column(name = "RP_NAME")
	private String rpName;

	public LiveMessage() {
	}

	public PKMesg getMesgPK() {
		return pKMesg;
	}

	public void setMesgPK(PKMesg pKMesg) {
		this.pKMesg = pKMesg;
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
		result = prime * result + ((pKMesg == null) ? 0 : pKMesg.hashCode());
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
		if (pKMesg == null) {
			if (other.pKMesg != null)
				return false;
		} else if (!pKMesg.equals(other.pKMesg))
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
		return "LiveMessage [pKMesg=" + pKMesg + ", lastRequest=" + lastRequest + ", rpName=" + rpName + "]";
	}

}