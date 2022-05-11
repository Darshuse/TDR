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

package com.eastnets.service.viewer;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.AddressBook;
import com.eastnets.domain.viewer.AppendixDetails;
import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.Country;
import com.eastnets.domain.viewer.DaynamicMsgDetailsParam;
import com.eastnets.domain.viewer.DaynamicViewerParam;
import com.eastnets.domain.viewer.DetailsHistory;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.FieldSearchInfo;
import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.GpiConfirmation;
import com.eastnets.domain.viewer.Identifier;
import com.eastnets.domain.viewer.InstanceDetails;
import com.eastnets.domain.viewer.InstanceExtDetails;
import com.eastnets.domain.viewer.InterventionExtDetails;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageNote;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.MessageSearchTemplate;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.domain.viewer.PayloadFile;
import com.eastnets.domain.viewer.PayloadType;
import com.eastnets.domain.viewer.SearchLookups;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.SearchTempletStatus;
import com.eastnets.domain.viewer.TableColumnsHeader;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.domain.viewer.XmlExportResult;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.service.Service;
import com.eastnets.service.util.ReportType;
import com.eastnets.service.viewer.report.ViewerReportStreamWriter;

/**
 * Viewer Service Interface
 * 
 * @author EastNets
 * @since July 12, 2012
 */
public interface ViewerService extends Service {

	/**
	 * Get viewer module lookups
	 * 
	 * @param loggedInUser
	 * @return SearchLookups
	 */
	public SearchLookups getViewerSearchLookups(String loggedInUser);

	/**
	 * 
	 * @param loggedInUser
	 * @param messages
	 * @param withPDE
	 * @param timeZoneOffset
	 * @return
	 * @throws InterruptedException
	 */
	public String exportRJEForRelatedMessage(String loggedInUser, RelatedMessage messages, boolean withPDE, int timeZoneOffset, boolean showBolckFour) throws InterruptedException;

	public List<String> exportJsonRJE(String loggedInUser, List<MessageDetails> messages, boolean withPDE, int timeZoneOffset, boolean showBolckFour, boolean includeHistory) throws InterruptedException;

	/**
	 * Search for messages on db based on certain search criteria
	 * 
	 * @param loggedInUser
	 * @param params
	 * @param fieldSearch
	 * @return List<SearchResultEntity>
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public List<SearchResultEntity> search(DaynamicViewerParam searchParamMethod) throws InterruptedException, SQLException;

	/**
	 * Get messages count
	 * 
	 * @param loggedInUser
	 * @param params
	 * @param fieldSearch
	 * @return Integer
	 */
	public Integer getMessagesCount(DaynamicViewerParam searchParamMethod);

	/**
	 * Do search based on message umidl, umidh
	 * 
	 * @param loggedInUser
	 * @param umidl
	 * @param umidh
	 * @param isDebug
	 * @param timeZoneOffset
	 * @param loggedInUserGroupID
	 *            used for security
	 * @return List<SearchResultEntity>
	 * @throws SQLException
	 */
	public List<SearchResultEntity> searchHL(String loggedInUser, int umidl, int umidh, int timeZoneOffset, Long loggedInUserGroupID, Integer aid) throws SQLException;

	/**
	 * Format network status to make readable
	 * 
	 * @param loggedInUser
	 * @param networkStatus
	 * @return String
	 */
	public String formatNetworkStatus(String loggedInUser, String networkStatus);

	// **** message details ****
	/**
	 * Get message details
	 * 
	 * @param loggedInUser
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 * @return MessageDetails
	 * @throws Exception
	 *             thrown if there is no message details , usually happens when the message has no instances
	 */
	public MessageDetails getMessageDetails(DaynamicMsgDetailsParam daynamicMsgDetailsParam) throws Exception;

	/**
	 * Force update to a certain message
	 * 
	 * @param loggedInUser
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 */
	public void forceMessageUpdate(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date);

	/**
	 * Get message instance details
	 * 
	 * @param loggedInUser
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 * @param instNum
	 * @return InstanceExtDetails
	 */
	public InstanceExtDetails getInstanceDetails(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int instNum, int timeZoneOffset);

