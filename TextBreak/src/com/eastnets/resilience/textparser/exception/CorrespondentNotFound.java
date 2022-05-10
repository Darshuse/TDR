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
public class CorrespondentNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6033873298311147834L;
	private String bic;

	/**
	 * constructor
	 * 
	 * @param code
	 */
	public CorrespondentNotFound(String bic) {
		super();
		setBic(bic);
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "CorrespondentNotFound : Version ( " + getBic() + " )";
	}

}
