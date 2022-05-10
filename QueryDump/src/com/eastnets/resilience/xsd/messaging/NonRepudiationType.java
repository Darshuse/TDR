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

@XmlType(name = "NonRepudiationType")
@XmlEnum
public enum NonRepudiationType {
	INVALID("NR_TYPE_INVALID"), MANDATORY("NR_TYPE_MANDATORY"), OPTIONAL("NR_TYPE_OPTIONAL");

	private final String value;

	private NonRepudiationType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static NonRepudiationType fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
