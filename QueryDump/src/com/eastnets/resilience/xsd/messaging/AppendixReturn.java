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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppendixReturn", propOrder = { "appendix" })
public class AppendixReturn {

	@XmlElement(name = "Appendix", required = true)
	protected Appendix appendix;

	public Appendix getAppendix() {
		return this.appendix;
	}

	public void setAppendix(Appendix paramAppendix) {
		this.appendix = paramAppendix;
	}
}
