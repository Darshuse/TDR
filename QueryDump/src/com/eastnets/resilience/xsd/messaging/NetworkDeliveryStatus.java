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

@XmlType(name = "NetworkDeliveryStatus")
@XmlEnum
public enum NetworkDeliveryStatus {
	N_A("DLV_N_A"), WAITING_ACK("DLV_WAITING_ACK"), TIMED_OUT("DLV_TIMED_OUT"), ACKED("DLV_ACKED"), NACKED("DLV_NACKED"), REJECTED_LOCALLY(
			"DLV_REJECTED_LOCALLY"), ABORTED("DLV_ABORTED");

	private final String value;

	private NetworkDeliveryStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static NetworkDeliveryStatus fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
