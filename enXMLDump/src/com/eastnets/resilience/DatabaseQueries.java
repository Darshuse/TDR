package com.eastnets.resilience;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public abstract class DatabaseQueries {	
	protected Connection connection;
	
	protected abstract String getTableName();
	protected abstract String getShema();
	public abstract void initTempTable(EnXmlDumpConfig config) throws SQLException;
	protected abstract String formatDate(  Date date ) ;
	
	protected DatabaseQueries( Connection connection ){
		this.connection= connection;
	}	
	
	protected final String tempTableInsert = "INSERT INTO " + getTableName() + " (t_aid,t_umidl,t_umidh) SELECT distinct z.aid,z.mesg_s_umidl,z.mesg_s_umidh from " + getShema() + ".rmesg z ";
	
	protected final String mesgSelect = "select MESG_S_UMIDL, MESG_S_UMIDH, MESG_VALIDATION_REQUESTED, MESG_VALIDATION_PASSED, MESG_CLASS, MESG_IS_TEXT_READONLY, " +
			"MESG_IS_DELETE_INHIBITED, MESG_IS_TEXT_MODIFIED, MESG_IS_PARTIAL, MESG_STATUS, MESG_CREA_APPL_SERV_NAME, MESG_CREA_MPFN_NAME, MESG_CREA_RP_NAME, MESG_CREA_OPER_NICKNAME, " +
			"MESG_CREA_DATE_TIME, MESG_MOD_OPER_NICKNAME, MESG_MOD_DATE_TIME, MESG_CAS_TARGET_RP_NAME, MESG_RECOVERY_ACCEPT_INFO, MESG_UUMID, MESG_UUMID_SUFFIX, MESG_SENDER_CORR_TYPE, " +
			"MESG_SENDER_X1, MESG_FRMT_NAME, MESG_SUB_FORMAT, MESG_SYNTAX_TABLE_VER, MESG_NATURE, MESG_NETWORK_APPL_IND, MESG_TYPE, MESG_IS_LIVE, MESG_NETWORK_PRIORITY, " +
			"MESG_DELV_OVERDUE_WARN_REQ, MESG_NETWORK_DELV_NOTIF_REQ, MESG_USER_REFERENCE_TEXT, MESG_FIN_VALUE_DATE, MESG_FIN_CCY_AMOUNT, MESG_TRN_REF, MESG_REL_TRN_REF, " +
			"MESG_MESG_USER_GROUP, MESG_ZZ41_IS_POSSIBLE_DUP, MESG_IDENTIFIER, MESG_SENDER_SWIFT_ADDRESS, MESG_RECEIVER_SWIFT_ADDRESS, MESG_SERVICE, MESG_COPY_SERVICE_ID, " +
			"MESG_USER_PRIORITY_CODE, MESG_USER_REFERENCE_TEXT, MESG_RELEASE_INFO, MESG_MESG_USER_GROUP "
			+ "from " + getShema() + ".RMESG m inner join " + getTableName() + " r on m.aid = r.t_aid AND m.mesg_s_umidl = r.t_umidl AND m.mesg_s_umidh = r.t_umidh order by m.mesg_s_umidl asc, m.mesg_s_umidh asc";
	
	protected final String instSelect = "select AID, INST_S_UMIDL, INST_S_UMIDH, INST_NUM, INST_TYPE, INST_NOTIFICATION_TYPE, INST_STATUS, INST_RELATED_NBR, INST_APPE_DATE_TIME, " +
			"INST_APPE_SEQ_NBR, INST_UNIT_NAME, INST_RP_NAME, INST_MPFN_NAME, INST_MPFN_HANDLE, INST_PROCESS_STATE, INST_LAST_MPFN_RESULT, INST_RELATIVE_REF, INST_SM2000_PRIORITY, " +
			"INST_DEFERRED_TIME, INST_CREA_APPL_SERV_NAME, INST_CREA_MPFN_NAME, INST_CREA_RP_NAME, INST_CREA_DATE_TIME, INITIAL_TARGET_RP_NAME, INST_AUTH_OPER_NICKNAME, " +
			"INST_LAST_OPER_NICKNAME, INST_RECEIVER_CORR_TYPE, INST_RECEIVER_X1, INST_RECEIVER_INSTITUTION_NAME, INST_RECEIVER_BRANCH_INFO, INST_RECEIVER_CITY_NAME, INST_RECEIVER_CTRY_CODE, " +
			"INST_INTV_SEQ_NBR, INST_INTV_DATE_TIME, INST_RCV_DELIVERY_STATUS, INST_RECEIVER_NETWORK_IAPP_NAM " +			
			"from " + getShema() + ".rinst i inner join " + getTableName() + " r on i.aid = r.t_aid AND i.inst_s_umidl = r.t_umidl AND i.inst_s_umidh = r.t_umidh order by i.inst_s_umidl asc, i.inst_s_umidh asc, i.inst_num asc";
	
	protected final String intvSelect = "select AID, INTV_S_UMIDL, INTV_S_UMIDH, INTV_INST_NUM, INTV_DATE_TIME, INTV_SEQ_NBR, INTV_INTY_NUM, INTV_INTY_NAME, INTV_INTY_CATEGORY, " +
			"INTV_OPER_NICKNAME, INTV_APPL_SERV_NAME, INTV_MPFN_NAME, INTV_APPE_SEQ_NBR, INTV_LENGTH, INTV_MERGED_TEXT " +
			"from " + getShema() + ".rIntv i inner join " + getTableName() + " r on i.aid = r.t_aid AND i.intv_s_umidl = r.t_umidl AND i.intv_s_umidh = r.t_umidh order by i.intv_s_umidl asc, i.intv_s_umidh asc, " +
			"i.intv_inst_num asc, i.intv_seq_nbr asc";
	
	protected final String appeSelect = "select AID, APPE_S_UMIDL, APPE_S_UMIDH, APPE_INST_NUM, APPE_DATE_TIME, APPE_SEQ_NBR, APPE_IAPP_NAME, APPE_TYPE, APPE_SESSION_HOLDER, APPE_SESSION_NBR, " +
			"APPE_SEQUENCE_NBR, APPE_TRANSMISSION_NBR, APPE_CREA_APPL_SERV_NAME, APPE_CREA_MPFN_NAME, APPE_CREA_RP_NAME, APPE_CHECKSUM_RESULT, APPE_CHECKSUM_VALUE, APPE_CONN_RESPONSE_CODE, " +
			"APPE_CONN_RESPONSE_TEXT, APPE_NETWORK_DELIVERY_STATUS, APPE_RCV_DELIVERY_STATUS, " + getShema() + ".getLargeFieldAppe( APPE_RECORD_ID,APPE_LARGE_DATA,'appe_ack_nack_text') as APPE_ACK_NACK_TEXT, " +
			"APPE_NAK_REASON, APPE_REMOTE_INPUT_REFERENCE, APPE_REMOTE_INPUT_TIME, APPE_LOCAL_OUTPUT_TIME, " + getShema() + ".getLargeFieldAppe( APPE_RECORD_ID,APPE_LARGE_DATA,'appe_pki_auth_value') as APPE_PKI_AUTH_VALUE, " +
			"APPE_COMBINED_AUTH_RES, APPE_PKI_PAC2_RESULT, APPE_PKI_AUTHORISATION_RES, APPE_PKI_AUTHENTICATION_RES, APPE_COMBINED_AUTH_RES, APPE_COMBINED_PAC_RES, APPE_RMA_CHECK_RESULT, " +
			"APPE_SENDER_SWIFT_ADDRESS " +
			"from " + getShema() + ".rappe a inner join " + getTableName() + " r on a.aid = r.t_aid AND a.appe_s_umidl = r.t_umidl AND a.appe_s_umidh = r.t_umidh order by a.appe_s_umidl asc, " +
			"a.appe_s_umidh asc, a.appe_inst_num, a.appe_seq_nbr asc";
	
	protected final String textSelect = "select AID, TEXT_S_UMIDL, TEXT_S_UMIDH, TEXT_DATA_BLOCK, TEXT_SWIFT_BLOCK_5 " +
			"from " + getShema() + ".rText t inner join " + getTableName() + " r on t.aid = r.t_aid AND t.text_s_umidl = r.t_umidl AND t.text_s_umidh = r.t_umidh order by t.text_s_umidl asc, t.text_s_umidh asc";
	
	protected final String textFieldSelect = "select AID, TEXT_S_UMIDL, TEXT_S_UMIDH, FIELD_CODE, FIELD_OPTION, VALUE, VALUE_MEMO " +
			"from  " + getShema() + ".rTextField t inner join " + getTableName() + " r on t.aid = r.t_aid AND t.text_s_umidl = r.t_umidl AND t.text_s_umidh = r.t_umidh order by t.text_s_umidl asc, t.text_s_umidh asc, field_cnt";
	
	protected final String systemTextFieldSelect = "select AID, TEXT_S_UMIDL, TEXT_S_UMIDH, FIELD_CNT, FIELD_CODE, VALUE " +
			"from  " + getShema() + ".rSystemTextField t inner join " + getTableName() + " r on t.aid = r.t_aid AND t.text_s_umidl = r.t_umidl AND t.text_s_umidh = r.t_umidh order by t.text_s_umidl asc, t.text_s_umidh asc, field_cnt";
	

	public ResultSet getMesgResultSet() throws SQLException {
		//no TYPE_SCROLL_SENSITIVE, cause we just want to move forward for messages 
		return connection.createStatement().executeQuery(mesgSelect);
	}

	public ResultSet getInstResultSet() throws SQLException {
		//TYPE_SCROLL_SENSITIVE, means that we can move forward and backward in the result set
		return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(instSelect);
	}

	public ResultSet getIntvResultSet() throws SQLException {
		//TYPE_SCROLL_SENSITIVE, means that we can move forward and backward in the result set
		return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(intvSelect);
	}

	public ResultSet getAppeResultSet() throws SQLException {
		//TYPE_SCROLL_SENSITIVE, means that we can move forward and backward in the result set
		return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(appeSelect);
	}

	public ResultSet getTextResultSet() throws SQLException {
		//TYPE_SCROLL_SENSITIVE, means that we can move forward and backward in the result set
		return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(textSelect);
	}

	public ResultSet getTextFieldResultSet() throws SQLException {
		//TYPE_SCROLL_SENSITIVE, means that we can move forward and backward in the result set
		return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(textFieldSelect);
	}

	public ResultSet getSystemTextFieldResultSet() throws SQLException {
		//TYPE_SCROLL_SENSITIVE, means that we can move forward and backward in the result set
		return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(systemTextFieldSelect);
	}
	

	protected String getWhereStatement(Integer aid, Date from_date, Date to_date, boolean swiftNet, boolean acked) {
		String where = String.format(" where z.mesg_frmt_name = 'Swift' and  z.aid = %d", aid);

		if (swiftNet) {
			where = ", rAppe a " + where;
			where += " AND a.aid = z.aid and a.appe_s_umidl = z.mesg_s_umidl and a.appe_s_umidh = z.mesg_s_umidh AND a.appe_iapp_name = 'SWIFT' ";
		}

		if (acked) {
			if (!swiftNet) {
				where = ", rAppe a " + where;
			}
			where += " AND ( (appe_type = 'APPE_RECEPTION' AND appe_rcv_delivery_status = 'RE_UACKED')or(appe_type = 'APPE_EMISSION'  AND appe_network_delivery_status = 'DLV_ACKED'  AND appe_inst_num = 0))" +
					 " AND appe_iapp_name = 'SWIFT' AND x_appe_last = 1";
		}
		
		String fromDateFormatted = formatDate(from_date);
		if (fromDateFormatted != null) {
			where += " and z.MESG_CREA_DATE_TIME >= " + fromDateFormatted;
		}
		String toDateFormatted = formatDate(to_date);
		if (toDateFormatted != null) {
			where += " and z.MESG_CREA_DATE_TIME <= " + toDateFormatted;
		}

		return where;
	}
}
