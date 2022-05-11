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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.admin.User;

/**
 * Call "SetProfile" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class SetProfileProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7342133982822145119L;
	private static final String PROCEDURE_NAME = "SetProfile";
		public SetProfileProcedure(JdbcTemplate jdbcTemplate) {
			super(jdbcTemplate, PROCEDURE_NAME);
			declareParameter(new SqlParameter("xprofilename", Types.VARCHAR));
			declareParameter(new SqlParameter("xusername", Types.VARCHAR));
			declareParameter(new SqlParameter("xvwlistdepth", Types.VARCHAR));
			declareParameter(new SqlParameter("xwdnbdayhistory", Types.VARCHAR));
			declareParameter(new SqlParameter("xrpdirectory", Types.VARCHAR));
			declareParameter(new SqlParameter("xemail", Types.VARCHAR));
			declareParameter(new SqlOutParameter("xerror", Types.VARCHAR));
		}

		@SuppressWarnings("rawtypes")
		public void execute(User param) {
			Map<String, Object> parameters = new HashMap<String, Object>(6);
			parameters.put("xprofilename", param.getProfile().getName());
			parameters.put("xusername", param.getUserName());
			parameters.put("xvwlistdepth", param.getVwListDepth());
			parameters.put("xwdnbdayhistory", param.getWdNbDayHistory());
			parameters.put("xrpdirectory", param.getRpDirectory());
			parameters.put("xemail", param.getEmail());
			Map result;
			try {
				result = execute(parameters);
				param.setError((String) result.get("xerror"));
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}
