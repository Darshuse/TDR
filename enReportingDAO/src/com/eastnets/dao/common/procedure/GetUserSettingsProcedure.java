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

package com.eastnets.dao.common.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * Call "getUserSettings" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class GetUserSettingsProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8915786590221135135L;
	private static final String PROCEDURE_NAME = "getUserSettings";
	public GetUserSettingsProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("username", Types.VARCHAR));
		declareParameter(new SqlParameter("paramName", Types.VARCHAR));
		declareParameter(new SqlOutParameter("result", Types.VARCHAR));
	}

	public String execute(String username,String paramName) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("username", username);
		parameters.put("paramName", paramName);
		Map<String, Object> result = execute(parameters);
		return (String)result.get("result");
	}
}
