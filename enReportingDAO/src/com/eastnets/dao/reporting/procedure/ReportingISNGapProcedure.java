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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;


/**
 * Call "wdGetLastIDs21" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class ReportingISNGapProcedure  extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8915670367133648651L;
	private static final String PROCEDURE_NAME = "repISNGap";
	public ReportingISNGapProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("fromDate", Types.TIMESTAMP));
		declareParameter(new SqlParameter("toDate", Types.TIMESTAMP));
	}

	@Override
	public Map<String, Object> execute(Map<String, ?> reportParametersMap)
			throws DataAccessException {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		
		parameters.put("fromDate", reportParametersMap.get("P_FROMDATETIME"));
		parameters.put("toDate", reportParametersMap.get("P_TODATETIME"));
		return super.execute(parameters);
	}
}




