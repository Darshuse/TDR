package com.eastnets.domain.monitoring;

import java.io.Serializable;

public class MonitoringLogInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5012794821717039800L;
	private String appVersion;
	private String databaseServer;

	private String databaseName;
	
	private boolean includeTraces;
	private boolean includeStatistics;
	private boolean includeAuditLogs;
	
	
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getDatabaseServer() {
		return databaseServer;
	}
	public void setDatabaseServer(String databaseServer) {
		this.databaseServer = databaseServer;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public boolean isIncludeTraces() {
		return includeTraces;
	}
	public void setIncludeTraces(boolean includeTraces) {
		this.includeTraces = includeTraces;
	}
	public boolean isIncludeStatistics() {
		return includeStatistics;
	}
	public void setIncludeStatistics(boolean includeStatistics) {
		this.includeStatistics = includeStatistics;
	}
	public boolean isIncludeAuditLogs() {
		return includeAuditLogs;
	}
	public void setIncludeAuditLogs(boolean includeAuditLogs) {
		this.includeAuditLogs = includeAuditLogs;
	}

}
