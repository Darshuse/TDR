/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */
package com.eastnets.resilience.xmldump.db;

/**
 * Supported database types enumeration
 * 
 * @author EHakawati
 * 
 */
public enum DatabaseType {
	ORACLE("oracle.jdbc.pool.OracleDataSource", 0), MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDataSource", 1);

	// JDBC data source class name
	private String connectionClazz;

	// Don't ask - porting to other **** libraries
	private int licenseIndex;

	DatabaseType(String connectionClazz, int licenseIndex) {
		this.connectionClazz = connectionClazz;
		this.licenseIndex = licenseIndex;

	}

	public String getClazzName() {
		return this.connectionClazz;
	}

	public int getLicenseID() {
		return licenseIndex;
	}

}
