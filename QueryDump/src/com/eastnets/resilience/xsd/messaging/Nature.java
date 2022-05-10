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

@XmlType(name = "Nature")
@XmlEnum
public enum Nature {
	FINANCIAL("FINANCIAL_MSG"), TEXT("TEXT_MSG"), NETWORK("NETWORK_MSG"), SECURITY("SECURITY_MSG"), BINARY("BINARY_MSG"), SERVICE(
			"SERVICE_MSG"), OTHER("OTHERS_MSG"), ALL("ALL_MESG"), N_A("N_A_MESG");

	private String value;

	private Nature(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static Nature fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
