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
import java.util.LinkedList;
import java.util.List;

import com.eastnets.resilience.xmldump.db.DatabaseType;
import com.eastnets.resilience.xmldump.logging.Logging;

/**
 * System Shared configuration
 * 
 * @author EHakawati
 * 
 */
public class GlobalConfiguration {

	// Singleton Object
	private static GlobalConfiguration SingletonObject;

	private DatabaseType databaseType;
	private String userName;
	private String password;
	private String serviceName;
	private String instanceName;
	private boolean dbServiceName;
	private Integer portNumber;
	private String serverName = "localhost";
	private boolean partitioned;
	private Integer allianceId;
	private boolean debug;
	private int maxThreadCount = 1;
	private int messagePerBatch = 200;
	private String logLevel;
	private String liveSource;
	private boolean textBreakEnabled;
	private List<File> archives = new LinkedList<File>();
	private String ecfFilename;
	private boolean forceUpdate = false;
	GlobalConfiguration() 
	{
		setLogLevel("WARNING");
	}

	
	public boolean isDbServiceName() {
		return dbServiceName;
	}

	public void setDbServiceName(boolean dbServiceName) {
		this.dbServiceName = dbServiceName;
	}
	
	
	/**
	 * @return List of archive files
	 */
	public List<File> getArchiveFiles() {
		return archives;
	}

	/**
	 * @param file
	 */
	public void addArchiveFile(File file) {
		archives.add(file);
	}

	/**
	 * @return MESG or INST
	 */
	public String getLiveSource() {
		return liveSource;
	}

	/**
	 * @param liveSource
	 */
	public void setLiveSource(String liveSource) {
		this.liveSource = liveSource;
	}

	/**
	 * @return boolean
	 */
	public boolean isTextBreakEnabled() {
		return textBreakEnabled;
	}

	/**
	 * @param textBreakEnabled
	 */
	public void setTextBreakEnabled(boolean textBreakEnabled) {
		this.textBreakEnabled = textBreakEnabled;
	}

	/**
	 * @return log level
	 */
	public String getLogLevel() {
		return logLevel;
	}

	/**
	 * @param logLevel
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
		Logging.changeLogLevel(this.logLevel);
	}

	/**
	 * @return boolean
	 */
	public boolean isPartitioned() {
		return partitioned;
	}

	/**
	 * @param partitioned
	 */
	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	/**
	 * @return server name/IP
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return database port number
	 */
	public int getPortNumber() {
		if (portNumber == null || portNumber == 0 ) {
			if (this.databaseType == DatabaseType.ORACLE) {
				portNumber = 1521;
			} else {
				portNumber = 1433;
			}
		}
		return portNumber;
	}

	/**
	 * @return max thread count
	 */
	public int getMaxThreadCount() {
		return maxThreadCount;
	}

	/**
	 * @param maxThreadCount
	 */
	public void setMaxThreadCount(int maxThreadCount) {
		if (maxThreadCount > 10) {
			Main.logger.warning("threads should not be above 10; considering 10 instead.");
			this.maxThreadCount = 10;
		} else if (maxThreadCount < 1) {
			Main.logger.warning("threads should not be below 1; considering 1 instead.");
			this.maxThreadCount = 1;
		} else {
			this.maxThreadCount = maxThreadCount;
		}
	}

	/**
	 * @param port
	 */
	public void setPortNumber(int port) {
		if(port <= 0) {
			Main.logger.warning("invalid port number; considering default.");
		}
		this.portNumber = port;
	}

	/**
	 * @return database user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param database
	 *            user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return database password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return SID/database name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return singleton object
	 */
	public static GlobalConfiguration getInstance() {

		if (SingletonObject == null) {
			SingletonObject = new GlobalConfiguration();
		}

		return SingletonObject;
	}

	/**
	 * @return database type
	 */
	public DatabaseType getDatabaseType() {
		if (databaseType == null) {
			databaseType = DatabaseType.ORACLE;
		}
		return databaseType;
	}

	/**
	 * set database type
	 * 
	 * @param databaseType
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * @return
	 */
	public Integer getAllianceId() {
		return allianceId;
	}

	/**
	 * @param allianceId
	 */
	public void setAllianceId(int allianceId) {
		this.allianceId = allianceId;
	}

	/**
	 * @return
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return message max count per StAX batch
	 */
	public int getMessagePerBatch() {
		return messagePerBatch;
	}

	/**
	 * Max of 2000 and minimum of 100
	 * 
	 * @param messagePerBatch
	 */
	public void setMessagePerBatch(int messagePerBatch) {
		if (messagePerBatch > 2000) {
			Main.logger.warning("batch should not be above 2000; considering 2000 instead.");
			this.messagePerBatch = 2000;
		} else if (messagePerBatch < 100) {
			Main.logger.warning("batch should not be below 100; considering 100 instead.");
			this.messagePerBatch = 100;
		} else {
			this.messagePerBatch = messagePerBatch;
		}
	}

	public String getEcfFilename() {
		return ecfFilename;
	}

	public void setEcfFilename(String ecfFilename) {
		this.ecfFilename = ecfFilename;
	}
	
	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

}
