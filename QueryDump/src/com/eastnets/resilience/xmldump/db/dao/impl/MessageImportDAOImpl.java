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
package com.eastnets.resilience.xmldump.db.dao.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.db.DatabaseType;
import com.eastnets.resilience.xmldump.db.dao.MessageImportDAO;
import com.eastnets.resilience.xmldump.logging.Logging;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xmldump.utils.GlobalSettings;
import com.eastnets.resilience.xmldump.utils.StringUtils;
import com.eastnets.resilience.xsd.messaging.Appendix;
import com.eastnets.resilience.xsd.messaging.Instance;
import com.eastnets.resilience.xsd.messaging.Intervention;
import com.eastnets.resilience.xsd.messaging.Message;
import com.eastnets.resilience.xsd.messaging.MessageInfo;
import com.eastnets.resilience.xsd.messaging.Text;

/**
 * Message importer
 * 
 * @author EHakawati
 * 
 */
public class MessageImportDAOImpl extends MessageImportDAO {

	// Logger
	private static final Logger logger = Logging.getLogger(MessageImportDAOImpl.class.getSimpleName());

	private boolean forceUpdate = false;

	/**
	 * Constructor
	 * 
	 * @param restoreSet
	 */
	public MessageImportDAOImpl(int restoreSet, boolean forceUpdate) {
		addObserver(new StatisticsObserver());
		setRestoreSet(restoreSet);
		this.forceUpdate = forceUpdate;
	}

