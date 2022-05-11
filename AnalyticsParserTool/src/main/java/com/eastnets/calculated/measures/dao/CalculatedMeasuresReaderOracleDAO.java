package com.eastnets.calculated.measures.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.eastnets.audit.service.AuditService;
import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.Bean.MessageSummaryPk;
import com.eastnets.message.summary.Bean.TextFieldBean;
import com.eastnets.message.summary.configuration.GlobalConfiguration;

@Repository
public class CalculatedMeasuresReaderOracleDAO implements CalculatedMeasuresReaderDAO {

	private static final Logger LOGGER = LogManager.getLogger(CalculatedMeasuresReaderOracleDAO.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	GlobalConfiguration globalConfiguration;

	@Autowired
	AuditService auditService;

	public List<MessageSummaryDTO> getMessageSummaryTextFields() {

		LOGGER.trace("Getting Messages from mesg_summary and rtextfield tables");

		String query = generateMessageSummayQuery();
		Map<MessageSummaryPk, MessageSummaryDTO> messageSummaryMap = new HashMap<>();
		List<MessageSummaryDTO> messageSummaryList = new ArrayList<>();
		try {
			Object[] param = null;
			jdbcTemplate.query(query, param, new RowMapper<MessageSummaryDTO>() {

				public MessageSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

					MessageSummaryPk messageSummaryPk = new MessageSummaryPk();
					MessageSummaryDTO messageSummaryBean = new MessageSummaryDTO();

					messageSummaryBean.setAid(Long.parseLong(rs.getString("AID")));
					messageSummaryBean.setUmidl(Long.parseLong(rs.getString("MESG_S_UMIDL")));
					messageSummaryBean.setUmidh(Long.parseLong(rs.getString("MESG_S_UMIDH")));
					messageSummaryBean.setMesgType(rs.getString("MESG_TYPE"));
					messageSummaryBean.setSenderBIC(rs.getString("MESG_SENDER_X1"));
					messageSummaryBean.setReceicerBIC(rs.getString("X_RECEIVER_X1"));
					messageSummaryBean.setMesgSubFormat(rs.getString("MESG_SUB_FORMAT"));
					messageSummaryBean.setMesgCreateDateTime(rs.getDate("MESG_CREA_DATE_TIME"));

					messageSummaryPk.setAid(messageSummaryBean.getAid());
					messageSummaryPk.setUmidh(messageSummaryBean.getUmidh());
					messageSummaryPk.setUmidl(messageSummaryBean.getUmidl());

					if (!messageSummaryMap.containsKey(messageSummaryPk)) {
						addTextFields(rs, messageSummaryBean);
						messageSummaryMap.put(messageSummaryPk, messageSummaryBean);
					} else {

						addTextFields(rs, messageSummaryMap.get(messageSummaryPk));
						messageSummaryMap.put(messageSummaryPk, messageSummaryMap.get(messageSummaryPk));
					}
					return messageSummaryBean;
				}
			});

			messageSummaryList.addAll(messageSummaryMap.values().stream().collect(Collectors.toList()));
			if (messageSummaryList.isEmpty()) {
				LOGGER.trace("No data returned from mesg_summary table");
			} else {
				LOGGER.trace(messageSummaryList.size() + " messages returned from  mesg_summary table");
			}

			return messageSummaryList;
		} catch (Exception e) {

			auditService.insertIntoErrorld("Error", "Error in reading data from mesg_summary table");
			LOGGER.error("Error in reading data from mesg_summary table");
			LOGGER.error(e);
			return new ArrayList<>();
		}
	}

