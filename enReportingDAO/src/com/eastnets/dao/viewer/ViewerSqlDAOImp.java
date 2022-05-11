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

package com.eastnets.dao.viewer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.DetailsHistory;
import com.eastnets.domain.viewer.GpiConfirmation;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageNote;
import com.eastnets.domain.viewer.MessageSearchTemplate;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.utils.Utils;

public class ViewerSqlDAOImp extends ViewerDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2437797497301629700L;

	@Override
	public void addNewMsgSearchTemplate(MessageSearchTemplate template) throws Exception {

		String insertQuery = " INSERT INTO rMsgSearchTemplates(name,template_value,creation_date,created_by ) " + " VALUES (?,?, getDate() , ?)";

		jdbcTemplate.update(insertQuery, new Object[] { template.getName(), template.getTemplateValue(), template.getCreatedBy().getUserId() });
		// Template is mapped to group
		mapNewMsgSearchTemplateToProfile(template);
	}

	@Override
	public void mapNewMsgSearchTemplateToProfile(MessageSearchTemplate savedTemplate) throws Exception {

		List<MessageSearchTemplate> templates = filterMsgSearchTemplates(savedTemplate.getName(), 0);
		MessageSearchTemplate template = templates.get(0);
		// get profile id, in case of profile migration take it from savedTemplate
		long profileId = savedTemplate.getProfileId() > 0 ? savedTemplate.getProfileId() : savedTemplate.getCreatedBy().getProfile().getGroupId();
		if (template != null) {
			String insertQuery = " INSERT INTO rMsgSearchProfileTemplate(template_id,group_id) " + " VALUES (?,?)";

			jdbcTemplate.update(insertQuery, new Object[] { template.getId(), profileId });
		}
	}

	@Override
	public MessageNote addNewMessageNote(MessageNote messageNote) {
		String insertQuery = "SET IDENTITY_INSERT RMESG_NOTES ON INSERT INTO RMESG_NOTES(NOTE_ID,AID,MESG_S_UMIDL,MESG_S_UMIDH,NOTE,CREATION_DATE,CREATED_BY) VALUES(?,?,?,?,?,?,?) SET IDENTITY_INSERT RMESG_NOTES OFF";

		String generatedKeyQuery = "SELECT IDENT_CURRENT('dbo.RMESG_NOTES') +1 ID";

		List<Long> ids = jdbcTemplate.query(generatedKeyQuery, new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long id = rs.getLong("ID");
				return id;
			}
		});

		jdbcTemplate.update(insertQuery, ids.get(0), messageNote.getAid(), messageNote.getMesgUmidl(), messageNote.getMesgUmidh(), messageNote.getNote(), messageNote.getCreationDate(), messageNote.getCreatedBy().getUserId());
		messageNote.setNoteId(ids.get(0));
		return messageNote;
	}

	@Override
	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg1, String errMsg2) {

		String insertQuery = "INSERT INTO ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, new Object[] { errorExe, new java.util.Date(), errorLevel, errorModule, errMsg1, "" });
	}

	@Override
	public List<NotifierMessage> getPendingGpiMesg(String query, int mailAtempt, int confirmAtempt, int timeInterval) throws InterruptedException, SQLException {
		int timeBefore = (-timeInterval);
		Object[] parameters = new Object[] { mailAtempt, confirmAtempt, timeBefore };

		List<NotifierMessage> gpiMessagePending = jdbcTemplate.query(query, parameters, new RowMapper<NotifierMessage>() {
			@Override
			public NotifierMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotifierMessage notifierMessage = new NotifierMessage();
				notifierMessage.setAid((rs.getInt("aid")));
				notifierMessage.setMesgUmidl(rs.getInt("mesg_s_umidl"));
				notifierMessage.setMesgUmidh(rs.getInt("mesg_s_umidh"));
				if (rs.getTimestamp("mesg_crea_date_time") != null) {
					notifierMessage.setMesgCreaDateTime(new Date(rs.getTimestamp("mesg_crea_date_time").getTime()));
				}

				if (rs.getTimestamp("Mail_DATE") != null) {
					notifierMessage.setMailAttemptsDate(new Date(rs.getTimestamp("Mail_DATE").getTime()));
				}

				if (rs.getTimestamp("Confirma_DATE") != null) {
					notifierMessage.setConfirmAttemptsDate(new Date(rs.getTimestamp("Confirma_DATE").getTime()));
				}

				notifierMessage.setMailAtempt(rs.getInt("mailAttempts"));
				notifierMessage.setConfirmAtempt(rs.getInt("confirmAttempts"));
				notifierMessage.setQueueName(rs.getString("inst_rp_name"));
				notifierMessage.setMesgSenderSwiftAddress(rs.getString("mesg_sender_swift_address"));
				notifierMessage.setMesgSenderX1(rs.getString("mesg_sender_X1"));
				notifierMessage.setxFinCcy(rs.getString("x_fin_ccy"));
				notifierMessage.setDataSource(DataSource.SAA);

				return notifierMessage;
			}

		});

		return gpiMessagePending;
	}

	private static String acountNumberQuery(String acountNumber) {
		return (acountNumber != null && !acountNumber.isEmpty()) ? " and (MESG_ORDER_CUS like" + getAcountNumber(acountNumber) + "or MESG_BEN_CUST like " + getAcountNumber(acountNumber) + " )" : "";
	}

	private static String getAcountNumber(String acountNumber) {

		return "'%" + acountNumber + "%'";
	}

	@Override
	public MessageKey getMessageKeyByUetr(String uetr, String acountNumber, String msgDirection, boolean isViewerTraker, String fromDate, String toDate) throws SQLException {
		if (msgDirection == null || msgDirection.isEmpty()) {
			msgDirection = "%input%";
		}
		String query = "";
		List<MessageKey> gpiMessageKeys = null;
		if (isViewerTraker) {
			query = "select aid, mesg_s_umidh, mesg_s_umidl, mesg_crea_date_time from rmesg  " + " where    MESG_E2E_TRANSACTION_REFERENCE = ? and mesg_type = '103' " + " order by mesg_crea_date_time ";
			gpiMessageKeys = jdbcTemplate.query(query, new Object[] { uetr }, new RowMapper<MessageKey>() {

				@Override
				public MessageKey mapRow(ResultSet rs, int rowNum) throws SQLException {
					MessageKey messageKey = new MessageKey();
					messageKey.setAid((rs.getInt("aid")));
					messageKey.setUnidl(rs.getInt("mesg_s_umidl"));
					messageKey.setUmidh(rs.getInt("mesg_s_umidh"));
					messageKey.setMesgCreaDate(rs.getDate("mesg_crea_date_time"));
					return messageKey;
				}
			});
		} else {
			String accountNumberQuerey = acountNumberQuery(acountNumber);
			if (accountNumberQuerey.isEmpty()) {
				return null;
			}

			query = "select aid, mesg_s_umidh, mesg_s_umidl, mesg_crea_date_time from rmesg  " + " where mesg_crea_date_time between ? and ? and  MESG_E2E_TRANSACTION_REFERENCE = ? and mesg_type = '103' and  UPPER(mesg_sub_format) like ?  "
					+ accountNumberQuerey + " order by mesg_crea_date_time ";

			gpiMessageKeys = jdbcTemplate.query(query, new Object[] { fromDate, toDate, uetr, msgDirection.toUpperCase() }, new RowMapper<MessageKey>() {

				@Override
				public MessageKey mapRow(ResultSet rs, int rowNum) throws SQLException {
					MessageKey messageKey = new MessageKey();
					messageKey.setAid((rs.getInt("aid")));
					messageKey.setUnidl(rs.getInt("mesg_s_umidl"));
					messageKey.setUmidh(rs.getInt("mesg_s_umidh"));
					messageKey.setMesgCreaDate(rs.getDate("mesg_crea_date_time"));
					return messageKey;
				}
			});
		}

		if (gpiMessageKeys != null && !gpiMessageKeys.isEmpty())
			return gpiMessageKeys.get(0);

		return null;
	}

	@Override
	public List<GpiConfirmation> getGpiAgent(String senderBic, String uter, final int timeZoneOffset, List<String> selectedSAA, boolean gSRPRequest, Date mesgCreaDateTime, String... mesgType) {
		String arr[] = mesgType;
		String identifiersQuery = "SELECT * from rMesg where " + getMTConfirmationType(Arrays.asList(arr)) + " and mesg_e2e_transaction_reference= ?  and mesg_frmt_name='Swift'  and  mesg_crea_date_time >= ?  " + " and MESG_SLA in('001','002') "
				+ " and MESG_PARSING_DATE_TIME is not null   " + addSenderIfExist(senderBic, gSRPRequest) + " order by mesg_s_umidh desc";

		List<GpiConfirmation> gpiConfirmationList = jdbcTemplate.query(identifiersQuery, new Object[] { uter, mesgCreaDateTime }, new RowMapper<GpiConfirmation>() {
			public GpiConfirmation mapRow(ResultSet rs, int rowNum) throws SQLException {
				GpiConfirmation gpiConfirmation = new GpiConfirmation();
				gpiConfirmation.setAid(rs.getInt("aid"));
				gpiConfirmation.setMesgUmidl(rs.getInt("mesg_s_umidl"));
				gpiConfirmation.setMesgUmidh(rs.getInt("mesg_s_umidh"));
				gpiConfirmation.setTimeZoneOffset(timeZoneOffset);
				Date dt = null;
				if (rs.getTimestamp("mesg_crea_date_time") != null) {
					dt = new Date(rs.getTimestamp("mesg_crea_date_time").getTime());
				}
				gpiConfirmation.setMesgCreaDateTime(dt);
				gpiConfirmation.setMesgType(rs.getString("mesg_type"));
				if (gpiConfirmation.getMesgType().equalsIgnoreCase("202") || gpiConfirmation.getMesgType().equalsIgnoreCase("205")) {
					return gpiConfirmation;
				}
				gpiConfirmation.setStatusCode(rs.getString("MESG_Status_code"));
				gpiConfirmation.setReasonCode(rs.getString("MESG_Reason_code"));
				gpiConfirmation.setForwardedTo(rs.getString("MESG_forwarded_to"));
				gpiConfirmation.setStatusOriginator(rs.getString("MESG_status_originator"));
				gpiConfirmation.setMsgSendAmount(rs.getString("MESG_SND_CHARGES_AMOUNT"));
				gpiConfirmation.setMsgSendCurr(rs.getString("MESG_SND_CHARGES_CURR"));

				// To Handle When charges are OUR --> they are not displayed in tracker because it always takes 0 as value (I think even though they don't affect the amount they should be displayed).
				if ((gpiConfirmation.getMsgSendAmount() == null || gpiConfirmation.getMsgSendAmount().equalsIgnoreCase("0")) && (gpiConfirmation.getMsgSendCurr() == null || gpiConfirmation.getMsgSendCurr().isEmpty())) {
					// re-Fill value for RCV
					gpiConfirmation.setMsgSendAmount(rs.getString("MESG_RCV_CHARGES_AMOUNT"));
					gpiConfirmation.setMsgSendCurr(rs.getString("MESG_RCV_CHARGES_CURR"));
				}

				if (gpiConfirmation.getMsgSendAmount() != null && !gpiConfirmation.getMsgSendAmount().isEmpty()) {
					gpiConfirmation.setMsgSendAmount(Utils.formatAmount(new BigDecimal(gpiConfirmation.getMsgSendAmount().trim()), gpiConfirmation.getMsgSendCurr(), getCurrenciesMap()).toString());
				}

				gpiConfirmation.setNakCode(rs.getString("MESG_NAK_code"));
				if (rs.getString("NOTIF_DATE_TIME") != null) {
					gpiConfirmation.setSendDate(convertNotifyDataToCuruntZone((rs.getString("NOTIF_DATE_TIME"))));
				}
				gpiConfirmation.setMsgTrnRef(rs.getString("MESG_REL_TRN_REF"));
				gpiConfirmation.setCreditedAmount(rs.getString("Confirmation_Amount"));
				gpiConfirmation.setCreditedCur(rs.getString("Confirmation_CCY"));
				gpiConfirmation.setMesgExchangeRate(rs.getString("MESG_EXCHANGE_RATE"));
				gpiConfirmation.setMesgSubFormat(rs.getString("MESG_sub_format"));
				gpiConfirmation.setMesgSla(rs.getString("MESG_SLA"));
				gpiConfirmation.setExChangeRateFromCcy(rs.getString("EXCH_RATE_FROM_CCY"));
				gpiConfirmation.setExChangeRateToCcy(rs.getString("EXCH_RATE_TO_CCY"));
				gpiConfirmation.setMesgCharges(rs.getString("MESG_CHARGES"));
				return gpiConfirmation;
			}
		});

		// gpiConfirmationList.remove(gpiConfirmationList.size()-1);

		/*
		 * GpiConfirmation agent=new GpiConfirmation(); agent.setStatusCode("ACSP"); agent.setReasonCode("000"); agent.setForwardedTo("CABOGB2LXXX"); agent.setStatusOriginator("new Agent");
		 * agent.setNakCode("XXXXXXXXX"); gpiConfirmationList.add(agent);
		 * 
		 * GpiConfirmation agent3=new GpiConfirmation(); agent3.setStatusCode("ACCC"); agent3.setReasonCode("000"); agent3.setForwardedTo(""); agent3.setStatusOriginator("CABOGB2LXXX");
		 * agent3.setNakCode("XXXXXXXXX"); gpiConfirmationList.add(agent3);
		 * 
		 * 
		 * GpiConfirmation agent4=new GpiConfirmation(); agent4.setStatusCode("ACCC"); agent4.setReasonCode("000"); agent4.setForwardedTo(""); agent4.setStatusOriginator("CABOGB2LXXX");
		 * agent4.setNakCode("XXXXXXXXX"); gpiConfirmationList.add(agent4);
		 * 
		 * GpiConfirmation agent2=new GpiConfirmation(); agent2.setStatusCode("ACSP"); agent2.setReasonCode("002"); agent2.setForwardedTo(""); agent2.setStatusOriginator("CABOGB2LXXX");
		 * agent2.setNakCode("XXXXXXXXX"); gpiConfirmationList.add(agent2);
		 * 
		 * GpiConfirmation agent3=new GpiConfirmation(); agent3.setStatusCode("ACSP"); agent3.setReasonCode("001"); agent3.setForwardedTo(""); agent3.setStatusOriginator("CABOGB2LXXX");
		 * agent3.setNakCode("XXXXXXXXX"); gpiConfirmationList.add(agent3);
		 * 
		 * GpiConfirmation agent2=new GpiConfirmation(); agent2.setStatusCode("ACSP"); agent2.setReasonCode("000"); agent2.setForwardedTo("XXX"); agent2.setStatusOriginator("new Agent");
		 * agent2.setNakCode("XXXXXXXXX");
		 * 
		 * GpiConfirmation agent3=new GpiConfirmation(); agent3.setStatusCode("ACSP"); agent3.setReasonCode("001"); agent3.setForwardedTo("CABOGB2LXXX"); agent3.setStatusOriginator("XXX");
		 * agent3.setNakCode("XXXXXXXXX");
		 * 
		 * gpiConfirmationList.add(agent2); gpiConfirmationList.add(agent3);
		 */
		return gpiConfirmationList;
	}

	@Override
	public List<DetailsHistory> getGpiDetailsHistory(String uter, java.util.Date mesg_crea_date) throws SQLException {
		String query = " select * from rmesg  where MESG_E2E_TRANSACTION_REFERENCE = ?  and  mesg_crea_date_time >= ?   " + "and mesg_type in ('205','103','199','299','202','192','196') order by mesg_s_umidh ASC";

		List<DetailsHistory> gpiDetelsHistoryList = jdbcTemplate.query(query, new Object[] { uter, mesg_crea_date }, new RowMapper<DetailsHistory>() {
			public DetailsHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				DetailsHistory detailsHistory = new DetailsHistory();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss 'GMT'X");
				detailsHistory.setMsgType(rs.getString("mesg_type"));
				detailsHistory.setAid(rs.getInt("AID"));
				detailsHistory.setMesgUmidh(rs.getInt("MESG_S_UMIDH"));
				detailsHistory.setMesgUmidl(rs.getInt("MESG_S_UMIDL"));
				Date dt = null;
				if (rs.getTimestamp("mesg_crea_date_time") != null) {
					dt = new Date(rs.getTimestamp("mesg_crea_date_time").getTime());
				}
				detailsHistory.setMesgCreaDateTime(dt);

				if (rs.getString("NOTIF_DATE_TIME") != null) {
					detailsHistory.setNotifDateTime(convertNotifyDataToCuruntZone((rs.getString("NOTIF_DATE_TIME"))));
				}
				// if (rs.getString("mesg_type").contains("103")) {

				if (rs.getString("Confirmation_Amount") != null) {
					detailsHistory.setConfirmationAmount(rs.getString("Confirmation_Amount"));
				}
				if (rs.getString("Confirmation_CCY") != null) {
					detailsHistory.setConfirmationCCY(rs.getString("Confirmation_CCY"));
				}
				if (rs.getString("EXCH_RATE_FROM_CCY") != null) {
					detailsHistory.setExChangeRateFromCcy(rs.getString("EXCH_RATE_FROM_CCY"));
				}
				if (rs.getString("EXCH_RATE_TO_CCY") != null) {
					detailsHistory.setExChangeRateToCcy(rs.getString("EXCH_RATE_TO_CCY"));
				}
				if (rs.getString("NOTIF_DATE_TIME") != null & !rs.getString("mesg_type").contains("103")) {
					detailsHistory.setTimeZone(rs.getString("NOTIF_DATE_TIME"));
				}
				if (rs.getString("mesg_type").contains("103")) {
					SimpleDateFormat SwiftTimeZone = new SimpleDateFormat("X");
					detailsHistory.setTimeZone(SwiftTimeZone.format(new java.util.Date()) + "");
				}
				// }

				if (rs.getString("MESG_SLA") != null && !rs.getString("MESG_SLA").contains("002")) {
					if ((rs.getString("mesg_type").contains("199")) || rs.getString("mesg_type").contains("299")) {
						detailsHistory.setStatusCode(rs.getString("MESG_Status_code"));
						detailsHistory.setReasonCode(rs.getString("MESG_Reason_code"));
						detailsHistory.setStatusDescription(detailsHistory.getstatusDesc(rs.getString("MESG_Status_code"), rs.getString("MESG_Reason_code"), rs.getString("mesg_type")));

						detailsHistory.setSenderBic(rs.getString("MESG_status_originator"));
						detailsHistory.setReceiverBic(rs.getString("MESG_forwarded_to"));
					}
					detailsHistory.setSenderDeducts(rs.getString("MESG_SND_CHARGES_AMOUNT"));
					detailsHistory.setSenderDeductsCur(rs.getString("MESG_SND_CHARGES_CURR"));

					if ((detailsHistory.getSenderDeducts() == null || detailsHistory.getSenderDeducts().equalsIgnoreCase("0")) && (detailsHistory.getSenderDeductsCur() == null || detailsHistory.getSenderDeductsCur().isEmpty())) {
						// re-Fill value for RCV
						detailsHistory.setSenderDeducts(rs.getString("MESG_RCV_CHARGES_AMOUNT"));
						detailsHistory.setSenderDeductsCur(rs.getString("MESG_RCV_CHARGES_CURR"));
					}
				}
				if ((rs.getString("mesg_type").contains("199")) || rs.getString("mesg_type").contains("299")) {
					detailsHistory.setSettlementMethod(rs.getString("MESG_Settlement_Method"));
					detailsHistory.setClearingSystem(rs.getString("MESG_Clearing_System"));
				}

				if (detailsHistory.getSenderDeducts() != null && !detailsHistory.getSenderDeducts().isEmpty()) {
					String chargesLine = detailsHistory.getSenderDeducts();
					String currLine = detailsHistory.getSenderDeductsCur();
					String[] charges;
					String[] currs;
					if (chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						currs = currLine.split(",");
						detailsHistory.setSenderDeducts(Utils.formatAmount(new BigDecimal(charges[charges.length - 1].trim()), currs[currs.length - 1], getCurrenciesMap()).toString());
						detailsHistory.setSenderDeductsCur(currs[currs.length - 1]);
					} else {
						detailsHistory.setSenderDeducts(Utils.formatAmount(new BigDecimal(detailsHistory.getSenderDeducts().trim()), detailsHistory.getSenderDeductsCur(), getCurrenciesMap()).toString());
					}
				}

				if (detailsHistory.getMesgCreaDateTime() != null) {
					detailsHistory.setMesgCreaDateTimeStr(sdf.format(detailsHistory.getMesgCreaDateTime()));

				}

				if (rs.getString("mesg_type").contains("103") || rs.getString("mesg_type").contains("202") || rs.getString("mesg_type").contains("205")) {

					detailsHistory.setSenderBic(rs.getString("mesg_sender_X1"));
					detailsHistory.setReceiverBic(rs.getString("x_receiver_X1"));

				}

				if (rs.getString("MESG_SLA") != null && rs.getString("MESG_SLA").contains("002")) {

					if (rs.getString("mesg_type").contains("192")) {
						detailsHistory.setSenderBic(rs.getString("mesg_sender_X1"));
						detailsHistory.setReceiverBic(rs.getString("x_receiver_X1"));
						detailsHistory.setReasonCode(rs.getString("MESG_Reason_code"));

					} else if (rs.getString("mesg_type").contains("199") || rs.getString("mesg_type").contains("196")) {
						if (rs.getString("MESG_status_originator") == null || rs.getString("MESG_status_originator").isEmpty()) {
							detailsHistory.setSenderBic(rs.getString("mesg_sender_X1"));
							detailsHistory.setReceiverBic(rs.getString("x_receiver_X1"));
						} else {
							detailsHistory.setSenderBic(rs.getString("MESG_status_originator"));
							detailsHistory.setReceiverBic(rs.getString("MESG_forwarded_to"));
						}
						detailsHistory.setStatusCode(rs.getString("MESG_Status_code"));
						detailsHistory.setReasonCode(rs.getString("MESG_Reason_code"));
					}
					detailsHistory.setStatusDescription(detailsHistory.getstatusDesc(rs.getString("MESG_Status_code"), rs.getString("MESG_Reason_code"), rs.getString("mesg_type")));
				}

				if (rs.getString("SENDER_TO_RECEIVER") != null && rs.getString("SENDER_TO_RECEIVER").equals("RETN")) {
					detailsHistory.setStatusDescription("The Payment was returned");
				}

				detailsHistory.setAid(rs.getInt("AID"));
				detailsHistory.setMesgUmidh(rs.getInt("MESG_S_UMIDH"));
				detailsHistory.setMesgUmidl(rs.getInt("MESG_S_UMIDL"));
				detailsHistory.setMesgSubFormat(rs.getString("MESG_sub_format"));
				if (detailsHistory.getMsgType() != null && detailsHistory.getMsgType().equalsIgnoreCase("103")) {
					detailsHistory.setMesgTrnRef(rs.getString("MESG_TRN_REF"));
				} else {
					detailsHistory.setMesgTrnRef(rs.getString("MESG_REL_TRN_REF"));
				}

				detailsHistory.setMesgCopyServiceId(rs.getString("MESG_COPY_SERVICE_ID"));
				detailsHistory.setOrdringInstution(rs.getString("MESG_ORDERING_INST"));
				detailsHistory.setMesgExchangeRate(rs.getString("MESG_EXCHANGE_RATE"));
				detailsHistory.setMesgNakedCode(rs.getString("MESG_NAK_CODE"));
				// detailsHistory.setExChangeRateFromCcy(rs.getString("EXCH_RATE_FROM_CCY"));
				// detailsHistory.setExChangeRateToCcy(rs.getString("EXCH_RATE_TO_CCY"));
				// detailsHistory.setSbInstructedCurr(rs.getString("SB_INSTR_CURR"));
				detailsHistory.setAllDeducts(rs.getString("ALL_DEDUCTS"));
				detailsHistory.setMesgCharges(rs.getString("MESG_CHARGES"));
				detailsHistory.setOrginlaSender(rs.getString("mesg_sender_X1"));
				return detailsHistory;
			}
		});

		return gpiDetelsHistoryList;
	}

	@Override
	public CorrInfo getBicInfo(String bic) {
		try {
			String query = "SELECT Top 1  corr_institution_name,  corr_branch_info, corr_city_name,  corr_ctry_code,   corr_ctry_name, " + "  corr_location FROM   rcorr WHERE   corr_type = 'CORR_TYPE_INSTITUTION'  AND corr_x1 LIKE ?";
			List<CorrInfo> listOfcorrespondent = new ArrayList<>();
			listOfcorrespondent = jdbcTemplate.query(query, new Object[] { "%" + bic + "%" }, new RowMapper<CorrInfo>() {
				public CorrInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CorrInfo corrInfo = new CorrInfo();
					corrInfo.setCorrInstitutionName(rs.getString("corr_institution_name"));
					corrInfo.setCorrCtryName(rs.getString("corr_ctry_name"));
					corrInfo.setCorrCityName(rs.getString("corr_city_name"));

					return corrInfo;
				}
			});
			if (listOfcorrespondent != null && !listOfcorrespondent.isEmpty())
				return listOfcorrespondent.get(0);

		} catch (Exception e) {
			return null;
		}
		return null;
	}

	@Override
	public List<MessageKey> getMessagesKeys(int aid, boolean isPartitionedDatabase, String fromDate, String toDate, int bulkSize) {

		String query = "select Top (?)   m.aid, mesg_s_umidh, mesg_s_umidl,mesg_sender_X1 ,mesg_receiver_swift_address,X_RECEIVER_X1,mesg_crea_date_time, mesg_type, mesg_identifier, mesg_mod_date_time,MESG_E2E_TRANSACTION_REFERENCE ,MESG_SUB_FORMAT, MESG_SLA, m.MESG_NAK_CODE   "
				+ " from rMesg m  "
				+ " where m.aid = ? and mesg_frmt_name = 'Swift' and ( ((mesg_type='199' or mesg_type='299' or mesg_type = '192' or mesg_type='196') and MESG_E2E_TRANSACTION_REFERENCE is not null and (MESG_SLA='001' or MESG_SLA='002'))"
				+ " or (mesg_type='103') or (mesg_type='202') or  (mesg_type='205') )" + " and (mesg_parsing_date_time is null or mesg_parsing_date_time < mesg_mod_date_time ) and archived = 0 " + " and m.mesg_crea_date_time BETWEEN ?  AND ?  "
				+ " order by mesg_crea_date_time ";
		List<MessageKey> gpiMessageKeys = jdbcTemplate.query(query, new Object[] { bulkSize, aid, fromDate, toDate }, new RowMapper<MessageKey>() {

			@Override
			public MessageKey mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageKey messageKey = new MessageKey();
				messageKey.setAid((rs.getInt("aid")));
				messageKey.setUnidl(rs.getInt("mesg_s_umidl"));
				messageKey.setUmidh(rs.getInt("mesg_s_umidh"));
				messageKey.setMesgType(rs.getString("mesg_type"));
				messageKey.setMesgIdentifier(rs.getString("mesg_identifier"));
				messageKey.setMesgCreaDate(rs.getDate("mesg_crea_date_time"));
				messageKey.setMesgCraetionUtil(rs.getTimestamp("mesg_crea_date_time"));
				messageKey.setMesgModFDate(rs.getTimestamp("mesg_mod_date_time"));
				messageKey.setUetr(rs.getString("MESG_E2E_TRANSACTION_REFERENCE"));
				messageKey.setMesgSLA(rs.getString("MESG_SLA"));
				messageKey.setAppeNacReason(rs.getString("MESG_NAK_CODE"));
				messageKey.setMesgSubFormat(rs.getString("MESG_SUB_FORMAT"));
				messageKey.setMesgSender(rs.getString("mesg_sender_X1"));
				messageKey.setMesgReciverX1(rs.getString("X_RECEIVER_X1"));
				messageKey.setMesgSwiftAdrres(rs.getString("mesg_receiver_swift_address"));
				return messageKey;
			}

		});

		return gpiMessageKeys;
	}

	public static void main(String[] args) {
		String query = "select Top (?) *  " + " from rMesg m  "
				+ " where m.aid = ? and mesg_frmt_name = 'Swift' and ( ((mesg_type='199' or mesg_type='299' or mesg_type = '192' or mesg_type='196') and MESG_E2E_TRANSACTION_REFERENCE is not null and (MESG_SLA='001' or MESG_SLA='002'))"
				+ " or (mesg_type='103') or (mesg_type='202') or  (mesg_type='205') )" + " and (mesg_parsing_date_time is null or mesg_parsing_date_time < mesg_mod_date_time ) and archived = 0 " + " and m.mesg_crea_date_time BETWEEN ?  AND ?  "
				+ " order by mesg_crea_date_time ";

		System.out.println(query);

	}
}
