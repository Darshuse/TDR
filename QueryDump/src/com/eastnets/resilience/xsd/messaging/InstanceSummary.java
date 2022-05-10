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
package com.eastnets.resilience.xsd.messaging;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceSummary", propOrder = { "routingPoint", "priority", "relativeReference", "sUmid",
		"instanceNumber", "isReserved", "mpfnHandle", "unitName", "deferredTime", "formatName", "subFormat",
		"syntaxTableVersion", "clazz", "templateName", "uumid", "uumidSuffix", "messageType", "relatedSUmid",
		"networkPriority", "finCurrencyAmount", "finValueDate", "senderCorrespondentInstitutionName",
		"networkApplicationIndication", "creationOperator", "modificationOperator", "verificationOperator",
		"senderSwiftAddress", "receiverSwiftAddress", "requestorDn", "service", "messageIdentifier",
		"creationMpfnName", "lastOperator", "mpfnName", "instanceType", "receiverCorrespondentInstitutionName",
		"receiverNetworkIntegratedApplicationName", "responderDn", "nackReason" })
public class InstanceSummary {

	@XmlElement(name = "RoutingPoint", required = true)
	protected String routingPoint;

	@XmlElement(name = "Priority")
	protected int priority;

	@XmlElement(name = "RelativeReference")
	protected int relativeReference;

	@XmlElement(name = "SUmid", required = true)
	protected String sUmid;

	@XmlElement(name = "InstanceNumber")
	protected int instanceNumber;

	@XmlElement(name = "IsReserved")
	protected boolean isReserved;

	@XmlElement(name = "MpfnHandle", required = true)
	protected String mpfnHandle;

	@XmlElement(name = "UnitName", required = true)
	protected String unitName;

	@XmlElement(name = "DeferredTime")
	protected XMLGregorianCalendar deferredTime;

	@XmlElement(name = "FormatName")
	protected String formatName;

	@XmlElement(name = "SubFormat")
	protected SubFormat subFormat;

	@XmlElement(name = "SyntaxTableVersion")
	protected String syntaxTableVersion;

	@XmlElement(name = "Class", required = true)
	protected Class clazz;

	@XmlElement(name = "TemplateName")
	protected String templateName;

	@XmlElement(name = "Uumid")
	protected String uumid;

	@XmlElement(name = "UumidSuffix")
	protected Integer uumidSuffix;

	@XmlElement(name = "MessageType")
	protected String messageType;

	@XmlElement(name = "RelatedSUmid")
	protected String relatedSUmid;

	@XmlElement(name = "NetworkPriority")
	protected NetworkPriority networkPriority;

	@XmlElement(name = "FinCurrencyAmount")
	protected String finCurrencyAmount;

	@XmlElement(name = "FinValueDate")
	protected String finValueDate;

	@XmlElement(name = "SenderCorrespondentInstitutionName")
	protected String senderCorrespondentInstitutionName;

	@XmlElement(name = "NetworkApplicationIndication")
	protected String networkApplicationIndication;

	@XmlElement(name = "CreationOperator", required = true)
	protected String creationOperator;

	@XmlElement(name = "ModificationOperator", required = true)
	protected String modificationOperator;

	@XmlElement(name = "VerificationOperator")
	protected String verificationOperator;

	@XmlElement(name = "SenderSwiftAddress")
	protected String senderSwiftAddress;

	@XmlElement(name = "ReceiverSwiftAddress")
	protected String receiverSwiftAddress;

	@XmlElement(name = "RequestorDn")
	protected String requestorDn;

	@XmlElement(name = "Service")
	protected String service;

	@XmlElement(name = "MessageIdentifier")
	protected String messageIdentifier;

	@XmlElement(name = "CreationMpfnName", required = true)
	protected String creationMpfnName;

	@XmlElement(name = "LastOperator")
	protected String lastOperator;

	@XmlElement(name = "MpfnName", required = true)
	protected String mpfnName;

	@XmlElement(name = "InstanceType", required = true)
	protected InstanceType instanceType;

	@XmlElement(name = "ReceiverCorrespondentInstitutionName")
	protected String receiverCorrespondentInstitutionName;

	@XmlElement(name = "ReceiverNetworkIntegratedApplicationName")
	protected String receiverNetworkIntegratedApplicationName;

	@XmlElement(name = "ResponderDn")
	protected String responderDn;

	@XmlElement(name = "NackReason")
	protected String nackReason;

	public String getRoutingPoint() {
		return this.routingPoint;
	}

	public void setRoutingPoint(String paramString) {
		this.routingPoint = paramString;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int paramInt) {
		this.priority = paramInt;
	}

	public int getRelativeReference() {
		return this.relativeReference;
	}

	public void setRelativeReference(int paramInt) {
		this.relativeReference = paramInt;
	}

	public String getSUmid() {
		return this.sUmid;
	}

	public void setSUmid(String paramString) {
		this.sUmid = paramString;
	}

	public int getInstanceNumber() {
		return this.instanceNumber;
	}

	public void setInstanceNumber(int paramInt) {
		this.instanceNumber = paramInt;
	}

	public boolean isIsReserved() {
		return this.isReserved;
	}

	public void setIsReserved(boolean paramBoolean) {
		this.isReserved = paramBoolean;
	}

	public String getMpfnHandle() {
		return this.mpfnHandle;
	}

