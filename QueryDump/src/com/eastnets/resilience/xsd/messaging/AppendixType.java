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

@XmlType(name = "AppendixType")
@XmlEnum
public enum AppendixType {
	EMISSION("APPE_EMISSION"), RECEPTION("APPE_RECEPTION");

	private final String value;

	private AppendixType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static AppendixType fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
