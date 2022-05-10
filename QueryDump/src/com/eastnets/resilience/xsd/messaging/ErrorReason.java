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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ErrorReason")
@XmlEnum
public enum ErrorReason {
	NONE("ERROR_NONE"), FORMAT("ERROR_FORMAT"), MESSAGE("ERROR_MESSAGE"), LENGTH("ERROR_LENGTH"), SYNTAX("ERROR_SYNTAX"), UNKNOWN(
			"ERROR_UNKNOWN");

	private final String value;

	private ErrorReason(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static ErrorReason fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
