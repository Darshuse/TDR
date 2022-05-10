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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArchiveTableIdentifier", propOrder = { "archiveType", "tableId", "passiveLockToken" })
public class ArchiveTableIdentifier {

	@XmlElement(name = "ArchiveType", required = true)
	protected ArchiveType archiveType;

	@XmlElement(name = "TableId", required = true)
	protected String tableId;

	@XmlElement(name = "PassiveLockToken")
	protected String passiveLockToken;

	public ArchiveType getArchiveType() {
		return this.archiveType;
	}

	public void setArchiveType(ArchiveType paramArchiveType) {
		this.archiveType = paramArchiveType;
	}

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String paramString) {
		this.tableId = paramString;
	}

	public String getPassiveLockToken() {
		return this.passiveLockToken;
	}

	public void setPassiveLockToken(String paramString) {
		this.passiveLockToken = paramString;
	}
}
