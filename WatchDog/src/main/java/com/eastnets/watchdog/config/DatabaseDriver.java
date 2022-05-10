package com.eastnets.watchdog.config;

public enum DatabaseDriver {

	ORACLE_DRIVER("oracle.jdbc.driver.OracleDriver","org.eclipse.persistence.platform.database.OraclePlatform"),
	MSSQL_DRIVER("com.microsoft.sqlserver.jdbc.SQLServerDriver","org.eclipse.persistence.platform.database.SQLServerPlatform");

	private String driver;
	private String platform;

	private DatabaseDriver(String driver,String platform) {
		this.setDriver(driver);
		this.setPlatform(platform);

	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}



}
