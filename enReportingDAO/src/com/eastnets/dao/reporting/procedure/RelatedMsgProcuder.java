package com.eastnets.dao.reporting.procedure;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.reporting.ReportSet;

public class RelatedMsgProcuder extends StoredProcedure implements Serializable{
	
	private static final long serialVersionUID = 1L; 
 	private static final String PROCEDURE_NAME = "REPSAVEPARAMSET";
	public RelatedMsgProcuder(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("related", Types.VARCHAR));
		declareParameter(new SqlParameter("from_date", Types.VARCHAR));
		declareParameter(new SqlOutParameter("to_date", Types.VARCHAR));
	}

	public ArrayList<RelatedMessage> execute(ReportSet reportSet) {
		ArrayList<RelatedMessage> arrRelMsg=new ArrayList<RelatedMessage>();
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("related", reportSet.getReportId());
		parameters.put("from_date", reportSet.getName());
		parameters.put("to_date", reportSet.getName());
		Map<String, Object> result = execute(parameters);
 	    ResultSet rs =(ResultSet) result.get("out_userexists"); 
		return arrRelMsg;
	}
}
