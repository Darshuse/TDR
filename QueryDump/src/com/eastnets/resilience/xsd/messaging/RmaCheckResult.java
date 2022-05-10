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

@XmlType(name = "RmaCheckResult")
@XmlEnum
public enum RmaCheckResult {
	N_A("RMA_CHECK_N_A"), SUCCESS("RMA_CHECK_SUCCESS"), FAILURE("RMA_CHECK_FAILURE"), BYPASSED("RMA_CHECK_BYPASSED"), NO_RMA_RECORD(
			"RMA_CHECK_NO_REC"), RMA_NOT_ENABLED("RMA_CHECK_NOT_ENABLED"), RMA_NOT_IN_VALID_PERIOD(
			"RMA_CHECK_NOT_IN_VALID_PERIOD"), RMA_NOT_AUTHORISED("RMA_CHECK_NOT_AUTHORISED");

	private final String value;

	private RmaCheckResult(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static RmaCheckResult fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
