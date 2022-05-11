package com.eastnets.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.NONE)
public class MessageRequest {

	@XmlElement(name = "Source")
	@NotBlank(message = "Source field is mandatory")
	private String source;

	@XmlElement(name = "MesgSyntaxVersion")
	@NotBlank(message = "MesgSyntaxVersion field is mandatory")
	private String mesgSyntaxVersion;

	@XmlElement(name = "MesgId")
	@NotBlank(message = "MesgId field is mandatory")
	private String id;

	@XmlElement(name = "CurrentStatusName")
	@NotBlank(message = "CurrentStatusName field is mandatory")
	private String currentStatusName;

	@XmlElement(name = "CreatedByName")
	@NotBlank(message = "CreatedByName field is mandatory")
	private String createdByName;

	@Pattern(regexp = "[0-9]{12}")
	@XmlElement(name = "CreationDate")
	@NotBlank(message = "CreationDate field is mandatory")
	private String mesgCreaDateTime;

	@XmlElement(name = "UnitName")
	@NotBlank(message = "UnitName field is mandatory")
	private String unitName;

	@XmlElement(name = "Sender")
	@NotBlank(message = "Sender field is mandatory")
	private String sender;

	@XmlElement(name = "Network")
	@NotBlank(message = "Network field is mandatory")
	private String mesgFrmtName;

	@XmlElement(name = "IsLive")
	private boolean mesgStatus = true;

	@XmlElement(name = "MessagePriority")
	@NotBlank(message = "MessagePriority field is mandatory")
	private String mesgNetworkPriority;

	@XmlElement(name = "DestinationAddress")
	@NotBlank(message = "DestinationAddress field is mandatory")
	private String mesgReceiverSwiftAddress;

	// Field 21
	@XmlElement(name = "MesgRelTrnRef")
	private String mesgRelTrnRef;

	@XmlElement(name = "mesgUserGroup")
	private String mesgUserGroup;

	@XmlElement(name = "LTAddress")
	@NotBlank(message = "LTAddress field is mandatory")
	private String mesgSenderSwiftAddress;

	@XmlElement(name = "Direction")
	@NotBlank(message = "Direction field is mandatory")
	private String mesgSubFormat;

	@XmlElement(name = "Reference")
	@NotBlank(message = "Reference field is mandatory")
	private String mesgTrnRef;

	@XmlElement(name = "MessageType")
	@NotBlank(message = "MessageType field is mandatory")
	private String mesgType;

	@XmlElement(name = "Currency")
	private String xFinCcy;

	@XmlElement(name = "Amount")
	private Double xFinAmount;

	@XmlElement(name = "ValueDate")
	private String valueDate;

	@XmlElement(name = "Receiver")
	@NotBlank(message = "Receiver field is mandatory")
	private String xReceiverX1;

	@XmlElement(name = "UTER")
	private String mesgE2ETransactionReference;

	@XmlElement(name = "SLA")
	private String mesgSla;

	@XmlElement(name = "TextBody")
	@NotBlank(message = "TextBody field is mandatory")
	private String textDataBlock;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrentStatusName() {
		return currentStatusName;
	}

	public void setCurrentStatusName(String currentStatusName) {
		this.currentStatusName = currentStatusName;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(String mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMesgFrmtName() {
		return mesgFrmtName;
	}

	public void setMesgFrmtName(String mesgFrmtName) {
		this.mesgFrmtName = mesgFrmtName;
	}

	public boolean getMesgStatus() {
		return mesgStatus;
	}

	public void setMesgStatus(boolean mesgStatus) {
		this.mesgStatus = mesgStatus;
	}

	public String getMesgNetworkPriority() {
		return mesgNetworkPriority;
	}

	public void setMesgNetworkPriority(String mesgNetworkPriority) {
		this.mesgNetworkPriority = mesgNetworkPriority;
	}

	public String getMesgReceiverSwiftAddress() {
		return mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverSwiftAddress(String mesgReceiverSwiftAddress) {
		this.mesgReceiverSwiftAddress = mesgReceiverSwiftAddress;
	}

	public String getMesgRelTrnRef() {
		return mesgRelTrnRef;
	}

	public void setMesgRelTrnRef(String mesgRelTrnRef) {
		this.mesgRelTrnRef = mesgRelTrnRef;
	}

	public String getMesgSenderSwiftAddress() {
		return mesgSenderSwiftAddress;
	}

	public void setMesgSenderSwiftAddress(String mesgSenderSwiftAddress) {
		this.mesgSenderSwiftAddress = mesgSenderSwiftAddress;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgTrnRef() {
		return mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getxFinCcy() {
		return xFinCcy;
	}

	public void setxFinCcy(String xFinCcy) {
		this.xFinCcy = xFinCcy;
	}

	public Double getxFinAmount() {
		return xFinAmount;
	}

	public void setxFinAmount(Double xFinAmount) {
		this.xFinAmount = xFinAmount;
	}

	public String getxReceiverX1() {
		return xReceiverX1;
	}

	public void setxReceiverX1(String xReceiverX1) {
		this.xReceiverX1 = xReceiverX1;
	}

	public String getMesgE2ETransactionReference() {
		return mesgE2ETransactionReference;
	}

	public void setMesgE2ETransactionReference(String mesgE2ETransactionReference) {
		this.mesgE2ETransactionReference = mesgE2ETransactionReference;
	}

	public String getTextDataBlock() {
		return textDataBlock;
	}

	public void setTextDataBlock(String textDataBlock) {
		this.textDataBlock = textDataBlock;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMesgUserGroup() {
		return mesgUserGroup;
	}

	public void setMesgUserGroup(String mesgUserGroup) {
		this.mesgUserGroup = mesgUserGroup;
	}

	public String getMesgSla() {
		return mesgSla;
	}

	public void setMesgSla(String mesgSla) {
		this.mesgSla = mesgSla;
	}

	public String getMesgSyntaxVersion() {
		return mesgSyntaxVersion;
	}

	public void setMesgSyntaxVersion(String mesgSyntaxVersion) {
		this.mesgSyntaxVersion = mesgSyntaxVersion;
	}

}
