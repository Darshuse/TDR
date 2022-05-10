package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Cacheable(false)
@Table(name = "WDUSERSEARCHPARAMETER")
public class WDMessageSearchRequest extends SearchRequest {

	@Id
	@Column(name = "REQUESTID")
	private Integer requestID;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SENDERBIC")
	private String senderBic;

	@Column(name = "RECEIVERBIC")
	private String receiverBic;

	@Column(name = "TRN_REF")
	private String transactionRef;

	@Column(name = "SUBFORMAT")
	private String subFormat;

	@Column(name = "AMOUNTOP")
	private int amountOP;

	@Column(name = "AMOUNT")
	private Double amountFrom;

	@Column(name = "AMOUNTTO")
	private Double amountTo;

	@Column(name = "CCY")
	private String currency;

	@Column(name = "MSGTYPE")
	private String messageType;

	@Column(name = "DATEVALUEOP")
	private Integer dateValueOP;

	@Column(name = "DAYSVALUE")
	private Integer daysValue;

	@Column(name = "FIELDCODE")
	private Integer fieldCode;

	@Column(name = "FIELDOPTION")
	private String fieldOption;

	@Column(name = "FIELDCODEID")
	private Integer fieldCodeID;

	@Column(name = "FIELDVALUE")
	private String fieldValue;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "IDENTIFIER")
	private String identifier;

	@Column(name = "AUTODELETE")
	private Integer autoDelete;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "USERGROUP")
	private Integer groupRequest;

	@Column(name = "EXPIRATIONDATE")
	private Date expirationDate;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "REQUESTOR_DN")
	private String requestorDN;

	@Column(name = "RESPONDER_DN")
	private String responderDN;

	public WDMessageSearchRequest() {

	}

	public Integer getRequestID() {
		return requestID;
	}

	public void setRequestID(Integer requestID) {
		this.requestID = requestID;
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

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public String getSubFormat() {
		return subFormat;
	}

	public void setSubFormat(String subFormat) {
		this.subFormat = subFormat;
	}

	public int getAmountOP() {
		return amountOP;
	}

	public void setAmountOP(int amountOP) {
		this.amountOP = amountOP;
	}

	public Double getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(Double amountFrom) {
		this.amountFrom = amountFrom;
	}

	public Double getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(Double amountTo) {
		this.amountTo = amountTo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Integer getDaysValue() {
		return daysValue;
	}

	public void setDaysValue(Integer daysValue) {
		this.daysValue = daysValue;
	}

	public Integer getDateValueOP() {
		return dateValueOP;
	}

	public void setDateValueOP(Integer dateValueOP) {
		this.dateValueOP = dateValueOP;
	}

	public Integer getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(Integer fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getFieldOption() {
		return fieldOption;
	}

	public void setFieldOption(String fieldOption) {
		this.fieldOption = fieldOption;
	}

	public Integer getFieldCodeID() {
		return fieldCodeID;
	}

	public void setFieldCodeID(Integer fieldCodeID) {
		this.fieldCodeID = fieldCodeID;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Integer getAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(Integer autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Integer getGroupRequest() {
		return groupRequest;
	}

	public void setGroupRequest(Integer groupRequest) {
		this.groupRequest = groupRequest;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "WDMessageSearchRequest [requestID=" + requestID + ", description=" + description + ", senderBic=" + senderBic + ", receiverBic=" + receiverBic + ", transactionRef=" + transactionRef + ", subFormat=" + subFormat + ", amountOP="
				+ amountOP + ", amountFrom=" + amountFrom + ", amountTo=" + amountTo + ", currency=" + currency + ", messageType=" + messageType + ", dateValueOP=" + dateValueOP + ", daysValue=" + daysValue + ", fieldCode=" + fieldCode
				+ ", fieldOption=" + fieldOption + ", fieldCodeID=" + fieldCodeID + ", fieldValue=" + fieldValue + ", email=" + email + ", identifier=" + identifier + ", autoDelete=" + autoDelete + ", groupRequest=" + groupRequest
				+ ", expirationDate=" + expirationDate + ", status=" + status + ", requestorDN=" + requestorDN + ", responderDN=" + responderDN;
	}

}