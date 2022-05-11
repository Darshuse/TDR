package com.eastnets.enGPIParser.GPIConfig;

import com.eastnets.config.ConfigBean;

/**
 * @author MKassab
 * 
 */
public class AppConfigBean extends ConfigBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dbConfigFilePath;
	private String clusterTcpConfigFilePath;
	private long sleepPeriod = 30;
	private boolean verbose = false;
	private Boolean enableDebugMode = false;
	private boolean ucEnabel = false;

	private String fromDate = "2018/01/01 00:00:00";
	private String toDate = "2099/12/31 23:59:59";

	private int batchSize = 500;

	public AppConfigBean() {

	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
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

	public String getDbConfigFilePath() {
		return dbConfigFilePath;
	}

	public void setDbConfigFilePath(String dbConfigFilePath) {
		this.dbConfigFilePath = dbConfigFilePath;
	}

	public Boolean getEnableDebugMode() {
		return enableDebugMode;
	}

	public void setEnableDebugMode(Boolean enableDebugMode) {
		this.enableDebugMode = enableDebugMode;
	}

	public String getClusterTcpConfigFilePath() {
		return clusterTcpConfigFilePath;
	}

	public void setClusterTcpConfigFilePath(String clusterTcpConfigFilePath) {
		this.clusterTcpConfigFilePath = clusterTcpConfigFilePath;
	}

	public long getSleepPeriod() {
		return sleepPeriod;
	}

	public void setSleepPeriod(long sleepPeriod) {
		this.sleepPeriod = sleepPeriod;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isUcEnabel() {
		return ucEnabel;
	}

	public void setUcEnabel(boolean ucEnabel) {
		this.ucEnabel = ucEnabel;
	}

}
