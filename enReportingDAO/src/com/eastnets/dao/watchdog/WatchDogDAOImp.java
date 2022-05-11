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

package com.eastnets.dao.watchdog;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
	import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.dao.watchdog.procedure.WDDeleteNotificationProcedure;
import com.eastnets.dao.watchdog.procedure.WDGetLastIDs21;
import com.eastnets.domain.watchdog.Annotaion;
import com.eastnets.domain.watchdog.EventRequest;
import com.eastnets.domain.watchdog.LastIDs;
import com.eastnets.domain.watchdog.MessageNotification;

/**
 * WatchDog DAO Implementation
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public abstract class WatchDogDAOImp extends DAOBaseImp implements WatchDogDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2794161376390484375L;

	private WDDeleteNotificationProcedure wdDeleteNotificationProcedure;
	private WDGetLastIDs21 wdGetLastIDs21;

	private Map<String, Integer> currenciesMap;
	private ApplicationFeatures applicationFeatures;

	public void init() {
		currenciesMap = getCurrenciesISO();
	}

	@Override
	public void deleteMessageRequest(Long id) {
		jdbcTemplate.execute(String.format("delete wdUserSearchParameter where RequestId = %d", id));
	}

	@Override
	public void deleteEventRequest(Long id) {
		jdbcTemplate.execute(String.format("delete wdEventSearchParameter where RequestId = %d", id));
	}

	@Override
	public MessageNotification getMessageNotification(Long sysId, boolean sideAdminUser, Long loggedInUserGroupId) {
		String targetSelectQuery = getWDUserRequestView(loggedInUserGroupId, sideAdminUser);
		String queryString = String.format(
				"select DISTINCT aid,UMIDL,UMIDH,MsgType,id,appe_date_time,Description,SubFormat,Sender,Receiver,MsgType,TransactionReference,CCy,Amount, Identifier, ValueDate,Partner,SessionNbr,SequenceNbr,InsertTime,username ,CREATIONDATE from %s where Id = %d",
				targetSelectQuery, sysId);
		List<MessageNotification> requestsList = jdbcTemplate.query(queryString, new RowMapper<MessageNotification>() {
			public MessageNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNotification request = new MessageNotification();
				request.setAid(rs.getInt("aid"));
				request.setUmidl(rs.getLong("UMIDL"));
				request.setUmidh(rs.getLong("UMIDH"));
				request.setMsgtype(rs.getString("MsgType"));
				request.setId(rs.getLong("id"));
				request.setReceptionDate(rs.getTimestamp("appe_date_time"));
				request.setDescription(rs.getString("Description"));
				request.setSubFormat(rs.getString("SubFormat"));
				request.setSender(rs.getString("Sender"));
				request.setReceiver(rs.getString("Receiver"));
				request.setReference(rs.getString("TransactionReference"));
				request.setCurrency(rs.getString("CCy"));
				request.setAmount(rs.getString("Amount"));
				request.setIdentifier(rs.getString("Identifier"));
				request.setValueDate(rs.getTimestamp("ValueDate"));
				request.setLogicalTerminal(rs.getString("Partner"));
				request.setSessionNbr(rs.getLong("sessionNbr"));
				request.setSequenceNbr(rs.getLong("sequenceNbr"));
				request.setInsertTime(rs.getTimestamp("insertTime"));
				request.setOwner(rs.getString("username"));
				request.setNotificationType(Constants.WATCHDOGE_FILTER_MESSAGE_NOTIFICATIONS);
				request.setMesgCreaDateTime(rs.getTimestamp("CREATIONDATE"));
				if (rs.getString("Amount") != null && !rs.getString("Amount").isEmpty()) {
					request.setAmount(formatAmount(new BigDecimal(rs.getString("Amount").trim()), rs.getString("CCy")).toString());
				}

				return request;
			}
		});

		if (requestsList == null || requestsList.size() <= 0) {
			return null;
		}

		return requestsList.get(0);
	}

	@Override
	public MessageNotification getCalculatedDupplicateNotification(Long sysId, Long loggedInUserGroupId) {
		String queryString = String.format("select SYSID, INSERTTIME, DUPLICATE_TYPE, DUPLICATE_STRING, AID, UMIDL, UMIDH, CREATIONDATE" + ", SUFFIX, UMID, CCY, AMOUNT, VALUEDATE, TRANSACTIONREFERENCE, DUPAID, DUPUMIDL, DUPUMIDH, DUPCREATIONDATE"
				+ ", DUPCCY, DUPAMOUNT, DUPVALUEDATE, RECEIVER, SENDER, SUBFORMAT, MSGTYPE, DUPSUFFIX, DATETIME, SESSIONNBR" + ", SEQUENCENBR, DUPDATETIME, DUPSESSIONNBR, DUPSEQUENCENBR, PROCESSED from "
				+ getWDCalculatedDupView21(loggedInUserGroupId) + "  where SysID = %d", sysId);
		List<MessageNotification> requestsList = jdbcTemplate.query(queryString, new RowMapper<MessageNotification>() {
			public MessageNotification mapRow(ResultSet rs, int rowNum) throws SQLException {

				MessageNotification request = new MessageNotification();

				request.setId(rs.getLong("SYSID"));
				request.setInsertTime(rs.getTimestamp("INSERTTIME"));
				request.setAid(rs.getInt("AID"));
				request.setUmidl(rs.getLong("UMIDL"));
				request.setUmidh(rs.getLong("UMIDH"));
				request.setAmount(rs.getString("AMOUNT"));
				request.setValueDate(rs.getTimestamp("VALUEDATE"));
				request.setReceiver(rs.getString("RECEIVER"));
				request.setSender(rs.getString("SENDER"));
				request.setSubFormat(rs.getString("SUBFORMAT"));
				request.setMsgtype(rs.getString("MSGTYPE"));
				request.setSessionNbr(rs.getLong("SESSIONNBR"));
				request.setSequenceNbr(rs.getLong("SEQUENCENBR"));
				request.setDescription("Calculated Duplicate");
				request.setReference(rs.getString("TRANSACTIONREFERENCE"));
				request.setNotificationType(Constants.WATCHDOGE_FILTER_CALCULATE_DUPLICATES);
				request.setMesgCreaDateTime(rs.getTimestamp("CREATIONDATE"));

				if (rs.getString("Amount") != null && !rs.getString("Amount").isEmpty()) {
					request.setAmount(formatAmount(new BigDecimal(rs.getString("Amount").trim()), rs.getString("CCy")).toString());
				}

				return request;
			}
		});

		if (requestsList == null || requestsList.size() <= 0) {
			return null;
		}

		return requestsList.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getCurrenciesISO() {

		return jdbcTemplate.query("select DISTINCT CURRENCYCODE , NUMBEROFDIGITS from CU ", new ResultSetExtractor<Map>() {
			@Override
			public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				HashMap<String, Integer> results = new HashMap<>();
				while (rs.next()) {
					results.put(rs.getString("CURRENCYCODE"), rs.getInt("NUMBEROFDIGITS"));
				}
				return results;
			}
		});

	}

	@Override
	public MessageNotification getPossibleDupplicateNotification(Long sysId, Long loggedInUserGroupId) {
		String queryString = String.format("select SYSID, AID, UMIDL, UMIDH, USERISSUEDPDE, POSSIBLEPDE" + ", POSSIBLEPDR, RECEIVER, SENDER, MSGTYPE, SUBFORMAT, TRANSACTIONREFERENCE" + ", INSERTTIME, PROCESSED, CREATIONDATE, SESSIONNBR, SEQNBR"
				+ " from %s " + " where SysID = %d", getWDPossibleDupView(loggedInUserGroupId), sysId);
		List<MessageNotification> requestsList = jdbcTemplate.query(queryString, new RowMapper<MessageNotification>() {
			public MessageNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNotification request = new MessageNotification();
				request.setId(rs.getLong("SYSID"));
				request.setAid(rs.getInt("AID"));
				request.setUmidl(rs.getLong("UMIDL"));
				request.setUmidh(rs.getLong("UMIDH"));
				request.setReceiver(rs.getString("RECEIVER"));
				request.setSender(rs.getString("SENDER"));
				request.setMsgtype(rs.getString("MSGTYPE"));
				request.setSubFormat(rs.getString("SUBFORMAT"));
				request.setReference(rs.getString("TRANSACTIONREFERENCE"));
				request.setInsertTime(rs.getTimestamp("INSERTTIME"));
				request.setSessionNbr(rs.getLong("SESSIONNBR"));
				request.setSequenceNbr(rs.getLong("SEQNBR"));
				request.setDescription("Possible Duplicate");
				request.setNotificationType(Constants.WATCHDOGE_FILTER_POSSIBLE_DUPLICATES);
				request.setMesgCreaDateTime(rs.getTimestamp("CREATIONDATE"));

				return request;
			}
		});

		if (requestsList == null || requestsList.size() <= 0) {
			return null;
		}

		return requestsList.get(0);
	}

	@Override
	public MessageNotification getIsnGapNotification(Long sysId, Long loggedInUserGroupId) {
		String queryString = String.format("select SYSID, LT, LASTSN, EXPECTEDSN, GAPTIME, INSERTTIME, ISISN, PROCESSED from %s where isIsn = 1 and SysID = %d", getSWDSnGaps(loggedInUserGroupId), sysId);
		List<MessageNotification> requestsList = jdbcTemplate.query(queryString, new RowMapper<MessageNotification>() {
			public MessageNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNotification request = new MessageNotification();

				request.setId(rs.getLong("SYSID"));
				request.setLogicalTerminal(rs.getString("LT"));
				request.setInsertTime(rs.getTimestamp("INSERTTIME"));
				request.setDescription("ISN Gap");
				request.setNotificationType(Constants.WATCHDOGE_FILTER_ISN_GAPS);

				return request;
			}
		});

		if (requestsList == null || requestsList.size() <= 0) {
			return null;
		}

		return requestsList.get(0);
	}

	@Override
	public MessageNotification getOsnGapNotification(Long sysId, Long loggedInUserGroupId) {
		String queryString = String.format("select SYSID, LT, LASTSN, EXPECTEDSN, GAPTIME, INSERTTIME, ISISN, PROCESSED from %s where isIsn = 0 and SysID = %d", getSWDSnGaps(loggedInUserGroupId), sysId);
		List<MessageNotification> requestsList = jdbcTemplate.query(queryString, new RowMapper<MessageNotification>() {
			public MessageNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNotification request = new MessageNotification();

				request.setId(rs.getLong("SYSID"));
				request.setLogicalTerminal(rs.getString("LT"));
				request.setInsertTime(rs.getTimestamp("INSERTTIME"));
				request.setDescription("OSN Gap");
				request.setNotificationType(Constants.WATCHDOGE_FILTER_OSN_GAPS);

				return request;
			}
		});

		if (requestsList == null || requestsList.size() <= 0) {
			return null;
		}

		return requestsList.get(0);
	}

	@Override
	public MessageNotification getNakedNotification(Long sysId, Long loggedInUserGroupId) {
		String queryString = String.format("select AID, UMIDL, UMIDH, APPE_INST_NUM, APPE_DATE_TIME" + ", APPE_SEQ_NBR, BICCODE, SESSIONNBR, SEQUENCENBR, ISNACK, ERRORTYPE" + ", ERRORCODE, ERRORLINE, MURREF, CREATIONDATE, RECEIVER, SENDER, SUBFORMAT"
				+ ", MSGTYPE, TRANSACTIONREFERENCE, CREATOR, INSERTTIME, SYSID, PROCESSED" + " from %s where SysID = %d", getWDNACKView(loggedInUserGroupId), sysId);
		List<MessageNotification> requestsList = jdbcTemplate.query(queryString, new RowMapper<MessageNotification>() {
			public MessageNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNotification request = new MessageNotification();

				request.setId(rs.getLong("SYSID"));
				request.setAid(rs.getInt("AID"));
				request.setUmidl(rs.getLong("UMIDL"));
				request.setUmidh(rs.getLong("UMIDH"));
				request.setSessionNbr(rs.getLong("SESSIONNBR"));
				request.setSequenceNbr(rs.getLong("SEQUENCENBR"));
				request.setReceiver(rs.getString("RECEIVER"));
				request.setSender(rs.getString("SENDER"));
				request.setSubFormat(rs.getString("SUBFORMAT"));
				request.setMsgtype(rs.getString("MSGTYPE"));
				request.setReference(rs.getString("TRANSACTIONREFERENCE"));
				request.setInsertTime(rs.getTimestamp("INSERTTIME"));
				request.setNotificationType(Constants.WATCHDOGE_FILTER_NAKED_MESSAGES);
				request.setMesgCreaDateTime(rs.getTimestamp("CREATIONDATE"));

				request.setDescription("NAKed message");
				return request;
			}
		});

		if (requestsList == null || requestsList.size() <= 0) {
			return null;
		}

		return requestsList.get(0);
	}

	@Override
	public List<EventRequest> getEventRequests(boolean sideAdminUser, String loggedInUser) {
		List<Object> parameters = new ArrayList<>();

		String queryString = "select RequestID, Description,jrnl_comp_name, jrnl_event_num,expirationdate,email from wdEventSearchParameter ";
		if (!sideAdminUser) {

			parameters.add(getDBSafeString(loggedInUser));

			// Bug 34739:TDR 3.1: viewing Watchdog Event Notification Requests

			queryString = queryString + " where upper(UserName) in (select upper(username) from suser where (username) = ?)";
		}

		Collection<EventRequest> requests = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<EventRequest>() {
			public EventRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
				EventRequest param = new EventRequest();
				param.setId((rs.getLong("RequestID")));
				param.setDescription(rs.getString("Description"));
				param.setComponentName(rs.getString("jrnl_comp_name"));
				param.setEventNbr(rs.getString("jrnl_event_num"));
				param.setExpirationDate(rs.getTimestamp("expirationdate"));
				param.setEmail(rs.getString("email"));
				return param;
			}
		});
		ArrayList<EventRequest> requestsList = new ArrayList<EventRequest>();
		requestsList.addAll(requests);
		return requestsList;
	}

	public WDDeleteNotificationProcedure getWdDeleteNotificationProcedure() {
		return wdDeleteNotificationProcedure;
	}

	public void setWdDeleteNotificationProcedure(WDDeleteNotificationProcedure wdDeleteNotificationProcedure) {
		this.wdDeleteNotificationProcedure = wdDeleteNotificationProcedure;
	}

	@Override
	public void deleteEventNotification(Long id) {
		wdDeleteNotificationProcedure.execute(Constants.WATCHDOGE_FILTER_EVENT, id);
	}

	@Override
	public void deleteMessageNotification(int notificationType, Long id) {
		wdDeleteNotificationProcedure.execute(notificationType, id);
	}

	@Override
	public LastIDs getLastIDs(Long iNbDayHistory) {
		return wdGetLastIDs21.execute(iNbDayHistory);
	}

	public WDGetLastIDs21 getWdGetLastIDs21() {
		return wdGetLastIDs21;
	}

	public void setWdGetLastIDs21(WDGetLastIDs21 wdGetLastIDs21) {
		this.wdGetLastIDs21 = wdGetLastIDs21;
	}

	@Override
	public List<Annotaion> getNotificationAnnotaions(Long sysId, int requstType) {
		String queryString = String.format("select ID, SYSID, REQUEST_TYPE, PROCESSED, PROCESSED_BY, PROCESSED_AT, USER_COMMENT from wdSysRequest where sysid = %d and REQUEST_TYPE = %d", sysId, requstType);
		List<Annotaion> annotaionsList = jdbcTemplate.query(queryString, new RowMapper<Annotaion>() {
			public Annotaion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Annotaion annotaion = new Annotaion();

				annotaion.setId(rs.getLong("ID"));
				annotaion.setSysID(rs.getLong("SYSID"));
				annotaion.setRequestType(rs.getInt("REQUEST_TYPE"));
				annotaion.setProcessed(rs.getLong("PROCESSED"));

				annotaion.setProcessedBy(rs.getString("PROCESSED_BY"));
				annotaion.setProcessedAt(rs.getTimestamp("PROCESSED_AT"));
				annotaion.setUserComment(rs.getString("USER_COMMENT"));

				return annotaion;
			}
		});

		return annotaionsList;
	}

	@Override
	public Long getMessageNotificationsCount(Date fromDate, boolean sideAdminUser, Long loggedInUserGroupId) {

		String targetSelectQuery = getWDUserRequestView(loggedInUserGroupId, sideAdminUser);
		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select count(1) as notificationsCount from %s where %s", targetSelectQuery, formatDateTime);
		List<Long> records = jdbcTemplate.query(queryString, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("notificationsCount");
			}
		});

		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	@Override
	public Long getCalculatedDupplicateNotificationsCount(Date fromDate, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select count(1) as notificationsCount from " + getWDCalculatedDupView21(loggedInUserGroupId) + " where %s", formatDateTime);
		List<Long> records = jdbcTemplate.query(queryString, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("notificationsCount");
			}
		});

		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	@Override
	public Long getPossibleDupplicateNotificationsCount(Date fromDate, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select count(1) as notificationsCount from %s where %s", getWDPossibleDupView(loggedInUserGroupId), formatDateTime);
		List<Long> records = jdbcTemplate.query(queryString, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("notificationsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	@Override
	public Long getIsnGapNotificationsCount(Date fromDate, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select count(1) as notificationsCount from %s where isIsn = 1 and %s", getSWDSnGaps(loggedInUserGroupId), formatDateTime);
		List<Long> records = jdbcTemplate.query(queryString, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("notificationsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	@Override
	public Long getOsnGapNotificationsCount(Date fromDate, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select count(1) as notificationsCount from %s where isIsn = 0 and %s ", getSWDSnGaps(loggedInUserGroupId), formatDateTime);
		List<Long> records = jdbcTemplate.query(queryString, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("notificationsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	@Override
	public Long getNakedNotificationsCount(Date fromDate, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select count(1) as notificationsCount from %s where %s", getWDNACKView(loggedInUserGroupId), formatDateTime);
		List<Long> records = jdbcTemplate.query(queryString, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("notificationsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	// Bug 30277:WatchDog - event notification created showing incorrect numbers of Rows for searches (Defect #157)
	@Override
	public Long getEventNotificationsCount(Date fromDate, String loggedInUsername, boolean sideAdminUser) {

		// prepare where condition for insert_time
		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "INSERT_TIME");
		String queryString = String.format("select count(1) as notificationsCount from %s where %s ", getWDEventRequestView(loggedInUsername, sideAdminUser), formatDateTime);
		List<Long> records = jdbcTemplate.query(queryString, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("notificationsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	String getWDUserRequestView(Long loggedInUserGroupId, boolean isAdminUser) {
		String query = "( SELECT " + "w.ID, w.aid, " + "w.appe_s_umidl AS umidl, " + "w.appe_s_umidh AS umidh, " + "w.appe_inst_num, " + "w.appe_date_time, " + "w.appe_seq_nbr, " + "w.request_id AS requestid, " + "w.description AS description, "
				+ "w.mesg_crea_oper_nickname AS OPERATOR, " + "w.mesg_crea_date_time AS creationdate, " + getDbPortabilityHandler().getNullValue("w.mesg_receiver_swift_address", "' '") + " AS receiver, "
				+ getDbPortabilityHandler().getNullValue("w.mesg_sender_swift_address", "' '") + " AS sender, " + "w.mesg_sub_format AS subformat, " + "w.mesg_type AS msgtype, " + "w.mesg_trn_ref AS transactionreference, " + "w.x_fin_ccy AS ccy, "
				+ "w.x_fin_amount AS amount, " + "w.x_fin_value_date AS valuedate, " + "w.identifier as identifier, " + "w.appe_session_holder AS partner, " + "w.appe_crea_mpfn_name AS creator, " + "w.appe_session_nbr AS sessionnbr, "
				+ "w.appe_sequence_nbr AS sequencenbr, " + "w.insert_time AS inserttime, " + "w.username, w.usergroup, ";

		if (isAdminUser) {
			query += getDbPortabilityHandler().getNullValue("wdSysRequest.processed", "0") + " AS processed, ";
		} else {
			query += "w.processed AS processed, ";
		}

		query += "rmesg.mesg_crea_date_time " + "FROM " + "wduserrequestresult w left outer join wdSysRequest" + " on " + " 	 wdSysRequest.sysid = w.id " + "  AND wdSysRequest.request_type = w.request_id "
				+ ", rmesg, suser u, SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP " + "WHERE " + "	    SBICUSERGROUP.GROUPID = " + loggedInUserGroupId + " " + " AND SMSGUSERGROUP.GROUPID = " + loggedInUserGroupId + " "
				+ " AND SUNITUSERGROUP.GROUPID = " + loggedInUserGroupId + " " + " AND (X_OWN_LT = BICCODE or X_OWN_LT ='XXXXXXXX' ) " + " AND X_CATEGORY = CATEGORY " + " AND X_INST0_UNIT_NAME = UNIT " + " AND rmesg.aid = w.aid "
				+ " AND rmesg.mesg_s_umidl = w.appe_s_umidl " + " AND rmesg.mesg_s_umidh = w.appe_s_umidh " + (getDbPortabilityHandler().isPartitionedDB() == true ? " AND rMesg.mesg_crea_date_time = w.X_CREA_DATE_TIME " : "")
				+ " AND UPPER (u.username)	= UPPER (w.Username) ";
		if (!isAdminUser) {
			query += " AND UserGroup = 0 " + "UNION " + "SELECT " + "w.ID, w.aid, " + "w.appe_s_umidl AS umidl, " + "w.appe_s_umidh AS umidh, " + "w.appe_inst_num, " + "w.appe_date_time, " + "w.appe_seq_nbr, " + "w.request_id AS requestid, "
					+ "w.description AS description, " + "w.mesg_crea_oper_nickname AS OPERATOR, " + "w.mesg_crea_date_time AS creationdate, " + getDbPortabilityHandler().getNullValue("w.mesg_receiver_swift_address", "' '") + " AS receiver, "
					+ getDbPortabilityHandler().getNullValue("w.mesg_sender_swift_address", "' '") + " AS sender, " + "w.mesg_sub_format AS subformat, " + "w.mesg_type AS msgtype, " + "w.mesg_trn_ref AS transactionreference, "
					+ "w.x_fin_ccy AS ccy, " + "w.x_fin_amount AS amount, " + "w.x_fin_value_date AS valuedate, " + "w.identifier as identifier, " + "w.appe_session_holder AS partner, " + "w.appe_crea_mpfn_name AS creator, "
					+ "w.appe_session_nbr AS sessionnbr, " + "w.appe_sequence_nbr AS sequencenbr, " + "w.insert_time AS inserttime, " + "w.username, w.usergroup, " + "w.processed AS processed, " + "rmesg.mesg_crea_date_time " + "FROM "
					+ "wduserrequestresult w left outer join wdSysRequest" + " on " + " 	 wdSysRequest.sysid = w.id " + "  AND wdSysRequest.request_type = w.request_id " + ", rmesg, suser u , SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP "
					+ "WHERE " + "	 SBICUSERGROUP.GROUPID = " + loggedInUserGroupId + " " + " AND SMSGUSERGROUP.GROUPID = " + loggedInUserGroupId + " " + " AND SUNITUSERGROUP.GROUPID = " + loggedInUserGroupId + " "
					+ " AND (X_OWN_LT = BICCODE or X_OWN_LT ='XXXXXXXX' ) " + " AND X_CATEGORY = CATEGORY " + " AND X_INST0_UNIT_NAME = UNIT " + " AND rmesg.aid = w.aid " + " AND rmesg.mesg_s_umidl = w.appe_s_umidl "
					+ " AND rmesg.mesg_s_umidh = w.appe_s_umidh " + " AND UPPER (u.username)	= UPPER (w.Username) " + " AND UserGroup = 1 " + " AND " + " ( UPPER(w.USERNAME) in " + " 	("
					+ " 		SELECT upper(UserName) from sUser where GroupId = " + loggedInUserGroupId + " " + " 	) " + " ) ";
		}
		query += " ) wdUserRequestView ";

		return query;
	}

	String getWDCalculatedDupView21(Long loggedInUserGroupId) {
		String query = "( " + " SELECT" + "    w.sysid," + "    w.insert_time as InsertTime," + "    w.duplicate_type as duplicate_type," + "    w.duplicate_string," + "    ref.aid as AID," + "    ref.mesg_s_umidl as UMIDL,"
				+ "    ref.mesg_s_umidh as UMIDH," + "    ref.mesg_crea_date_time as CreationDate," + "    ref.mesg_uumid_suffix as Suffix," + "    ref.mesg_uumid as UMID," + "    ref.x_fin_ccy as Ccy," + "    ref.x_fin_amount as Amount,"
				+ "    ref.x_fin_value_date as ValueDate," + "    ref.mesg_identifier as Identifier," + "    ref.mesg_trn_ref as TransactionReference," + "    ref.aid as DupAID," + "    ref.mesg_s_umidl as DupUMIDL,"
				+ "    ref.mesg_s_umidh as DupUMIDH," + "    ref.mesg_crea_date_time as DupCreationDate," + "    ref.x_fin_ccy as DupCcy," + "    ref.x_fin_amount as DupAmount," + "    ref.x_fin_value_date as DupValueDate,"
				+ "    ref.mesg_receiver_swift_address as Receiver," + "    ref.mesg_sender_swift_address as Sender," + "    ref.mesg_sub_format as SubFormat," + "    ref.mesg_type as MsgType," + "    ref.mesg_uumid_suffix as DupSuffix,"
				+ "    a1.appe_date_time as DateTime," + "    a1.appe_session_nbr as SessionNbr," + "    a1.appe_sequence_nbr as SequenceNbr," + "    a2.appe_date_time as DupDateTime," + "    a2.appe_session_nbr as DupSessionNbr,"
				+ "    a2.appe_sequence_nbr as DupSequenceNbr," + "    " + getDbPortabilityHandler().getNullValue("sys.processed", "0") + " AS processed " + " from wdCalcDupResult w" + " left outer join rMesg ref on" + "    ref.aid = w.dup_aid and"
				+ "    ref.mesg_s_umidl = w.dup_mesg_s_umidl and" + "    ref.mesg_s_umidh = w.dup_mesg_s_umidh"
				+ (getDbPortabilityHandler().getDbType() == 0 && getDbPortabilityHandler().isPartitionedDB() == true ? " AND ref.mesg_crea_date_time = w.mesg_crea_date_time " : "") + " left outer join rAppe a1 on" + "    w.aid = a1.aid and "
				+ "		w.mesg_s_umidl = a1.appe_s_umidl and " + "		w.mesg_s_umidh = a1.appe_s_umidh and" + "    a1.appe_iapp_name = 'SWIFT' and a1.x_appe_last = 1"
				+ (getDbPortabilityHandler().getDbType() == 0 && getDbPortabilityHandler().isPartitionedDB() == true ? " AND a1.X_CREA_DATE_TIME_MESG = w.mesg_crea_date_time " : "") + " left outer join rAppe a2 on" + "    w.dup_aid = a2.aid and "
				+ "		w.dup_mesg_s_umidl = a2.appe_s_umidl and " + "		w.dup_mesg_s_umidh = a2.appe_s_umidh and " + "    a2.appe_iapp_name = 'SWIFT' and a2.x_appe_last = 1"
				+ (getDbPortabilityHandler().getDbType() == 0 && getDbPortabilityHandler().isPartitionedDB() == true ? " AND a2.X_CREA_DATE_TIME_MESG = w.mesg_crea_date_time " : "")
				// Process information
				+ " left outer join wdSysRequest sys on" + "    w.sysid = sys.sysid and sys.request_type = 2" + " , SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP" + " where" + "	SBICUSERGROUP.GROUPID = " + loggedInUserGroupId
				+ " AND SMSGUSERGROUP.GROUPID = " + loggedInUserGroupId + " AND SUNITUSERGROUP.GROUPID = " + loggedInUserGroupId + " AND (X_OWN_LT = BICCODE or X_OWN_LT ='XXXXXXXX' )" + " AND X_CATEGORY = CATEGORY" + " AND X_INST0_UNIT_NAME = UNIT"
				+ " ) wdCalculatedDupView21 ";

		return query;
	}

	String getWDPossibleDupView(Long loggedInUserGroupId) {
		String query = "( " + " SELECT wdPossibleDupResult.Sysid," + "    wdPossibleDupResult.aid as aid," + "    wdPossibleDupResult.appe_s_umidl AS UMIDL," + "    wdPossibleDupResult.appe_s_umidh AS UMIDH,"
				+ "    wdPossibleDupResult.mesg_user_issued_as_pde AS UserIssuedPDE," + "    CASE wdPossibleDupResult.mesg_possible_dup_creation" + "    WHEN 'PDE' THEN 1" + "    WHEN 'PDE_PDR' THEN 1" + "    ELSE 0" + "    END AS PossiblePDE,"
				+ "    CASE wdPossibleDupResult.mesg_possible_dup_creation" + "    WHEN 'PDR' THEN 1" + "    WHEN 'PDE_PDR' THEN 1" + "    ELSE 0" + "    END AS PossiblePDR," + "    rMesg.mesg_receiver_swift_address AS Receiver,"
				+ "    rMesg.mesg_sender_swift_address AS Sender," + "    rMesg.mesg_type AS MsgType," + "    rMesg.mesg_sub_format AS SubFormat," + "    rMesg.mesg_trn_ref AS TransactionReference," + "    rMesg.x_fin_ccy AS ccy, "
				+ "    rMesg.x_fin_amount AS amount, " + "    rMesg.x_fin_value_date AS valuedate, " + "    rMesg.mesg_identifier as identifier, " + "    wdPossibleDupResult.insert_time AS InsertTime," + "    "
				+ getDbPortabilityHandler().getNullValue("wdSysRequest.processed", "0") + " as processed," + "    rappe.Appe_date_time as CreationDate," + "    rappe.Appe_session_nbr as SessionNbr," + "    rappe.Appe_sequence_nbr as SeqNbr"
				+ " FROM wdPossibleDupResult" + " INNER JOIN rappe ON" + "    wdPossibleDupResult.aid = rappe.aid AND" + "    wdPossibleDupResult.appe_s_umidl = rappe.appe_s_umidl AND" + "    wdPossibleDupResult.appe_s_umidh = rappe.appe_s_umidh AND"
				+ "    wdPossibleDupResult.appe_inst_num = rappe.appe_inst_num AND" + "    wdPossibleDupResult.appe_date_time = rappe.appe_date_time AND" + "    wdPossibleDupResult.appe_seq_nbr = rappe.appe_seq_nbr"
				+ (getDbPortabilityHandler().getDbType() == 0 && getDbPortabilityHandler().isPartitionedDB() == true ? " AND wdPossibleDupResult.X_CREA_DATE_TIME_MESG = rappe.X_CREA_DATE_TIME_MESG " : "") + " INNER JOIN rMesg ON"
				+ "    wdPossibleDupResult.aid = rMesg.aid AND" + "    wdPossibleDupResult.appe_s_umidl = rMesg.mesg_s_umidl AND" + "    wdPossibleDupResult.appe_s_umidh = rMesg.mesg_s_umidh"
				+ (getDbPortabilityHandler().getDbType() == 0 && getDbPortabilityHandler().isPartitionedDB() == true ? " AND wdPossibleDupResult.X_CREA_DATE_TIME_MESG = rMesg.mesg_crea_date_time " : "") + " LEFT OUTER JOIN wdSysRequest ON"
				+ "    wdPossibleDupResult.sysid = wdSysRequest.sysid and" + "    wdSysRequest.request_type = 3" + " , SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP" + " where" + "     SBICUSERGROUP.GROUPID = " + loggedInUserGroupId
				+ " AND SMSGUSERGROUP.GROUPID = " + loggedInUserGroupId + " AND SUNITUSERGROUP.GROUPID = " + loggedInUserGroupId + " AND (X_OWN_LT = BICCODE or X_OWN_LT ='XXXXXXXX' )" + " AND X_CATEGORY = CATEGORY" + " AND X_INST0_UNIT_NAME = UNIT"

				+ " ) wdPossibleDupView ";
		return query;
	}

	String getSWDSnGaps(Long loggedInUserGroupId) {
		String query = " ( " + " SELECT" + "     WdSnGap.SYSID," + "     WdSnGap.LT," + "     WdSnGap.lastsn," + "     WdSnGap.expectedsn," + "     WdSnGap.gaptime," + "     WdSnGap.inserttime," + "     WdSnGap.isIsn," + "     WdSnGap.processed"
				+ " FROM WdSnGap, sBICUserGroup" + " where" + "     sBICUserGroup.GroupID = " + loggedInUserGroupId + "  and ( " + getDbPortabilityHandler().getSubStr("lt", 1, 8) + " = sBICUserGroup.BICCode )" + "  or  ( "
				+ getDbPortabilityHandler().getSubStr("lt", 1, 8) + " = 'XXXXXXXX' )" + " ) swdSnGap ";
		return query;
	}

	String getWDNACKView(Long loggedInUserGroupId) {
		String query = " ( " + " SELECT" + "     wdNACKResult.aid as aid," + "     wdNACKResult.appe_s_umidl AS UMIDL," + "     wdNACKResult.appe_s_umidh AS UMIDH," + "     wdNACKResult.appe_inst_num," + "     wdNACKResult.appe_date_time,"
				+ "     wdNACKResult.appe_seq_nbr," + "     " + getDbPortabilityHandler().getSubStr("rAppe.appe_session_holder", 1, 9) + " as biccode," + "     rAppe.appe_session_nbr AS SessionNbr," + "     rAppe.appe_sequence_nbr AS SequenceNbr,"
				+ "     case rAppe.appe_network_delivery_status" + "         when 'DLV_NACKED' then 1" + "         else 0" + "     end as isnack," + "     case rAppe.appe_network_delivery_status" + "         when 'DLV_NACKED' then "
				+ getDbPortabilityHandler().getSubStr("rAppe.appe_nak_reason", 1, 1) + "         else NULL" + "     end as errortype," + "     case rAppe.appe_network_delivery_status" + "         when 'DLV_NACKED' then "
				+ getDbPortabilityHandler().getSubStr("rAppe.appe_nak_reason", 2, 2) + "         else NULL" + "     end as errorcode," + "     case rAppe.appe_network_delivery_status" + "         when 'DLV_NACKED' then "
				+ getDbPortabilityHandler().getSubStr("rAppe.appe_nak_reason", 4, 3) + "         else NULL" + "     end as errorline," + "     '' as murref," // TODO Need to figure out where to get it
				+ "     rMesg.mesg_crea_date_time AS CreationDate," + "     rMesg.mesg_receiver_swift_address AS Receiver," + "     rMesg.mesg_sender_swift_address AS Sender," + "     rMesg.mesg_sub_format AS SubFormat,"
				+ "     rMesg.mesg_type AS MsgType," + "     rMesg.mesg_trn_ref AS TransactionReference," + "     rMesg.x_fin_ccy AS ccy," + "     rMesg.x_fin_amount AS amount," + "     rMesg.x_fin_value_date AS valuedate,"
				+ "     rMesg.mesg_identifier as identifier," + "     rAppe.appe_crea_mpfn_name AS Creator," + "     wdNACKResult.insert_time AS InsertTime," + "     wdNACKResult.SysId," + "     "
				+ getDbPortabilityHandler().getNullValue("wdSysRequest.processed", "0") + " as processed" + " FROM wdNACKResult" + " INNER JOIN rMesg ON " + "     wdNACKResult.aid = rMesg.aid AND"
				+ "     wdNACKResult.appe_s_umidl = rMesg.mesg_s_umidl AND" + "     wdNACKResult.appe_s_umidh = rMesg.mesg_s_umidh"
				+ (getDbPortabilityHandler().getDbType() == 0 && getDbPortabilityHandler().isPartitionedDB() == true ? " AND wdNACKResult.X_CREA_DATE_TIME_MESG = rMesg.mesg_crea_date_time " : "") + " LEFT OUTER JOIN rAppe ON"
				+ "     wdNACKResult.aid = rAppe.aid AND" + "     wdNACKResult.appe_s_umidl = rAppe.appe_s_umidl AND" + "     wdNACKResult.appe_s_umidh = rAppe.appe_s_umidh AND" + "     wdNACKResult.appe_inst_num = rAppe.appe_inst_num AND"
				+ "     wdNACKResult.appe_date_time = rAppe.appe_date_time AND" + "     wdNACKResult.appe_seq_nbr = rAppe.appe_seq_nbr"
				+ (getDbPortabilityHandler().getDbType() == 0 && getDbPortabilityHandler().isPartitionedDB() == true ? " AND wdNACKResult.X_CREA_DATE_TIME_MESG = rAppe.X_CREA_DATE_TIME_MESG " : "") + " LEFT OUTER JOIN wdSysRequest ON"
				+ "     wdNACKResult.sysid = wdSysRequest.sysid and" + "     wdSysRequest.request_type = 6" + " " + "     , SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP" + " where" + " 	SBICUSERGROUP.GROUPID = " + loggedInUserGroupId
				+ " AND SMSGUSERGROUP.GROUPID = " + loggedInUserGroupId + " AND SUNITUSERGROUP.GROUPID = " + loggedInUserGroupId + " AND (X_OWN_LT = BICCODE or X_OWN_LT ='XXXXXXXX' )" + " AND X_CATEGORY = CATEGORY" + " AND X_INST0_UNIT_NAME = UNIT"

				+ " ) wdNACKView ";

		return query;
	}

	String getWDEventRequestView(String loggedInUsername, boolean isAdminUser) {
		String query = " ( " + " SELECT " + "   j.aid," + "   j.jrnl_rev_date_time," + "   j.jrnl_seq_nbr," + "   j.jrnl_comp_name," + "   j.jrnl_event_is_security," + "   j.jrnl_event_num," + "   j.jrnl_event_name," + "   j.jrnl_event_class,"
				+ "   j.jrnl_event_severity," + "   j.jrnl_event_is_alarm," + "   j.jrnl_appl_serv_name," + "   j.jrnl_func_name," + "   j.jrnl_hostname," + "   j.jrnl_oper_nickname," + "   j.jrnl_date_time," + "   j.jrnl_alarm_status,"
				+ "   j.jrnl_alarm_dist_status," + "   j.jrnl_alarm_date_time," + "   j.jrnl_alarm_oper_nickname," + "   j.jrnl_display_text,";

		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			query += "   dbms_lob.substr(j.jrnl_merged_text, 4000, 1) AS jrnl_merged_text,";
		} else {
			query += "   j.jrnl_merged_text,";
		}
		query += "   j.jrnl_length," + "   j.jrnl_token," + "   j.archived," + "   j.restored," + "   j.last_update," + "   j.x_umidl," + "   j.x_umidh," + "   w.insert_time," + "   w.id," + "   w.request_id," + "   w.description," + "   w.processed"
				+ " FROM rJrnl j" + "   ,wdEventRequestResult w";
		if (isAdminUser) {
			query += "   ,suser u";
		}
		query += " WHERE " + "       j.aid                = w.aid" + "   AND j.jrnl_rev_date_time = w.jrnl_rev_date_time" + "   AND j.jrnl_seq_nbr       = w.jrnl_seq_nbr";
		if (isAdminUser) {
			query += "   AND UPPER (u.username)   = UPPER (w.Username)";
		} else {
			query += "   AND UPPER (w.username)   = '" + loggedInUsername.toUpperCase() + "' ";
		}
		query += " ) wdEventRequestView ";

		return query;
	}

	String nullWhenEmpty(String value) {
		return nullWhenEmpty(value, false);
	}

	String nullWhenEmpty(String value, boolean trim) {
		return (value == null || value.trim().isEmpty()) ? null : (trim ? value.trim() : value);
	}

	public BigDecimal formatAmount(BigDecimal amount, String currency) {

		try {
			if (amount == null || (currency == null || currency.isEmpty())) {
				return amount;
			}

			Integer numberOfDigits = currenciesMap.get(currency);

			if (numberOfDigits == null) {
				return amount;
			}

			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
			numberFormat.setMaximumFractionDigits(numberOfDigits);
			String format = numberFormat.format(amount);
			format = format.replaceAll("[^\\d.]", "");
			BigDecimal bigDecimal = new BigDecimal(format);
			return bigDecimal;

		} catch (Exception e) {
			System.out.println("Filed when Parsing amount");
		}

		return null;
	}

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

}
