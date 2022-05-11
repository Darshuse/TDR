package com.eastnets.message.summary.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.message.summary.Bean.MessageSummaryDTO;

public class MessageReaderOracleDAO extends MessageReaderDAO {

	private static final Logger LOGGER = LogManager.getLogger(MessageReaderOracleDAO.class);

	@Override
	public List<MessageSummaryDTO> getMessageSummaryMesgInformation() {

		LOGGER.trace("MessageReaderOracleDAO - getting message summary list");

		String query = generateQuery();

		try {
			Object[] param = null;
			List<MessageSummaryDTO> messageSummaryList = jdbcTemplate.query(query, param, new RowMapper<MessageSummaryDTO>() {
				public MessageSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					MessageSummaryDTO messageSummaryDTO = new MessageSummaryDTO();
					messageSummaryDTO.setAid(Long.parseLong(rs.getString("AID")));
					messageSummaryDTO.setUmidl(Long.parseLong(rs.getString("MESG_S_UMIDL")));
					messageSummaryDTO.setUmidh(Long.parseLong(rs.getString("MESG_S_UMIDH")));
					messageSummaryDTO.setMesgReference(rs.getString("MESG_TRN_REF"));
					messageSummaryDTO.setMesgRelatedReference(rs.getString("MESG_REL_TRN_REF"));
					messageSummaryDTO.setMesgCreateMPFNName(rs.getString("MESG_CREA_MPFN_NAME"));
					messageSummaryDTO.setMesgCreateRPName(rs.getString("MESG_CREA_RP_NAME"));
					messageSummaryDTO.setMesgCreateOperatorNickName(rs.getString("MESG_CREA_OPER_NICKNAME"));
					messageSummaryDTO.setMesgCreateDateTime(rs.getDate("MESG_CREA_DATE_TIME"));
					messageSummaryDTO.setModificationOperatorNickName(rs.getString("MESG_MOD_OPER_NICKNAME"));
					messageSummaryDTO.setModificationDateTime(rs.getDate("MESG_MOD_DATE_TIME"));
					messageSummaryDTO.setMesgFormatName(rs.getString("MESG_FRMT_NAME"));
					messageSummaryDTO.setxOwnLT(rs.getString("X_OWN_LT"));
					messageSummaryDTO.setxInst0UnitName(rs.getString("X_INST0_UNIT_NAME"));
					messageSummaryDTO.setxCategory(rs.getString("X_CATEGORY"));
					messageSummaryDTO.setArchived(Long.parseLong(rs.getString("ARCHIVED")));
					messageSummaryDTO.setMesgStatus(rs.getString("MESG_STATUS"));
					messageSummaryDTO.setMesgNetworkPriority(rs.getString("MESG_NETWORK_PRIORITY"));
					messageSummaryDTO.setMesgPossibleDuplicateCreation(rs.getString("MESG_POSSIBLE_DUP_CREATION"));
					messageSummaryDTO.setMesgReceiverSwiftAddress(rs.getString("MESG_RECEIVER_SWIFT_ADDRESS"));
					messageSummaryDTO.setMesgSubFormat(rs.getString("MESG_SUB_FORMAT"));
					messageSummaryDTO.setMesgType(rs.getString("MESG_TYPE"));
					messageSummaryDTO.setxFinCcy(rs.getString("X_FIN_CCY"));
					messageSummaryDTO.setxFinAmount(rs.getDouble("X_FIN_AMOUNT"));
					messageSummaryDTO.setxFinValueDate(rs.getDate("X_FIN_VALUE_DATE"));
					messageSummaryDTO.setReceicerBIC(rs.getString("X_RECEIVER_X1"));
					messageSummaryDTO.setSenderBIC(rs.getString("MESG_SENDER_X1"));
					messageSummaryDTO.setMesgSLA(rs.getString("MESG_SLA"));
					messageSummaryDTO.setMesgUETR(rs.getString("MESG_E2E_TRANSACTION_REFERENCE"));
					messageSummaryDTO.setxInst0RPName(rs.getString("X_INST0_RP_NAME"));
					messageSummaryDTO.setxLastEMIAppeDelvStatus(rs.getString("X_LAST_EMI_APPE_DELV_STS"));
					messageSummaryDTO.setMesgStatusCode(rs.getString("MESG_STATUS_CODE"));
					messageSummaryDTO.setMesgReasonCode(rs.getString("MESG_REASON_CODE"));
					messageSummaryDTO.setMesgNakCode(rs.getString("MESG_NAK_CODE"));
					messageSummaryDTO.setMesgCharges(rs.getString("MESG_CHARGES"));
					messageSummaryDTO.setFetchStatus(rs.getInt("FETCH_STATUS"));
					messageSummaryDTO.setCurrencyName(rs.getString("CURRENCYNAME"));
					messageSummaryDTO.setAppeSessionHolder(rs.getString("APPE_SESSION_HOLDER"));
					messageSummaryDTO.setAppeApplicationName(rs.getString("APPE_IAPP_NAME"));
					messageSummaryDTO.setAppeType(rs.getString("APPE_TYPE"));
					messageSummaryDTO.setAppeDateTime(rs.getDate("APPE_DATE_TIME"));
					messageSummaryDTO.setMesgStxVersion(rs.getString("MESG_SYNTAX_TABLE_VER"));
					messageSummaryDTO.setMesgCopyService(rs.getString("mesg_copy_service_id"));

					if (messageSummaryDTO.getAppeDateTime() != null) {
						long timeDiff = messageSummaryDTO.getAppeDateTime().getTime() - messageSummaryDTO.getMesgCreateDateTime().getTime();
						messageSummaryDTO.setTransmissionDelay((timeDiff / 3600000) + " hour/s " + (timeDiff % 3600000) / 60000 + " minutes");

					}

					return messageSummaryDTO;
				}
			});

			LOGGER.trace("Messages List is ready to be returned ");
			return messageSummaryList;
		} catch (Exception e) {

			auditService.insertIntoErrorld("Error", "Error in reading messages");
			LOGGER.error("Error in reading messages");
			LOGGER.error(e);
			return null;
		}
	}

	@Autowired
	public String generateQuery() {

		LOGGER.trace("Generating Oracle Reading Query");
		try {

			StringBuilder query = new StringBuilder();
			query.append("SELECT M.AID ,M.MESG_S_UMIDL,M.MESG_S_UMIDH,M.MESG_TRN_REF, M.MESG_REL_TRN_REF,M.MESG_SYNTAX_TABLE_VER, M.MESG_CREA_MPFN_NAME,M.mesg_copy_service_id ,"
					+ "	M.MESG_CREA_RP_NAME, M.MESG_CREA_OPER_NICKNAME, M.MESG_CREA_DATE_TIME, M.MESG_MOD_OPER_NICKNAME,	" + "M.MESG_MOD_DATE_TIME, M.MESG_FRMT_NAME, M.X_OWN_LT, M.X_INST0_UNIT_NAME,	M.X_CATEGORY,"
					+ "	M.ARCHIVED,	M.MESG_STATUS,	M.MESG_NETWORK_PRIORITY, M.MESG_POSSIBLE_DUP_CREATION," + "	M.MESG_RECEIVER_SWIFT_ADDRESS, M.MESG_SUB_FORMAT, M.MESG_TYPE, M.X_FIN_CCY, M.X_FIN_AMOUNT,"
					+ "	M.X_FIN_VALUE_DATE,	M.X_RECEIVER_X1, M.MESG_SENDER_X1, M.MESG_SLA, M.MESG_E2E_TRANSACTION_REFERENCE, M.X_INST0_RP_NAME,"
					+ "	M.X_LAST_EMI_APPE_DELV_STS,	M.MESG_STATUS_CODE,	M.MESG_REASON_CODE,	M.MESG_NAK_CODE, M.MESG_CHARGES ," + " M.FETCH_STATUS ," + getCurrenciesColumnsQuery() + ", " + getAppendixColumnsQuery() + " " + " FROM RMESG M WHERE");

			query.append(buildFetchMessagesConditionsQuery());

			query.append(" ORDER BY MESG_CREA_DATE_TIME ASC");

			return query.toString();

		} catch (Exception e) {
			auditService.insertIntoErrorld("Error", "Error in generating messages reading query");
			LOGGER.error("Error in generating messages reading query");
			LOGGER.error(e);
			return null;
		}
	}

	@Autowired
	public String getAppendixColumnsQuery() {

		StringBuilder appendixQuery = new StringBuilder();

		String[] appendixColumns = { "APPE_SESSION_HOLDER", "APPE_DATE_TIME", "APPE_TYPE", "APPE_IAPP_NAME" };

		for (int i = 0; i < appendixColumns.length; i++) {

			String query = " (SELECT " + appendixColumns[i].trim() + " FROM RAPPE A WHERE A.APPE_S_UMIDH = M.MESG_S_UMIDH AND A.APPE_S_UMIDL = M.MESG_S_UMIDL AND A.AID = M.AID ";
			if (globalConfiguration.isPartitioned()) {
				query += "AND A.X_CREA_DATE_TIME_MESG = M.MESG_CREA_DATE_TIME ";
			}
			query += "AND A.APPE_INST_NUM = 0 AND A.APPE_SEQ_NBR = (SELECT MAX(APPE_SEQ_NBR) FROM RAPPE " + "WHERE APPE_S_UMIDH = M.MESG_S_UMIDH AND APPE_S_UMIDL = MESG_S_UMIDL" + " AND AID = M.AID AND APPE_INST_NUM = 0 ";
			if (globalConfiguration.isPartitioned()) {
				query += "AND A.X_CREA_DATE_TIME_MESG = M.MESG_CREA_DATE_TIME ";
			}
			query += ")) AS " + appendixColumns[i].trim() + " ";

			if (i != appendixColumns.length - 1) {
				query += ", ";
			}

			appendixQuery.append(query);

		}

		return appendixQuery.toString();
	}

	@Autowired
	public String getCurrenciesColumnsQuery() {

		StringBuilder currencyQuery = new StringBuilder();

		String[] currencyColumns = { "CURRENCYNAME" };

		for (int i = 0; i < currencyColumns.length; i++) {

			String query = " (SELECT " + currencyColumns[i].trim() + " FROM CU WHERE CURRENCYCODE = M.X_FIN_CCY AND ROWNUM = 1) AS " + currencyColumns[i].trim() + " ";
			if (i != currencyColumns.length - 1) {
				query += ", ";
			}

			currencyQuery.append(query);

		}

		return currencyQuery.toString();
	}

	@Override
	public String buildFetchMessagesConditionsQuery() {

		LOGGER.trace("Building the conditions statements");

		try {

			StringBuilder query = new StringBuilder("");
			Integer aid = globalConfiguration.getAid();
			String fromDate = globalConfiguration.getFromDate();
			String toDate = globalConfiguration.getToDate();

			query.append(" (FETCH_STATUS  is null or FETCH_STATUS  IN (0,2)) ");

			if (aid != null) {
				query.append("AND AID = ");
				query.append(aid + " ");
			}

			if ((fromDate != null && !fromDate.isEmpty()) && (toDate == null || toDate.isEmpty())) {
				query.append("AND MESG_CREA_DATE_TIME >= ");
				query.append("TO_DATE('" + fromDate + "','dd/mm/yyyy') ");

			} else if ((fromDate == null || fromDate.isEmpty()) && (toDate != null && !toDate.isEmpty())) {
				query.append("AND MESG_CREA_DATE_TIME <= ");
				query.append("TO_DATE('" + toDate + "','dd/mm/yyyy') ");

			} else if ((fromDate != null && !fromDate.isEmpty()) && (toDate != null && !toDate.isEmpty())) {
				query.append("AND MESG_CREA_DATE_TIME BETWEEN  ");
				query.append("TO_DATE('" + fromDate + "','dd/mm/yyyy') AND ");
				query.append("TO_DATE('" + toDate + "','dd/mm/yyyy') ");
			}

			query.append("AND ROWNUM <= " + globalConfiguration.getAnalyticsParserBulkSize() + " ");
			return query.toString();

		} catch (Exception e) {
			auditService.insertIntoErrorld("Error", "Error in building conditions query");
			return "";
		}
	}

}
