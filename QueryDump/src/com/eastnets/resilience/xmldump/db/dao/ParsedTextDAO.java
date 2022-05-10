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
package com.eastnets.resilience.xmldump.db.dao;

import java.sql.SQLException;
import java.util.List;

import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.xsd.messaging.Message;

/**
 * Text break object DAO
 * 
 * @author EHakawati
 * 
 */
public abstract class ParsedTextDAO extends ImporterDAO {

	public abstract void addTextFields(Message message, ParsedMessage parsedMessage) throws SQLException;

	public abstract void addTextLoops(Message message, ParsedMessage parsedMessage) throws SQLException;

	public abstract void addSystemText(Message message, List<String[]> systemTexts) throws SQLException;

}
