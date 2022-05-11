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
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.admin.LoaderConnection;

/**
 * Call "ldAddNewConnection" stored procedure
 * @author EastNets
 * @since September 4, 2012
 */
public class HelpLoginProcedure extends StoredProcedure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6910152856350405994L;
	private static final String PROCEDURE_NAME = "ldAddNewConnection";

	public HelpLoginProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		
		declareParameter(new SqlParameter("v_allianceinfo", Types.VARCHAR));
		declareParameter(new SqlParameter("v_port", Types.INTEGER));
		declareParameter(new SqlParameter("v_timeout", Types.INTEGER));
		declareParameter(new SqlParameter("v_servername", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_RETSTATUS", Types.INTEGER));
	}

	public void execute(LoaderConnection param) {
	
		Map<String, Object> parameters = new HashMap<String, Object>(4);
		parameters.put("v_allianceinfo", param.getAllianceInfo());
		parameters.put("v_port", param.getPort());
		parameters.put("v_timeout", param.getTimeOut());
		parameters.put("v_servername", param.getServerAddress());
		
		Map<String, Object> result = execute(parameters);
		param.setReturenStatus( (Integer)result.get("v_RETSTATUS"));
		
		
	}
	
}
