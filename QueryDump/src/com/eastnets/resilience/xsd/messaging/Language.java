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

@XmlType(name = "Language")
@XmlEnum
public enum Language {
	NONE("SWA_LANG_NONE"), ENGLISH("SWA_LANG_ENGLISH"), FRENCH("SWA_LANG_FRENCH"), GERMAN("SWA_LANG_GERMAN"), ITALIAN(
			"SWA_LANG_ITALIAN"), SPANISH("SWA_LANG_SPANISH");

	private final String value;

	private Language(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static Language fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
