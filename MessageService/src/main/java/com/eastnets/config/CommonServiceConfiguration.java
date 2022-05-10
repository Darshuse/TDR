package com.eastnets.config;

public class CommonServiceConfiguration {
	private DBConfiguration dbConfig;
	static CommonServiceConfiguration commonServiceConfiguration;

	public static CommonServiceConfiguration getInstance() {
		if (commonServiceConfiguration == null) {
			commonServiceConfiguration = new CommonServiceConfiguration();
			commonServiceConfiguration.setDbConfig(new DBConfiguration());
		}
		return commonServiceConfiguration;
	}

	public DBConfiguration getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(DBConfiguration dbConfig) {
		this.dbConfig = dbConfig;
	}
}
