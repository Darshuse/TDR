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
package com.eastnets.resilience.xmldump.importer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

import com.eastnets.resilience.xmldump.db.DBConnection;
import com.eastnets.resilience.xmldump.db.dao.EventImporterDAO;
import com.eastnets.resilience.xmldump.db.dao.impl.EventImporterDAOImpl;
import com.eastnets.resilience.xmldump.logging.Logging;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xsd.eventjournal.EventReturn;

/**
 * Dumping an EventReturn list to database
 * 
 * @author EHakawati
 * 
 */
public class EventImporter extends Observable implements ObjectsImporter {

	// logging
	private static final Logger logger = Logging.getLogger(EventImporter.class.getSimpleName());

	private List<EventReturn> events;
	private int restoreSet;
	private Connection conn;

	

	/**
	 * Constructor
	 * 
	 * @param events
	 * @param restoreSet
	 */
	public EventImporter(List<EventReturn> events, int restoreSet, Connection conn) {
		// register observer
		addObserver(new StatisticsObserver());
		this.events = events;
		this.restoreSet = restoreSet;
		this.conn = conn;
	}

	/**
	 * Thread method implementation
	 */
	@Override
	public void run() {

		// Notify statistics observer about a new worker starting
		setChanged();
		notifyObservers(+1);

		EventImporterDAO dao = new EventImporterDAOImpl();

		try {

			for (EventReturn event : this.events) {
				dao.addEvent(conn, event);
			}

		} catch (SQLException ex) {
			logger.severe(ex.getMessage());
		}

		// notify statistics observer about a new worker ends
		setChanged();
		notifyObservers(-1);

	}

	/**
	 * @return restore set id
	 */
	public int getRestoreSet() {
		return restoreSet;
	}

	/**
	 * @param restoreSet
	 */
	public void setRestoreSet(int restoreSet) {
		this.restoreSet = restoreSet;
	}
	
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}