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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WDEMAILNOTIFICATION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WDEmailNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "AID")
	private Long aid;

	@Column(name = "UMIDL")
	private Long umidl;

	@Column(name = "UMIDH")
	private Long umidh;

	@Column(name = "MSGTYPE")
	private String messageType;

	@Column(name = "WDID")
	private Integer wdID;

	@Column(name = "PROCESS_STATUS")
	private Integer processStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESG_CREA_DATE_TIME", nullable = true)
	private Date mesgCreaDateTime;

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Long getUmidl() {
		return umidl;
	}

	public void setUmidl(Long umidl) {
		this.umidl = umidl;
	}

	public Long getUmidh() {
		return umidh;
	}

	public void setUmidh(Long umidh) {
		this.umidh = umidh;
	}

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

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

}
