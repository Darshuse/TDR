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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ArchiveType")
@XmlEnum
public enum ArchiveType {
	EVT_ARCHIVE, MSG_ARCHIVE;

	public String value() {
		return name();
	}

	public static ArchiveType fromValue(String paramString) {
		return valueOf(paramString);
	}
}
