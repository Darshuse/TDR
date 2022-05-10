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

import com.eastnets.resilience.textparser.syntax.entry.Option;
import com.eastnets.resilience.textparser.syntax.entry.Option.OptionEntry;

/**
 * Required Option not found exception
 * 
 * @author EHakawati
 * 
 */
public class RequiredOptionNotFound extends RequiredNotFound {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7062703961420983620L;
	private Option option;

	public RequiredOptionNotFound(Option option) {
		setOption(option);
	}

	public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {

		StringBuilder errMess = new StringBuilder();

		errMess.append("RequiredOptionNotFound : Option ");
		errMess.append(getOption().getTag());
		errMess.append(" ( ");

		for (OptionEntry entry : getOption().getOptions()) {
			errMess.append(entry.getOptionChoice());
			errMess.append(" ");
		}
		errMess.append(")");

		return errMess.toString();
	}

}
