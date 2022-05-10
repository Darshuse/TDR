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

@XmlType(name = "InstanceType")
@XmlEnum
public enum InstanceType {

	N_A("INST_TYPE_N_A"), NONE("INST_TYPE_NONE"), ORIGINAL("INST_TYPE_ORIGINAL"), COPY("INST_TYPE_COPY"), NOTIFICATION(
			"INST_TYPE_NOTIFICATION");

	private String value;

	private InstanceType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static InstanceType fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
