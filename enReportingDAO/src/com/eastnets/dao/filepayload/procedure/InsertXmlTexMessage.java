package com.eastnets.dao.filepayload.procedure;

import java.io.Serializable;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.filepayload.FilePayload;

/**
 * @author MKassab
 * 
 */
public class InsertXmlTexMessage extends StoredProcedure implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private static final String PROCEDURE_NAME = "ldProcessAMH";

	public InsertXmlTexMessage(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("aid", Types.INTEGER));
		declareParameter(new SqlParameter("xmltext_s_umidl", Types.INTEGER));
		declareParameter(new SqlParameter("xmltext_s_umidh", Types.INTEGER));
		declareParameter(new SqlParameter("xmltext_mesg_order", Types.INTEGER));
		declareParameter(new SqlParameter("mesg_crea_date_time", Types.TIMESTAMP));
		declareParameter(new SqlParameter("mesg_identifier", Types.VARCHAR));
		declareParameter(new SqlParameter("xmltext_data", Types.CLOB));
		declareParameter(new SqlParameter("xmltext_header", Types.CLOB));
		declareParameter(new SqlParameter("xmltext_size", Types.INTEGER));
		declareParameter(new SqlParameter("xsd_blk_flag", Types.INTEGER));
		declareParameter(new SqlOutParameter("RETSTATUS", Types.INTEGER));

	}

	public int execute(final FilePayload filePayload, String mesgText, String mesgHeader, String mesgId, int mesgOrder, int mesgSize, int blkFlag) {
		Map<String, Object> parameters = new HashMap<String, Object>(3);

		parameters.put("aid", filePayload.getAid());
		parameters.put("xmltext_s_umidl", filePayload.getSumidl());
		parameters.put("xmltext_s_umidh", filePayload.getSumidh());
		parameters.put("xmltext_mesg_order", mesgOrder);
		parameters.put("mesg_crea_date_time", filePayload.getCreationDateTime());
		parameters.put("mesg_identifier", mesgId);
		parameters.put("xmltext_data", mesgText);
		parameters.put("xmltext_header", mesgHeader);
		parameters.put("xmltext_size", mesgSize);
		parameters.put("xsd_blk_flag", blkFlag);
		Map<String, Object> resultMap = execute(parameters);
		Integer retStatus = (Integer) resultMap.get("RETSTATUS");
		return retStatus;
	}

}
