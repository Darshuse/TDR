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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.reporting.licensing.beans.ModuleValidationInfo;


/**
 * Call "ldGetCounters" stored procedure
 * @author EastNets
 * @since August 30, 2012
 */
public class LicenseCheckProductProcedure extends StoredProcedure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3286020485003753013L;
	private static final String PROCEDURE_NAME = "sCheckProduct";
	
	public LicenseCheckProductProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("productID", Types.VARCHAR));
		declareParameter(new SqlOutParameter("expirationDate", Types.DATE));
		declareParameter(new SqlOutParameter("licensed", Types.INTEGER));
	}

	public ModuleValidationInfo execute(String productID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("productID", productID);

		Map<String, Object> resultMap = execute(parameters);

		ModuleValidationInfo info = new ModuleValidationInfo();

		if(resultMap.get("licensed") !=null)
		{
			info.setExpirationDate((Date) resultMap.get("expirationDate"));
			info.setLicensed(Integer.valueOf(resultMap.get("licensed").toString()));
		}
		else
		{
			info.setLicensed(0);
		}
		return info;
	}
}



