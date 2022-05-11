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

package com.eastnets.domain.archive;

import java.io.Serializable;
import java.util.List;

/**
 * RestoreOptions POJO
 * @author EastNets
 * @since September 19, 2012
 */
public abstract class RestoreOptions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -54060391559186264L;
	private String execPath;
	private String userName;
	private String password;
	private String serverName;
	private String databaseName;
	private String databasePortNumber;
	private String swiftVersion;
	private boolean showRestored;
	private boolean isTrustedConnection;
	private boolean force;
	
	private String instanceName;

	private List<ArchiveSettings> restoreSettings;
	
	public String getExecPath() {
		return execPath;
	}
	public void setExecPath(String execPath) {
		this.execPath = execPath;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public String getSwiftVersion() {
		return swiftVersion;
	}
	public void setSwiftVersion(String swiftVersion) {
		this.swiftVersion = swiftVersion;
	}
	public List<ArchiveSettings> getRestoreSettings() {
		return restoreSettings;
	}
	public void setRestoreSettings(List<ArchiveSettings> restoreSettings) {
		this.restoreSettings = restoreSettings;
	}
	/**
	 * @param showRestored the showRestored to set
	 */
	public void setShowRestored(boolean showRestored) {
		this.showRestored = showRestored;
	}
	/**
	 * @return the showRestored
	 */
	public boolean isShowRestored() {
		return showRestored;
	}
	

	public boolean isTrustedConnection() {
		return isTrustedConnection;
	}
	public void setTrustedConnection(boolean isTrustedConnection) {
		this.isTrustedConnection = isTrustedConnection;
	}
	
	public void setForce(boolean force) {
		
		this.force = force;
		
	}
	
	public boolean isForce() {
		return this.force;
		
	}
	public String getDatabasePortNumber() {
		return databasePortNumber;
	}
	public void setDatabasePortNumber(String databasePortNumber) {
		this.databasePortNumber = databasePortNumber;
	}

	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	
}