	public void setMpfnHandle(String paramString) {
		this.mpfnHandle = paramString;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String paramString) {
		this.unitName = paramString;
	}

	public XMLGregorianCalendar getDeferredTime() {
		return this.deferredTime;
	}

	public void setDeferredTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.deferredTime = paramXMLGregorianCalendar;
	}

	public String getFormatName() {
		return this.formatName;
	}

	public void setFormatName(String paramString) {
		this.formatName = paramString;
	}

	public SubFormat getSubFormat() {
		return this.subFormat;
	}

	public void setSubFormat(SubFormat paramSubFormat) {
		this.subFormat = paramSubFormat;
	}

	public String getSyntaxTableVersion() {
		return this.syntaxTableVersion;
	}

	public void setSyntaxTableVersion(String paramString) {
		this.syntaxTableVersion = paramString;
	}

	public Class getClazz() {
		return this.clazz;
	}

	public void setClazz(Class paramClass) {
		this.clazz = paramClass;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String paramString) {
		this.templateName = paramString;
	}

	public String getUumid() {
		return this.uumid;
	}

	public void setUumid(String paramString) {
		this.uumid = paramString;
	}

	public Integer getUumidSuffix() {
		return this.uumidSuffix;
	}

	public void setUumidSuffix(Integer paramInteger) {
		this.uumidSuffix = paramInteger;
	}

	public String getMessageType() {
		return this.messageType;
	}

	public void setMessageType(String paramString) {
		this.messageType = paramString;
	}

	public String getRelatedSUmid() {
		return this.relatedSUmid;
	}

	public void setRelatedSUmid(String paramString) {
		this.relatedSUmid = paramString;
	}

	public NetworkPriority getNetworkPriority() {
		return this.networkPriority;
	}

	public void setNetworkPriority(NetworkPriority paramNetworkPriority) {
		this.networkPriority = paramNetworkPriority;
	}

	public String getFinCurrencyAmount() {
		return this.finCurrencyAmount;
	}

	public void setFinCurrencyAmount(String paramString) {
		this.finCurrencyAmount = paramString;
	}

	public String getFinValueDate() {
		return this.finValueDate;
	}

	public void setFinValueDate(String paramString) {
		this.finValueDate = paramString;
	}

	public String getSenderCorrespondentInstitutionName() {
		return this.senderCorrespondentInstitutionName;
	}

	public void setSenderCorrespondentInstitutionName(String paramString) {
		this.senderCorrespondentInstitutionName = paramString;
	}

	public String getNetworkApplicationIndication() {
		return this.networkApplicationIndication;
	}

	public void setNetworkApplicationIndication(String paramString) {
		this.networkApplicationIndication = paramString;
	}

	public String getCreationOperator() {
		return this.creationOperator;
	}

	public void setCreationOperator(String paramString) {
		this.creationOperator = paramString;
	}

	public String getModificationOperator() {
		return this.modificationOperator;
	}

	public void setModificationOperator(String paramString) {
		this.modificationOperator = paramString;
	}

	public String getVerificationOperator() {
		return this.verificationOperator;
	}

	public void setVerificationOperator(String paramString) {
		this.verificationOperator = paramString;
	}

	public String getSenderSwiftAddress() {
		return this.senderSwiftAddress;
	}

	public void setSenderSwiftAddress(String paramString) {
		this.senderSwiftAddress = paramString;
	}

	public String getReceiverSwiftAddress() {
		return this.receiverSwiftAddress;
	}

	public void setReceiverSwiftAddress(String paramString) {
		this.receiverSwiftAddress = paramString;
	}

	public String getRequestorDn() {
		return this.requestorDn;
	}

	public void setRequestorDn(String paramString) {
		this.requestorDn = paramString;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String paramString) {
		this.service = paramString;
	}

	public String getMessageIdentifier() {
		return this.messageIdentifier;
	}

	public void setMessageIdentifier(String paramString) {
		this.messageIdentifier = paramString;
	}

	public String getCreationMpfnName() {
		return this.creationMpfnName;
	}

	public void setCreationMpfnName(String paramString) {
		this.creationMpfnName = paramString;
	}

	public String getLastOperator() {
		return this.lastOperator;
	}

	public void setLastOperator(String paramString) {
		this.lastOperator = paramString;
	}

	public String getMpfnName() {
		return this.mpfnName;
	}

	public void setMpfnName(String paramString) {
		this.mpfnName = paramString;
	}

	public InstanceType getInstanceType() {
		return this.instanceType;
	}

	public void setInstanceType(InstanceType paramInstanceType) {
		this.instanceType = paramInstanceType;
	}

	public String getReceiverCorrespondentInstitutionName() {
		return this.receiverCorrespondentInstitutionName;
	}

	public void setReceiverCorrespondentInstitutionName(String paramString) {
		this.receiverCorrespondentInstitutionName = paramString;
	}

	public String getReceiverNetworkIntegratedApplicationName() {
		return this.receiverNetworkIntegratedApplicationName;
	}

	public void setReceiverNetworkIntegratedApplicationName(String paramString) {
		this.receiverNetworkIntegratedApplicationName = paramString;
	}

	public String getResponderDn() {
		return this.responderDn;
	}

	public void setResponderDn(String paramString) {
		this.responderDn = paramString;
	}

	public String getNackReason() {
		return this.nackReason;
	}

	public void setNackReason(String paramString) {
		this.nackReason = paramString;
	}
}
