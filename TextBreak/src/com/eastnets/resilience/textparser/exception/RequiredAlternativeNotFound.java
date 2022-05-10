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

import com.eastnets.resilience.textparser.syntax.entry.Alternative;

/**
 * Required Alternative not found exception
 * 
 * @author EHakawati
 * 
 */
public class RequiredAlternativeNotFound extends RequiredNotFound {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4850446144794984150L;
	private Alternative alternatice;

	public RequiredAlternativeNotFound(Alternative alternative) {
		setAlternatice(alternative);
	}

	public Alternative getAlternatice() {
		return alternatice;
	}

	public void setAlternatice(Alternative alternatice) {
		this.alternatice = alternatice;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "RequiredAlternativeNotFound : Alternative ( " + getAlternatice().getId() + " )";
	}

}
