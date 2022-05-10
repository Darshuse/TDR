package com.eastnets.config;

import org.apache.commons.lang.StringUtils;

public class DBConfiguration {

	private String serverName;
	private Integer port;
	private String dbType;
	private String serviceName;
	private String instanceName;
	private String dbName;
	private String username;
	private String password;
	private String ecf;
	private String dbUrl;
	private DBProvider dbProvider;

	public DBConfiguration() {
	}

	public String getServerName() {
		return StringUtils.defaultString(serverName, "");
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getPort() {
		if (port == null) {
			return 0;
		}

		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDbType() {
		return StringUtils.defaultString(dbType, "");
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getServiceName() {
		return StringUtils.defaultString(serviceName, "");
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInstanceName() {
		return StringUtils.defaultString(instanceName, "");
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getUsername() {
		return StringUtils.defaultString(username, "");
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return StringUtils.defaultString(password, "");
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return StringUtils.defaultString(dbName, "");
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getEcf() {
		return StringUtils.defaultString(ecf, "");
	}

	public void setEcf(String ecf) {
		this.ecf = ecf;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public DBProvider getDbProvider() {
		return dbProvider;
	}

	public void setDbProvider(DBProvider dbProvider) {
		this.dbProvider = dbProvider;
	}

}
