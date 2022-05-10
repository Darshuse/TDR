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
import java.util.logging.Logger;

import com.eastnets.encdec.ConnectionSettings;
import com.eastnets.encdec.EnEcfParser;
import com.eastnets.resilience.xmldump.db.DatabaseType;
import com.eastnets.resilience.xmldump.logging.Logging;

/**
 * User input parameters handler
 * 
 * @author EHakawati
 * 
 */
public class ArgumentHandler {

	// Logger instance
	private static final Logger logger = Logging.getLogger(ArgumentHandler.class.getSimpleName());

	/**
	 * Parse user input parameters into global configuration instance
	 * 
	 * @param args
	 */
	public void fillGlobalSettings(String[] args) {

		// Get global configuration instance
		GlobalConfiguration configuration = GlobalConfiguration.getInstance();
		String fileName = null;
		String folderName = null;
		try {
			for (int index = 0; index < args.length; index++) {
				String param = args[index];

				if ("-aid".equalsIgnoreCase(param)) {
					// allianceId
					index++;
					configuration.setAllianceId(Integer.parseInt(args[index]));
				} else if ("-dbtype".equalsIgnoreCase(param)) {
					// ORACLE/MSSQL
					index++;
					configuration.setDatabaseType(DatabaseType.valueOf(args[index].toUpperCase()));
				} else if ("-u".equalsIgnoreCase(param)) {
					// User name
					index++;
					configuration.setUserName(args[index]);
				} else if ("-p".equalsIgnoreCase(param)) {
					// Password
					index++;
					configuration.setPassword(args[index]);
				} else if ("-dbname".equalsIgnoreCase(param)) {
					// database name
					index++;
					configuration.setServiceName(args[index]);
					configuration.setDbServiceName(false);
				} 
				else if ("-dbservicename".equalsIgnoreCase(param)) {
					// SID
					index++;
					configuration.setServiceName(args[index]);
					configuration.setDbServiceName(true);
					
				} else if ("-instancename".equalsIgnoreCase(param)) {
					// instance name
					index++;
					configuration.setInstanceName(args[index]);
				} 
				else if ("-port".equalsIgnoreCase(param)) {
					// database port
					index++;
					configuration.setPortNumber(Integer.parseInt(args[index]));
				} else if ("-ip".equalsIgnoreCase(param)) {
					// database server IP/name
					index++;
					configuration.setServerName(args[index]);
				} else if ("-file".equalsIgnoreCase(param)) {
					// archive file path
					index++;
					fileName = args[index];
				} else if ("-folder".equalsIgnoreCase(param)) {
					// archive folder path
					index++;
					folderName = args[index];
				} else if ("-threads".equalsIgnoreCase(param)) {
					// maximum number of working threads
					index++;
					configuration.setMaxThreadCount(Integer.parseInt(args[index]));
				} else if ("-batch".equalsIgnoreCase(param)) {
					// StAX reading batch size
					index++;
					configuration.setMessagePerBatch(Integer.parseInt(args[index]));
				} else if ("-level".equalsIgnoreCase(param)) {
					// Logging level
					index++;
					configuration.setLogLevel(args[index].toUpperCase());
				} else if ("-debug".equalsIgnoreCase(param)) {
					// Debug
					configuration.setDebug(true);
				} else if ("-partitioned".equalsIgnoreCase(param)) {
					// Partitioned
					configuration.setPartitioned(true);
				}else if ("-force".equalsIgnoreCase(param)) {
					// Force Update
					configuration.setForceUpdate(true);
				} else if ("-ecf".equalsIgnoreCase(param)) {
					// ECF
					index++;
					configuration.setEcfFilename(args[index]);
					ConnectionSettings cs = EnEcfParser.parseECF(args[index]);
					if ( cs.getServerName() != null  ){
						configuration.setServerName(cs.getServerName()) ;
					}
					if ( cs.getPassword() != null  ){
						configuration.setPassword(cs.getPassword() ) ;
					}
					if ( cs.getPortNumber() != null  ){
						configuration.setPortNumber(cs.getPortNumber() ) ;
					}
					if ( cs.getServiceName() != null  ){
						configuration.setServiceName(cs.getServiceName() ) ;
					}
					if ( cs.getUserName() != null  ){
						configuration.setUserName(cs.getUserName() ) ;
					}
				} else if ("-h".equalsIgnoreCase(param)) {
					// Help message
					displayUsage();
					System.exit(1);
				}
			}
			// double check configuration
			checkConfiguration();

			bindArchiveFiles(folderName, fileName);

		} catch (Exception ex) {
			logger.severe(ex.getMessage());
			displayUsage();
			System.exit(1);
		}

	}

