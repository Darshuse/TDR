package com.eastnets.enGpiNotifer.config;


import com.eastnets.config.ConfigBean;

/**
 * @author MKassab
 * 
 * */
 

public class AppConfigBean extends ConfigBean {
	 
	private static final long serialVersionUID = -1832940635910968590L; 
	
	private String aid;
	private String dbConfigFilePath; 
	private long sleepPeriod = 30;
	private Boolean enableDebugMode = false;

	public void testInit() {
		System.out.println("test");
	}

	public AppConfigBean() {
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

	public long getSleepPeriod() {
		return sleepPeriod;
	}

	public void setSleepPeriod(long sleepPeriod) {
		this.sleepPeriod = sleepPeriod;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}
}
