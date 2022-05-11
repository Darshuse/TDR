
package com.eastnets.notifier.repository;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialClob;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.eastnets.notifier.domain.EventData;
import com.eastnets.notifier.domain.Message;
import com.eastnets.notifier.domain.NotifierEventObserver;
import com.eastnets.notifier.domain.PrimaryKey;
import com.eastnets.notifier.domain.Text;
import com.eastnets.notifier.service.MessageStatus;
import com.eastnets.notifier.util.NotifierUtil;

@Component
public class ENEventsDAOImpl implements ENEventsDAO {
	@Value("${schemaName}")
	private String defaultSchema;
	@Autowired
	private LockReadEventsProcedure lockReadEventsProcedure;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Map<String, List<String>> notifiedEventsMap;

	private static final Logger LOGGER = LoggerFactory.getLogger(ENEventsDAOImpl.class);
	// private static final Pattern CHARGES_DETAILS_PATTERN =
	// Pattern.compile("(?<=71F:|71G:)(.*)");
	private static final Pattern NARRATIVE_DESCRIPTION = Pattern.compile("(?<=79:)(.*)((\\Q\\E))");
	private String sequences;

	@Override
	public List<EventData> fetchNewEvents(int bulkSize) {
		LOGGER.info("Start  Debug Version .... ! ");
		LOGGER.info("Start  fetching new events from  RUPDATENOTIFIER .... ");
		sequences = lockReadEventsProcedure.execute(bulkSize);
		LOGGER.info("Events with SEQ_NUM [" + sequences + "] ");

		if (sequences.equals("-1")) {
			return new ArrayList<EventData>();
		}
		try {
			String query = "SELECT SEQ_NUM,AID,S_UMIDL,S_UMIDH,INST_NUM,DATE_TIME,SEQ_NBR,TABLE_NAME,OPERATION_MODE,INSERTION_DATE_TIME FROM " + defaultSchema + '.' + "RUPDATENOTIFIER WHERE SEQ_NUM IN (" + sequences + ")";

			LOGGER.debug("Query for fetchNewEvents " + query);
			List<EventData> events = jdbcTemplate.query(query, new RowMapper<EventData>() {

				@Override
				public EventData mapRow(ResultSet rs, int rowNum) throws SQLException {
					EventData eventData = new EventData();
					eventData.setSequenceNumber(rs.getString("SEQ_NUM"));
					PrimaryKey primaryKey = new PrimaryKey();
					primaryKey.setAid(rs.getInt("AID"));
					primaryKey.setUmidl(rs.getString("S_UMIDL"));
					primaryKey.setUmidh(rs.getString("S_UMIDH"));
					eventData.setPrimaryKey(primaryKey);
					eventData.setInterventionSeqNumber(rs.getString("SEQ_NBR"));
					eventData.setInsertionDateTime(rs.getTimestamp("INSERTION_DATE_TIME").toLocalDateTime());
					// if null
					if (rs.getTimestamp("DATE_TIME") != null) {
						eventData.setDateTime(rs.getTimestamp("DATE_TIME").toLocalDateTime());
					} else {
						eventData.setDateTime(rs.getTimestamp("INSERTION_DATE_TIME").toLocalDateTime());
					}
					eventData.setTableName(rs.getString("TABLE_NAME"));
					eventData.setOperationType(rs.getInt("OPERATION_MODE"));
					eventData.setInstanceNumber(Integer.toString(rs.getInt("INST_NUM")));

					return eventData;
				}
			});

			if (!events.isEmpty()) {
				LOGGER.debug("Get " + events.size() + " Events from RUPDATENOTIFIER");
				// remove any event row realted with another table which is not RINTV
				List<EventData> removableEvent = events.stream().filter(e -> !e.getTableName().equalsIgnoreCase("RINTV")).collect(Collectors.toList());
				if (!removableEvent.isEmpty())
					deleteNonIntervDataFromRUpdateNotifier();
				events = events.stream().filter(e -> e.getTableName().equalsIgnoreCase("RINTV")).collect(Collectors.toList());
				if (events.isEmpty()) {
					return events;
				}

				Map<String, EventData> map = new HashMap<>();
				for (EventData eventData : events) {
					map.put(composeKey(eventData), eventData);
				}
				String primaryKeys = buildPrimaryKeyString(events);
				String intvSeqNumber = buildInterventionSeqNumberString(events);

				LOGGER.debug("primaryKeys" + primaryKeys);
				LOGGER.debug("Start Prepare Event Data Map");
				for (String key : map.keySet()) {
					LOGGER.debug("Event Data Map Element :: primary key : " + key);
				}

				LOGGER.debug("Start fetchMessageDetails .... ");

				try {
					events = fetchInterventionDetails(map, primaryKeys, intvSeqNumber);

					events = fetchMessageDetails(events, primaryKeys);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

				LOGGER.debug("End fetchMessageDetails .... ");
			}
			Collections.sort(events);
			return events;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private String composeKey(EventData data) {

		return data.getTableName().concat("_").concat(data.getInstanceNumber()).concat("_").concat(data.getInterventionSeqNumber()).concat("_").concat(data.getPrimaryKey().toString());

	}

	/**
	 * 
	 */
	private List<EventData> fetchMessageDetails(List<EventData> events, String primaryKeys) {
		String query = "SELECT M.AID,M.MESG_S_UMIDL,M.MESG_S_UMIDH,M.MESG_TYPE,M.X_FIN_AMOUNT,M.MESG_TRN_REF,M.MESG_REL_TRN_REF,M.MESG_E2E_TRANSACTION_REFERENCE,M.MESG_SLA,M.X_FIN_VALUE_DATE,M.MESG_CREA_RP_NAME,M.MESG_MOD_DATE_TIME,M.MESG_FIN_VALUE_DATE,M.MESG_FIN_CCY_AMOUNT,"
				+ "M.MESG_STATUS,M.MESG_SENDER_X1,M.X_RECEIVER_X1,M.MESG_SUB_FORMAT,M.MESG_RECEIVER_SWIFT_ADDRESS,M.MESG_SENDER_SWIFT_ADDRESS,M.MESG_NETWORK_PRIORITY,M.MESG_NETWORK_DELV_NOTIF_REQ,M.MESG_DELV_OVERDUE_WARN_REQ,M.MESG_NETWORK_OBSO_PERIOD,M.MESG_CREA_DATE_TIME,M.MESG_COPY_SERVICE_ID, "
				+ "M.MESG_USER_PRIORITY_CODE,M.X_INST0_UNIT_NAME,M.MESG_USER_REFERENCE_TEXT,M.MESG_RELEASE_INFO,M.MESG_MESG_USER_GROUP,M.MESG_POSSIBLE_DUP_CREATION,M.MESG_USER_ISSUED_AS_PDE,T.TEXT_DATA_BLOCK,T.TEXT_SWIFT_BLOCK_5,M.MESG_IDENTIFIER FROM "
				+ defaultSchema + '.' + "RMESG M," + defaultSchema + '.' + "RTEXT T WHERE M.AID = T.AID AND  M.MESG_S_UMIDH = T.TEXT_S_UMIDH  AND M.MESG_S_UMIDL = T.TEXT_S_UMIDL AND " + "(T.AID,T.TEXT_S_UMIDH,T.TEXT_S_UMIDL) IN (" + primaryKeys
				+ ")";

		LOGGER.trace("Fetch message details query  " + query + " to get messages based on Event Map :: For every message inside Map");

		jdbcTemplate.query(query, new RowMapper<EventData>() {

			@Override
			public EventData mapRow(ResultSet rs, int rowNum) throws SQLException {
				StringBuilder primaryKey = new StringBuilder();

				primaryKey.append("(").append(rs.getString("AID")).append(',').append(rs.getString("MESG_S_UMIDH")).append(',').append(rs.getString("MESG_S_UMIDL")).append(')');

				LOGGER.trace("message ID >>  " + primaryKey.toString());

				List<EventData> eventList = events.stream().filter(e -> e.getPrimaryKey().toString().equals(primaryKey.toString())).collect(Collectors.toList());
				eventList.forEach(e -> {
					try {
						e.getMessage().setMessageType(rs.getString("MESG_TYPE"));
						e.getMessage().setMessageIdentifer(rs.getString("MESG_IDENTIFIER"));
						e.getMessage().setAmount(rs.getBigDecimal("X_FIN_AMOUNT"));
						e.getMessage().setAmountStr(rs.getString("MESG_FIN_CCY_AMOUNT"));
						e.getMessage().setTransactionReference(rs.getString("MESG_TRN_REF"));
						e.getMessage().setRelatedReference(rs.getString("MESG_REL_TRN_REF"));
						e.getMessage().setUterReferenceNumber(rs.getString("MESG_E2E_TRANSACTION_REFERENCE"));
						e.getMessage().setServiceTypeIdentifier(rs.getString("MESG_SLA"));
						e.getMessage().setUnitName(rs.getString("X_INST0_UNIT_NAME"));
						if (rs.getString("MESG_FIN_VALUE_DATE") != null) {
							e.getMessage().setValueDateStr(rs.getString("MESG_FIN_VALUE_DATE"));

						} else {
							e.getMessage().setValueDate(null);

						}

						if (rs.getTimestamp("X_FIN_VALUE_DATE") != null) {
							e.getMessage().setValueDate(rs.getTimestamp("X_FIN_VALUE_DATE").toLocalDateTime());
						} else {
							e.getMessage().setValueDate(null);
						}

						e.getMessage().setMessageStatus(rs.getString("MESG_STATUS"));
						e.getMessage().setSenderBIC(rs.getString("MESG_SENDER_X1"));
						e.getMessage().setRecieverBIC(rs.getString("X_RECEIVER_X1"));
						e.getMessage().setDirection(rs.getString("MESG_SUB_FORMAT"));
						e.getMessage().setMesgCreRpName(rs.getString("MESG_CREA_RP_NAME"));
						if (rs.getTimestamp("MESG_MOD_DATE_TIME") != null) {
							e.getMessage().setMesgModifiedDateTime(rs.getTimestamp("MESG_MOD_DATE_TIME").toLocalDateTime());
						}

						else {
							e.getMessage().setMesgModifiedDateTime(null);

						}
						e.getMessage().setMesgReceiverSwiftAddress(rs.getString("MESG_RECEIVER_SWIFT_ADDRESS"));
						e.getMessage().setMesgSenderSwiftAddress(rs.getString("MESG_SENDER_SWIFT_ADDRESS"));
						e.getMessage().setMesgNetworkPriority(rs.getString("MESG_NETWORK_PRIORITY"));
						e.getMessage().setMesgNetworkDelvNotifReq(rs.getString("MESG_NETWORK_DELV_NOTIF_REQ"));
						e.getMessage().setMesgDelvOverdueWarnReq(rs.getString("MESG_DELV_OVERDUE_WARN_REQ"));
						e.getMessage().setMesgNetworkObsoPeriod(rs.getString("MESG_NETWORK_OBSO_PERIOD"));
						if (rs.getTimestamp("MESG_CREA_DATE_TIME") != null) {
							e.getMessage().setMesgCreaDateTime(new Date(rs.getTimestamp("MESG_CREA_DATE_TIME").getTime()));
						}
						e.getMessage().setMesgCopyServiceId(rs.getString("MESG_COPY_SERVICE_ID"));
						e.getMessage().setMesgUserPriorityCode(rs.getString("MESG_USER_PRIORITY_CODE"));
						e.getMessage().setMesgUserReferenceText(rs.getString("MESG_USER_REFERENCE_TEXT"));
						e.getMessage().setMesgReleaseInfo(rs.getString("MESG_RELEASE_INFO"));
						e.getMessage().setMesgUserGroup(rs.getString("MESG_MESG_USER_GROUP"));
						e.getMessage().setTextSwiftBlock5(rs.getString("TEXT_SWIFT_BLOCK_5"));
						e.getMessage().setMesgPossibleDupCreation(rs.getString("MESG_POSSIBLE_DUP_CREATION"));
						e.getMessage().setMesgUserIssuedAsPde(rs.getString("MESG_USER_ISSUED_AS_PDE"));

						Text text = new Text();
						Clob textBlock = rs.getClob("TEXT_DATA_BLOCK");
						text.setFullText(textBlock.getSubString(1, (int) textBlock.length()));

						e.getMessage().setText(text);

						e = fillGPIFields(e);
						e.setEventProcessed(true);
					} catch (Exception ex) {
						LOGGER.error("error felling message details with primary key " + primaryKey.toString());
						ex.printStackTrace();
					}
				});

				return null;
			}
		});

		LOGGER.info("start fetchAppendixDetails >> ");
		fetchAppendixDetails(events, primaryKeys);

		return events;

	}

	private EventData fillGPIFields(EventData eventData) {
		/**
		 * Check if this message is gpi
		 */

		try {
			if (eventData.getMessage().getMessageType() != null && (eventData.getMessage().getMessageType().equals("199") || eventData.getMessage().getMessageType().equals("299")) && eventData.getMessage().getUterReferenceNumber() != null
					&& !eventData.getMessage().getUterReferenceNumber().isEmpty() && eventData.getMessage().getServiceTypeIdentifier() != null
					&& (eventData.getMessage().getServiceTypeIdentifier().equals("001") || eventData.getMessage().getServiceTypeIdentifier().equals("002"))) {
				// fetch text block
				String textBlock = eventData.getMessage().getText().getFullText();
				if (textBlock != null && !textBlock.isEmpty()) {
					// extract Narrative description
					Matcher matcher = NARRATIVE_DESCRIPTION.matcher(textBlock);
					String narrativeDescription = textBlock.substring(textBlock.indexOf(":79:"));
					if (!matcher.matches()) {
						eventData.getMessage().getText().setNarrativeDescription(narrativeDescription);
					}
					// matcher = CHARGES_DETAILS_PATTERN.matcher(textBlock);
					List<String> charges = new ArrayList<>();
					/*
					 * for (int index = 0; index < matcher.groupCount(); index++) { charges.add(matcher.group(index)); }
					 */
					narrativeDescription = narrativeDescription.replaceAll("\n", "");
					narrativeDescription = narrativeDescription.replaceAll("\r", "");
					narrativeDescription = narrativeDescription.replaceAll(",", ".");
					String[] lines = narrativeDescription.split("//");

					for (String line : lines) {
						if (line.contains(":71F")) {
							charges.add(line.substring(5));
						} else if (line.contains(":71G")) {
							charges.add(line.substring(5));
						}
					}

					if (!charges.isEmpty()) {
						eventData.getMessage().getText().setChargesDetails(charges);

					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception inside fillGPIFields ");
			e.printStackTrace();
		}

		return eventData;

	}

	private String buildPrimaryKeyString(List<EventData> events) {
		Assert.noNullElements(events, "Events must not be empty");
		StringBuilder pkString = new StringBuilder();
		events.forEach((event) -> {
			pkString.append(event.getPrimaryKey().toString()).append(',');
		});
		return pkString.substring(0, pkString.length() - 1);
	}

	private String buildInterventionSeqNumberString(List<EventData> events) {
		Assert.noNullElements(events, "Events must not be empty");
		StringBuilder seqNumberString = new StringBuilder();
		events.forEach((event) -> {
			if (event.getInterventionSeqNumber() != null) {
				seqNumberString.append(event.getInterventionSeqNumber()).append(',');
			}
		});
		return seqNumberString.toString().isEmpty() ? null : seqNumberString.substring(0, seqNumberString.length() - 1);
	}

	private String buildCompsiteKeyString(List<EventData> events) {
		Assert.noNullElements(events, "Events must not be empty");
		StringBuilder compsiteKey = new StringBuilder();
		events.forEach((event) -> {
			compsiteKey.append('(').append(event.getPrimaryKey().getAid()).append(',').append(event.getPrimaryKey().getUmidl()).append(',').append(event.getPrimaryKey().getUmidh()).append(",\'").append(event.getTableName()).append("\'),");
		});
		return compsiteKey.substring(0, compsiteKey.length() - 1);
	}

	/**
	 * Fetch last appendix for specific instance number</br>
	 * This method will fetch the appendices for bulk of messages
	 * 
	 * @param eventsMap
	 * @param primaryKeys
	 * @return list of events that have updated appendices if exist
	 */
	private List<EventData> fetchAppendixDetails(List<EventData> events, String primaryKeys) {

		String query = "SELECT DISTINCT A2.APPE_IAPP_NAME,A2.APPE_TYPE, A2.X_APPE_LAST,A2.APPE_NETWORK_DELIVERY_STATUS,A2.APPE_SESSION_NBR,A2.APPE_SEQUENCE_NBR,A2.APPE_LOCAL_OUTPUT_TIME,A2.APPE_REMOTE_INPUT_TIME,A2.APPE_REMOTE_INPUT_REFERENCE,"
				+ "A2.APPE_ACK_NACK_TEXT,A2.APPE_CHECKSUM_VALUE,A2.APPE_CHECKSUM_RESULT,A2.APPE_AUTH_VALUE,A2.appe_auth_result,A2.APPE_SESSION_HOLDER,INST.INST_UNIT_NAME,INST.INST_RP_NAME,A1.* FROM ("
				+ "SELECT AID,APPE_S_UMIDL,APPE_S_UMIDH,APPE_INST_NUM,MAX(APPE_SEQ_NBR) M_SEQ_NBR FROM RAPPE WHERE " + "(AID,APPE_S_UMIDH,APPE_S_UMIDL) in (" + primaryKeys
				+ ") AND APPE_IAPP_NAME='SWIFT' GROUP BY AID,APPE_S_UMIDL,APPE_S_UMIDH,APPE_INST_NUM) A1 ,RAPPE A2,RINST INST " + "WHERE A1.AID = A2.AID AND A1.APPE_S_UMIDH = A2.APPE_S_UMIDH AND A1.APPE_S_UMIDL = A2.APPE_S_UMIDL "
				+ "     AND A1.APPE_INST_NUM = A2.APPE_INST_NUM AND A1.M_SEQ_NBR = A2.APPE_SEQ_NBR " + "     AND A2.AID = INST.AID AND A2.APPE_S_UMIDH = INST.INST_S_UMIDH AND A2.APPE_S_UMIDL = INST.INST_S_UMIDL";

		jdbcTemplate.query(query, new RowMapper<EventData>() {

			@Override
			public EventData mapRow(ResultSet rs, int rowNum) throws SQLException {

				StringBuilder primaryKey = new StringBuilder();
				primaryKey.append("(").append(rs.getString("AID")).append(',').append(rs.getString("APPE_S_UMIDH")).append(',').append(rs.getString("APPE_S_UMIDL")).append(')');

				LOGGER.trace("fetch appendix details RAPPE >> with primary key  " + primaryKey.toString());

				List<EventData> eventList = events.stream().filter(e -> e.getPrimaryKey().toString().equals(primaryKey.toString())).collect(Collectors.toList());
				eventList.forEach(e -> {
					try {

						e.getMessage().setNetwrokStatus(rs.getString("APPE_NETWORK_DELIVERY_STATUS"));
						e.getMessage().setSourceEntity(rs.getString("APPE_SESSION_HOLDER"));
						e.getMessage().setAppeSessionNbr(rs.getInt("APPE_SESSION_NBR"));
						e.getMessage().setAppeSequenceNbr(rs.getInt("APPE_SEQUENCE_NBR"));
						e.getMessage().setAppendixIappName(rs.getString("APPE_IAPP_NAME"));
						e.getMessage().setAppendixType(rs.getString("APPE_TYPE"));

						Date dt = null;
						if (rs.getTimestamp("APPE_LOCAL_OUTPUT_TIME") != null) {
							dt = new Date(rs.getTimestamp("APPE_LOCAL_OUTPUT_TIME").getTime());
						}
						e.getMessage().setAppeLocalOutputTime(dt);
						dt = null;
						if (rs.getTimestamp("APPE_REMOTE_INPUT_TIME") != null) {
							dt = new Date(rs.getTimestamp("APPE_REMOTE_INPUT_TIME").getTime());
						}

						e.getMessage().setAppeRemoteInputTime(dt);
						e.getMessage().setAppeRemoteInputReference(rs.getString("APPE_REMOTE_INPUT_REFERENCE"));
						if (rs.getString("APPE_ACK_NACK_TEXT") != null) {

							e.getMessage().setAppeAckNackText(new SerialClob(rs.getString("APPE_ACK_NACK_TEXT").toCharArray()));
						}
						e.getMessage().setAppeChecksumValue(rs.getString("APPE_CHECKSUM_VALUE"));
						e.getMessage().setAppeChecksumResult(rs.getString("APPE_CHECKSUM_RESULT"));

						if (rs.getString("APPE_AUTH_VALUE") != null) {

							e.getMessage().setAppeAuthValue(new SerialClob(rs.getString("APPE_AUTH_VALUE").toCharArray()));
						}
						e.getMessage().setAppeAuthResult(rs.getString("APPE_AUTH_RESULT"));
					} catch (Exception ex) {
						LOGGER.error("error felling message details with primary key " + primaryKey.toString());
						ex.printStackTrace();
					}
				});
				return null;
			}
		});
		return events;
	}

	/**
	 * Fetch last intervention for specific instance number</br>
	 * This method will fetch the interventions for bulk of messages
	 * 
	 * @param eventsMap
	 * @param primaryKeys
	 * @return list of events that have updated interventions if exist
	 */
	private List<EventData> fetchInterventionDetails(Map<String, EventData> eventsMap, String primaryKeys, String intvSeqNumber) {

		String queryStr = "SELECT I2.AID,I2.INTV_S_UMIDL,I2.INTV_S_UMIDH,I2.INTV_OPER_NICKNAME,I2.INTV_DATE_TIME,I2.INTV_MERGED_TEXT,I2.INTV_INST_NUM,I2.INTV_SEQ_NBR" + "" + " FROM RINTV I2 WHERE (I2.AID,I2.INTV_S_UMIDH,I2.INTV_S_UMIDL) in ("
				+ primaryKeys + ") AND I2.INTV_SEQ_NBR IN (" + intvSeqNumber + ") AND INTV_INTY_CATEGORY='INTY_ROUTING' " + "  ORDER BY I2.INTV_SEQ_NBR ";

		List<EventData> interventionsEvents = jdbcTemplate.query(queryStr, new RowMapper<EventData>() {

			@Override
			public EventData mapRow(ResultSet rs, int rowNum) throws SQLException {
				StringBuilder primaryKey = new StringBuilder();
				primaryKey.append("RINTV_").append(rs.getString("INTV_INST_NUM")).append("_").append(rs.getString("INTV_SEQ_NBR")).append("_(").append(rs.getString("AID")).append(',').append(rs.getString("INTV_S_UMIDH")).append(',')
						.append(rs.getString("INTV_S_UMIDL")).append(')');

				EventData eventData = eventsMap.get(primaryKey.toString());
				if (eventData == null) {
					LOGGER.warn("(fetchInterventionDetails) message with this id   " + primaryKey.toString() + "  dose not have eventData ( eventData = null) ");
				} else {
					eventData.setMessage(new Message());
					// getting the INTV_MERGED_TEXT to update the message in eventData
					String intervText = rs.getString("INTV_MERGED_TEXT");
					eventData.getMessage().setMergedText(intervText);
					String routingQueueName = NotifierUtil.getRoutingPointStatus(eventData).orElse("");
					if (StringUtils.isEmpty(routingQueueName)) {
						eventData.setMessage(null);
						return eventData;
					}
					String networkStatusFromIntervintion = NotifierUtil.getNackedFromIntervintionText(eventData.getMessage().getMergedText()).orElse("");
					eventData.getMessage().setIntervintionNetworkStatus(networkStatusFromIntervintion);
					eventData.getMessage().setOperatorNickName(rs.getString("INTV_OPER_NICKNAME"));
					eventData.getMessage().setInstanceNumber(rs.getString("INTV_INST_NUM"));
					eventData.getMessage().setSequenceNumber(rs.getLong("INTV_SEQ_NBR"));
					eventData.getMessage().setQuqueStatus(routingQueueName);
					if (rs.getTimestamp("INTV_DATE_TIME") != null) {
						eventData.getMessage().setInterventionDateTime(rs.getTimestamp("INTV_DATE_TIME").toLocalDateTime());
					}
				}
				return eventData;
			}
		});
		if (interventionsEvents == null || interventionsEvents.isEmpty()) {
			return null;
		}
		List<EventData> removableEvent = eventsMap.values().stream().filter(e -> e.getMessage() == null).collect(Collectors.toList());
		if (!removableEvent.isEmpty())
			updateEvents(removableEvent, MessageStatus.SENT.getStatus());

		return eventsMap.values().stream().filter(e -> e.getMessage() != null).collect(Collectors.toList());
	}

	public void bulkInsertEventHistory(final List<EventData> eventMetadataList) {

		String insertStatemnt = "INSERT INTO " + defaultSchema + '.'
				+ " RUPDATENOTIFIERHISTORY (SEQ_NUM,AID,S_UMIDL,S_UMIDH,INST_NUM,DATE_TIME,SEQ_NBR,TABLE_NAME,OPERATION_MODE,INSERTION_DATE_TIME,XML_EVENT_DATA) VALUES (SEQ_UPDATENOTIFIER_HISTORY_ID.nextVal,?,?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				EventData eventData = eventMetadataList.get(index);
				// preparedStatement.setString(1, eventData.getSequenceNumber());
				preparedStatement.setInt(1, eventData.getPrimaryKey().getAid());
				preparedStatement.setString(2, eventData.getPrimaryKey().getUmidl());
				preparedStatement.setString(3, eventData.getPrimaryKey().getUmidh());
				preparedStatement.setString(4, eventData.getInstanceNumber());
				if (eventData.getDateTime() != null) {
					preparedStatement.setTimestamp(5, Timestamp.valueOf(eventData.getDateTime()));
				} else {
					preparedStatement.setTimestamp(5, null);
				}
				preparedStatement.setString(6, eventData.getInterventionSeqNumber());
				preparedStatement.setString(7, eventData.getTableName());
				preparedStatement.setInt(8, eventData.getOperationType());
				preparedStatement.setTimestamp(9, Timestamp.valueOf(eventData.getDateTime()));
				Reader reader = new StringReader(eventData.getCreatedMessageEvent());
				preparedStatement.setClob(10, reader);

			}

			@Override
			public int getBatchSize() {
				return eventMetadataList.size();
			}
		});

	}

	@Override
	public boolean removeSentMessages() {
		// Status = 1 means these messages sent successfully
		String deleteStatement = "DELETE FROM " + defaultSchema + '.' + "RUPDATENOTIFIER WHERE STATUS = 1 ";
		int value = jdbcTemplate.update(deleteStatement);
		if (value > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateEvents(List<EventData> eventdataList, int status) {

		String interventionSeqNumberString = buildInterventionSeqNumberString(eventdataList);

		String UpdateStatement = "UPDATE " + defaultSchema + '.' + "RUPDATENOTIFIER SET STATUS = ? WHERE " + "(AID,S_UMIDL,S_UMIDH,TABLE_NAME) in (" + buildCompsiteKeyString(eventdataList) + ") AND SEQ_NBR in (" + interventionSeqNumberString + ") ";

		int value = jdbcTemplate.update(UpdateStatement, new Object[] { status });
		if (value > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteNonIntervDataFromRUpdateNotifier() {
		String UpdateStatement = "DELETE FROM " + defaultSchema + '.' + "RUPDATENOTIFIER  WHERE TABLE_NAME!='RINTV' ";
		int value = jdbcTemplate.update(UpdateStatement);
		if (value > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void changeProcessingToNewEvents() {
		String updateStatement = "UPDATE " + defaultSchema + '.' + "RUPDATENOTIFIER SET STATUS = 0 WHERE STATUS=3";
		jdbcTemplate.update(updateStatement);
	}

	/**
	 * A convenience method to insert all sent event for tracking purpose
	 * 
	 * @since 21/04/2020
	 * @author AHammad
	 * @param eventObserverList
	 */
	@Override
	public void bulkInsertNotifierEventObserver(List<NotifierEventObserver> eventObserverList) {
		LOGGER.info("start to insert sent Notified event ");
		String insertStatemnt = "INSERT INTO " + defaultSchema + '.' + " RNOTIFIER_DATA_OBSERVER (ID,AID,S_UMIDL,S_UMIDH,INST_NUM,ROUTING_POINT_NAME,QUEUE_STATUS) VALUES (SEQ_PK_RNOTIFIER_ID.nextval,?,?,?,?,?,?)";

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				NotifierEventObserver eventObserver = eventObserverList.get(index);
				preparedStatement.setInt(1, eventObserver.getMsgPrimaryKey().getAid());
				preparedStatement.setString(2, eventObserver.getMsgPrimaryKey().getUmidl());
				preparedStatement.setString(3, eventObserver.getMsgPrimaryKey().getUmidh());
				preparedStatement.setString(4, eventObserver.getInstanceSequanceNumber());
				preparedStatement.setString(5, eventObserver.getRoutingPointName());
				preparedStatement.setString(6, eventObserver.getQueueStatus());
			}

			@Override
			public int getBatchSize() {
				return eventObserverList.size();
			}
		});

	}

	/**
	 * A convenience method to fetch all tracked message to check for the routing point of the message
	 * 
	 * @since 21/04/2020
	 * @author AHammad
	 * @param primaryKey
	 * @return all sent messages
	 */
	@Override
	public void getAllRoutingPointByMsgPrimaryKey() {
		LOGGER.info("start fetch all notified messages from observer ");
		String query = "SELECT * FROM " + defaultSchema + '.' + "RNOTIFIER_DATA_OBSERVER WHERE Queue_Status= '" + NotifierUtil.LIVE_MESSAGE_STATUS + "'";

		List<NotifierEventObserver> allNotifiedMsgs = jdbcTemplate.query(query, (rs, rowNum) -> {

			NotifierEventObserver notifiedEvent = new NotifierEventObserver();
			notifiedEvent.setId(rs.getLong("ID"));
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setAid(rs.getInt("AID"));
			primaryKey.setUmidl(rs.getString("S_UMIDL"));
			primaryKey.setUmidh(rs.getString("S_UMIDH"));
			notifiedEvent.setMsgPrimaryKey(primaryKey);
			notifiedEvent.setRoutingPointName(rs.getString("ROUTING_POINT_NAME"));
			notifiedEvent.setInstanceSequanceNumber(rs.getString("INST_NUM"));
			notifiedEvent.setPrimaryKeyWithInstNum(notifiedEvent.getMsgPrimaryKey().toString() + notifiedEvent.getInstanceSequanceNumber());
			return notifiedEvent;
		});

		Map<String, List<NotifierEventObserver>> eventsByPrimaryKeyWithIstNumber = allNotifiedMsgs.stream().collect(Collectors.groupingBy(NotifierEventObserver::getPrimaryKeyWithInstNum, Collectors.toList()));
		notifiedEventsMap.putAll(eventsByPrimaryKeyWithIstNumber.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(NotifierEventObserver::getRoutingPointName).collect(Collectors.toList()))));
		int size = CollectionUtils.isEmpty(allNotifiedMsgs) ? 0 : allNotifiedMsgs.size();
		LOGGER.trace("fetched messages size " + size);
	}

	/**
	 * A convenience method to remove all messages that have status completed from the tracking table
	 * 
	 * @since 21/04/2020
	 * @author AHammad
	 * @param eventObserverList
	 * @return true if removed otherwise false
	 */
	@Override
	public boolean removetNotifierEventObserver(int aid, int umidl, int umidh) {
		LOGGER.trace("prepare to delete from the notified table");
		int value = 0;
		try {
			String deleteQuery = "DELETE FROM " + defaultSchema + '.' + "RNOTIFIER_DATA_OBSERVER WHERE AID = ?  and S_UMIDL = ? and S_UMIDH=?";
			LOGGER.trace("start to excute delete for completed messages ");
			value = jdbcTemplate.update(deleteQuery, new Object[] { aid, umidl, umidh });
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (value > 0) {
			return true;
		}
		return false;

	}

	public boolean upadteNotifierStatusBeforePullMessages() {
		String UpdateStatement = "UPDATE " + defaultSchema + '.' + "RUPDATENOTIFIER SET STATUS = ?  WHERE " + " STATUS = 3";
		// jdbcTemplate.batchUpdate(UpdateStatement, new BatchPreparedStatementSetter() {
		//
		// public void setValues(PreparedStatement ps, int i) throws SQLException {
		// ps.setLong(1, MessageStatus.NEW.getStatus());
		//
		// }
		//
		// public int getBatchSize() {
		// return getBatchSize();
		// }
		//
		// });
		int value = jdbcTemplate.update(UpdateStatement, new Object[] { MessageStatus.NEW.getStatus() });
		if (value > 0) {
			return true;
		}
		return false;

	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	public LockReadEventsProcedure getLockReadEventsProcedure() {
		return lockReadEventsProcedure;
	}

	public void setLockReadEventsProcedure(LockReadEventsProcedure lockReadEventsProcedure) {
		this.lockReadEventsProcedure = lockReadEventsProcedure;
	}

}
