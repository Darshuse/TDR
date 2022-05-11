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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eastnets.dao.DAO;
import com.eastnets.dao.security.data.SecurityDataBean;
import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.Pair;
import com.eastnets.domain.viewer.AddressBook;
import com.eastnets.domain.viewer.AppendixDetails;
import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.AppendixJREDetails;
import com.eastnets.domain.viewer.CorrespondentBean;
import com.eastnets.domain.viewer.DetailsHistory;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.FileAct;
import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.GpiConfirmation;
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
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.domain.viewer.PayloadFile;
import com.eastnets.domain.viewer.SearchLookups;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.exception.UnrecognizedBlockException;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.Message;

/**
 * Viewer DAO Interface
 * 
 * @author EastNets
 * @since July 12, 2012
 */
public interface ViewerDAO extends DAO {

	/**
	 * Fill message files lookup into SearchLookups object
	 * 
	 * @param lookups
	 */
	public void fillLookupMessageFiles(SearchLookups lookups);

	/**
	 * Fill formats lookup into SearchLookups object
	 * 
	 * @param lookups
	 */
	public void fillLookupFormats(SearchLookups lookups);

	/**
	 * Fill nature lookup into SearchLookups object
	 * 
	 * @return
	 */
	public List<String> getLookupNature();

	/**
	 * Fill queues lookup into SearchLookups object
	 * 
	 * @param lookups
	 */
	public void fillLookupQueues(SearchLookups lookups);

	/**
	 * Fill units lookup into SearchLookups object
	 * 
	 * @param lookups
	 */
	public void fillLookupUnits(SearchLookups lookups, String loggedInUser);

	/**
	 * Search for messages by umidl, umidh
	 * 
	 * @param umidl
	 * @param umidh
	 * @param userGroupID
	 *            the logged in user group id, used for security
	 * @return Search result
	 * @throws SQLException
	 */
	public List<SearchResultEntity> searchHL(int umidl, int umidh, Long userGroupID, Integer aid) throws SQLException;

	/**
	 * get Appendix Non Repudiation Type
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param instNum
	 * @return
	 */
	public String setAppeNonRepudiationType(int aid, int umidl, int umidh, Date mesg_crea_date, int instNum);

	// message details
	/**
	 * Get message details
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param timeZoneOffset
	 * @return MessageDetails
	 * @throws Exception
	 *             thrown if there is no message details , usually happens when the message has no instances
	 */
	public MessageDetails getMessageDetails(int aid, int umidl, int umidh, Date mesg_crea_date, final int timeZoneOffset, String loggedInUser) throws Exception;

	public List<MessageDetails> getMessageDetailsList(List<SearchResultEntity> messages, String loggedInUser) throws Exception;

	/**
	 * Get list of text field data
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @return List< TextFieldData >
	 */
	public List<TextFieldData> getTextFieldData(int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset);

	/**
	 * Get list of system text field data
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @return List< TextFieldData >
	 */
	public List<TextFieldData> getSystemTextFieldData(int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset);

	/**
	 * Get instance list
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param timeZoneOffset
	 * @return List< InstanceDetails >
	 */
	public List<InstanceDetails> getInstanceList(int aid, int umidl, int umidh, Date mesg_crea_date, final int timeZoneOffset);

	/**
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @return list of message's notes that previously added by the reporting web application
	 */
	public List<MessageNote> getMessageNotesList(int aid, int umidl, int umidh);

	/**
	 * Get intervention list
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param instance_no
	 * @param timeZoneOffset
	 * @return List< InterventionDetails >
	 */
	public List<InterventionDetails> getInterventionList(int aid, int umidl, int umidh, Date mesg_crea_date, int instance_no, final int timeZoneOffset);

	/**
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param instance_no
	 * @param timeZoneOffset
	 * @return
	 */
	public List<InterventionDetails> getInstanceInterventionList(int aid, int umidl, int umidh, Date mesg_crea_date, int instance_no, final int timeZoneOffset);

	/**
	 * Get appendix list
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param instance_no
	 * @param timeZoneOffset
	 * @return List< AppendixDetails >
	 */
	public List<AppendixDetails> getAppendixList(int aid, int umidl, int umidh, Date mesg_crea_date, int instance_no, final int timeZoneOffset);

