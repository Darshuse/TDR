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

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

import com.eastnets.domain.BaseEntity;

/**
 * MessageRequest POJO
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class MessageRequest extends BaseEntity implements Diffable<MessageRequest> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8582945961003060258L;
	private Long id;
	private Long amountOp;
	private Long dateValueOp;
	private String msgType;

	private Boolean autoDelete;
	private String amount;
	private String amountTo;
	private Long daysValue;
	private Date daysValueDate;
	private Date expirationDate;
	private Long status;
	private String email;
	private String fieldValue;
	private String userName;
	private String description;
	private String senderBic;
	private String receiverBic;
	private String subFormat;
	private String reference;
	private String ccy;
	private String identifier;
	private String requestorDN;
	private String responderDN;
	private Boolean ignoreFieldOption;

	private boolean delete;
	private boolean groupRequst;

	private Map<String, Long> statusMap;
	private Map<String, String> subFormatMap;

	SyntaxEntryField syntaxEntryField;

	public MessageRequest() {
		id = -1L;
		amountOp = -1L;
		daysValue = null;
		dateValueOp = -1L;

		autoDelete = null;

		status = 0L;

		email = null;
		fieldValue = null;
		userName = null;
		description = null;
		senderBic = null;
		receiverBic = null;
		identifier = null;
		subFormat = null;
		ignoreFieldOption = false;
		Calendar calendar = Calendar.getInstance();
		Calendar clone = (Calendar) calendar.clone();
		clone.add(Calendar.DAY_OF_MONTH, 1);
		daysValueDate = clone.getTime();

		calendar.add(Calendar.WEEK_OF_YEAR, 1);

		expirationDate = calendar.getTime();

		ccy = null;
		msgType = null;
		amount = null;
		amountTo = null;

		syntaxEntryField = new SyntaxEntryField();

		statusMap = new ConcurrentHashMap<String, Long>();
		statusMap.put("Any", 0L);
		statusMap.put("Ack", 1L);
		statusMap.put("Nack", 2L);

		subFormatMap = new TreeMap<String, String>();
		subFormatMap.put("I", "INPUT");
		subFormatMap.put("O", "OUTPUT");
		subFormatMap.put("%", null);

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSenderBic() {
		return senderBic;
	}

	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}

	public String getReceiverBic() {
		return receiverBic;
	}

	public void setReceiverBic(String receiverBic) {
		this.receiverBic = receiverBic;
	}

	public String getSubFormat() {
		return subFormat;
	}

	public void setSubFormat(String subFormat) {
		if (subFormat == "") {
			this.subFormat = null;
		} else {
			this.subFormat = subFormat;
		}
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Long getAmountOp() {
		return amountOp;
	}

	public void setAmountOp(Long amountOp) {
		this.amountOp = amountOp;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(String amountTo) {
		this.amountTo = amountTo;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getRequestorDN() {
		return requestorDN;
	}

	public void setRequestorDN(String requestorDN) {
		this.requestorDN = requestorDN;
	}

	public String getResponderDN() {
		return responderDN;
	}

	public void setResponderDN(String responderDN) {
		this.responderDN = responderDN;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Long getDaysValue() {
		return daysValue;
	}

	public void setDaysValue(Long daysValue) {
		this.daysValue = daysValue;
	}

	public Date getDaysValueDate() {
		return daysValueDate;
	}

	public void setDaysValueDate(Date daysValueDate) {
		this.daysValueDate = daysValueDate;
	}

	public Long getDateValueOp() {
		return dateValueOp;
	}

	public void setDateValueOp(Long dateValueOp) {
		this.dateValueOp = dateValueOp;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Boolean getAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(Boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRunOnce() {

		if (autoDelete == false) {
			return "No";
		}
		return "Yes";
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public Map<String, Long> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, Long> statusMap) {
		this.statusMap = statusMap;
	}

	public Map<String, String> getSubFormatMap() {
		return subFormatMap;
	}

	public void setSubFormatMap(Map<String, String> subFormatMap) {
		this.subFormatMap = subFormatMap;
	}

	public boolean isGroupRequst() {
		return groupRequst;
	}

	public void setGroupRequst(boolean groupRequst) {
		this.groupRequst = groupRequst;
	}

	public String getField() {

		if (syntaxEntryField == null) {
			return "";
		}

		return syntaxEntryField.getFieldValue();
	}

	public void setField(String field) {
		if (field == null) {
			return;
		}

		switch (field.length()) {
		case 3:
			syntaxEntryField.setCode(Long.parseLong(field.substring(0, 1)));
			syntaxEntryField.setFieldOption(field.substring(2));
			break;
		case 2:
			syntaxEntryField.setCode(Long.parseLong(field.substring(0, 1)));
			break;
		default:

		}

	}

	public SyntaxEntryField getSyntaxEntryField() {
		return syntaxEntryField;
	}

	public void setSyntaxEntryField(SyntaxEntryField syntaxEntryField) {
		this.syntaxEntryField = syntaxEntryField;
	}

	public Boolean getIgnoreFieldOption() {
		return ignoreFieldOption;
	}

	public void setIgnoreFieldOption(Boolean ignoreFieldOption) {
		this.ignoreFieldOption = ignoreFieldOption;
	}

	@Override
	public DiffResult diff(MessageRequest oldMessageRequest) {
		DiffBuilder compare = new DiffBuilder(this, oldMessageRequest, null, false);
		compare.append("Message Type", this.getMsgType(), emptyWhenNull(oldMessageRequest.getMsgType()));
		compare.append("Amount From", this.getAmount(), emptyWhenNull(oldMessageRequest.getAmount()));
		compare.append("Amount To", this.getAmountTo(), emptyWhenNull(oldMessageRequest.getAmountTo()));
		compare.append("Expiration Date", this.getExpirationDate(), oldMessageRequest.getExpirationDate());
		compare.append("Email", this.getEmail(), emptyWhenNull(oldMessageRequest.getEmail()));
		compare.append("Field Name", this.getSyntaxEntryField().getFieldValue(), oldMessageRequest.getSyntaxEntryField().getFieldValue());
		compare.append("Value Date", this.getDaysValueDate(), oldMessageRequest.getDaysValueDate());
		compare.append("Days Value", this.getDaysValue(), oldMessageRequest.getDaysValue());
		compare.append("Field Value", this.getFieldValue(), emptyWhenNull(oldMessageRequest.getFieldValue()));
		compare.append("Description", this.getDescription(), emptyWhenNull(oldMessageRequest.getDescription()));
		compare.append("Sender Bic", this.getSenderBic(), emptyWhenNull(oldMessageRequest.getSenderBic()));
		compare.append("Receiver Bic", this.getReceiverBic(), emptyWhenNull(oldMessageRequest.getReceiverBic()));
		compare.append("Message Direction", this.getSubFormat(), oldMessageRequest.getSubFormat());
		compare.append("Message Reference", this.getReference(), emptyWhenNull(oldMessageRequest.getReference()));
		compare.append("Message Currency", this.getCcy(), oldMessageRequest.getCcy());
		compare.append("Message Identifier", this.getIdentifier(), emptyWhenNull(oldMessageRequest.getIdentifier()));
		compare.append("Requestor DN", this.getRequestorDN(), emptyWhenNull(oldMessageRequest.getRequestorDN()));
		compare.append("Responder DN", this.getResponderDN(), emptyWhenNull(oldMessageRequest.getResponderDN()));
		compare.append("Ignore Field Option", this.getIgnoreFieldOption(), oldMessageRequest.getIgnoreFieldOption());
		compare.append("Delete Request After Notification", this.getAutoDelete(), oldMessageRequest.getAutoDelete());
		compare.append("Group Request", this.isGroupRequst(), oldMessageRequest.isGroupRequst());
		compare.append("WD Requst Status", this.getStatus(), oldMessageRequest.getStatus());
		return compare.build();

	}

	private String emptyWhenNull(String value) {
		if (value == null) {
			return "";
		}
		return value;
	}

}
