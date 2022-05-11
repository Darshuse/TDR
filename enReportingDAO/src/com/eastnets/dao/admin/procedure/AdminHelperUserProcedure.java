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

package com.eastnets.dao.admin.procedure;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * Call "sp_helpuser" stored procedure
 * @author EastNets
 * @since September 4, 2012
 */

public class AdminHelperUserProcedure extends StoredProcedure implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1741111197344663274L;
	private static final String PROCEDURE_NAME = "sp_helpuser";
	
	public AdminHelperUserProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		
		declareParameter(new SqlParameter("name_in_db", Types.VARCHAR));
		declareParameter(new SqlReturnResultSet("roles", new SPRowMapper()));
		compile();
	}

	@SuppressWarnings({ "unchecked" })
	public List<String> execute(String username) {
	
		Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put("name_in_db", username);
		Map<String, Object> result = execute(parameters);
		if(result != null) {
			Object object = result.get("roles");
			if(object != null) {
				return ((List<String>)object);
			}
		}
		
		return null;
	}
}
	
	class SPRowMapper implements RowMapper<String> {
		 
	    /**
	     * Maps the result set rows to a Claim object
	     */
	    public String mapRow(ResultSet rs, int index) throws SQLException {	 
	        String string = rs.getString(2);
			String upperCase = string.toUpperCase();
			return  upperCase;
	    }
	}