	/**
	 * Force message update
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 */
	public void forceMessageUpdate(int aid, int umidl, int umidh, Date mesg_crea_date);

	/**
	 * Get list of interventions text
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param inst_num
	 * @return List< String >
	 */
	public List<String> getIntvText(int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, int timeZoneOffset);

	/**
	 * Get instance details
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param inst_num
	 * @param timeZoneOffset
	 * @return InstanceExtDetails
	 */
	public InstanceExtDetails getInstanceDetails(int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, final int timeZoneOffset);

	/**
	 * Get appendix details
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param inst_num
	 * @param intv_seq_num
	 * @param intv_date_time
	 * @param timeZoneOffset
	 * @return AppendixExtDetails
	 */
	public AppendixExtDetails getAppendixDetails(int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, Long intv_seq_num, Date intv_date_time, final int timeZoneOffset);

	/**
	 * Get intervention details
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param inst_num
	 * @param intv_seq_num
	 * @param intv_date_time
	 * @param timeZoneOffset
	 * @return InterventionExtDetails
	 */
	public InterventionExtDetails getInterventionDetails(int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, Long intv_seq_num, Date intv_date_time, final int timeZoneOffset);

	/**
	 * check if the message exist in the ldRequestUpdate table or not , if so then an update request has already been set for that message
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @return
	 */
	public boolean isBeingUpdated(int aid, int umidl, int umidh, Date mesg_crea_date);

	/**
	 * Get the file act info related to the message passed
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param creationDateFormatted
	 */
	public FileAct getMessageFile(int aid, int umidl, int umidh, Date creation_date_time);

	/**
	 * Get the Instance Transmission Info to be printed or mailed or dumped to some JRE file
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param timeZoneOffset
	 */
	public InstanceTransmissionPrintInfo getInstanceTransmissionPrintInfo(int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset);

	/**
	 * Get the message message syntax table version and message syntax description
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param mesgType
	 * @return pair of two strings, the left string is MESG_SYNTAX_TABLE_VER and the right string is STX_DESCRIPTION
	 */
	public Pair<String, String> getMTExpantion(int aid, int umidl, int umidh, Date mesg_crea_date, String mesgType);

	/**
	 * Get the Appendix details to be dumped to the JRE file
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 *            only needed in partition environments
	 * @param isInputMesg
	 *            : true if the MesgSubformat is equal to 'Input'
	 */
	public AppendixJREDetails getAppendixJREDetails(int aid, int umidl, int umidh, Date mesg_crea_date, boolean isInputMesg);

	/**
	 * get the message text expanded
	 * 
	 * @param syntaxVersion
	 * @param messageType
	 * @param unexpandedText
	 * @param messageDate
	 * @return message expanded text
	 * @throws SQLException
	 */
	public String getExpandedMesssageText(String syntaxVersion, String messageType, String unexpandedText, Date messageDate, String thousandAmountFormat, String decimalAmountFormat) throws SQLException;

	public String getExpandedMesssageText(String syntaxVersion, String messageType, String unexpandedText, java.util.Date messageDate, String thousandAmountFormat, String decimalAmountFormat, String specificFeilds, boolean expandMessage)
			throws SQLException;

	/**
	 * check if the field tag passed is available or not
	 * 
	 * @param fieldTag
	 * @return the field tag is correct or not
	 */
	public Message getMessageParser(String syntaxVersion, String messageType) throws SQLException;

	public boolean isFieldTagValid(String fieldTag);

	public List<MessageSearchTemplate> getMsgSearchTemplates(long profileId) throws Exception;

	public void addNewMsgSearchTemplate(MessageSearchTemplate template) throws Exception;

	/*
	 * insert new message note
	 */
	public MessageNote addNewMessageNote(MessageNote messageNote);

	public void deleteMessageNotes(List<Long> notesIds);

	public void deleteMsgSearchTemplateById(long templateId) throws Exception;

	public List<MessageSearchTemplate> filterMsgSearchTemplates(String templateName, long profileId) throws Exception;

	public void mapNewMsgSearchTemplateToProfile(MessageSearchTemplate savedTemplate) throws Exception;

