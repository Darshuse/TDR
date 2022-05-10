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

@XmlType(name = "AuthenticationResult")
@XmlEnum
public enum AuthenticationResult {
	N_A("AUTH_N_A"), SUCCESS("AUTH_SUCCESS"), SUCCESS_OLD_KEY("AUTH_SUCCESS_OLD_KEY"), SUCCESS_FUTURE_KEY(
			"AUTH_SUCCESS_FUTURE_KEY"), FAILURE("AUTH_FAILURE"), NO_KEY("AUTH_NO_KEY"), BYPASSED("AUTH_BYPASSED"), NO_RMA_RECORD(
			"AUTH_NO_RMA_REC"), RMA_NOT_ENABLED("AUTH_RMA_NOT_ENABLED"), RMA_NOT_IN_VALID_PERIOD(
			"AUTH_RMA_NOT_IN_VALID_PERIOD"), RMA_NOT_AUTHORISED("AUTH_RMA_NOT_AUTHORISED"), INVALID_DIGEST(
			"AUTH_RMA_INVALID_DIGEST"), INVALID_SIGN_DN("AUTH_RMA_INVALID_SIGN_DN"), SIGNATURE_FAILURE(
			"AUTH_RMA_SIG_FAILURE"), INVALID_CERT_ID("AUTH_RMA_INVALID_CERT_ID"), NO_RMAU_BKEY("AUTH_RMA_NO_RMAU_BKEY");

	private final String value;

	private AuthenticationResult(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static AuthenticationResult fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