	/**
	 * List the required archive files
	 * 
	 * @param folderName
	 * @param fileName
	 * @throws Exception
	 */
	private void bindArchiveFiles(String folderName, String fileName) throws Exception {

		GlobalConfiguration configuration = GlobalConfiguration.getInstance();

		if (fileName != null) {
			if(folderName!=null) {
				throw new Exception("Both -file and -folder can not be set.");
			}
			configuration.addArchiveFile(new File(fileName));
		} else if (folderName != null) {

			File folder = new File(folderName);
			
			if(folder.exists()) {
				for (String name : folder.list()) {
					if (name.endsWith(".xml")) {
						configuration.addArchiveFile(new File(folderName + File.separator + name));
					}
				}
			} else {
				throw new Exception("input 'file or folder' is not valid");
			}
		} else {
			throw new Exception("Mandatory input 'file or folder' is missing");
		}

	}

	/**
	 * System usage message
	 */
	private void displayUsage() {
		StringBuilder display = new StringBuilder();

		display.append("\n");		
		display.append(" { ( -u <user name> -p <password>... : User name and password to connect to database.\n");
		display.append("     -dbname <database name> ... : database name to connect to .\n");
		display.append("     -instancename <instance name for MSSQL database> ... : database instance name to connect to .\n");
		display.append("     -dbservicename <SID for ORACLE database> ... : service name to connect to .\n");
		display.append("     [-dbtype oracle|mssql] ........ : Oracle or MS SQL database, default is oracle.\n");
		display.append("     [-ip <ip/server name>] ........ : The ip address or server name of the database server, default is localhost.\n");
		display.append("     [-port <db port>] ) ........... : Database port, default 1521 for oracle and 1433 for mssql.\n");
		display.append("   OR -ecf <ECF file> } ............ : pass the Encrypted Connection File to connect to database.\n");
		display.append(" -aid <alliance id> ................ : AID to restore messages/events to.\n");
		display.append(" {   -folder < path > .............. : Events/Messages SAA_QUERY dump folder.\n");
		display.append("  OR -file < path > } .............. : Events/Messages SAA_QUERY dump file.\n");
		display.append(" [-threads <number>] ............... : maximum number of workers, default is 1.\n");
		display.append(" [-batch <number>] ................. : maximum number of messages per worker, default is 200.\n");
		display.append(" [-partitioned] .................... : used in the case of a partitioned database.\n");
		display.append(" [-level <SEVERE|WARNING|INFO>] .... : output level, default is WARNING.\n");
		display.append(" [-debug] .......................... : enable debugging, -threads will be forced to 1.\n");
		display.append(" [-h] .............................. : display this help menu and exit.\n");
		display.append(" [-force] .......................... : force update messages.\n");

		
		
		
		System.out.println(display.toString());
	}

	/**
	 * Check configuration validity
	 * 
	 * @throws Exception
	 */
	private void checkConfiguration() throws Exception {

		GlobalConfiguration configuration = GlobalConfiguration.getInstance();

		if (configuration.getUserName() == null) {
			throw new Exception("Mandatory input 'username' is missing");
		}
		if (configuration.getPassword() == null) {
			throw new Exception("Mandatory input 'password' is missing");
		}
		if (configuration.getServiceName() == null) {
			throw new Exception("Mandatory input 'dbname' is missing");
		}
		if (configuration.getAllianceId() == null) {
			throw new Exception("Mandatory input 'aid' is missing");
		}
	}
}
