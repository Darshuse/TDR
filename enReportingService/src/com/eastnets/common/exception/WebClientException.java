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

package com.eastnets.common.exception;

/**
 * EnReporting WebClient Exception
 * @author EastNets
 * @since July 17, 2012
 */
public class WebClientException extends RuntimeException {

	private static final long serialVersionUID = 4006320485167690791L;
	private int errorCode;

	public WebClientException() {
		super();
	}

	public WebClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebClientException(String message) {
		super(message);
	}

	public WebClientException(Throwable cause) {
		super(cause);
	}
	public WebClientException(Throwable cause, int errorCode) {
		super(cause);
		this.errorCode=errorCode;
		// TODO Auto-generated constructor stub
	}
	public int getErrorCode() {
		return errorCode;
	}

}
