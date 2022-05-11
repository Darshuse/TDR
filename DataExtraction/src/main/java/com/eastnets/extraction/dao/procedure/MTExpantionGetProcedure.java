package com.eastnets.extraction.dao.procedure;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;

import com.eastnets.extraction.config.DataSourceConfig;
import com.eastnets.extraction.service.helper.Pair;

@Component
public class MTExpantionGetProcedure extends StoredProcedure {

	private static final String PROCEDURE_NAME = "vwMTExpansionGet";

	private DataSourceConfig dataSourceConfig;

	public MTExpantionGetProcedure(JdbcTemplate jdbcTemplate, final DataSourceConfig dataSourceConfig) {
		super(jdbcTemplate, PROCEDURE_NAME);
		// partitioned = isPartitioned;
		declareParameter(new SqlParameter("V_AID", Types.INTEGER));
		declareParameter(new SqlParameter("V_UMIDL", Types.INTEGER));
		declareParameter(new SqlParameter("V_UMIDH", Types.INTEGER));
		declareParameter(new SqlParameter("V_MESG_TYPE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_MESG_SYNTAX_TABLE_VER", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_STX_DESCRIPTION", Types.VARCHAR));
		this.dataSourceConfig = dataSourceConfig;
		if (dataSourceConfig.isPartitioned()) {
			declareParameter(new SqlParameter("V_MESG_CREA_DATE_TIME", Types.TIMESTAMP));
		}
	}

	@SuppressWarnings("rawtypes")
	public Pair<String, String> execute(int aid, int umidl, int umidh, Date mesg_crea_date, String mesgType) {
		Map<String, Object> parameters = new HashMap<String, Object>(5);
		parameters.put("V_AID", aid);
		parameters.put("V_UMIDL", umidl);
		parameters.put("V_UMIDH", umidh);
		parameters.put("V_MESG_TYPE", mesgType);
		if (dataSourceConfig.isPartitioned()) {
			parameters.put("V_MESG_CREA_DATE_TIME", mesg_crea_date);
		}

		Map result = execute(parameters);
		Pair<String, String> resultPair = new Pair<String, String>();
		resultPair.setKey((String) result.get("V_MESG_SYNTAX_TABLE_VER"));
		resultPair.setValue((String) result.get("V_STX_DESCRIPTION"));
		return resultPair;
	}
}
