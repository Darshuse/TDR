
package com.eastnets.notifier.repository;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;

@Component
public class LockReadEventsProcedure extends StoredProcedure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 288346441930461693L;
	private static final String PROCEDURE_NAME = "side.NOTIFIERGETEVENTS";

	public LockReadEventsProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("V_PATCHSIZE", Types.INTEGER));
		declareParameter(new SqlOutParameter("V_RESULT", Types.VARCHAR));
	}

	/*
	 * This procedure will lock after read the records in order to prevent any overlap that may happened in cluster mode
	 */
	public String execute(int bulkSize) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("V_PATCHSIZE", bulkSize);
		Map<String, Object> result = execute(parameters);

		Object seqences = result.get("V_RESULT");
		if (seqences != null && !seqences.toString().isEmpty()) {
			return seqences.toString();
		}
		return "-1";
	}

}
