package com.eastnets.watchdog.resultbeans;

import java.util.Date;

public class EventEmailNotification extends EmailNotification {

	private Integer id;

	private String description;

	private String username;

	private Long aid;

	private Long umidhl;

	private Long umidh;

	private Integer status;

	private String messageType;

	private String jrnlEventClass;

	private Integer jrnlEventNum = 0;

	private String jrnlEventName;

	private String jrnlCompName;

	private String jrnlEventServerity;

	private String jrnlApplServName;

	private String jrnlOperNickName;

	private String jrnlHostname;

	private String jrnlMergedText;

	private Date jrnlDateTime;

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Long getUmidhl() {
		return umidhl;
	}

	public void setUmidhl(Long umidhl) {
		this.umidhl = umidhl;
	}

	public Long getUmidh() {
		return umidh;
	}

	public void setUmidh(Long umidh) {
		this.umidh = umidh;
	}

	public EventEmailNotification(Integer id, String description, String username, Long aid, Long umidhl, Long umidh, Integer status, String jrnlEventClass, Integer jrnlEventNum, String jrnlEventName, String jrnlCompName, String jrnlEventServerity,
			String jrnlApplServName, String jrnlOperNickName, String jrnlHostname, String jrnlMergedText, Date jrnlDateTime) {
		this.id = id;
		this.description = description;
		this.username = username;
		this.aid = aid;
		this.umidhl = umidhl;
		this.umidh = umidh;
		this.status = status;
		this.jrnlEventClass = jrnlEventClass;
		this.jrnlEventNum = jrnlEventNum;
		this.jrnlEventName = jrnlEventName;
		this.jrnlCompName = jrnlCompName;
		this.jrnlEventServerity = jrnlEventServerity;
		this.jrnlApplServName = jrnlApplServName;
		this.jrnlOperNickName = jrnlOperNickName;
		this.jrnlHostname = jrnlHostname;
		this.jrnlMergedText = jrnlMergedText;
		this.jrnlDateTime = jrnlDateTime;
	}

	public Date getJrnlDateTime() {
		return jrnlDateTime;
	}

	public void setJrnlDateTime(Date jrnlDateTime) {
		this.jrnlDateTime = jrnlDateTime;
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

	public String getJrnlEventClass() {
		return jrnlEventClass;
	}

	public void setJrnlEventClass(String jrnlEventClass) {
		this.jrnlEventClass = jrnlEventClass;
	}

	public Integer getJrnlEventNum() {
		return jrnlEventNum;
	}

	public void setJrnlEventNum(Integer jrnlEventNum) {
		this.jrnlEventNum = jrnlEventNum;
	}

	public String getJrnlEventName() {
		return jrnlEventName;
	}

	public void setJrnlEventName(String jrnlEventName) {
		this.jrnlEventName = jrnlEventName;
	}

	public String getJrnlCompName() {
		return jrnlCompName;
	}

	public void setJrnlCompName(String jrnlCompName) {
		this.jrnlCompName = jrnlCompName;
	}

	public String getJrnlEventServerity() {
		return jrnlEventServerity;
	}

	public void setJrnlEventServerity(String jrnlEventServerity) {
		this.jrnlEventServerity = jrnlEventServerity;
	}

	public String getJrnlApplServName() {
		return jrnlApplServName;
	}

	public void setJrnlApplServName(String jrnlApplServName) {
		this.jrnlApplServName = jrnlApplServName;
	}

	public String getJrnlOperNickName() {
		return jrnlOperNickName;
	}

	public void setJrnlOperNickName(String jrnlOperNickName) {
		this.jrnlOperNickName = jrnlOperNickName;
	}

	public String getJrnlHostname() {
		return jrnlHostname;
	}

	public void setJrnlHostname(String jrnlHostname) {
		this.jrnlHostname = jrnlHostname;
	}

	public String getJrnlMergedText() {
		return jrnlMergedText;
	}

	public void setJrnlMergedText(String jrnlMergedText) {
		this.jrnlMergedText = jrnlMergedText;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
