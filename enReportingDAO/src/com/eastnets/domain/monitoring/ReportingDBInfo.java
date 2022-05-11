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

package com.eastnets.domain.monitoring;

import java.io.Serializable;

/**
 * ReportingDBInfo POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class ReportingDBInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5088406856298092479L;
	private String InstallationDate;
	private String InstallationUser;
	private String Major;
	private String Minor;
	private String Revision;
	private String databaseVersion;
	private String lastDatabaseUpdate;

	public String getInstallationDate() {
		return InstallationDate;
	}

	public void setInstallationDate(String installationDate) {
		InstallationDate = installationDate;
	}

	public String getInstallationUser() {
		return InstallationUser;
	}

	public void setInstallationUser(String installationUser) {
		InstallationUser = installationUser;
	}

	public String getMajor() {
		return Major;
	}

	public void setMajor(String major) {
		Major = major;
	}

	public String getMinor() {
		return Minor;
	}

	public void setMinor(String minor) {
		Minor = minor;
	}

	public String getRevision() {
		return Revision;
	}

	public void setRevision(String revision) {
		Revision = revision;
	}

	public String getDatabaseVersion() {
		return databaseVersion;
	}

	public void setDatabaseVersion(String databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	public String getLastDatabaseUpdate() {
		return lastDatabaseUpdate;
	}

	public void setLastDatabaseUpdate(String lastDatabaseUpdate) {
		this.lastDatabaseUpdate = lastDatabaseUpdate;
	}
}