	/**
	 * Entry point of message dump
	 * 
	 * @param Connection
	 * @param Message
	 * @return boolean
	 * @throws SQLException
	 */
	@Override
	public boolean addMessage(Connection conn, Message message) throws SQLException {

		boolean returnValue = true;

		// prepare statement
		CallableStatement cs = conn.prepareCall("{ call " + ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_RESTORE_MESG") + " }");

		int index = 1;
		int aid = GlobalConfiguration.getInstance().getAllianceId();
		// V_REQUEST_TYPE Param
		cs.setInt(index++, REQUEST_TYPE);

		// V_AID param
		cs.setInt(index++, aid);

		// V_MESG_S_UMIDL param
		cs.setInt(index++, message.getUmidL());

		// V_MESG_S_UMIDH param
		cs.setInt(index++, message.getUmidH());

		// V_MESG_VALIDATION_REQUESTED param
		this.setEnumString(cs, index++, message.getValidationRequested());

		// V_MESG_VALIDATION_PASSED param
		this.setEnumString(cs, index++, message.getValidationPassed());

		// V_MESG_CLASS param

		// double checking
		this.setEnumString(cs, index++, message.getClazz());

		// V_MESG_RELATED_S_UMID param
		cs.setString(index++, message.getRelatedSUmid());

		// V_MESG_IS_TEXT_READONLY param
		// boolean to integer :S
		cs.setInt(index++, message.isTextReadonly() ? 1 : 0);

		// V_MESG_IS_DELETE_INHIBITED param
		// boolean to integer :S
		cs.setInt(index++, message.isDeleteInhibited() ? 1 : 0);

		// V_MESG_IS_TEXT_MODIFIED param
		// boolean to integer :S
		cs.setInt(index++, message.isTextModified() ? 1 : 0);

		// V_MESG_IS_PARTIAL param
		// boolean to integer :S
		cs.setInt(index++, message.isPartial() ? 1 : 0);

		// V_MESG_STATUS param
		this.setEnumString(cs, index++, message.getStatus());

		// V_MESG_CREA_APPL_SERV_NAME param
		cs.setString(index++, message.getCreationApplication());

		// V_MESG_CREA_MPFN_NAME param
		cs.setString(index++, message.getCreationMpfnName());

		// V_MESG_CREA_RP_NAME param
		cs.setString(index++, message.getCreationRoutingPoint());

		// V_MESG_CREA_OPER_NICKNAME param
		cs.setString(index++, message.getCreationOperator());

		// V_MESG_CREA_DATE_TIME param
		this.setTimestamp(cs, index++, message.getCreationDate());

		// V_MESG_MOD_OPER_NICKNAME param
		cs.setString(index++, message.getModificationOperator());

		// V_MESG_MOD_DATE_TIME param
		this.setTimestamp(cs, index++, message.getModificationDate());

		// V_MESG_VERF_OPER_NICKNAME param
		cs.setString(index++, message.getVerificationOperator());

		// V_MESG_DATA_LAST param
		// Not available (mesg_data_last)
		cs.setNull(index++, java.sql.Types.INTEGER);

		// generate random token to force update of the Message
		int token = message.getObjectCRC();
		if (forceUpdate) {
			Random rand = new Random();
			int nxt = rand.nextInt(100000) * rand.nextInt(10000);
			while (token == nxt) {
				nxt = rand.nextInt(100000) * rand.nextInt(10000);
			}
			token = nxt;
		}

		// V_MESG_TOKEN param
		// cs.setInt(index++, message.getObjectCRC());
		cs.setInt(index++, token);

		// V_MESG_BATCH_REFERENCE param
		cs.setString(index++, message.getBatchReference());

		// V_MESG_CAS_SENDER_REFERENCE param
		cs.setString(index++, message.getCasSenderReference());

		// V_MESG_CAS_TARGET_RP_NAME param
		cs.setString(index++, message.getCasTargetRoutingPoint());

		// V_MESG_CCY_AMOUNT param
		// Not available (mesg_ccy_amount) (TELEX interface)
		cs.setNull(index++, java.sql.Types.VARCHAR);

		// V_MESG_COPY_SERVICE_ID param
		cs.setString(index++, message.getCopyServiceId());

		// V_MESG_DATA_KEYWORD1 param
		cs.setString(index++, message.getDataKeyword1());

		// V_MESG_DATA_KEYWORD2 param
		cs.setString(index++, message.getDataKeyword2());

		// V_MESG_DATA_KEYWORD3 param
		cs.setString(index++, message.getDataKeyword3());

		// V_MESG_DELV_OVERDUE_WARN_REQ param
		this.setBoolean(cs, index++, message.isDeliveryOverdueWarningRequired());

		// V_MESG_FIN_CCY_AMOUNT param
		cs.setString(index++, StringUtils.commafyAmount(message.getFinCurrencyAmount()));

		// V_MESG_FIN_VALUE_DATE param
		// Not a date CHAR(6)
		cs.setString(index++, message.getFinValueDate());

		// V_MESG_FRMT_NAME param
		cs.setString(index++, message.getFormatName());

		// V_MESG_IS_LIVE param
		this.setBoolean(cs, index++, message.isLive());

		// V_MESG_IS_RETRIEVED param
		this.setBoolean(cs, index++, message.isRetrieved());

		// V_MESG_MESG_USER_GROUP param
		cs.setString(index++, message.getMessageUserGroup());

		// V_MESG_NATURE param
		this.setEnumString(cs, index++, message.getNature());

		// V_MESG_NETWORK_APPL_IND param
		cs.setString(index++, message.getNetworkApplicationIndication());

		// V_MESG_NETWORK_DELV_NOTIF_REQ param
		this.setBoolean(cs, index++, message.isNetworkDeliveryNotificationRequired());

		// V_MESG_NETWORK_OBSO_PERIOD param
		this.setInt(cs, index++, message.getNetworkObsolescencePeriod());

		// V_MESG_NETWORK_PRIORITY param
		this.setEnumString(cs, index++, message.getNetworkPriority());

		// V_MESG_POSSIBLE_DUP_CREATION param
		this.setEnumString(cs, index++, message.getPossibleDuplicateCreation());

		// V_MESG_RECEIVER_ALIA_NAME param
		cs.setString(index++, message.getReceiverAliasName());

		// V_MESG_RECEIVER_SWIFT_ADDRESS param
		cs.setString(index++, message.getReceiverSwiftAddress());

		// V_MESG_RECOVERY_ACCEPT_INFO param
		this.setBoolean(cs, index++, message.isRecoveryAcceptInfo());

		// V_MESG_REL_TRN_REF param
		cs.setString(index++, message.getRelatedTransactionReference());

		// V_MESG_RELEASE_INFO param
		cs.setString(index++, message.getReleaseInfo());

		// V_MESG_SECURITY_IAPP_NAME param
		cs.setString(index++, message.getSecurityApplicationName());

		// V_MESG_SECURITY_REQUIRED param
		this.setBoolean(cs, index++, message.isSecurityRequired());

		// V_MESG_SENDER_X1 param
		// double check (mesg_sender_x1)
		cs.setString(index++, message.getSenderCorrespondentInstitutionName());

		// V_MESG_SENDER_X2 param
		// double check (mesg_sender_x2)
		cs.setString(index++, message.getSenderCorrespondentDepartment());

		// V_MESG_SENDER_X3 param
		// double check (mesg_sender_x3)
		cs.setString(index++, message.getSenderCorrespondentLastName());

		// V_MESG_SENDER_X4 param
		// double check (mesg_sender_x4)
		cs.setString(index++, message.getSenderCorrespondentFirstName());

		// V_MESG_SENDER_BRANCH_INFO param
		cs.setString(index++, message.getSenderBranchInfo());

		// V_MESG_SENDER_CITY_NAME param
		cs.setString(index++, message.getSenderCityName());

		// V_MESG_SENDER_CORR_TYPE param
		this.setEnumString(cs, index++, message.getSenderCorrespondentType());

		// V_MESG_SENDER_CTRY_CODE param
		cs.setString(index++, message.getSenderCountryCode());

		// V_MESG_SENDER_CTRY_NAME param
		cs.setString(index++, message.getSenderCountryName());

		// V_MESG_SENDER_INSTITUTION_NAME param
		cs.setString(index++, message.getSenderInstitutionName());

		// V_MESG_SENDER_LOCATION param
		cs.setString(index++, message.getSenderLocation());

		// V_MESG_SENDER_SWIFT_ADDRESS param
		cs.setString(index++, message.getSenderSwiftAddress());

		// V_MESG_SUB_FORMAT param
		this.setEnumString(cs, index++, message.getSubFormat());

		// V_MESG_SYNTAX_TABLE_VER param
		cs.setString(index++, message.getSyntaxTableVersion());

		// V_MESG_TEMPLATE_NAME param
		cs.setString(index++, message.getTemplateName());

		// V_MESG_TRN_REF param
		cs.setString(index++, message.getTransactionReference());

		// V_MESG_TYPE param
		cs.setString(index++, message.getType());

		// V_MESG_USER_ISSUED_AS_PDE param
		this.setBoolean(cs, index++, message.isUserIssuedAsPde());

		// V_MESG_USER_PRIORITY_CODE param
		cs.setString(index++, message.getUserPriorityCode());

		// V_MESG_USER_REFERENCE_TEXT param
		cs.setString(index++, message.getUserReferenceText());

		// V_MESG_UUMID param
		cs.setString(index++, message.getUumid());

		// V_MESG_UUMID_SUFFIX param
		this.setInt(cs, index++, message.getUumidSuffix());

		// V_RESTORE_SET_ID param
		cs.setInt(index++, getRestoreSet());

		// V_ARCHIVE_TYPE param
		// swift archive
		cs.setInt(index++, message.getIdentifier().getArchiveName() == null ? 0 : 1);

		// V_MESG_ZZ41_IS_POSSIBLE_DUP param
		// CHECK (mesg_zz41_is_possible_dup)
		this.setBoolean(cs, index++, message.isPossibleDuplicate());

		// V_LIVE_SOURCE param
		cs.setString(index++, GlobalConfiguration.getInstance().getLiveSource());

		// V_MESG_REQUESTOR_DN param
		cs.setString(index++, message.getRequestorDn());

		// V_MESG_SERVICE param
		cs.setString(index++, message.getService());

		// V_MESG_REQUEST_TYPE param
		cs.setString(index++, message.getRequestType());

		// V_MESG_IDENTIFIER param
		cs.setString(index++, message.getMessageIdentifier());

		// V_MESG_XML_QUERY_REF1 param
		cs.setString(index++, message.getXmlQueryReference1());

		// V_MESG_XML_QUERY_REF2 param
		cs.setString(index++, message.getXmlQueryReference2());

		// V_MESG_XML_QUERY_REF3 param
		cs.setString(index++, message.getXmlQueryReference3());

		// V_MESG_APPL_SENDER_REFERENCE param
		cs.setString(index++, message.getApplicationSenderReference());

		// V_MESG_PAYLOAD_TYPE param
		cs.setString(index++, message.getPayloadType());

		// V_MESG_SIGN_DIGEST_REFERENCE param
		cs.setString(index++, message.getSignatureDigestReference().size() == 0 ? "" : message.getSignatureDigestReference().get(0)); // double check

		// V_MESG_SIGN_DIGEST_VALUE param
		cs.setString(index++, message.getSignatureDigestValue().size() == 0 ? "" : message.getSignatureDigestValue().get(0)); // double check

		// V_MESG_USE_PKI_SIGNATURE param
		this.setBoolean(cs, index++, message.isUsePkiSignature());

		// v_mesg_sla param
		cs.setString(index++, message.getMesg_sla());

		// v_mesg_e2e_transaction_ref param
		cs.setString(index++, message.getMesg_e2e_transaction_ref());

		// v_mesg_is_copy_required param
		this.setBoolean(cs, index++, message.isCopyRequired());

		// v_mesg_auth_delv_notif_req param
		this.setBoolean(cs, index++, message.getMesg_auth_delv_notif_req());

		// v_mesg_copy_recipient_dn param
		this.setBoolean(cs, index++, message.getMesg_copy_recipient_dn());

		// v_mesg_copy_state
		this.setEnumString(cs, index++, message.getMesg_copy_state());

		// v_mesg_copy_type
		this.setEnumString(cs, index++, message.getMesg_copy_type());

		// v_mesg_fin_inform_release_info
		cs.setString(index++, message.getMesg_fin_inform_release_info());

		// v_mesg_is_copy
		this.setBoolean(cs, index++, message.getMesg_is_copy());

		// v_mesg_overdue_warning_delay
		this.setInt(cs, index++, message.getMesg_overdue_warning_delay());

		// v_mesg_overdue_warning_time
		this.setTimestamp(cs, index++, message.getMesg_overdue_warning_time());

		// v_mesg_source_template_name
		cs.setString(index++, message.getMesg_source_template_name());

		cs.setInt(index++, 0);
		// V_REQUEST_CORRESPONDENT param
		cs.registerOutParameter(index++, java.sql.Types.INTEGER);

		// RETSTATUS param
		cs.registerOutParameter(index, java.sql.Types.INTEGER);

		cs.executeUpdate();

		int updateCount = cs.getInt(index);

		cs.close();
		if (updateCount != 0) {
			setChanged();
			notifyObservers(StatisticsObserver.Type.MESG);
		} else {
			System.out.println(String.format("Message( %d, %d ) not updated.", message.getUmidL(), message.getUmidH()));
			returnValue = false;
		}

		return returnValue;
	}

