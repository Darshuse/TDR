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
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.watchdog.ActiveSyntax;

/**
 * Call "getUserSettings" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class GetActiveSyntaxVersionProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8100829780041868375L;
	private static final String PROCEDURE_NAME = "stxGetActiveVersion";
	
	
	
	public GetActiveSyntaxVersionProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlOutParameter("version_idx", Types.INTEGER));
		declareParameter(new SqlOutParameter("version", Types.VARCHAR));
	}

	public ActiveSyntax execute() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Map<String, Object> result = execute(parameters);
		
		ActiveSyntax activeSyntax = new ActiveSyntax();
		
		activeSyntax.setVersionIdx(Long.parseLong(result.get("version_idx").toString()));
		activeSyntax.setVersion(result.get("version").toString());
		
		return activeSyntax;
	}

}
