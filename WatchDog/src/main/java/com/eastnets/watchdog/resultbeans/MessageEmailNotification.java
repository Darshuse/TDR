package com.eastnets.watchdog.resultbeans;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageEmailNotification extends EmailNotification {

	private SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Integer id;

	private String description;

	private String username;

	private Long aid;

	private Long umidhl;

	private Long umidh;

	private Integer status;

	private String messageType;

	private Date mesgCreaDateTime;

	private String mesgRelTrnRef = null;

	private String mesgUumid;

	private BigDecimal mesgUumidSuffix = BigDecimal.ZERO;

	private String mesgSenderX1;

	private String xReceiverX1;

	private String mesgSubFormat;

	private String mesgNature;

	private String mesgTrnRef = null;

	private Date xFinValueDate = null;

	private BigDecimal xFinAmount = null;

	private String xFinCcy = null;

	public String mesgText = null;

	private String mesgSyntaxTableVer;

	public MessageEmailNotification(Integer id, String description, String username, Long aid, Long umidhl, Long umidh, Integer status, String messageType, Date mesgCreaDateTime, String mesgRelTrnRef, String mesgUumid, BigDecimal mesgUumidSuffix,
			String mesgSenderX1, String xReceiverX1, String mesgSubFormat, String mesgNature, String mesgTrnRef, Date xFinValueDate, BigDecimal xFinAmount, String xFinCcy, String mesgText, String mesgSyntaxTableVer) {
		this.id = id;
		this.description = description;
		this.username = username;
		this.aid = aid;
		this.umidhl = umidhl;
		this.umidh = umidh;
		this.status = status;
		this.messageType = messageType;
		this.mesgCreaDateTime = mesgCreaDateTime;
		this.mesgRelTrnRef = mesgRelTrnRef;
		this.mesgUumid = mesgUumid;
		this.mesgUumidSuffix = mesgUumidSuffix;
		this.mesgSenderX1 = mesgSenderX1;
		this.xReceiverX1 = xReceiverX1;
		this.mesgSubFormat = mesgSubFormat;
		this.mesgNature = mesgNature;
		this.mesgTrnRef = mesgTrnRef;
		this.xFinValueDate = xFinValueDate;
		this.xFinAmount = xFinAmount;
		this.xFinCcy = xFinCcy;
		this.mesgText = mesgText;
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
	}

	public String getMesgSyntaxTableVer() {
		return mesgSyntaxTableVer;
	}

	public void setMesgSyntaxTableVer(String mesgSyntaxTableVer) {
		this.mesgSyntaxTableVer = mesgSyntaxTableVer;
	}

	public String getMesgCreaDateTimeFormate() {
		return fullDateTimeFormat.format(mesgCreaDateTime);
	}

	public String getFinValueDateFormate() {
		if (xFinValueDate != null) {
			return fullDateTimeFormat.format(xFinValueDate);
		}
		return fullDateTimeFormat.format(mesgCreaDateTime);
	}

	public String getMesgText() {
		return mesgText;
	}

	public void setMesgText(String mesgText) {
		this.mesgText = mesgText;
	}

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

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getMesgRelTrnRef() {
		if (mesgRelTrnRef == null)
			return "";
		return mesgRelTrnRef;
	}

	public void setMesgRelTrnRef(String mesgRelTrnRef) {
		this.mesgRelTrnRef = mesgRelTrnRef;
	}

	public String getMesgUumid() {
		return mesgUumid;
	}

	public void setMesgUumid(String mesgUumid) {
		this.mesgUumid = mesgUumid;
	}

	public BigDecimal getMesgUumidSuffix() {
		return mesgUumidSuffix;
	}

	public void setMesgUumidSuffix(BigDecimal mesgUumidSuffix) {
		this.mesgUumidSuffix = mesgUumidSuffix;
	}

	public String getMesgSenderX1() {
		return mesgSenderX1;
	}

	public void setMesgSenderX1(String mesgSenderX1) {
		this.mesgSenderX1 = mesgSenderX1;
	}

	public String getxReceiverX1() {
		return xReceiverX1;
	}

	public void setxReceiverX1(String xReceiverX1) {
		this.xReceiverX1 = xReceiverX1;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgNature() {
		return mesgNature;
	}

	public void setMesgNature(String mesgNature) {
		this.mesgNature = mesgNature;
	}

	public String getMesgTrnRef() {
		return mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		this.mesgTrnRef = mesgTrnRef;
	}

	public Date getxFinValueDate() {
		return xFinValueDate;
	}

	public void setxFinValueDate(Date xFinValueDate) {
		this.xFinValueDate = xFinValueDate;
	}

	public BigDecimal getxFinAmount() {
		return xFinAmount;
	}

	public void setxFinAmount(BigDecimal xFinAmount) {
		this.xFinAmount = xFinAmount;
	}

	public String getxFinCcy() {
		return xFinCcy;
	}

	public void setxFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MessageEmailNotification: id=" + id + ", description=" + description + ", username=" + username + ", aid=" + aid + ", umidhl=" + umidhl + ", umidh=" + umidh + ", messageType=" + messageType + ", mesgCreaDateTime=" + mesgCreaDateTime
				+ ", mesgRelTrnRef=" + mesgRelTrnRef + ", mesgUumid=" + mesgUumid + ", mesgUumidSuffix=" + mesgUumidSuffix + ", mesgSenderX1=" + mesgSenderX1 + ", xReceiverX1=" + xReceiverX1 + ", mesgSubFormat=" + mesgSubFormat + ", mesgNature="
				+ mesgNature + ", mesgTrnRef=" + mesgTrnRef + ", xFinValueDate=" + xFinValueDate + ", xFinAmount=" + xFinAmount + ", xFinCcy=" + xFinCcy;
	}

}
