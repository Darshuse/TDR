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
package com.eastnets.resilience.textparser.exception;

/**
 * Base parsing exception
 * 
 * @author EHakawati
 */
public class RequiredNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3662969564671779447L;

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "RequiredNotFound : Required entry not found";
	}
}
