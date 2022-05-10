package com.eastnets.config;

public enum DBProvider {

	ORACLE("oracle.jdbc.driver.OracleDriver", "org.eclipse.persistence.platform.database.OraclePlatform"), MSSQL(
			"com.microsoft.sqlserver.jdbc.SQLServerDriver",
			"org.eclipse.persistence.platform.database.SQLServerPlatform");

	String driver;
	String provider;

	public String getDriver() {
		return driver;
	}

	public String getProvider() {
		return provider;
	}

	DBProvider(String driver, String provider) {
		this.driver = driver;
		this.provider = provider;
	}
}
