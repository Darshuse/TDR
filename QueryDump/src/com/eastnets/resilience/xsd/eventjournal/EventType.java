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

@XmlType(name = "EventType")
@XmlEnum
public enum EventType {
	ALL_EVENTS, SECURITY_EVENTS, ALARM_EVENTS, SECURITY_ALARM_EVENTS, CONFIG_MGMT_EVENTS;

	public String value() {
		return name();
	}

	public static EventType fromValue(String paramString) {
		return valueOf(paramString);
	}
}
