package com.eastnets.utility;

import com.eastnets.config.DBConfiguration;

public class Util {

	public static String getDatabaseURL(DBConfiguration dbConfig) {
		StringBuffer dbUrl = null;

		if (dbConfig.getDbType().equalsIgnoreCase("Oracle")) {
			dbUrl = new StringBuffer("jdbc:oracle:thin:@");
		} else {
			dbUrl = new StringBuffer("jdbc:jtds:sqlserver://");
		}

		if (dbConfig.getDbType().equalsIgnoreCase("Oracle")) {
			dbUrl.append(dbConfig.getServerName()).append(':').append(dbConfig.getPort());
			if (dbConfig.getServiceName() != null && !dbConfig.getServiceName().trim().isEmpty()) {
				dbUrl.append('/').append(dbConfig.getServiceName());
			} else {
				dbUrl.append(':').append(dbConfig.getDbName());
			}

		} else if (dbConfig.getDbType().equalsIgnoreCase("MSSQL")) {
			dbUrl.append(dbConfig.getServerName()).append(':').append(dbConfig.getPort());
			dbUrl.append(";databaseName=").append(dbConfig.getDbName());
			dbUrl.append(";instance=").append(dbConfig.getInstanceName());
		}
		return dbUrl.toString();
	}
}
