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
@XmlType(name = "MessageSummary", propOrder = { "identifier", "location", "creationDate", "status", "umid",
		"formatName", "subFormat", "creationApplication", "finValueDate", "finCurrencyAmount", "transactionReference",
		"service", "messageIdentifier", "senderSwiftAddress", "receiverSwiftAddress", "networkDeliveryStatus",
		"appendixDateTime", "xmlQueryReference1", "xmlQueryReference2", "xmlQueryReference3" })
public class MessageSummary {

	@XmlElement(name = "Identifier", required = true)
	protected MessageIdentifier identifier;

	@XmlElement(name = "Location", required = true)
	protected MessageLocation location;

	@XmlElement(name = "CreationDate", required = true)
	protected XMLGregorianCalendar creationDate;

	@XmlElement(name = "Status", required = true)
	protected Status status;

	@XmlElement(name = "Umid")
	protected String umid;

	@XmlElement(name = "FormatName")
	protected String formatName;

	@XmlElement(name = "SubFormat")
	protected SubFormat subFormat;

	@XmlElement(name = "CreationApplication", required = true)
	protected String creationApplication;

	@XmlElement(name = "FinValueDate")
	protected String finValueDate;

	@XmlElement(name = "FinCurrencyAmount")
	protected String finCurrencyAmount;

	@XmlElement(name = "TransactionReference")
	protected String transactionReference;

	@XmlElement(name = "Service")
	protected String service;

	@XmlElement(name = "MessageIdentifier")
	protected String messageIdentifier;

	@XmlElement(name = "SenderSwiftAddress")
	protected String senderSwiftAddress;

	@XmlElement(name = "ReceiverSwiftAddress")
	protected String receiverSwiftAddress;

	@XmlElement(name = "NetworkDeliveryStatus", required = true)
	protected NetworkDeliveryStatus networkDeliveryStatus;

	@XmlElement(name = "AppendixDateTime")
	protected XMLGregorianCalendar appendixDateTime;

	@XmlElement(name = "XmlQueryReference1")
	protected String xmlQueryReference1;

	@XmlElement(name = "XmlQueryReference2")
	protected String xmlQueryReference2;

	@XmlElement(name = "XmlQueryReference3")
	protected String xmlQueryReference3;

	public MessageIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(MessageIdentifier paramMessageIdentifier) {
		this.identifier = paramMessageIdentifier;
	}

	public MessageLocation getLocation() {
		return this.location;
	}

	public void setLocation(MessageLocation paramMessageLocation) {
		this.location = paramMessageLocation;
	}

	public XMLGregorianCalendar getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.creationDate = paramXMLGregorianCalendar;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status paramStatus) {
		this.status = paramStatus;
	}

	public String getUmid() {
		return this.umid;
	}

	public void setUmid(String paramString) {
		this.umid = paramString;
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

	public String getCreationApplication() {
		return this.creationApplication;
	}

	public void setCreationApplication(String paramString) {
		this.creationApplication = paramString;
	}

	public String getFinValueDate() {
		return this.finValueDate;
	}

	public void setFinValueDate(String paramString) {
		this.finValueDate = paramString;
	}

	public String getFinCurrencyAmount() {
		return this.finCurrencyAmount;
	}

	public void setFinCurrencyAmount(String paramString) {
		this.finCurrencyAmount = paramString;
	}

	public String getTransactionReference() {
		return this.transactionReference;
	}

	public void setTransactionReference(String paramString) {
		this.transactionReference = paramString;
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

	public NetworkDeliveryStatus getNetworkDeliveryStatus() {
		return this.networkDeliveryStatus;
	}

	public void setNetworkDeliveryStatus(NetworkDeliveryStatus paramNetworkDeliveryStatus) {
		this.networkDeliveryStatus = paramNetworkDeliveryStatus;
	}

	public XMLGregorianCalendar getAppendixDateTime() {
		return this.appendixDateTime;
	}

	public void setAppendixDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.appendixDateTime = paramXMLGregorianCalendar;
	}

	public String getXmlQueryReference1() {
		return this.xmlQueryReference1;
	}

	public void setXmlQueryReference1(String paramString) {
		this.xmlQueryReference1 = paramString;
	}

	public String getXmlQueryReference2() {
		return this.xmlQueryReference2;
	}

	public void setXmlQueryReference2(String paramString) {
		this.xmlQueryReference2 = paramString;
	}

	public String getXmlQueryReference3() {
		return this.xmlQueryReference3;
	}

	public void setXmlQueryReference3(String paramString) {
		this.xmlQueryReference3 = paramString;
	}
}
