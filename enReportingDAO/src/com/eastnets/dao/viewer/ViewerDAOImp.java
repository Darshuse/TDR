package com.eastnets.dao.viewer;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.rowset.serial.SerialClob;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.dao.exciptions.SQLInterruptException;
import com.eastnets.dao.security.data.SecurityDataBean;
import com.eastnets.dao.viewer.procedure.ForceMessageUpdateProcedure;
import com.eastnets.dao.viewer.procedure.MTExpantionGetProcedure;
import com.eastnets.dao.viewer.procedure.PrintInstanceTransmissionGetProcedure;
import com.eastnets.domain.Config;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.viewer.AddressBook;
import com.eastnets.domain.viewer.AppendixDetails;
import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.AppendixJREDetails;
import com.eastnets.domain.viewer.CorrespondentBean;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.FileAct;
import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.Identifier;
import com.eastnets.domain.viewer.InstanceDetails;
import com.eastnets.domain.viewer.InstanceExtDetails;
import com.eastnets.domain.viewer.InstanceTransmissionPrintInfo;
import com.eastnets.domain.viewer.InterventionDetails;
import com.eastnets.domain.viewer.InterventionExtDetails;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageNote;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.MessageSearchTemplate;
import com.eastnets.domain.viewer.PayloadFile;
import com.eastnets.domain.viewer.PayloadType;
import com.eastnets.domain.viewer.SearchLookups;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.domain.viewer.nodes.AlternativeNode;
import com.eastnets.domain.viewer.nodes.OptionNode;
import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.exception.UnrecognizedBlockException;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.utils.ApplicationUtils;
import com.eastnets.utils.StatusDesc;
import com.eastnets.utils.Utils;

/**
 * Viewer DAO Implementation
 * 
 * @author EastNets
 * @since July 12, 2012
 */
public abstract class ViewerDAOImp extends DAOBaseImp implements ViewerDAO {

	private static final Logger LOGGER = Logger.getLogger(ViewerDAOImp.class);

	private static final long serialVersionUID = 483174879903453819L;
	private ForceMessageUpdateProcedure forceMessageUpdateProcedure;
	private PrintInstanceTransmissionGetProcedure printInstanceTransmissionGetProcedure;
	private MTExpantionGetProcedure mtExpantionGetProcedure;
	private Config config;
	private boolean partitioned;
	static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMddHHmm");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private boolean enableMesgSecCheck;
	final String separator = System.getProperty("line.separator");
	Connection expandConn = null;
	final PreparedStatement[] stmtSearch = new PreparedStatement[1];
	final PreparedStatement[] stmtCount = new PreparedStatement[1];
	private SecurityDataBean securityDataBean;
	private final static String SEPERATOR = " ";

	private ApplicationFeatures applicationFeatures;

	private Map<String, Integer> currenciesMap;

