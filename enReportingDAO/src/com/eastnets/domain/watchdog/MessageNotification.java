/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.domain.watchdog;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.BaseEntity;

/**
 * MessageNotification POJO
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class MessageNotification extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2659735468928233403L;

	DateFormat dateFormat;

	private String amount;

	private Long id;
	private Integer aid;
	private Long umidl;
	private Long umidh;
	private Long sessionNbr;
	private Long sequenceNbr;

	private String msgtype;
	private String description;
	private String subFormat;
	private String receiver;
	private String sender;
	private String reference;
	private String currency;
	private String logicalTerminal;
	private String owner;
	private String identifier;
	private String lastSequenceNumber;
	private String expectedSequenceNumber;

	private Timestamp gapTime;
	private Timestamp receptionDate;
	private Timestamp valueDate;
	private Timestamp insertTime;
	private Timestamp mesgCreaDateTime;

	private int notificationType;
	private List<Annotaion> annotaionsList;
	private int annotationType;
	public static transient SimpleDateFormat fullDateTimeFormat;

	public MessageNotification() {

		dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
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

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getReceptionDate() {
		if (receptionDate == null) {
			return dateFormat.format(receptionDate);
		} else {
			return null;
		}
	}

	public void setReceptionDate(Timestamp receptionDate) {
		this.receptionDate = receptionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubFormat() {
		return subFormat;
	}

	public void setSubFormat(String subFormat) {
		this.subFormat = subFormat;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Timestamp getValueDate() {

		return valueDate;
	}

	public void setValueDate(Timestamp valueDate) {
		this.valueDate = valueDate;
	}

	public String getLogicalTerminal() {
		return logicalTerminal;
	}

	public void setLogicalTerminal(String logicalTerminal) {
		this.logicalTerminal = logicalTerminal;
	}

	public Long getSessionNbr() {
		return sessionNbr;
	}

	public void setSessionNbr(Long sessionNbr) {
		this.sessionNbr = sessionNbr;
	}

	public Long getSequenceNbr() {
		return sequenceNbr;
	}

	public void setSequenceNbr(Long sequenceNbr) {
		this.sequenceNbr = sequenceNbr;
	}

	public String getInsertTime() {
		if (insertTime != null) {
			return dateFormat.format(insertTime);
		} else {
			return null;
		}
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLastSequenceNumber() {
		return lastSequenceNumber;
	}

	public void setLastSequenceNumber(String lastSequenceNumber) {
		this.lastSequenceNumber = lastSequenceNumber;
	}

	public String getExpectedSequenceNumber() {
		return expectedSequenceNumber;
	}

	public void setExpectedSequenceNumber(String expectedSequenceNumber) {
		this.expectedSequenceNumber = expectedSequenceNumber;
	}

	public String getGapTime() {
		if (gapTime != null) {
			return dateFormat.format(gapTime);
		} else {
			return null;
		}
	}

	public void setGapTime(Timestamp gapTime) {
		this.gapTime = gapTime;
	}

	@Override
	public boolean equals(Object obj) {
		MessageNotification msgReq = (MessageNotification) obj;
		boolean isEqual = (this.getId() == msgReq.getId());

		return isEqual;
	}

	public int getNotificationType() {

		return this.notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}

	public List<Annotaion> getAnnotaionsList() {
		return annotaionsList;
	}

	public void setAnnotaionsList(List<Annotaion> annotaionsList) {
		if (annotaionsList == null || annotaionsList.size() <= 0) {
			this.setAnnotationType(0);
		} else {
			this.setAnnotationType(1);
		}
		this.annotaionsList = annotaionsList;
	}

	public int getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(int annotationType) {
		this.annotationType = annotationType;
	}

	@Override
	public String toString() {

		String format = String.format("%s at %s", this.description, this.insertTime.toString());
		return format;
	}

	public Timestamp getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public String getMesgCreaDateTimeFormatted() {
		return fullDateTimeFormat.format(getMesgCreaDateTime());
	 
	}

	public void setMesgCreaDateTime(Timestamp mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

}
