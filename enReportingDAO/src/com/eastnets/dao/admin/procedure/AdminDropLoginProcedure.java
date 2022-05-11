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

import com.eastnets.domain.admin.User;

/**
 * Call "sp_droplogin" stored procedure
 * @author EastNets
 * @since September 4, 2012
 */
public class AdminDropLoginProcedure extends StoredProcedure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8444337900654963962L;
	private static final String PROCEDURE_NAME = "sp_droplogin";

	public AdminDropLoginProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		
		declareParameter(new SqlParameter("loginame", Types.VARCHAR));
	}

	public void execute(User user) {
	
		Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put("loginame", user.getUserName());

		execute(parameters);
	}
}
