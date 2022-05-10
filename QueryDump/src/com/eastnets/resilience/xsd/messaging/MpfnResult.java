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

@XmlType(name = "MpfnResult")
@XmlEnum
public enum MpfnResult {
	N_A("R_N_A"), NONE("R_NONE"), V1("R_V1"), V2("R_V2"), V3("R_V3"), V4("R_V4"), V5("R_V5"), V6("R_V6"), V7("R_V7"), V8(
			"R_V8"), V9("R_V9"), V10("R_V10"), V11("R_V11"), V12("R_V12"), V13("R_V13"), V14("R_V14"), V15("R_V15"), V16(
			"R_V16"), V17("R_V17"), V18("R_V18"), V19("R_V19"), V20("R_V20"), WILDCARD("R_WILDCARD"), FAILURE(
			"R_FAILURE"), SUCCESS("R_SUCCESS");

	private final String value;

	private MpfnResult(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static MpfnResult fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
