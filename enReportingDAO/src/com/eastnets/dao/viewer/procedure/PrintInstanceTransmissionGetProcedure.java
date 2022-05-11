package com.eastnets.dao.viewer.procedure;

import java.util.Date;
import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.domain.viewer.InstanceTransmissionPrintInfo;

public class PrintInstanceTransmissionGetProcedure extends StoredProcedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3586110948617384657L;
	private static final String PROCEDURE_NAME = "vwPrintInstanceTransmissionGet";
	boolean partitioned= false;
	
	public PrintInstanceTransmissionGetProcedure(JdbcTemplate jdbcTemplate, boolean isPartitioned){
		super(jdbcTemplate, PROCEDURE_NAME);
		partitioned= isPartitioned;
		declareParameter(new SqlParameter("V_AID", Types.INTEGER));
		declareParameter(new SqlParameter("V_UMIDL", Types.INTEGER));
		declareParameter(new SqlParameter("V_UMIDH", Types.INTEGER));
		declareParameter(new SqlOutParameter("V_MESG_SUB_FORMAT", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_MESG_NETWORK_PRIORITY", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_LAST_INST_NUM", Types.INTEGER));
		declareParameter(new SqlOutParameter("V_LAST_INST_TYPE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_LAST_INST_NOTIFICATION_TYPE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_LAST_INST_RELATED_NBR", Types.INTEGER));
		declareParameter(new SqlOutParameter("V_FIRST_INST_TYPE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_IAPP_NAME", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_NETWORK_DELIVERY_STATUS", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_NAK_REASON", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_RCV_DELIVERY_STATUS", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_DATE_TIME", Types.TIMESTAMP));
		declareParameter(new SqlOutParameter("V_APPE_REMOTE_INPUT_REFERENCE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_REMOTE_INPUT_TIME", Types.TIMESTAMP));
		declareParameter(new SqlOutParameter("V_APPE_LOCAL_OUTPUT_TIME", Types.TIMESTAMP));
		declareParameter(new SqlOutParameter("V_APPE_SESSION_HOLDER", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_SESSION_NBR", Types.INTEGER));
		declareParameter(new SqlOutParameter("V_APPE_SEQUENCE_NBR", Types.INTEGER));
		declareParameter(new SqlOutParameter("V_TEXT_SWIFT_BLOCK_5", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_TYPE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("V_APPE_ACK_NACK_TEXT", Types.VARCHAR));
		if ( partitioned ){
			declareParameter(new SqlParameter("v_MESG_CREA_DATE_TIME", Types.TIMESTAMP));
		}
	}
	
	@SuppressWarnings("rawtypes")
	public InstanceTransmissionPrintInfo execute(int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset ){	
		Map<String, Object> parameters = new HashMap<String, Object>(4);		
		parameters.put("V_AID", aid );
		parameters.put("V_UMIDL", umidl);
		parameters.put("V_UMIDH", umidh);
		if ( partitioned ){
			parameters.put("v_MESG_CREA_DATE_TIME",   mesg_crea_date );
		}
		Map result=null;
		try {
			  result= execute(parameters);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	
		
		InstanceTransmissionPrintInfo info= new InstanceTransmissionPrintInfo();
		info.setTimeZoneOffset(timeZoneOffset);
		info.setMesgSubFormat(StringUtils.trim((String)result.get("V_MESG_SUB_FORMAT")));
		info.setMesgNetworkPriority(StringUtils.trim((String)result.get("V_MESG_NETWORK_PRIORITY")));
		info.setLastInstNum((Integer)result.get("V_LAST_INST_NUM"));
		info.setLastInstType(StringUtils.trim((String)result.get("V_LAST_INST_TYPE")));
		info.setLastInstNotificationType(StringUtils.trim((String)result.get("V_LAST_INST_NOTIFICATION_TYPE")));
		info.setLastInstRelatedNbr((Integer)result.get("V_LAST_INST_RELATED_NBR"));
		info.setFirstInstType(StringUtils.trim((String)result.get("V_FIRST_INST_TYPE")));
		info.setAppeIappName(StringUtils.trim((String)result.get("V_APPE_IAPP_NAME")));
		info.setAppeNetworkDeliveryStatus(StringUtils.trim((String)result.get("V_APPE_NETWORK_DELIVERY_STATUS")));
		info.setAppeNakReason(StringUtils.trim((String)result.get("V_APPE_NAK_REASON")));
		info.setAppeRcvDeliveryStatus(StringUtils.trim((String)result.get("V_APPE_RCV_DELIVERY_STATUS")));
		
		Timestamp timestamp= (Timestamp)result.get("V_APPE_DATE_TIME");
		if ( timestamp != null ){
			info.setAppeDateTime( new Date(timestamp.getTime() ) ) ;
		}  
		info.setAppeRemoteInputReference(StringUtils.trim((String)result.get("V_APPE_REMOTE_INPUT_REFERENCE")));
		timestamp= (Timestamp)result.get("V_APPE_REMOTE_INPUT_TIME");
		if ( timestamp != null ){
			info.setAppeRemoteInputTime( new Date(timestamp.getTime() ) ) ;
		}
		timestamp= (Timestamp)result.get("V_APPE_LOCAL_OUTPUT_TIME");
		if ( timestamp != null ){
			info.setAppeLocalOutputTime( new Date(timestamp.getTime() ) ) ;
		}
		info.setAppeSessionHolder(StringUtils.trim((String)result.get("V_APPE_SESSION_HOLDER")));    
		info.setAppeSessionNbr((Integer)result.get("V_APPE_SESSION_NBR"));    
		info.setAppeSequenceNbr((Integer)result.get("V_APPE_SEQUENCE_NBR"));    
		info.setTextSwiftBlock5(StringUtils.trim((String)result.get("V_TEXT_SWIFT_BLOCK_5")));    
		info.setAppeType(StringUtils.trim((String)result.get("V_APPE_TYPE")));    
		info.setAppeAckNackText(StringUtils.trim((String)result.get("V_APPE_ACK_NACK_TEXT")));
		return info;
	}
}