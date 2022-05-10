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
package com.eastnets.resilience.xsd.eventjournal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventIdentifier", propOrder = { "dateTime", "sequenceNumber", "tableId", "archiveName" })
public class EventIdentifier {

	@XmlElement(name = "DateTime", required = true)
	protected XMLGregorianCalendar dateTime;

	@XmlElement(name = "SequenceNumber")
	protected long sequenceNumber;

	@XmlElement(name = "TableId", required = true)
	protected String tableId;

	@XmlElement(name = "ArchiveName")
	protected String archiveName;

	public XMLGregorianCalendar getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.dateTime = paramXMLGregorianCalendar;
	}

	public long getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(long paramLong) {
		this.sequenceNumber = paramLong;
	}

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String paramString) {
		this.tableId = paramString;
	}

	public String getArchiveName() {
		return this.archiveName;
	}

	public void setArchiveName(String paramString) {
		this.archiveName = paramString;
	}
}