	public void init() {
		setCurrenciesMap(getCurrenciesISO());

	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public void fillLookupMessageFiles(SearchLookups lookups) {
		List<String> tempList = new ArrayList<String>();

		tempList.add("'Live MSG File");
		lookups.setSourceSearchFile(tempList);
	}

	@Override
	public void fillLookupFormats(SearchLookups lookups) {
		String queryString = "select mesg_frmt_name from ldHelperMesgFormat order by mesg_frmt_name";
		List<String> formatList = jdbcTemplate.query(queryString, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String format = rs.getString("mesg_frmt_name");
				return format;
			}
		});
		lookups.setUmidFormat(formatList);
	}

	@Override
	public void deleteMessageNotes(List<Long> notesIds) {
		for (Long noteID : notesIds) {
			String removeStatement = "DELETE FROM RMESG_NOTES WHERE NOTE_ID IN (?)";
			jdbcTemplate.update(removeStatement, new Object[] { noteID });
		}
	}

	@Override
	public List<String> getLookupNature() {
		String queryString = "select mesg_nature from ldHelperMesgNature order by mesg_nature";
		List<String> natureList = jdbcTemplate.query(queryString, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String nature = rs.getString("mesg_nature");
				return nature;
			}
		});
		return natureList;
	}

	@Override
	public List<CorrespondentBean> cacheCorrespondentsInformation() {

		List<CorrespondentBean> listOfcorrespondent = new ArrayList<>();
		try {
			String query = " SELECT DISTINCT C.CORR_X1, C.CORR_INSTITUTION_NAME, C.CORR_BRANCH_INFO, C.CORR_CTRY_CODE, C.CORR_CTRY_NAME FROM RCORR C WHERE  C.corr_type = 'CORR_TYPE_INSTITUTION'  ORDER BY CORR_X1 ";

			Object[] param = null;
			listOfcorrespondent = jdbcTemplate.query(query, param, new RowMapper<CorrespondentBean>() {
				@Override
				public CorrespondentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					CorrespondentBean correspondentBean = new CorrespondentBean();
					String bic11 = rs.getString("CORR_X1");
					if (bic11 != null && !bic11.isEmpty()) {
						String bic8 = bic11.substring(0, 8);
						correspondentBean.setCorrBIC11(bic11);
						correspondentBean.setCorrBIC8(bic8);
						correspondentBean.setCorrBranchCode(bic11.substring(8, 11));
						correspondentBean.setCorrInstitutionName(rs.getString("CORR_INSTITUTION_NAME"));
						correspondentBean.setCorrBranchInfo(rs.getString("CORR_BRANCH_INFO"));
						correspondentBean.setCorrCountryName(rs.getString("CORR_CTRY_NAME"));
						correspondentBean.setCorrCountryCode(rs.getString("CORR_CTRY_CODE"));
						return correspondentBean;
					}
					return null;
				}
			});

			if (listOfcorrespondent == null || listOfcorrespondent.isEmpty()) {
				listOfcorrespondent = new ArrayList<>();
			}

			return listOfcorrespondent;

		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	public void fillLookupQueues(SearchLookups lookups) {
		String queryString = "select inst_rp_name from ldHelperRPName order by inst_rp_name";
		List<String> queuesList = jdbcTemplate.query(queryString, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String queue = rs.getString("inst_rp_name");
				return queue;
			}
		});
		lookups.setQueuesAvilable(queuesList);
	}

	@Override
	public List<Identifier> getMXIdentifiers() {

		String query = " SELECT ID , Identifier_Name , Identifier_Value " + " FROM XMLTYPES " + " where XML_TYPE = 'MX' " + " ORDER BY Identifier_Name ";

		List<Identifier> identifierDetails = jdbcTemplate.query(query, new RowMapper<Identifier>() {
			@Override
			public Identifier mapRow(ResultSet rs, int rowNum) throws SQLException {

				Identifier identifier = new Identifier();
				identifier.setId(rs.getLong("ID"));

				identifier.setIdentifierValue(rs.getString("Identifier_Value"));

				/*
				 * String [] capitalizedNames = rs.getString("Identifier_Name").split( "(?<=\\p{Ll})(?=\\p{Lu})"); String name = ""; for(String identifierName :capitalizedNames ){ name =
				 * name.isEmpty()? identifierName : name+ " " + identifierName ; }
				 */
				identifier.setIdentiferName(rs.getString("Identifier_Name"));

				return identifier;
			}
		});

		return identifierDetails;
	}

	@Override
	public void fillLookupUnits(SearchLookups lookups, String loggedInUser) {
		String queryString = "select sUnitUserGroup.Unit from sUser, sUnitUserGroup where " + " UPPER(sUser.UserName) = ? and   sUser.GroupID = sUnitUserGroup.GroupID  order by unit";

		List<String> unitList = jdbcTemplate.query(queryString, new Object[] { loggedInUser.toUpperCase() }, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String unit = rs.getString("unit");
				return unit;
			}
		});
		lookups.setUnitsAvailable(unitList);
	}

	public void setPrintInstanceTransmissionGetProcedure(PrintInstanceTransmissionGetProcedure printInstanceTransmissionGetProcedure) {
		this.printInstanceTransmissionGetProcedure = printInstanceTransmissionGetProcedure;
	}

	public PrintInstanceTransmissionGetProcedure getPrintInstanceTransmissionGetProcedure() {
		return printInstanceTransmissionGetProcedure;
	}

	public MTExpantionGetProcedure getMtExpantionGetProcedure() {
		return mtExpantionGetProcedure;
	}

	public void setMtExpantionGetProcedure(MTExpantionGetProcedure mtExpantionGetProcedure) {
		this.mtExpantionGetProcedure = mtExpantionGetProcedure;
	}

	public MessageDetails getMessageDetails(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, final int timeZoneOffset, String loggedInUser) throws Exception {

		List<Object> parameters = new ArrayList<Object>();

		if (enableMesgSecCheck) {

			parameters.add(loggedInUser.toUpperCase());
		}
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select m.aid, m.mesg_s_umidl, m.mesg_s_umidh," + "m.mesg_uumid,m.mesg_uumid_suffix,m.mesg_user_issued_as_pde," + "m.mesg_possible_dup_creation,m.mesg_is_partial,m.mesg_class,"
				+ "m.mesg_receiver_alia_name,m.mesg_is_live,m.mesg_is_text_readonly,m.MESG_ORDERING_INST," + "m.mesg_is_text_modified,m.mesg_is_delete_inhibited,m.mesg_frmt_name,"
				+ "m.mesg_sub_format,m.mesg_type,m.mesg_nature,m.mesg_sender_swift_address," + "m.mesg_sender_corr_type,m.mesg_sender_X1,m.mesg_sender_X2,m.mesg_sender_X3,"
				+ "m.mesg_sender_X4,m.mesg_receiver_swift_address,x1.inst_receiver_corr_type,x1.inst_crea_appl_serv_name,x1.inst_type,m.mesg_request_type,x1.inst_cbt_reference,x1.INST_NR_INDICATOR,"
				+ "x1.inst_receiver_X1,x1.inst_receiver_X2,x1.inst_receiver_X3,x1.inst_receiver_X4,x1.inst_delivery_mode," + "m.mesg_trn_ref,m.mesg_rel_trn_ref,m.x_fin_amount,m.x_fin_ccy,m.x_fin_value_date,"
				+ "m.last_update, m.archived, m.restored, NULL as request_aid,m.mesg_network_priority," + "m.mesg_delv_overdue_warn_req,m.mesg_copy_service_id,m.mesg_network_delv_notif_req,"
				+ "m.mesg_user_priority_code,m.mesg_user_reference_text,m.mesg_crea_appl_serv_name," + "m.mesg_crea_mpfn_name,m.mesg_crea_date_time,m.mesg_syntax_table_ver,"
				+ "m.mesg_validation_requested,m.mesg_network_appl_ind,m.mesg_validation_passed,"
				+ "t.text_data_block ,m.MESG_ACCOUNT_INST,m.MESG_CHARGES,m.MESG_INSTR_AMOUNT,m.MESG_EXCHANGE_RATE,m.MESG_INSTR_CCY,m.MESG_SND_CHARGES_AMOUNT,m.MESG_Status_code,m.MESG_Reason_code,m.MESG_SND_CORR,m.MESG_RCVR_CORR,m.MESG_REIMBURS_INST,m.MESG_Settlement_Method,m.MESG_Clearing_System,"
				+ "m.MESG_SND_CHARGES_CURR,m.MESG_RCV_CHARGES_CURR,m.MESG_RCV_CHARGES_AMOUNT,t.text_swift_block_5, m.mesg_release_info,m.mesg_mesg_user_group,"
				+ "m.mesg_identifier, m.mesg_requestor_dn,x1.inst_responder_dn, m.mesg_security_required, m.mesg_service," + "mesg_xml_query_ref1, mesg_xml_query_ref2, mesg_xml_query_ref3, " + "mesg_is_retrieved, mesg_network_obso_period ,"
				+ " m.mesg_user_reference_text, " + " m.MESG_SLA," + " m.mesg_user_priority_code, " + " m.mesg_e2e_transaction_reference, " + " m.MESG_IS_COPY_REQUIRED, " + " m.MESG_AUTH_DELV_NOTIF_REQ," + " m.MESG_OVERDUE_WARNING_TIME "
				+ " from rInst x1 , rMesg m left join rText t " + "on t.aid = m.aid and t.text_s_umidl = m.mesg_s_umidl and t.text_s_umidh = m.mesg_s_umidh ";
		if (isPartitioned()) {
			queryString += " and t.x_crea_date_time_mesg = m.mesg_crea_date_time ";
		}
		if (enableMesgSecCheck) {
			queryString += ", SUSER su INNER JOIN  SBICUSERGROUP ON(su.GROUPID = SBICUSERGROUP.GROUPID )" + "INNER JOIN  SMSGUSERGROUP ON(su.GROUPID = SMSGUSERGROUP.GROUPID )" + "INNER JOIN  SUNITUSERGROUP ON(su.GROUPID = SUNITUSERGROUP.GROUPID )";
			queryString += " where x1.aid = m.aid and   UPPER(su.USERNAME) = ? and (X_OWN_LT  = BICCODE) and X_CATEGORY  = CATEGORY and X_INST0_UNIT_NAME = UNIT and    x1.inst_s_umidl = m.mesg_s_umidl and x1.inst_s_umidh = m.mesg_s_umidh ";
		} else {
			queryString += " where x1.aid = m.aid and x1.inst_s_umidl = m.mesg_s_umidl and x1.inst_s_umidh = m.mesg_s_umidh ";
		}

		if (isPartitioned()) {
			queryString += " and x1.x_crea_date_time_mesg = m.mesg_crea_date_time ";
		}
		queryString += " and x1.inst_num = 0 and m.aid = ? " + " and m.mesg_s_umidl = ? " + " and m.mesg_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and m.mesg_crea_date_time = ?";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}
		List<MessageDetails> messageDetails = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<MessageDetails>() {
			@Override
			public MessageDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				if (Thread.interrupted()) {
					throw new SQLInterruptException();
				}

				MessageDetails details = new MessageDetails();
				details.setTimeZoneOffset(timeZoneOffset);
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
					details.setxFinAmount(Utils.formatAmount(new BigDecimal(string), rs.getString("x_fin_ccy"), getCurrenciesMap()));
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
				details.setInstructedAmount(Utils.formatAmount(rs.getBigDecimal("MESG_INSTR_AMOUNT"), rs.getString("MESG_INSTR_CCY"), getCurrenciesMap()));
				details.setSenderCorr(rs.getString("MESG_SND_CORR"));
				details.setRecieverCorr(rs.getString("MESG_RCVR_CORR"));
				details.setReimbInst(rs.getString("MESG_REIMBURS_INST"));
				details.setSattlmentMethod(rs.getString("MESG_Settlement_Method"));
				details.setClearingSystem(rs.getString("MESG_Clearing_System"));

				details.setStatusCode(rs.getString("MESG_Status_code"));
				details.setReasonCode(rs.getString("MESG_Reason_code"));

				details.setInstMessageCreator(rs.getString("inst_crea_appl_serv_name"));
				details.setInstMessageContext(rs.getString("inst_type"));
				details.setMesgRequestType(rs.getString("mesg_request_type"));
				details.setInstCBTReference(rs.getString("inst_cbt_reference"));
				details.setInstIsNRRequested(rs.getString("INST_NR_INDICATOR"));

				if (rs.getString("MESG_SND_CHARGES_AMOUNT") != null && !rs.getString("MESG_SND_CHARGES_AMOUNT").isEmpty()) {
					String chargesLine = rs.getString("MESG_SND_CHARGES_AMOUNT");
					String[] charges;
					if (chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						details.setMesgSndChargesAmount(charges[charges.length - 1]);
					}

					else {
						details.setMesgSndChargesAmount(rs.getString("MESG_SND_CHARGES_AMOUNT"));

					}

					details.setSenderChargeAmount(rs.getString("MESG_SND_CHARGES_AMOUNT"));

				}

				if (rs.getString("MESG_SND_CHARGES_CURR") != null && !rs.getString("MESG_SND_CHARGES_CURR").isEmpty()) {
					String currLine = rs.getString("MESG_SND_CHARGES_CURR");
					String[] currs;
					if (currLine.contains(",")) {
						currs = currLine.split(",");
						details.setMesgSndChargesCurr(currs[currs.length - 1]);
					}

					else {
						details.setMesgSndChargesCurr(rs.getString("MESG_SND_CHARGES_CURR"));

					}
					details.setSenderChargeCur(rs.getString("MESG_SND_CHARGES_CURR"));
				}

				if (details.getMesgSndChargesAmount() != null && !details.getMesgSndChargesAmount().isEmpty()) {
					details.setMesgSndChargesAmount(Utils.formatAmount(new BigDecimal(details.getMesgSndChargesAmount().trim()), details.getMesgSndChargesCurr(), getCurrenciesMap()).toString());
				}

				if (rs.getString("MESG_RCV_CHARGES_AMOUNT") != null && !rs.getString("MESG_RCV_CHARGES_AMOUNT").isEmpty()) {
					String chargesLine = rs.getString("MESG_SND_CHARGES_AMOUNT");
					String[] charges;
					if (chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						details.setMesgRcvChargesAmount(charges[charges.length - 1]);
					}

					else {
						details.setMesgRcvChargesAmount(rs.getString("MESG_RCV_CHARGES_AMOUNT"));

					}

				}

				if (rs.getString("MESG_RCV_CHARGES_CURR") != null && !rs.getString("MESG_RCV_CHARGES_CURR").isEmpty()) {
					String currLine = rs.getString("MESG_RCV_CHARGES_CURR");
					String[] currs;
					if (currLine.contains(",")) {
						currs = currLine.split(",");
						details.setMesgRcvChargesCurr(currs[currs.length - 1]);
					}

					else {
						details.setMesgRcvChargesCurr(rs.getString("MESG_RCV_CHARGES_CURR"));

					}

				}

				if (details.getMesgRcvChargesAmount() != null && !details.getMesgRcvChargesAmount().isEmpty()) {
					details.setMesgRcvChargesAmount(Utils.formatAmount(new BigDecimal(details.getMesgRcvChargesAmount().trim()), details.getMesgRcvChargesCurr(), getCurrenciesMap()).toString());
				}

				details.setOrdringInstution(rs.getString("MESG_ORDERING_INST"));
				details.setMesgExchangeRate(rs.getString("MESG_EXCHANGE_RATE"));
				details.setMesgInstructedCur(rs.getString("MESG_INSTR_CCY"));

				return details;
			}

		});

		if (messageDetails.size() == 0) {
			throw new Exception(String.format("Cannot get details for message( aid=%d, umidl=%d, umidh=%d ), make sure that the message has instances.", aid, umidl, umidh));
		}
		return messageDetails.get(0);
	}

	public String getSelectedAid(Set<Integer> selectedAid) {
		String aIdQuery = "";
		if (selectedAid != null && !selectedAid.isEmpty()) {

			for (Integer tmp : selectedAid) {
				String val = tmp.toString();
				if (val != null) {
					aIdQuery += val;
					aIdQuery += ", ";
				}
			}
			aIdQuery = aIdQuery.substring(0, aIdQuery.length() - 2);
		}
		return aIdQuery;
	}

	public String getSelectedUmidl(Set<Integer> selectedUmidl) {
		String umidlQuery = "";
		if (selectedUmidl != null && !selectedUmidl.isEmpty()) {

			for (Integer tmp : selectedUmidl) {
				String val = tmp.toString();
				if (val != null) {
					umidlQuery += val;
					umidlQuery += ", ";
				}
			}
			umidlQuery = umidlQuery.substring(0, umidlQuery.length() - 2);
		}
		return umidlQuery;
	}

	public String getSelectedUmidh(Set<Integer> selectedUmidh) {
		String umidhQuery = "";
		if (selectedUmidh != null && !selectedUmidh.isEmpty()) {

			for (Integer tmp : selectedUmidh) {
				String val = tmp.toString();
				if (val != null) {
					umidhQuery += val;
					umidhQuery += ", ";
				}
			}
			umidhQuery = umidhQuery.substring(0, umidhQuery.length() - 2);
		}
		return umidhQuery;
	}

	public String getSelectedCreaTime(Set<java.util.Date> creaTimeList) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String creaMesgTimeQuery = "";

		if (creaTimeList != null && !creaTimeList.isEmpty()) {
			for (java.util.Date tmp : creaTimeList) {
				Timestamp ts = new Timestamp(tmp.getTime());
				creaMesgTimeQuery += "TO_DATE('";
				creaMesgTimeQuery += formatter.format(ts);
				creaMesgTimeQuery += "', 'yyyy-MM-dd HH24:MI:SS')";
				creaMesgTimeQuery += ", ";
			}
			creaMesgTimeQuery = creaMesgTimeQuery.substring(0, creaMesgTimeQuery.length() - 2);
		}
		return creaMesgTimeQuery;
	}

	@Override
	public List<MessageDetails> getMessageDetailsList(List<SearchResultEntity> messages, String loggedInUser) throws Exception {
		final Integer timeZoneOffset = messages.get(0).getTimeZoneOffset();

		Set<Integer> aidList = new HashSet<Integer>();
		Set<Integer> umidlList = new HashSet<Integer>();
		Set<Integer> umidhList = new HashSet<Integer>();
		Set<java.util.Date> creaTimeList = new HashSet<java.util.Date>();
		for (SearchResultEntity message : messages) {
			aidList.add(message.getAid());
			umidlList.add(message.getMesgUmidl());
			umidhList.add(message.getMesgUmidh());
			creaTimeList.add(message.getMesgCreaDateTime());
		}

		String queryString = "select m.aid, m.mesg_s_umidl, m.mesg_s_umidh," + "m.mesg_uumid,m.mesg_uumid_suffix,m.mesg_user_issued_as_pde," + "m.mesg_possible_dup_creation,m.mesg_is_partial,m.mesg_class,"
				+ "m.mesg_receiver_alia_name,m.mesg_is_live,m.mesg_is_text_readonly,m.MESG_ORDERING_INST," + "m.mesg_is_text_modified,m.mesg_is_delete_inhibited,m.mesg_frmt_name,"
				+ "m.mesg_sub_format,m.mesg_type,m.mesg_nature,m.mesg_sender_swift_address," + "m.mesg_sender_corr_type,m.mesg_sender_X1,m.mesg_sender_X2,m.mesg_sender_X3," + "m.mesg_sender_X4,m.mesg_receiver_swift_address, m.X_RECEIVER_X1, "
				+ "m.mesg_trn_ref,m.mesg_rel_trn_ref,m.x_fin_amount,m.x_fin_ccy,m.x_fin_value_date," + "m.last_update, m.archived, m.restored, NULL as request_aid,m.mesg_network_priority,"
				+ "m.mesg_delv_overdue_warn_req,m.mesg_copy_service_id,m.mesg_network_delv_notif_req," + "m.mesg_user_priority_code,m.mesg_user_reference_text,m.mesg_crea_appl_serv_name,"
				+ "m.mesg_crea_mpfn_name,m.mesg_crea_date_time,m.mesg_syntax_table_ver," + "m.mesg_validation_requested,m.mesg_network_appl_ind,m.mesg_validation_passed," + "t.text_data_block,"
				+ "m.MESG_ACCOUNT_INST,m.MESG_CHARGES,m.MESG_INSTR_AMOUNT,m.MESG_EXCHANGE_RATE,m.MESG_INSTR_CCY,m.MESG_SND_CHARGES_AMOUNT,m.MESG_Status_code,m.MESG_Reason_code,m.MESG_SND_CORR,m.MESG_RCVR_CORR,m.MESG_REIMBURS_INST,m.MESG_Settlement_Method,m.MESG_Clearing_System,"
				+ "m.MESG_SND_CHARGES_CURR,m.MESG_RCV_CHARGES_CURR,m.MESG_RCV_CHARGES_AMOUNT,t.text_swift_block_5, m.mesg_release_info,m.mesg_mesg_user_group," + "m.mesg_identifier, m.mesg_requestor_dn, m.mesg_security_required, m.mesg_service,"
				+ "mesg_xml_query_ref1, mesg_xml_query_ref2, mesg_xml_query_ref3, " + "mesg_is_retrieved, mesg_network_obso_period ," + " m.mesg_user_reference_text, " + " m.MESG_SLA," + " m.mesg_user_priority_code, "
				+ " m.mesg_e2e_transaction_reference, " + " m.MESG_IS_COPY_REQUIRED, " + " m.MESG_AUTH_DELV_NOTIF_REQ," + " m.MESG_OVERDUE_WARNING_TIME " + " from rMesg m left join rText t "
				+ "on t.aid = m.aid and t.text_s_umidl = m.mesg_s_umidl and t.text_s_umidh = m.mesg_s_umidh ";
		if (isPartitioned()) {
			queryString += " and t.x_crea_date_time_mesg = m.mesg_crea_date_time ";
		}

		queryString += " where m.aid IN (" + getSelectedAid(aidList) + ")  and m.mesg_s_umidl IN (" + getSelectedUmidl(umidlList) + ") and m.mesg_s_umidh IN (" + getSelectedUmidh(umidhList) + ")";

		if (isPartitioned()) {
			queryString += " and m.mesg_crea_date_time IN (" + getSelectedCreaTime(creaTimeList) + " )";
		}

		List<MessageDetails> messageDetails = jdbcTemplate.query(queryString, new RowMapper<MessageDetails>() {
			@Override
			public MessageDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				if (Thread.interrupted()) {
					throw new SQLInterruptException();
				}

				MessageDetails details = new MessageDetails();
				details.setTimeZoneOffset(timeZoneOffset);
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
				details.setMesgReciverX1(rs.getString("X_RECEIVER_X1"));
				details.setMesgTrnRef(rs.getString("mesg_trn_ref"));
				details.setMesgRelTrnRef(rs.getString("mesg_rel_trn_ref"));

				Object amountObj = rs.getObject("x_fin_amount");
				if (amountObj != null) {
					String string = amountObj.toString().trim();
					string = string.replaceAll("[^\\d.]", "");
					details.setxFinAmount(Utils.formatAmount(new BigDecimal(string), rs.getString("x_fin_ccy"), getCurrenciesMap()));
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
				details.setInstructedAmount(Utils.formatAmount(rs.getBigDecimal("MESG_INSTR_AMOUNT"), rs.getString("MESG_INSTR_CCY"), getCurrenciesMap()));
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
					if (chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						details.setMesgSndChargesAmount(charges[charges.length - 1]);
					}

					else {
						details.setMesgSndChargesAmount(rs.getString("MESG_SND_CHARGES_AMOUNT"));

					}

					details.setSenderChargeAmount(rs.getString("MESG_SND_CHARGES_AMOUNT"));

				}

				if (rs.getString("MESG_SND_CHARGES_CURR") != null && !rs.getString("MESG_SND_CHARGES_CURR").isEmpty()) {
					String currLine = rs.getString("MESG_SND_CHARGES_CURR");
					String[] currs;
					if (currLine.contains(",")) {
						currs = currLine.split(",");
						details.setMesgSndChargesCurr(currs[currs.length - 1]);
					}

					else {
						details.setMesgSndChargesCurr(rs.getString("MESG_SND_CHARGES_CURR"));

					}
					details.setSenderChargeCur(rs.getString("MESG_SND_CHARGES_CURR"));
				}

				if (details.getMesgSndChargesAmount() != null && !details.getMesgSndChargesAmount().isEmpty()) {
					details.setMesgSndChargesAmount(Utils.formatAmount(new BigDecimal(details.getMesgSndChargesAmount().trim()), details.getMesgSndChargesCurr(), getCurrenciesMap()).toString());
				}

				if (rs.getString("MESG_RCV_CHARGES_AMOUNT") != null && !rs.getString("MESG_RCV_CHARGES_AMOUNT").isEmpty()) {
					String chargesLine = rs.getString("MESG_SND_CHARGES_AMOUNT");
					String[] charges;
					if (chargesLine.contains(",")) {
						charges = chargesLine.split(",");
						details.setMesgRcvChargesAmount(charges[charges.length - 1]);
					}

					else {
						details.setMesgRcvChargesAmount(rs.getString("MESG_RCV_CHARGES_AMOUNT"));

					}

				}

				if (rs.getString("MESG_RCV_CHARGES_CURR") != null && !rs.getString("MESG_RCV_CHARGES_CURR").isEmpty()) {
					String currLine = rs.getString("MESG_RCV_CHARGES_CURR");
					String[] currs;
					if (currLine.contains(",")) {
						currs = currLine.split(",");
						details.setMesgRcvChargesCurr(currs[currs.length - 1]);
					}

					else {
						details.setMesgRcvChargesCurr(rs.getString("MESG_RCV_CHARGES_CURR"));

					}

				}

				if (details.getMesgRcvChargesAmount() != null && !details.getMesgRcvChargesAmount().isEmpty()) {
					details.setMesgRcvChargesAmount(Utils.formatAmount(new BigDecimal(details.getMesgRcvChargesAmount().trim()), details.getMesgRcvChargesCurr(), getCurrenciesMap()).toString());
				}

				details.setOrdringInstution(rs.getString("MESG_ORDERING_INST"));
				details.setMesgExchangeRate(rs.getString("MESG_EXCHANGE_RATE"));
				details.setMesgInstructedCur(rs.getString("MESG_INSTR_CCY"));

				return details;
			}

		});

		if (messageDetails.size() == 0) {
			throw new Exception(String.format("Cannot get details for message, make sure that the messages has instances."));
		}
		return messageDetails;
	}

	// To be uncommented for TFS 59252
	// private String getMessageSecurityData(List<Object> queryBindingVar) {
	//
	//
	// String bicsInParam = "and X_OWN_LT IN (" + prepareInClauseValuesFromCache(securityDataBean.getAllLicenseBics(), queryBindingVar) + ")";
	//
	// String bicCodeCondition = bicsInParam;
	//
	// String value = SEPERATOR + bicCodeCondition + SEPERATOR + " AND " + "X_INST0_UNIT_NAME IN (" + prepareInClauseValuesFromCache(securityDataBean.getAllUnitsList(), queryBindingVar) + ")"
	// + SEPERATOR + " AND X_CATEGORY IN (" + prepareInClauseValuesFromCache(securityDataBean.getAllCategoriesList(), queryBindingVar) + ") " + SEPERATOR;
	//
	// return value;
	// }
	//
	// private static String prepareInClauseValuesFromCache(List<String> values, List<Object> queryBindingVar) {
	// List<String> varList = new ArrayList<String>();
	// for (String str : values) {
	// varList.add("?");
	// queryBindingVar.add(str);
	// }
	// return StringUtils.join(varList, ",");
	//
	// }

	@Override
	public List<TextFieldData> getTextFieldData(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select field_code,field_option,value,value_memo  from  rTextField  where aid = ? " + " and text_s_umidl = ? " + " and text_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and X_CREA_DATE_TIME_MESG = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));

		}

		queryString += " order by field_cnt";
		List<TextFieldData> textFieldDataList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<TextFieldData>() {
			@Override
			public TextFieldData mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer field_code = rs.getInt("field_code");
				String field_option = rs.getString("field_option");
				String value = rs.getString("value");
				Clob value_memo = rs.getClob("value_memo");

				TextFieldData textFieldData = new TextFieldData();
				textFieldData.setFieldCode(field_code);
				textFieldData.setFieldOption(field_option);
				textFieldData.setValue(value);
				textFieldData.setValueMemo(value_memo);
				return textFieldData;
			}
		});
		return textFieldDataList;
	}

	@Override
	public String getTextMessages(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select * from  rtext  where aid = ? " + " and text_s_umidl = ? " + " and text_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and X_CREA_DATE_TIME_MESG = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));

		}
		List<String> textList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("TEXT_DATA_BLOCK");
			}
		});

		if (textList == null || textList.isEmpty()) {
			return "";
		}
		return textList.get(0);
	}

	@Override
	public List<TextFieldData> getSystemTextFieldData(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select field_code,value from rSystemTextField  where aid = ? " + " and text_s_umidl = ? " + " and text_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		queryString += " order by field_cnt";
		List<TextFieldData> textFieldDataList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<TextFieldData>() {
			@Override
			public TextFieldData mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer field_code = rs.getInt("field_code");
				String value = rs.getString("value");

				TextFieldData textFieldData = new TextFieldData();
				textFieldData.setFieldCode(field_code);
				textFieldData.setFieldOption(null);
				textFieldData.setValue(value);
				textFieldData.setValueMemo(null);
				return textFieldData;
			}
		});
		return textFieldDataList;
	}

	@Override
	public List<MessageNote> getMessageNotesList(int aid, int umidl, int umidh) {
		Object[] parameters = new Object[] { aid, umidl, umidh };

		String queryString = "SELECT * FROM RMESG_NOTES WHERE AID = ? AND MESG_S_UMIDL = ? AND MESG_S_UMIDH = ? ORDER BY CREATION_DATE";

		List<MessageNote> messageNotes = jdbcTemplate.query(queryString, parameters, new RowMapper<MessageNote>() {

			@Override
			public MessageNote mapRow(ResultSet rs, int rowNum) throws SQLException {
				MessageNote messageNote = new MessageNote();
				messageNote.setNoteId(rs.getLong("NOTE_ID"));
				messageNote.setAid(rs.getInt("AID"));
				messageNote.setMesgUmidl(rs.getInt("MESG_S_UMIDL"));
				messageNote.setMesgUmidh(rs.getInt("MESG_S_UMIDH"));
				messageNote.setCreationDate(rs.getTimestamp("CREATION_DATE"));
				messageNote.setNote(rs.getString("NOTE"));
				/*
				 * mock user object just to set the user id for later fetching the full user details
				 */
				User createdBy = new User();
				createdBy.setUserId(rs.getLong("CREATED_BY"));
				messageNote.setCreatedBy(createdBy);
				return messageNote;
			}

		});
		return messageNotes;
	}

	@Override
	public List<InstanceDetails> getInstanceList(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, final int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select " + "inst_num, inst_rp_name, inst_status, inst_type, inst_crea_appl_serv_name, inst_crea_rp_name, "
				+ " inst_crea_date_time , inst_crea_mpfn_name, inst_mpfn_name, inst_related_nbr, inst_unit_name, inst_responder_dn,inst_nr_indicator " + " from rInst  where aid = ? and inst_s_umidl = ? and inst_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));

		}

		queryString += " order by inst_num";

		List<InstanceDetails> instances = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<InstanceDetails>() {
			@Override
			public InstanceDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				InstanceDetails instance = new InstanceDetails();
				instance.setTimeZoneOffset(timeZoneOffset);
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
	public List<InterventionDetails> getInterventionList(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int instance_no, final int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select intv_seq_nbr,intv_date_time,intv_text,intv_oper_nickname,intv_inty_category,intv_inty_name from vIntv where" + " aid = ? " + " and intv_s_umidl = ? " + " and intv_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		parameters.add(instance_no);
		queryString += " and intv_inst_num = ? " + " and intv_inty_category = 'INTY_ROUTING' order by intv_date_time, intv_seq_nbr";

		List<InterventionDetails> interventions = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<InterventionDetails>() {
			@Override
			public InterventionDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				InterventionDetails intervention = new InterventionDetails();
				intervention.setTimeZoneOffset(timeZoneOffset);
				intervention.setIntvSeqNbr(rs.getLong("intv_seq_nbr"));
				intervention.setIntvDateTime(new Date(rs.getTimestamp("intv_date_time").getTime()));
				intervention.setIntvText(rs.getClob("intv_text"));
				intervention.setIntvOperNickname(rs.getString("intv_oper_nickname"));
				intervention.setIntvIntyCategory(rs.getString("intv_inty_category"));
				intervention.setIntvIntyName(rs.getString("intv_inty_name"));

				return intervention;
			}
		});
		return interventions;
	}

	@Override
	public List<InterventionDetails> getInstanceInterventionList(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int instance_no, final int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select intv_seq_nbr,intv_date_time,intv_text,intv_oper_nickname,intv_inty_category,intv_inty_name from vIntv where " + " aid = ? " + " and intv_s_umidl = ? " + " and intv_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		parameters.add(instance_no);
		queryString += " and intv_inst_num = ? " + " order by intv_seq_nbr";

		List<InterventionDetails> interventions = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<InterventionDetails>() {
			@Override
			public InterventionDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				InterventionDetails intervention = new InterventionDetails();
				intervention.setIntvSeqNbr(rs.getLong("intv_seq_nbr"));
				intervention.setIntvDateTime(new Date(rs.getTimestamp("intv_date_time").getTime()));
				intervention.setIntvText(rs.getClob("intv_text"));
				intervention.setIntvOperNickname(rs.getString("intv_oper_nickname"));
				intervention.setIntvIntyCategory(rs.getString("intv_inty_category"));
				intervention.setIntvIntyName(rs.getString("intv_inty_name"));

				return intervention;
			}
		});
		return interventions;
	}

	@Override
	public List<AppendixDetails> getAppendixList(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int instance_no, final int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select appe_seq_nbr, appe_date_time, appe_type, appe_iapp_name, appe_session_holder, appe_session_nbr,"
				+ " appe_sequence_nbr, appe_network_delivery_status, appe_ack_nack_text, appe_pki_auth_result, appe_pki_authorisation_res," + " appe_pki_pac2_result, appe_rma_check_result   from vAppe  where " + " aid = ? " + " and appe_s_umidl = ? "
				+ " and appe_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			Calendar cal = Calendar.getInstance();
			cal.setTime(mesg_crea_date);
			cal.add(Calendar.HOUR, -timeZoneOffset);
			parameters.add(cal.getTime());
		}

		parameters.add(instance_no);
		queryString += " and appe_inst_num = ?  order by appe_date_time, appe_seq_nbr";

		List<AppendixDetails> appendices = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<AppendixDetails>() {
			@Override
			public AppendixDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				AppendixDetails appe = new AppendixDetails();
				appe.setTimeZoneOffset(timeZoneOffset);
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
				return appe;
			}
		});
		return appendices;
	}

	@Override
	public void forceMessageUpdate(int aid, int umidl, int umidh, java.util.Date mesg_crea_date) {
		forceMessageUpdateProcedure.execute(aid, umidl, umidh, mesg_crea_date);
	}

	public void setForceMessageUpdateProcedure(ForceMessageUpdateProcedure forceMessageUpdateProcedure) {
		this.forceMessageUpdateProcedure = forceMessageUpdateProcedure;
	}

	public ForceMessageUpdateProcedure getForceMessageUpdateProcedure() {
		return forceMessageUpdateProcedure;
	}

	@Override
	public List<String> getIntvText(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int inst_num, int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select INTV_TEXT from  vIntv  where aid = ? and intv_s_umidl = ? and intv_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}
		parameters.add(inst_num);
		queryString += " and intv_inst_num = ? and INTV_TEXT like '%with result%;%' order by intv_seq_nbr desc ";
		List<String> intvTextList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				Clob intvTextClob = rs.getClob("INTV_TEXT");
				if (intvTextClob != null)
					return ApplicationUtils.convertClob2String(intvTextClob);
				return null;
			}
		});
		return intvTextList;
	}

	@Override
	public InstanceExtDetails getInstanceDetails(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int inst_num, final int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select " + " inst_type, inst_notification_type, inst_unit_name, inst_status, inst_rp_name, inst_mpfn_name," + " inst_last_mpfn_result, inst_receiver_x1, inst_receiver_institution_name, inst_receiver_branch_info,"
				+ " inst_receiver_city_name, inst_receiver_ctry_name, inst_receiver_ctry_code, inst_routing_code," + " inst_receiver_network_iapp_nam , inst_crea_appl_serv_name, inst_crea_rp_name, inst_crea_date_time,"
				+ " inst_oper_comment, inst_disp_address_code, inst_receiver_corr_type, inst_receiver_x1, inst_receiver_x2,"
				+ " inst_receiver_x3, inst_receiver_x4, inst_crea_mpfn_name, inst_related_nbr, inst_responder_dn, inst_nr_indicator , inst_sm2000_priority " + " from rInst  where" + " aid = ? " + " and inst_s_umidl = ? " + " and inst_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		parameters.add(inst_num);
		queryString += " and inst_num = ? order by inst_num ";

		InstanceExtDetails instanceExtDetails = jdbcTemplate.queryForObject(queryString, parameters.toArray(), new RowMapper<InstanceExtDetails>() {
			@Override
			public InstanceExtDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				InstanceExtDetails instance = new InstanceExtDetails();
				instance.setTimeZoneOffset(timeZoneOffset);
				instance.setInstStatus(rs.getString("inst_status"));
				instance.setInstType(rs.getString("inst_type"));
				instance.setInstCreaApplServName(rs.getString("inst_crea_appl_serv_name"));
				instance.setInstCreaDateTime(new Date(rs.getTimestamp("inst_crea_date_time").getTime()));
				instance.setInstCreaMpfnName(rs.getString("inst_crea_mpfn_name"));
				instance.setInstCreaRpName(rs.getString("inst_crea_rp_name"));
				instance.setInstRpName(rs.getString("inst_rp_name"));

				instance.setInstRelatedNbr(rs.getInt("inst_related_nbr"));
				instance.setInstUnitName(rs.getString("inst_unit_name"));
				instance.setInstResponderDn(rs.getString("inst_responder_dn"));
				instance.setInstNrIndicator(rs.getString("inst_nr_indicator"));

				instance.setInstNotificationType(rs.getString("inst_notification_type"));
				instance.setInstMpfnName(rs.getString("inst_mpfn_name"));
				instance.setInstLastMpfnResult(rs.getString("inst_last_mpfn_result"));
				instance.setInstReceiverInstitutionName(rs.getString("inst_receiver_institution_name"));
				instance.setInstReceiverBranchInfo(rs.getString("inst_receiver_branch_info"));
				instance.setInstReceiverCityName(rs.getString("inst_receiver_city_name"));
				instance.setInstReceiverCtryName(rs.getString("inst_receiver_ctry_name"));
				instance.setInstReceiverCtryCode(rs.getString("inst_receiver_ctry_code"));
				instance.setInstRoutingCode(rs.getString("inst_routing_code"));
				instance.setInstReceiverNetworkIAppNam(rs.getString("inst_receiver_network_iapp_nam"));
				instance.setInstOperComment(rs.getString("inst_oper_comment"));
				instance.setInstDispAddressCode(rs.getString("inst_disp_address_code"));
				instance.setInstReceiverCorrType(rs.getString("inst_receiver_corr_type"));
				instance.setInstReceiverX1(rs.getString("inst_receiver_x1"));
				instance.setInstReceiverX2(rs.getString("inst_receiver_x2"));
				instance.setInstReceiverX3(rs.getString("inst_receiver_x3"));
				instance.setInstReceiverX4(rs.getString("inst_receiver_x4"));
				instance.setInstSm2000Priority(rs.getString("inst_sm2000_priority"));

				return instance;
			}
		});
		if (instanceExtDetails != null) {
			instanceExtDetails.setInstNum(inst_num);
		}
		return instanceExtDetails;
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
		if (isPartitioned()) {
			queryString += " and a.x_crea_date_time_mesg = i.x_crea_date_time_mesg";
		}
		queryString += " and a.aid = ? and appe_s_umidl = ? and appe_s_umidh = ? and appe_inst_num = ? ";

		if (isPartitioned()) {
			queryString += " and a.x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		parameters.add(new Timestamp(intv_date_time.getTime()));
		parameters.add(intv_seq_num);
		queryString += " and appe_date_time = ?  and appe_seq_nbr = ? ";

		AppendixExtDetails appeExtDetails = jdbcTemplate.queryForObject(queryString, parameters.toArray(), new RowMapper<AppendixExtDetails>() {
			@Override
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

	@Override
	public InterventionExtDetails getInterventionDetails(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int inst_num, Long intv_seq_num, java.util.Date intv_date_time, final int timeZoneOffset) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);
		parameters.add(inst_num);

		String queryString = "select inst_unit_name, intv_inty_name, intv_inty_category, intv_oper_nickname, intv_appl_serv_name," + " intv_mpfn_name, intv_date_time, intv_text" + " from  vIntv  t, rInst  i"
				+ " Where  t.aid = i.aid and t.intv_s_umidl = i.inst_s_umidl and t.intv_s_umidh = i.inst_s_umidh and t.intv_inst_num = i.inst_num";
		if (isPartitioned()) {
			queryString += " and t.x_crea_date_time_mesg = i.x_crea_date_time_mesg ";
		}
		queryString += " and t.aid = ? and intv_s_umidl = ? and intv_s_umidh = ? and intv_inst_num = ? ";

		if (isPartitioned()) {
			queryString += " and t.x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		parameters.add(new Timestamp(intv_date_time.getTime()));
		parameters.add(intv_seq_num);
		queryString += " and intv_date_time = ? and intv_seq_nbr = ? ";

		InterventionExtDetails intvExtDetails = jdbcTemplate.queryForObject(queryString, parameters.toArray(), new RowMapper<InterventionExtDetails>() {
			@Override
			public InterventionExtDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				InterventionExtDetails intvDetails = new InterventionExtDetails();
				intvDetails.setTimeZoneOffset(timeZoneOffset);
				intvDetails.setInstUnitName(rs.getString("inst_unit_name"));
				intvDetails.setIntvIntyName(rs.getString("intv_inty_name"));
				intvDetails.setIntvIntyCategory(rs.getString("intv_inty_category"));
				intvDetails.setIntvOperNickname(rs.getString("intv_oper_nickname"));
				intvDetails.setIntvApplServName(rs.getString("intv_appl_serv_name"));
				intvDetails.setIntvMpfnName(rs.getString("intv_mpfn_name"));
				intvDetails.setIntvDateTime(new Date(rs.getTimestamp("intv_date_time").getTime()));
				intvDetails.setIntvText(rs.getClob("intv_text"));

				return intvDetails;
			}
		});
		return intvExtDetails;
	}

	private List<MessageDetails> executeRelatedMessagesQuery(final String xmlQueryReference1, final String xmlQueryReference2, final java.util.Date sourceMessageDate, final int period) throws SQLException {
		if (Thread.interrupted()) {
			cancleSearch();
			throw new SQLInterruptException();
		}

		final String query = "select m.AID AID,m.MESG_S_UMIDH UMIDH,m.MESG_CREA_DATE_TIME CREATION_DATE,m.MESG_S_UMIDL UMIDL,m.MESG_UUMID UUMID,m.MESG_IDENTIFIER MESG_IDENTIFIER,x.IDENTIFIER_NAME IDENTIFIER_NAME,m.MESG_USER_REFERENCE_TEXT REFERENCE_TEXT,"
				+ "m.MESG_UUMID_SUFFIX UUMID_SUFFIX,m.MESG_SERVICE SERVICE,m.ARCHIVED ARCHIVED FROM RMESG m INNER JOIN XMLTYPES x ON m.MESG_IDENTIFIER = x.IDENTIFIER_VALUE "
				+ "WHERE (m.MESG_XML_QUERY_REF2 = ? or m.MESG_XML_QUERY_REF1 = ?) and m.MESG_CREA_DATE_TIME between ? and ?";

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(sourceMessageDate);
		toCalendar.add(Calendar.DAY_OF_MONTH, period);
		final Date toDate = new Date(toCalendar.getTimeInMillis());
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(sourceMessageDate);
		fromCalendar.add(Calendar.DAY_OF_MONTH, period * -1);
		final Date fromDate = new Date(fromCalendar.getTimeInMillis());

		List<MessageDetails> detailsEntity = jdbcTemplate.query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, xmlQueryReference1);
				statement.setString(2, xmlQueryReference2);
				statement.setDate(3, fromDate);
				statement.setDate(4, toDate);
				return statement;
			}
		}, new RowMapper<MessageDetails>() {

			@Override
			public MessageDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				Date dt;
				if (Thread.interrupted()) {
					cancleSearch();
					throw new SQLInterruptException();
				}
				MessageDetails message = new MessageDetails();
				message.setAid((rs.getInt("AID")));
				message.setMesgUmidh(rs.getInt("UMIDH"));
				message.setMesgUmidl(rs.getInt("UMIDL"));
				dt = null;
				if (rs.getTimestamp("CREATION_DATE") != null) {
					dt = new Date(rs.getTimestamp("CREATION_DATE").getTime());
				}
				message.setMesgCreaDateTime(dt);
				message.setMesgCreaDateTimeStr(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dt));
				message.setMesgUumid(rs.getString("UUMID"));
				message.setMesgSubFormat(rs.getString("UUMID").substring(0, 1));
				message.setCorrespondent(message.getMesgUumid().substring(1, 11));
				message.setMesgIdentifier(rs.getString("MESG_IDENTIFIER"));
				message.setIdentifierDescription(rs.getString("IDENTIFIER_NAME"));
				message.setMesgUserReferenceText(rs.getString("REFERENCE_TEXT"));
				message.setMesgUumidSuffix(rs.getInt("UUMID_SUFFIX"));
				message.setMesgService(rs.getString("SERVICE"));
				message.setArchived(rs.getInt("ARCHIVED") == 0 ? false : true);
				return message;
			}
		});
		return detailsEntity;
	}

	@Override
	public List<MessageDetails> findRelatedMessages(String xmlQueryReference1, String xmlQueryReference2, java.util.Date sourceMessageDate, int period) throws SQLException {
		return executeRelatedMessagesQuery(xmlQueryReference1, xmlQueryReference2, sourceMessageDate, period);
	}

	@Override
	public List<SearchResultEntity> searchHL(int umidl, int umidh, Long userGroupID, Integer aid) throws SQLException {
		String query = "select m.aid, m.mesg_s_umidl, m.mesg_s_umidh, m.mesg_sub_format, m.mesg_type, m.mesg_uumid, m.mesg_sender_X1, m.mesg_trn_ref, m.mesg_user_reference_text, m.mesg_crea_date_time, m.x_fin_value_date, "
				+ " m.x_fin_amount, m.x_fin_ccy, m.mesg_frmt_name, m.mesg_identifier, m.mesg_uumid_suffix, m.mesg_status, m.mesg_mesg_user_group, m.MESG_ORDER_CUS, m.MESG_ORDERING_INST, m.MESG_BEN_CUST, m.MESG_ACCOUNT_INST, "
				+ " m.MESG_CHARGES, m.MESG_RCV_CHARGES_AMOUNT, m.MESG_SND_CHARGES_AMOUNT, m.X_INST0_RP_NAME, m.MESG_SND_CHARGES_CURR, m.MESG_Reason_code, m.MESG_Status_code, m.MESG_status_originator, m.MESG_SND_CORR, "
				+ " m.MESG_RCVR_CORR, m.MESG_REIMBURS_INST, m.MESG_forwarded_to, m.MESG_NAK_code, m.MESG_INSTR_AMOUNT, m.MESG_INSTR_CCY, m.mesg_syntax_table_ver, m.mesg_rel_trn_ref, m.mesg_service, m.MESG_SLA, "
				+ " m.mesg_e2e_transaction_reference, m.mesg_xml_query_ref1, m.mesg_xml_query_ref2, m.mesg_xml_query_ref3, m.MESG_EXCHANGE_RATE, NOTIF_DATE_TIME , m.MESG_Settlement_Method ,m.MESG_Clearing_System , "
				+ " x1.inst_rp_name, x1.inst_receiver_X1, y1.appe_iapp_name as emi_iapp_name, y1.appe_session_nbr as emi_session_nbr, y1.appe_sequence_nbr as emi_sequence_nbr, "
				+ " y1.appe_network_delivery_status as emi_network_delivery_status, y2.appe_iapp_name as rec_iapp_name, y2.appe_session_nbr as rec_session_nbr, y2.appe_sequence_nbr as rec_sequence_nbr from"
				+ " ( select rMesg.* from rMesg, SBICUSERGROUP, SMSGUSERGROUP, SUNITUSERGROUP WHERE SBICUSERGROUP.GROUPID = ? AND SMSGUSERGROUP.GROUPID = ? AND SUNITUSERGROUP.GROUPID = ?"
				+ " AND (X_OWN_LT = BICCODE  or X_OWN_LT ='XXXXXXXX') AND X_CATEGORY = CATEGORY AND X_INST0_UNIT_NAME = UNIT AND mesg_s_umidl = ? and mesg_s_umidh= ?" + addAid(aid) + " 	) m left outer join (rInst x1"
				+ " left outer join rAppe y1 on x1.Aid = y1.Aid And x1.inst_s_umidl = y1.appe_s_umidl And x1.inst_s_umidh = y1.appe_s_umidh And" + addPartitioned(" x1.X_CREA_DATE_TIME_MESG = y1.X_CREA_DATE_TIME_MESG And ")
				+ " x1.inst_num = y1.appe_inst_num And x1.x_last_emi_appe_date_time = y1.appe_date_time And x1.x_last_emi_appe_seq_nbr = y1.appe_seq_nbr left outer join rAppe y2 on x1.Aid = y2.Aid And"
				+ " x1.inst_s_umidl = y2.appe_s_umidl And x1.inst_s_umidh = y2.appe_s_umidh And" + addPartitioned(" x1.X_CREA_DATE_TIME_MESG = y2.X_CREA_DATE_TIME_MESG And ")
				+ " x1.inst_num = y2.appe_inst_num And x1.x_last_rec_appe_date_time = y2.appe_date_time And x1.x_last_rec_appe_seq_nbr = y2.appe_seq_nbr) on m.aid = x1.aid and m.mesg_s_umidl = x1.inst_s_umidl and"
				+ " m.mesg_s_umidh = x1.inst_s_umidh and" + addPartitioned(" m.mesg_crea_date_time= x1.X_CREA_DATE_TIME_MESG and ") + " x1.inst_num = 0";

		if (addAid(aid).isEmpty()) {
			List<Object> parameter = Arrays.asList(userGroupID, userGroupID, userGroupID, umidl, umidh);
			return execSearchQuery(query, null, null, parameter);
		}

		List<Object> parameter = Arrays.asList(userGroupID, userGroupID, userGroupID, umidl, umidh, aid);
		return execSearchQuery(query, null, null, parameter);
	}

	private String addPartitioned(String str) {
		if (isPartitioned())
			return str;
		return "";
	}

	private String addAid(Integer aid) {
		if (aid != null)
			return " and aid= ?";
		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String setAppeNonRepudiationType(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int instNum) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select appe_nr_indicator  from rAppe where aid = ? and appe_s_umidl = ? and appe_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}
		parameters.add(instNum);
		queryString += " and appe_inst_num = ? ";
		List<String> appeNonrepType = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("appe_nr_indicator");
			}
		});
		if (appeNonrepType.size() != 0) {
			return appeNonrepType.get(0);
		}

		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isBeingUpdated(int aid, int umidl, int umidh, java.util.Date mesg_crea_date) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select aid from ldRequestUpdate where aid = ? and mesg_s_umidl = ? and mesg_s_umidh = ? ";
		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}
		List<String> aids = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("aid");
			}
		});
		if (aids.size() != 0) {
			return true;
		}
		return false;
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

		if (applicationFeatures.isFilePayloadSupported()) {
			queryString += ", CASE WHEN  payload is not null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload) <> null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload) >  0  THEN '" + PayloadType.BINARY_TAG + "' "
					+ "WHEN  payload_text is not null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload_text) <> null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload_text) >  0  THEN '" + PayloadType.TEXT_TAG + "' "
					+ "END AS payload_type ";
		}

		queryString += " from RFILE where aid = ? " + " and file_s_umidl = ? " + " and file_s_umidh = ? " + " and mesg_crea_date_time = ? ";

		List<FileAct> fileActList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper() {
			@Override
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

				if (applicationFeatures.isFilePayloadSupported())
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
	public InstanceTransmissionPrintInfo getInstanceTransmissionPrintInfo(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int timeZoneOffset) {
		return printInstanceTransmissionGetProcedure.execute(aid, umidl, umidh, mesg_crea_date, timeZoneOffset);
	}

	@Override
	public Pair<String, String> getMTExpantion(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, String mesgType) {
		return mtExpantionGetProcedure.execute(aid, umidl, umidh, mesg_crea_date, mesgType);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public AppendixJREDetails getAppendixJREDetails(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, boolean isInputMesg) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select appe_session_nbr, appe_sequence_nbr, appe_session_holder," + " appe_local_output_time, appe_remote_input_time, appe_remote_input_reference, "
				+ " appe_ack_nack_text, appe_checksum_value, appe_checksum_result, appe_auth_value, appe_auth_result " + " from vAppe " + " where aid = ? and appe_s_umidl = ? and appe_s_umidh = ? ";

		if (isPartitioned()) {
			queryString += " and x_crea_date_time_mesg = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		queryString += " and  x_appe_last = 1 and appe_iapp_name = 'SWIFT' and appe_inst_num = 0 and appe_type = ? ";
		if (isInputMesg) {
			parameters.add("APPE_EMISSION");
		} else {
			parameters.add("APPE_RECEPTION");
		}
		List<AppendixJREDetails> appeList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AppendixJREDetails appeDetails = new AppendixJREDetails();
				appeDetails.setAppeSessionNbr(rs.getInt("appe_session_nbr"));
				appeDetails.setAppeSequenceNbr(rs.getInt("appe_sequence_nbr"));
				appeDetails.setAppeSessionHolder(rs.getString("appe_session_holder"));
				Date dt = null;
				if (rs.getTimestamp("appe_local_output_time") != null) {
					dt = new Date(rs.getTimestamp("appe_local_output_time").getTime());
				}
				appeDetails.setAppeLocalOutputTime(dt);
				dt = null;
				if (rs.getTimestamp("appe_remote_input_time") != null) {
					dt = new Date(rs.getTimestamp("appe_remote_input_time").getTime());
				}

				appeDetails.setAppeRemoteInputTime(dt);
				appeDetails.setAppeRemoteInputReference(rs.getString("appe_remote_input_reference"));
				if (rs.getString("appe_ack_nack_text") != null) {

					appeDetails.setAppeAckNackText(new SerialClob(rs.getString("appe_ack_nack_text").toCharArray()));
				}
				appeDetails.setAppeChecksumValue(rs.getString("appe_checksum_value"));
				appeDetails.setAppeChecksumResult(rs.getString("appe_checksum_result"));

				if (rs.getString("appe_auth_value") != null) {

					appeDetails.setAppeAuthValue(new SerialClob(rs.getString("appe_auth_value").toCharArray()));
				}
				appeDetails.setAppeAuthResult(rs.getString("appe_auth_result"));
				return appeDetails;
			}
		});
		if (appeList.size() != 0) {
			return appeList.get(0);
		}
		return null;
	}

	@Override
	public String getExpandedMesssageText(String syntaxVersion, String messageType, String unexpandedText, java.util.Date messageDate, String thousandAmountFormat, String decimalAmountFormat) throws SQLException {
		ParsedMessage list = null;
		if (expandConn == null || expandConn.isClosed()) {
			expandConn = jdbcTemplate.getDataSource().getConnection();
		}
		try {
			Message messageParser = Syntax.getParser(syntaxVersion, messageType, expandConn);
			list = messageParser.parseMessageText(unexpandedText);
		} catch (UnrecognizedBlockException ex) {

			return "Message Expand Failed: Message Contains Unrecognized Block";
		} catch (Exception ex) {
			return "Message Expand Failed: " + ex.getMessage();
		}
		String val = unexpandedText;
		if (list != null) {
			val = list.getExpandedMessage(new Timestamp(messageDate.getTime()), thousandAmountFormat, decimalAmountFormat, getCurrenciesMap());
		}
		return val;
	}

	@Override
	public String getExpandedMesssageText(String syntaxVersion, String messageType, String unexpandedText, java.util.Date messageDate, String thousandAmountFormat, String decimalAmountFormat, String specificFeilds, boolean expandMessage)
			throws SQLException {
		ParsedMessage list = null;
		if (expandConn == null || expandConn.isClosed()) {
			expandConn = jdbcTemplate.getDataSource().getConnection();
		}
		try {
			Message messageParser = Syntax.getParser(syntaxVersion, messageType, expandConn);
			list = messageParser.parseMessageText(unexpandedText);
		} catch (UnrecognizedBlockException ex) {

			return "Message Expand Failed: Message Contains Unrecognized Block";
		} catch (Exception ex) {
			return "Message Expand Failed: " + ex.getMessage();
		}
		String val = unexpandedText;
		if (list != null) {
			val = list.getExpandedSpecificMessage(new Timestamp(messageDate.getTime()), "", "", getCurrenciesMap(), specificFeilds, expandMessage);
		}
		return val;
	}

	@Override

	public Message getMessageParser(String syntaxVersion, String messageType) throws SQLException {
		Message messageParser;
		if (expandConn == null || expandConn.isClosed()) {
			expandConn = jdbcTemplate.getDataSource().getConnection();
		}
		try {
			messageParser = Syntax.getParser(syntaxVersion, messageType, expandConn);
		} catch (Exception ex) {
			return null;
		}

		return messageParser;
	}

	@Override
	public ParsedMessage getExpandedMesssageTextJRE(String syntaxVersion, String messageType, String unexpandedText, java.util.Date messageDate) throws SQLException {
		ParsedMessage list = null;
		if (expandConn == null || expandConn.isClosed()) {
			expandConn = jdbcTemplate.getDataSource().getConnection();
			// System.out.println(" :::: Connection Aquired :::::");
		}
		try {
			Message messageParser = Syntax.getParser(syntaxVersion, messageType, expandConn);
			list = messageParser.parseMessageTextRJE(unexpandedText);
		} catch (UnrecognizedBlockException ex) {

			return null;
		} catch (Exception ex) {
			return null;
		}
		/*
		 * String val = unexpandedText; if (list != null) { //val = list.getExpandedMessage(new Timestamp(messageDate.getTime())); }
		 */
		return list;
	}

	@Override
	public boolean isFieldTagValid(String fieldTag) {
		if ("103".equals(fieldTag) || "108".equals(fieldTag) || "113".equals(fieldTag) || "115".equals(fieldTag) || "119".equals(fieldTag)) {
			return true;
		}

		String option = "0";
		fieldTag = fieldTag.trim();
		String fieldCode = fieldTag;
		boolean tagHasOption = false;
		if (fieldTag.length() == 3) {
			option = fieldTag.substring(2);
			fieldCode = fieldTag.substring(0, 2);
			tagHasOption = true;
		}

		List<Object> parameters = new ArrayList<Object>();
		parameters.add("F" + fieldTag);

		String queryString = "select count(*) from STXENTRYFIELD where rtrim(TAG) = ? and ENTRY_OPTION = '0'";
		if (tagHasOption) {
			parameters.add("F" + fieldCode);
			parameters.add(option);
			queryString = "select count(*) from STXENTRYFIELD where rtrim(TAG) = ?  Or ( rtrim(TAG) = ? and ENTRY_OPTION = ? )";
		}

		try {
			List<String> vals = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString(1);
				}
			});
			return vals.size() > 0 && vals.get(0) != null && !vals.get(0).trim().equalsIgnoreCase("0");
		} catch (Exception ex) {
		} // should not happen as the count should always give a result
		return false;
	}

	@Override
	public List<MessageSearchTemplate> getMsgSearchTemplates(long profileId) throws Exception {

		List<MessageSearchTemplate> MsgSearchTemplateList = new ArrayList<MessageSearchTemplate>();

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(profileId);

		String selectQuery = "select t.id, t.name, t.template_value,t.created_by as userid from rMsgSearchTemplates t " + " inner join rMsgSearchProfileTemplate g on t.id = g.template_id " + " where g.group_id = ? order by t.name ";

		MsgSearchTemplateList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<MessageSearchTemplate>() {
			@Override
			public MessageSearchTemplate mapRow(ResultSet rs, int arg1) throws SQLException {

				MessageSearchTemplate msgSearchTemplate = new MessageSearchTemplate();
				msgSearchTemplate.setId(rs.getInt("id"));
				msgSearchTemplate.setName(rs.getString("name"));
				msgSearchTemplate.setUserId(rs.getInt("userid"));
				msgSearchTemplate.setTemplateValue(rs.getString("template_value"));
				return msgSearchTemplate;
			}

		});

		return MsgSearchTemplateList;
	}

	@Override
	public void deleteMsgSearchTemplateById(long templateId) throws Exception {
		// The mapped between Template and group is deleted first
		jdbcTemplate.execute("DELETE FROM rMsgSearchProfileTemplate WHERE template_id =  " + templateId);
		jdbcTemplate.execute("DELETE FROM rMsgSearchTemplates       WHERE          id =  " + templateId);
	}

	@Override
	public List<MessageSearchTemplate> filterMsgSearchTemplates(String templateName, long profileId) throws Exception {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(templateName);

		List<MessageSearchTemplate> MsgSearchTemplateList = new ArrayList<MessageSearchTemplate>();
		String selectQuery = "select t.id, t.name, t.template_value, t.created_by from rMsgSearchTemplates t ";
		if (profileId > 0) {
			selectQuery += " inner join rMsgSearchProfileTemplate g on t.id = g.template_id ";
		}

		selectQuery += " where t.name like ? ";

		if (profileId > 0) {
			selectQuery += " and g.group_id = ? ";
			parameters.add(profileId);
		}

		selectQuery += " order by t.name ";

		MsgSearchTemplateList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<MessageSearchTemplate>() {
			@Override
			public MessageSearchTemplate mapRow(ResultSet rs, int arg1) throws SQLException {

				MessageSearchTemplate msgSearchTemplate = new MessageSearchTemplate();
				msgSearchTemplate.setId(rs.getInt("id"));
				msgSearchTemplate.setName(rs.getString("name"));
				return msgSearchTemplate;
			}

		});

		return MsgSearchTemplateList;
	}

	@Override
	public List<MessageSearchTemplate> getMsgSearchTemplatesById(long templateID) throws Exception {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(templateID);

		List<MessageSearchTemplate> MsgSearchTemplateList = new ArrayList<MessageSearchTemplate>();
		String selectQuery = "select id, name, template_value, created_by from rMsgSearchTemplates where id = ? ";

		MsgSearchTemplateList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<MessageSearchTemplate>() {
			@Override
			public MessageSearchTemplate mapRow(ResultSet rs, int arg1) throws SQLException {

				MessageSearchTemplate msgSearchTemplate = new MessageSearchTemplate();
				msgSearchTemplate.setId(rs.getInt("id"));
				msgSearchTemplate.setName(rs.getString("name"));
				msgSearchTemplate.setTemplateValue(rs.getString("template_value"));
				return msgSearchTemplate;
			}

		});

		return MsgSearchTemplateList;
	}

	@Override
	public List<SearchResultEntity> getMessagesByKey(String msgDirection, String msgType, String msgRef, String msgMur, String msgSuffix, String networkDelivery) throws Exception {

		List<Object> parameters = new ArrayList<Object>();

		String networkDeliveryPrefex = "DLV_";

		String queryString = "select r.aid, r.mesg_s_umidl, r.mesg_s_umidh from rmesg r, rappe a";

		queryString += " where r.aid = a.aid and r.mesg_s_umidl = a.appe_s_umidl and r.mesg_s_umidh = a.appe_s_umidh " + " and a.appe_inst_num = 0 and a.appe_iapp_name like 'SWIFT%'  and a.x_appe_last = 1 ";

		if (msgDirection != null && msgDirection.length() > 0) {
			queryString += " and r.MESG_SUB_FORMAT = ? ";
			parameters.add(msgDirection);
		}
		if (msgType != null && msgType.length() > 0) {
			queryString += " and r.MESG_TYPE = ? ";
			parameters.add(msgType);
		}
		if (msgRef != null && msgRef.length() > 0) {
			queryString += " and r.MESG_TRN_REF = ? ";
			parameters.add(msgRef);
		}
		if (msgMur != null && msgMur.length() > 0) {
			queryString += " and r.MESG_USER_REFERENCE_TEXT = ? ";
			parameters.add(msgMur);
		}
		if (msgSuffix != null && msgSuffix.length() > 0) {
			if (msgSuffix.length() == 6) {
				queryString += " and " + getDbPortabilityHandler().getDate6("r.mesg_crea_date_time") + " = ? ";
				parameters.add(msgSuffix.substring(0, 6));
			} else if (msgSuffix.length() > 6) {
				queryString += " and " + getDbPortabilityHandler().getDate6("r.mesg_crea_date_time") + " = ? ";
				parameters.add(msgSuffix.substring(0, 6));
				queryString += " and r.MESG_UUMID_SUFFIX = ? ";
				parameters.add(msgSuffix.substring(6));
			}
		}
		if (networkDelivery != null && networkDelivery.length() > 0) {
			queryString += " and upper(a.appe_network_delivery_status) = ? ";
			parameters.add(networkDeliveryPrefex + networkDelivery.toUpperCase());
		}

		List<SearchResultEntity> detailsEntity = (jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<SearchResultEntity>() {
			@Override
			public SearchResultEntity mapRow(ResultSet rs, int arg1) throws SQLException {

				SearchResultEntity searchResultEntity = new SearchResultEntity();
				searchResultEntity.setAid(rs.getInt("aid"));
				searchResultEntity.setMesgUmidh(rs.getInt("mesg_s_umidh"));
				searchResultEntity.setMesgUmidl(rs.getInt("mesg_s_umidl"));
				return searchResultEntity;
			}
		}));

		return detailsEntity;
	}

	public boolean isPartitioned() {
		return partitioned && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE;
	}

	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	@Override
	public void cancleSearch() throws SQLException {
		if (stmtSearch[0] != null && !stmtSearch[0].isClosed()) {
			try {
				stmtSearch[0].cancel();
			} catch (Exception e) {
				e.printStackTrace();
				throw (SQLException) e;
			}

		}
	}

	@Override
	public List<SearchResultEntity> execSearchQuery(String query, String decimalAmountFormat, String thousandAmountFormat, List<Object> queryBindingVar) throws SQLException {
		if (Thread.interrupted()) {
			cancleSearch();
			throw new SQLInterruptException();
		}
		final Object[] arguments = queryBindingVar.toArray();

		List<SearchResultEntity> detailsEntity = new ArrayList<SearchResultEntity>();
		try {
			detailsEntity = jdbcTemplate.query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					stmtSearch[0] = connection.prepareStatement(query);
					for (int i = 1; i <= arguments.length; i++) {
						stmtSearch[0].setObject(i, arguments[i - 1]);
					}

					return stmtSearch[0];
				}
			}, new RowMapper<SearchResultEntity>() {

				@Override
				public SearchResultEntity mapRow(ResultSet rs, int arg1) throws SQLException {
					if (Thread.interrupted()) {
						cancleSearch();
						throw new SQLInterruptException();
					}
					SearchResultEntity entity = new SearchResultEntity();
					entity.setAid((rs.getInt("aid")));
					entity.setMesgUmidl(rs.getInt("mesg_s_umidl"));
					entity.setMesgUmidh(rs.getInt("mesg_s_umidh"));
					entity.setMesgSubFormat(rs.getString("mesg_sub_format"));
					entity.setMesgType(rs.getString("mesg_type"));
					entity.setMesgUumid(rs.getString("mesg_uumid"));
					entity.setMesgSenderX1(rs.getString("mesg_sender_X1"));
					entity.setMesgTrnRef(rs.getString("mesg_trn_ref"));
					entity.setMesgUserReferenceText(rs.getString("mesg_user_reference_text"));
					entity.setMesgCreaDateTime(new Date(rs.getTimestamp("mesg_crea_date_time").getTime()));
					entity.setxFinValueDate(rs.getDate("x_fin_value_date"));
					Object object = rs.getObject("x_fin_amount");
					if (object != null) {
						LOGGER.debug("the amount of message before convert is " + object.toString().trim());
						String string = object.toString().trim();
						string = string.replaceAll("[^\\d.]", "");
						entity.setxFinAmount(Utils.formatAmount(new BigDecimal(string), rs.getString("x_fin_ccy"), getCurrenciesMap()));
					} else {
						entity.setxFinAmount(null);
					}

					entity.setxFinCcy(rs.getString("x_fin_ccy"));
					entity.setMesgFrmtName(rs.getString("mesg_frmt_name"));
					String status = rs.getString("mesg_status");
					if (status != null) {
						status = status.trim();
					}
					entity.setMesgStatus(status);
					entity.setMesgMesgUserGroup(rs.getString("mesg_mesg_user_group"));
					entity.setMesgIdentifier(rs.getString("mesg_identifier"));
					entity.setMesgUumidSuffix(rs.getInt("mesg_uumid_suffix"));
					entity.setInstRpName(rs.getString("X_INST0_RP_NAME"));
					entity.setInstReceiverX1(rs.getString("inst_receiver_X1"));
					entity.setEmiIAppName(rs.getString("emi_iapp_name"));
					entity.setEmiSessionNbr(rs.getInt("emi_session_nbr"));
					entity.setEmiSequenceNbr(rs.getString("emi_sequence_nbr"));
					entity.setEmiNetworkDeliveryStatus(rs.getString("emi_network_delivery_status"));
					entity.setRecIAppName(rs.getString("rec_iapp_name"));
					entity.setRecSessionNbr(rs.getInt("rec_session_nbr"));
					entity.setRecSequenceNbr(rs.getString("rec_sequence_nbr"));
					entity.setMesgSyntaxTableVer(rs.getString("mesg_syntax_table_ver"));

					/*
					 * the below fields was added in SEPA sprint in order to show MX messages Details 2016-10-06 by Mohammad Alzarai
					 */
					entity.setMesgRelatedReference(rs.getString("mesg_rel_trn_ref"));
					entity.setServiceName(rs.getString("mesg_service"));
					entity.setSlaId(rs.getString("MESG_SLA"));
					entity.setUetr(rs.getString("mesg_e2e_transaction_reference"));
					entity.setmXKeyword1(rs.getString("mesg_xml_query_ref1"));
					entity.setmXKeyword2(rs.getString("mesg_xml_query_ref2"));
					entity.setmXKeyword3(rs.getString("mesg_xml_query_ref3"));

					// GPI Colums
					entity.setOrderingCustomer(rs.getString("MESG_ORDER_CUS"));
					entity.setOrderingInstitution(rs.getString("MESG_ORDERING_INST"));
					entity.setBeneficiaryCustomer(rs.getString("MESG_BEN_CUST"));
					entity.setAccountWithInstitution(rs.getString("MESG_ACCOUNT_INST"));
					entity.setDetailsOfcharges(rs.getString("MESG_CHARGES"));

					String deductsCurr = "";

					if (rs.getString("MESG_SND_CHARGES_AMOUNT") != null && !rs.getString("MESG_SND_CHARGES_AMOUNT").isEmpty()) {
						String chargesLine = rs.getString("MESG_SND_CHARGES_AMOUNT");
						String[] charges;
						if (chargesLine.contains(",")) {
							charges = chargesLine.split(",");
							entity.setDeducts(charges[charges.length - 1]);
						}

						else {
							entity.setDeducts(rs.getString("MESG_SND_CHARGES_AMOUNT"));

						}

					} else {
						entity.setDeducts(rs.getString("MESG_RCV_CHARGES_AMOUNT"));
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
					if (entity.getDeducts() != null && !entity.getDeducts().isEmpty()) {

						entity.setDeducts(Utils.formatAmount(new BigDecimal(entity.getDeducts().trim()), deductsCurr, getCurrenciesMap()).toString());

					}

					entity.setDeductsCurr(deductsCurr);

					entity.setExchangeRate(rs.getString("MESG_EXCHANGE_RATE"));

					if (rs.getString("NOTIF_DATE_TIME") != null)
						try {
							entity.setNotifDateTime(sdf.format(dateFormatter.parse(rs.getString("NOTIF_DATE_TIME"))));
						} catch (ParseException e) {
							LOGGER.error("Error parsing the NOTIF_DATE_TIME to date ", e);
						}

					entity.setStatusCode(rs.getString("MESG_Status_code"));
					entity.setReasonCodes(rs.getString("MESG_Reason_code"));
					entity.setStatusOriginatorBIC(rs.getString("MESG_status_originator"));
					entity.setForwardedToAgent(rs.getString("MESG_forwarded_to"));
					String transactionStatusMapper = "";
					if (rs.getString("MESG_Status_code") != null && !rs.getString("MESG_Status_code").isEmpty()) {

						if (EnumUtils.isValidEnum(StatusDesc.class, entity.getStatusCode())) {
							entity.setStatusDesc(StatusDesc.valueOf(entity.getStatusCode()).getStatusDesc());

						}

						if (rs.getString("MESG_Status_code").equalsIgnoreCase("ACSC") || rs.getString("MESG_Status_code").equalsIgnoreCase("ACCC")) {
							transactionStatusMapper = "Completed";
						} else if (rs.getString("MESG_Status_code").equalsIgnoreCase("ACSP")) {
							transactionStatusMapper = "In Progess";
						} else if (rs.getString("MESG_Status_code").equalsIgnoreCase("RJCT")) {
							transactionStatusMapper = "Rejected";
						} else if (rs.getString("MESG_Status_code").equalsIgnoreCase("RETN")) {
							transactionStatusMapper = "Returned";
						}
					} else {
						transactionStatusMapper = null;
					}
					entity.setTransactionStatus(transactionStatusMapper);
					entity.setNAKCode(rs.getString("MESG_NAK_code"));
					entity.setGpiCur(rs.getString("MESG_INSTR_CCY"));
					entity.setViewerAmountZerosPadding(getDbPortabilityHandler().isViewerAmountZerosPadding());
					entity.setNote("");
					entity.setSuffix(rs.getString("MESG_UUMID_SUFFIX"));
					entity.setDecimalAmountFormat(decimalAmountFormat);
					entity.setThousandAmountFormat(thousandAmountFormat);
					entity.setSenderCorr(rs.getString("MESG_SND_CORR"));
					entity.setReceiverCorr(rs.getString("MESG_RCVR_CORR"));
					entity.setReimbursementInst(rs.getString("MESG_REIMBURS_INST"));
					entity.setSattlmentMethod(rs.getString("MESG_Settlement_Method"));
					entity.setClearingSystem(rs.getString("MESG_Clearing_System"));
					entity.setInstructedAmount(Utils.formatAmount(rs.getBigDecimal("MESG_INSTR_AMOUNT"), rs.getString("MESG_INSTR_CCY"), getCurrenciesMap()));

					return entity;
				}
			});

		} catch (Exception e) {
			LOGGER.error("Exception in execSearchQuery method ", e);
		}
		return detailsEntity;
	}

	@Override
	public void cancleCount() throws SQLException {
		if (stmtCount[0] != null && !stmtCount[0].isClosed()) {
			try {
				stmtCount[0].cancel();
			} catch (Exception e) {
				e.printStackTrace();
				throw (SQLException) e;
			}

		}

	}

	@Override
	public int execCountQuery(final String query, List<Object> queryBindingVar) {
		Object[] arguments = queryBindingVar.toArray();
		final int count = getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				stmtCount[0] = connection.prepareStatement(query);
				return stmtCount[0];
			}
		}, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for (int i = 1; i <= arguments.length; i++) {
					ps.setObject(i, arguments[i - 1]);
				}
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (Thread.interrupted()) {
					cancleCount();
					throw new SQLInterruptException();
				}
				resultSet.next();
				return resultSet.getInt(1);
			}
		});
		return count;
	}

	@Override
	public void logQuery(String loggedInUser, String query) {
		// query = query.replace("'", "''");
		do {
			String queryFragment = query;
			if (query.length() > 3998) {
				query = query.substring(3998);
				queryFragment = queryFragment.substring(0, 3998);

				/*
				 * if (queryFragment.charAt(0) == '\'' && queryFragment.charAt(1) != '\'') queryFragment = "'" + queryFragment; if (queryFragment.charAt(queryFragment.length() - 1) == '\'' &&
				 * queryFragment.charAt(queryFragment.length() - 2) != '\'') queryFragment = queryFragment.substring(0, queryFragment.length() - 1);
				 */

			} else {
				query = "";
				/*
				 * if (queryFragment.charAt(0) == '\'' && queryFragment.charAt(1) != '\'') queryFragment = "'" + queryFragment;
				 */
			}

			List<Object> parameters = new ArrayList<Object>();
			parameters.add(loggedInUser);
			parameters.add(queryFragment);

			jdbcTemplate.update("INSERT INTO QueryExecuted VALUES (" + getDbPortabilityHandler().getCurrentDateFunction() + ", ?, ? )", parameters.toArray());

		} while (!query.isEmpty());
	}

	@Override
	public PayloadFile getPayloadFile(String aid, String umidl, String umidh) throws Exception {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select payload, mesg_file_logical_name from rfile  where " + " aid = ? and file_s_umidl = ? and file_s_umidh = ? ";

		List<PayloadFile> downloadFilesList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<PayloadFile>() {
			@Override
			public PayloadFile mapRow(ResultSet rs, int rowNum) throws SQLException {

				PayloadFile pf = new PayloadFile();

				pf.setFileName(rs.getString("mesg_file_logical_name"));
				pf.setFileStream(rs.getBinaryStream("payload"));

				return pf;
			}
		});

		return downloadFilesList.get(0);
	}

	@Override
	public PayloadFile getPayloadFileText(String aid, String umidl, String umidh) throws Exception {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidl);
		parameters.add(umidh);

		String queryString = "select payload_text, mesg_file_logical_name from rfile  where " + " aid = ? and file_s_umidl = ? and file_s_umidh = ? ";

		List<PayloadFile> downloadFilesList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<PayloadFile>() {
			@Override
			public PayloadFile mapRow(ResultSet rs, int rowNum) throws SQLException {

				PayloadFile pf = new PayloadFile();

				pf.setFileName(rs.getString("mesg_file_logical_name"));
				pf.setFileStream(rs.getAsciiStream("payload_text"));

				return pf;
			}
		});

		return downloadFilesList.get(0);
	}

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	@Override
	public List<Entry> getMessageFields(String syntax, String msgType) throws SQLException, SyntaxNotFound {
		if (expandConn == null || !expandConn.isClosed()) {
			expandConn = jdbcTemplate.getDataSource().getConnection();
		}

		Message messageParser = Syntax.getParser(syntax, msgType, expandConn);
		List<Entry> parseList = messageParser.getParseList();
		return parseList;
	}

	@Override
	public ParsedMessage getMessageFieldsWithValues(String syntaxVersion, String mesgType, String messageText) throws SQLException, SyntaxNotFound, RequiredNotFound, UnrecognizedBlockException {
		if (expandConn == null || !expandConn.isClosed()) {
			expandConn = jdbcTemplate.getDataSource().getConnection();
		}

		Message messageParser = Syntax.getParser(syntaxVersion, mesgType, expandConn);
		ParsedMessage parsedMessage = messageParser.parseMessageText(messageText);

		return parsedMessage;
	}

	@Override
	public String getLatestInstalledSyntaxVer() {
		String queryString = "select max(version) as version from stxVersion where version <> '9805'";

		List<String> version = jdbcTemplate.query(queryString, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("version");
			}
		});

		return version.get(0);
	}

	@Override
	public List<String> getMessageTypes(String stxvesrion) {
		String queryString = "select type from STXMESSAGE where VERSION_IDX =(select IDX from STXVERSION where VERSION=?) order by type";

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(stxvesrion);
		List<String> mesgTypeList = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("type");
			}
		});

		return mesgTypeList;

	}

	@Override
	public List<AddressBook> getAddressBook(Long userId, Long profileId, String filteredUserName) {

		List<AddressBook> userAddressBooks = new ArrayList<AddressBook>();
		List<Object> parameters = new ArrayList<Object>();

		String queryString = " SELECT ID ,userName , Email FROM SUserAddressBook ";

		if (filteredUserName != null && !filteredUserName.isEmpty()) {
			queryString = queryString + " where userName like ?";
			parameters.add(filteredUserName);
		}

		queryString += " ORDER BY UserName ";

		userAddressBooks = jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<AddressBook>() {

			@Override
			public AddressBook mapRow(ResultSet rs, int rowNum) throws SQLException {
				AddressBook userAddressBook = new AddressBook();

				userAddressBook.setId(rs.getLong("ID"));
				userAddressBook.setUserName(rs.getString("userName"));
				userAddressBook.setUserEmail(rs.getString("EMail"));

				return userAddressBook;
			}
		});

		return userAddressBooks;
	}

	@Override
	public void addAddressBook(AddressBook addressBook) {

		StringBuilder insertAddressBookQuery = new StringBuilder();

		insertAddressBookQuery.append(" insert into sUserAddressBook ( ");
		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			insertAddressBookQuery.append("ID,");
		}
		insertAddressBookQuery.append(" userName ");
		insertAddressBookQuery.append(" ,EMail ");
		insertAddressBookQuery.append(" ,ReferencedUserId ");
		insertAddressBookQuery.append(" ,ReferencedUserProfileId ");
		insertAddressBookQuery.append(" ) ");
		insertAddressBookQuery.append("VALUES (");
		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			insertAddressBookQuery.append("SUSERADDRESSBOOK_ID.nextval, ");
		}
		insertAddressBookQuery.append(" ?, ?, ?, ? ");
		insertAddressBookQuery.append(")");

		List<Object> args = new ArrayList<Object>();

		args.add(addressBook.getUserName());
		args.add(addressBook.getUserEmail());
		args.add(addressBook.getUser().getUserId());
		args.add(addressBook.getUser().getProfile().getGroupId());

		jdbcTemplate.update(insertAddressBookQuery.toString(), args.toArray());

	}

	@Override
	public int deleteAddressBook(AddressBook addressBook) {
		String query = " delete  from SUserAddressBook where id = ? ";

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(addressBook.getId());

		int deleteStatus = jdbcTemplate.update(query, parameters.toArray());

		return deleteStatus;
	}

	@Override
	public List<Identifier> getIdentifiers() {
		String identifiersQuery = "SELECT IDENTIFIER_NAME,XSD_NAME,IDENTIFIER_VALUE FROM XMLTYPES WHERE IS_BULK <> 1 ORDER BY IDENTIFIER_VALUE ASC";
		List<Identifier> identifiers = jdbcTemplate.query(identifiersQuery, new RowMapper<Identifier>() {
			@Override
			public Identifier mapRow(ResultSet rs, int rowNum) throws SQLException {
				Identifier identifier = new Identifier();
				identifier.setIdentiferName(rs.getString("IDENTIFIER_NAME"));
				identifier.setXsdName(rs.getString("XSD_NAME"));
				identifier.setIdentifierValue(rs.getString("IDENTIFIER_VALUE"));
				return identifier;
			}
		});
		return identifiers;
	}

	@Override
	public Identifier getMXIdentifierKeyword(String identifier) {

		String query = " SELECT MX_Keyword_1 , MX_Keyword_2 , MX_keyword_3 " + " FROM XMLTYPES " + " where XML_TYPE = 'MX' " + " AND Identifier_Value = \'" + identifier + "\'";

		Identifier identifierDetail = new Identifier();

		List<Identifier> identifierDetails = jdbcTemplate.query(query, new RowMapper<Identifier>() {
			@Override
			public Identifier mapRow(ResultSet rs, int rowNum) throws SQLException {

				Identifier identifier = new Identifier();

				identifier.setMxKeyword1(rs.getString("MX_Keyword_1"));
				identifier.setMxKeyword2(rs.getString("MX_Keyword_2"));
				identifier.setMxKeyword3(rs.getString("MX_Keyword_3"));

				return identifier;
			}
		});

		identifierDetail = identifierDetails != null && identifierDetails.size() > 0 ? identifierDetails.get(0) : null;

		return identifierDetail;
	}

	@Override
	public String getLastNoteToSpecificMessage(int aid, int umidl, int umidh) {

		String query = "select * from RMESG_NOTES where MESG_S_UMIDH= ? and MESG_S_UMIDL= ? and aid= ? and CREATION_DATE =(select MAX (CREATION_DATE)    from RMESG_NOTES where MESG_S_UMIDH= ? and MESG_S_UMIDL= ? and aid= ? )";
		List<String> lastNote = jdbcTemplate.query(query, new Object[] { umidh, umidl, aid, umidh, umidl, aid }, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("Note");
			}
		});

		if (lastNote.isEmpty() || lastNote.size() == 0) {
			return "";

		}
		return lastNote.get(0);
	}

	public String getSelectedSAA(List<String> selectedSAA) {
		String mulSAAQuery = "";
		if (selectedSAA != null && !selectedSAA.isEmpty()) {
			mulSAAQuery = " and AID in ( ";
			for (int i = 0; i < selectedSAA.size(); ++i) {
				String val = selectedSAA.get(i);
				if (val != null) {
					mulSAAQuery += val;
					if (i < selectedSAA.size() - 1) {
						mulSAAQuery += ", ";
					}
				}
			}
			mulSAAQuery += " )";
		}
		return mulSAAQuery;

	}

	public String getMTConfirmationType(List<String> confirmationMesgType) {
		String mulSAAQuery = "";
		if (confirmationMesgType != null && !confirmationMesgType.isEmpty()) {
			mulSAAQuery = "  mesg_type in ( ";
			for (int i = 0; i < confirmationMesgType.size(); ++i) {
				String val = confirmationMesgType.get(i);
				if (val != null) {
					mulSAAQuery += val;
					if (i < confirmationMesgType.size() - 1) {
						mulSAAQuery += ", ";
					}
				}
			}
			mulSAAQuery += " )";
		}
		return mulSAAQuery;

	}

	public String addSenderIfExist(String senderBic, boolean gSRPRequest) {
		if (senderBic.isEmpty() || gSRPRequest)
			return "";
		return "and mesg_sender_X1 ='" + senderBic + "'";
	}

	@Override
	public List<MessageKey> getMessagesUCKeys(int aid, boolean isPartitionedDatabase) {

		String query = "select m.aid, mesg_s_umidh, mesg_s_umidl, mesg_sender_X1,mesg_crea_date_time, mesg_type, mesg_identifier, mesg_mod_date_time,MESG_E2E_TRANSACTION_REFERENCE , MESG_SUB_FORMAT , MESG_SLA, m.MESG_NAK_CODE " + " from rMesg m  "
				+ " where m.aid = ? and mesg_frmt_name = 'Swift' and ( ((mesg_type='199') and MESG_E2E_TRANSACTION_REFERENCE is not null and ( MESG_SLA is null ) and MESG_SUB_FORMAT ='INPUT' )"
				+ " or (mesg_type='103' and MESG_SUB_FORMAT ='OUTPUT' and MESG_SLA is null ) )" + " and (mesg_parsing_date_time is null or mesg_parsing_date_time < mesg_mod_date_time ) and archived = 0 " + " order by mesg_crea_date_time ";
		List<MessageKey> gpiMessageKeys = jdbcTemplate.query(query, new Object[] { aid }, new RowMapper<MessageKey>() {

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
				return messageKey;
			}

		});

		return gpiMessageKeys;
	}

	@Override
	public List<TextFieldData> getMessageFields(MessageKey key, boolean isPartitionedDatabase) {

		String query = " select * from rTextField where aid = ? and text_s_umidl = ? and text_s_umidh = ?";
		List<Object> parameters = new ArrayList<>();
		parameters.add(key.getAid());
		parameters.add(key.getUnidl());
		parameters.add(key.getUmidh());
		if (isPartitionedDatabase) {
			query = query + " and  x_crea_date_time_mesg = ?";
			parameters.add(key.getMesgCraetionUtil());
		}
		List<TextFieldData> gpiMessageFields = jdbcTemplate.query(query, parameters.toArray(), new RowMapper<TextFieldData>() {
			@Override
			public TextFieldData mapRow(ResultSet rs, int rowNum) throws SQLException {
				TextFieldData textFields = new TextFieldData();

				textFields.setFieldCode(rs.getInt("field_code"));
				textFields.setFieldCnt(rs.getInt("field_cnt"));
				textFields.setValue(rs.getString("value"));
				textFields.setFieldOption(rs.getString("field_option"));
				textFields.setSequence(rs.getString("sequence_id"));
				return textFields;
			}
		});

		return gpiMessageFields;
	}

	@Override
	public void updateGPIFields(MessageKey key, GPIMesgFields gpiMessageFields, MessageParsingResult parsingResult) {

		Object[] param = null;
		if (parsingResult == MessageParsingResult.WRONGFIELDS) {
			insertIntoErrorld("GPIParser", "Failed", "GPIParser", "Wrong Fields, Parsing Time will be Updated to Skip this Message.", "Failed");

			StringBuffer updateRmesgStatement = new StringBuffer();
			updateRmesgStatement.append("UPDATE RMESG SET ");
			updateRmesgStatement.append("MESG_PARSING_DATE_TIME = ? ");
			updateRmesgStatement.append("WHERE aid = ? and mesg_s_umidl = ? and mesg_s_umidh = ? and MESG_CREA_DATE_TIME = ?");

			jdbcTemplate.update(updateRmesgStatement.toString(), new Object[] { key.getMesgModFDate(), key.getAid(), key.getUnidl(), key.getUmidh(), key.getMesgCraetionUtil() });

			return;
		}

		try {
			StringBuffer updateRmesgStatement = new StringBuffer();
			updateRmesgStatement.append("UPDATE RMESG SET ");
			updateRmesgStatement.append("MESG_INSTR_AMOUNT = ? , ");
			updateRmesgStatement.append("MESG_INSTR_CCY = ? , ");
			updateRmesgStatement.append("MESG_ORDER_CUS = ? , ");
			updateRmesgStatement.append("MESG_SENDING_INST  = ? , ");
			updateRmesgStatement.append("MESG_ORDERING_INST = ? , ");
			updateRmesgStatement.append("MESG_BENEFICIARY_INST = ? , ");
			updateRmesgStatement.append("MESG_SND_CORR = ? , ");
			updateRmesgStatement.append("MESG_EXCHANGE_RATE = ? , ");
			updateRmesgStatement.append("MESG_CHARGES  = ? , ");
			updateRmesgStatement.append("MESG_RCVR_CORR = ? , ");
			updateRmesgStatement.append("MESG_REIMBURS_INST = ? , ");
			updateRmesgStatement.append("MESG_INTERM_INST = ? , ");
			updateRmesgStatement.append("MESG_ACCOUNT_INST = ? , ");
			updateRmesgStatement.append("MESG_BEN_CUST = ? , ");
			updateRmesgStatement.append("MESG_SND_CHARGES_AMOUNT = ? , ");
			updateRmesgStatement.append("MESG_SND_CHARGES_CURR = ? , ");
			updateRmesgStatement.append("MESG_RCV_CHARGES_AMOUNT = ? , ");
			updateRmesgStatement.append("MESG_RCV_CHARGES_CURR = ? , ");
			updateRmesgStatement.append("MESG_REMIT_INFO = ? , ");
			updateRmesgStatement.append("MESG_Status_code = ? , ");
			updateRmesgStatement.append("MESG_Reason_code = ? , ");
			updateRmesgStatement.append("MESG_forwarded_to = ? , ");
			updateRmesgStatement.append("MESG_status_originator = ? , ");
			updateRmesgStatement.append("NOTIF_DATE_TIME = ? , ");
			updateRmesgStatement.append("MESG_Settlement_Method = ? , ");
			updateRmesgStatement.append("MESG_Clearing_System = ? , ");

			if (key.getMesgType().contains("199")) {
				updateRmesgStatement.append("Confirmation_CCY = ? , ");
				updateRmesgStatement.append("Confirmation_Amount = ? , ");
				updateRmesgStatement.append("EXCH_RATE_FROM_CCY = ? , ");
				updateRmesgStatement.append("EXCH_RATE_TO_CCY = ? , ");
				updateRmesgStatement.append("ALL_DEDUCTS = ? , ");

			} else if (key.getMesgIdentifier().contains("COV") && (key.getMesgType().contains("202") || key.getMesgType().contains("205"))) {
				updateRmesgStatement.append("SB_Ordering_Customer = ? , ");
				updateRmesgStatement.append("SB_Ordering_Institution = ? , ");
				updateRmesgStatement.append("SB_Intermediary_Institution = ? , ");
				updateRmesgStatement.append("SB_Account_With_Institution = ? , ");
				updateRmesgStatement.append("SB_Beneficiary_Customer = ? , ");
				updateRmesgStatement.append("SB_Remittance_Information = ? , ");
				updateRmesgStatement.append("SB_INSTR_AMOUNT = ? , ");
				updateRmesgStatement.append("SB_INSTR_CURR = ? , ");
			}
			// if MT isn't 103 the below fields values are null
			updateRmesgStatement.append("SENDER_TO_RECEIVER = ? , ");
			updateRmesgStatement.append("SENDER_TO_RECEIVER_FIELD_CUAS = ? , ");
			updateRmesgStatement.append("SENDER_TO_RECEIVER_REASON_CODE = ? , ");
			updateRmesgStatement.append("SENDER_TO_RECEIVER_REFF  = ? , ");

			updateRmesgStatement.append("MESG_PARSING_DATE_TIME = ? ");
			updateRmesgStatement.append("WHERE aid = ? and mesg_s_umidl = ? and mesg_s_umidh = ? ");

			if (key.getMesgIdentifier().contains("COV") && (key.getMesgType().contains("202") || key.getMesgType().contains("205"))) {
				param = new Object[] { gpiMessageFields.getMesgInstrAmount(), gpiMessageFields.getMesgInstrCcy(), gpiMessageFields.getMesgOrderCus(), gpiMessageFields.getMesgSendingInst(), gpiMessageFields.getMesgOrderingInst(),
						gpiMessageFields.getMesgBeneficiaryInst(), gpiMessageFields.getMesgSnd_Corr(), gpiMessageFields.getMesgExchangeRate(), gpiMessageFields.getMesgCharges(), gpiMessageFields.getMesgRcvrCorr(),
						gpiMessageFields.getMesgReimbursInst(), gpiMessageFields.getMesgIntermInst(), gpiMessageFields.getMesgAccountInst(), gpiMessageFields.getMesgBenCust(), gpiMessageFields.getMesgSndChargesAmount(),
						gpiMessageFields.getMesgSndChargesCurr(), gpiMessageFields.getMesgRcvChargesAmount(), gpiMessageFields.getMesgRcvChargesCurr(), gpiMessageFields.getMesgRemitInfo(), gpiMessageFields.getStatusCode(),
						gpiMessageFields.getReasonCode(), gpiMessageFields.getForwardedTo(), gpiMessageFields.getStatusOriginator(), gpiMessageFields.getNotifyTime(), gpiMessageFields.getSettlementMethod(), gpiMessageFields.getClearingSystem(),
						gpiMessageFields.getSbOrderingCus(), gpiMessageFields.getSbOrderingInst(), gpiMessageFields.getSbIntermediaryInst(), gpiMessageFields.getSbAccountWithInst(), gpiMessageFields.getSbBeneficiaryCustomer(),
						gpiMessageFields.getSbRemittanceInfo(), gpiMessageFields.getSbInstructedAmount(), gpiMessageFields.getSbInstructedCurr(), gpiMessageFields.getSenderToReceiver(), gpiMessageFields.getSenderToReceiverFieldCuasing(),
						gpiMessageFields.getSenderToReceiverReasonCode(), gpiMessageFields.getSenderToReceiverReff(), key.getMesgModFDate(), key.getAid(), key.getUnidl(), key.getUmidh() };
			} else if (key.getMesgType().contains("199")) {
				param = new Object[] { gpiMessageFields.getMesgInstrAmount(), gpiMessageFields.getMesgInstrCcy(), gpiMessageFields.getMesgOrderCus(), gpiMessageFields.getMesgSendingInst(), gpiMessageFields.getMesgOrderingInst(),
						gpiMessageFields.getMesgBeneficiaryInst(), gpiMessageFields.getMesgSnd_Corr(), gpiMessageFields.getMesgExchangeRate(), gpiMessageFields.getMesgCharges(), gpiMessageFields.getMesgRcvrCorr(),
						gpiMessageFields.getMesgReimbursInst(), gpiMessageFields.getMesgIntermInst(), gpiMessageFields.getMesgAccountInst(), gpiMessageFields.getMesgBenCust(), gpiMessageFields.getMesgSndChargesAmount(),
						gpiMessageFields.getMesgSndChargesCurr(), gpiMessageFields.getMesgRcvChargesAmount(), gpiMessageFields.getMesgRcvChargesCurr(), gpiMessageFields.getMesgRemitInfo(), gpiMessageFields.getStatusCode(),
						gpiMessageFields.getReasonCode(), gpiMessageFields.getForwardedTo(), gpiMessageFields.getStatusOriginator(), gpiMessageFields.getNotifyTime(), gpiMessageFields.getSettlementMethod(), gpiMessageFields.getClearingSystem(),
						gpiMessageFields.getMesgCreditCcy(), gpiMessageFields.getMesgCreditAmount(), gpiMessageFields.getMesgExchangeCurrFrom(), gpiMessageFields.getMesgExchangeCurrTo(), gpiMessageFields.getAllDeducts(),
						gpiMessageFields.getSenderToReceiver(), gpiMessageFields.getSenderToReceiverFieldCuasing(), gpiMessageFields.getSenderToReceiverReasonCode(), gpiMessageFields.getSenderToReceiverReff(), key.getMesgModFDate(), key.getAid(),
						key.getUnidl(), key.getUmidh() };
			} else {
				param = new Object[] { gpiMessageFields.getMesgInstrAmount(), gpiMessageFields.getMesgInstrCcy(), gpiMessageFields.getMesgOrderCus(), gpiMessageFields.getMesgSendingInst(), gpiMessageFields.getMesgOrderingInst(),
						gpiMessageFields.getMesgBeneficiaryInst(), gpiMessageFields.getMesgSnd_Corr(), gpiMessageFields.getMesgExchangeRate(), gpiMessageFields.getMesgCharges(), gpiMessageFields.getMesgRcvrCorr(),
						gpiMessageFields.getMesgReimbursInst(), gpiMessageFields.getMesgIntermInst(), gpiMessageFields.getMesgAccountInst(), gpiMessageFields.getMesgBenCust(), gpiMessageFields.getMesgSndChargesAmount(),
						gpiMessageFields.getMesgSndChargesCurr(), gpiMessageFields.getMesgRcvChargesAmount(), gpiMessageFields.getMesgRcvChargesCurr(), gpiMessageFields.getMesgRemitInfo(), gpiMessageFields.getStatusCode(),
						gpiMessageFields.getReasonCode(), gpiMessageFields.getForwardedTo(), gpiMessageFields.getStatusOriginator(), gpiMessageFields.getNotifyTime(), gpiMessageFields.getSettlementMethod(), gpiMessageFields.getClearingSystem(),
						gpiMessageFields.getSenderToReceiver(), gpiMessageFields.getSenderToReceiverFieldCuasing(), gpiMessageFields.getSenderToReceiverReasonCode(), gpiMessageFields.getSenderToReceiverReff(), key.getMesgModFDate(), key.getAid(),
						key.getUnidl(), key.getUmidh() };
			}

			jdbcTemplate.update(updateRmesgStatement.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * If the exception is a Data Truncation exception, update the parsing_Date_Time column in order to prevent the tool of parsing it again.
			 */
			if (e.getMessage().toLowerCase().contains("truncated")) {
				insertIntoErrorld("GPIParser", "Failed", "GPIParser", "DATA Truncation ERROR, PARSING TIME WILL BE UPDATED TO SKIP THIS MESSAGE.", null);
				StringBuffer updateRmesgStatement = new StringBuffer();
				updateRmesgStatement.append("UPDATE RMESG SET ");
				updateRmesgStatement.append("MESG_PARSING_DATE_TIME = ? ");
				updateRmesgStatement.append("WHERE aid = ? and mesg_s_umidl = ? and mesg_s_umidh = ? and MESG_CREA_DATE_TIME = ? ");
				jdbcTemplate.update(updateRmesgStatement.toString(), new Object[] { key.getMesgModFDate(), key.getAid(), key.getUnidl(), key.getUmidh(), key.getMesgCraetionUtil() });
				return;
			}

			insertIntoErrorld("GPIParser", "Failed", "GPIParser", "Failed to Update RMESG Table.", "");
		}
	}

	@Override
	public void updateTransactionStatus(MessageKey key, GPIMesgFields gpiMessageFields, String dir) {

		try {
			StringBuffer updateRmesgStatement = new StringBuffer();
			updateRmesgStatement.append("UPDATE RMESG SET ");
			updateRmesgStatement.append("MESG_Status_code = ? , ");
			updateRmesgStatement.append("MESG_Reason_code = ? , ");
			updateRmesgStatement.append("MESG_forwarded_to = ? , ");
			updateRmesgStatement.append("MESG_status_originator = ? , ");
			updateRmesgStatement.append("MESG_Settlement_Method = ? , ");
			updateRmesgStatement.append("MESG_Clearing_System = ? ");
			updateRmesgStatement.append("WHERE MESG_TYPE like 103 and aid = ? and UPPER(mesg_sub_format) like  ? and " + "MESG_E2E_TRANSACTION_REFERENCE like ? ");
			jdbcTemplate.update(updateRmesgStatement.toString(), new Object[] { gpiMessageFields.getStatusCode(), gpiMessageFields.getReasonCode(), gpiMessageFields.getForwardedTo(), gpiMessageFields.getStatusOriginator(),
					gpiMessageFields.getSettlementMethod(), gpiMessageFields.getClearingSystem(), key.getAid(), dir.toUpperCase(), key.getUetr() });

		} catch (Exception e) {
			insertIntoErrorld("GPIParser", "Failed", "GPIParser", "Failed to Update RMESG Table.", "Failed");

			e.printStackTrace();
		}
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
	public String maskingMessagesFields(SearchResultEntity message, List<EntryNode> checkedFields) {
		try {
			LOGGER.debug("Updating: " + (checkedFields.size()) + " Messages");
			String insertStatemnt = "UPDATE RTEXTFIELD SET VALUE ='XXXX' WHERE AID = ? AND TEXT_S_UMIDL =? AND TEXT_S_UMIDH = ?  " + " AND FIELD_CODE = ?  AND (1 = ? OR FIELD_OPTION = ?)";

			int[] batchUpdate = jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
					EntryNode entryNode = checkedFields.get(index);
					preparedStatement.setLong(1, message.getAid());
					preparedStatement.setLong(2, message.getMesgUmidl());
					preparedStatement.setLong(3, message.getMesgUmidh());
					String fieldTag = entryNode.getTag();
					fieldTag = fieldTag.substring(1);
					preparedStatement.setInt(4, Integer.parseInt(fieldTag.substring(0, 2)));

					if (entryNode instanceof OptionNode || entryNode instanceof AlternativeNode || fieldTag.substring(2).isEmpty()) {
						preparedStatement.setInt(5, 1);
						preparedStatement.setString(6, "A");
					} else {
						preparedStatement.setInt(5, 0);
						preparedStatement.setString(6, fieldTag.substring(2));
					}
				}

				@Override
				public int getBatchSize() {
					return checkedFields.size();
				}
			});

			boolean isRowAffected = checkifUpdateAffected(batchUpdate);

			if (isRowAffected) {
				message.setMessageMasked(true);
				return "succeed";
			} else {
				message.setMessageMasked(true);
				return "{ No row affected For ::  " + message.getAid() + "," + message.getMesgUmidl() + "," + message.getMesgUmidh() + "}";
			}

		} catch (Exception e) {
			message.setMessageMasked(false);
			e.printStackTrace();
			return "{Exception occurred For :: " + message.getAid() + "," + message.getMesgUmidl() + "," + message.getMesgUmidh() + "}";
		}

	}

	private boolean checkifUpdateAffected(int[] updateResp) {

		for (int i = 0; i <= updateResp.length - 1; i++) {
			if (updateResp[i] == 1) {
				return true;
			}

		}

		return false;

	}

	private String getCurSysTimeZone() {

		String sysZone = "";

		try {
			sysZone = ZoneOffset.systemDefault().getRules().getOffset(Instant.now()).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (sysZone == null || sysZone.isEmpty()) {
				sysZone = new SimpleDateFormat("X").format(new java.util.Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (sysZone == null || sysZone.isEmpty()) {
				sysZone = OffsetDateTime.now().getOffset().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sysZone;
	}

	public String convertNotifyDataToCuruntZone(String notifyDateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss 'GMT'X");
		try {

			String sing = (notifyDateTime.contains("+")) ? "+" : notifyDateTime.contains("-") ? "-" : "";
			// check if there is no Zone ID
			if (!sing.isEmpty()) {
				String notifyZone = notifyDateTime.substring(notifyDateTime.indexOf(sing));// +0200
				String sysZone = getCurSysTimeZone();
				Integer notificationZoneId = Integer.parseInt((notifyZone.length() >= 3) ? notifyZone.substring(0, 3) : notifyZone); // to get Only hours +02
				Integer systemZoneId = Integer.parseInt((sysZone.length() >= 3) ? sysZone.substring(0, 3) : sysZone); // to get Only hours +02

				Integer appliedZoneOffset = systemZoneId - notificationZoneId;
				java.util.Date parse = dateFormatter.parse(notifyDateTime);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(parse);
				calendar.add(Calendar.HOUR_OF_DAY, appliedZoneOffset);
				String format = sdf.format(calendar.getTime());
				return format;
			} else {
				return sdf.format(dateFormatter.parse(notifyDateTime));
			}
		} catch (Exception e) {
			try {
				return sdf.format(dateFormatter.parse(notifyDateTime));
			} catch (ParseException e1) {
			}
			e.printStackTrace();
		}
		return "";
	}

	public ZonedDateTime convertDateBetweenTimeZones(LocalDateTime sourceDateTime, String sourceZone, String targetZone) {
		return sourceDateTime.atZone(ZoneId.of(sourceZone)).withZoneSameInstant(ZoneId.of(targetZone));
	}

	@Override
	public void fillSecurityDataBean(SecurityDataBean securityDataBean) {
		setSecurityDataBean(securityDataBean);
	}

	public SecurityDataBean getSecurityDataBean() {
		return securityDataBean;
	}

	public void setSecurityDataBean(SecurityDataBean securityDataBean) {
		this.securityDataBean = securityDataBean;
	}

	public Map<String, Integer> getCurrenciesMap() {
		return currenciesMap;
	}

	public void setCurrenciesMap(Map<String, Integer> currenciesMap) {
		this.currenciesMap = currenciesMap;
	}

	public boolean isEnableMesgSecCheck() {
		return enableMesgSecCheck;
	}

	public void setEnableMesgSecCheck(boolean enableMesgSecCheck) {
		this.enableMesgSecCheck = enableMesgSecCheck;
	}

}