	public List<MessageSearchTemplate> getMsgSearchTemplatesById(long templateID) throws Exception;

	public List<SearchResultEntity> getMessagesByKey(String msgDirection, String msgType, String msgRef, String msgMur, String msgSuffix, String networkDelivery) throws Exception;

	public List<SearchResultEntity> execSearchQuery(String query, String decimalAmountFormat, String thousandAmountFormat, List<Object> queryBindingVar) throws SQLException;

	public void logQuery(String loggedInUser, String query);

	public int execCountQuery(String query, List<Object> queryBindingVar);

	public PayloadFile getPayloadFile(String aid, String umidl, String umidh) throws Exception;

	public PayloadFile getPayloadFileText(String aid, String umidl, String umidh) throws Exception;

	public List<Entry> getMessageFields(String syntax, String msgType) throws SQLException, SyntaxNotFound;

	public ParsedMessage getMessageFieldsWithValues(String syntaxVersion, String mesgType, String messageText) throws SQLException, SyntaxNotFound, RequiredNotFound, UnrecognizedBlockException;

	public String getLatestInstalledSyntaxVer();

	public List<String> getMessageTypes(String stxvesrion);

	void cancleSearch() throws SQLException;

	void cancleCount() throws SQLException;

	public ParsedMessage getExpandedMesssageTextJRE(String stxVersion, String mesgType, String text, Date mesgCrea) throws SQLException;

	/**
	 * 
	 * @param userId
	 * @param profileId
	 * @param filteredUserName
	 * @return
	 */
	public List<AddressBook> getAddressBook(Long userId, Long profileId, String filteredUserName);

	/**
	 * 
	 * @param addressBook
	 */
	public void addAddressBook(AddressBook addressBook);

	/**
	 * 
	 * @param addressBook
	 * @return
	 */
	public int deleteAddressBook(AddressBook addressBook);

	/**
	 * 
	 * @return
	 */
	public List<Identifier> getIdentifiers();

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public Identifier getMXIdentifierKeyword(String identifier);

	/**
	 * 
	 * @param lookups
	 */
	public List<Identifier> getMXIdentifiers();

	/**
	 * 
	 * @param xmlQueryReference1
	 * @param xmlQueryReference2
	 * @param sourceMessageDate
	 * @param period
	 * @return
	 * @throws SQLException
	 */
	public List<MessageDetails> findRelatedMessages(String xmlQueryReference1, String xmlQueryReference2, Date sourceMessageDate, int period) throws SQLException;

	public String getLastNoteToSpecificMessage(int aid, int umidl, int umidh);

	public List<GpiConfirmation> getGpiAgent(String senderBic, String uter, final int timeZoneOffset, List<String> selectedSAA, boolean gSRPRequest, java.sql.Date mesgCreaDateTime, String... mesgType);

	public List<NotifierMessage> getPendingGpiMesg(String query, int mailAtempt, int confirmAtempt, int timeInterval) throws InterruptedException, SQLException;

	public List<MessageKey> getMessagesKeys(int aid, boolean isPartitionedDatabase, String fromDate, String toDate, int bulkSize);

	public List<MessageKey> getMessagesUCKeys(int aid, boolean isPartitionedDatabase);

	public List<TextFieldData> getMessageFields(MessageKey key, boolean isPartitionedDatabase);

	public void updateGPIFields(MessageKey key, GPIMesgFields gpiMessageFields, MessageParsingResult parsingResult);

	public void updateTransactionStatus(MessageKey key, GPIMesgFields fields, String dir);

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg1, String errMsg2);

	public List<DetailsHistory> getGpiDetailsHistory(String uter, Date mesg_crea_date) throws SQLException;

	public MessageKey getMessageKeyByUetr(String uetr, String acountNumber, String msgDirection, boolean isViewerTraker, String fromDate, String toDate) throws SQLException;

	Map<String, Integer> getCurrenciesISO();

	public String maskingMessagesFields(SearchResultEntity message, List<EntryNode> checkedFields);

	public CorrInfo getBicInfo(String bic);

	public void fillSecurityDataBean(SecurityDataBean securityDataBean);

	public String getTextMessages(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int timeZoneOffset);

	public List<CorrespondentBean> cacheCorrespondentsInformation();

}