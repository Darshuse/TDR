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
public class SyntaxNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1956486544819790817L;
	private String messageType;
	private String version;

	/**
	 * constructor
	 * 
	 * @param version
	 * @param messageType
	 */
	public SyntaxNotFound(String version, String messageType) {
		super();
		setMessageType(messageType);
		setVersion(version);

	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "SyntaxNotFound : Version ( " + getVersion() + " ), Type ( " + getMessageType() + " )";
	}

}