	/**
	 * Add message text TODO: merge text parser with this function
	 * 
	 * @param Connection
	 * @param Message
	 * @throws SQLException
	 */
	@Override
	public void addText(Connection conn, Message message) throws SQLException {

		Text text = message.getText();

		if (text != null) {

			String query = null;
			int aid = GlobalConfiguration.getInstance().getAllianceId();

			int index = 1;
			int dataBlockLength = 0;
			String dataBlock = "";
			String xmlHeader = "";
			String xmlDocument = "";
			boolean isHeaderValid = false;
			boolean isDocumentValid = false;

			if (text.getDataBlock() != null) {
				// filter text data block
				dataBlock = filterTextDataBlock(text.getDataBlock());
				// get length of text data block
				dataBlockLength = dataBlock.length();
				// in case of MX Message
				if (message.getFormatName().equalsIgnoreCase("MX")) {
					// parse xml header
					xmlHeader = parseXMLHeader(dataBlock);

					// validate xml
					isHeaderValid = validateXMLType(xmlHeader);

					// parse xml document
					xmlDocument = parseXMLDocument(dataBlock, xmlHeader.length());

					// validate xml
					isDocumentValid = validateXMLType(xmlDocument);
				}
			}

			if (message.getFormatName().equalsIgnoreCase("MX") && (isHeaderValid == true || isDocumentValid == true)) {
				query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_RESTORE_XML_TEXT");
			} else {
				// Load the right query (partitioning have 1 extra input)
				if (GlobalConfiguration.getInstance().isPartitioned()) {
					query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_TEXT_PARTITIONED");
				} else {
					query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_TEXT");
				}

			}

			CallableStatement cs = conn.prepareCall("{ call " + query + " }");

			int outputIndex = 0;
			int textIndex = 0;

			// v_request_type param
			cs.setInt(index++, REQUEST_TYPE);

			// v_aid param
			cs.setInt(index++, aid);

			// v_text_s_umidl or v_xmltext_s_umidl param
			cs.setInt(index++, StringUtils.getUmidl(text.getIdentifier().getSUmid()));

			// v_text_s_umidh || v_xmltext_s_umidh param
			cs.setInt(index++, StringUtils.getUmidh(text.getIdentifier().getSUmid()));

			int token = text.getObjectCRC();
			if (forceUpdate) {
				Random rand = new Random();
				int nxt = rand.nextInt(100000) * rand.nextInt(10000);
				while (token == nxt) {
					nxt = rand.nextInt(100000) * rand.nextInt(10000);
				}
				token = nxt;
			}

			if (message.getFormatName().equalsIgnoreCase("MX") && (isHeaderValid == true || isDocumentValid == true)) {
				// v_mesg_crea_date_time param
				this.setTimestamp(cs, index++, message.getCreationDate());

				// v_mesg_identifier param
				cs.setString(index++, message.getMessageIdentifier());

				// v_text_token param
				// cs.setInt(index++, text.getObjectCRC());
				cs.setInt(index++, token);

				// v_text_document param
				cs.setString(index++, xmlDocument);

				// v_text_header param
				cs.setString(index++, xmlHeader);

				// RETSTATUS param
				outputIndex = index;
				cs.registerOutParameter(index, java.sql.Types.INTEGER);
			} else {
				// v_text_data_last param
				// Not available (text_data_last)
				cs.setNull(index++, java.sql.Types.INTEGER);

				// v_text_token param
				// cs.setInt(index++, text.getObjectCRC());
				cs.setInt(index++, token);

				// v_text_data_block param
				// TODO : check If applicable in mssql
				textIndex = index++;
				if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.MSSQL) {
					cs.setString(textIndex, dataBlock);
				} else {
					cs.registerOutParameter(textIndex, java.sql.Types.CLOB);
				}

				// v_text_swift_block_5 param
				cs.setString(index++, text.getSwiftBlock5());

				// v_text_swift_block_U param
				cs.setString(index++, text.getSwiftBlockU());

				// v_text_swift_prompted param
				// Not available (text_swift_prompted)
				cs.setNull(index++, java.sql.Types.VARCHAR);

				// v_text_data_block_len param
				cs.setInt(index++, dataBlockLength);

				// v_text_checksum param
				cs.setInt(index++, StringUtils.computeCRC(dataBlock));

				cs.setInt(index++, 0);

				// RETSTATUS param
				outputIndex = index;
				cs.registerOutParameter(index++, java.sql.Types.INTEGER);

				// v_text_checksum param
				if (GlobalConfiguration.getInstance().isPartitioned()) {
					this.setTimestamp(cs, index, message.getCreationDate());
				}
			}
			cs.executeUpdate();

			int updateCount = cs.getInt(outputIndex);

			if (updateCount == 0) {

				System.out.println(String.format("Text( %d, %d ) not updated.", message.getUmidL(), message.getUmidH()));
				return;
				// throw new SQLException("Text execution failed");
			}

			// in case of MT Message or not valid MX Message
			if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.ORACLE && text.getDataBlock() != null && isHeaderValid == false && isDocumentValid == false) {
				Clob clob = cs.getClob(textIndex);
				Writer writer = clob.setCharacterStream(0);
				try {
					writer.write(dataBlock);
					writer.flush();
					writer.close();
				} catch (Exception ex) {
					logger.warning("Text: " + ex.getMessage());
				}
			}

