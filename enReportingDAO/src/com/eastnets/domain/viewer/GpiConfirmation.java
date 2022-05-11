package com.eastnets.domain.viewer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GpiConfirmation implements Comparable<GpiConfirmation> {
	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private String statusCode;
	private String reasonCode;
	private String forwardedTo;
	private String statusOriginator;
	private String nakCode;
	private Date mesgCreaDateTime;
	private Integer timeZoneOffset;
	private String msgTrnRef;
	private String sendDate;
	private String msgSendAmount;
	private String msgSendCurr;
	private String mesgType;
	private String creditedAmount;
	private String creditedCur;
	private String mesgExchangeRate;
	private String mesgSla;
	private String mesgSubFormat;
	private String mesgCharges;
	private String exChangeRateFromCcy;
	private String exChangeRateToCcy;
	private boolean updateFromIncoming = false;
	private String mesgNakedCode;

	public String getMesgCreaDateTimeStr() {
		if (mesgCreaDateTime == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(mesgCreaDateTime);
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getForwardedTo() {
		return forwardedTo;
	}

	public void setForwardedTo(String forwardedTo) {
		this.forwardedTo = forwardedTo;
	}

	public String getStatusOriginator() {
		return statusOriginator;
	}

	public void setStatusOriginator(String statusOriginator) {
		this.statusOriginator = statusOriginator;
	}

	public String getNakCode() {
		return nakCode;
	}

	public void setNakCode(String nakCode) {
		this.nakCode = nakCode;
	}

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

	public Date getMesgCreaDateTime() {
		if (mesgCreaDateTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(mesgCreaDateTime);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getMsgTrnRef() {
		return msgTrnRef;
	}

	public void setMsgTrnRef(String msgTrnRef) {
		this.msgTrnRef = msgTrnRef;
	}

	public String getMsgSendAmount() {
		return msgSendAmount;
	}

	public void setMsgSendAmount(String msgSendAmount) {
		this.msgSendAmount = msgSendAmount;
	}

	public String getMsgSendCurr() {
		return msgSendCurr;
	}

	public void setMsgSendCurr(String msgSendCurr) {
		this.msgSendCurr = msgSendCurr;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getCreditedAmount() {
		return creditedAmount;
	}

	public void setCreditedAmount(String creditedAmount) {
		this.creditedAmount = creditedAmount;
	}

	public String getCreditedCur() {
		return creditedCur;
	}

	public void setCreditedCur(String creditedCur) {
		this.creditedCur = creditedCur;
	}

	public String getMesgExchangeRate() {
		return mesgExchangeRate;
	}

	public void setMesgExchangeRate(String mesgExchangeRate) {
		this.mesgExchangeRate = mesgExchangeRate;
	}

	@Override
	public int compareTo(GpiConfirmation o) {
		return getMesgCreaDateTime().compareTo(o.getMesgCreaDateTime());
	}

	public String getMesgSla() {
		return mesgSla;
	}

	public void setMesgSla(String mesgSla) {
		this.mesgSla = mesgSla;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgNakedCode() {
		return mesgNakedCode;
	}

	public void setMesgNakedCode(String mesgNakedCode) {
		this.mesgNakedCode = mesgNakedCode;
	}

	public String getExChangeRateFromCcy() {
		return exChangeRateFromCcy;
	}

	public void setExChangeRateFromCcy(String exChangeRateFromCcy) {
		this.exChangeRateFromCcy = exChangeRateFromCcy;
	}

	public String getExChangeRateToCcy() {
		return exChangeRateToCcy;
	}

	public void setExChangeRateToCcy(String exChangeRateToCcy) {
		this.exChangeRateToCcy = exChangeRateToCcy;
	}

	public String getMesgCharges() {
		return mesgCharges;
	}

	public void setMesgCharges(String mesgCharges) {
		this.mesgCharges = mesgCharges;
	}

	public boolean isUpdateFromIncoming() {
		return updateFromIncoming;
	}

	public void setUpdateFromIncoming(boolean updateFromIncoming) {
		this.updateFromIncoming = updateFromIncoming;
	}
}
