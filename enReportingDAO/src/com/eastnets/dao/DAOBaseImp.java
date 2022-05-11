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

package com.eastnets.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import com.eastnets.dao.common.DBPortabilityHandler;

/**
 * DAO Implementation, contains all implemented common methods in all DAOs Implementation
 * @author EastNets
 * @since July 11, 2012
 */
public class DAOBaseImp implements Serializable, DAO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6913312430841437719L;
	private DBPortabilityHandler dbPortabilityHandler;
	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	

	public DBPortabilityHandler getDbPortabilityHandler() {
		return dbPortabilityHandler;
	}

	public void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandler) {
		this.dbPortabilityHandler = dbPortabilityHandler;
	}

	@Override
	public int getDbType() {
		return dbPortabilityHandler.getDbType();
	}

	public <T> T getNullableValue(ResultSet rs, T value) throws SQLException {
		if ( rs.wasNull() ){
			return null;
		}
		return value;
	}

	protected Long defaultLong(Long value) {
		return defaultLong( value, 0L );
	}
	
	protected Long defaultLong(Long value, Long defaultValue) {
		return (value == null)? defaultValue : value;
	}
	
	protected String defaultString(String value) {
		return (value == null)? "" : value;
	}

	protected boolean defaultBool(Boolean value) {
		return (value == null)? false : value;
	}
	
	protected boolean defaultBool(Boolean value, boolean defaultValue) {
		return (value == null)? defaultValue : value;
	}

	protected String getDBSafeString(String str) {
		if ( str == null ){
			return "";
		}
		return str.replace("'", "''");
	}
}
