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
 * Call "sCheckAdmit" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class SCheckAdmitProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8306688090551188407L;
	private static final String PROCEDURE_NAME = "sCheckAdmit";
	public SCheckAdmitProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_ProgramId", Types.INTEGER));
		declareParameter(new SqlParameter("v_ObjectId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_IsAllowed", Types.INTEGER));
	}

	public Integer execute(int programId,int objectId) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("v_ProgramId", programId);
		parameters.put("v_ObjectId", objectId);
		Map<String, Object> result = execute(parameters);
		return (Integer)result.get("v_IsAllowed");
	}
}
