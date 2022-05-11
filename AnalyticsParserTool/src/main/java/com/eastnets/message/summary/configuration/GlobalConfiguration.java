package com.eastnets.message.summary.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource(value = { "file:${EN_REPORTING_CONFIG_HOME}/analyticsParserTool.properties" })
public class GlobalConfiguration {

	private String dbType;
	private String serverName;
	private String portNumber;
	private String dbServiceName;
	private String dbUserName;
	private String password;
	private String databaseName;
	private String dbInstanceName;
	private boolean partitioned;
	private Integer analyticsParserBulkSize;
	private Integer analyticsParserDetailsBulkSize;
	private String schemaName;
	private String fromDate;
	private String toDate;
	private Integer aid;
	private String debugLevel;
	private String analyticsParserScheduler;
	private String analyticsParserDetailsScheduler;
	private boolean enableTnsName;
	private String tnsPath;

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getDbServiceName() {
		return dbServiceName;
	}

	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDbInstanceName() {
		return dbInstanceName;
	}

	public void setDbInstanceName(String dbInstanceName) {
		this.dbInstanceName = dbInstanceName;
	}

	public boolean isPartitioned() {
		return partitioned;
	}

	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	public Integer getAnalyticsParserBulkSize() {
		return analyticsParserBulkSize;
	}

	public void setAnalyticsParserBulkSize(Integer analyticsParserBulkSize) {
		this.analyticsParserBulkSize = analyticsParserBulkSize;
	}

	public Integer getAnalyticsParserDetailsBulkSize() {
		return analyticsParserDetailsBulkSize;
	}

	public void setAnalyticsParserDetailsBulkSize(Integer analyticsParserDetailsBulkSize) {
		this.analyticsParserDetailsBulkSize = analyticsParserDetailsBulkSize;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getDebugLevel() {
		return debugLevel;
	}

	public void setDebugLevel(String debugLevel) {
		this.debugLevel = debugLevel;
	}

	public String getAnalyticsParserScheduler() {
		return analyticsParserScheduler;
	}

	public void setAnalyticsParserScheduler(String analyticsParserScheduler) {
		this.analyticsParserScheduler = analyticsParserScheduler;
	}

	public String getAnalyticsParserDetailsScheduler() {
		return analyticsParserDetailsScheduler;
	}

	public void setAnalyticsParserDetailsScheduler(String analyticsParserDetailsScheduler) {
		this.analyticsParserDetailsScheduler = analyticsParserDetailsScheduler;
	}

	public boolean isEnableTnsName() {
		return enableTnsName;
	}

	public void setEnableTnsName(boolean enableTnsName) {
		this.enableTnsName = enableTnsName;
	}

	public String getTnsPath() {
		return tnsPath;
	}

	public void setTnsPath(String tnsPath) {
		this.tnsPath = tnsPath;
	}

}