	public void addTextFields(ResultSet rs, MessageSummaryDTO messageSummaryBean) throws SQLException {

		Map<Integer, TextFieldBean> textFieldsList = new HashMap<>();
		textFieldsList = messageSummaryBean.getFieldsOptionsValueList();
		TextFieldBean textFieldBean = new TextFieldBean();
		List<String> optionValues = new ArrayList<>();
		Map<String, List<String>> optionsValuesMap = new HashMap<>();

		String fieldOption = rs.getString("FIELD_OPTION");
		String value = rs.getString("VALUE");
		Integer fieldCode = rs.getInt("FIELD_CODE");

		if (!textFieldsList.containsKey(fieldCode)) {

			textFieldBean.setFieldCode(fieldCode);
			textFieldBean.setFieldCodeId(rs.getInt("FIELD_CODE_ID"));
			textFieldBean.setSequenceId(rs.getString("SEQUENCE_ID"));
			textFieldBean.setGroupIdx(rs.getInt("GROUP_IDX"));
		} else if (fieldOption != null && !fieldOption.isEmpty() && textFieldsList.containsKey(fieldCode)
				&& textFieldsList.get(fieldCode).getOptionsValuesMap().containsKey(fieldOption)) {

			optionValues.addAll(textFieldsList.get(fieldCode).getOptionsValuesMap().get(fieldOption));
			// TODO: should override without removing
			textFieldsList.get(fieldCode).getOptionsValuesMap().get(fieldOption).clear();

		}

		if (fieldOption != null) {
			optionValues.add(value);
			optionsValuesMap.put(fieldOption, optionValues);
			textFieldBean.setOptionsValuesMap(optionsValuesMap);
		} else {
			if (textFieldsList.containsKey(fieldCode)) {
				optionValues.addAll(textFieldsList.get(fieldCode).getListOfvalue());
			}
			optionValues.add(value);
			textFieldBean.setListOfvalue(optionValues);

		}
		textFieldsList.put(fieldCode, textFieldBean);
		messageSummaryBean.setFieldsOptionsValueList(textFieldsList);
	}

	public String generateMessageSummayQuery() {

		try {
			StringBuilder query = new StringBuilder();
			query.append("select M.AID,M.MESG_S_UMIDL,M.MESG_S_UMIDH,M.MESG_TYPE,M.MESG_SENDER_X1 ,"
					+ "M.X_RECEIVER_X1,M.MESG_SUB_FORMAT,M.MESG_CREA_DATE_TIME ,"
					+ "F.FIELD_CODE,F.FIELD_CODE_ID,F.FIELD_OPTION,F.VALUE,F.SEQUENCE_ID,F.GROUP_IDX  "
					+ "from ( (SELECT AID,MESG_S_UMIDL,MESG_S_UMIDH,MESG_TYPE,MESG_SENDER_X1 , "
					+ "X_RECEIVER_X1,MESG_SUB_FORMAT,MESG_CREA_DATE_TIME FROM MESG_SUMMARY where ");

			query.append(buildFetchMessagesConditionsQuery());

			query.append(" ) M " + "inner join ( SELECT  AID ,TEXT_S_UMIDH,TEXT_S_UMIDL, FIELD_CODE,FIELD_CODE_ID,"
					+ "FIELD_OPTION,VALUE,SEQUENCE_ID,GROUP_IDX FROM RTEXTFIELD ) F" + " on F.AID = M.AID "
					+ "AND F.TEXT_S_UMIDH = M.MESG_S_UMIDH " + "AND F.TEXT_S_UMIDL= M.MESG_S_UMIDL) ");
			query.append(" ORDER BY MESG_CREA_DATE_TIME ASC");

			return query.toString();

		} catch (Exception e) {
			auditService.insertIntoErrorld("Error", "Error in generating MESG_SUMMARY query");
			LOGGER.error("Error in generating MESG_SUMMARY query");
			LOGGER.error(e);
			return null;
		}
	}

	public String buildFetchMessagesConditionsQuery() {

		StringBuilder query = new StringBuilder("");
		Integer aid = globalConfiguration.getAid();
		String fromDate = globalConfiguration.getFromDate();
		String toDate = globalConfiguration.getToDate();

		query.append(" MESG_TYPE IN('103' ,'202' ,'205') AND CALCULATED_STATUS  = 0 ");

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
		query.append("AND ROWNUM <= " + globalConfiguration.getAnalyticsParserDetailsBulkSize() + " ");
		return query.toString();

	}

}
