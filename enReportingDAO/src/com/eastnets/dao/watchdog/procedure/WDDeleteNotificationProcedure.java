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

package com.eastnets.dao.watchdog.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * Call "wdDeleteNotification" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class WDDeleteNotificationProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2452798106723154252L;
	private static final String PROCEDURE_NAME = "wdDeleteNotification";
	public WDDeleteNotificationProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_Type_Req", Types.INTEGER));
		declareParameter(new SqlParameter("v_sysid", Types.INTEGER));
	}

	public void execute(Integer reqType, Integer id) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("v_Type_Req", reqType);
		parameters.put("v_sysid", id);
		execute(parameters);
	}
}



