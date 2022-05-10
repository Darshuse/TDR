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

@XmlType(name = "DeliveryMode")
@XmlEnum
public enum DeliveryMode {
	REAL_TIME("DELIVERY_MODE_REAL_TIME"), STORE_AND_FORWARD("DELIVERY_MODE_STORE_AND_FORWARD");

	private String value;

	private DeliveryMode(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static DeliveryMode fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
