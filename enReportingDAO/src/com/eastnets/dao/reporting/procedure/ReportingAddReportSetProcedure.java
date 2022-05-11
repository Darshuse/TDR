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
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.reporting.ReportSet;

/**
 * Call "wdGetLastIDs21" stored procedure
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class ReportingAddReportSetProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1369992769682836325L;
	private static final String PROCEDURE_NAME = "REPSAVEPARAMSET";

	public ReportingAddReportSetProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_report_id", Types.BIGINT));
		// declareParameter(new SqlParameter("v_user_Profile_id", Types.BIGINT));
		declareParameter(new SqlParameter("v_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_id", Types.BIGINT));
	}

	public Long execute(ReportSet reportSet) {

		Map<String, Object> parameters = new HashMap<String, Object>(3);

		parameters.put("v_report_id", reportSet.getReportId());
		// parameters.put("v_user_Profile_id", reportSet.getUser_profile_id());
		parameters.put("v_name", reportSet.getName());
		Map<String, Object> result = execute(parameters);
		Long id = (Long) result.get("v_id");
		reportSet.setId(id);
		return id;
	}
}
