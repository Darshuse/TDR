package com.eastnets.extraction.dao.search;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.eastnets.extraction.bean.AllianceInstance;
import com.eastnets.extraction.bean.AppendixDetails;
import com.eastnets.extraction.bean.AppendixExtDetails;
import com.eastnets.extraction.bean.CorrInfo;
import com.eastnets.extraction.bean.FileAct;
import com.eastnets.extraction.bean.InstanceDetails;
import com.eastnets.extraction.bean.InstanceTransmissionPrintInfo;
import com.eastnets.extraction.bean.InterventionDetails;
import com.eastnets.extraction.bean.MessageDetails;
import com.eastnets.extraction.bean.PayloadFile;
import com.eastnets.extraction.bean.PayloadType;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.TextFieldData;
import com.eastnets.extraction.config.YAMLConfig;
import com.eastnets.extraction.dao.procedure.MTExpantionGetProcedure;
import com.eastnets.extraction.dao.procedure.PrintInstanceTransmissionGetProcedure;
import com.eastnets.extraction.dao.procedure.VWGetCorrInfoProcedure;
import com.eastnets.extraction.service.helper.DBPortabilityHandler;
import com.eastnets.extraction.service.helper.Pair;
import com.eastnets.extraction.service.helper.SearchUtils;
import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.UnrecognizedBlockException;
import com.eastnets.resilience.textparser.syntax.Message;

