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
package com.eastnets.resilience.textparser.syntax;

import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;

/**
 * Base entry class
 * 
 * @author EHakawati
 * 
 */
abstract public class Entry {

	abstract public boolean isOptional();

	abstract public boolean isValid(ParsedMessage parsedMessage) throws RequiredNotFound;

	public void throwException() throws RequiredNotFound {
		throw new RequiredNotFound();

	}

}
