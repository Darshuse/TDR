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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageIdentifier", propOrder = { "sUmid", "passiveLockToken", "tableId", "archiveName" })
public class MessageIdentifier {

	@XmlElement(name = "SUmid", required = true)
	protected String sUmid;

	@XmlElement(name = "PassiveLockToken")
	protected String passiveLockToken;

	@XmlElement(name = "TableId")
	protected String tableId;

	@XmlElement(name = "ArchiveName")
	protected String archiveName;

	public String getSUmid() {
		return this.sUmid;
	}

	public void setSUmid(String paramString) {
		this.sUmid = paramString;
	}

	public String getPassiveLockToken() {
		return this.passiveLockToken;
	}

	public void setPassiveLockToken(String paramString) {
		this.passiveLockToken = paramString;
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
