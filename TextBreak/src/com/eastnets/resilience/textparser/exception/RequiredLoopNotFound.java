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

import com.eastnets.resilience.textparser.syntax.entry.Loop;

public class RequiredLoopNotFound extends RequiredNotFound {

	/**
	 * 
	 */
	private static final long serialVersionUID = -749152846574604643L;
	private Loop loop;

	public RequiredLoopNotFound(Loop loop) {
		setLoop(loop);
	}

	public Loop getLoop() {
		return loop;
	}

	public void setLoop(Loop loop) {
		this.loop = loop;
	}

	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "RequiredLoopNotFound : Loop ( " + loop.getId() + " )";
	}
}
