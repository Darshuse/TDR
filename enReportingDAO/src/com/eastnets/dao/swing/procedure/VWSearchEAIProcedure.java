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

package com.eastnets.dao.swing.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.swing.SwingEntity;

/**
 * Call "VWSearchEAI" stored procedure
 * @author EastNets
 * @since July 11, 2012
 */
public class VWSearchEAIProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3361369368032381777L;
	private static final String PROCEDURE_NAME = "SIDE.VWSearchEAI";

	public VWSearchEAIProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("v_sSubSQLF", Types.VARCHAR));
		declareParameter(new SqlParameter("v_sSubSQLE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("v_result_cursor", oracle.jdbc.OracleTypes.CURSOR, new SwingSearchResultMapper()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<SwingEntity> execute(String subSQLF, String subSQLE) {
		ArrayList<SwingEntity> resultList = new ArrayList<SwingEntity>();
		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("v_sSubSQLF", "e.id = f.ruserstatus_id and f.fin_swift_IO = 'finSwiftIO' and ( f.fin_corr = 'corr' or f.fin_own_dest = 'finOwnDist') and f.fin_mesg_type = 'MsgType' and f.fin_trn_ref = 'finTrnRef' and f.fin_amnt between 0 and 100000 and f.fin_curr = 'finCurr' and f.fin_value_date between TO_DATE('20112516 15:25:32, 'YYYYMMDD HH24:MI:SS') and TO_DATE('20112516 15:25:32, 'YYYYMMDD HH24:MI:SS') and f.fin_isn_osn = 'ISN/OSN'");
//		parameters.put("v_sSubSQLE", "e.eai_unique_identifier = 'eaiUniqueIdentifier' and e.eai_time_stamp between TO_DATE('20112516 15:25:32','YYYYMMDD HH24:MI:SS') and TO_DATE('20112516 15:25:32','YYYYMMDD HH24:MI:SS') and e.eai_status = 'eaiStatus' and e.eai_operator = 'eaiOperator' and e.eai_error_code = 'eaiErrorCode' and e.eai_comment = 'eaiComment' and e.eai_message_source = 'eaiMessageSource' and e.standard = 'standard' and e.category = 'category'");

//		parameters
//				.put("v_sSubSQLF","" +
//						"e.id = f.ruserstatus_id "
//								+ "and f.fin_value_date between TO_DATE('20101111 00:00:00','YYYYMMDD HH24:MI:SS') and TO_DATE('20121111 23:59:59','YYYYMMDD HH24:MI:SS')"
//								);
//		parameters
//				.put("v_sSubSQLE",
//							"e.eai_time_stamp between TO_DATE('20001115 00:00:00','YYYYMMDD HH24:MI:SS') and TO_DATE('20111115 23:59:59','YYYYMMDD HH24:MI:SS') and e.eai_operator = 'eai_operator'"						
//								);
		parameters.put("v_sSubSQLF", subSQLF);
		parameters.put("v_sSubSQLE",subSQLE );
		Map r = execute(parameters);
		resultList = (ArrayList<SwingEntity>)r.get("v_result_cursor");
		return resultList;
	}
}
