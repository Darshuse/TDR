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
package com.eastnets.resilience.xmldump;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;
import com.eastnets.resilience.xmldump.db.DBConnection;
import com.eastnets.resilience.xmldump.db.DatabaseType;
import com.eastnets.resilience.xmldump.db.dao.GlobalSettingsDAO;
import com.eastnets.resilience.xmldump.db.dao.GlobalSettingsDAO.Setting;
import com.eastnets.resilience.xmldump.db.dao.impl.GlobalSettingsDAOImpl;
import com.eastnets.resilience.xmldump.importer.FileImporter;
import com.eastnets.resilience.xmldump.logging.Logging;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xmldump.utils.GlobalSettings;

/**
 * Application entry point
 * 
 * @author EHakawati
 * 
 */
public class Main {

	// private static final String RESTORE_LIECENSE_NAME = "01";
	// Logger instance
	public static Logger logger;

	// used to license/check license for the xml dump module
	public static final String PRODUCT_ID = "17";

	/**
	 * Application main function
	 * 
	 * @param args
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws JAXBException, FileNotFoundException, XMLStreamException, InterruptedException, SQLException {

		try {
			logger = Logging.getLogger(Main.class.getSimpleName());

			printProductInfo();
			long startTime = System.currentTimeMillis();

			// Input parameter parsing
			ArgumentHandler argumentHandler = new ArgumentHandler();
			argumentHandler.fillGlobalSettings(args);

			GlobalConfiguration globalConfiguration = GlobalConfiguration.getInstance();

			// query updates so that we can support users other than side
			if (globalConfiguration.getDatabaseType() == DatabaseType.ORACLE) {
				GlobalSettings.schemaName = "side";
				GlobalSettings.db_postfix = "";
			} else {
				GlobalSettings.schemaName = "dbo";
				GlobalSettings.db_postfix = "_sql";
			}

			// print the configuration
			System.out.println();
			printConfiguration(globalConfiguration);
			Connection conn = null;
			// test Database connection
			System.out.println();
			System.out.println("Checking database connection...");
			try {
				conn = DBConnection.getConnection();
			} catch (Exception e) {
				System.err.println("Database connection error: " + e.getMessage());
				System.exit(1);
			}

			System.out.println("Database check succeeded");

			System.out.println("Checking license...");
			XmlRestoreApp app = new XmlRestoreApp(getConfigFromArgs(globalConfiguration));
			if (!app.isLicensed()) {
				System.err.println("XmlRestore is not licensed, please contact EastNets support to obtain a license.");
				System.exit(1);
			}
			System.out.println("License check succeeded");
			System.out.println();

			printDate("Restore started");

			logger.info("Fill livesource/textblock indicators");
			// System global settings (Live source and text break status)
			GlobalSettingsDAO dao = new GlobalSettingsDAOImpl();
			Map<Setting, String> settings = dao.getGlobalSettings(conn);

			// Fill with MESG or INTV
			globalConfiguration.setLiveSource(settings.get(Setting.LIVE_SOURCE));
			// convert to boolean
			globalConfiguration.setTextBreakEnabled("0".equals(settings.get(Setting.PARSE_TEXTBLOCK)) ? false : true);

			// status loop (disabled in debug mode)
			if (!globalConfiguration.isDebug()) {
				StatisticsObserver.startSummayLoop();
			}

			logger.info("Start file importing");
			// find archive files list
			List<File> files = globalConfiguration.getArchiveFiles();
			for (File file : files) {
				logger.info("Importing " + file.getName());
				// Start archive importing
				FileImporter fileImporter = new FileImporter(file, conn);
				fileImporter.importXMLFile(conn);
			}

			StatisticsObserver.stopSummayLoop();

			long seconds = ((System.currentTimeMillis() - startTime) / 1000);

			System.out.println();
			printDate("Done in " + seconds + " seconds - with " + StatisticsObserver.getSummary());

		} catch (Exception ex) {
			logger.severe(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
	}

	private static void printDate(String postfix) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println(String.format("%s : %s", df.format(new Date()), postfix));

	}

	private static void printConfiguration(GlobalConfiguration globalConfiguration) {
		System.out.println(String.format("Database server           : %s.", globalConfiguration.getDatabaseType() == DatabaseType.ORACLE ? "Oracle" : "MS-SQL Server"));
		if (globalConfiguration.getEcfFilename() == null) {
			System.out.println(String.format("Database Connection       : %s/*****@%s:%d/%s", globalConfiguration.getUserName(), globalConfiguration.getServerName(), globalConfiguration.getPortNumber(),
					globalConfiguration.getDatabaseType() == DatabaseType.ORACLE ? globalConfiguration.getServiceName()
							: globalConfiguration.getInstanceName() == null || globalConfiguration.getInstanceName().isEmpty() ? globalConfiguration.getServiceName() : globalConfiguration.getInstanceName()));

		} else {
			System.out.println(String.format("Encrypted connection file : %s", globalConfiguration.getEcfFilename()));
		}

		System.out.println(String.format("Alliance ID               : %d", globalConfiguration.getAllianceId()));
		System.out.println(String.format("Max. Threads              : %d", globalConfiguration.getMaxThreadCount()));
		System.out.println(String.format("Log level                 : %s", globalConfiguration.getLogLevel()));
		System.out.println(String.format("Messages per batch        : %d", globalConfiguration.getMessagePerBatch()));

		System.out.println(String.format("Partitioned database      : %s", (globalConfiguration.isPartitioned() ? "True" : "False")));
	}

	private static void printProductInfo() {
		System.out.println(String.format("%s %s.%s.%s Build %s", XmlRestoreConstants.PRODUCT_NAME, XmlRestoreConstants.PRODUCT_VERSION_MAJOR, XmlRestoreConstants.PRODUCT_VERSION_MINOR, XmlRestoreConstants.PRODUCT_VERSION_PATCH,
				XmlRestoreConstants.PRODUCT_VERSION_BUILD));

	}

	private static ConfigBean getConfigFromArgs(GlobalConfiguration configuration) {
		ConfigBean config = new ConfigBean();

		if (configuration.isDbServiceName() == true)
			config.setDbServiceName(configuration.getServiceName());
		else
			config.setDatabaseName(configuration.getServiceName());

		config.setServerName(configuration.getServerName());

		try {
			config.setPortNumber("" + configuration.getPortNumber());
		} catch (Exception e) {
		}

		config.setUsername(configuration.getUserName());
		try {
			config.setPasswordText(configuration.getPassword());
		} catch (Exception e) {
		}

		config.setSchemaName(configuration.getDatabaseType() == DatabaseType.ORACLE ? "side" : "dbo");
		config.setTableSpace("sidedb");
		config.setDatabaseType(configuration.getDatabaseType() == DatabaseType.ORACLE ? DBType.ORACLE : DBType.MSSQL);
		return config;
	}
}
