package com.eastnets.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "WDEMAILNOTIFICATION")
public class WDEmailNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailSeq")
	@SequenceGenerator(name = "emailSeq", sequenceName = "WDEMAILNOTIFICATION_ID", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "AID")
	private Integer aid;

	@Column(name = "UMIDL")
	private Integer umidl;

	@Column(name = "UMIDH")
	private Integer umidh;

	@Column(name = "MSGTYPE")
	private String messageType;

	@Column(name = "WDID")
	private Integer wdID;

	@Column(name = "STATUS")
	private Integer status;

	public WDEmailNotification() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getUmidhl() {
		return umidl;
	}

	public void setUmidhl(Integer umidhl) {
		this.umidl = umidhl;
	}

	public Integer getUmidh() {
		return umidh;
	}

	public void setUmidh(Integer umidh) {
		this.umidh = umidh;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Integer getWdID() {
		return wdID;
	}

	public void setWdID(Integer wdID) {
		this.wdID = wdID;
	}

	public Integer getUmidl() {
		return umidl;
	}

	public void setUmidl(Integer umidl) {
		this.umidl = umidl;
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
		result = prime * result + ((aid == null) ? 0 : aid.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + ((umidh == null) ? 0 : umidh.hashCode());
		result = prime * result + ((umidl == null) ? 0 : umidl.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((wdID == null) ? 0 : wdID.hashCode());
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
		WDEmailNotification other = (WDEmailNotification) obj;
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
		if (messageType == null) {
			if (other.messageType != null)
				return false;
		} else if (!messageType.equals(other.messageType))
			return false;
		if (umidh == null) {
			if (other.umidh != null)
				return false;
		} else if (!umidh.equals(other.umidh))
			return false;
		if (umidl == null) {
			if (other.umidl != null)
				return false;
		} else if (!umidl.equals(other.umidl))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (wdID == null) {
			if (other.wdID != null)
				return false;
		} else if (!wdID.equals(other.wdID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WDEmailNotification [id=" + id + ", description=" + description + ", username=" + username + ", aid="
				+ aid + ", umidl=" + umidl + ", umidh=" + umidh + ", messageType=" + messageType + ", wdID=" + wdID
				+ "]";
	}

}
