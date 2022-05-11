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
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.Config;
import com.eastnets.domain.admin.User;

/**
 * Call "sp_addlogin" stored procedure
 * @author EastNets
 * @since September 4, 2012
 */
public class AdminAddLoginProcedure extends StoredProcedure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2851907174769880076L;
	private static final String PROCEDURE_NAME = "sp_addlogin";
	private Config config;
	
	public AdminAddLoginProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		
		declareParameter(new SqlParameter("loginame", Types.VARCHAR));
		declareParameter(new SqlParameter("passwd", Types.VARCHAR));
		declareParameter(new SqlParameter("defdb", Types.VARCHAR));
	}

	public void execute(User user) {
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("loginame", user.getUserName());
		parameters.put("passwd", user.getPassword());
		parameters.put("defdb", config.getDatabaseName());
		execute(parameters);
	}
	public Config getConfig() {
		return config;
	}
	
	public void setConfig(Config config) {
		this.config = config;
	}	
	
}
