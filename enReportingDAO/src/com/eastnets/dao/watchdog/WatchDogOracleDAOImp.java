package com.eastnets.dao.watchdog;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.watchdog.Annotaion;
import com.eastnets.domain.watchdog.EventNotification;
import com.eastnets.domain.watchdog.EventRequest;
import com.eastnets.domain.watchdog.MessageNotification;
import com.eastnets.domain.watchdog.MessageRequest;
import com.eastnets.domain.watchdog.SyntaxEntryField;

public class WatchDogOracleDAOImp extends WatchDogDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -713733336862888520L;

	@Override
	public List<EventNotification> getEventNotifications(Date fromDate, String loggedInUser, boolean sideAdminUser, Long from, Long to) {
		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "insert_time");
		String queryString = String.format(
				"select * from ( select a.*, ROWNUM rnum from (select insert_time, aid,jrnl_rev_date_time,jrnl_seq_nbr,id,jrnl_date_time,description,jrnl_comp_name,jrnl_event_num,jrnl_event_name,jrnl_merged_text from %s where %s order by insert_time desc, Id desc) a)Where rnum <= %d and rnum  >= %d",
				getWDEventRequestView(loggedInUser, sideAdminUser), formatDateTime, to, from);
		Collection<EventNotification> requests = jdbcTemplate.query(queryString, new RowMapper<EventNotification>() {
			public EventNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
				EventNotification request = new EventNotification();
				request.setReceptionDate(rs.getTimestamp("insert_time"));
				request.setAid(rs.getLong("aid"));
				request.setJrnlRevDatetime(rs.getLong("jrnl_rev_date_time"));
				request.setJrnlSeqNbr(rs.getDouble("jrnl_seq_nbr"));
				request.setNotificationId(rs.getLong("id"));
				// Fix TFS 17478 see reception time above.
				// request.setReceptionDate(rs.getTimestamp("jrnl_date_time"));
				request.setDescription(rs.getString("description"));
				request.setComponentName(rs.getString("jrnl_comp_name"));
				request.setEventNbr(rs.getString("jrnl_event_num"));
				request.setEventName(rs.getString("jrnl_event_name"));
				return request;
			}
		});
		ArrayList<EventNotification> requestsList = new ArrayList<EventNotification>();
		requestsList.addAll(requests);
		return requestsList;
	}

	@Override
	public List<MessageNotification> getMessageNotifications(Date fromDate, boolean sideAdminUser, Long from, Long to, Long loggedInUserGroupId) {
		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select * from ( select ROWNUM rnum,a.* from ( select * from %s where %s order by InsertTime desc , id DESC  ) a) where  rnum <= %d  and rnum  >= %d",
				getWDUserRequestView(loggedInUserGroupId, sideAdminUser), formatDateTime, to, from);
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

		return requestsList;
	}

	@Override
	public List<MessageNotification> getCalculatedDupplicateNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format("select * from ( select a.*, ROWNUM rnum from (select SYSID, INSERTTIME, DUPLICATE_TYPE, DUPLICATE_STRING" + ", AID, UMIDL, UMIDH, CREATIONDATE, SUFFIX, UMID, CCY, AMOUNT, VALUEDATE"
				+ ", TRANSACTIONREFERENCE, DUPAID, DUPUMIDL, DUPUMIDH, DUPCREATIONDATE" + ", DUPCCY, DUPAMOUNT, DUPVALUEDATE, RECEIVER, SENDER, SUBFORMAT, MSGTYPE," + " DUPSUFFIX, DATETIME, SESSIONNBR, SEQUENCENBR, DUPDATETIME, DUPSESSIONNBR,"
				+ " DUPSEQUENCENBR, PROCESSED , Ccy , Amount, ValueDate, Identifier from " + getWDCalculatedDupView21(loggedInUserGroupId) + " where %s order by InsertTime desc , SYSID DESC ) a )Where rnum <= %d and rnum  >= %d", formatDateTime, to,
				from);
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
				request.setIdentifier(rs.getString("Identifier"));
				request.setAmount(rs.getString("Amount"));
				request.setCurrency(rs.getString("Ccy"));
				request.setValueDate(rs.getTimestamp("ValueDate"));
				request.setReference(rs.getString("TRANSACTIONREFERENCE"));
				request.setNotificationType(Constants.WATCHDOGE_FILTER_CALCULATE_DUPLICATES);
				request.setMesgCreaDateTime(rs.getTimestamp("CREATIONDATE"));

				if (rs.getString("Amount") != null && !rs.getString("Amount").isEmpty()) {
					request.setAmount(formatAmount(new BigDecimal(rs.getString("Amount").trim()), rs.getString("CCy")).toString());
				}
				return request;
			}
		});
		return requestsList;
	}

	@Override
	public List<MessageNotification> getPossibleDupplicateNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format(
				"select * from ( select a.*, ROWNUM rnum from (select SYSID, AID" + ", UMIDL, UMIDH, USERISSUEDPDE, POSSIBLEPDE, POSSIBLEPDR, RECEIVER, SENDER, MSGTYPE, SUBFORMAT"
						+ ", TRANSACTIONREFERENCE, INSERTTIME, PROCESSED, CREATIONDATE, SESSIONNBR, SEQNBR , identifier, ccy, amount, valuedate" + " from %s where %s order by InsertTime desc , SYSID DESC ) a )Where rnum <= %d and rnum  >= %d",
				getWDPossibleDupView(loggedInUserGroupId), formatDateTime, to, from);
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
				request.setIdentifier(rs.getString("identifier"));
				request.setCurrency(rs.getString("ccy"));
				request.setAmount(rs.getString("amount"));
				request.setValueDate(rs.getTimestamp("valuedate"));
				request.setDescription("Possible Duplicate");
				request.setNotificationType(Constants.WATCHDOGE_FILTER_POSSIBLE_DUPLICATES);
				request.setMesgCreaDateTime(rs.getTimestamp("CREATIONDATE"));
				return request;
			}
		});

		return requestsList;
	}

	@Override
	public List<MessageNotification> getNakedNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format(
				"select * from ( select a.*, ROWNUM rnum from ( select AID, UMIDL, UMIDH, APPE_INST_NUM, APPE_DATE_TIME, APPE_SEQ_NBR, BICCODE, SESSIONNBR, SEQUENCENBR, ISNACK, ERRORTYPE, ERRORCODE, ERRORLINE, MURREF, CREATIONDATE, RECEIVER, SENDER, SUBFORMAT, MSGTYPE, TRANSACTIONREFERENCE, CREATOR, INSERTTIME, SYSID, PROCESSED ccy, amount, valuedate, identifier from %s where %s order by InsertTime desc , SYSID DESC ) a )Where rnum <= %d and rnum  >= %d",
				getWDNACKView(loggedInUserGroupId), formatDateTime, to, from);
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
				request.setIdentifier(rs.getString("identifier"));
				request.setAmount(rs.getString("amount"));
				request.setCurrency(rs.getString("ccy"));
				request.setValueDate(rs.getTimestamp("valuedate"));
				request.setNotificationType(Constants.WATCHDOGE_FILTER_NAKED_MESSAGES);

				request.setDescription("NAKed message");
				request.setMesgCreaDateTime(rs.getTimestamp("CREATIONDATE"));
				return request;
			}
		});

		return requestsList;
	}

	@Override
	public List<MessageRequest> getMessageRequests(boolean sideAdminUser, Long loggedInUserGroupId) {
		boolean wdmsEnable = super.getApplicationFeatures().isEnableWdmsChangesSupported();
		StringBuilder builder = new StringBuilder();
		if (!wdmsEnable) {
			builder.append(
					"select RequestID, USERNAME, Description,SenderBic,ReceiverBic,Trn_ref,SubFormat,MsgType,Amount,AmountTo,Ccy,DaysValue,SYSDATE + DaysValue as DaysValueDate, DateValueOp,autodelete,expirationdate, FIELDCODE, FIELDCODEID, FIELDOPTION, FIELDVALUE, USERGROUP,status, EMAIL, IDENTIFIER,REQUESTOR_DN, RESPONDER_DN from wdUserSearchParameter ");

		} else {
			builder.append(
					"select RequestID, USERNAME, Description,SenderBic,ReceiverBic,Trn_ref,SubFormat,MsgType,Amount,AmountTo,Ccy,DaysValue,SYSDATE + DaysValue as DaysValueDate, DateValueOp,autodelete,expirationdate, FIELDCODE, FIELDCODEID, FIELDOPTION, FIELDVALUE, USERGROUP,status, EMAIL, IDENTIFIER,REQUESTOR_DN, RESPONDER_DN"
							+ ", IGNORE_FIELD_OPTION " + "from wdUserSearchParameter ");

		}

		if (!sideAdminUser) {
			builder.append("where (UserGroup = 0 OR (UserGroup = 1 and (UPPER(USERNAME)in(select upper(UserName) from sUser where GroupId = " + loggedInUserGroupId + " ))))");
		}
		builder.append("order by description");

		String query = builder.toString();

		Collection<MessageRequest> requests = jdbcTemplate.query(query, new RowMapper<MessageRequest>() {
			public MessageRequest mapRow(ResultSet rs, int rowNum) throws SQLException {

				MessageRequest param = new MessageRequest();
				param.setId(rs.getLong("RequestID"));
				param.setUserName(rs.getString("USERNAME"));
				param.setDescription(rs.getString("Description"));
				param.setSenderBic(rs.getString("SenderBic"));
				param.setReceiverBic(rs.getString("ReceiverBic"));
				param.setReference(rs.getString("Trn_ref"));
				String value = rs.getString("SubFormat");
				value = (value == null || value.trim().equals("")) ? null : value.trim();
				param.setSubFormat(value);
				String msgType = rs.getString("MsgType");
				param.setMsgType(msgType);
				param.setAmount(rs.getString("Amount"));
				param.setAmountTo(rs.getString("AmountTo"));
				param.setCcy(rs.getString("Ccy"));
				param.setDaysValue(rs.getLong("DaysValue"));
				param.setDaysValueDate(rs.getDate(("DaysValueDate")));
				param.setDateValueOp(rs.getLong("DateValueOp"));
				param.setFieldValue(rs.getString("FIELDVALUE"));
				Long long1 = rs.getLong("autodelete");
				if (wdmsEnable) {
					Long ignoreFieldOptionFlag = rs.getLong("IGNORE_FIELD_OPTION");
					param.setIgnoreFieldOption((ignoreFieldOptionFlag == null || ignoreFieldOptionFlag == 0) ? false : true);
				}

				param.setAutoDelete((long1 == null || long1 == 0) ? false : true);
				param.setExpirationDate(rs.getDate("expirationdate"));
				param.setStatus(rs.getLong("status"));

				Long groupe = rs.getLong("USERGROUP");
				param.setGroupRequst(groupe == 1L);

				param.setEmail(rs.getString("EMAIL"));
				param.setIdentifier(rs.getString("IDENTIFIER"));
				param.setRequestorDN(rs.getString("REQUESTOR_DN"));
				param.setResponderDN(rs.getString("RESPONDER_DN"));

				Long fieldCode = rs.getLong("FIELDCODE");

				SyntaxEntryField syntaxEntryField = new SyntaxEntryField();
				if (msgType != null && !msgType.equals("") && fieldCode != null && fieldCode != 0L) {

					String fieldOption = rs.getString("FIELDOPTION");
					long fieldCodeId = rs.getLong("FIELDCODEID");

					syntaxEntryField.setCode(fieldCode);
					syntaxEntryField.setCodeId(fieldCodeId);
					syntaxEntryField.setFieldOption(fieldOption);

					String fieldValue = String.format("%d%s", fieldCode, (fieldOption == null || fieldOption.equals("0")) ? "" : fieldOption);
					syntaxEntryField.setFieldValue(fieldValue);

				}
				param.setSyntaxEntryField(syntaxEntryField);

				if (rs.getString("Amount") != null && !rs.getString("Amount").isEmpty()) {
					param.setAmount(formatAmount(new BigDecimal(rs.getString("Amount").trim()), rs.getString("CCy")).toString());
				}

				if (rs.getString("AmountTo") != null && !rs.getString("AmountTo").isEmpty()) {
					param.setAmountTo(formatAmount(new BigDecimal(rs.getString("AmountTo").trim()), rs.getString("CCy")).toString());
				}

				return param;
			}
		});
		ArrayList<MessageRequest> requestsList = new ArrayList<MessageRequest>();
		requestsList.addAll(requests);
		return requestsList;
	}

	@Override
	public MessageRequest getMessageRequest(Long id, boolean sideAdminUser, Long loggedInUserGroupId) {
		boolean wdmsEnable = super.getApplicationFeatures().isEnableWdmsChangesSupported();

		StringBuilder builder = new StringBuilder();
		if (!wdmsEnable) {
			builder.append(
					"select RequestID, USERNAME, Description,SenderBic,ReceiverBic,Trn_ref,SubFormat,MsgType,Amount,AmountTo,Ccy,DaysValue,SYSDATE + DaysValue as DaysValueDate, DateValueOp,autodelete,expirationdate, FIELDCODE, FIELDCODEID, FIELDOPTION, FIELDVALUE, USERGROUP,status, EMAIL,IDENTIFIER, REQUESTOR_DN, RESPONDER_DN from wdUserSearchParameter");
		} else {
			builder.append(
					"select RequestID, USERNAME, Description,SenderBic,ReceiverBic,Trn_ref,SubFormat,MsgType,Amount,AmountTo,Ccy,DaysValue,SYSDATE + DaysValue as DaysValueDate, DateValueOp,autodelete,expirationdate, FIELDCODE, FIELDCODEID, FIELDOPTION, FIELDVALUE, USERGROUP,status, EMAIL,IDENTIFIER, REQUESTOR_DN, RESPONDER_DN,IGNORE_FIELD_OPTION from wdUserSearchParameter");
		}
		builder.append(" where ");
		builder.append("RequestID = ");
		builder.append(id);
		if (sideAdminUser == false) {
			builder.append(" and (UserGroup = 0 OR (UserGroup = 1 and (UPPER(USERNAME)in(select upper(UserName) from sUser where GroupId = " + loggedInUserGroupId + "))))");

		}

		List<MessageRequest> requestsList = jdbcTemplate.query(builder.toString(), new RowMapper<MessageRequest>() {
			public MessageRequest mapRow(ResultSet rs, int rowNum) throws SQLException {

				MessageRequest param = new MessageRequest();
				param.setId(rs.getLong("RequestID"));
				param.setUserName(rs.getString("USERNAME"));
				param.setDescription(rs.getString("Description"));
				param.setSenderBic(rs.getString("SenderBic"));
				param.setReceiverBic(rs.getString("ReceiverBic"));
				param.setReference(rs.getString("Trn_ref"));
				String value = rs.getString("SubFormat");
				value = (value == null || value.trim().equals("")) ? null : value.trim();
				param.setSubFormat(value);
				String msgType = rs.getString("MsgType");
				param.setMsgType(msgType);
				param.setAmount(rs.getString("Amount"));
				param.setAmountTo(rs.getString("AmountTo"));
				param.setCcy(rs.getString("Ccy"));
				param.setDaysValue(rs.getLong("DaysValue"));
				param.setDaysValueDate(rs.getDate(("DaysValueDate")));
				param.setDateValueOp(rs.getLong("DateValueOp"));
				param.setFieldValue(rs.getString("FIELDVALUE"));
				if (wdmsEnable) {
					Long ignoreFieldOptionFlag = rs.getLong("IGNORE_FIELD_OPTION");
					param.setIgnoreFieldOption((ignoreFieldOptionFlag == null || ignoreFieldOptionFlag == 0) ? false : true);
				}
				Long long1 = rs.getLong("autodelete");
				param.setAutoDelete((long1 == null || long1 == 0) ? false : true);
				param.setExpirationDate(rs.getDate("expirationdate"));
				param.setStatus(rs.getLong("status"));

				Long groupe = rs.getLong("USERGROUP");
				param.setGroupRequst(groupe == 1L);

				param.setEmail(rs.getString("EMAIL"));
				param.setIdentifier(rs.getString("IDENTIFIER"));
				param.setRequestorDN(rs.getString("REQUESTOR_DN"));
				param.setResponderDN(rs.getString("RESPONDER_DN"));

				Long fieldCode = rs.getLong("FIELDCODE");

				SyntaxEntryField syntaxEntryField = new SyntaxEntryField();
				if (msgType != null && !msgType.equals("") && fieldCode != null && fieldCode != 0L) {

					String fieldOption = rs.getString("FIELDOPTION");
					long fieldCodeId = rs.getLong("FIELDCODEID");

					syntaxEntryField.setCode(fieldCode);
					syntaxEntryField.setCodeId(fieldCodeId);
					syntaxEntryField.setFieldOption(fieldOption);

					String fieldValue = String.format("%d%s", fieldCode, (fieldOption == null || fieldOption.equals("0")) ? "" : fieldOption);
					syntaxEntryField.setFieldValue(fieldValue);

				}
				param.setSyntaxEntryField(syntaxEntryField);

				if (rs.getString("Amount") != null && !rs.getString("Amount").isEmpty()) {
					param.setAmount(formatAmount(new BigDecimal(rs.getString("Amount").trim()), rs.getString("CCy")).toString());
				}

				if (rs.getString("AmountTo") != null && !rs.getString("AmountTo").isEmpty()) {
					param.setAmountTo(formatAmount(new BigDecimal(rs.getString("AmountTo").trim()), rs.getString("CCy")).toString());
				}

				return param;
			}
		});
		if (requestsList == null || requestsList.size() <= 0) {
			return null;
		}
		return requestsList.get(0);
	}

	@Override
	public void addNotificationAnnotaion(String loggedInUser, Annotaion annotaion) {

		StringBuilder builder = new StringBuilder();
		builder.append("insert into wdSysRequest (ID, sysid,request_type,processed,processed_by,processed_at,user_comment) values(wdSysRequest_id.nextval,");
		builder.append(annotaion.getSysID());
		builder.append(", ");
		builder.append(annotaion.getRequestType());
		builder.append(", 1, '");
		builder.append(loggedInUser);
		builder.append("', SYSDATE,'");
		builder.append(annotaion.getUserComment());
		builder.append("')");
		String insertQuery = builder.toString();

		jdbcTemplate.execute(insertQuery);

	}

	@Override
	public Long addEventRequest(EventRequest param, String loggedInUser) {
		Long requestID = jdbcTemplate.queryForLong("select wdEventSearchParameter_id.nextval from dual ");

		String query = " INSERT INTO wdEventSearchParameter " + " ( RequestID, UserName, jrnl_comp_name, jrnl_event_num, Description, ExpirationDate, email ) " + " VALUES " + " ( ?, ?, ?, ?, ?, ?, ? ) ";

		jdbcTemplate.update(query, new Object[] { requestID, loggedInUser.toUpperCase(), param.getComponentName(), param.getEventNbr(), param.getDescription(), param.getExpirationDate(), param.getEmail() });

		param.setId(requestID);
		return requestID;
	}

	@Override
	public Long addMessageRequest(MessageRequest messageType, String loggedInUser) {
		boolean wdmsEnable = super.getApplicationFeatures().isEnableWdmsChangesSupported();
		SyntaxEntryField syntaxEntryField = messageType.getSyntaxEntryField();
		Long fieldCode = null;
		Long fieldCodeId = null;
		String fieldOption = null;
		if (syntaxEntryField != null) {

			fieldCode = syntaxEntryField.getCode();
			fieldCode = (fieldCode == -1L) ? null : fieldCode;

			fieldCodeId = syntaxEntryField.getCodeId();
			fieldCodeId = (fieldCodeId == -1) ? null : fieldCodeId;

			fieldOption = syntaxEntryField.getFieldOption();
			fieldOption = (fieldOption != null && fieldOption.trim().equals("")) ? null : fieldOption;
		}

		Long requestID = jdbcTemplate.queryForLong("select wdUserSearchParameter_Id.nextval from dual ");

		if (!wdmsEnable) {
			String query = "insert into wdUserSearchParameter ( " + "   RequestId," + "   UserName," + "   Description," + "   Trn_Ref," + "   SenderBic," + "   ReceiverBic," + "   SubFormat," + "   AmountOp," + "   Amount," + "   AmountTo,"
					+ "   Ccy," + "   MsgType," + "   DaysValue," + "   DateValueOp," + "   FieldCode," + "   FieldCodeId," + "   FieldOption," + "   FieldValue," + "   AutoDelete," + "   ExpirationDate," + "   UserGroup," + "   Status,"
					+ "   Email," + "   Identifier," + "   Requestor_DN," + "   Responder_DN " + " ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

			jdbcTemplate.update(query,
					new Object[] { requestID, loggedInUser.toUpperCase(), messageType.getDescription(), nullWhenEmpty(messageType.getReference()), nullWhenEmpty(messageType.getSenderBic()), nullWhenEmpty(messageType.getReceiverBic()),
							nullWhenEmpty(messageType.getSubFormat(), true), messageType.getAmountOp(), nullWhenEmpty(messageType.getAmount()), nullWhenEmpty(messageType.getAmountTo()), nullWhenEmpty(messageType.getCcy()),
							nullWhenEmpty(messageType.getMsgType()), messageType.getDaysValue(), messageType.getDateValueOp(), fieldCode, fieldCodeId, fieldOption, nullWhenEmpty(messageType.getFieldValue()), messageType.getAutoDelete(),
							messageType.getExpirationDate(), (messageType.isGroupRequst() == true) ? 1L : 0L, messageType.getStatus(), nullWhenEmpty(messageType.getEmail()), nullWhenEmpty(messageType.getIdentifier()),
							nullWhenEmpty(messageType.getRequestorDN()), nullWhenEmpty(messageType.getResponderDN()) });
		} else {
			String query = "insert into wdUserSearchParameter ( " + "   RequestId," + "   UserName," + "   Description," + "   Trn_Ref," + "   SenderBic," + "   ReceiverBic," + "   SubFormat," + "   AmountOp," + "   Amount," + "   AmountTo,"
					+ "   Ccy," + "   MsgType," + "   DaysValue," + "   DateValueOp," + "   FieldCode," + "   FieldCodeId," + "   FieldOption," + "   FieldValue," + "   AutoDelete," + "   ExpirationDate," + "   UserGroup," + "   Status,"
					+ "   Email," + "   Identifier," + "   Requestor_DN," + "   Responder_DN " + "  , IGNORE_FIELD_OPTION" + " ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)";

			jdbcTemplate.update(query,
					new Object[] { requestID, loggedInUser.toUpperCase(), messageType.getDescription(), nullWhenEmpty(messageType.getReference()), nullWhenEmpty(messageType.getSenderBic()), nullWhenEmpty(messageType.getReceiverBic()),
							nullWhenEmpty(messageType.getSubFormat(), true), messageType.getAmountOp(), nullWhenEmpty(messageType.getAmount()), nullWhenEmpty(messageType.getAmountTo()), nullWhenEmpty(messageType.getCcy()),
							nullWhenEmpty(messageType.getMsgType()), messageType.getDaysValue(), messageType.getDateValueOp(), fieldCode, fieldCodeId, fieldOption, nullWhenEmpty(messageType.getFieldValue()), messageType.getAutoDelete(),
							messageType.getExpirationDate(), (messageType.isGroupRequst() == true) ? 1L : 0L, messageType.getStatus(), nullWhenEmpty(messageType.getEmail()), nullWhenEmpty(messageType.getIdentifier()),
							nullWhenEmpty(messageType.getRequestorDN()), nullWhenEmpty(messageType.getResponderDN()), messageType.getIgnoreFieldOption() });
		}

		messageType.setId(requestID);
		return requestID;
	}

	@Override
	public List<MessageNotification> getGapNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId, boolean isnGap, boolean osnGap) {

		String formatDateTime = getDbPortabilityHandler().getOneDayGreatthanOrEqualCondition(fromDate, "InsertTime");
		String queryString = String.format(
				"select * from ( select a.*, ROWNUM rnum from (select SYSID, LT, LASTSN, EXPECTEDSN, GAPTIME, INSERTTIME, ISISN, PROCESSED from %s where "
						+ (isnGap == true && osnGap == false ? " isIsn = 1 and " : isnGap == false && osnGap == true ? " isIsn = 0 and " : "") + " %s order by InsertTime desc  , SYSID DESC ) a ) where rnum <= %d and rnum  >= %d",
				getSWDSnGaps(loggedInUserGroupId), formatDateTime, to, from);
		List<MessageNotification> requestsList = jdbcTemplate.query(queryString, new RowMapper<MessageNotification>() {
			public MessageNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNotification request = new MessageNotification();

				request.setId(rs.getLong("SYSID"));
				request.setLogicalTerminal(rs.getString("LT"));
				request.setLastSequenceNumber(rs.getString("LASTSN"));
				request.setExpectedSequenceNumber(rs.getString("EXPECTEDSN"));
				request.setGapTime(rs.getTimestamp("GAPTIME"));
				request.setInsertTime(rs.getTimestamp("INSERTTIME"));
				request.setDescription(rs.getLong("ISISN") == 0 ? "OSN Gap" : "ISN Gap");
				request.setNotificationType(rs.getLong("ISISN") == 1 ? Constants.WATCHDOGE_FILTER_ISN_GAPS : Constants.WATCHDOGE_FILTER_OSN_GAPS);

				return request;
			}
		});
		return requestsList;

	}

	@Override
	public Long updateMessageRequest(MessageRequest messageType, String loggedInUser) {
		boolean wdmsEnable = super.getApplicationFeatures().isEnableWdmsChangesSupported();
		SyntaxEntryField syntaxEntryField = messageType.getSyntaxEntryField();
		Long fieldCode = null;
		Long fieldCodeId = null;
		String fieldOption = null;
		if (syntaxEntryField != null) {

			fieldCode = syntaxEntryField.getCode();
			fieldCode = (fieldCode == -1L) ? null : fieldCode;

			fieldCodeId = syntaxEntryField.getCodeId();
			fieldCodeId = (fieldCodeId == -1) ? null : fieldCodeId;

			fieldOption = syntaxEntryField.getFieldOption();
			fieldOption = (fieldOption != null && fieldOption.trim().equals("")) ? null : fieldOption;
		}

		if (!wdmsEnable) {
			String query = "update   wdUserSearchParameter  SET  " + "   UserName = ? , " + "   Description  = ? , " + "   Trn_Ref  = ? , " + "   SenderBic  = ? , " + "   ReceiverBic = ? , " + "   SubFormat  = ? , " + "   AmountOp  = ? , "
					+ "   Amount  = ? , " + "   AmountTo  = ? , " + "   Ccy  = ? , " + "   MsgType = ? , " + "   DaysValue = ? , " + "   DateValueOp = ? , " + "   FieldCode = ? , " + "   FieldCodeId = ? , " + "   FieldOption = ? , "
					+ "   FieldValue = ? , " + "   AutoDelete = ? , " + "   ExpirationDate = ? , " + "   UserGroup = ? , " + "   Status = ? , " + "   Email = ? , " + "   Identifier = ? , " + "   Requestor_DN = ? , " + "   Responder_DN = ? "
					+ " WHERE  REQUESTID = ?  ";

			jdbcTemplate.update(query,
					new Object[] { loggedInUser.toUpperCase(), messageType.getDescription(), nullWhenEmpty(messageType.getReference()), nullWhenEmpty(messageType.getSenderBic()), nullWhenEmpty(messageType.getReceiverBic()),
							nullWhenEmpty(messageType.getSubFormat(), true), messageType.getAmountOp(), nullWhenEmpty(messageType.getAmount()), nullWhenEmpty(messageType.getAmountTo()), nullWhenEmpty(messageType.getCcy()),
							nullWhenEmpty(messageType.getMsgType()), messageType.getDaysValue(), messageType.getDateValueOp(), fieldCode, fieldCodeId, fieldOption, nullWhenEmpty(messageType.getFieldValue()), messageType.getAutoDelete(),
							messageType.getExpirationDate(), (messageType.isGroupRequst() == true) ? 1L : 0L, messageType.getStatus(), nullWhenEmpty(messageType.getEmail()), nullWhenEmpty(messageType.getIdentifier()),
							nullWhenEmpty(messageType.getRequestorDN()), nullWhenEmpty(messageType.getResponderDN()), messageType.getId() });
		} else {
			String query = "update   wdUserSearchParameter  SET  " + "   UserName = ? , " + "   Description  = ? , " + "   Trn_Ref  = ? , " + "   SenderBic  = ? , " + "   ReceiverBic = ? , " + "   SubFormat  = ? , " + "   AmountOp  = ? , "
					+ "   Amount  = ? , " + "   AmountTo  = ? , " + "   Ccy  = ? , " + "   MsgType = ? , " + "   DaysValue = ? , " + "   DateValueOp = ? , " + "   FieldCode = ? , " + "   FieldCodeId = ? , " + "   FieldOption = ? , "
					+ "   FieldValue = ? , " + "   AutoDelete = ? , " + "   ExpirationDate = ? , " + "   UserGroup = ? , " + "   Status = ? , " + "   Email = ? , " + "   Identifier = ? , " + "   Requestor_DN = ? , " + "   Responder_DN = ?, "
					+ "  IGNORE_FIELD_OPTION = ? " + " WHERE  REQUESTID = ?  ";

			jdbcTemplate.update(query,
					new Object[] { loggedInUser.toUpperCase(), messageType.getDescription(), nullWhenEmpty(messageType.getReference()), nullWhenEmpty(messageType.getSenderBic()), nullWhenEmpty(messageType.getReceiverBic()),
							nullWhenEmpty(messageType.getSubFormat(), true), messageType.getAmountOp(), nullWhenEmpty(messageType.getAmount()), nullWhenEmpty(messageType.getAmountTo()), nullWhenEmpty(messageType.getCcy()),
							nullWhenEmpty(messageType.getMsgType()), messageType.getDaysValue(), messageType.getDateValueOp(), fieldCode, fieldCodeId, fieldOption, nullWhenEmpty(messageType.getFieldValue()), messageType.getAutoDelete(),
							messageType.getExpirationDate(), (messageType.isGroupRequst() == true) ? 1L : 0L, messageType.getStatus(), nullWhenEmpty(messageType.getEmail()), nullWhenEmpty(messageType.getIdentifier()),
							nullWhenEmpty(messageType.getRequestorDN()), nullWhenEmpty(messageType.getResponderDN()), messageType.getIgnoreFieldOption(), messageType.getId() });
		}

		// Long requestID = jdbcTemplate.queryForLong("select max(RequestID) from wdEventSearchParameter ");
		// messageType.setId(requestID);

		return null;
	}

	private String buildFeildQuerey(String base, String newFeild) {
		if (!base.isEmpty()) {
			return base + " and " + newFeild;
		}
		return newFeild;
	}

	@Override
	public List<MessageRequest> serachMessageRequests(String loggedInUser, boolean sideAdminUser, Long loggedInUserGroupId, String email, String accountValue, String description) {
		boolean wdmsEnable = super.getApplicationFeatures().isEnableWdmsChangesSupported();

		StringBuilder builder = new StringBuilder();
		if (!wdmsEnable) {
			builder.append(
					"select RequestID, USERNAME, Description,SenderBic,ReceiverBic,Trn_ref,SubFormat,MsgType,Amount,AmountTo,Ccy,DaysValue,SYSDATE + DaysValue as DaysValueDate, DateValueOp,autodelete,expirationdate, FIELDCODE, FIELDCODEID, FIELDOPTION, FIELDVALUE, USERGROUP,status, EMAIL, IDENTIFIER,REQUESTOR_DN, RESPONDER_DN from wdUserSearchParameter ");
		} else {
			builder.append(
					"select RequestID, USERNAME, Description,SenderBic,ReceiverBic,Trn_ref,SubFormat,MsgType,Amount,AmountTo,Ccy,DaysValue,SYSDATE + DaysValue as DaysValueDate, DateValueOp,autodelete,expirationdate, FIELDCODE, FIELDCODEID, FIELDOPTION, FIELDVALUE, USERGROUP,status, EMAIL, IDENTIFIER,REQUESTOR_DN, RESPONDER_DN,IGNORE_FIELD_OPTION from wdUserSearchParameter ");

		}

		List<Object> parameters = new ArrayList<>();

		if (accountValue.isEmpty() && email.isEmpty() && description.isEmpty()) {
			// do nothing
		} else {

			builder.append(" where ");

			String feildQuery = "";

			if (!accountValue.isEmpty()) {
				feildQuery = buildFeildQuerey(feildQuery, " FIELDVALUE like  ?");
				parameters.add("%".concat(accountValue).concat("%"));
			}
			if (!email.isEmpty()) {
				feildQuery = buildFeildQuerey(feildQuery, "  EMAIL like  ?");
				parameters.add("%".concat(email).concat("%"));

			}
			if (!description.isEmpty()) {
				feildQuery = buildFeildQuerey(feildQuery, "  Description like  ?");
				parameters.add("%".concat(description).concat("%"));

			}

			builder.append(feildQuery);

		}

		if (!sideAdminUser) {
			if (accountValue.isEmpty() && email.isEmpty() & description.isEmpty()) {

				builder.append(" where " + "(UserGroup = 0 OR (UserGroup = 1 and (UPPER(USERNAME)in(select upper(UserName) from sUser where GroupId = " + loggedInUserGroupId + "))))");
			} else {

				builder.append(" and " + "(UserGroup = 0 OR (UserGroup = 1 and (UPPER(USERNAME)in(select upper(UserName) from sUser where GroupId = " + loggedInUserGroupId + "))))");
			}
		}

		builder.append(" order by description");
		String query = builder.toString();

		Collection<MessageRequest> requests = jdbcTemplate.query(query, parameters.toArray(), new RowMapper<MessageRequest>() {
			public MessageRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageRequest param = new MessageRequest();
				param.setId(rs.getLong("RequestID"));
				param.setUserName(rs.getString("USERNAME"));
				param.setDescription(rs.getString("Description"));
				param.setSenderBic(rs.getString("SenderBic"));
				param.setReceiverBic(rs.getString("ReceiverBic"));
				param.setReference(rs.getString("Trn_ref"));
				String value = rs.getString("SubFormat");
				value = (value == null || value.trim().equals("")) ? null : value.trim();
				param.setSubFormat(value);
				String msgType = rs.getString("MsgType");
				param.setMsgType(msgType);
				param.setAmount(rs.getString("Amount"));
				param.setAmountTo(rs.getString("AmountTo"));
				param.setCcy(rs.getString("Ccy"));
				param.setDaysValue(rs.getLong("DaysValue"));
				param.setDaysValueDate(rs.getDate(("DaysValueDate")));
				param.setDateValueOp(rs.getLong("DateValueOp"));
				param.setFieldValue(rs.getString("FIELDVALUE"));
				Long long1 = rs.getLong("autodelete");
				if (wdmsEnable) {
					Long ignoreFieldOptionFlag = rs.getLong("IGNORE_FIELD_OPTION");
					param.setIgnoreFieldOption((ignoreFieldOptionFlag == null || ignoreFieldOptionFlag == 0) ? false : true);
				}

				param.setAutoDelete((long1 == null || long1 == 0) ? false : true);
				param.setExpirationDate(rs.getDate("expirationdate"));
				param.setStatus(rs.getLong("status"));

				Long groupe = rs.getLong("USERGROUP");
				param.setGroupRequst(groupe == 1L);

				param.setEmail(rs.getString("EMAIL"));
				param.setIdentifier(rs.getString("IDENTIFIER"));
				param.setRequestorDN(rs.getString("REQUESTOR_DN"));
				param.setResponderDN(rs.getString("RESPONDER_DN"));

				Long fieldCode = rs.getLong("FIELDCODE");

				SyntaxEntryField syntaxEntryField = new SyntaxEntryField();
				if (msgType != null && !msgType.equals("") && fieldCode != null && fieldCode != 0L) {

					String fieldOption = rs.getString("FIELDOPTION");
					long fieldCodeId = rs.getLong("FIELDCODEID");

					syntaxEntryField.setCode(fieldCode);
					syntaxEntryField.setCodeId(fieldCodeId);
					syntaxEntryField.setFieldOption(fieldOption);

					String fieldValue = String.format("%d%s", fieldCode, (fieldOption == null || fieldOption.equals("0")) ? "" : fieldOption);
					syntaxEntryField.setFieldValue(fieldValue);

				}
				param.setSyntaxEntryField(syntaxEntryField);

				if (rs.getString("Amount") != null && !rs.getString("Amount").isEmpty()) {
					param.setAmount(formatAmount(new BigDecimal(rs.getString("Amount").trim()), rs.getString("CCy")).toString());
				}

				if (rs.getString("AmountTo") != null && !rs.getString("AmountTo").isEmpty()) {
					param.setAmountTo(formatAmount(new BigDecimal(rs.getString("AmountTo").trim()), rs.getString("CCy")).toString());
				}

				return param;
			}
		});
		ArrayList<MessageRequest> requestsList = new ArrayList<MessageRequest>();
		requestsList.addAll(requests);
		return requestsList;

	}

}
