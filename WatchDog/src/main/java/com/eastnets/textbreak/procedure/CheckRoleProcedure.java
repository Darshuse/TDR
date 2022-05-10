package com.eastnets.textbreak.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CheckRoleProcedure extends StoredProcedure implements Serializable {

	private static final long serialVersionUID = -3703360268988282897L;
	private static final String PROCEDURE_NAME = "sCheckRole ";

	public CheckRoleProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("ROLE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("STATUS", Types.INTEGER));
		compile();
	}

	
	 
	public Integer execute(String role) {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ROLE", role);

		Map<String, Object> resultMap = execute(parameters);


		return (Integer)resultMap.get("STATUS");
	}
}
