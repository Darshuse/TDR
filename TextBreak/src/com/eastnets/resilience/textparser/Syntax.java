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

package com.eastnets.resilience.textparser;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.resilience.textparser.syntax.Version;
import com.eastnets.resilience.textparser.util.GlobalSettings;

/**
 * 
 * @author EHakawati
 * @version 1.0 Syntax Tree Holder
 */
public class Syntax {

	/**
	 * Syntax Version Parser Holder
	 */
	private static Map<String, Version> syntaxsMap;
	private static Connection connection;

	/**
	 * Static initializer
	 */
	static {
		syntaxsMap = new HashMap<String, Version>();
	}

	/**
	 * Private Constructor
	 */
	private Syntax() {
		// Nothing to do
	}

	/**
	 * Parser entry point
	 * 
	 * @param syntaxVersion
	 * @param mesgType
	 * @param connection
	 * @return Message Parser
	 * @throws SyntaxNotFound
	 * @throws SQLException
	 */
	public static Message getParser(String syntaxVersion, String mesgType, Connection connection) throws SQLException,
			SyntaxNotFound {

		// Add DB session reference
		Syntax.connection = connection;
		
		//query updats so that we can support users other than side 
		if ( connection.getMetaData().getDatabaseProductName().equalsIgnoreCase("Oracle") ){
			GlobalSettings.schemaName= "side";
			GlobalSettings.db_postfix= "";
		}
		else {
			GlobalSettings.schemaName= "dbo";
			GlobalSettings.db_postfix= "_sql";
		}

		// Check version loaded
		if (!syntaxsMap.containsKey(syntaxVersion)) {
			syntaxsMap.put(syntaxVersion, new Version(syntaxVersion));
		}

		// Return message type parser
		return syntaxsMap.get(syntaxVersion).getMessageParser(mesgType);

	}

	/**
	 * Get syntax DB session
	 * 
	 * @return
	 */
	public static Connection getDBSession() {
		return Syntax.connection;
	}
}
