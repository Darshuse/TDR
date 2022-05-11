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

package com.eastnets.dao.viewer.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * Call "ldForceRequestUpdate" stored procedure
 * @author EastNets
 * @since September 25, 2012
 */
public class ForceMessageUpdateProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7236519924351670919L;
	private static final String PROCEDURE_NAME = "ldForceRequestUpdate";
	private boolean partitioned;
	
	public ForceMessageUpdateProcedure(JdbcTemplate jdbcTemplate, boolean isPartitioned){
		super(jdbcTemplate, PROCEDURE_NAME);
		partitioned= isPartitioned;
		declareParameter(new SqlParameter("V_AID", Types.INTEGER));
		declareParameter(new SqlParameter("V_MESG_S_UMIDL", Types.INTEGER));
		declareParameter(new SqlParameter("V_MESG_S_UMIDH", Types.INTEGER));
		if ( partitioned )
		{
			declareParameter(new SqlParameter("V_X_CREA_DATE_TIME_MESG", Types.TIMESTAMP));
		}
	}
	
	public void execute(int aid, int umidl, int umidh, Date mesg_crea_date){	
		Map<String, Object> parameters = new HashMap<String, Object>(4);		
		parameters.put("V_AID", aid );
		parameters.put("V_MESG_S_UMIDL", umidl);
		parameters.put("V_MESG_S_UMIDH", umidh);
		
		if ( partitioned ){
			parameters.put("V_X_CREA_DATE_TIME_MESG", mesg_crea_date);
		}
		execute(parameters);		
	}
}
