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

package com.eastnets.dao.license.procedures;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.reporting.licensing.beans.LicenseValidationInfo;


/**
 * Call "ldGetCounters" stored procedure
 * @author EastNets
 * @since August 30, 2012
 */
public class LicenseCheckLicenseProcedure extends StoredProcedure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3703360268988282897L;
	private static final String PROCEDURE_NAME = "sCheckLicense";
	
	public LicenseCheckLicenseProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlOutParameter("strData", Types.VARCHAR)); 
		declareParameter(new SqlOutParameter("offset", Types.INTEGER));  
		declareParameter(new SqlOutParameter("strKey", Types.VARCHAR));
		declareParameter(new SqlOutParameter("retStatus", Types.INTEGER));  
	}

	public LicenseValidationInfo execute() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		LicenseValidationInfo info = new LicenseValidationInfo();
		Map<String, Object> resultMap = execute(parameters);
		Integer retStatus = (Integer)resultMap.get("retStatus");
		if(resultMap.get("strData")==null)
		{
			info.setStatus(0);
			return info;
		}
			
		info.setData(resultMap.get("strData").toString());
	
		info.setOffset(Integer.valueOf(resultMap.get("offset").toString()));
		info.setKey(resultMap.get("strKey").toString());
		info.setStatus(Integer.valueOf(retStatus.toString()));
		
		return info;
	}
}



