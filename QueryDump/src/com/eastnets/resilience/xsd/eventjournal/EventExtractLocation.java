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

@XmlType(name = "EventExtractLocation")
@XmlEnum
public enum EventExtractLocation {
	ARCHIVE, LIVE, BOTH;

	public String value() {
		return name();
	}

	public static EventExtractLocation fromValue(String paramString) {
		return valueOf(paramString);
	}
}
