package com.eastnets.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RAUDITLOG")
@Cacheable(value = false)
public class AuditLog implements Serializable {

	private static final long serialVersionUID = 2657184912450896142L;

	@Column(name = "id", unique = true, nullable = false)
	@Id
	@SequenceGenerator(name = "AUDIT_LOG_EVENTID_GENERATOR", sequenceName = "RAUDITLOG_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_LOG_EVENTID_GENERATOR")
	private Integer id;

	@Column(name = "loginame")
	private String loginame;

	@Column(name = "program_name")
	private String programName;

	@Column(name = "event")
	private String event;

	@Column(name = "action")
	private String action;

	@Column(name = "timestamp")
	private LocalDateTime timestamp;

	@Column(name = "ipaddress")
	private String ipAddress;

	public AuditLog() {
	}

	public AuditLog(String loginame, String programName, String event, String action, LocalDateTime timestamp,
			String ipAddress) {
		this.loginame = loginame;
		this.programName = programName;
		this.event = event;
		this.action = action;
		this.timestamp = timestamp;
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString() {
		return "AuditLog [id=" + id + ", loginame=" + loginame + ", programName=" + programName + ", event=" + event
				+ ", action=" + action + ", timestamp=" + timestamp + ", ipAddress=" + ipAddress + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginame() {
		return loginame;
	}

	public void setLoginame(String loginame) {
		this.loginame = loginame;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;

	}

}
