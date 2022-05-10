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

import com.eastnets.resilience.textparser.syntax.entry.Sequence;

/**
 * Required sequence not found
 * 
 * @author EHakawati
 * 
 */
public class RequiredSequenceNotFound extends RequiredNotFound {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7243485456702218981L;
	private Sequence sequence;

	public RequiredSequenceNotFound(Sequence sequence) {
		setSequence(sequence);
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "RequiredSequenceNotFound : Sequence ( " + getSequence().getId() + " )";
	}

}