	/**
	 * Get appendix details
	 * 
	 * @param loggedInUser
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 * @param instNum
	 * @param intvSeqNum
	 * @param intvDateTimeOnDB
	 * @return AppendixExtDetails
	 */
	public AppendixExtDetails getAppendixDetails(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int instNum, Long intvSeqNum, Date intvDateTimeOnDB, int timeZoneOffset);

	/**
	 * Get intervention details
	 * 
	 * @param loggedInUser
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 * @param instNum
	 * @param intv_seq_num
	 * @param intvDateTimeOnDB
	 * @return InterventionExtDetails
	 */
	public InterventionExtDetails getInterventionDetails(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int instNum, Long intv_seq_num, Date intvDateTimeOnDB, int timeZoneOffset);

	/**
	 * formats the instance status text
	 * 
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @param mesg_crea_date
	 * @param inst_num
	 * @param inst_status
	 * @return
	 */
	public String formatMPFNStatus(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, String inst_status, int timeZoneOffset);

	/**
	 * returns an formated string value for the authentication result
	 * 
	 * @param loggedInUser
	 * @param authResult
	 * @return
	 */
	public String formatAuthResult(String loggedInUser, String authResult);

	/**
	 * return the formatted relation ship management application string
	 * 
	 * @param loggedInUser
	 * @param rmaCheckResult
	 * @return
	 */
	public String formatRmaCheckResult(String loggedInUser, String rmaCheckResult);

	/**
	 * return the formatted message acknowledgment status
	 * 
	 * @param loggedInUser
	 * @param status
	 * @param app_name
	 * @return
	 */
	public String formatAckSts(String loggedInUser, String status, String app_name);

	/**
	 * set the date time formats that will be used to generate and date/time string
	 * 
	 * @param dateTimeFormat
	 * @param dateFormat
	 * @param timeFormat
	 */
	public void setDateTimeFormats(String loggedInUser, String dateTimeFormat, String dateFormat, String timeFormat);

	/**
	 * send messages via email
	 * 
	 * @param loggedInUser
	 * @param subject
	 * @param to
	 * @param messages
	 * @param expandText
	 * @param includeHistory
	 * @return 0: success, 1:Mail server connection failed, 2:server timeout, 3:server authentication failed
	 * @throws InterruptedException
	 */
	public String mailMessages(String loggedInUser, String subject, String to, List<SearchResultEntity> messages, boolean expandText, boolean includeHistory, int timeZoneOffset, boolean showMessageText) throws InterruptedException;

	/**
	 * expand the message text
	 * 
	 * @param messageDetails
	 * @return the expanded message text
	 * @throws SQLException
	 */

	public String getMessageExpandedText(String loggedInUser, MessageDetails messageDetails, String thousandAmountFormat, String decimalAmountFormat) throws SQLException;

	public String getMessageExpandedText(String loggedInUser, MessageDetails messageDetails, String thousandAmountFormat, String decimalAmountFormat, String fields, boolean expandMessages) throws SQLException;

	/**
	 * get the formatted version of the passed messages to be printed
	 * 
	 * @param loggedInUser
	 * @param messages
	 * @param expandText
	 * @param includeHistory
	 * @return error message if error occurred or empty string if operation succeeded
	 * @throws InterruptedException
	 */
	public String printMessages(String loggedInUser, List<SearchResultEntity> messages, boolean expandText, boolean includeHistory, int timeZoneOffset, boolean showMessageText, boolean printExsitPointFormatAsPDF, boolean enableCreatedBy)
			throws InterruptedException;

	/**
	 * export jre file
	 * 
	 * @param loggedInUser
	 * @param messages
	 * @param withPDE
	 * @return
	 * @throws InterruptedException
	 */
	public String exportRJE(String loggedInUser, List<SearchResultEntity> messages, boolean withPDE, int timeZoneOffset, boolean showBolckFour) throws InterruptedException;

	/**
	 * generate and return the csv text to be saved as file
	 * 
	 * @param loggedInUser
	 * @param messages
	 * @return
	 * @throws InterruptedException
	 */
	public String exportCSV(String loggedInUser, List<SearchResultEntity> messages, List<TableColumnsHeader> selectedColumnsHeadre, String csvSeperator) throws InterruptedException;

