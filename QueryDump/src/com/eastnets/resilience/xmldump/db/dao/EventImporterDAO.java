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

import com.eastnets.resilience.xsd.eventjournal.EventReturn;

/**
 * Event(Jrnl) importer
 * 
 * @author EHakawati
 * 
 */
public abstract class EventImporterDAO extends ImporterDAO {

	public abstract boolean addEvent(Connection conn, EventReturn event) throws SQLException;
}
