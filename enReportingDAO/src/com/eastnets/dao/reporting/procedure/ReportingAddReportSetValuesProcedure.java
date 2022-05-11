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

package com.eastnets.dao.reporting.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.domain.reporting.ReportSetParameter;

/**
 * Call "wdInsertUserSearchParameter" stored procedure
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class ReportingAddReportSetValuesProcedure extends StoredProcedure implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -40760121633072616L;
	private static final String PROCEDURE_NAME = "REPSAVEPARAMVALUE";

	public ReportingAddReportSetValuesProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_set_id", Types.BIGINT));
		declareParameter(new SqlParameter("v_idx", Types.BIGINT));
		declareParameter(new SqlParameter("v_param_name", Types.VARCHAR));
		declareParameter(new SqlParameter("v_type", Types.BIGINT));
		declareParameter(new SqlParameter("v_rep_param1", Types.VARCHAR));
		declareParameter(new SqlParameter("v_rep_param2", Types.VARCHAR));
		declareParameter(new SqlParameter("v_value1", Types.VARCHAR));
		declareParameter(new SqlParameter("v_value2", Types.VARCHAR));
		declareParameter(new SqlParameter("v_force_insert", Types.BIGINT));

	}

	public void execute(ReportSet reportSet, List<Parameter> parameters) {

		Map<String, Object> procedureParameters = new HashMap<String, Object>(9);

		Long current = 0L;

		for (Parameter parameter : parameters) {

			ReportSetParameter setParameter = (ReportSetParameter) parameter;

			procedureParameters.put("v_set_id", reportSet.getId());
			procedureParameters.put("v_idx", setParameter.getId());
			procedureParameters.put("v_param_name", setParameter.getName());
			procedureParameters.put("v_type", setParameter.getType());
			procedureParameters.put("v_rep_param1", setParameter.getFirstName());
			procedureParameters.put("v_rep_param2", setParameter.getSecondName());
			procedureParameters.put("v_value1", setParameter.getFirstValue());

			boolean resetValue2 = true;
			if (setParameter.getType() == Constants.REPORT_PARAMTER_TYPE_DATE || setParameter.getType() == Constants.REPORT_PARAMTER_TYPE_DATE_TIME) {
				AdvancedDate advancedDate = (AdvancedDate) setParameter.getFirstValueObject();
				if (advancedDate != null && advancedDate.getType() != AdvancedDate.TYPE_DATE) {
					procedureParameters.put("v_value2", "");
					resetValue2 = false;
				}
			}

			if (resetValue2) {
				procedureParameters.put("v_value2", setParameter.getSecondValue());
			}
			procedureParameters.put("v_force_insert", new Integer(0));
			execute(procedureParameters);
			current++;
		}
	}
}
