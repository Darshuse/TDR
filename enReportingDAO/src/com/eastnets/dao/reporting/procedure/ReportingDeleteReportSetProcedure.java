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

package com.eastnets.dao.reporting.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.reporting.ReportSet;

/**
 * Call "wdDeleteNotification" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class ReportingDeleteReportSetProcedure extends StoredProcedure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5058797731197703792L;
	private static final String PROCEDURE_NAME = "REPDELETEPARAMSET";
	public ReportingDeleteReportSetProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_set_id", Types.BIGINT));
	}

	public void execute(ReportSet reportSet) {
		Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put("v_set_id", reportSet.getId());

		execute(parameters);
	}
}



