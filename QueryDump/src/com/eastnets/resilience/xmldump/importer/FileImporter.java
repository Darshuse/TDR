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

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.db.dao.RestoreSetDAO;
import com.eastnets.resilience.xmldump.db.dao.impl.RestoreSetDAOImpl;
import com.eastnets.resilience.xmldump.logging.Logging;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xmldump.parser.Parser;
import com.eastnets.resilience.xmldump.parser.SaaQueryParser;
import com.eastnets.resilience.xmldump.thread.WorkQueue;
import com.eastnets.resilience.xsd.InputStartEntry;

/**
 * Import XML file to the database
 * 
 * @author EHakawati
 * 
 */
public class FileImporter {

	// logger
	private static final Logger logger = Logging.getLogger(FileImporter.class.getSimpleName());

	private File file;
	private int restoreSet;

	/**
	 * Constructor
	 * 
	 * @param file
	 * @throws SQLException
	 */
	public FileImporter(File file, Connection connection) throws SQLException {
		setFile(file);

		// Log XML importing process starting
		RestoreSetDAO restoreSet = new RestoreSetDAOImpl();
		setRestoreSet(restoreSet.callRestoreSet(RestoreSetDAO.MODE_NEW, getFile().getName(), 0, 0, connection));
	}

	/**
	 * The entry point of file processing execution
	 * 
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 * @throws JAXBException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void importXMLFile(Connection conn) throws FileNotFoundException, XMLStreamException, JAXBException, SQLException,
			InterruptedException {

		GlobalConfiguration config = GlobalConfiguration.getInstance();

		// Initialize XML parser
		Parser parser = new SaaQueryParser(getFile(), config.getMessagePerBatch());

		parser.startParsing();

		List<? extends InputStartEntry> list = null;

		if (config.isDebug() || config.getMaxThreadCount() == 1) {
			// SINGLE thread mode
			logger.info("Run in single thread mode");
			while ((list = parser.getNextBatch()) != null) {

				logger.info("Restore new batch of (" + list.size() + ") entry");

				// Initialize the appropriate importer (message/event)
				ObjectsImporter imporer = ObjectsImporterFactory.getObjectsImporter(parser.getObjectsType(), list,
						getRestoreSet(), conn,config.isForceUpdate());

				// run on the current thread
				imporer.run();

			}
		} else {
			// MULTI thread mode
			logger.info("Run in multi thread mode");

			// 1 = main + 1 pooled = 2 threads
			WorkQueue pool = new WorkQueue(config.getMaxThreadCount() - 1);

			while ((list = parser.getNextBatch()) != null) {

				logger.info("Queue new batch of (" + list.size() + ") entry");

				// Initialize the appropriate importer (message/event)
				ObjectsImporter imporer = ObjectsImporterFactory.getObjectsImporter(parser.getObjectsType(), list,
						getRestoreSet(), conn,config.isForceUpdate());

				// Append to the thread pool
				pool.execute(imporer);
			}

			// wait until thread pool finish
			pool.joinExecution();
		}

		updateRestorSet(conn);

	}

	/**
	 * Update restore statistics
	 * 
	 * @throws SQLException
	 */
	private void updateRestorSet(Connection connection) throws SQLException {

		RestoreSetDAO restoreSet = new RestoreSetDAOImpl();

		restoreSet.callRestoreSet(RestoreSetDAO.MODE_UPDATE, getFile().getName(),
				StatisticsObserver.getMessageCount(getRestoreSet()),
				StatisticsObserver.getTotalRecordsCount(getRestoreSet()), connection);
	}

	/**
	 * @return File
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return
	 */
	public int getRestoreSet() {
		return restoreSet;
	}

	/**
	 * @param restoreSet
	 */
	private void setRestoreSet(int restoreSet) {
		this.restoreSet = restoreSet;
	}
}
