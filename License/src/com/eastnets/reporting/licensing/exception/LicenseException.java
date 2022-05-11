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

package com.eastnets.reporting.licensing.exception;

/**
 * EnReporting WebClient Exception
 * @author EastNets
 * @since July 17, 2012
 */
public class LicenseException extends RuntimeException {

	private static final long serialVersionUID = 4006320485167690791L;
	private int errorCode;

	public LicenseException() {
		super();
	}

	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseException(String message) {
		super(message);
	}

	public LicenseException(Throwable cause) {
		super(cause);
	}
	public int getErrorCode() {
		return errorCode;
	}

}
