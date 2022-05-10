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
package com.eastnets.resilience.textparser.syntax;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.eastnets.resilience.textparser.exception.SyntaxNotFound;

/**
 * 
 * @author EHakawati
 * @version 1.0
 */
public class Version {

	/**
	 * Syntax table version
	 */
	private String version;

	/**
	 * Message parsers map
	 */
	private Map<String, Message> messages;

	/**
	 * Constructor
	 * 
	 * @param version
	 */
	public Version(String version) {
		messages = new HashMap<String, Message>();
		setVersion(version);
	}

	/**
	 * Check if message type parser is already loaded
	 * 
	 * @param mesgType
	 * @return
	 */
	public boolean isMessageLoaded(String mesgType) {
		return messages.containsKey(mesgType);
	}

	/**
	 * Get message type parser and load the parser if it is not
	 * 
	 * @param mesgType
	 * @param connection
	 * @return
	 * @throws SyntaxNotFound
	 * @throws SQLException
	 */
	public Message getMessageParser(String mesgType) throws SQLException, SyntaxNotFound {

		// Check if message is not loaded
		if (!this.isMessageLoaded(mesgType)) {
			this.loadMessageParser(mesgType);
		}

		// Return message parser
		return this.messages.get(mesgType);

	}

	/**
	 * Load message parser
	 * 
	 * @param mesgType
	 * @param connection
	 * @throws SyntaxNotFound
	 * @throws SQLException
	 */
	private void loadMessageParser(String mesgType) throws SQLException, SyntaxNotFound {

		// Double check
		if (this.isMessageLoaded(mesgType)) {
			return;
		}

		// Load parser
		Message mesgParser = new Message(mesgType);
		mesgParser.loadMessageType(this.getVersion());

		messages.put(mesgType, mesgParser);

	}

	/**
	 * @return version number
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * set version number
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
