package com.eastnets.resilience;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageFormatter {

	public static String format(ResultSet resultSet, String field_tag) throws SQLException {
		if ( field_tag.equalsIgnoreCase("INST_NOTIFICATION_TYPE") ){
			String val=  ExportHelper.substring( ExportHelper.defaultString( resultSet.getString(field_tag) ), 18) ;
			if ( "INFO".equalsIgnoreCase(val.trim()) ){
				val= "INFORMATION";
			}
			return val;
		}
		if ( field_tag.equalsIgnoreCase("APPE_PKI_AUTHENTICATION_RES") || field_tag.equalsIgnoreCase("APPE_PKI_AUTHORISATION_RES") || field_tag.equalsIgnoreCase("APPE_PKI_PAC2_RESULT") || field_tag.equalsIgnoreCase("APPE_COMBINED_AUTH_RES") ){
			String val=  ExportHelper.substring( ExportHelper.defaultString( resultSet.getString(field_tag) ), 5) ;
			if ( "NO_RMA_REC".equalsIgnoreCase(val) ){
				val= "NO_RMA_RECORD";
			}
			if ( "RMA_SIG_FAILURE".equalsIgnoreCase(val) ){
				val= "SIGNATURE_FAILURE";
			}
			if ( "SIG_FAILURE".equalsIgnoreCase(val) ){
				val= "SIGNATURE_FAILURE";
			}
			if ( "RMA_NO_RMAU_BKEY".equalsIgnoreCase(val) ){
				val= "NO_RMAU_BKEY";
			}
			if ( "RMA_INVALID_SIGN_DN".equalsIgnoreCase(val) ){
				val= "INVALID_SIGN_DN";
			}
			return val;
		}
		if ( field_tag.equalsIgnoreCase("APPE_RMA_CHECK_RESULT") ){
			String val=  ExportHelper.substring( ExportHelper.defaultString( resultSet.getString(field_tag) ), 10) ;
			if ( "NO_REC".equalsIgnoreCase(val) ){
				val= "NO_RMA_RECORD";
			}
			if ( "NOT_ENABLED".equalsIgnoreCase(val) ){
				val= "RMA_NOT_ENABLED";
			}
			if ( "NOT_IN_VALID_PERIOD".equalsIgnoreCase(val) ){
				val= "RMA_NOT_IN_VALID_PERIOD";
			}
			if ( "NOT_AUTHORISED".equalsIgnoreCase(val) ){
				val= "RMA_NOT_AUTHORISED";
			}
			return val;
		}
		if ( field_tag.equalsIgnoreCase("APPE_RCV_DELIVERY_STATUS") || field_tag.equalsIgnoreCase("INST_RCV_DELIVERY_STATUS") ){
			String receiverDelStatus= ExportHelper.defaultString( resultSet.getString(field_tag) );
			if ( receiverDelStatus.startsWith("EM_") ){
				receiverDelStatus= receiverDelStatus.substring(3);
			}
			if ( "RE_N_A".equalsIgnoreCase( receiverDelStatus ) ){
				receiverDelStatus= "N_A";
			}
			return receiverDelStatus;
		}
		return "";
	}

}
