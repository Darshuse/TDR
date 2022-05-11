package com.eastnets.domain.viewer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotifierMessage {
	private static final long serialVersionUID = -7346150922764901227L;

	private DataSource dataSource;
	private String mesgSenderX1;
	private String mesgUserReferenceText;
	private String mesgSenderSwiftAddress;
	private Date mailAttemptsDate;
	private Date confirmAttemptsDate;
	private String queueName;
	private String reasonCode;
	private Integer mailAtempt = 0;
	private Integer confirmAtempt = 0;
	private NotifyStatus notifyStatus;
	private boolean sendMail;
	private boolean writeConfirmation;
	private String settlementMethod;
	private String statusCode;

	// for SSA
	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private Date mesgCreaDateTime;
	private String xFinCcy;

	private String mesgID;
	private String uetr;
	private String insertionMessageDataTime;
	private String messageText;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(Integer mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public Integer getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(Integer mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

	public String getMesgSenderX1() {
		return mesgSenderX1;
	}

	public void setMesgSenderX1(String mesgSenderX1) {
		this.mesgSenderX1 = mesgSenderX1;
	}

	public String getMesgUserReferenceText() {
		return mesgUserReferenceText;
	}

	public void setMesgUserReferenceText(String mesgUserReferenceText) {
		if (mesgUserReferenceText == null) {
			mesgUserReferenceText = "";
		}
		this.mesgUserReferenceText = mesgUserReferenceText;
	}

	public Date getMesgCreaDateTimeOnDB() {
		return mesgCreaDateTime;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Date getMesgCreaDateTime() {
		if (mesgCreaDateTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(mesgCreaDateTime);
		return new java.sql.Date(cal.getTime().getTime());
	}

	public String getMesgCreaDateTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(mesgCreaDateTime);
	}

	public String getMailAttemptsDateTimeStr() {
		if (mailAttemptsDate == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(mailAttemptsDate);
	}//

	public String getConfirmAttemptsDateTimeStr() {
		if (confirmAttemptsDate == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(confirmAttemptsDate);
	}//

	public String getMesgCreaTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(mesgCreaDateTime);
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getMesgSenderSwiftAddress() {
		return mesgSenderSwiftAddress;
	}

	public void setMesgSenderSwiftAddress(String mesgSenderSwiftAddress) {
		this.mesgSenderSwiftAddress = mesgSenderSwiftAddress;
	}

	public Integer getMailAtempt() {
		return mailAtempt;
	}

	public void setMailAtempt(Integer mailAtempt) {
		this.mailAtempt = mailAtempt;
	}

	public Integer getConfirmAtempt() {
		return confirmAtempt;
	}

	public void setConfirmAtempt(Integer confirmAtempt) {
		this.confirmAtempt = confirmAtempt;
	}

	public NotifyStatus getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(NotifyStatus notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public boolean isWriteConfirmation() {
		return writeConfirmation;
	}

	public void setWriteConfirmation(boolean writeConfirmation) {
		this.writeConfirmation = writeConfirmation;
	}

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}

	public Date getMailAttemptsDate() {
		return mailAttemptsDate;
	}

	public void setMailAttemptsDate(Date mailAttemptsDate) {
		this.mailAttemptsDate = mailAttemptsDate;
	}

	public Date getConfirmAttemptsDate() {
		return confirmAttemptsDate;
	}

	public void setConfirmAttemptsDate(Date confirmAttemptsDate) {
		this.confirmAttemptsDate = confirmAttemptsDate;
	}

	public String getxFinCcy() {
		return xFinCcy;
	}

	public void setxFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}

	public String getUetr() {
		return uetr;
	}

	public void setUetr(String uetr) {
		this.uetr = uetr;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getMesgID() {
		return mesgID;
	}

	public void setMesgID(String mesgID) {
		this.mesgID = mesgID;
	}

	public String getInsertionMessageDataTime() {
		return insertionMessageDataTime;
	}

	public void setInsertionMessageDataTime(String insertionMessageDataTime) {
		this.insertionMessageDataTime = insertionMessageDataTime;
	}

	public String getSettlementMethod() {
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}

}
