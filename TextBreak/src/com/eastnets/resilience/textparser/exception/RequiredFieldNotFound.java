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

import com.eastnets.resilience.textparser.syntax.entry.Field;

/**
 * Required field not found exception
 * 
 * @author EHakawati
 * 
 */
public class RequiredFieldNotFound extends RequiredNotFound {

	/**
	 * 
	 */
	private static final long serialVersionUID = 826352779777040806L;
	private Field field;

	public RequiredFieldNotFound(Field field) {
		setField(field);
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "RequiredFieldNotFound : Field ( " + getField().getTag() + " ," + getField().getId() + " )";
	}

}
