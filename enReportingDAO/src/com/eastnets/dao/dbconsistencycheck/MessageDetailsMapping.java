package com.eastnets.dao.dbconsistencycheck;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.RowMapper;


public class MessageDetailsMapping implements RowMapper<MessageDetails>{

	@Override
	public MessageDetails mapRow(ResultSet arg0, int arg1) throws SQLException {
		MessageDetails  messageDetails = new MessageDetails();
		
		SimpleDateFormat message_crea_date_time_format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		SimpleDateFormat message_value_date_format = new SimpleDateFormat("dd-MMM-yyyy");
		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		
		
		messageDetails.setMesg_amount(( (Double)arg0.getDouble("X_FIN_AMOUNT")).toString() == null ? "" : decimalFormat.format(((Double)(arg0.getDouble("X_FIN_AMOUNT")))));
		messageDetails.setMesg_crea_date_time( (arg0.getDate("mesg_crea_date_time") == null ? "" : message_crea_date_time_format.format(arg0.getTimestamp("mesg_crea_date_time")) )  );
		messageDetails.setMesg_createdby( arg0.getString("MESG_CREA_OPER_NICKNAME") == null ? "" : arg0.getString("MESG_CREA_OPER_NICKNAME"));
		messageDetails.setMesg_currency((arg0.getString("X_FIN_CCY") == null ? "" :arg0.getString("X_FIN_CCY") ));
		messageDetails.setMesg_frmt_name((arg0.getString("mesg_frmt_name")== null ? "" : arg0.getString("mesg_frmt_name")));
		messageDetails.setMesg_receiver((arg0.getString("X_RECEIVER_X1")) == null ? "" : arg0.getString("X_RECEIVER_X1"));
		messageDetails.setMesg_sender((arg0.getString("MESG_SENDER_X1") == null ? "" : arg0.getString("MESG_SENDER_X1")));
		messageDetails.setMesg_sub_format((arg0.getString("mesg_sub_format") == null ? "" : arg0.getString("mesg_sub_format")));
		messageDetails.setMesg_type((arg0.getString("mesg_identifier")== null ? "" :arg0.getString("mesg_identifier") ));
		messageDetails.setTrn_ref((arg0.getString("MESG_TRN_REF") != null ? arg0.getString("MESG_TRN_REF") : (arg0.getString("MESG_USER_REFERENCE_TEXT") != null ? arg0.getString("MESG_USER_REFERENCE_TEXT") : "") ));
		messageDetails.setValueDate((arg0.getDate("X_FIN_VALUE_DATE") == null ? "" : message_value_date_format.format(arg0.getDate("X_FIN_VALUE_DATE") )));
		
		return messageDetails;
	}
	
	
}
