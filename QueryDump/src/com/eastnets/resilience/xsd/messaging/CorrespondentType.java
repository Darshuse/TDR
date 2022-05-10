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

@XmlType(name = "CorrespondentType")
@XmlEnum
public enum CorrespondentType {
	CORR_TYPE_INSTITUTION, CORR_TYPE_DEPARTMENT, CORR_TYPE_INDIVIDUAL, CORR_TYPE_N_A;

	public String value() {
		return name();
	}

	public static CorrespondentType fromValue(String paramString) {
		return valueOf(paramString);
	}

	@Override
	public String toString() {
		return value();
	}
}
