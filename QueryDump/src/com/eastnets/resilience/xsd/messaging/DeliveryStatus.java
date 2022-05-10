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

@XmlType(name = "DeliveryStatus")
@XmlEnum
public enum DeliveryStatus {
	UNKNOWN("EM_UNKNOWN"), OVERDUE("EM_OVERDUE"), DELIVERED("EM_DELIVERED"), ABORTED("EM_ABORTED"), DELAYED_NAK(
			"EM_DELAYED_NAK"), RE_UACKED("RE_UACKED"), RE_UNACKED("RE_UNACKED"), RE_TRUNCATED("RE_TRUNCATED"), N_A(
			"RE_N_A"), FCP_RELEASED("ADK_EM_FCP_RELEASED"), FCS_PROCESSED("ADK_EM_FCS_PROCESSED"), FCS_NOT_PROCESSED(
			"ADK_EM_FCS_NOT_PROCESSED"), FCS_INACTIVE("ADK_EM_FCS_INACTIVE");

	private String value;

	private DeliveryStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static DeliveryStatus fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
