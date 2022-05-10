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
package com.eastnets.resilience.xsd;

import com.eastnets.resilience.xmldump.utils.StringUtils;

/**
 * Base of Message/Instance/Intervention/Text/Appendix
 * 
 * @author EHakawati
 * 
 */
public abstract class BaseObject {

	public abstract int getUmidL();

	public abstract int getUmidH();

	public int getObjectCRC() {
		int crc = 0;
		crc = this.hashCode();
		crc = StringUtils.computeCRC(new Integer(crc).toString());
		return crc;
	}
}