	/**
	 * return the value saven in the settings database of the passes setting name
	 * 
	 * @param loggedInUser
	 * @param loggedInUserID
	 * @param settingName
	 * @return
	 */
	public String getSetting(String loggedInUser, Long loggedInUserID, String settingName);

	/**
	 * set a setting value in the settings of the viewer application in the database, if the setting does not exist it will be added
	 * 
	 * @param loggedInUser
	 * @param loggedInUserID
	 * @param settingName
	 * @param value
	 */
	public void setSetting(String loggedInUser, Long loggedInUserID, String settingName, String value);

	/**
	 * check if the field tag passed is available or not
	 * 
	 * @param loggedInUser
	 * @param fieldTag
	 * @return the field tag is correct or not
	 */
	public boolean isFieldTagValid(String loggedInUser, String fieldTag);

	public List<MessageSearchTemplate> getMsgSearchTemplates(long profileId) throws Exception;

	public void addNewMsgSearchTemplate(String templateName, String templateValue, User user) throws Exception;

	public void addNewMsgSearchTemplate(MessageSearchTemplate template) throws Exception;

	public List<MessageNote> reloadMessageNotes(int aid, int umidl, int umidh);

	/**
	 * 
	 * @param messageNote
	 */
	public MessageNote addNewMessageNote(MessageNote messageNote);

	public void deleteMessageNotes(List<Long> notesIds);

	public void deleteMsgSearchTemplate(long templateId) throws Exception;

	public List<MessageSearchTemplate> filterMsgSearchTemplates(String templateName, long profileId) throws Exception;

	public List<MessageSearchTemplate> getMsgSearchTemplatesById(long templateID) throws Exception;

	public List<SearchResultEntity> getMessagesByKey(String msgDirection, String msgType, String msgRef, String msgMur, String msgSuffix, String networkDelivery) throws Exception;

	public PayloadFile getPayloadFile(String aid, String umidl, String umidh, PayloadType payloadType) throws Exception;

	/**
	 * get a list of all message syntax fields for the passed msgType
	 * 
	 * @param loggedInUser
	 * @param syntaxVer
	 * @param msgType
	 * @return
	 * @throws SyntaxNotFound
	 * @throws SQLException
	 */
	public List<Entry> getMessageFields(String loggedInUser, String syntaxVer, String msgType) throws SQLException, SyntaxNotFound;

	public ViewerReportStreamWriter generateReport(String loggedInUser, List<SearchResultEntity> messages, String msgType, List<EntryNode> fields, ReportType type, ViewerSearchParam params, List<FieldSearchInfo> fieldSearch, boolean expandText,
			boolean landscape, Boolean showSummary, Boolean showBlockFour, String reportLogo, List<String> mesgList, String thousandAmountFormat, String decimalAmountFormat) throws Exception;

	public List<SearchResultEntity> getReportMessages(DaynamicViewerParam daynamicViewerParam, String msgType) throws SQLException;

	public String getLatestInstalledSyntaxVer(String userName);

	public List<String> getMessageTypes(String userName, String stxvesrion);

	void cancleSearch() throws SQLException;

	public void cancleCount() throws SQLException;

	public ParsedMessage getMessageExpandedTextJRE(String string, String mesgType, String blockFour, Date mesgCreaDateTime) throws SQLException;

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
	 */
	public int deleteAddressBook(AddressBook addressBook);

	/**
	 * 
	 * @return
	 */
	public boolean checkAddressBookFeatures();

	/**
	 * 
	 * @param identifer
	 * @return
	 */

	public List<Identifier> getIdentifiers();

	public Identifier getMXIdentifierKeyword(String identifer);

	/**
	 * 
	 * @param loggedInUser
	 * @param messages
	 * @return
	 * @throws InterruptedException
	 */
	public XmlExportResult exportXML(String loggedInUser, List<SearchResultEntity> messages) throws InterruptedException;

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

	public List<GpiConfirmation> getGpiAgent(String senderBic, String uter, int timeZoneOffset, List<String> selectedSAA, boolean gSRPRequest, java.sql.Date mesgCreaDateTime, String... mesgType);

	public List<Identifier> getMXIdentifiers();

	public List<String> getAllCurancy();

