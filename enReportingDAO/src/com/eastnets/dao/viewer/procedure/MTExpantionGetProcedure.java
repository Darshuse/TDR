package com.eastnets.dao.viewer.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import com.eastnets.domain.Pair;

public class MTExpantionGetProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5069950579020833510L;
	private static final String PROCEDURE_NAME = "vwMTExpansionGet";
	boolean partitioned= false;
	
	public MTExpantionGetProcedure(JdbcTemplate jdbcTemplate, boolean isPartitioned){
		super(jdbcTemplate, PROCEDURE_NAME);
		partitioned= isPartitioned;
		declareParameter(new SqlParameter("V_AID", Types.INTEGER));
		declareParameter(new SqlParameter("V_UMIDL", Types.INTEGER));
		declareParameter(new SqlParameter("V_UMIDH", Types.INTEGER));
		declareParameter(new SqlParameter("V_MESG_TYPE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_MESG_SYNTAX_TABLE_VER", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_STX_DESCRIPTION", Types.VARCHAR));
		if ( partitioned ){
			declareParameter(new SqlParameter("v_MESG_CREA_DATE_TIME", Types.TIMESTAMP));
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Pair<String, String> execute(int aid, int umidl, int umidh, Date mesg_crea_date, String mesgType){	
		Map<String, Object> parameters = new HashMap<String, Object>(5);		
		parameters.put("V_AID", aid );
		parameters.put("V_UMIDL", umidl);
		parameters.put("V_UMIDH", umidh);
		parameters.put("V_MESG_TYPE", mesgType);
		if ( partitioned ){
			parameters.put("v_MESG_CREA_DATE_TIME", mesg_crea_date);
		}
		
		Map result= execute(parameters);
		Pair<String, String> resultPair= new Pair<String, String>();
		resultPair.setKey((String)result.get("V_MESG_SYNTAX_TABLE_VER"));
		resultPair.setValue((String)result.get("V_STX_DESCRIPTION"));
		return resultPair;
	}
}