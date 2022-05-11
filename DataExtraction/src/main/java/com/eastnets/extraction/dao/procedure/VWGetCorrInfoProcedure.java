package com.eastnets.extraction.dao.procedure;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;

import com.eastnets.extraction.bean.CorrInfo;

@Component
public class VWGetCorrInfoProcedure extends StoredProcedure implements Serializable {

	private static final String PROCEDURE_NAME = "vwGetCorrInfo_2";

	public VWGetCorrInfoProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_corr_type", Types.VARCHAR));
		declareParameter(new SqlParameter("v_corr_X1", Types.VARCHAR));
		declareParameter(new SqlParameter("v_corr_X2", Types.VARCHAR));
		declareParameter(new SqlParameter("v_corr_X3", Types.VARCHAR));
		declareParameter(new SqlParameter("v_corr_X4", Types.VARCHAR));
		declareParameter(new SqlParameter("v_mesg_date", Types.DATE));
		declareParameter(new SqlOutParameter("v_corr_institution_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_corr_branch_info", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_corr_city_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_corr_ctry_code", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_corr_ctry_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_corr_location", Types.VARCHAR));
		declareParameter(new SqlOutParameter("RETSTATUS", Types.NUMERIC));

	}

	public void execute(CorrInfo corr) {
		Map<String, Object> parameters = new HashMap<String, Object>(6);
		parameters.put("v_corr_type", corr.getCorrType());
		parameters.put("v_corr_X1", corr.getCorrX1());
		parameters.put("v_corr_X2", corr.getCorrX2());
		parameters.put("v_corr_X3", corr.getCorrX3());
		parameters.put("v_corr_X4", corr.getCorrX4());
		parameters.put("v_mesg_date", corr.getMesgDate());

		Map<String, Object> result = execute(parameters);
		corr.setCorrInstitutionName((String) result.get("v_corr_institution_name"));
		corr.setCorrBranchInfo((String) result.get("v_corr_branch_info"));
		corr.setCorrCityName((String) result.get("v_corr_city_name"));
		corr.setCorrCtryCode((String) result.get("v_corr_ctry_code"));
		corr.setCorrCtryName((String) result.get("v_corr_ctry_name"));
		corr.setCorrLocation((String) result.get("v_corr_location"));
		BigDecimal retstatus = (BigDecimal) result.get("RETSTATUS");
		corr.setRetstatus(retstatus.intValue());
	}

}