	public List<String> getAllMsgType();

	public List<Country> getAllCountry();

	public List<String> getUnitOrQueue(int flag, String loggedInUser);

	public String getLastNoteToSpecificMessage(int aid, int umidl, int umidh);

	public List<NotifierMessage> getPendingGpiMesg(String query, int mailAtempt, int confirmAtempt, int timeInterval) throws InterruptedException, SQLException;

	public String getMessageIORef(boolean isOutPutMesg, String type, int aid, int umidl, int umidh, java.sql.Date craeDateTime, Integer timeZone);

	public List<MessageKey> getMessageKeys(int aid, boolean isPartitionedDatabase, String fromDate, String toDate, int bulkSize);

	public List<MessageKey> getMessageUCKeys(int aid, boolean isPartitionedDatabase);

	public List<TextFieldData> getMessageFields(MessageKey key, boolean isPartitionedDatabase);

	public void updateGPIFields(MessageKey key, GPIMesgFields gpiMessageFields, MessageParsingResult parsingResult);

	public void updateTransactionStatus(MessageKey key, GPIMesgFields gpiMessageFields, String dir);

	// For Refactor
	public List<String> initColumnVisibilty(String loggedInUser, Long loggedInUserID, String settingName, List<TableColumnsHeader> avialableColumns);

	public Map<String, String> getAddressBookMap(List<AddressBook> cashedAddressBooks);

	public String userAfterFilter(String filterByUserName);

	public boolean addMailToList(List<AddressBook> selectedAddressBooks, List<String> mailToList);

	public List<Long> getDeletedNodeIDs(List<MessageNote> messageNotes, User loggedInUser);

	public List<SearchResultEntity> getPageListMessages(int maxFetchSize, List<SearchResultEntity> result);

	public void fillMXKeywordsLabel(MessageDetails messageDetails);

	public String preperMailTo(List<SearchResultEntity> messages, boolean enableAddressBook, List<String> mailToList, String mailToFromView, String subject, Map<String, String> addressBookMap);

	public SearchResultEntity getMessageDetailsForExport(MessageDetails msgDetails, List<List<SearchResultEntity>> pageListMessages, String loggedInUser, int umidl, int umidh, int timeZoneOffset, Long loggedInUserGroupID, Integer aid);

	public List<SearchResultEntity> getMessagesToExport(Integer messagesToPrint, List<List<SearchResultEntity>> pageListMessages, DaynamicViewerParam searchParamMethod, int currentPage);

	public SearchTempletStatus addNewMsgSearchTemplate(User user, String newSearchTemplate, String Template) throws Exception;

	public String parseTempalateParam(ViewerSearchParam searchParam, String field_name, String field_value);

	public List<SearchResultEntity> getMessagesReportMessages(DaynamicViewerParam daynamicViewerParam, String msgType, Integer reportMessagesToPrint, List<List<SearchResultEntity>> pageListMessages, int currentPage) throws SQLException;

	public void insertIntoErrorld(String errorExe, String errorLevel, String errModule, String errMsg1, String errMsg2);

	public String mailMessagesForGpi(int mailAttempts, String loggedInUser, String subject, String to, List<NotifierMessage> messages, boolean expandText, boolean includeHistory, int timeZoneOffset, String duration, List<String> messagesRjeText)
			throws InterruptedException;

	public List<DetailsHistory> getGpiDetailsHistory(Date mesg_crea_date, String uetr, String messageHistory) throws Exception;

	public List<AppendixDetails> getAppendixList(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int instance_no, final int timeZoneOffset);

	public MessageKey getMessageKeyByUetr(String uetr, String acountNumber, String msgDirection, boolean isViewerTraker, String fromDate, String toDate);

	public Map<String, Integer> getCurrenciesISO();

	public List<String> maskingMessagesFields(List<SearchResultEntity> messages, List<EntryNode> checkedFields);

	public CorrInfo getBicInfo(String bic);

	public Message getMessageParser(String syntaxVersion, String messageType);

	public String getMessageTex(SearchResultEntity message, String userName) throws Exception;

	public List<InstanceDetails> getInstanceList(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset);

	public String getHistory(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset, List<InstanceDetails> instanceList);
}