@Component
public class SearchDAOImpl implements SearchDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PrintInstanceTransmissionGetProcedure printInstanceTransmissionGetProcedure;

	@Autowired
	private MTExpantionGetProcedure mtExpantionGetProcedure;

	@Autowired
	private VWGetCorrInfoProcedure vwGetCorrInfoProcedure;

	Connection expandConn = null;
	private Map<String, Integer> currenciesMap;

	final String separator = System.getProperty("line.separator");

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchDAO.class);

	private boolean fullDebugPrinted;

	@Autowired
	private YAMLConfig config;

	@PostConstruct
	public void init() {
		currenciesMap = getCurrenciesISO();
	}

	public List<SearchResult> execSearchQuery(String query) {

		List<SearchResult> searchResultList = jdbcTemplate.query(query, new RowMapper<SearchResult>() {

			@Override
			public SearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {

				SearchResult searchResult = new SearchResult();
				searchResult.setAid(rs.getInt("aid"));
				searchResult.setMesgUmidl(rs.getInt("mesg_s_umidl"));
				searchResult.setMesgUmidh(rs.getInt("mesg_s_umidh"));
				searchResult.setMesgSubFormat(rs.getString("mesg_sub_format"));
				searchResult.setMesgType(rs.getString("mesg_type"));
				searchResult.setMesgUumid(rs.getString("mesg_uumid"));
				searchResult.setMesgSenderX1(rs.getString("mesg_sender_X1"));
				searchResult.setMesgTrnRef(rs.getString("mesg_trn_ref"));
				searchResult.setMesgUserReferenceText(rs.getString("mesg_user_reference_text"));
				searchResult.setMesgCreaDateTime(new Date(rs.getTimestamp("mesg_crea_date_time").getTime()));

				searchResult.setxFinValueDate(rs.getDate("x_fin_value_date"));

				searchResult.setxFinCcy(rs.getString("x_fin_ccy"));
				searchResult.setMesgFrmtName(rs.getString("mesg_frmt_name"));
				String status = rs.getString("mesg_status");
				if (status != null) {
					status = status.trim();
				}
				searchResult.setMesgStatus(status);
				searchResult.setMesgMesgUserGroup(rs.getString("mesg_mesg_user_group"));
				searchResult.setMesgIdentifier(rs.getString("mesg_identifier"));
				searchResult.setMesgUumidSuffix(rs.getInt("mesg_uumid_suffix"));
				searchResult.setInstRpName(rs.getString("X_INST0_RP_NAME"));
				searchResult.setInstReceiverX1(rs.getString("inst_receiver_X1"));
				searchResult.setEmiIAppName(rs.getString("emi_iapp_name"));
				searchResult.setEmiSessionNbr(rs.getInt("emi_session_nbr"));
				searchResult.setEmiSequenceNbr(rs.getString("emi_sequence_nbr"));
				searchResult.setEmiNetworkDeliveryStatus(rs.getString("emi_network_delivery_status"));
				searchResult.setRecIAppName(rs.getString("rec_iapp_name"));
				searchResult.setRecSessionNbr(rs.getInt("rec_session_nbr"));
				searchResult.setRecSequenceNbr(rs.getString("rec_sequence_nbr"));

				// GPI Colums
				searchResult.setOrderingCustomer(rs.getString("MESG_ORDER_CUS"));
				searchResult.setOrderingInstitution(rs.getString("MESG_ORDERING_INST"));
				searchResult.setBeneficiaryCustomer(rs.getString("MESG_BEN_CUST"));
				searchResult.setAccountWithInstitution(rs.getString("MESG_ACCOUNT_INST"));
				searchResult.setDetailsOfcharges(rs.getString("MESG_CHARGES"));

				String deductsCurr = "";

				if (rs.getString("MESG_SND_CHARGES_AMOUNT") != null && !rs.getString("MESG_SND_CHARGES_AMOUNT").isEmpty()) {
					String chargesLine = rs.getString("MESG_SND_CHARGES_AMOUNT");
					String[] charges;
					if (chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						searchResult.setDeducts(charges[charges.length - 1]);
					} else {
						searchResult.setDeducts(rs.getString("MESG_SND_CHARGES_AMOUNT"));
					}

				} else {
					searchResult.setDeducts(rs.getString("MESG_RCV_CHARGES_AMOUNT"));
				}

				if (rs.getString("MESG_SND_CHARGES_CURR") != null && !rs.getString("MESG_SND_CHARGES_CURR").isEmpty()) {
					String currLine = rs.getString("MESG_SND_CHARGES_CURR");
					String[] currs;
					if (currLine.contains(",")) {
						currs = currLine.split(",");
						deductsCurr = currs[currs.length - 1];
					} else {
						deductsCurr = rs.getString("MESG_SND_CHARGES_CURR");
					}

				}

				searchResult.setExchangeRate(rs.getString("MESG_EXCHANGE_RATE"));

				searchResult.setStatusCode(rs.getString("MESG_Status_code"));
				searchResult.setReasonCodes(rs.getString("MESG_Reason_code"));
				searchResult.setStatusOriginatorBIC(rs.getString("MESG_status_originator"));
				searchResult.setForwardedToAgent(rs.getString("MESG_forwarded_to"));
				String transactionStatusMapper = "";

				searchResult.setTransactionStatus(transactionStatusMapper);
				searchResult.setNAKCode(rs.getString("MESG_NAK_code"));
				searchResult.setGpiCur(rs.getString("MESG_INSTR_CCY"));
				searchResult.setSenderCorr(rs.getString("MESG_SND_CORR"));
				searchResult.setReceiverCorr(rs.getString("MESG_RCVR_CORR"));
				searchResult.setReimbursementInst(rs.getString("MESG_REIMBURS_INST"));
				searchResult.setSattlmentMethod(rs.getString("MESG_Settlement_Method"));
				searchResult.setClearingSystem(rs.getString("MESG_Clearing_System"));

				return searchResult;
			}
		});

		LOGGER.info("Query execution finished.");
		return searchResultList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<MessageDetails> getMessageDetails(String compsiteKeyString, boolean enableDebugFull) throws Exception {

		String queryString = "select m.aid, m.mesg_s_umidl, m.mesg_s_umidh, m.mesg_uumid,m.mesg_uumid_suffix,m.mesg_user_issued_as_pde, m.mesg_possible_dup_creation,m.mesg_is_partial,m.mesg_class,"
				+ "m.mesg_receiver_alia_name,m.mesg_is_live,m.mesg_is_text_readonly,m.MESG_ORDERING_INST, m.mesg_is_text_modified,m.mesg_is_delete_inhibited,m.mesg_frmt_name,"
				+ "m.mesg_sub_format,m.mesg_type,m.mesg_nature,m.mesg_sender_swift_address, m.mesg_sender_corr_type,m.mesg_sender_X1,m.mesg_sender_X2,m.mesg_sender_X3,"
				+ "m.mesg_sender_X4,m.mesg_receiver_swift_address,x1.inst_receiver_corr_type, x1.inst_receiver_X1,x1.inst_receiver_X2,x1.inst_receiver_X3,x1.inst_receiver_X4,x1.inst_delivery_mode,"
				+ "m.mesg_trn_ref,m.mesg_rel_trn_ref,m.x_fin_amount,m.x_fin_ccy,m.x_fin_value_date, m.last_update, m.archived, m.restored, NULL as request_aid,m.mesg_network_priority,"
				+ "m.mesg_delv_overdue_warn_req,m.mesg_copy_service_id,m.mesg_network_delv_notif_req,m.mesg_user_priority_code,m.mesg_user_reference_text,m.mesg_crea_appl_serv_name,"
				+ "m.mesg_crea_mpfn_name,m.mesg_crea_date_time,m.mesg_syntax_table_ver, m.mesg_validation_requested,m.mesg_network_appl_ind,m.mesg_validation_passed,"
				+ "t.text_data_block ,m.MESG_ACCOUNT_INST,m.MESG_CHARGES,m.MESG_INSTR_AMOUNT,m.MESG_EXCHANGE_RATE,m.MESG_INSTR_CCY,m.MESG_SND_CHARGES_AMOUNT,m.MESG_Status_code,m.MESG_Reason_code,m.MESG_SND_CORR,m.MESG_RCVR_CORR,m.MESG_REIMBURS_INST,m.MESG_Settlement_Method,m.MESG_Clearing_System,"
				+ "m.MESG_SND_CHARGES_CURR,m.MESG_RCV_CHARGES_CURR,m.MESG_RCV_CHARGES_AMOUNT,t.text_swift_block_5, m.mesg_release_info,m.mesg_mesg_user_group,"
				+ "m.mesg_identifier, m.mesg_requestor_dn,x1.inst_responder_dn, m.mesg_security_required, m.mesg_service,mesg_xml_query_ref1, mesg_xml_query_ref2, mesg_xml_query_ref3, mesg_is_retrieved, mesg_network_obso_period ,"
				+ " m.mesg_user_reference_text, m.MESG_SLA, m.mesg_user_priority_code, m.mesg_e2e_transaction_reference, m.MESG_IS_COPY_REQUIRED, " + " m.MESG_AUTH_DELV_NOTIF_REQ, m.MESG_OVERDUE_WARNING_TIME "
				+ " from rInst x1, rMesg m left join rText t on t.aid = m.aid and t.text_s_umidl = m.mesg_s_umidl and t.text_s_umidh = m.mesg_s_umidh ";

		if (config.isPartitioned()) {
			queryString += " and t.x_crea_date_time_mesg = m.mesg_crea_date_time ";
		}

		queryString += "where x1.aid = m.aid and x1.inst_s_umidl = m.mesg_s_umidl and x1.inst_s_umidh = m.mesg_s_umidh ";

		if (config.isPartitioned()) {
			queryString += " and x1.x_crea_date_time_mesg = m.mesg_crea_date_time ";
		}

		if (config.isPartitioned()) {
			queryString += " and x1.inst_num = 0 and (m.aid,m.mesg_s_umidl,m.mesg_s_umidh, m.mesg_crea_date_time) in (" + compsiteKeyString + ")";
		} else {
			queryString += " and x1.inst_num = 0 and (m.aid,m.mesg_s_umidl,m.mesg_s_umidh) in (" + compsiteKeyString + ")";

		}
		if (enableDebugFull && !fullDebugPrinted) {
			fullDebugPrinted = true;
			LOGGER.trace(queryString);
		}

		List<MessageDetails> messageDetails = jdbcTemplate.query(queryString, new RowMapper<MessageDetails>() {
			@Override
			public MessageDetails mapRow(ResultSet rs, int arg1) throws SQLException {

				MessageDetails details = new MessageDetails();
				details.setAid(rs.getInt("aid"));
				details.setMesgUmidl(rs.getInt("mesg_s_umidl"));
				details.setMesgUmidh(rs.getInt("mesg_s_umidh"));
				details.setMesgUumid(rs.getString("mesg_uumid"));
				details.setMesgUumidSuffix(rs.getInt("mesg_uumid_suffix"));
				details.setMesgUserIssuedAsPde(rs.getString("mesg_user_issued_as_pde"));
				details.setMesgPossibleDupCreation(rs.getString("mesg_possible_dup_creation"));
				details.setMesgIsPartial(rs.getBoolean("mesg_is_partial"));
				details.setMesgClass(rs.getString("mesg_class"));
				details.setMesgReceiverAliaName(rs.getString("mesg_receiver_alia_name"));
				details.setMesgIsLive(rs.getBoolean("mesg_is_live"));
				details.setMesgIsTextReadonly(rs.getBoolean("mesg_is_text_readonly"));
				details.setMesgIsTextModified(rs.getBoolean("mesg_is_text_modified"));
				details.setMesgIsDeleteInhibited(rs.getBoolean("mesg_is_delete_inhibited"));
				details.setMesgFrmtName(rs.getString("mesg_frmt_name"));
				details.setMesgSubFormat(rs.getString("mesg_sub_format"));
				details.setMesgType(rs.getString("mesg_type"));
				details.setMesgNature(rs.getString("mesg_nature"));
				details.setMesgSenderSwiftAddress(rs.getString("mesg_sender_swift_address"));
				details.setMesgSenderCorrType(rs.getString("mesg_sender_corr_type"));
				details.setMesgSenderX1(rs.getString("mesg_sender_X1"));
				details.setMesgSenderX2(rs.getString("mesg_sender_X2"));
				details.setMesgSenderX3(rs.getString("mesg_sender_X3"));
				details.setMesgSenderX4(rs.getString("mesg_sender_X4"));
				details.setMesgReceiverSwiftAddress(rs.getString("mesg_receiver_swift_address"));
				details.setInstReceiverCorrType(rs.getString("inst_receiver_corr_type"));
				details.setInstReceiverX1(rs.getString("inst_receiver_X1"));
				details.setInstReceiverX2(rs.getString("inst_receiver_X2"));
				details.setInstReceiverX3(rs.getString("inst_receiver_X3"));
				details.setInstReceiverX4(rs.getString("inst_receiver_X4"));
				details.setInstDeliveryMode(rs.getString("inst_delivery_mode"));
				details.setMesgTrnRef(rs.getString("mesg_trn_ref"));
				details.setMesgRelTrnRef(rs.getString("mesg_rel_trn_ref"));

				Object amountObj = rs.getObject("x_fin_amount");
				if (amountObj != null) {
					String string = amountObj.toString().trim();
					string = string.replaceAll("[^\\d.]", "");
					details.setxFinAmount(formatAmount(new BigDecimal(string), rs.getString("x_fin_ccy")));
				} else {
					details.setxFinAmount(null);
				}
				details.setxFinCcy(rs.getString("x_fin_ccy"));
				details.setxFinValueDate(rs.getDate("x_fin_value_date"));
				Date dt = null;
				if (rs.getTimestamp("last_update") != null) {
					dt = new Date(rs.getTimestamp("last_update").getTime());
				}
				details.setLastUpdate(dt);
				details.setArchived(rs.getBoolean("archived"));
				details.setRestored(rs.getBoolean("restored"));
				details.setRequestAid(rs.getInt("request_aid"));
				details.setMesgNetworkPriority(rs.getString("mesg_network_priority"));
				details.setMesgDelvOverdueWarnReq(rs.getString("mesg_delv_overdue_warn_req"));
				details.setMesgCopyServiceId(rs.getString("mesg_copy_service_id"));
				details.setMesgNetworkDelvNotifReq(rs.getString("mesg_network_delv_notif_req"));
				details.setMesgUserPriorityCode(rs.getString("mesg_user_priority_code"));
				details.setMesgUserReferenceText(rs.getString("mesg_user_reference_text"));
				details.setMesgCreaApplServName(rs.getString("mesg_crea_appl_serv_name"));
				details.setMesgCreaMpfnName(rs.getString("mesg_crea_mpfn_name"));
				dt = null;
				if (rs.getTimestamp("mesg_crea_date_time") != null) {
					dt = new Date(rs.getTimestamp("mesg_crea_date_time").getTime());
				}
				details.setMesgCreaDateTime(dt);
				details.setMesgSyntaxTableVer(rs.getString("mesg_syntax_table_ver"));
				details.setMesgValidationRequested(rs.getString("mesg_validation_requested"));
				details.setMesgNetworkApplInd(rs.getString("mesg_network_appl_ind"));
				details.setMesgValidationPassed(rs.getString("mesg_validation_passed"));
				details.setTextDataBlock(rs.getClob("text_data_block"));
				details.setTextSwiftBlock5(rs.getString("text_swift_block_5"));
				details.setMesgReleaseInfo(rs.getString("mesg_release_info"));
				details.setMesgMesgUserGroup(rs.getString("mesg_mesg_user_group"));
				details.setMesgIdentifier(rs.getString("mesg_identifier"));
				details.setMesgRequestorDn(rs.getString("mesg_requestor_dn"));
				details.setInstResponderDn(rs.getString("inst_responder_dn"));
				details.setMesgService(rs.getString("mesg_service"));
				details.setMesgSecurityRequired(rs.getString("mesg_security_required"));
				details.setMesgXmlQueryRef1(rs.getString("mesg_xml_query_ref1"));
				details.setMesgXmlQueryRef2(rs.getString("mesg_xml_query_ref2"));
				details.setMesgXmlQueryRef3(rs.getString("mesg_xml_query_ref3"));
				details.setMesgIsRetrieved(rs.getBoolean("mesg_is_retrieved"));
				details.setMesgNetworkObsoPeriod(rs.getInt("mesg_network_obso_period"));
				details.setMur(rs.getString("mesg_user_reference_text"));
				details.setSlaId(rs.getString("MESG_SLA"));
				details.setBankingPriority(rs.getString("mesg_user_priority_code"));
				details.setUetr(rs.getString("mesg_e2e_transaction_reference"));
				details.setMesgAuthDelNotificationRequest(rs.getString("MESG_AUTH_DELV_NOTIF_REQ"));
				details.setMesgIsCopyRequest(rs.getString("MESG_IS_COPY_REQUIRED"));
				details.setMesgOverDueDateTime(rs.getString("MESG_OVERDUE_WARNING_TIME"));

				details.setMesgCharges(rs.getString("MESG_CHARGES"));
				details.setMesgBeneficiaryBankCode(rs.getString("MESG_ACCOUNT_INST"));
				if (rs.getBigDecimal("MESG_INSTR_AMOUNT") != null) {
					details.setInstructedAmount(formatAmount(rs.getBigDecimal("MESG_INSTR_AMOUNT"), rs.getString("MESG_INSTR_CCY")));
				}
				details.setSenderCorr(rs.getString("MESG_SND_CORR"));
				details.setRecieverCorr(rs.getString("MESG_RCVR_CORR"));
				details.setReimbInst(rs.getString("MESG_REIMBURS_INST"));
				details.setSattlmentMethod(rs.getString("MESG_Settlement_Method"));
				details.setClearingSystem(rs.getString("MESG_Clearing_System"));

				details.setStatusCode(rs.getString("MESG_Status_code"));
				details.setReasonCode(rs.getString("MESG_Reason_code"));

				if (rs.getString("MESG_SND_CHARGES_AMOUNT") != null && !rs.getString("MESG_SND_CHARGES_AMOUNT").isEmpty()) {
					String chargesLine = rs.getString("MESG_SND_CHARGES_AMOUNT");
					String[] charges;
					if ((chargesLine != null && !chargesLine.isEmpty()) && chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						details.setMesgSndChargesAmount(charges[charges.length - 1]);
					} else {
						details.setMesgSndChargesAmount(rs.getString("MESG_SND_CHARGES_AMOUNT"));

					}

					details.setSenderChargeAmount(rs.getString("MESG_SND_CHARGES_AMOUNT"));

				}

				if (rs.getString("MESG_SND_CHARGES_CURR") != null && !rs.getString("MESG_SND_CHARGES_CURR").isEmpty()) {
					String currLine = rs.getString("MESG_SND_CHARGES_CURR");
					String[] currs;
					if ((currLine != null && !currLine.isEmpty()) && currLine.contains(",")) {
						currs = currLine.split(",");
						details.setMesgSndChargesCurr(currs[currs.length - 1]);
					}

					else {
						details.setMesgSndChargesCurr(rs.getString("MESG_SND_CHARGES_CURR"));

					}
					details.setSenderChargeCur(rs.getString("MESG_SND_CHARGES_CURR"));
				}
				if (details.getMesgSndChargesAmount() != null && details.getMesgSndChargesAmount().length() > 0) {
					try {
						details.setMesgSndChargesAmount(formatAmount(new BigDecimal(details.getMesgSndChargesAmount().trim()), details.getMesgSndChargesCurr()).toString());
					} catch (NullPointerException e) {
						System.out.println(e.getMessage());
					}
				}

				if (rs.getString("MESG_RCV_CHARGES_AMOUNT") != null && !rs.getString("MESG_RCV_CHARGES_AMOUNT").isEmpty()) {
					String chargesLine = rs.getString("MESG_SND_CHARGES_AMOUNT");
					String[] charges;
					if ((chargesLine != null && !chargesLine.isEmpty()) && chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						details.setMesgRcvChargesAmount(charges[charges.length - 1]);
					} else {
						details.setMesgRcvChargesAmount(rs.getString("MESG_RCV_CHARGES_AMOUNT"));

					}

				}

				if (rs.getString("MESG_RCV_CHARGES_CURR") != null && !rs.getString("MESG_RCV_CHARGES_CURR").isEmpty()) {
					String currLine = rs.getString("MESG_RCV_CHARGES_CURR");
					String[] currs;
					if ((currLine != null && !currLine.isEmpty()) && currLine.contains(",")) {
						currs = currLine.split(",");
						details.setMesgRcvChargesCurr(currs[currs.length - 1]);
					} else {
						details.setMesgRcvChargesCurr(rs.getString("MESG_RCV_CHARGES_CURR"));

					}

				}

				if (details.getMesgRcvChargesAmount() != null && !details.getMesgRcvChargesAmount().isEmpty()) {
					details.setMesgRcvChargesAmount(formatAmount(new BigDecimal(details.getMesgRcvChargesAmount().trim()), details.getMesgRcvChargesCurr()).toString());
				}

				details.setOrdringInstution(rs.getString("MESG_ORDERING_INST"));
				details.setMesgExchangeRate(rs.getString("MESG_EXCHANGE_RATE"));
				details.setMesgInstructedCur(rs.getString("MESG_INSTR_CCY"));

				return details;
			}

		});
		if (messageDetails.size() == 0) {
			throw new Exception(String.format("Cannot get details for message( aid, umidl, umidh ) = %s , make sure that the message has instances.", compsiteKeyString));
		}
		return messageDetails;

	}

	@Override
	public List<InstanceDetails> getInstanceList(String compsiteKeyString) {

		String queryString = "select aid, inst_s_umidl, inst_s_umidh, inst_num, inst_rp_name, inst_status, inst_type, inst_crea_appl_serv_name, inst_crea_rp_name, "
				+ " inst_crea_date_time, inst_crea_mpfn_name, inst_mpfn_name, inst_related_nbr, inst_unit_name, inst_responder_dn,inst_nr_indicator from rInst where ";

		if (config.isPartitioned()) {
			queryString += " (aid, inst_s_umidl, inst_s_umidh, x_crea_date_time_mesg) in (" + compsiteKeyString + ")";

		} else {
			queryString += " (aid, inst_s_umidl, inst_s_umidh) in (" + compsiteKeyString + ")";
		}

		queryString += " order by inst_num";

		@SuppressWarnings("deprecation")
		List<InstanceDetails> instances = jdbcTemplate.query(queryString, new RowMapper<InstanceDetails>() {
			public InstanceDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				InstanceDetails instance = new InstanceDetails();
				instance.setAid(rs.getInt("aid"));
				instance.setMesgUmidl(rs.getInt("inst_s_umidl"));
				instance.setMesgUmidh(rs.getInt("inst_s_umidh"));
				instance.setInstNum(rs.getInt("inst_num"));
				instance.setInstStatus(rs.getString("inst_status"));
				instance.setInstType(rs.getString("inst_type"));
				instance.setInstCreaApplServName(rs.getString("inst_crea_appl_serv_name"));
				instance.setInstCreaDateTime(new java.util.Date(rs.getTimestamp("inst_crea_date_time").getTime()));
				instance.setInstCreaMpfnName(rs.getString("inst_crea_mpfn_name"));
				instance.setInstMpfnName(rs.getString("inst_mpfn_name"));
				instance.setInstCreaRpName(rs.getString("inst_crea_rp_name"));
				instance.setInstRpName(rs.getString("inst_rp_name"));
				instance.setInstRelatedNbr(rs.getInt("inst_related_nbr"));
				instance.setInstUnitName(rs.getString("inst_unit_name"));
				instance.setInstResponderDn(rs.getString("inst_responder_dn"));
				instance.setInstNrIndicator(rs.getString("inst_nr_indicator"));

				return instance;
			}
		});
		return instances;
	}

	@Override
	public List<TextFieldData> getTextFieldData(String compsiteKeyString) {

		String queryString = "select AID, TEXT_S_UMIDL, TEXT_S_UMIDH, field_code, field_option, value, value_memo from rTextField where ";

		if (config.isPartitioned()) {
			queryString += " (AID, TEXT_S_UMIDL, TEXT_S_UMIDH, X_CREA_DATE_TIME_MESG) in (" + compsiteKeyString + ")";
		} else {
			queryString += " (AID, TEXT_S_UMIDL, TEXT_S_UMIDH) in (" + compsiteKeyString + ")";
		}

		queryString += " order by field_cnt";

		List<TextFieldData> textFieldDataList = jdbcTemplate.query(queryString, new RowMapper<TextFieldData>() {
			public TextFieldData mapRow(ResultSet rs, int rowNum) throws SQLException {

				TextFieldData textFieldData = new TextFieldData();

				textFieldData.setAid(rs.getInt("AID"));
				textFieldData.setMesgUmidl(rs.getInt("TEXT_S_UMIDL"));
				textFieldData.setMesgUmidh(rs.getInt("TEXT_S_UMIDH"));
				textFieldData.setFieldCode(rs.getInt("field_code"));
				textFieldData.setFieldOption(rs.getString("field_option"));
				textFieldData.setValue(rs.getString("value"));
				textFieldData.setValueMemo(rs.getClob("value_memo"));
				return textFieldData;
			}
		});
		return textFieldDataList;
	}

	@Override
	public List<TextFieldData> getSystemTextFieldData(String compsiteKeyString) {

		String queryString = "select AID, TEXT_S_UMIDL, TEXT_S_UMIDH, FIELD_CODE, VALUE from rSystemTextField where ";

		if (config.isPartitioned()) {
			queryString += " (AID, TEXT_S_UMIDL, TEXT_S_UMIDH, X_CREA_DATE_TIME_MESG) in (" + compsiteKeyString + ")";
		} else {
			queryString += " (AID, TEXT_S_UMIDL, TEXT_S_UMIDH) in (" + compsiteKeyString + ")";
		}

		queryString += " order by FIELD_CNT";
		List<TextFieldData> textFieldDataList = jdbcTemplate.query(queryString, new RowMapper<TextFieldData>() {
			public TextFieldData mapRow(ResultSet rs, int rowNum) throws SQLException {

				TextFieldData textFieldData = new TextFieldData();
				textFieldData.setAid(rs.getInt("AID"));
				textFieldData.setMesgUmidl(rs.getInt("TEXT_S_UMIDL"));
				textFieldData.setMesgUmidh(rs.getInt("TEXT_S_UMIDH"));
				textFieldData.setFieldCode(rs.getInt("field_code"));
				textFieldData.setFieldOption(null);
				textFieldData.setValue(rs.getString("value"));
				textFieldData.setValueMemo(null);
				return textFieldData;
			}
		});
		return textFieldDataList;
	}

	@Override
	public InstanceTransmissionPrintInfo getInstanceTransmissionPrintInfo(int aid, int umidl, int umidh, Date mesg_crea_date) {
		return printInstanceTransmissionGetProcedure.execute(aid, umidl, umidh, mesg_crea_date, 0);

	}

	@Override
	public Pair<String, String> getMTExpantion(int aid, int umidl, int umidh, Date mesg_crea_date, String mesgType) {
		return mtExpantionGetProcedure.execute(aid, umidl, umidh, mesg_crea_date, mesgType);
	}

	@Override
	public String getExpandedMesssageText(Integer aid, Integer umidl, Integer umidh, String syntaxVersion, String messageType, String unexpandedText, java.util.Date messageDate, String thousandAmountFormat, String decimalAmountFormat)
			throws SQLException {
		String val = "";
		ParsedMessage list = null;
		if (expandConn == null || expandConn.isClosed()) {
			expandConn = jdbcTemplate.getDataSource().getConnection();
		}
		try {
			Message messageParser = Syntax.getParser(syntaxVersion, messageType, expandConn);
			list = messageParser.parseMessageText(unexpandedText);
			val = unexpandedText;
			if (list != null) {
				val = list.getExpandedMessage(new Timestamp(messageDate.getTime()), thousandAmountFormat, decimalAmountFormat, currenciesMap);
			}
		} catch (UnrecognizedBlockException ex) {
			if (config.isEnableDebug()) {
				LOGGER.error("DataExtraction  exception  :: " + ex.getMessage());
			}
			return "Message Expand Failed: Message Contains Unrecognized Block";
		} catch (Exception ex) {
			if (config.isEnableDebug()) {
				LOGGER.error(ex + " message aid " + aid + " umidl " + umidl + " umidh " + umidh);
			}
			return "Message Expand Failed: " + ex.getMessage();
		} finally {
			expandConn.close();
		}

		return val;
	}

	@Override
	public List<AppendixDetails> getAppendixList(String compsiteKeyString) {

		List<Object> parameters = new ArrayList<Object>();

		String queryString = "select AID, APPE_S_UMIDL, APPE_S_UMIDH, appe_seq_nbr, appe_date_time, appe_type, appe_iapp_name, appe_session_holder, appe_session_nbr,"
				+ " appe_sequence_nbr, appe_network_delivery_status, appe_ack_nack_text, appe_pki_auth_result, appe_pki_authorisation_res, appe_pki_pac2_result, appe_rma_check_result, appe_inst_num from vAppe where ";

		if (config.isPartitioned()) {
			queryString += " (AID, APPE_S_UMIDL, APPE_S_UMIDH, X_CREA_DATE_TIME_MESG) in (" + compsiteKeyString + ")";
		} else {
			queryString += "(AID, APPE_S_UMIDL, APPE_S_UMIDH) in (" + compsiteKeyString + ")";
		}

		queryString += " order by appe_date_time, appe_seq_nbr";

		@SuppressWarnings("deprecation")
		List<AppendixDetails> appendices = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<AppendixDetails>() {
			public AppendixDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				AppendixDetails appe = new AppendixDetails();
				appe.setTimeZoneOffset(0);
				appe.setAid(rs.getInt("AID"));
				appe.setMesgUmidl(rs.getInt("APPE_S_UMIDL"));
				appe.setMesgUmidh(rs.getInt("APPE_S_UMIDH"));
				appe.setAppeSeqNbr(rs.getLong("appe_seq_nbr"));
				appe.setAppeDateTime(new Date(rs.getTimestamp("appe_date_time").getTime()));
				appe.setAppeType(rs.getString("appe_type"));
				appe.setAppeIAppName(rs.getString("appe_iapp_name"));
				appe.setAppeSessionHolder(rs.getString("appe_session_holder"));
				appe.setAppeSessionNbr(rs.getInt("appe_session_nbr"));
				appe.setAppeSequenceNbr(rs.getInt("appe_sequence_nbr"));
				appe.setAppeNetworkDeliveryStatus(rs.getString("appe_network_delivery_status"));
				appe.setAppeAckNackText(rs.getString("appe_ack_nack_text"));
				appe.setAppePkiAuthResult(rs.getString("appe_pki_auth_result"));
				appe.setAppePkiAuthorisationRes(rs.getString("appe_pki_authorisation_res"));
				appe.setAppePkiPac2Result(rs.getString("appe_pki_pac2_result"));
				appe.setAppeRmaCheckResult(rs.getString("appe_rma_check_result"));
				appe.setAppeInstNum(rs.getInt("appe_inst_num"));
				return appe;
			}
		});
		return appendices;
	}

	@Override
	public AppendixExtDetails getAppendixDetails(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int inst_num, Long intv_seq_num, java.util.Date intv_date_time, final int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);
		parameters.add(inst_num);

		String queryString = "select inst_unit_name, appe_type, appe_iapp_name," + " appe_session_holder, appe_session_nbr, appe_sequence_nbr, appe_date_time," + " appe_local_output_time, appe_remote_input_reference, appe_checksum_value,"
				+ " appe_checksum_result, appe_auth_value, appe_auth_result, appe_pac_value," + " appe_pac_result, appe_rcv_delivery_status, appe_network_delivery_status," + " appe_sender_cancel_status, appe_crea_appl_serv_name, appe_crea_rp_name,"
				+ " appe_ack_nack_text, appe_telex_number, appe_answerback, appe_tnap_name," + " appe_fax_number, appe_fax_tnap_name, appe_pki_auth_result, appe_pki_pac2_result,"
				+ " appe_rma_check_result, appe_use_pki_signature, appe_pki_authorisation_res," + " inst_telex_number, inst_answerback, inst_tnap_name, inst_fax_number,"
				+ " inst_fax_tnap_name, appe_record_id, appe_pki_auth_value, appe_pki_pac2_value," + " appe_signer_dn, appe_snf_delv_notif_req, appe_nr_indicator," + " appe_resp_mval_result, appe_swift_response_ref , appe_swift_ref,"
				+ " appe_swift_request_ref " + " from vAppe a,  rInst  i " + " where a.aid = i.aid and a.appe_s_umidl = i.inst_s_umidl and " + " a.appe_s_umidh = i.inst_s_umidh and a.appe_inst_num = i.inst_num";
		if (config.isPartitioned()) {
			queryString += " and a.x_crea_date_time_mesg = i.x_crea_date_time_mesg";
		}
		queryString += " and a.aid = ? and appe_s_umidl = ? and appe_s_umidh = ? and appe_inst_num = ? ";

		if (config.isPartitioned()) {
			queryString += " and a.x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		parameters.add(new Timestamp(intv_date_time.getTime()));
		parameters.add(intv_seq_num);
		queryString += " and appe_date_time = ?  and appe_seq_nbr = ? ";

		@SuppressWarnings("deprecation")
		AppendixExtDetails appeExtDetails = (AppendixExtDetails) jdbcTemplate.queryForObject(queryString, parameters.toArray(), new RowMapper<AppendixExtDetails>() {
			public AppendixExtDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				AppendixExtDetails appDetails = new AppendixExtDetails();
				appDetails.setTimeZoneOffset(timeZoneOffset);
				appDetails.setAppeType(rs.getString("appe_type"));
				appDetails.setInstUnitName(rs.getString("inst_unit_name"));
				appDetails.setInstTelexNumber(rs.getString("inst_telex_number"));
				appDetails.setInstAnswerBack(rs.getString("inst_answerback"));
				appDetails.setInstTnapName(rs.getString("inst_tnap_name"));
				appDetails.setInstFaxNumber(rs.getString("inst_fax_number"));
				appDetails.setInstFaxTnapName(rs.getString("inst_fax_tnap_name"));

				appDetails.setAppeIAppName(rs.getString("appe_iapp_name"));
				appDetails.setAppeSessionHolder(rs.getString("appe_session_holder"));
				appDetails.setAppeSessionNbr(rs.getInt("appe_session_nbr"));
				appDetails.setAppeSequenceNbr(rs.getLong("appe_sequence_nbr"));
				java.sql.Timestamp ts = rs.getTimestamp("appe_date_time");
				if (ts != null) {
					appDetails.setAppeDateTime(new Date(ts.getTime()));
				} else {
					appDetails.setAppeDateTime(null);
				}

				ts = rs.getTimestamp("appe_local_output_time");
				if (ts != null) {
					appDetails.setAppeLocalOutputTime(new Date(ts.getTime()));
				} else {
					appDetails.setAppeLocalOutputTime(null);
				}
				appDetails.setAppeRemoteInputReference(rs.getString("appe_remote_input_reference"));
				appDetails.setAppeCheckSumValue(rs.getString("appe_checksum_value"));
				appDetails.setAppeCheckSumResult(rs.getString("appe_checksum_result"));
				appDetails.setAppeAuthValue(rs.getString("appe_auth_value"));
				appDetails.setAppeAuthResult(rs.getString("appe_auth_result"));
				appDetails.setAppePacValue(rs.getString("appe_pac_value"));
				appDetails.setAppePacResult(rs.getString("appe_pac_result"));/**/
				appDetails.setAppeRcvDeliveryStatus(rs.getString("appe_rcv_delivery_status"));
				appDetails.setAppeNetworkDeliveryStatus(rs.getString("appe_network_delivery_status"));
				appDetails.setAppeSenderCancelStatus(rs.getString("appe_sender_cancel_status"));
				appDetails.setAppeCreaApplServName(rs.getString("appe_crea_appl_serv_name"));
				appDetails.setAppeCreaRpName(rs.getString("appe_crea_rp_name"));
				appDetails.setAppeAckNackText(rs.getString("appe_ack_nack_text"));
				appDetails.setAppeTelexNumber(rs.getString("appe_telex_number"));
				appDetails.setAppeAnswerBack(rs.getString("appe_answerback"));
				appDetails.setAppeTnapName(rs.getString("appe_tnap_name"));
				appDetails.setAppeFaxNumber(rs.getString("appe_fax_number"));
				appDetails.setAppeFaxTnapName(rs.getString("appe_fax_tnap_name"));
				appDetails.setAppePkiAuthResult(rs.getString("appe_pki_auth_result"));
				appDetails.setAppePkiPac2Result(rs.getString("appe_pki_pac2_result"));
				appDetails.setAppeRmaCheckResult(rs.getString("appe_rma_check_result"));
				appDetails.setAppeUsePkiSignature(rs.getBoolean("appe_use_pki_signature"));
				appDetails.setAppePkiAuthorisationRes(rs.getString("appe_pki_authorisation_res"));
				appDetails.setAppeRecordId(rs.getLong("appe_record_id"));
				appDetails.setAppePkiAuthValue(rs.getString("appe_pki_auth_value"));
				appDetails.setAppePkiPac2Value(rs.getString("appe_pki_pac2_value"));
				appDetails.setAppeSignerDn(rs.getString("appe_signer_dn"));
				appDetails.setAppeSnfDelvNotifReq(getBoolean(rs, "appe_snf_delv_notif_req"));
				appDetails.setAppeNrIndicator(getBoolean(rs, "appe_nr_indicator"));
				appDetails.setAppeRespMvalResult(rs.getString("appe_resp_mval_result"));
				appDetails.setAppeSwiftResponseRef(rs.getString("appe_swift_response_ref"));
				appDetails.setAppeSwiftRef(rs.getString("appe_swift_ref"));
				appDetails.setAppeSwiftRequestRef(rs.getString("appe_swift_request_ref"));
				return appDetails;
			}

			private Boolean getBoolean(ResultSet rs, String field) throws SQLException {
				String val = rs.getString(field);
				if (val != null) {
					return rs.getBoolean(field);
				}
				return null;
			}
		});
		return appeExtDetails;
	}

	public BigDecimal formatAmount(BigDecimal amount, String currency) {

		try {
			if ((currency == null || currency.isEmpty())) {
				return amount;
			}

			Integer numberOfDigits = currenciesMap.get(currency);

			if (numberOfDigits == null) {
				return amount;
			}

			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
			numberFormat.setMaximumFractionDigits(numberOfDigits);
			// 54279 : TDR 3.2: Amount decimal digits are truncated at trailing 0
			String value = amount.toString();
			String digits = "";
			if (value.contains(".")) {
				String[] amountValue = value.split("\\.");
				digits = amountValue[1];
			}
			if (digits.length() < numberOfDigits) {
				while (digits.length() < numberOfDigits) {
					digits = digits + "0";
					value = value + "0";
				}
			}
			if (digits.length() > numberOfDigits) {
				while (digits.length() > numberOfDigits) {
					digits = digits.substring(0, digits.length() - 1);
					value = value.substring(0, value.length() - 1);
				}
			}
			BigDecimal bigDecimal = new BigDecimal(value);
			return bigDecimal;

		} catch (Exception e) {

		}

		return null;

	}

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
	public CorrInfo getCorrInfo(CorrInfo corr) {
		vwGetCorrInfoProcedure.execute(corr);
		return corr;
	}

	@SuppressWarnings("deprecation")
	@Override
	public CorrInfo getBicInfoStr(String bic) {
		try {
			String query = "SELECT  corr_institution_name,  corr_branch_info, corr_city_name,  corr_ctry_code,   corr_ctry_name, " + "  corr_location FROM   rcorr WHERE   corr_type = 'CORR_TYPE_INSTITUTION'  AND corr_x1 LIKE ?  AND ROWNUM = 1 ";
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
	public InstanceTransmissionPrintInfo getInstanceTransmissionPrintInfo(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int timeZoneOffset) {
		return printInstanceTransmissionGetProcedure.execute(aid, umidl, umidh, mesg_crea_date, timeZoneOffset);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> isBeingUpdated(String compsiteKeyString) {

		String queryString = "select aid from ldRequestUpdate where ";

		if (config.isPartitioned()) {
			queryString += " (aid, mesg_s_umidl, mesg_s_umidh, x_crea_date_time_mesg) in (" + compsiteKeyString + ")";
		} else {
			queryString += "(aid, mesg_s_umidl, mesg_s_umidh) in (" + compsiteKeyString + ")";
		}
		List<String> aids = (List<String>) jdbcTemplate.query(queryString, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("aid");
			}
		});

		return aids;
	}

	@Override
	public List<InterventionDetails> getInterventionList(String compsiteKeyString) {

		String queryString = "select AID, INTV_S_UMIDL, INTV_S_UMIDH, INTV_SEQ_NBR, INTV_DATE_TIME, INTV_TEXT, INTV_OPER_NICKNAME, INTV_INTY_CATEGORY, INTV_INTY_NAME, INTV_INST_NUM FROM VINTV WHERE ";

		if (config.isPartitioned()) {
			queryString += "(AID, INTV_S_UMIDL, INTV_S_UMIDH, X_CREA_DATE_TIME_MESG) IN (" + compsiteKeyString + ")";
		} else {
			queryString += "(AID, INTV_S_UMIDL, INTV_S_UMIDH) in (" + compsiteKeyString + ")";
		}

		queryString += " AND INTV_INTY_CATEGORY = 'INTY_ROUTING' ORDER BY INTV_DATE_TIME, INTV_SEQ_NBR";

		@SuppressWarnings("deprecation")
		List<InterventionDetails> interventions = jdbcTemplate.query(queryString, new RowMapper<InterventionDetails>() {
			public InterventionDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				InterventionDetails intervention = new InterventionDetails();
				intervention.setTimeZoneOffset(0);
				intervention.setAid(rs.getInt("AID"));
				intervention.setMesgUmidl(rs.getInt("INTV_S_UMIDL"));
				intervention.setMesgUmidh(rs.getInt("INTV_S_UMIDH"));
				intervention.setIntvSeqNbr(rs.getLong("INTV_SEQ_NBR"));
				intervention.setIntvDateTime(new Date(rs.getTimestamp("INTV_DATE_TIME").getTime()));
				intervention.setIntvText(rs.getClob("INTV_TEXT"));
				intervention.setIntvOperNickname(rs.getString("INTV_OPER_NICKNAME"));
				intervention.setIntvIntyCategory(rs.getString("INTV_INTY_CATEGORY"));
				intervention.setIntvIntyName(rs.getString("INTV_INTY_NAME"));
				intervention.setIntvInstNum(rs.getInt("INTV_INST_NUM"));
				return intervention;
			}
		});
		return interventions;
	}

	public PayloadFile getPayloadFile(String aid, String umidl, String umidh, Date creation_date_time) throws Exception {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);
		parameters.add(new Timestamp(creation_date_time.getTime()));
		String queryString = "select payload, mesg_file_logical_name from rfile  where " + " aid = ? and file_s_umidl = ? and file_s_umidh = ? " + " and mesg_crea_date_time = ? ";

		@SuppressWarnings("deprecation")
		List<PayloadFile> downloadFilesList = (List<PayloadFile>) jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<PayloadFile>() {
			public PayloadFile mapRow(ResultSet rs, int rowNum) throws SQLException {

				PayloadFile pf = new PayloadFile();

				pf.setFileName(rs.getString("mesg_file_logical_name"));
				pf.setFileText(rs.getString("payload"));

				return pf;
			}
		});

		return downloadFilesList.get(0);
	}

	public PayloadFile getPayloadFileText(String aid, String umidl, String umidh, Date creation_date_time) throws Exception {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);
		parameters.add(new Timestamp(creation_date_time.getTime()));
		String queryString = "select payload_text, mesg_file_logical_name from rfile  where " + " aid = ? and file_s_umidl = ? and file_s_umidh = ? " + " and mesg_crea_date_time = ? ";

		@SuppressWarnings("deprecation")
		List<PayloadFile> downloadFilesList = (List<PayloadFile>) jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<PayloadFile>() {
			public PayloadFile mapRow(ResultSet rs, int rowNum) throws SQLException {

				PayloadFile pf = new PayloadFile();

				pf.setFileName(rs.getString("mesg_file_logical_name"));
				pf.setFileText(rs.getString("payload_text"));

				return pf;
			}
		});
		return downloadFilesList.get(0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public FileAct getMessageFile(int aid, int umidl, int umidh, java.util.Date creation_date_time) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);
		parameters.add(new Timestamp(creation_date_time.getTime()));

		String queryString = "select mesg_transfer_desc, mesg_transfer_info, mesg_file_logical_name, mesg_file_size, mesg_file_desc, mesg_file_info, mesg_file_digest_algo, mesg_file_digest_value, mesg_file_header_info ";

		queryString += ", CASE WHEN  payload is not null or " + DBPortabilityHandler.getDataLengthFn() + "(payload) <> null or " + DBPortabilityHandler.getDataLengthFn() + "(payload) >  0  THEN '" + PayloadType.BINARY_TAG + "' "
				+ "WHEN  payload_text is not null or " + DBPortabilityHandler.getDataLengthFn() + "(payload_text) <> null or " + DBPortabilityHandler.getDataLengthFn() + "(payload_text) >  0  THEN '" + PayloadType.TEXT_TAG + "' "
				+ "END AS payload_type ";

		queryString += " from RFILE where aid = ? " + " and file_s_umidl = ? " + " and file_s_umidh = ? " + " and mesg_crea_date_time = ? ";

		@SuppressWarnings("deprecation")
		List<FileAct> fileActList = (List<FileAct>) jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FileAct fileAct = new FileAct();
				fileAct.setMesgTransferDesc(rs.getString("mesg_transfer_desc") != null ? rs.getString("mesg_transfer_desc").toString().replaceAll("\\\\n", separator).replace("\\r", "") : " ");
				fileAct.setMesgTransferInfo(rs.getString("mesg_transfer_info") != null ? rs.getString("mesg_transfer_info").toString().replaceAll("\\\\n", separator).replace("\\r", "") : " ");
				fileAct.setMesgFileLogicalName(rs.getString("mesg_file_logical_name"));
				fileAct.setMesgFileSize(rs.getInt("mesg_file_size"));
				fileAct.setMesgFileDesc(rs.getString("mesg_file_desc") != null ? rs.getString("mesg_file_desc").toString().replaceAll("\\\\n", separator).replace("\\r", "") : " ");
				fileAct.setMesgFileInfo(rs.getString("mesg_file_info") != null ? rs.getString("mesg_file_info").toString().replaceAll("\\\\n", separator).replace("\\r", "") : " ");
				fileAct.setMesgFileDigestAlgo(rs.getString("mesg_file_digest_algo"));
				fileAct.setMesgFileDigestValue(rs.getString("mesg_file_digest_value"));
				fileAct.setMesgFileHeaderInfo(rs.getClob("mesg_file_header_info"));

				fileAct.setPayloadType(new PayloadType(rs.getString("payload_type")));

				return fileAct;
			}
		});
		if (fileActList.size() != 0) {
			return fileActList.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> execSearchQueryAsResultSet(String query) {
		List<Map<String, Object>> result = jdbcTemplate.query(query, new RowMapper<Map<String, Object>>() {

			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> data = new HashMap<String, Object>();
				ResultSetMetaData rsMetaData = rs.getMetaData();
				// Retrieving the list of column names
				int count = rsMetaData.getColumnCount();
				for (int i = 1; i <= count; i++) {

					if (rsMetaData.getColumnName(i).toLowerCase().equalsIgnoreCase("creation_date_time")) {
						String dateToSqlStr = SearchUtils.DateToSqlStr(new Date(rs.getTimestamp("creation_date_time").getTime()));
						data.put("creation_date_time_query", dateToSqlStr);
						data.put(rsMetaData.getColumnName(i).toLowerCase(), rs.getObject(i));
					} else {
						data.put(rsMetaData.getColumnName(i).toLowerCase(), rs.getObject(i));
					}
				}

				return data;
			}
		});

		return result;
	}

	@Override
	public void execUpdateQuery(String query) {
		jdbcTemplate.update(query);
	}

	@Override
	public Date getDateOfPreviousExtractedMessage() {
		String query = "SELECT Min (MESG_CREA_DATE_TIME)  FROM RMESG where X_EXTRACTED is null or X_EXTRACTED = 0 ";

		Date date = jdbcTemplate.queryForObject(query, Date.class);
		try {
			String dateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
			date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

	@Override
	public List<AllianceInstance> CashAllianceInstanceByAid() {
		String query = "select AID,ALLIANCE_INSTANCE from LDSETTINGS";
		// Map info=new HashMap<Long, String>();
		List<AllianceInstance> listOfInfo = new ArrayList<AllianceInstance>();
		listOfInfo = jdbcTemplate.query(query, new RowMapper<AllianceInstance>() {
			@Override
			public AllianceInstance mapRow(ResultSet rs, int rowNum) throws SQLException {

				AllianceInstance rowInst = new AllianceInstance();
				rowInst.setAid(rs.getLong("AID"));
				rowInst.setAllianceInstance(rs.getString("ALLIANCE_INSTANCE"));

				return rowInst;
			}
		});
		return listOfInfo;
	}
}
