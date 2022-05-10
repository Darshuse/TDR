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

@XmlType(name = "PossibleDuplicateCreation")
@XmlEnum
public enum PossibleDuplicateCreation {
	PDE("PDE"), PDR("PDR"), PDE_PDR("PDE_PDR"), N_A("DUP_N_A");

	private final String value;

	private PossibleDuplicateCreation(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static PossibleDuplicateCreation fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
