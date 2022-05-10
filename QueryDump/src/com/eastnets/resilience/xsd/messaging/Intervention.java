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

import com.eastnets.resilience.xmldump.utils.StringUtils;
import com.eastnets.resilience.xsd.BaseObject;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Intervention", propOrder = { "identifier", "number", "name", "category", "operator", "application",
		"mpfnName", "appendixDateTime", "appendixSequenceNumber", "length", "text" })
public class Intervention extends BaseObject {

	@XmlElement(name = "Identifier")
	protected InterventionIdentifier identifier;

	@XmlElement(name = "Number")
	protected int number;

	@XmlElement(name = "Name", required = true)
	protected String name;

	@XmlElement(name = "Category", required = true)
	protected InterventionCategory category;

	@XmlElement(name = "Operator")
	protected String operator;

	@XmlElement(name = "Application")
	protected String application;

	@XmlElement(name = "MpfnName", required = true)
	protected String mpfnName;

	@XmlElement(name = "AppendixDateTime")
	protected XMLGregorianCalendar appendixDateTime;

	@XmlElement(name = "AppendixSequenceNumber")
	protected Integer appendixSequenceNumber;

	@XmlElement(name = "Length")
	protected int length;

	@XmlElement(name = "Text", required = true)
	protected String text;

	public InterventionIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(InterventionIdentifier paramInterventionIdentifier) {
		this.identifier = paramInterventionIdentifier;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int paramInt) {
		this.number = paramInt;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public InterventionCategory getCategory() {
		return this.category;
	}

	public void setCategory(InterventionCategory paramInterventionCategory) {
		this.category = paramInterventionCategory;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String paramString) {
		this.operator = paramString;
	}

	public String getApplication() {
		return this.application;
	}

	public void setApplication(String paramString) {
		this.application = paramString;
	}

	public String getMpfnName() {
		return this.mpfnName;
	}

	public void setMpfnName(String paramString) {
		this.mpfnName = paramString;
	}

	public XMLGregorianCalendar getAppendixDateTime() {
		return this.appendixDateTime;
	}

	public void setAppendixDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.appendixDateTime = paramXMLGregorianCalendar;
	}

	public Integer getAppendixSequenceNumber() {
		return this.appendixSequenceNumber;
	}

	public void setAppendixSequenceNumber(Integer paramInteger) {
		this.appendixSequenceNumber = paramInteger;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int paramInt) {
		this.length = paramInt;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String paramString) {
		this.text = paramString;
	}

	@Override
	public int getUmidL() {
		return StringUtils.getUmidl(this.getIdentifier().getSUmid());
	}

	@Override
	public int getUmidH() {
		return StringUtils.getUmidh(this.getIdentifier().getSUmid());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UmidL " + this.getUmidL() + "\n");
		builder.append("UmidH " + this.getUmidH() + "\n");
		builder.append("Seq  " + this.getAppendixSequenceNumber() + "\n");
		builder.append("Name  " + this.getName() + "\n");
		builder.append("Length  " + this.getLength() + "\n");
		return builder.toString();
	}
}
