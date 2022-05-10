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

import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedMessage;

/**
 * custom exception
 * 
 * @author EHakawati
 */
public class UnrecognizedBlockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5660166187491125923L;
	private ParsedMessage parsedField;

	public UnrecognizedBlockException(ParsedMessage parsedField) {
		setParsedMessage(parsedField);
	}

	public ParsedMessage getParsedMessage() {
		return parsedField;
	}

	public void setParsedMessage(ParsedMessage parsedField) {
		this.parsedField = parsedField;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {

		StringBuilder strExt = new StringBuilder("UnrecognizedBlockException :\n");

		while (this.getParsedMessage().hasNextTextEntry()) {
			ParsedField pField = this.getParsedMessage().getNextTextEntry();
			strExt.append("Field : " + pField.getFieldCode());

			strExt.append(":");
			strExt.append(pField.getValue());
			strExt.append("\n");
		}

		return strExt.toString();
	}

}
