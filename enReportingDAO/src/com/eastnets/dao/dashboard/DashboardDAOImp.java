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

package com.eastnets.dao.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.domain.dashboard.ChartRecord;
import com.eastnets.domain.dashboard.MultiSeriesChartRecord;

/**
 * Dashboard DAO Implementation 
 * @author EastNets
 * @since July 22, 2012
 */
public class DashboardDAOImp extends DAOBaseImp implements DashboardDAO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1042351497350409811L;

	@Override
	public List<ChartRecord> getMessagesCountPerCountry(Date dateFrom, Date dateTo, long loggedUserGroupID) {
		
		String messagesPerCountryQuery = getMessagesPerCountryQuery(dateFrom, dateTo, loggedUserGroupID);
		List<ChartRecord> records = jdbcTemplate.query(messagesPerCountryQuery, new RowMapper<ChartRecord>() {
			
			@Override
			public ChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				ChartRecord msgPerCountryChart = new ChartRecord();
				msgPerCountryChart.setName(rs.getString("countryCode"));
				msgPerCountryChart.setValue(rs.getString("number_of_messages"));
				return msgPerCountryChart;
			}
		});
		
		return new ArrayList<ChartRecord>(records);
	}
	
	@Override
	public List<ChartRecord> getMessagesCountPerCurrency(Date dateFrom, Date dateTo, long loggedUserGroupID) {
		List<ChartRecord> records = jdbcTemplate.query(
				getMessagesPerCurrencyQuery(dateFrom, dateTo, loggedUserGroupID), new RowMapper<ChartRecord>() {

					@Override
					public ChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						ChartRecord msgPerCountryChart = new ChartRecord();
						msgPerCountryChart.setName(rs.getString("currency"));
						msgPerCountryChart.setValue(rs.getString("number_of_messages"));
						return msgPerCountryChart;
					}
				});

		return new ArrayList<ChartRecord>(records);
	}
	
	@Override
	public List<ChartRecord> getAmountPerCategory(Date dateFrom, Date dateTo, String currency, long loggedUserGroupID) {
		List<ChartRecord> records = jdbcTemplate.query(
				getAmountPerCategoryQuery(dateFrom, dateTo, currency, loggedUserGroupID), new RowMapper<ChartRecord>() {

					@Override
					public ChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						ChartRecord msgPerCountryChart = new ChartRecord();
						msgPerCountryChart.setName(String.format("%s Cat" , rs.getString("message_category")));
						msgPerCountryChart.setValue(rs.getString("amount"));
						return msgPerCountryChart;
					}
				});

		return new ArrayList<ChartRecord>(records);
	}

	private String getAmountPerCategoryQuery(Date dateFrom, Date dateTo, String currency, long loggedUserGroupID) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("select mesg.x_category as message_category, sum(x_fin_amount) as amount");
		queryString.append(" from rmesg mesg, rAppe, SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP");
		queryString.append(" where ");		
		queryString.append(getQueryDefaultCondition(loggedUserGroupID));
		queryString.append(" AND ");
		queryString.append(getFromDateToDateCondition(dateFrom, dateTo, "mesg", "MESG_CREA_DATE_TIME"));
		queryString.append(" and x_fin_ccy ='");
		queryString.append(currency);
		queryString.append("'");
		queryString.append(" and x_fin_amount <> 0");
		queryString.append(" group by mesg.x_category");
		queryString.append(" order by mesg.x_category");
		return queryString.toString();
	}

	private StringBuilder getQueryDefaultCondition(long loggedUserGroupID) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" SBICUSERGROUP.GROUPID = " + loggedUserGroupID );
		queryString.append(" AND SMSGUSERGROUP.GROUPID = " + loggedUserGroupID );
		queryString.append(" AND SUNITUSERGROUP.GROUPID = " + loggedUserGroupID );
		queryString.append(" AND (X_OWN_LT = BICCODE or X_OWN_LT ='XXXXXXXX')");
		queryString.append(" AND X_CATEGORY = CATEGORY");
		queryString.append(" AND X_INST0_UNIT_NAME = UNIT");
		queryString.append(" AND rAppe.aid = mesg.aid");
		queryString.append(" AND rAppe.appe_s_umidh = mesg.mesg_s_umidh");
		queryString.append(" AND rAppe.appe_s_umidl = mesg.mesg_s_umidl");
		queryString.append(" AND x_category <> '0'");
		queryString.append(" AND x_category <> 'O'");
		queryString.append(" AND ( ");
		queryString.append("(appe_type = 'APPE_RECEPTION' AND appe_rcv_delivery_status = 'RE_UACKED')");
		queryString.append("or");
		queryString.append("(appe_type = 'APPE_EMISSION'  AND appe_network_delivery_status = 'DLV_ACKED'  AND appe_inst_num = 0)");
		queryString.append(")");
		queryString.append(" AND appe_iapp_name = 'SWIFT'");
		queryString.append(" AND x_appe_last = 1 ");
		
		return queryString;
	}

	private String getMessagesPerCountryQuery(Date dateFrom, Date dateTo, long loggedUserGroupID) {
		StringBuilder queryString = new StringBuilder();
		String countryCodeExpr=getDbPortabilityHandler().getSubStringFunctionName()+"( mesg.MESG_SENDER_X1,5,2) ";
		queryString.append("select "+countryCodeExpr+" as countryCode, count(MESG_SENDER_X1) as number_of_messages");
		queryString.append(" from rmesg mesg, rAppe, SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP ");		
		queryString.append(" where ");		
		queryString.append(getQueryDefaultCondition(loggedUserGroupID));
		queryString.append(" AND ");
		queryString.append(getFromDateToDateCondition(dateFrom, dateTo, "mesg", "MESG_CREA_DATE_TIME"));
		queryString.append(" group by "+countryCodeExpr);
		return queryString.toString();
	}
	
	private String getMessagesPerCurrencyQuery(Date dateFrom, Date dateTo, long loggedUserGroupID) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("select mesg.x_fin_ccy as currency, count(x_fin_ccy) as number_of_messages");
		queryString.append(" from rmesg mesg, rAppe, SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP");
		queryString.append(" where x_fin_ccy is not null ");
		queryString.append(" AND ");
		queryString.append(getQueryDefaultCondition(loggedUserGroupID));
		queryString.append(" AND ");		
		queryString.append(getFromDateToDateCondition(dateFrom, dateTo, "mesg", "MESG_CREA_DATE_TIME"));
		queryString.append(" group by mesg.x_fin_ccy");
		return queryString.toString();
	}

	private String getFromDateToDateCondition(Date dateFrom, Date dateTo, String aliasName, String dateColumnName) {		
		return getDbPortabilityHandler().getFromDateToDateCondition(dateFrom, dateTo , dateColumnName);
	}

	@Override
	public List<MultiSeriesChartRecord> getMessageCountPerCategory(Date dateFrom,	Date dateTo, long loggedUserGroupID) {
		String messageCountPerCategoryQuery = getMessageCountPerCategoryQuery(dateFrom, dateTo, loggedUserGroupID);
		List<MultiSeriesChartRecord> records = jdbcTemplate.query(
				messageCountPerCategoryQuery, new RowMapper<MultiSeriesChartRecord>() {

					@Override
					public MultiSeriesChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						MultiSeriesChartRecord chartRecord = new MultiSeriesChartRecord();
						chartRecord.setChartCategoryName(rs.getDate("value_date").toString());
						chartRecord.getChartRecord().setName(rs.getString("mesg_category"));
						chartRecord.getChartRecord().setValue(rs.getString("number_of_messages"));
						return chartRecord;
					}
				});
		
		return new ArrayList<MultiSeriesChartRecord>(records);
	}

	private String getMessageCountPerCategoryQuery(Date dateFrom, Date dateTo, long loggedUserGroupID) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("select MESG.X_FIN_VALUE_DATE as value_date, mesg.x_category as mesg_category, count(mesg.x_category) as number_of_messages");
		queryString.append(" from rmesg mesg, rAppe, SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP");
		queryString.append(" where ");
		queryString.append(getQueryDefaultCondition(loggedUserGroupID));
		queryString.append(" AND ");
		queryString.append(getFromDateToDateCondition(dateFrom, dateTo, "mesg", "X_FIN_VALUE_DATE"));
		queryString.append(" and MESG.X_FIN_VALUE_DATE is not null");
		queryString.append(" group by MESG.X_FIN_VALUE_DATE, mesg.x_category");
		queryString.append(" order by MESG.X_FIN_VALUE_DATE");
		
		return queryString.toString();
	}

	@Override
	public List<MultiSeriesChartRecord> getSentReceivedMessagesCountPerLogicalTerminal(Date dateFrom, Date dateTo, long loggedUserGroupID) {
		List<MultiSeriesChartRecord> records = jdbcTemplate.query(
				getSentReceivedMessagesCountPerLogicalTerminalQuery(dateFrom, dateTo, loggedUserGroupID), new RowMapper<MultiSeriesChartRecord>() {

					@Override
					public MultiSeriesChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						MultiSeriesChartRecord chartRecord = new MultiSeriesChartRecord();
						chartRecord.setChartCategoryName(rs.getString("logical_terminal").toString());
						chartRecord.getChartRecord().setName(rs.getString("message_format"));
						chartRecord.getChartRecord().setValue(rs.getString("number_of_messages"));
						return chartRecord;
					}
				});
		
		return new ArrayList<MultiSeriesChartRecord>(records);
	}
	
	private String getSentReceivedMessagesCountPerLogicalTerminalQuery(Date dateFrom, Date dateTo, long loggedUserGroupID) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("SELECT ");
		queryString.append(getDbPortabilityHandler().getSubStringFunctionName());
		queryString.append("(rAppe.appe_session_holder, 1, 9) AS logical_terminal, mesg_sub_format as message_format,");
		queryString.append(" count(mesg_sub_format) as number_of_messages");
		queryString.append(" FROM rMesg mesg, rAppe, SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP");
		queryString.append(" WHERE");
		queryString.append(getQueryDefaultCondition(loggedUserGroupID));
		queryString.append(" AND ");
		queryString.append(getFromDateToDateCondition(dateFrom, dateTo, "mesg", "MESG_CREA_DATE_TIME"));
		queryString.append(" group by ");
		queryString.append(getDbPortabilityHandler().getSubStringFunctionName());
		queryString.append( "(rAppe.appe_session_holder, 1, 9), mesg_sub_format");
		queryString.append(" order by ");
		queryString.append(getDbPortabilityHandler().getSubStringFunctionName()); 
		queryString.append("(rAppe.appe_session_holder, 1, 9)");	
		
		return queryString.toString();
	}

	/**
	 * Gets the notifications by the given table name
	 * @param dateFrom
	 * @param dateTo
	 * @param tableName
	 * @return
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	private List<ChartRecord> getNotifications(Date dateFrom, Date dateTo,final String tableName) {
		String nemberOfReceivedNotificationsQuery = getNemberOfReceivedNotificationsQuery(dateFrom, dateTo, tableName);
		List<ChartRecord> records = jdbcTemplate.query(
				nemberOfReceivedNotificationsQuery, new RowMapper<ChartRecord>() {

					@Override
					public ChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						ChartRecord msgPerCountryChart = new ChartRecord();
						
						if(tableName.equalsIgnoreCase("WDUSERREQUESTRESULT") || tableName.equalsIgnoreCase("WDEVENTREQUESTRESULT")){
							msgPerCountryChart.setName(rs.getString("insert_time"));
						}else{
							msgPerCountryChart.setName(rs.getString("update_time"));
							
						}
						msgPerCountryChart.setValue(rs.getString("count"));
						return msgPerCountryChart;
					}
				});

		return new ArrayList<ChartRecord>(records);
	}

	private String getNemberOfReceivedNotificationsQuery(Date dateFrom, Date dateTo, String tableName) {
	
		StringBuilder queryString = new StringBuilder();
		String fieldValue = "";
		String fieldValue2 = "";
		
		String fieldName ="";
		String fieldName2 ="";
		String condition = "";

		if(tableName.equalsIgnoreCase("WDUSERREQUESTRESULT") || tableName.equalsIgnoreCase("WDEVENTREQUESTRESULT")){
			fieldName = "INSERT_TIME";
			fieldName2 = "INSERT_TIME1";
		}else{
			fieldName = "UPDATE_TIME";
			fieldName2 = "UPDATE_TIME1";
			
		}

		queryString.append("select ");
		fieldValue = getDbPortabilityHandler().getDayMonthFromDate(fieldName);
		fieldValue2 = getDbPortabilityHandler().getDayMonthYearFromDate(fieldName);
		condition = getFromDateToDateCondition(dateFrom, dateTo, tableName, fieldName);
		
		queryString.append(fieldValue);
		queryString.append(" as ");
		queryString.append(fieldName);
		
		queryString.append(", ");
		queryString.append(fieldValue2);
		queryString.append(" as ");
		queryString.append(fieldName2);
		
		queryString.append(" , count(1) as count from ");
		queryString.append(tableName);
		queryString.append(" where ");
			
		queryString.append(condition);
		queryString.append(" group by ");
		queryString.append(fieldValue);
		queryString.append(", ");
		queryString.append(fieldValue2);
		queryString.append(" order by ");
		queryString.append(fieldName2);
		queryString.append(" asc");
			
			
		
		return queryString.toString();
	}

	@Override
	public List<ChartRecord> getEventNotifications(Date dateFrom, Date dateTo) {
		return getNotifications(dateFrom, dateTo, "WDEVENTREQUESTRESULT");
	}

	@Override
	public List<ChartRecord> getMessageNotifications(Date dateFrom, Date dateTo) {
		return getNotifications(dateFrom, dateTo, "WDUSERREQUESTRESULT");
	}

	@Override
	public List<ChartRecord> getLoaderNewEvents(Date insertionDate, String aid) {
		List<ChartRecord> records = jdbcTemplate.query(
				getLoaderNewEventsQuery(insertionDate, aid), new RowMapper<ChartRecord>() {

					@Override
					public ChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						
						ChartRecord chartRecord = new ChartRecord();
						chartRecord.setName(rs.getString("insertion_hour"));
						chartRecord.setValue(rs.getString("new_events_count"));						
						
						return chartRecord;
					}
				});

		return new ArrayList<ChartRecord>(records);
	}
	
	@Override
	public List<ChartRecord> getLoaderNewMessages(Date insertionDate, String aid) {
		String loaderNewMessagesQuery = getLoaderNewMessagesQuery(insertionDate, aid);
		List<ChartRecord> records = jdbcTemplate.query(
				loaderNewMessagesQuery, new RowMapper<ChartRecord>() {

					@Override
					public ChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						
						ChartRecord chartRecord = new ChartRecord();
						chartRecord.setName(rs.getString("insertion_hour"));
						chartRecord.setValue(rs.getString("new_messages_count"));						
						
						return chartRecord;
					}
				});

		return new ArrayList<ChartRecord>(records);
	}
	
	@Override
	public List<ChartRecord> getLoaderUpdatedMessages(Date insertionDate, String aid) {
		List<ChartRecord> records = jdbcTemplate.query(
				getLoaderUpdatedMessagesQuery(insertionDate, aid), new RowMapper<ChartRecord>() {

					@Override
					public ChartRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						
						ChartRecord chartRecord = new ChartRecord();
						chartRecord.setName(rs.getString("insertion_hour"));
						chartRecord.setValue(rs.getString("updated_messages_count"));						
						
						return chartRecord;
					}
				});

		return new ArrayList<ChartRecord>(records);
	}
	
	
	private String getLoaderQuery(Date insertionDate, String aid, String column, String dateColumn) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("select ");
		queryString.append(getDbPortabilityHandler().getHourFromDate(dateColumn));
		queryString.append(" insertion_hour,");		
		queryString.append(column);
		queryString.append(" from LDUPDATESTATISTICS ");
		queryString.append(" WHERE");
		queryString.append(" aid = '");
		queryString.append(aid);
		queryString.append("'");
		queryString.append(" and ");
		queryString.append(getDbPortabilityHandler().getOneDayCondition(insertionDate, dateColumn));
		
		queryString.append(" group by aid, ");
		queryString.append(getDbPortabilityHandler().getHourFromDate(dateColumn));
		queryString.append(" order by ");
		queryString.append(getDbPortabilityHandler().getHourFromDate(dateColumn));
		
		return queryString.toString();
	}
	
	private String getLoaderNewEventsQuery(Date insertionDate, String aid) {		
		return getLoaderQuery(insertionDate, aid, " sum(jrnl_msg_count) new_events_count ","UPDATE_TIME");
	}
	
	private String getLoaderNewMessagesQuery(Date insertionDate, String aid) {		
		return getLoaderQuery(insertionDate, aid, " sum(new_msg_count) as new_messages_count ","UPDATE_TIME");
	}
	
	private String getLoaderUpdatedMessagesQuery(Date insertionDate, String aid) {		
		return getLoaderQuery(insertionDate, aid, "  sum(update_msg_count) updated_messages_count ","UPDATE_TIME");
	}
}