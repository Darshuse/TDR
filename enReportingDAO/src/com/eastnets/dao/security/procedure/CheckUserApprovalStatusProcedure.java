package com.eastnets.dao.security.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.security.UserApprovalStatusInfo;

public class CheckUserApprovalStatusProcedure extends StoredProcedure implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = -2799502111599342601L;
private static final String PROCEDURE_NAME = "sCheckUserApprovalStatus";
	
	public CheckUserApprovalStatusProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		
		declareParameter(new SqlOutParameter("status", Types.INTEGER));
		declareParameter(new SqlOutParameter("description", Types.VARCHAR));
	}
	
		public UserApprovalStatusInfo execute() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		

		Map<String, Object> resultMap = execute(parameters);

		UserApprovalStatusInfo info = new UserApprovalStatusInfo();

		
			info.setStatus((Integer) resultMap.get("status"));
			info.setDescription(resultMap.get("description").toString());
		
		return info;
	}
}
