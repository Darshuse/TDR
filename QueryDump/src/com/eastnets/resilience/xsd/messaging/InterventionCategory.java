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

@XmlType(name = "InterventionCategory")
@XmlEnum
public enum InterventionCategory {
	NONE("INTY_NONE"), ROUTING("INTY_ROUTING"), SECURITY("INTY_SECURITY"), NETWORK_REPORT("INTY_NETWORK_REPORT"), DELIVERY_REPORT(
			"INTY_DELIVERY_REPORT"), MESG_AS_TRANSMITTED("INTY_MESG_AS_TRANSMITTED"), MESG_AS_RECEIVED(
			"INTY_MESG_AS_RECEIVED"), MESG_MODIFIED("INTY_MESG_MODIFIED"), MESG_SCISSOR("INTY_MESG_SCISSOR"), OTHER(
			"INTY_OTHER"), TRANSMISSION_RESPONSE("INTY_TRANSMISSION_RESPONSE");

	private final String value;

	private InterventionCategory(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static InterventionCategory fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
