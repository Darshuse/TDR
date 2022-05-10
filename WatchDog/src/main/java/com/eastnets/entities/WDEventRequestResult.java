package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "WDEVENTREQUESTRESULT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WDEventRequestResult extends WDSearchResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "AID")
	private Integer aid;

	@Column(name = "JRNL_REV_DATE_TIME")
	private Integer jrnlRevDateTime;

	@Column(name = "JRNL_SEQ_NBR")
	private Long jrnlSeqNumber;

	@Column(name = "REQUEST_ID")
	private Integer requestId;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PROCESSED")
	private Integer processed;

	@Column(name = "INSERT_TIME")
	private Date insertTime;

	@Transient
	private Date tempCreaDateTime;

	public Date getTempCreaDateTime() {
		return tempCreaDateTime;
	}

	public void setTempCreaDateTime(Date tempCreaDateTime) {
		this.tempCreaDateTime = tempCreaDateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getProcessed() {
		return processed;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aid == null) ? 0 : aid.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((insertTime == null) ? 0 : insertTime.hashCode());
		result = prime * result + ((jrnlRevDateTime == null) ? 0 : jrnlRevDateTime.hashCode());
		result = prime * result + ((jrnlSeqNumber == null) ? 0 : jrnlSeqNumber.hashCode());
		result = prime * result + ((processed == null) ? 0 : processed.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		WDEventRequestResult other = (WDEventRequestResult) obj;
		if (aid == null) {
			if (other.aid != null)
				return false;
		} else if (!aid.equals(other.aid))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (insertTime == null) {
			if (other.insertTime != null)
				return false;
		} else if (!insertTime.equals(other.insertTime))
			return false;
		if (jrnlRevDateTime == null) {
			if (other.jrnlRevDateTime != null)
				return false;
		} else if (!jrnlRevDateTime.equals(other.jrnlRevDateTime))
			return false;
		if (jrnlSeqNumber == null) {
			if (other.jrnlSeqNumber != null)
				return false;
		} else if (!jrnlSeqNumber.equals(other.jrnlSeqNumber))
			return false;
		if (processed == null) {
			if (other.processed != null)
				return false;
		} else if (!processed.equals(other.processed))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WDEventRequestResult [id=" + id + ", aid=" + aid + ", jrnlRevDateTime=" + jrnlRevDateTime + ", jrnlSeqNumber=" + jrnlSeqNumber + ", requestId=" + requestId + ", description=" + description + ", username=" + username + ", processed="
				+ processed + ", insertTime=" + insertTime + "]";
	}

	public JrnlPK getJrnlKey() {
		JrnlPK id = new JrnlPK();
		id.setAid(this.aid);
		id.setJrnlRevDateTime(this.jrnlRevDateTime);
		id.setJrnlSeqNumber(this.jrnlSeqNumber);

		return id;
	}

}
