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
 * custom exception
 * 
 * @author EHakawati
 * 
 */
public class CurrencyNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8371236767361064393L;
	private String code;

	/**
	 * constructor
	 * 
	 * @param code
	 */
	public CurrencyNotFound(String code) {
		super();
		setCode(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "CurrencyNotFound : Version ( " + getCode() + " )";
	}

}
