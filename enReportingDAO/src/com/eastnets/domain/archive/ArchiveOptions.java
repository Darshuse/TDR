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
import java.util.Date;

/**
 * ArchiveOptions abstract POJO
 * @author EastNets
 * @since September 20, 2012
 */
public abstract class ArchiveOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1590175482649661893L;

	private boolean isTrustedConnection;
	
	private Long transactionSize;	
	private boolean delete; 
	
	private String execPath;
	private String userName;
	private String password;
	private String serverName;
	private String databaseName;
	private String databasePortNumber;
	private boolean includeIncomplete;

	private String instanceName;
	
	private Date dateFrom;
	private Date dateTo;
	
	public boolean isTrustedConnection() {
		return isTrustedConnection;
	}
	public void setTrustedConnection(boolean isTrustedConnection) {
		this.isTrustedConnection = isTrustedConnection;
	}
	public Long getTransactionSize() {
		return transactionSize;
	}
	public void setTransactionSize(Long transactionSize) {
		this.transactionSize = transactionSize;
	}
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
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public String getDatabasePortNumber() {
		return databasePortNumber;
	}
	public void setDatabasePortNumber(String databasePortNumber) {
		this.databasePortNumber = databasePortNumber;
	}
	public boolean isIncludeIncomplete() {
		return includeIncomplete;
	}
	public void setIncludeIncomplete(boolean includeIncomplete) {
		this.includeIncomplete = includeIncomplete;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	
	
}
