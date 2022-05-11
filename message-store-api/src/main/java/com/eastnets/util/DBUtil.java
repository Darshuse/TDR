package com.eastnets.util;

import com.eastnets.configuration.DBConfig;

public class DBUtil {
	
	public static String getDatabaseURL(DBConfig dbConfig) {

		StringBuffer dbUrl = null;
		if (dbConfig.getDbType().equalsIgnoreCase("Oracle")) {
			dbUrl = new StringBuffer("jdbc:oracle:thin:@");
		} else {
			dbUrl = new StringBuffer("jdbc:sqlserver://");
		}

		if (dbConfig.getDbType().equalsIgnoreCase("Oracle")) {
			dbUrl.append(dbConfig.getServerName()).append(':').append(dbConfig.getPortNumber());
			if (dbConfig.getServiceName() != null && !dbConfig.getServiceName().trim().isEmpty()) {
				dbUrl.append('/').append(dbConfig.getServiceName());
			} else {
				dbUrl.append(':').append(dbConfig.getDatabaseName());
			}

		} else if (dbConfig.getDbType().equalsIgnoreCase("MSSQL")) {
			dbUrl.append(dbConfig.getServerName()).append(':').append(dbConfig.getPortNumber());
			dbUrl.append(";databaseName=").append(dbConfig.getDatabaseName());
			dbUrl.append(";instance=").append(dbConfig.getInstanceName());
		}
		return dbUrl.toString();
	}
}
