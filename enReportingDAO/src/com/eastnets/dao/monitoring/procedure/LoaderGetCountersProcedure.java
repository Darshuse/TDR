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

package com.eastnets.dao.monitoring.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.monitoring.Statistics;

/**
 * Call "ldGetCounters" stored procedure
 * @author EastNets
 * @since August 30, 2012
 */
public class LoaderGetCountersProcedure extends StoredProcedure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 873807303719960485L;
	private static final String PROCEDURE_NAME = "ldGetCounters";
	
	public LoaderGetCountersProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlOutParameter("total_count", Types.VARCHAR));
		declareParameter(new SqlOutParameter("live_count", Types.VARCHAR));
		declareParameter(new SqlOutParameter("complete_count", Types.VARCHAR));
		declareParameter(new SqlOutParameter("incomplete_count", Types.VARCHAR));
		declareParameter(new SqlOutParameter("archive_count", Types.VARCHAR));
	}

	public Statistics execute(Statistics statistics) {
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		Map<String, Object> result = execute(parameters);
		statistics.setTotalCount(new Integer(result.get("total_count").toString()));
		statistics.setLiveCount(new Integer(result.get("live_count").toString()));
		statistics.setCompleteCount(new Integer(result.get("complete_count").toString()));
		statistics.setIncompleteCount(new Integer(result.get("incomplete_count").toString()));
		statistics.setArchiveCount(new Integer(result.get("archive_count").toString()));
		return statistics;
	}
}



