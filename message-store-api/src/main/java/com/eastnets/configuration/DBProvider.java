package com.eastnets.configuration;

public enum DBProvider {

	ORACLE("oracle.jdbc.OracleDriver", "org.hibernate.dialect.Oracle10gDialect"), MSSQL(
			"com.microsoft.sqlserver.jdbc.SQLServerDriver", "org.hibernate.dialect.SQLServer2012Dialect");

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