			cs.close();
			setChanged();
			notifyObservers(StatisticsObserver.Type.TEXT);
		}
	}

	/**
	 * Add message Instance
	 * 
	 * @param conn
	 * @param message
	 * @param instance
	 * @throws SQLException
	 */
	@Override
	public void addInstance(Connection conn, Message message, Instance instance) throws SQLException {

		String query;
		int aid = GlobalConfiguration.getInstance().getAllianceId();

		// Load the right query (partitioning have 1 extra input)
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_INSTANCE_PARTITIONED");
		} else {
			query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_INSTANCE");
		}

		// Prepare statement
		CallableStatement cs = conn.prepareCall("{ call " + query + " }");

		// reset statement
		cs.clearParameters();

		int index = 1;

		// v_request_type
		cs.setInt(index++, REQUEST_TYPE);
		// v_aid
		cs.setInt(index++, aid);
		// v_inst_s_umidl
		cs.setInt(index++, StringUtils.getUmidl(instance.getIdentifier().getSUmid()));
		// v_inst_s_umidh
		cs.setInt(index++, StringUtils.getUmidh(instance.getIdentifier().getSUmid()));
		// v_inst_num
		cs.setInt(index++, instance.getIdentifier().getNumber());
		// v_inst_type
		this.setEnumString(cs, index++, instance.getType());
		// v_inst_notification_type
		this.setEnumString(cs, index++, instance.getNotificationType());
		// v_inst_status
		this.setEnumString(cs, index++, instance.getStatus());
		// v_inst_related_nbr
		cs.setInt(index++, instance.getRelatedNumber());
		// v_inst_appe_date_time
		if (instance.getAppendixDateTime() == null) {
			cs.setDate(index++, new Date(0));
		} else {
			this.setTimestamp(cs, index++, instance.getAppendixDateTime());
		}
		// v_inst_appe_seq_nbr
		cs.setInt(index++, instance.getAppendixSequenceNumber());
		// v_inst_unit_name
		cs.setString(index++, instance.getUnitName());
		// double check
		// v_inst_rp_name
		cs.setString(index++, instance.getRoutingPoint());
		// v_inst_mpfn_name
		cs.setString(index++, instance.getMpfnName());
		// v_inst_mpfn_handle
		cs.setString(index++, instance.getMpfnHandle());
		// v_inst_process_state
		cs.setInt(index++, instance.getProcessState());
		// v_inst_last_mpfn_result
		cs.setString(index++, instance.getLastMpfnResult());// this.setEnumString(cs, index++, instance.getLastMpfnResult());
		// v_inst_relative_ref
		cs.setInt(index++, instance.getRelativeReference());
		// double check (inst_sm2000_priority)
		// v_inst_sm2000_priority
		cs.setInt(index++, instance.getPriority());
		// v_inst_deferred_time
		if (instance.getDeferredTime() == null) {
			cs.setDate(index++, new Date(0));
		} else {
			this.setTimestamp(cs, index++, instance.getDeferredTime());
		}
		// double check (inst_crea_appl_serv_name)
		// v_inst_crea_appl_serv_name
		cs.setString(index++, instance.getCreationApplication());
		// v_inst_crea_mpfn_name
		cs.setString(index++, instance.getCreationMpfnName());
		// v_inst_crea_rp_name
		cs.setString(index++, instance.getCreationRoutingPoint());
		// v_inst_crea_date_time
		this.setTimestamp(cs, index++, instance.getCreationDate());
		// v_initial_target_rp_name
		cs.setString(index++, instance.getInitialTargetRoutingPoint());
		// v_inst_auth_oper_nickname
		cs.setString(index++, instance.getAuthoriserOperator());
		// v_inst_last_oper_nickname
		cs.setString(index++, instance.getLastOperator());
		// Not available
		// v_inst_data_last
		cs.setNull(index++, java.sql.Types.INTEGER);

		// generate random token to force update of the instance
		int token = instance.getObjectCRC();
		if (forceUpdate) {
			Random rand = new Random();
			int nxt = rand.nextInt(100000) * rand.nextInt(10000);
			while (token == nxt) {
				nxt = rand.nextInt(100000) * rand.nextInt(10000);
			}
			token = nxt;
		}

		// v_inst_token
		// cs.setInt(index++, instance.getObjectCRC());
		cs.setInt(index++, token);
		// double check (inst_answerback)
		// v_inst_answerback
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_appli_rp_name
		cs.setString(index++, instance.getApplicationRoutingPoint());
		// v_inst_calc_description (TELEX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_calc_testkey_value (TELEX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_computation_details (TELEX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_corr_X1 not exists in SAA 7
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_corr_X2 not exists in SAA 7
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_crest_com_server_id
		cs.setString(index++, instance.getCrestComServerId());
		// v_inst_crest_gateway_id
		cs.setString(index++, instance.getCrestGatewayId());
		// v_inst_extracted_testkey_value (TELEX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// inst_fax_cui (FAX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// inst_fax_number (FAX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// inst_fax_origin (FAX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// inst_fax_tnap_name (FAX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// inst_letter_coding (FAX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_rcv_delivery_status
		this.setEnumString(cs, index++, instance.getReceiverDeliveryStatus());
		// v_inst_receiver_X1
		cs.setString(index++, instance.getReceiverCorrespondentInstitutionName());
		// double check
		// v_inst_receiver_X2
		cs.setString(index++, instance.getReceiverCorrespondentDepartment());
		// double check
		// inst_receiver_X3
		cs.setString(index++, instance.getReceiverCorrespondentFirstName());
		// double check
		// inst_receiver_X4
		cs.setString(index++, instance.getReceiverCorrespondentLastName());
		// v_inst_receiver_branch_info
		cs.setString(index++, instance.getReceiverBranchInfo());
		// v_inst_receiver_city_name
		cs.setString(index++, instance.getReceiverCityName());
		// v_inst_receiver_corr_type
		this.setEnumString(cs, index++, instance.getReceiverCorrespondentType());
		// v_inst_receiver_ctry_code
		cs.setString(index++, instance.getReceiverCountryCode());
		// v_inst_receiver_ctry_name
		cs.setString(index++, instance.getReceiverCountryName());
		// v_inst_receiver_institution_na
		cs.setString(index++, instance.getReceiverInstitutionName());
		// v_inst_receiver_location
		cs.setString(index++, instance.getReceiverLocation());
		// v_inst_receiver_network_iapp_n
		cs.setString(index++, instance.getReceiverNetworkIntegratedApplicationName());
		// v_inst_receiver_secu_iapp_name
		cs.setString(index++, instance.getReceiverSecurityIntegratedApplicationName());
		// inst_retry_count
		cs.setNull(index++, java.sql.Types.INTEGER);
		// inst_retry_cycle
		cs.setNull(index++, java.sql.Types.INTEGER);
		// v_inst_routing_code
		cs.setString(index++, instance.getRoutingCode());
		// v_inst_telex_number (TELEX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_telex_origin (TELEX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_test_comment (TELEX)
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_test_date_time (TELEX)
		cs.setNull(index++, java.sql.Types.DATE);
		// v_inst_test_system
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_testkey_required
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_testkey_status
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_tnap_name
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_oper_comment
		cs.setString(index++, instance.getOperatorComment());
		// v_inst_va41_audi_seq_nbr
		cs.setNull(index++, java.sql.Types.INTEGER);
		// v_inst_va41_awb_check_text_res
		cs.setNull(index++, java.sql.Types.VARCHAR);
		// v_inst_va41_calcul_date_time
		cs.setNull(index++, java.sql.Types.DATE);
		// v_inst_va41_contains_unscissor
		cs.setNull(index++, java.sql.Types.INTEGER);
		// v_inst_va41_testkey_calculated
		cs.setNull(index++, java.sql.Types.INTEGER);
		// v_live_source
		cs.setString(index++, GlobalConfiguration.getInstance().getLiveSource());
		// v_inst_disp_address_code
		cs.setString(index++, instance.getDispositionAddressCode());
		// v_inst_responder_dn
		cs.setString(index++, instance.getResponderDn());
		// v_inst_nr_indicator
		this.setBoolean(cs, index++, instance.isNonRepudiationIndicator());
		// v_inst_cbt_reference
		cs.setString(index++, instance.getCbtReference());
		// v_inst_delivery_mode
		this.setEnumString(cs, index++, instance.getDeliveryMode());
		// v_inst_intv_date_time
		this.setTimestamp(cs, index++, instance.getInterventionDateTime());
		// v_inst_intv_seq_nbr
		this.setInt(cs, index++, instance.getInterventionSequenceNumber());
		// v_request_correspondent
		cs.registerOutParameter(index++, java.sql.Types.INTEGER);
		int outputIndex = index;
		// RETSTATUS
		cs.registerOutParameter(outputIndex, java.sql.Types.INTEGER);
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			// v_x_crea_date_time_mesg
			this.setTimestamp(cs, ++index, message.getCreationDate());
		}

		cs.executeUpdate();

		int updateCount = cs.getInt(outputIndex);

		if (updateCount == 0) {
			System.out.println(String.format("Instance( %d, %d, %d ) not updated.", message.getUmidL(), message.getUmidH(), instance.getAppendixSequenceNumber()));
			return;
			// throw new SQLException("Instance execution faild");
		}

		// notify observer
		setChanged();
		notifyObservers(StatisticsObserver.Type.INST);

		cs.close();

	}

	/**
	 * Add instance interventions
	 * 
	 * @param Connection
	 * @param Message
	 * @param Instance
	 * @throws SQLException
	 */
	@Override
	public void addIntervention(Connection conn, Message message, Instance instance, Intervention intervention) throws SQLException {

		String query;
		int aid = GlobalConfiguration.getInstance().getAllianceId();

		// Load the right query (partitioning have 1 extra input)
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_INTERVENTION_PARTITIONED");
		} else {
			query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_INTERVENTION");
		}

		// Prepare statement
		CallableStatement cs = conn.prepareCall("{ call " + query + " }");

		int index = 1;

		cs.setInt(index++, REQUEST_TYPE);
		cs.setInt(index++, aid);
		cs.setInt(index++, intervention.getUmidL());
		cs.setInt(index++, intervention.getUmidH());
		cs.setInt(index++, intervention.getIdentifier().getInstanceNumber());
		this.setTimestamp(cs, index++, intervention.getIdentifier().getDateTime());
		cs.setInt(index++, intervention.getIdentifier().getInternalSequenceNumber());
		cs.setInt(index++, intervention.getNumber());
		cs.setString(index++, intervention.getName());
		this.setEnumString(cs, index++, intervention.getCategory());
		cs.setString(index++, intervention.getOperator());
		cs.setString(index++, intervention.getApplication());
		cs.setString(index++, intervention.getMpfnName());

		// Null is not accepted
		if (intervention.getAppendixDateTime() == null) {
			cs.setDate(index++, new Date(0));
		} else {
			this.setTimestamp(cs, index++, intervention.getAppendixDateTime());
		}

		cs.setInt(index++, intervention.getAppendixSequenceNumber());
		cs.setInt(index++, intervention.getLength());

		// generate random token to force update of the intervention
		int token = intervention.getObjectCRC();
		if (forceUpdate) {
			Random rand = new Random();
			int nxt = rand.nextInt(100000) * rand.nextInt(10000);
			while (token == nxt) {
				nxt = rand.nextInt(100000) * rand.nextInt(10000);
			}
			token = nxt;
		}

		// Faking intv_token
		// cs.setInt(index++, intervention.getObjectCRC());
		cs.setInt(index++, token);

		// intv_text Not needed
		cs.setNull(index++, java.sql.Types.VARCHAR);

		// TODO : check If applicable in MSSQL
		int intvTextIndex = index++;
		if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.MSSQL) {
			cs.setString(intvTextIndex, intervention.getText());
		} else {
			cs.registerOutParameter(intvTextIndex, java.sql.Types.CLOB);
		}

		int outputIndex = index;
		cs.registerOutParameter(outputIndex, java.sql.Types.INTEGER);
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			this.setTimestamp(cs, ++index, message.getCreationDate());
		}

		cs.executeUpdate();

		int updateCount = cs.getInt(outputIndex);

		if (updateCount == 0) {

			System.out.println(String.format("intervention( %d, %d, %d, %d ) not updated.", message.getUmidL(), message.getUmidH(), instance.getAppendixSequenceNumber(), intervention.getIdentifier().getInternalSequenceNumber()));
			return;
			// throw new SQLException("Intervention execution faild");
		}

		if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.ORACLE && intervention.getText() != null) {

			// special Oracle CLOB handling
			Clob clob = cs.getClob(intvTextIndex);
			Writer writer = clob.setCharacterStream(0);
			try {
				writer.write(intervention.getText());
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				logger.warning("Intervention:" + ex.getMessage());
			}

		}

		// Notify observer
		setChanged();
		notifyObservers(StatisticsObserver.Type.INTV);

		cs.close();

	}

	/**
	 * Add instance appendixes
	 * 
	 * @param conn
	 * @param message
	 * @param instance
	 * @throws SQLException
	 */
	@Override
	public void addAppendix(Connection conn, Message message, Instance instance, Appendix appendix) throws SQLException {

		String query;
		int aid = GlobalConfiguration.getInstance().getAllianceId();
		// Load the right query (partitioning have 1 extra input)
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_APPENDIX_PARTITIONED");
		} else {
			query = ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_APPENDIX");
		}

		// Prepare statement
		CallableStatement cs = conn.prepareCall("{ call " + query + " }");

		cs.clearParameters();
		int index = 0;

		// v_request_type
		cs.setInt(++index, REQUEST_TYPE);
		// v_aid
		cs.setInt(++index, aid);
		// v_appe_s_umidl
		cs.setInt(++index, appendix.getUmidL());
		// v_appe_s_umidh
		cs.setInt(++index, appendix.getUmidH());
		// v_appe_inst_num
		cs.setInt(++index, appendix.getIdentifier().getInstanceNumber());
		// v_appe_date_time
		this.setTimestamp(cs, ++index, appendix.getIdentifier().getDateTime());
		// v_appe_seq_nbr
		cs.setInt(++index, appendix.getIdentifier().getInternalSequenceNumber());
		// v_appe_iapp_name
		cs.setString(++index, appendix.getIntegratedApplication());
		// v_appe_type
		this.setEnumString(cs, ++index, appendix.getType());
		// v_appe_session_holder
		cs.setString(++index, appendix.getSessionHolder());
		// v_appe_session_nbr
		cs.setString(++index, appendix.getSessionNumber());
		// v_appe_sequence_nbr
		cs.setString(++index, appendix.getSequenceNumber());
		// v_appe_transmission_nbr
		cs.setInt(++index, appendix.getTransmissionNumber());
		// v_appe_crea_appl_serv_name
		cs.setString(++index, appendix.getCreatingApplication());
		// v_appe_crea_mpfn_name
		cs.setString(++index, appendix.getCreatingMpfn());
		// v_appe_crea_rp_name
		cs.setString(++index, appendix.getCreatingRoutingPoint());
		// Not available (appe_data_last)
		cs.setNull(++index, java.sql.Types.INTEGER);

		// generate random token to force update of the appendix
		int token = appendix.getObjectCRC();
		if (forceUpdate) {
			Random rand = new Random();
			int nxt = rand.nextInt(100000) * rand.nextInt(10000);
			while (token == nxt) {
				nxt = rand.nextInt(100000) * rand.nextInt(10000);
			}
			token = nxt;
		}

		// v_appe_token
		// cs.setInt(++index, appendix.getObjectCRC());
		cs.setInt(++index, token);
		// v_appe_ack_nack_text (will be filled in large appe table)
		cs.setString(++index, appendix.getAckNackText());
		// cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_answerback
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// v_appe_auth_result
		this.setEnumString(cs, ++index, appendix.getAuthenticationResult());
		// v_appe_auth_value
		cs.setString(++index, appendix.getAuthenticationValue());
		// appe_carrier_acceptance_id
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// v_appe_checksum_result
		this.setEnumString(cs, ++index, appendix.getChecksumResult());
		// v_appe_checksum_value
		cs.setString(++index, appendix.getChecksumValue());
		// v_appe_conn_response_code
		this.setEnumString(cs, ++index, appendix.getConnectionResponseCode());
		// v_appe_conn_response_text
		cs.setString(++index, appendix.getConnectionResponseText());
		// v_appe_crest_com_server_id
		cs.setString(++index, appendix.getCrestComServerId());
		// v_appe_crest_gateway_id
		cs.setString(++index, appendix.getCrestGatewayId());
		// v_appe_cui
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_fax_batch_sequence
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_fax_duration
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_fax_number
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_fax_tnap_name
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// v_appe_local_output_time
		this.setTimestamp(cs, ++index, appendix.getLocalOutputTime());
		// v_appe_nak_reason
		cs.setString(++index, appendix.getNackReason());
		// v_appe_network_delivery_status
		this.setEnumString(cs, ++index, appendix.getNetworkDeliveryStatus());
		// v_appe_pac_result
		this.setEnumString(cs, ++index, appendix.getProprietaryAuthenticationResult());
		// v_appe_pac_value
		cs.setString(++index, appendix.getProprietaryAuthenticationValue());
		// v_appe_rcv_delivery_status
		this.setEnumString(cs, ++index, appendix.getReceiverDeliveryStatus());
		// v_appe_remote_input_reference
		cs.setString(++index, appendix.getRemoteInputReference());
		// v_appe_remote_input_time
		this.setTimestamp(cs, ++index, appendix.getRemoteInputTime());
		// v_appe_sender_cancel_status
		this.setEnumString(cs, ++index, appendix.getSenderCancelationStatus());
		// appe_telex_batch_sequence
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_telex_duration
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_telex_number
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// appe_tnap_name
		cs.setNull(++index, java.sql.Types.VARCHAR);
		// v_appe_sender_swift_address
		cs.setString(++index, appendix.getSenderSwiftAddress());
		// v_appe_swift_ref
		cs.setString(++index, appendix.getSwiftReference());
		// v_appe_swift_request_ref
		cs.setString(++index, appendix.getSwiftRequestReference());
		// v_appe_nr_indicator
		this.setBoolean(cs, ++index, appendix.isNonRepudiation());
		// v_appe_nonrep_type
		this.setEnumString(cs, ++index, appendix.getNonRepudiationType());
		// v_appe_nonrep_warning
		cs.setString(++index, appendix.getNonRepudiationWarning());
		// v_appe_authoriser_dn
		cs.setString(++index, appendix.getAuthoriserDn());
		// v_appe_signer_dn
		cs.setString(++index, appendix.getSignerDn());
		// v_appe_snl_endpoint
		cs.setString(++index, appendix.getSnlEndpoint());
		// v_appe_snf_queue_name
		cs.setString(++index, appendix.getSnfQueueName());
		// v_appe_snf_input_time
		this.setTimestamp(cs, ++index, appendix.getSnfInputTime());
		// v_appe_snf_delv_notif_req
		this.setBoolean(cs, ++index, appendix.isSnfDeliveryNotificationRequested());
		// v_appe_swift_response_ref
		cs.setString(++index, appendix.getSwiftResponseReference());
		// v_appe_response_ref
		cs.setString(++index, appendix.getResponseReference());
		// v_appe_resp_nonrep_type
		this.setEnumString(cs, ++index, appendix.getResponseNonRepudiationType());
		// v_appe_resp_nonrep_warning
		cs.setString(++index, appendix.getResponseNonRepudiationWarning());
		// v_appe_resp_cbt_reference
		cs.setString(++index, appendix.getResponseCbtReference());
		// v_appe_resp_possible_dup_crea
		this.setEnumString(cs, ++index, appendix.getResponsePossibleDuplicateCreation());
		// v_appe_resp_responder_dn
		cs.setString(++index, appendix.getResponseResponderDn());
		// v_appe_resp_auth_result
		this.setEnumString(cs, ++index, appendix.getResponseAuthenticationResult());
		// v_appe_resp_signer_dn
		cs.setString(++index, appendix.getResponseSignerDn());
		// v_appe_ack_nack_lau_result
		this.setEnumString(cs, ++index, appendix.getAckNackLauResult());
		// v_appe_combined_auth_res
		this.setEnumString(cs, ++index, appendix.getCombinedAuthenticationResult());
		// v_appe_combined_pac_res
		this.setEnumString(cs, ++index, appendix.getCombinedProprietaryAuthenticationResult());
		// v_appe_lau_result
		this.setEnumString(cs, ++index, appendix.getLauResult());
		// v_appe_pki_auth_result
		this.setEnumString(cs, ++index, appendix.getPkiCombinedAuthenticationResult());
		// v_appe_pki_authentication_res
		this.setEnumString(cs, ++index, appendix.getPkiAuthenticationResult());
		// v_appe_pki_authorisation_res
		this.setEnumString(cs, ++index, appendix.getPkiAuthorisationResult());
		// TODO: CHECK THE VALUE v_appe_pki_pac2_result
		this.setEnumString(cs, ++index, appendix.getPkiProprietaryAuthenticationResult());
		// v_appe_rma_check_result
		this.setEnumString(cs, ++index, appendix.getRmaCheckResult());
		// v_appe_use_pki_signature
		this.setBoolean(cs, ++index, appendix.isUsePkiSignature());

		StringBuilder largeAppe = new StringBuilder();

		StringBuilder largeAppeRecDesc = new StringBuilder();

		extractLargeAppe(appendix, largeAppe, largeAppeRecDesc);

		// v_appe_record_desc
		cs.setString(++index, largeAppeRecDesc.toString());

		// v_appe_large_data
		int largeAppeIndex = ++index;
		if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.MSSQL) {
			cs.setString(largeAppeIndex, largeAppe.toString());
		} else {
			cs.registerOutParameter(largeAppeIndex, java.sql.Types.CLOB);
		}

		// RETSTATUS
		int outputIndex = ++index;
		cs.registerOutParameter(outputIndex, java.sql.Types.INTEGER);

		if (GlobalConfiguration.getInstance().isPartitioned()) {
			// v_x_crea_date_time_mesg
			this.setTimestamp(cs, ++index, message.getCreationDate());
		}

		cs.executeUpdate();

		int updateCount = cs.getInt(outputIndex);

		if (updateCount == 0) {
			System.out.println(String.format("Appendix( %d, %d, %d, %d ) not updated.", message.getUmidL(), message.getUmidH(), instance.getAppendixSequenceNumber(), appendix.getIdentifier().getInternalSequenceNumber()));
			return;
			// throw new SQLException("Appendix execution faild");
		}

		if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.ORACLE && largeAppe.length() > 0) {
			Clob clob = cs.getClob(largeAppeIndex);
			Writer writer = clob.setCharacterStream(0);
			try {
				writer.write(largeAppe.toString());
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				logger.warning("Appendix:" + ex.getMessage());
			}

		}

		// notify observer
		setChanged();
		notifyObservers(StatisticsObserver.Type.APPE);

		cs.close();

	}

	/**
	 * 
	 * @param appendix
	 * @param largeAppe
	 * @param largeAppeRecDesc
	 */
	private void extractLargeAppe(Appendix appendix, StringBuilder largeAppe, StringBuilder largeAppeRecDesc) {

		/*
		 * appe_pdm_history appe_mval_result appe_resp_auth_value appe_resp_mval_result appe_pki_auth_value appe_pki_pac2_value appe_ack_nack_text appe_auth_value appe_pac_value
		 */

		int currentPosition = 0;

		currentPosition = extractSingleLargeAppe("appe_ack_nack_text", appendix.getAckNackText(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_auth_value", appendix.getAuthenticationValue(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_pac_value", appendix.getProprietaryAuthenticationValue(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_pdm_history", appendix.getPdmHistory(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_mval_result", appendix.getMvalResult(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_resp_auth_value", appendix.getResponseAuthenticationValue(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_resp_mval_result", appendix.getResponseMvalResult(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_pki_auth_value", appendix.getPkiAuthenticationValue(), currentPosition, largeAppe, largeAppeRecDesc);
		currentPosition = extractSingleLargeAppe("appe_pki_pac2_value", appendix.getPkiProprietaryAuthenticationValue(), currentPosition, largeAppe, largeAppeRecDesc);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @param currentPosition
	 * @param largeAppe
	 * @param largeAppeRecDesc
	 * @return
	 */
	private int extractSingleLargeAppe(String key, String value, int currentPosition, StringBuilder largeAppe, StringBuilder largeAppeRecDesc) {
		if (value != null && !"".equals(value)) {
			value = StringUtils.forceStringCarriageReturn(value);
			largeAppe.append(value);
			// "%-30s%08ld%08ld", Fieldname(30 char)+Position(8 digits)+Length(8
			// digits)
			largeAppeRecDesc.append(String.format("%-30s%08d%08d", key, currentPosition + 1, value.length()));
			currentPosition += value.length();
		}
		return currentPosition;
	}

	@Override
	public MessageInfo getMessageInfoFromDB(Connection conn, int aid, int umidL, int umidH) throws SQLException {
		CallableStatement cs = conn.prepareCall("{ call " + ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_MESG_UPDATE_INFO") + " }");

		int index = 1;
		cs.setInt(index++, aid);
		cs.setInt(index++, umidL);
		cs.setInt(index++, umidH);

		// message data to be set by the procedure
		int messageModDateIndex = index++;
		int appeIntvCount = index++;

		cs.registerOutParameter(messageModDateIndex, java.sql.Types.TIMESTAMP);// message modification date
		cs.registerOutParameter(appeIntvCount, java.sql.Types.INTEGER);// appe. and intv. count
		cs.registerOutParameter(index, java.sql.Types.INTEGER);// return status

		cs.execute();

		int updateCount = cs.getInt(index);

		if (updateCount == 0) {
			cs.close();
			return null;// message does not exist
		}

		MessageInfo messageInfo = new MessageInfo();

		Timestamp ts = cs.getTimestamp(messageModDateIndex);
		if (ts != null) {
			messageInfo.setMesgCreationDateTime(new Date(ts.getTime()));
		}
		messageInfo.setIntvAppeCount(cs.getInt(appeIntvCount));

		cs.close();
		return messageInfo;
	}

	@Override
	public String filterTextDataBlock(String text) {

		// back to Fin format
		if (text.charAt(0) == '{') {
			text = text.replaceAll("\\{", "\n:");
			text = text.replaceAll("\\}", "");
		}

		// force the \r as sometimes Java skips it
		text = text.replace("\r\n", "\n");
		text = text.replace("\n", "\r\n");

		return text;
	}

	@Override
	public String parseXMLHeader(String text) {
		text = text.substring(0, text.indexOf("AppHdr>") + 7);
		return text;
	}

	@Override
	public String parseXMLDocument(String text, int firstIndex) {
		text = text.substring(firstIndex);
		return text;
	}

	@Override
	public boolean validateXMLType(String text) {

		try {

			DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(text)));
			logger.info("the text is valid XML type.");
			return true;

		} catch (SAXException e) {
			logger.severe("the text is not valid XML type, for reason [" + e.getMessage() + "], text = [" + text + "]");
		} catch (IOException e) {
			logger.severe("the text is not valid XML type, for reason [" + e.getMessage() + "], text = [" + text + "]");
		} catch (ParserConfigurationException e) {
			logger.severe("the text is not valid XML type, for reason [" + e.getMessage() + "], text = [" + text + "]");
		}
		return false;

	}

}
