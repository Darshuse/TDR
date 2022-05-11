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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.watchdog.LastIDs;

/**
 * Call "wdGetLastIDs21" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class WDGetLastIDs21  extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5695386343038076189L;
	private static final String PROCEDURE_NAME = "wdGetLastIDs21";
	public WDGetLastIDs21(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_SinceDate", Types.TIMESTAMP));
		declareParameter(new SqlOutParameter("v_SysRequestId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_UserRequestId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_CalculatedDupId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_PossibleDupId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_ISNId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_OSNId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_NACKId", Types.INTEGER));
		declareParameter(new SqlOutParameter("v_EventRequestId", Types.INTEGER));
	}

	@SuppressWarnings("rawtypes")
	public LastIDs execute(Long iNbDayHistory) {
		LastIDs lastIDs = new LastIDs();
		Calendar cal = Calendar.getInstance();
		String string = iNbDayHistory.toString();
		cal.add(Calendar.DATE, - Integer.parseInt(string));
		Timestamp ts = new Timestamp (cal.getTimeInMillis());
		Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put("v_SinceDate", ts);
		Map result = execute(parameters);
		String value = result.get("v_SysRequestId").toString();
		lastIDs.setNewSysRequest(Long.parseLong(value));
		value = result.get("v_UserRequestId").toString();
		lastIDs.setNewUserRequest(Long.parseLong(value));
		value = result.get("v_CalculatedDupId").toString();
		lastIDs.setNewCalculatedDup(Long.parseLong(value));
		value = result.get("v_PossibleDupId").toString();
		lastIDs.setNewPossibleDup(Long.parseLong(value));
		value = result.get("v_ISNId").toString();
		lastIDs.setNewISN(Long.parseLong(value));
		value = result.get("v_OSNId").toString();
		lastIDs.setNewOSN(Long.parseLong(value));
		value = result.get("v_NACKId").toString();
		lastIDs.setNewNACK(Long.parseLong(value));
		value = result.get("v_EventRequestId").toString();
		lastIDs.setNewEvent(Long.parseLong(value));
		return lastIDs;
	}
}



