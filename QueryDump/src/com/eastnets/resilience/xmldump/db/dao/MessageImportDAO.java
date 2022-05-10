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

import java.sql.Connection;
import java.sql.SQLException;

import com.eastnets.resilience.xsd.messaging.Appendix;
import com.eastnets.resilience.xsd.messaging.Instance;
import com.eastnets.resilience.xsd.messaging.Intervention;
import com.eastnets.resilience.xsd.messaging.Message;
import com.eastnets.resilience.xsd.messaging.MessageInfo;

/**
 * Full Message Importer
 * 
 * @author EHakawati
 * 
 */
public abstract class MessageImportDAO extends ImporterDAO {
	
	public abstract boolean addMessage(Connection conn, Message message) throws SQLException;

	public abstract void addText(Connection conn, Message message) throws SQLException;

	public abstract void addInstance(Connection conn, Message message, Instance instance) throws SQLException;

	public abstract void addIntervention(Connection conn, Message message, Instance instance, Intervention intervention) throws SQLException;

	public abstract void addAppendix(Connection conn, Message message, Instance instance, Appendix appendix) throws SQLException;

	public abstract MessageInfo getMessageInfoFromDB( Connection conn, int aid, int umidL, int umidH) throws SQLException;
	
	public abstract String filterTextDataBlock(String text);
	
	public abstract String parseXMLHeader (String text);
	
	public abstract String parseXMLDocument (String text, int firstIndex);
	
	public abstract boolean validateXMLType (String text);
}
