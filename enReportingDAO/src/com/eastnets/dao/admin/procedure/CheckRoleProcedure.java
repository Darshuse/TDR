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

package com.eastnets.dao.admin.procedure;

import java.io.Serializable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

/**
 * Call "sp_helpuser" stored procedure
 * @author EastNets
 * @since September 4, 2012
 */

public class CheckRoleProcedure implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1741111197344663274L;
	private static final String PROCEDURE_NAME = "IS_MEMBER";
	private SimpleJdbcCall simpleJdbcCall;
	
	public CheckRoleProcedure(JdbcTemplate jdbcTemplate) {
		 jdbcTemplate.setResultsMapCaseInsensitive(true);
		 this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName(PROCEDURE_NAME);
	}

	@SuppressWarnings({ "unchecked" })
	public Integer execute(String roleName) {
		SqlParameterSource in = new MapSqlParameterSource().addValue("role",roleName);
    	Integer x = simpleJdbcCall.executeFunction(Integer.class, in);
    	return x;

	}
}
