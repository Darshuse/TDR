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

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.logging.Logging;

/**
 * Database connection provider
 * 
 * @author EHakawati
 * @version 1.0
 */
public class DBConnection {

	// Logger object
	private static final Logger logger = Logging.getLogger(DBConnection.class.getSimpleName());

	// Connection pool instance
	private static DataSource dataSource;

	// Static initializer
	static {

		try {

			GlobalConfiguration configuration = GlobalConfiguration.getInstance();

			// Load data source object
			Class<?> clazz = Class.forName(configuration.getDatabaseType().getClazzName());
			dataSource = (DataSource) clazz.newInstance();

			// Reflected call property
			Method method = clazz.getMethod("setServerName", String.class);
			method.invoke(dataSource, configuration.getServerName());
			// Reflected call property
			method = clazz.getMethod("setPortNumber", int.class);
			method.invoke(dataSource, configuration.getPortNumber());
			// Reflected call property
			method = clazz.getMethod("setUser", String.class);
			method.invoke(dataSource, configuration.getUserName());
			// Reflected call property
			method = clazz.getMethod("setPassword", String.class);
			method.invoke(dataSource, configuration.getPassword());
			
			// Bug 32179:XMLRestore tool doesn’t support named instances
			if (configuration.isDbServiceName() == true)
			{
				// handle db service name SID orcl db
				method = clazz.getMethod("setServiceName", String.class);
			}
			else
			{
				// handle dbname orcl db && dbname for MSSQL db
				method = clazz.getMethod("setDatabaseName", String.class);
			}	

			method.invoke(dataSource, configuration.getServiceName());


			// handle instance name only MSSQL
			if (DatabaseType.MSSQL == configuration.getDatabaseType()) {
				// handle instance name sql server db
				method = clazz.getMethod("setInstanceName", String.class);
				method.invoke(dataSource, configuration.getInstanceName());
			}		


			// if oracle set diverType property
			if (DatabaseType.ORACLE == configuration.getDatabaseType()) {
				method = clazz.getMethod("setDriverType", String.class);
				method.invoke(dataSource, "thin");
			}

			//logger.info(configuration.getDatabaseType() + " : Database connection created successfully");

		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}
	}

	/**
	 * Default constructor as private for Singleton
	 */
	private DBConnection() {
	}

	/**
	 * Getting a connection JDBC instance
	 * 
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static Connection getConnection() throws SQLException {

		return dataSource.getConnection();

	}
	
	/**
	 * 
	 * @return data source
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}

}
