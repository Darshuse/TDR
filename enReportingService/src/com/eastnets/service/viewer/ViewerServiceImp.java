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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.admin.AdminDAO;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.dao.xml.XMLReaderDAO;
import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.Alliance;
import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.AddressBook;
import com.eastnets.domain.viewer.AppendixDetails;
import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.CorrespondentBean;
import com.eastnets.domain.viewer.Country;
import com.eastnets.domain.viewer.DaynamicMsgDetailsParam;
import com.eastnets.domain.viewer.DaynamicViewerParam;
import com.eastnets.domain.viewer.DetailsHistory;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.ExportMessageType;
import com.eastnets.domain.viewer.FieldSearchInfo;
import com.eastnets.domain.viewer.GPIMesgFields;
import com.eastnets.domain.viewer.GpiConfirmation;
import com.eastnets.domain.viewer.Identifier;
import com.eastnets.domain.viewer.InstanceDetails;
import com.eastnets.domain.viewer.InstanceExtDetails;
import com.eastnets.domain.viewer.InstanceTransmissionPrintInfo;
import com.eastnets.domain.viewer.InterventionDetails;
import com.eastnets.domain.viewer.InterventionExtDetails;
import com.eastnets.domain.viewer.IntvAppe;
import com.eastnets.domain.viewer.MailMessagesStatus;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.domain.viewer.MessageNote;
import com.eastnets.domain.viewer.MessageParsingResult;
import com.eastnets.domain.viewer.MessageSearchTemplate;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.domain.viewer.PayloadFile;
import com.eastnets.domain.viewer.PayloadType;
import com.eastnets.domain.viewer.PrintMessagesStatus;
import com.eastnets.domain.viewer.SearchCriteria;
import com.eastnets.domain.viewer.SearchLookups;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.SearchTempletStatus;
import com.eastnets.domain.viewer.TableColumnsHeader;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.domain.viewer.XMLMessage;
import com.eastnets.domain.viewer.XmlExportResult;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.service.ServiceBaseImp;
import com.eastnets.service.common.CommonService;
import com.eastnets.service.common.MailService;
import com.eastnets.service.util.MessageViewerHeaderEnum;
import com.eastnets.service.util.ReportType;
import com.eastnets.service.viewer.helper.RJEExporter;
import com.eastnets.service.viewer.helper.ViewerReportGenerator;
import com.eastnets.service.viewer.helper.ViewerSearchQueryBuilder;
import com.eastnets.service.viewer.helper.ViewerSearchQueryBuilder.QueryType;
import com.eastnets.service.viewer.helper.ViewerServiceUtils;
import com.eastnets.service.viewer.report.ViewerReportStreamWriter;
import com.eastnets.service.viewer.report.ViewerSearchExcelReportGenerator;
import com.eastnets.service.viewer.report.ViewerSearchJasperReportGenerator;
import com.eastnets.utils.ApplicationUtils;
import com.eastnets.utils.Utils;

/**
 * Viewer Service Implementation
 * 
 * @author EastNets
 * @since July 12, 2012
 */
public class ViewerServiceImp extends ServiceBaseImp implements ViewerService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8799952036035216474L;
	private ViewerDAO viewerDAO;
	private CommonDAO commonDAO;
	private AdminDAO adminDAO;
	private XMLReaderDAO xmlReaderDAO;
	private CommonService commonService;
	private MailService mailService;
	private DBPortabilityHandler dbPortabilityHandler;
	private String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";
	private String dateFormat = "yyyy/MM/dd";
	private String timeFormat = "HH:mm:ss";
	private ViewerSearchQueryBuilder queryBuilder;
	private Map<String, Integer> currencies;
	private static final Logger LOGGER = Logger.getLogger(ViewerServiceImp.class);
	private boolean enableCorrChacing;

	private boolean debugSearch;
	private ApplicationFeatures applicationFeatures;
	private Map<String, CorrespondentBean> correspondentsInformationMap = new HashMap<>();

	public void init() {
		currencies = viewerDAO.getCurrenciesISO();
		if (enableCorrChacing) {
			cacheCorrespondentsInformation();
		}
	}

	private void cacheCorrespondentsInformation() {
		List<CorrespondentBean> correspondentsInformation = viewerDAO.cacheCorrespondentsInformation();
		if (correspondentsInformation != null && !correspondentsInformation.isEmpty()) {
			for (CorrespondentBean correspondent : correspondentsInformation) {
				correspondentsInformationMap.put(correspondent.getCorrBIC8(), correspondent);
			}
		}
	}

	@Override
	public SearchLookups getViewerSearchLookups(String loggedInUser) {
		SearchLookups lookups = new SearchLookups();

		viewerDAO.fillLookupMessageFiles(lookups);
		lookups.setSourceAvailableSAA(commonDAO.getAlliances());
		// lookups.setSourceAvailableCountry(commonDAO.getAllCountry());
		// lookups.setSourceAvailablecurancy(commonDAO.getAllCurancy());
		// lookups.setSourceAvailableMsgTy(sourceAvailableMsgTy);
		viewerDAO.fillLookupFormats(lookups);
		viewerDAO.fillLookupQueues(lookups);
		viewerDAO.fillLookupUnits(lookups, loggedInUser);
		lookups.setMessageNames(viewerDAO.getMXIdentifiers());
		lookups.setDetailOfChangeList(commonDAO.getChangeList());
		lookups.setQualifierList(commonDAO.getQualifierList());
		lookups.setStatusCodeList(commonDAO.getStatusCodeList());
		lookups.setReasonCodes(commonDAO.getReasonCodeList());
		lookups.setgSRPReasonCodes(commonDAO.getGSRPCodeList());
		lookups.setStatusList(commonDAO.getStatusList());
		lookups.setSattlmentMethodList(commonDAO.getSattlmentMethodList());
		lookups.setClearingSystemList(commonDAO.getStatusClearingSystemList());
		lookups.setServiceTypeList(commonDAO.getServiceType());
		List<String> natureListOrg = viewerDAO.getLookupNature();
		List<String> natureList = new ArrayList<String>();
		for (int i = 0; i < natureListOrg.size(); ++i) {
			String nature = natureListOrg.get(i);
			// mesg_nature is something like FINANICIAL_MSG, we need it like
			// Financial
			// first we will remove the _MSG
			if (!StringUtils.isEmpty(nature) && nature.length() > 4) {
				nature = StringUtils.left(nature, nature.length() - 4);
			}
			// then we need to fix the case of the letters and capitalize
			// the first letter only
			nature = formatCase(nature);
			natureList.add(nature);
		}
		lookups.setContentNature(natureList);

		return lookups;
	}

	public String formatCase(String inStr) {
		return StringUtils.capitalize(StringUtils.lowerCase(inStr));
	}

	@Override
	public void cancleSearch() throws SQLException {
		viewerDAO.cancleSearch();
	}

	@Override
	public List<SearchResultEntity> search(DaynamicViewerParam searchParamMethod) throws InterruptedException, SQLException {
		String query = getQueryBuilder().generateQuery(QueryType.Search, searchParamMethod.getParams(), searchParamMethod.getListFilter(), searchParamMethod.getListMax(), searchParamMethod.getTimeZoneOffset(), searchParamMethod.getFieldSearch(),
				searchParamMethod.getLoggedInUser(), searchParamMethod.isAddColums(), searchParamMethod.isShowInternalMessages(), searchParamMethod.getTextDecompostionType(), searchParamMethod.isCaseSensitive(),
				searchParamMethod.isIncludeSysMessages(), searchParamMethod.isEnableUnicodeSearch(), searchParamMethod.isEnableGpiSearch(), searchParamMethod.getGroupId(), searchParamMethod.getPageNumber(), searchParamMethod.isWebService());
		List<Alliance> Alliances = this.getViewerSearchLookups(searchParamMethod.getLoggedInUser()).getSourceAvailableSAA();
		if (debugSearch) {
			viewerDAO.logQuery(searchParamMethod.getLoggedInUser().replace("'", "''"), query);
		}
		// System.out.println(query);
		// System.out.println(searchParamMethod.getParams().getQueryVariablesBinding());

		List<SearchResultEntity> res = viewerDAO.execSearchQuery(query, searchParamMethod.getDecimalAmountFormat(), searchParamMethod.getThousandAmountFormat(), searchParamMethod.getParams().getQueryVariablesBinding());

		for (SearchResultEntity searchResult : res) {
			for (Alliance a : Alliances) {
				if (Integer.parseInt(a.getId()) == (searchResult.getAid())) {
					searchResult.setAlliance_instance(a.getName());
					break;
				}
			}

			String formatName = searchResult.getMesgSubFormat() != null && searchResult.getMesgSubFormat().equals("INPUT") ? "I" : searchResult.getMesgSubFormat().equals("OUTPUT") ? "O" : "";

			searchResult.setMesgSubFormat(formatName);

			searchResult.setTimeZoneOffset(searchParamMethod.getTimeZoneOffset());
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
		}
		return res;
	}

	@Override
	public Integer getMessagesCount(DaynamicViewerParam searchParamMethod) {

		String query = getQueryBuilder().generateQuery(QueryType.Count, searchParamMethod.getParams(), "", 0, searchParamMethod.getTimeZoneOffset(), searchParamMethod.getFieldSearch(), searchParamMethod.getLoggedInUser(),
				searchParamMethod.isAddColums(), searchParamMethod.isShowInternalMessages(), searchParamMethod.getTextDecompostionType(), searchParamMethod.isCaseSensitive(), searchParamMethod.isIncludeSysMessages(),
				searchParamMethod.isEnableUnicodeSearch(), searchParamMethod.isEnableGpiSearch(), searchParamMethod.getGroupId(), searchParamMethod.getPageNumber(), searchParamMethod.isWebService());

		if (debugSearch) {
			viewerDAO.logQuery(searchParamMethod.getLoggedInUser(), query);
		}
		// System.out.println(query);
		// System.out.println(searchParamMethod.getParams().getQueryVariablesBinding());
		int count = viewerDAO.execCountQuery(query, searchParamMethod.getParams().getQueryVariablesBinding());
		return count;
	}

	private DaynamicMsgDetailsParam buildMsgDeaitlsParam(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset, boolean includeHistory, boolean includeMessageNotes, String thousandAmountFormat,
			String decimalAmountFormat) {

		DaynamicMsgDetailsParam daynamicMsgDetailsParam = new DaynamicMsgDetailsParam();
		daynamicMsgDetailsParam.setLoggedInUser(loggedInUser);
		daynamicMsgDetailsParam.setAid(aid);
		daynamicMsgDetailsParam.setUmidh(umidh);
		daynamicMsgDetailsParam.setUmidl(umidl);
		daynamicMsgDetailsParam.setMesg_crea_date(mesg_crea_date);
		daynamicMsgDetailsParam.setTimeZoneOffset(timeZoneOffset);
		daynamicMsgDetailsParam.setIncludeHistory(true);
		daynamicMsgDetailsParam.setIncludeMessageNotes(includeMessageNotes);
		daynamicMsgDetailsParam.setDecimalAmountFormat(decimalAmountFormat);
		daynamicMsgDetailsParam.setThousandAmountFormat(thousandAmountFormat);
		return daynamicMsgDetailsParam;

	}

	@Override
	public MessageDetails getMessageDetails(DaynamicMsgDetailsParam daynamicMsgDetailsParam) throws Exception {
		MessageDetails messageDetails = viewerDAO.getMessageDetails(daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh(), daynamicMsgDetailsParam.getMesg_crea_date(),
				daynamicMsgDetailsParam.getTimeZoneOffset(), daynamicMsgDetailsParam.getLoggedInUser());
		messageDetails.setMesgInstances(getInstanceList(daynamicMsgDetailsParam.getLoggedInUser(), daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh(), daynamicMsgDetailsParam.getMesg_crea_date(),
				daynamicMsgDetailsParam.getTimeZoneOffset()));

		messageDetails.setDecimalAmountFormat(daynamicMsgDetailsParam.getDecimalAmountFormat());
		messageDetails.setThousandAmountFormat(daynamicMsgDetailsParam.getThousandAmountFormat());
		if (daynamicMsgDetailsParam.isIncludeMessageNotes()) {
			messageDetails.setMessageNotes(getMessageNotesList(daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh()));
		}
		if (daynamicMsgDetailsParam.isIncludeHistory()) // get only the history if the user want to display it
			messageDetails.setMesgHistory(getHistory(daynamicMsgDetailsParam.getLoggedInUser(), daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh(), daynamicMsgDetailsParam.getMesg_crea_date(),
					daynamicMsgDetailsParam.getTimeZoneOffset(), messageDetails.getMesgInstances()));

		if (messageDetails.getMesgFrmtName() != null && (messageDetails.getMesgFrmtName().equals("File") || messageDetails.getMesgFrmtName().equals("MX") || messageDetails.getMesgFrmtName().equals("AnyXML"))) {
			messageDetails.setXmlMessages(xmlReaderDAO.getXMLMessage(daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh(), daynamicMsgDetailsParam.getMesg_crea_date(), false));
			messageDetails.setMesgCanExpand(true);
		} else {

			Pair<String, Boolean> unexpandedText = getUnexpandedText(daynamicMsgDetailsParam.getLoggedInUser(), ApplicationUtils.convertClob2String(messageDetails.getTextDataBlock()), messageDetails.getMesgType(), messageDetails.getMesgFrmtName(),
					daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh(), daynamicMsgDetailsParam.getMesg_crea_date(), daynamicMsgDetailsParam.getTimeZoneOffset());

			// if it is an xml message we will just format the xml within it,
			// cause it can't be expanded
			if (unexpandedText != null) {
				String str = unexpandedText.getKey();
				if (str.startsWith("<") && str.endsWith(">")) {

					String strFormatted = ViewerServiceUtils.formatXML("<?xml version=\"1.0\"?><s>" + str + "</s>");// xml
					// messages
					// that
					// come
					// from
					// swift
					// are
					// not
					// complete,
					// as
					// they
					// has
					// more
					// that
					// one
					// root
					// node,
					// this
					// was
					// added
					// to
					// fix
					// this
					if (strFormatted == null) {
						strFormatted = str;
						strFormatted = strFormatted.replace("\\n", "");
						strFormatted = strFormatted.replace("\\\"", "\"");
						strFormatted = ViewerServiceUtils.formatXML("<?xml version=\"1.0\"?><s>" + strFormatted + "</s>");// xml
					}

					if (strFormatted != null) {
						strFormatted = strFormatted.substring(strFormatted.indexOf("<s>") + 4, strFormatted.length() - 5);
						strFormatted = strFormatted.replace("\n  ", "\n");
						strFormatted = strFormatted.substring(2);
						str = strFormatted;
					}
				} else {
					messageDetails.setMesgBlock5(getBlock5(messageDetails));
				}
				messageDetails.setMesgUnExpandedText(str);
				messageDetails.setMesgCanExpand(unexpandedText.getValue());
			}
		}

		messageDetails.setMesgStatus(getMesgStatus(messageDetails));

		messageDetails.setMesgSAAName("");// i will fill it from the
		// presentation layer for
		// optimization
		CorrInfo corrInf = new CorrInfo();
		corrInf.setCorrType(messageDetails.getMesgSenderCorrType());
		corrInf.setCorrX1(messageDetails.getMesgSenderX1());
		corrInf.setCorrX2(messageDetails.getMesgSenderX2());
		corrInf.setCorrX3(messageDetails.getMesgSenderX3());
		corrInf.setCorrX4(messageDetails.getMesgSenderX4());
		corrInf.setMesgDate(messageDetails.getMesgCreaDateTime());
		messageDetails.setMesgSenderAddress(commonService.getCorrInfoString(daynamicMsgDetailsParam.getLoggedInUser(), corrInf));

		corrInf = new CorrInfo();
		corrInf.setCorrType(messageDetails.getInstReceiverCorrType());
		corrInf.setCorrX1(messageDetails.getInstReceiverX1());
		corrInf.setCorrX2(messageDetails.getInstReceiverX2());
		corrInf.setCorrX3(messageDetails.getInstReceiverX3());
		corrInf.setCorrX4(messageDetails.getInstReceiverX4());
		corrInf.setMesgDate(messageDetails.getMesgCreaDateTime());
		messageDetails.setMesgReceiverAddress(commonService.getCorrInfoString(daynamicMsgDetailsParam.getLoggedInUser(), corrInf));

		/*
		 * String appeNonRepudiationType= viewerDAO.setAppeNonRepudiationType( aid, umidl, umidh, mesg_crea_date_formatted, 0 );//instNum is always 0 in the fat client if ( appeNonRepudiationType !=
		 * null && !appeNonRepudiationType.trim().isEmpty() ){ if( "1".equalsIgnoreCase( appeNonRepudiationType.trim() ) ){ appeNonRepudiationType= "True"; }else{ appeNonRepudiationType= "False"; } }
		 * else{ appeNonRepudiationType= ""; } messsageDetails.setAppeNonRepudiationType(appeNonRepudiationType);
		 */

		messageDetails.setIsBeingUpdated(viewerDAO.isBeingUpdated(daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh(), daynamicMsgDetailsParam.getMesg_crea_date()));

		if (StringUtils.equalsIgnoreCase(messageDetails.getMesgFrmtName(), "file")) {
			messageDetails.setMesgFile(viewerDAO.getMessageFile(daynamicMsgDetailsParam.getAid(), daynamicMsgDetailsParam.getUmidl(), daynamicMsgDetailsParam.getUmidh(), messageDetails.getMesgCreaDateTimeOnDB()));
		}
		return messageDetails;
	}

	private String getBlock5(MessageDetails messsageDetails) {
		if (messsageDetails.getTextSwiftBlock5() == null || messsageDetails.getTextSwiftBlock5().trim().isEmpty()) {
			return "";
		}
		return "\r\nBlock 5:\r\n" + messsageDetails.getTextSwiftBlock5().trim();
	}

	private String getMesgStatus(MessageDetails messsageDetails) {
		String status = "";

		if (messsageDetails.getMesgUserIssuedAsPde() != null && messsageDetails.getMesgUserIssuedAsPde().equals("1")) {
			status = status + "Possible Duplicate Emission\n";
		}
		if (StringUtils.containsIgnoreCase(messsageDetails.getMesgPossibleDupCreation(), "PDE") && status.isEmpty()) {
			status = status + "Possible Duplicate Emission\n";
		}
		if (StringUtils.containsIgnoreCase(messsageDetails.getMesgPossibleDupCreation(), "PDR")) {
			status = status + "Possible Duplicate Reception\n";
		}
		if (messsageDetails.getMesgIsPartial() == true) {
			status = status + "Partial Message\n";
		}
		if (StringUtils.equalsIgnoreCase(messsageDetails.getMesgClass(), "MESG_NORMAL")) {
			status = status + "Normal\n";
		}
		if (StringUtils.equalsIgnoreCase(messsageDetails.getMesgClass(), "MESG_SCISSOR")) {
			status = status + "Scissor \n";
		}
		if (StringUtils.equalsIgnoreCase(messsageDetails.getMesgClass(), "MESG_BROADCAST")) {
			status = status + "Broadcast " + messsageDetails.getMesgReceiverAliaName() + "\n";
		} else if (messsageDetails.getMesgIsLive() != null && messsageDetails.getMesgIsLive() == false) {
			status = status + "Test Message\n";
		}
		if (messsageDetails.getMesgIsTextReadonly() == true) {
			status = status + "Read-only\n";
		}
		if (messsageDetails.getMesgIsTextModified() == true) {
			status = status + "Message Modified\n";
		}
		if (messsageDetails.getMesgIsDeleteInhibited() == false) {
			status = status + "Deletable\n";
		}
		if (messsageDetails.getMesgIsRetrieved() != null && messsageDetails.getMesgIsRetrieved() == true) {
			status = status + "Message Retrieved\n";
		}
		if (!status.isEmpty()) {
			status = status.substring(0, status.length() - 1);
		}

		return status;
	}

	// @Override
	private Pair<String, Boolean> getUnexpandedText(String loggedInUser, String currentText, String mesg_type, String mesg_format, int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset) {
		Pair<String, Boolean> result = new Pair<String, Boolean>();
		result.setKey(currentText);

		if (StringUtils.isEmpty(currentText)) {
			if (!ViewerServiceUtils.IsSystemMessage(mesg_type)) {
				List<TextFieldData> textFieldData = viewerDAO.getTextFieldData(aid, umidl, umidh, mesg_crea_date, timeZoneOffset);
				if (textFieldData == null || textFieldData.size() == 0) {
					return null;// error: Record Set is null !
				}
				result = buildUnexpandedText(textFieldData);
			} else {
				List<TextFieldData> textFieldData = viewerDAO.getSystemTextFieldData(aid, umidl, umidh, mesg_crea_date, timeZoneOffset);
				if (textFieldData == null || textFieldData.size() == 0) {
					return null;// error: Record Set is null !
				}
				result.setKey("");
				for (int i = 0; i < textFieldData.size(); ++i) {
					if (textFieldData.get(i).getFieldCode() == null) {
						throw new NullPointerException();

					}
					String str = textFieldData.get(i).getValue();
					str = StringUtils.replace(str, "\\r\\n", "\r\n"); // added
					// to
					// remove
					// \r\n
					result.setKey(result.getKey() + "{" + textFieldData.get(i).getFieldCode().toString() + ":" + StringUtils.defaultString(str) + "}");
					result.setValue(false);
				}
			}
		} else {
			result.setValue(false);
			if ((!ViewerServiceUtils.IsSystemMessage(StringUtils.defaultString(mesg_type, "XXX")) && StringUtils.equalsIgnoreCase(StringUtils.substring(result.getKey(), 0, 2), "\n:")) || (StringUtils.equalsIgnoreCase(mesg_format, "MX"))) {
				result.setValue(true);
			}
		}
		if (StringUtils.equalsIgnoreCase(mesg_format, "MX")) {
			result.setValue(true);
		}
		return result;

	}

	private Pair<String, Boolean> buildUnexpandedText(List<TextFieldData> textFieldData) {
		Pair<String, Boolean> result = new Pair<String, Boolean>();
		result.setValue(true);
		result.setKey("");
		for (int i = 0; i < textFieldData.size(); ++i) {
			if (textFieldData.get(i).getFieldCode() == null) {
				throw new NullPointerException();
			}
			int iCode = textFieldData.get(i).getFieldCode();
			if (iCode == 0) {
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = ApplicationUtils.convertClob2String(textFieldData.get(i).getValueMemo());
				}
				result.setKey(result.getKey() + "\n" + StringUtils.defaultString(valueMemo));
			} else if (iCode > 0 && iCode < 100) {
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = ApplicationUtils.convertClob2String(textFieldData.get(i).getValueMemo());
				}
				result.setKey(result.getKey() + "\n:" + iCode + StringUtils.defaultString(textFieldData.get(i).getFieldOption()) + ":");
				if (!StringUtils.isEmpty(textFieldData.get(i).getValue())) {
					result.setKey(result.getKey() + StringUtils.defaultString(textFieldData.get(i).getValue()));
				} else {
					result.setKey(result.getKey() + valueMemo);
				}
			} else if (iCode == 255) {
				result.setValue(false);
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = ApplicationUtils.convertClob2String(textFieldData.get(i).getValueMemo());
				}
				result.setKey(result.getKey() + valueMemo);
			}
		}
		return result;
	}

	@Override
	public List<InstanceDetails> getInstanceList(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset) {
		return viewerDAO.getInstanceList(aid, umidl, umidh, mesg_crea_date, timeZoneOffset);
	}

	private List<MessageNote> getMessageNotesList(int aid, int umidl, int umidh) {
		List<MessageNote> messageNotes = viewerDAO.getMessageNotesList(aid, umidl, umidh);
		long userId = 0;
		for (MessageNote messageNote : messageNotes) {
			// get fetched user id
			userId = messageNote.getCreatedBy().getUserId();
			/*
			 * fetch user object from admin doa by user id and set it in message note as created by user.
			 */
			messageNote.setCreatedBy(adminDAO.getUser(userId));
		}
		return messageNotes;
	}

	@Override
	public String getHistory(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int timeZoneOffset, List<InstanceDetails> instanceList) {

		String buffer = "";
		for (int i = 0; i < instanceList.size(); ++i) {
			String sInstType = formatCase(StringUtils.substring(StringUtils.defaultString(instanceList.get(i).getInstType()).trim(), 10));
			Integer iSeqNo = instanceList.get(i).getInstNum();
			buffer = buffer + "*" + sInstType;
			if (iSeqNo > 0) {
				buffer = buffer + " - " + iSeqNo;
			}
			if (StringUtils.equalsIgnoreCase(instanceList.get(i).getInstStatus(), "LIVE")) {
				buffer = buffer + " (Live in '" + StringUtils.defaultString(instanceList.get(i).getInstRpName()) + "')\n";
			} else {
				buffer = buffer + " (Completed)\n";
			}
			if (iSeqNo == 0) {
				buffer = buffer + "\n";
			}
			SimpleDateFormat formatter1 = new SimpleDateFormat(dateFormat);
			SimpleDateFormat formatter2 = new SimpleDateFormat(timeFormat);

			if (StringUtils.equalsIgnoreCase(instanceList.get(i).getInstCreaMpfnName(), "mpc") || StringUtils.equalsIgnoreCase(instanceList.get(i).getInstCreaMpfnName(), "_SI_to_SWIFT")) {
				Date date = instanceList.get(i).getInstCreaDateTime();
				buffer = buffer + "Created at '" + StringUtils.defaultString(instanceList.get(i).getInstCreaRpName()) + "' on " + formatter1.format(date) + " at " + formatter2.format(date) + "\n";
			}
			if (StringUtils.equalsIgnoreCase(instanceList.get(i).getInstCreaMpfnName(), "RMA") || StringUtils.equalsIgnoreCase(instanceList.get(i).getInstCreaMpfnName(), "_SI_TO_SWIFTNET")
					|| StringUtils.equalsIgnoreCase(instanceList.get(i).getInstCreaMpfnName(), "_SI_FROM_SWIFTNET")) {
				Date date = instanceList.get(i).getInstCreaDateTime();
				buffer = buffer + "Created at '" + StringUtils.defaultString(instanceList.get(i).getInstRpName()) + "' on " + formatter1.format(date) + " at " + formatter2.format(date) + "\n";
			}

			List<InterventionDetails> interventionList = viewerDAO.getInterventionList(aid, umidl, umidh, mesg_crea_date, iSeqNo, timeZoneOffset);
			if (interventionList == null) {
				return null;
			}
			long iIntvNo = 0;
			long iIntvMax = -1;
			if (interventionList.size() != 0) {
				iIntvMax = interventionList.size() - 1;
			}
			List<AppendixDetails> appendixList = viewerDAO.getAppendixList(aid, umidl, umidh, mesg_crea_date, iSeqNo, timeZoneOffset);

			if (appendixList == null) {
				return null;
			}
			long iAppeNo = 0;
			long iAppeMax = -1;
			if (appendixList.size() != 0) {
				iAppeMax = appendixList.size() - 1;
			}
			SimpleDateFormat strDateformat = new SimpleDateFormat("MM/dd/yyyy");
			while (true) {
				Date dAppeDate = new Date();
				Date dIntvDate = new Date();
				long nAppeSeq = 0L, nIntvSeq = 0L;
				if (iIntvNo > iIntvMax && iAppeNo > iAppeMax) {
					break;
				} else {
					if (iAppeNo <= iAppeMax) {
						dAppeDate = appendixList.get((int) iAppeNo).getAppeDateTime();
						nAppeSeq = appendixList.get((int) iAppeNo).getAppeSeqNbr();
					} else {
						try {
							dAppeDate = strDateformat.parse("12/31/9999");
							nAppeSeq = 4294967296L;
						} catch (Exception e) {
						}
					}
					if (iIntvNo <= iIntvMax) {
						dIntvDate = interventionList.get((int) iIntvNo).getIntvDateTime();
						nIntvSeq = interventionList.get((int) iIntvNo).getIntvSeqNbr();
					} else {
						try {
							dIntvDate = strDateformat.parse("12/31/9999");
							nIntvSeq = 4294967296L;
						} catch (Exception e) {
						}
					}
				}
				if (dAppeDate.after(dIntvDate)) {
					buffer = buffer + ViewerServiceUtils.formatIntvStatus(interventionList.get((int) iIntvNo), iIntvNo);
					iIntvNo = iIntvNo + 1;
				} else if (dAppeDate.compareTo(dIntvDate) == 0) {
					if (nAppeSeq > nIntvSeq) {
						buffer = buffer + ViewerServiceUtils.formatIntvStatus(interventionList.get((int) iIntvNo), iIntvNo);
						iIntvNo = iIntvNo + 1;
					} else {
						buffer = buffer + formatAppeStatus(appendixList.get((int) iAppeNo));
						iAppeNo = iAppeNo + 1;
					}
				} else {
					buffer = buffer + formatAppeStatus(appendixList.get((int) iAppeNo));
					iAppeNo = iAppeNo + 1;
				}
			}
			buffer = buffer + "\n";
		}
		return buffer;
	}

	private String formatAppeStatus(AppendixDetails appendixDetails) {
		String sAppeType = StringUtils.trim(appendixDetails.getAppeType());
		String sBuf2 = "";
		SimpleDateFormat formatter1 = new SimpleDateFormat(dateFormat);
		SimpleDateFormat formatter2 = new SimpleDateFormat(timeFormat);
		if (StringUtils.equalsIgnoreCase(sAppeType, "APPE_EMISSION")) {
			Date localdte = appendixDetails.getAppeDateTime();
			sBuf2 = "Sent to " + StringUtils.defaultString(appendixDetails.getAppeIAppName()) + " '" + StringUtils.defaultString(appendixDetails.getAppeSessionHolder()) + "' on " + formatter1.format(localdte) + " at " + formatter2.format(localdte)
					+ "\n";
			sBuf2 = sBuf2 + "Session Nr " + String.format("%04d", appendixDetails.getAppeSessionNbr()) + " Sequence Nr " + String.format("%06d", appendixDetails.getAppeSequenceNbr()) + " Result: "
					+ formatAckSts("", appendixDetails.getAppeNetworkDeliveryStatus(), appendixDetails.getAppeIAppName()) + "\n";
			if (appendixDetails.getAppeAckNackText() != null) {
				if (StringUtils.equalsIgnoreCase(appendixDetails.getAppeNetworkDeliveryStatus(), "DLV_ACKED")) {
					sBuf2 = sBuf2 + " ACK Text: [" + StringUtils.defaultString(appendixDetails.getAppeAckNackText()) + "]\n";
				} else if (StringUtils.equalsIgnoreCase(appendixDetails.getAppeNetworkDeliveryStatus(), "DLV_NACKED")) {
					sBuf2 = sBuf2 + " NACK Text: [" + StringUtils.defaultString(appendixDetails.getAppeAckNackText()) + "]\n";
				} else {
					sBuf2 = sBuf2 + " Text: [" + StringUtils.defaultString(appendixDetails.getAppeAckNackText()) + "]\n";
				}
			}
		} else {
			Date localdte = appendixDetails.getAppeDateTime();
			sBuf2 = "Received from " + StringUtils.defaultString(appendixDetails.getAppeIAppName()) + " '" + StringUtils.defaultString(appendixDetails.getAppeSessionHolder()) + "' on " + formatter1.format(localdte) + " at "
					+ formatter2.format(localdte) + "\n";
			sBuf2 = sBuf2 + "Session Nr " + String.format("%04d", appendixDetails.getAppeSessionNbr()) + " Sequence Nr " + String.format("%06d", appendixDetails.getAppeSequenceNbr()) + " Result: "
					+ formatAckSts("", appendixDetails.getAppeNetworkDeliveryStatus(), appendixDetails.getAppeIAppName()) + "\n";
		}

		if (!StringUtils.isEmpty(appendixDetails.getAppePkiAuthResult())) {
			String sAuthResultFormatted = "";
			sAuthResultFormatted = ViewerServiceUtils.formatAuthResult(StringUtils.defaultString(appendixDetails.getAppePkiAuthResult()));
			if (!StringUtils.isEmpty(sAuthResultFormatted)) {
				sBuf2 = sBuf2 + " MAC-Equivalent PKI Signed with result " + sAuthResultFormatted + "\n";
			}
		}
		if (!StringUtils.isEmpty(appendixDetails.getAppePkiPac2Result())) {
			String sAuthResultFormatted = ViewerServiceUtils.formatAuthResult(StringUtils.defaultString(appendixDetails.getAppePkiPac2Result()));
			if (!StringUtils.isEmpty(sAuthResultFormatted)) {
				sBuf2 = sBuf2 + " PAC-Equivalent PKI Signed with result " + sAuthResultFormatted + "\n";
			}
		}
		if (!StringUtils.isEmpty(appendixDetails.getAppeRmaCheckResult())) {
			String sAuthResultFormatted = ViewerServiceUtils.formatRmaCheckResult(StringUtils.defaultString(appendixDetails.getAppeRmaCheckResult()));
			if (!StringUtils.isEmpty(sAuthResultFormatted) && !StringUtils.equals(sAuthResultFormatted, "-")) {
				sBuf2 = sBuf2 + " RMA Check with result " + sAuthResultFormatted + "\n";
			}
		}
		return sBuf2;

	}

	@Override
	public void forceMessageUpdate(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date) {
		viewerDAO.forceMessageUpdate(aid, umidl, umidh, mesg_crea_date);
	}

	@Override
	public String formatMPFNStatus(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, String inst_status, int timeZoneOffset) {
		String leftValue = StringUtils.substring(inst_status, 0, 1);
		String rightValue = StringUtils.substring(inst_status, 1);

		String result = inst_status;
		if (NumberUtils.isDigits(rightValue) && StringUtils.equalsIgnoreCase(leftValue, "v")) {
			List<String> intvTextList = viewerDAO.getIntvText(aid, umidl, umidh, mesg_crea_date, inst_num, timeZoneOffset);
			if (intvTextList != null && intvTextList.size() != 0) {
				String intv_merged_text = intvTextList.get(0);
				int strStart = StringUtils.indexOf(intv_merged_text, " with result ");
				int strLast = StringUtils.indexOf(intv_merged_text, ";", strStart);
				if (strStart > -1 && strLast > strStart) {
					strStart = strStart + " with result ".length();
					result = StringUtils.substring(intv_merged_text, strStart, strLast);
				}
			}
		}
		return result;
	}

	@Override
	public InstanceExtDetails getInstanceDetails(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int instNum, int timeZoneOffset) {
		InstanceExtDetails instanceDetails = viewerDAO.getInstanceDetails(aid, umidl, umidh, mesg_crea_date, instNum, timeZoneOffset);

		// file the Interventions and appendices list within the instance
		/*
		 * now well just add the appendices and the interventions to the list, ordered by their sequence numbers and as the appendices and interventions lists are both ordered by their sequence
		 * numbers well just add from appendices and interventions in a way that they stay ordered i was gonna do some logic to do that but why not using the SortedMap class
		 */
		Map<Long, IntvAppe> sortedIntvsAndAppes = new TreeMap<Long, IntvAppe>();

		// fill all interventions in the map to be sorted
		List<InterventionDetails> interventions = viewerDAO.getInstanceInterventionList(aid, umidl, umidh, mesg_crea_date, instNum, timeZoneOffset);
		for (int i = 0; i < interventions.size(); ++i) {
			IntvAppe intvAppe = new IntvAppe();
			intvAppe.setAid(aid);
			intvAppe.setUmidh(umidh);
			intvAppe.setUmidl(umidl);
			intvAppe.setInstNum(instNum);
			intvAppe.setIntervention(true);
			intvAppe.setDate(interventions.get(i).getIntvDateTimeOnDB());
			intvAppe.setTimeZoneOffset(timeZoneOffset);
			intvAppe.setSeqNbr(interventions.get(i).getIntvSeqNbr());

			intvAppe.setIntv(getInterventionDetails(loggedInUser, aid, umidl, umidh, mesg_crea_date, instNum, interventions.get(i).getIntvSeqNbr(), interventions.get(i).getIntvDateTimeOnDB(), timeZoneOffset));

			intvAppe.setDisplayText(
					formatIntvIntyCategory(interventions.get(i).getIntvIntyCategory()) + ", " + StringUtils.defaultString(interventions.get(i).getIntvIntyName()) + ", " + StringUtils.defaultString(interventions.get(i).getIntvOperNickname()));

			sortedIntvsAndAppes.put((-1) * intvAppe.getSeqNbr(), intvAppe);// -1
			// *
			// to
			// sort
			// from
			// the
			// biggest
			// to
			// the
			// lowest
			// seqno
		}

		// fill all appendices in the map to be sorted
		List<AppendixDetails> appendices = viewerDAO.getAppendixList(aid, umidl, umidh, mesg_crea_date, instNum, timeZoneOffset);
		for (int i = 0; i < appendices.size(); ++i) {
			IntvAppe intvAppe = new IntvAppe();
			intvAppe.setAid(aid);
			intvAppe.setUmidh(umidh);
			intvAppe.setUmidl(umidl);
			intvAppe.setInstNum(instNum);
			intvAppe.setIntervention(false);

			intvAppe.setAppe(getAppendixDetails(loggedInUser, aid, umidl, umidh, mesg_crea_date, instNum, appendices.get(i).getAppeSeqNbr(), appendices.get(i).getAppeDateTimeOnDB(), timeZoneOffset));

			intvAppe.setDisplayText(formatCase(StringUtils.substring(appendices.get(i).getAppeType(), 5)) + ", " + StringUtils.defaultString(appendices.get(i).getAppeIAppName()) + ", "
					+ StringUtils.defaultString(appendices.get(i).getAppeSessionHolder()) + ", " + String.format("%04d", appendices.get(i).getAppeSessionNbr()) + ", " + String.format("%06d", appendices.get(i).getAppeSequenceNbr()));

			intvAppe.setDate(appendices.get(i).getAppeDateTimeOnDB());
			intvAppe.setTimeZoneOffset(timeZoneOffset);
			intvAppe.setSeqNbr(appendices.get(i).getAppeSeqNbr());
			sortedIntvsAndAppes.put((-1) * intvAppe.getSeqNbr(), intvAppe);// -1
			// *
			// to
			// sort
			// from
			// the
			// biggest
			// to
			// the
			// lowest
			// seqno
		}

		// get the interventions and appendices from the sorted map and fill
		// them into the instanceDetails object
		List<IntvAppe> instIntvList = new ArrayList<IntvAppe>();
		Iterator<Long> iterator = sortedIntvsAndAppes.keySet().iterator();
		while (iterator.hasNext()) {
			Long key = iterator.next();
			instIntvList.add(sortedIntvsAndAppes.get(key));
		}
		instIntvList = reverseList(instIntvList);
		instanceDetails.setInstIntvList(instIntvList);

		String instMpfnLastStatus = StringUtils.mid(instanceDetails.getInstLastMpfnResult(), 3, StringUtils.defaultString(instanceDetails.getInstLastMpfnResult()).length() - 3);
		instMpfnLastStatus = StringUtils.capitalize(StringUtils.lowerCase(instMpfnLastStatus));
		instMpfnLastStatus = formatMPFNStatus(loggedInUser, aid, umidl, umidh, mesg_crea_date, instNum, instMpfnLastStatus, timeZoneOffset);
		if (StringUtils.equalsIgnoreCase(instanceDetails.getInstStatus(), "LIVE")) {
			instanceDetails.setInstMpfnStatus("Instance in rp (" + StringUtils.defaultString(instanceDetails.getInstRpName()) + ") previously processed by (" + instanceDetails.getInstMpfnName() + ") with a return status=" + instMpfnLastStatus);
		} else {
			instanceDetails.setInstMpfnStatus("Instance completed last processed by (" + StringUtils.defaultString(instanceDetails.getInstRpName()) + ") with a return status=" + instMpfnLastStatus);
		}

		return instanceDetails;
	}

	private List<IntvAppe> reverseList(List<IntvAppe> instIntvList) {
		ArrayList<IntvAppe> instIntvListInv = new ArrayList<IntvAppe>();
		for (IntvAppe element : instIntvList) {
			instIntvListInv.add(0, element);
		}
		return instIntvListInv;
	}

	@Override
	public AppendixExtDetails getAppendixDetails(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, Long intv_seq_num, Date intv_date_time_on_db, int timeZoneOffset) {
		return viewerDAO.getAppendixDetails(aid, umidl, umidh, mesg_crea_date, inst_num, intv_seq_num, intv_date_time_on_db, timeZoneOffset);
	}

	@Override
	public InterventionExtDetails getInterventionDetails(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, Long intv_seq_num, Date intv_date_time_on_db, int timeZoneOffset) {
		return viewerDAO.getInterventionDetails(aid, umidl, umidh, mesg_crea_date, inst_num, intv_seq_num, intv_date_time_on_db, timeZoneOffset);
	}

	@Override
	public List<MessageDetails> findRelatedMessages(String xmlQueryReference1, String xmlQueryReference2, Date sourceMessageDate, int period) throws SQLException {
		if (period == 0) {
			period = 7;
		}
		return viewerDAO.findRelatedMessages(xmlQueryReference1, xmlQueryReference2, sourceMessageDate, period);
	}

	@Override
	public List<SearchResultEntity> searchHL(String loggedInUser, int umidl, int umidh, int timeZoneOffset, Long loggedInUserGroupID, Integer aid) throws SQLException {
		List<SearchResultEntity> results = viewerDAO.searchHL(umidl, umidh, loggedInUserGroupID, aid);
		List<Alliance> Alliances = this.getViewerSearchLookups(loggedInUser).getSourceAvailableSAA();
		for (SearchResultEntity searchResult : results) {
			for (Alliance a : Alliances) {
				if (Integer.parseInt(a.getId()) == (searchResult.getAid())) {
					searchResult.setAlliance_instance(a.getName());
					break;
				}
				String formatName = searchResult.getMesgSubFormat() != null && searchResult.getMesgSubFormat().equals("INPUT") ? "I" : searchResult.getMesgSubFormat().equals("OUTPUT") ? "O" : "";

				searchResult.setMesgSubFormat(formatName);

				searchResult.setTimeZoneOffset(timeZoneOffset);
			}
		}
		return results;
	}

	@Override
	public String formatNetworkStatus(String loggedInUser, String networkStatus) {
		return formatAckSts(loggedInUser, networkStatus, "Network");
	}

	private String formatIntvIntyCategory(String txt) {
		String tmp = "";
		tmp = formatCase(StringUtils.substring(txt, 5));
		if (StringUtils.equalsIgnoreCase(tmp, "Mesg_modified")) {
			tmp = "Message modified";
		}
		return tmp;
	}

	@Override
	public String formatAuthResult(String loggedInUser, String authResult) {
		return ViewerServiceUtils.formatAuthResult(authResult);
	}

	@Override
	public String formatRmaCheckResult(String loggedInUser, String rmaCheckResult) {
		return ViewerServiceUtils.formatRmaCheckResult(rmaCheckResult);
	}

	@Override
	public String formatAckSts(String loggedInUser, String status, String app_name) {
		return ViewerServiceUtils.formatAckSts(status, app_name);
	}

	@Override
	public void setDateTimeFormats(String loggedInUser, String dateTimeFormat, String dateFormat, String timeFormat) {
		this.setDateTimeFormat(dateTimeFormat);
		this.setDateFormat(dateFormat);
		this.setTimeFormat(timeFormat);
	}

	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	@Override
	public String mailMessages(String loggedInUser, String subject, String to, List<SearchResultEntity> messages, boolean expandText, boolean includeHistory, int timeZoneOffset, boolean showMessageText) throws InterruptedException {
		ViewerReportGenerator generator = new ViewerReportGenerator(viewerDAO, getCommonDAO(), xmlReaderDAO, dateFormat, timeFormat);
		List<MessageDetails> detailedMessages = new ArrayList<MessageDetails>();
		for (SearchResultEntity message : messages) {
			try {

				MessageDetails messageDetails = getMessageDetails(buildMsgDeaitlsParam(loggedInUser, message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB(), timeZoneOffset, includeHistory, false,
						message.getThousandAmountFormat(), message.getDecimalAmountFormat()));
				if (messageDetails != null) {
					detailedMessages.add(messageDetails);
				}
			} catch (Exception ex) {
				System.out.println("Mail Message error : " + ex.getMessage());
			}
		}
		String report = generator.printReport("", detailedMessages, true, false, expandText, includeHistory, timeZoneOffset, showMessageText, false, false);
		report = report.replaceAll("<", "&lt;");
		report = report.replaceAll(">", "&gt;");

		return mailService.sendMail(subject, to, report, null, false);
	}

	@Override
	public String getMessageExpandedText(String loggedInUser, MessageDetails messageDetails, String thousandAmountFormat, String decimalAmountFormat) throws SQLException {
		String stxVersion = StringUtils.defaultString(messageDetails.getMesgSyntaxTableVer(), "0000");
		return viewerDAO.getExpandedMesssageText(stxVersion, messageDetails.getMesgType(), messageDetails.getMesgUnExpandedText(), messageDetails.getMesgCreaDateTime(), thousandAmountFormat, decimalAmountFormat) + getBlock5(messageDetails);
	}

	@Override
	public String getMessageExpandedText(String loggedInUser, MessageDetails messageDetails, String thousandAmountFormat, String decimalAmountFormat, String fields, boolean expandMessages) throws SQLException {
		String stxVersion = StringUtils.defaultString(messageDetails.getMesgSyntaxTableVer(), "0000");
		return viewerDAO.getExpandedMesssageText(stxVersion, messageDetails.getMesgType(), messageDetails.getMesgUnExpandedText(), messageDetails.getMesgCreaDateTime(), thousandAmountFormat, decimalAmountFormat, fields, expandMessages)
				+ getBlock5(messageDetails);
	}

	@Override
	public String printMessages(String loggedInUser, List<SearchResultEntity> messages, boolean expandText, boolean includeHistory, int timeZoneOffset, boolean showMessageText, boolean printExsitPointFormatAsPDF, boolean enableCreatedBy)
			throws InterruptedException {

		if (messages == null || messages.isEmpty()) {
			return PrintMessagesStatus.EMPTY_MESSAGE.getStatus();
		}

		if (ViewerServiceUtils.containsFileMessage(messages)) {
			return PrintMessagesStatus.CONTAINS_FILE_MESSAGE.getStatus();
		}

		ViewerReportGenerator generator = new ViewerReportGenerator(viewerDAO, getCommonDAO(), xmlReaderDAO, dateFormat, timeFormat);
		List<MessageDetails> detailedMessages = new ArrayList<MessageDetails>();
		for (SearchResultEntity message : messages) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			try {

				MessageDetails messageDetails = getMessageDetails(buildMsgDeaitlsParam(loggedInUser, message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB(), timeZoneOffset, includeHistory, false,
						message.getThousandAmountFormat(), message.getDecimalAmountFormat()));
				if (messageDetails != null) {
					detailedMessages.add(messageDetails);
				}
			} catch (Exception ex) {
				System.out.println("Print Message error : " + ex.getMessage());
			}
		}
		return generator.printReport(loggedInUser, detailedMessages, false, true, expandText, includeHistory, timeZoneOffset, showMessageText, printExsitPointFormatAsPDF, enableCreatedBy);
	}

	@Override
	public String exportRJE(String loggedInUser, List<SearchResultEntity> messages, boolean withPDE, int timeZoneOffset, boolean showBolckFour) throws InterruptedException {
		String rje = (new RJEExporter(viewerDAO)).generateRJE(messages, withPDE, timeZoneOffset, loggedInUser, showBolckFour);
		return rje;
	}

	@Override
	public List<String> exportJsonRJE(String loggedInUser, List<MessageDetails> messages, boolean withPDE, int timeZoneOffset, boolean showBolckFour, boolean includeHistory) throws InterruptedException {
		return (new RJEExporter(viewerDAO)).generateJsonRJE(messages, withPDE, timeZoneOffset, loggedInUser, showBolckFour, includeHistory);
	}

	@Override
	public String exportRJEForRelatedMessage(String loggedInUser, RelatedMessage messages, boolean withPDE, int timeZoneOffset, boolean showBolckFour) throws InterruptedException {
		String rje = (new RJEExporter(viewerDAO)).generateRJEForRelated(loggedInUser, messages, withPDE, timeZoneOffset, showBolckFour);
		return rje;
	}

	@Override
	public String exportCSV(String loggedInUser, List<SearchResultEntity> messages, List<TableColumnsHeader> selectedColumnsHeadre, String csvSeperator) throws InterruptedException {
		StringBuilder result = new StringBuilder("");
		// Set<String> list = selectedColumnsMap.keySet();

		/*
		 * for(String str : list){ result.append("\""+StringUtils.defaultString(selectedColumnsMap.get( str))+"\"" +csvSeperator); }
		 */
		for (TableColumnsHeader header : selectedColumnsHeadre) {
			if (header.getColumnName().equalsIgnoreCase("note")) {
				// doNoting

			} else {

				result.append("\"" + StringUtils.defaultString(MessageViewerHeaderEnum.valueOf(header.getColumnName()).getHeader()) + "\"" + csvSeperator);

			}
			// result.append("\""+StringUtils.defaultString(header.getColumnName())+"\""
			// +csvSeperator);
		}
		result.append("\r\n");
		for (SearchResultEntity msg : messages) {
			for (TableColumnsHeader header : selectedColumnsHeadre) {

				if (header.getId() == 0) {
					result.append("\"" + StringUtils.defaultString(msg.getAlliance_instance()) + "\"" + csvSeperator);
					continue;
				}

				if (header.getId() == 1) {
					result.append("\"" + StringUtils.substring(msg.getMesgSubFormat(), 0, 1) + "\"" + csvSeperator);
					continue;
				}

				if (header.getId() == 2) {
					result.append("\"" + StringUtils.defaultString(msg.getCorrespondent()) + "\"" + csvSeperator);
					continue;
				}

				if (header.getId() == 3) {
					// fixing bug TFS 49396 related Message Reference converted to scientic notation incorrectly

					// boolean numeric = org.apache.commons.lang3.StringUtils.isNumeric(msg.getMesgReference());
					// 54666: TDR 3.2: Message with 16-digit (or longer) reference number is exported in exponential notation

					// if (numeric) {
					// result.append("\"" + "' " + StringUtils.defaultString(msg.getMesgReference()) + " '" + "\"" + csvSeperator);
					// } else {
					result.append("\"" + "' " + StringUtils.defaultString(msg.getMesgReference()) + " '" + "\"" + csvSeperator);
					// }
					continue;
				}

				if (header.getId() == 4) {
					result.append("\"" + StringUtils.defaultString(msg.getMesgFrmtName()) + "\"" + csvSeperator);
					continue;
				}

				if (header.getId() == 5) {
					result.append("\"" + StringUtils.defaultString(msg.getMesgIdentifier()) + "\"" + csvSeperator);
					continue;
				}

				if (header.getId() == 6) {
					result.append("\"" + StringUtils.defaultString(msg.getMesgStatus()) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 7) {
					result.append("\"" + Utils.formatAmount(msg.getxFinAmount(), msg.getxFinCcy(), currencies) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 8) {
					result.append("\"" + StringUtils.defaultString(msg.getxFinCcy()) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 9) {
					result.append("\"" + formatDate(msg.getxFinValueDate(), dateFormat) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 10) {
					result.append("\"" + StringUtils.defaultString(msg.getMesgSenderX1()) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 11) {
					result.append("\"" + StringUtils.defaultString(msg.getInstReceiverX1()) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 12) {
					result.append("\"" + formatDate(msg.getMesgCreaDateTime(), dateTimeFormat) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 13) {
					result.append("\"" + StringUtils.defaultString(msg.getInstRpName()) + "\"" + csvSeperator);
					continue;
				}
				if (header.getId() == 14) {
					result.append("\"" + formatNetworkStatus(loggedInUser, msg.getEmiNetworkDeliveryStatus()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 15) {
					if (msg.getEmiIAppName() != null && !msg.getEmiIAppName().isEmpty()) {
						result.append("\"" + StringUtils.defaultString(msg.getEmiIAppName()) + " " + formatNum(msg.getEmiSessionNbr(), 4) + " " + formatNum(msg.getEmiSequenceNbr(), 6));
					} else {
						result.append("\"");
					}
					result.append("\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 16) {
					if (msg.getRecIAppName() != null && !msg.getRecIAppName().isEmpty()) {
						result.append("\"" + StringUtils.defaultString(msg.getRecIAppName()) + " " + formatNum(msg.getRecSessionNbr(), 4) + " " + formatNum(msg.getRecSequenceNbr(), 6));
					} else {
						result.append("\"");
					}
					result.append("\"" + csvSeperator);
					continue;
				}

				if (header.getId() == 17) {
					result.append("\"" + StringUtils.defaultString(msg.getSlaId()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 18) {
					result.append("\"" + StringUtils.defaultString(msg.getUetr()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 19) {
					result.append("\"" + StringUtils.defaultString(msg.getMesgRelatedReference()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 20) {
					result.append("\"" + StringUtils.defaultString(msg.getMesgUserReferenceText()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 21) {
					result.append("\"" + StringUtils.defaultString(msg.getmXKeyword1()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 22) {
					result.append("\"" + StringUtils.defaultString(msg.getmXKeyword2()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 23) {
					result.append("\"" + StringUtils.defaultString(msg.getmXKeyword3()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 24) {
					result.append("\"" + StringUtils.defaultString(msg.getServiceName()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				/*
				 * //For GPI Column listOfAvialableColumns.add(new TableColumnsHeader(index++, "orderingCustomer", false, 25, false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++,
				 * "orderingInstitution", false, 25, false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++, "beneficiaryCustomer", false, 25, false,80)); listOfAvialableColumns.add(new
				 * TableColumnsHeader(index++, "accountWithInstitution", false, 25, false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++, "detailsOfcharges", false, 25, false,80));
				 * listOfAvialableColumns.add(new TableColumnsHeader(index++, "deductsFrom", false, 25, false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++, "deductsTo", false, 25,
				 * false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++, "statusCode", false, 25, false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++, "reasonCodes",
				 * false, 25, false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++, "statusOriginatorBIC", false, 25, false,80)); listOfAvialableColumns.add(new
				 * TableColumnsHeader(index++, "forwardedToAgent", false, 25, false,80)); listOfAvialableColumns.add(new TableColumnsHeader(index++, "transactionStatus", false, 25, false,80));
				 * listOfAvialableColumns.add(new TableColumnsHeader(index++, "NAKCode", false, 25, false,80));
				 */

				if (header.getId() == 25) {
					result.append("\"" + StringUtils.defaultString(msg.getOrderingCustomer()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 26) {
					result.append("\"" + StringUtils.defaultString(msg.getOrderingInstitution()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 27) {
					result.append("\"" + StringUtils.defaultString(msg.getBeneficiaryCustomer()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 28) {
					result.append("\"" + StringUtils.defaultString(msg.getAccountWithInstitution()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 29) {
					result.append("\"" + StringUtils.defaultString(msg.getDetailsOfcharges()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 30) {
					result.append("\"" + StringUtils.defaultString(msg.getDeductsFormatted()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 31) {
					result.append("\"" + StringUtils.defaultString(msg.getStatusCode()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 32) {
					result.append("\"" + StringUtils.defaultString(msg.getReasonCodes()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 33) {
					result.append("\"" + StringUtils.defaultString(msg.getStatusOriginatorBIC()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 34) {
					result.append("\"" + StringUtils.defaultString(msg.getForwardedToAgent()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 35) {
					result.append("\"" + StringUtils.defaultString(msg.getTransactionStatus()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 36) {
					result.append("\"" + StringUtils.defaultString(msg.getNAKCode()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 37) {
					result.append("\"" + StringUtils.defaultString(msg.getGpiCur()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 38) {
					result.append("\"" + StringUtils.defaultString(msg.getDate_time_suffix()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 39) {
					result.append("\"" + StringUtils.defaultString(msg.getSenderCorr()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 40) {
					result.append("\"" + StringUtils.defaultString(msg.getReceiverCorr()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 41) {
					result.append("\"" + StringUtils.defaultString(msg.getReimbursementInst()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 42) {
					result.append("\"" + StringUtils.defaultString(msg.getSattlmentMethod()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 43) {
					result.append("\"" + StringUtils.defaultString(msg.getClearingSystem()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}
				if (header.getId() == 44) {
					result.append("\"" + StringUtils.defaultString(msg.getNotifDateTime()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 45) {
					result.append("\"" + StringUtils.defaultString(msg.getStatusDesc()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

				if (header.getId() == 46) {
					result.append("\"" + StringUtils.defaultString(msg.getExchangeRate()) + "\"" + csvSeperator);
					// result.append( "\"" );
					continue;
				}

			}
			result.append("\r\n");
		}

		/*
		 * result.append("\r\n"); //result.
		 * append("\"I/O\",\"Correspondant\",\"Reference\",\"Format\",\"Identifier\",\"Status\",\"Amount\",\"Currency\",\"Value Date\",\"Sender\",\"Receiver\",\"Message Creation Date Time\",\"Net Status\",\"Emission Info\",\"Reception Info\"\r\n"
		 * ); for ( SearchResultEntity msg : messages ){ if (Thread.interrupted()) { throw new InterruptedException(); }
		 * 
		 * result.append("\"" + StringUtils.defaultString(msg.getAlliance_instance()) + "\"" +csvSeperator);
		 * 
		 * if(header.getId()== 0")) result.append( "\"" + StringUtils.defaultString(msg.getAlliance_instance()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("1")) result.append( "\"" + StringUtils.substring(msg.getMesgSubFormat(), 0, 1) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("2")) result.append( "\"" + StringUtils.defaultString(msg.getCorrespondent()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("3")) result.append( "\"" + StringUtils.defaultString(msg.getMesgReference()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("4")) result.append( "\"" + StringUtils.defaultString(msg.getMesgFrmtName()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("5")) result.append( "\"" + StringUtils.defaultString(msg.getMesgIdentifier()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("6")) result.append( "\"" + StringUtils.defaultString(msg.getMesgStatus()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("7")) result.append( "\"" + formatAmount(msg.getxFinAmount()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("8")) result.append( "\"" + StringUtils.defaultString(msg.getxFinCcy()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("9")) result.append( "\"" + formatDate( msg.getxFinValueDate() ,dateFormat ) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("10")) result.append( "\"" + StringUtils.defaultString(msg.getMesgSenderX1()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("11")) result.append( "\"" + StringUtils.defaultString(msg.getInstReceiverX1()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("12")) result.append( "\"" + formatDate( msg.getMesgCreaDateTime() ,dateTimeFormat ) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("13")) result.append( "\"" + StringUtils.defaultString(msg.getInstRpName()) + "\"" +csvSeperator);
		 * 
		 * if(list.contains("14")) result.append( "\"" + formatNetworkStatus(loggedInUser, msg.getEmiNetworkDeliveryStatus()) + "\"" +csvSeperator);
		 * 
		 * result.append( "\"" );
		 * 
		 * if(list.contains("15")){ if ( msg.getEmiIAppName() != null ){ result.append( StringUtils.defaultString(msg.getEmiIAppName()) + " " + formatNum( msg.getEmiSessionNbr(), 4 ) + " " +
		 * formatNum( msg.getEmiSequenceNbr(), 6 )); } } result.append( "\"" +csvSeperator); result.append( "\"" );
		 * 
		 * if(list.contains("16")){ if( msg.getRecIAppName() != null ){ result.append( StringUtils.defaultString(msg.getRecIAppName()) + " " + formatNum( msg.getRecSessionNbr(), 4 ) + " " + formatNum(
		 * msg.getRecSequenceNbr(), 6 )); } } result.append( "\"\r\n"); }
		 */
		return result.toString();
	}

	private String formatDate(java.sql.Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		if (date != null) {
			return sdf.format(date);
		}
		return "";
	}

	private String formatNum(String number, int size) {
		int num = 0;
		try {
			num = NumberUtils.toInt(number);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatNum(num, size);
	}

	private String formatNum(int num, int size) {
		return String.format("%0" + size + "d", num);
	}

	@Override
	public String getSetting(String loggedInUser, Long loggedInUserID, String settingName) {
		Long aid = new Long(0);// we don't care about the alliance id so that i
		// always use the 0
		ApplicationSetting applicationSetting = commonDAO.getApplicationSetting(Constants.APPLICATION_Viewer_OPTIONS, loggedInUserID, aid, settingName);
		if (applicationSetting != null) {
			return applicationSetting.getFieldValue();
		}
		return null;
	}

	@Override
	public void setSetting(String loggedInUser, Long loggedInUserID, String settingName, String value) {
		Long aid = new Long(0);// we don't care about the alliance id so that i
		// always use the 0
		ApplicationSetting applicationSetting = commonDAO.getApplicationSetting(Constants.APPLICATION_Viewer_OPTIONS, loggedInUserID, aid, settingName);
		// if the setting already exist just update it
		if (applicationSetting != null) {
			applicationSetting.setFieldValue(value);
			commonDAO.updateApplicationSetting(applicationSetting);
			return;
		}
		// els add the setting to the database
		applicationSetting = new ApplicationSetting();
		applicationSetting.setAllianceID(aid);
		applicationSetting.setFieldName(settingName);
		applicationSetting.setFieldValue(value);
		applicationSetting.setId(Constants.APPLICATION_Viewer_OPTIONS);
		applicationSetting.setUserID(loggedInUserID);
		commonDAO.addApplicationSetting(applicationSetting);
	}

	@Override
	public boolean isFieldTagValid(String loggedInUser, String fieldTag) {
		return viewerDAO.isFieldTagValid(fieldTag);
	}

	@Override
	public List<MessageSearchTemplate> getMsgSearchTemplates(long profileId) throws Exception {
		return viewerDAO.getMsgSearchTemplates(profileId);
	}

	@Override
	public void addNewMsgSearchTemplate(String templateName, String templateValue, User user) throws Exception {

		MessageSearchTemplate template = new MessageSearchTemplate();
		template.setName(templateName);
		template.setTemplateValue(templateValue);
		template.setCreatedBy(user);

		viewerDAO.addNewMsgSearchTemplate(template);
	}

	@Override
	public void addNewMsgSearchTemplate(MessageSearchTemplate template) throws Exception {
		viewerDAO.addNewMsgSearchTemplate(template);

	};

	@Override
	public List<MessageNote> reloadMessageNotes(int aid, int umidl, int umidh) {
		return viewerDAO.getMessageNotesList(aid, umidl, umidh);
	}

	@Override
	public MessageNote addNewMessageNote(MessageNote messageNote) {
		return viewerDAO.addNewMessageNote(messageNote);

	}

	@Override
	public void deleteMessageNotes(List<Long> notesIds) {
		viewerDAO.deleteMessageNotes(notesIds);
	}

	@Override
	public void deleteMsgSearchTemplate(long templateId) throws Exception {
		viewerDAO.deleteMsgSearchTemplateById(templateId);
	}

	@Override
	public List<MessageSearchTemplate> filterMsgSearchTemplates(String templateName, long profileId) throws Exception {
		return viewerDAO.filterMsgSearchTemplates(templateName, profileId);
	}

	@Override
	public List<MessageSearchTemplate> getMsgSearchTemplatesById(long templateID) throws Exception {
		return viewerDAO.getMsgSearchTemplatesById(templateID);
	}

	@Override
	public List<SearchResultEntity> getMessagesByKey(String msgDirection, String msgType, String msgRef, String msgMur, String msgSuffix, String networkDelivery) throws Exception {
		return viewerDAO.getMessagesByKey(msgDirection, msgType, msgRef, msgMur, msgSuffix, networkDelivery);
	}

	@Override
	public PayloadFile getPayloadFile(String aid, String umidl, String umidh, PayloadType payloadType) throws Exception {
		if (payloadType == null || payloadType.isNotLoaded()) {
			return new PayloadFile();
		}
		if (payloadType.isBinary()) {
			return viewerDAO.getPayloadFile(aid, umidl, umidh);
		}
		return viewerDAO.getPayloadFileText(aid, umidl, umidh);

	}

	@Override
	public List<Entry> getMessageFields(String loggedInUser, String syntaxVer, String msgType) throws SQLException, SyntaxNotFound {
		return viewerDAO.getMessageFields(syntaxVer, msgType);
	}

	@Override
	public List<SearchResultEntity> getReportMessages(DaynamicViewerParam daynamicViewerParam, String msgType) throws SQLException {
		ViewerSearchParam parameters = daynamicViewerParam.getParams().createCopy();
		parameters.setUmidFormat("Swift");
		if (msgType != null && !msgType.trim().isEmpty()) {
			if (msgType.equals("202")) {
				parameters.setIdentifier("fin.202");// needed so that we don't
				// list the 202.cov messages
				// when searching for 202
			} else if (msgType.equals("202.COV")) {
				parameters.setUmidQual("COV");
				msgType = "202";
			}

			parameters.setUmidType(msgType);
		}

		String query = getQueryBuilder().generateQuery(QueryType.Search, parameters, daynamicViewerParam.getListFilter(), daynamicViewerParam.getListMax(), daynamicViewerParam.getTimeZoneOffset(), daynamicViewerParam.getFieldSearch(),
				daynamicViewerParam.getLoggedInUser(), true, daynamicViewerParam.isShowInternalMessages(), daynamicViewerParam.getTextDecompostionType(), daynamicViewerParam.isCaseSensitive(), daynamicViewerParam.isIncludeSysMessages(),
				daynamicViewerParam.isEnableUnicodeSearch(), daynamicViewerParam.isEnableGpiSearch(), daynamicViewerParam.getGroupId(), daynamicViewerParam.getPageNumber(), daynamicViewerParam.isWebService());

		List<SearchResultEntity> res = viewerDAO.execSearchQuery(query, null, null, parameters.getQueryVariablesBinding());
		for (SearchResultEntity searchResult : res) {
			searchResult.setTimeZoneOffset(daynamicViewerParam.getTimeZoneOffset());
		}
		return res;
	}

	@Override
	public ViewerReportStreamWriter generateReport(String loggedInUser, List<SearchResultEntity> messages, String msgType, List<EntryNode> fields, ReportType type, ViewerSearchParam params, List<FieldSearchInfo> fieldSearch, boolean expandText,
			boolean landscape, Boolean showSummary, Boolean showBlockFour, String reportLogo, List<String> mesgList, String thousandAmountFormat, String decimalAmountFormat) throws Exception {
		String searchCriteria = ViewerSearchJasperReportGenerator.generateSearchCriteria(commonDAO, params, fieldSearch, dateTimeFormat, dateFormat, !landscape);

		if (msgType.equalsIgnoreCase("exsitPoint")) {
			ViewerSearchJasperReportGenerator generator = new ViewerSearchJasperReportGenerator(viewerDAO);
			ViewerReportStreamWriter streamWriter = generator.generateExitPointReport(messages, msgType, type, dateTimeFormat, loggedInUser, searchCriteria, expandText, landscape, params, showSummary, showBlockFour, reportLogo, mesgList);

			return streamWriter;
		} else {
			if (type == ReportType.XLS || type == ReportType.XLSX) {
				ViewerSearchExcelReportGenerator generator = new ViewerSearchExcelReportGenerator(viewerDAO, commonDAO);
				ViewerReportStreamWriter streamWriter = generator.generateReport(messages, msgType, dateTimeFormat, fields, searchCriteria, loggedInUser, params, showSummary, showBlockFour);

				return streamWriter;
			}

			if (type == ReportType.PDF || type == ReportType.DOC) {
				ViewerSearchJasperReportGenerator generator = new ViewerSearchJasperReportGenerator(viewerDAO);
				ViewerReportStreamWriter streamWriter = generator.generateReport(messages, msgType, type, dateTimeFormat, loggedInUser, searchCriteria, expandText, landscape, params, showSummary, showBlockFour, reportLogo, thousandAmountFormat,
						decimalAmountFormat);

				return streamWriter;
			}
		}
		LOGGER.error("Error : unhandled report type : " + type.toString());
		return null;
	}

	@Override
	public String getLatestInstalledSyntaxVer(String userName) {
		return viewerDAO.getLatestInstalledSyntaxVer();
	}

	@Override
	public List<String> getMessageTypes(String userName, String stxvesrion) {
		return viewerDAO.getMessageTypes(stxvesrion);
	}

	@Override
	public void cancleCount() throws SQLException {
		viewerDAO.cancleCount();
	}

	@Override
	public ParsedMessage getMessageExpandedTextJRE(String stxVersion, String mesgType, String blockFour, Date mesgCreaDateTime) throws SQLException {
		return viewerDAO.getExpandedMesssageTextJRE(stxVersion, mesgType, blockFour, mesgCreaDateTime);
	}

	@Override
	public Identifier getMXIdentifierKeyword(String identifier) {
		return viewerDAO.getMXIdentifierKeyword(identifier);
	}

	@Override
	public List<AddressBook> getAddressBook(Long userId, Long profileId, String filteredUserName) {
		return viewerDAO.getAddressBook(userId, profileId, filteredUserName);
	}

	@Override
	public void addAddressBook(AddressBook addressBook) {
		viewerDAO.addAddressBook(addressBook);
	}

	@Override
	public int deleteAddressBook(AddressBook addressBook) {
		return viewerDAO.deleteAddressBook(addressBook);
	}

	@Override
	public boolean checkAddressBookFeatures() {
		return getApplicationFeatures().isAddressBookSupported();
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	public XMLReaderDAO getXmlReaderDAO() {
		return xmlReaderDAO;
	}

	/**
	 * retrieve MX message text
	 */
	@Override
	public XmlExportResult exportXML(String loggedInUser, List<SearchResultEntity> messages) throws InterruptedException {

		int numberOfEmptyMessages = 0;
		List<String> detailedMessages = new ArrayList<String>();

		XmlExportResult exportResult = new XmlExportResult();

		for (SearchResultEntity message : messages) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			try {
				List<XMLMessage> xmlMessages = getXMLMessageDetails(loggedInUser, message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB());
				if (xmlMessages != null && xmlMessages.size() > 0) {

					for (XMLMessage xmlMessage : xmlMessages) {

						if (xmlMessage != null && xmlMessage.getXmlContent() != null && !xmlMessage.getXmlContent().isEmpty()) {

							detailedMessages.add(xmlMessage.getXmlContent());
						}
					}
				} else {
					numberOfEmptyMessages++;
				}

			} catch (Exception ex) {
				System.out.println("Print Message error : " + ex.getMessage());
			}
		}

		if (detailedMessages != null && detailedMessages.size() > 0) {

			exportResult.setXmlMessage(prepareMXMessages(detailedMessages));

		}

		exportResult.setMissingXMLContentCount(numberOfEmptyMessages);

		return exportResult;
	}

	/**
	 * 
	 * @param loggedInUser
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @return
	 */
	public List<XMLMessage> getXMLMessageDetails(String loggedInUser, int aid, int umidl, int umidh, Date mesg_crea_date) {

		List<XMLMessage> xmlMessages = xmlReaderDAO.getXMLMessage(aid, umidl, umidh, mesg_crea_date, true);

		return xmlMessages;
	}

	public void setXmlReaderDAO(XMLReaderDAO xmlReaderDAO) {
		this.xmlReaderDAO = xmlReaderDAO;
	}

	/**
	 * format
	 * 
	 * @param details
	 * @return
	 */
	public String prepareMXMessages(List<String> detailedMessages) {

		StringBuilder mxString = new StringBuilder();

		mxString.append("<Messages>");

		for (String detail : detailedMessages) {
			mxString.append(detail);

		}
		mxString.append("</Messages>");
		return mxString.toString();
	}

	@Override
	public List<Identifier> getIdentifiers() {
		List<Identifier> identifiers = new ArrayList<Identifier>();
		// add header identifier
		Identifier identifier = new Identifier();
		identifier.setXsdName("ahV10.xsd");
		identifier.setIdentiferName("MX Header");
		identifier.setIdentifierValue("MX Header");
		identifiers.add(identifier);
		identifiers.addAll(viewerDAO.getIdentifiers());
		return identifiers;
	}

	@Override
	public List<Identifier> getMXIdentifiers() {

		return viewerDAO.getMXIdentifiers();
	}

	@Override
	public List<String> getAllCurancy() {
		return commonDAO.getAllCurancy();
	}

	@Override
	public List<String> getAllMsgType() {
		return viewerDAO.getMessageTypes(viewerDAO.getLatestInstalledSyntaxVer());
	}

	@Override
	public List<Country> getAllCountry() {

		return commonDAO.getAllCountry();
	}

	@Override
	public List<String> getUnitOrQueue(int flag, String loggedInUser) {
		SearchLookups lookups = new SearchLookups();

		/*
		 * flag=0 >> Unit flag=1 >> Queue
		 */
		viewerDAO.fillLookupFormats(lookups);

		if (flag == 0) {
			viewerDAO.fillLookupUnits(lookups, loggedInUser);
			return lookups.getUnitsAvailable();

		} else if (flag == 1) {
			viewerDAO.fillLookupQueues(lookups);
			return lookups.getQueuesAvilable();
		}

		return null;
	}

	@Override
	public String getLastNoteToSpecificMessage(int aid, int umidl, int umidh) {
		return viewerDAO.getLastNoteToSpecificMessage(aid, umidl, umidh);
	}

	@Override
	public String getMessageIORef(boolean isOutPutMesg, String type, int aid, int umidl, int umidh, java.sql.Date craeDateTime, Integer timeZone) {
		InstanceTransmissionPrintInfo instanceTransmissionPrintInfo = viewerDAO.getInstanceTransmissionPrintInfo(aid, umidl, umidh, craeDateTime, timeZone);

		if (instanceTransmissionPrintInfo.getAppeIappName() != null) {
			if (type.equalsIgnoreCase("Input")) {
				String mesgInputReference = "";

				if (isOutPutMesg) {
					SimpleDateFormat df = new SimpleDateFormat("HHmm");
					if (instanceTransmissionPrintInfo.getAppeRemoteInputTime() != null) {
						mesgInputReference = df.format(instanceTransmissionPrintInfo.getAppeRemoteInputTime()) + " ";
					}
					mesgInputReference = mesgInputReference + StringUtils.defaultString(instanceTransmissionPrintInfo.getAppeRemoteInputReference());

				} else {
					mesgInputReference = ViewerServiceUtils.formatMessageInputReference(instanceTransmissionPrintInfo.getAppeDateTime(), instanceTransmissionPrintInfo.getAppeSessionHolder(), instanceTransmissionPrintInfo.getAppeSessionNbr(),
							instanceTransmissionPrintInfo.getAppeSequenceNbr(), instanceTransmissionPrintInfo.getAppeIappName(), instanceTransmissionPrintInfo.getAppeType(),
							StringUtils.defaultString(instanceTransmissionPrintInfo.getAppeAckNackText()));
				}
				return mesgInputReference;
			} else {
				String mesgOutputReference = "";
				mesgOutputReference = ViewerServiceUtils.formatMessageOutputReference(instanceTransmissionPrintInfo.getAppeLocalOutputTime(), instanceTransmissionPrintInfo.getAppeSessionHolder(), instanceTransmissionPrintInfo.getAppeSessionNbr(),
						instanceTransmissionPrintInfo.getAppeSequenceNbr());

				return mesgOutputReference;

			}

		}

		return "";
	}

	@Override
	public List<GpiConfirmation> getGpiAgent(String senderBic, String uter, int timeZoneOffset, List<String> selectedSAA, boolean gSRPRequest, java.sql.Date mesgCreaDateTime, String... mesgType) {
		return viewerDAO.getGpiAgent(senderBic, uter, timeZoneOffset, selectedSAA, gSRPRequest, mesgCreaDateTime, mesgType);
	}

	@Override
	public List<NotifierMessage> getPendingGpiMesg(String query, int mailAtempt, int confirmAtempt, int timeInterval) throws InterruptedException, SQLException {
		try {
			return viewerDAO.getPendingGpiMesg(query, mailAtempt, confirmAtempt, timeInterval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MessageKey> getMessageKeys(int aid, boolean isPartitionedDatabase, String fromDate, String toDate, int bulkSize) {

		return viewerDAO.getMessagesKeys(aid, isPartitionedDatabase, fromDate, toDate, bulkSize);
	}

	@Override
	public List<MessageKey> getMessageUCKeys(int aid, boolean isPartitionedDatabase) {
		return viewerDAO.getMessagesUCKeys(aid, isPartitionedDatabase);
	}

	@Override
	public List<TextFieldData> getMessageFields(MessageKey key, boolean isPartitionedDatabase) {
		return viewerDAO.getMessageFields(key, isPartitionedDatabase);
	}

	@Override
	public void updateGPIFields(MessageKey key, GPIMesgFields gpiMessageFields, MessageParsingResult parsingResult) {

		LOGGER.debug("Update GPI FIELDS for Message with AID: " + key.getAid() + " and UMIDL " + key.getUnidl() + " and UMIDH " + key.getUmidh());
		viewerDAO.updateGPIFields(key, gpiMessageFields, parsingResult);
	}

	@Override
	public void updateTransactionStatus(MessageKey key, GPIMesgFields gpiMessageFields, String dir) {
		LOGGER.debug("Update Transaction Status for 103 Message: " + key.getAid() + " and UMIDL " + key.getUnidl() + " and UMIDH " + key.getUmidh());
		viewerDAO.updateTransactionStatus(key, gpiMessageFields, dir);

	}

	// For Code Refactoring

	@Override
	public List<String> initColumnVisibilty(String loggedInUser, Long loggedInUserID, String settingName, List<TableColumnsHeader> avialableColumns) {

		String columnVisibleStr = getSetting(loggedInUser, loggedInUserID, "vwVisibleColumns");

		if (columnVisibleStr != null && columnVisibleStr.trim() != "") {
			String[] str = columnVisibleStr.split(",");
			List<String> list = new LinkedList<String>();
			for (int i = 0; i < str.length; i++) {
				list.add(str[i]);
			}

			return list;
		} else {
			List<String> list = new LinkedList<String>();
			String columns = "";
			for (TableColumnsHeader columnsHeader : avialableColumns) {
				if (columnsHeader.isSeletedByDefault()) {
					list.add(String.valueOf(columnsHeader.getId()));
					columns += columnsHeader.getId() + ",";
				}
			}
			setSetting(loggedInUser, loggedInUserID, "vwVisibleColumns", columns);
			return list;

		}

	}

	@Override
	public Map<String, String> getAddressBookMap(List<AddressBook> cashedAddressBooks) {
		Map<String, String> addressBookMap = new HashMap<String, String>();

		for (AddressBook addressBook : cashedAddressBooks) {
			addressBookMap.put(addressBook.getUserName(), addressBook.getUserEmail());
		}
		return addressBookMap;

	}

	@Override
	public String userAfterFilter(String filterByUserName) {
		if (filterByUserName == null || filterByUserName.trim().isEmpty()) {
			return "%"; // return all template when search for empty template
			// name
		}
		return filterByUserName;
	}

	@Override
	public boolean addMailToList(List<AddressBook> selectedAddressBooks, List<String> mailToList) {
		for (AddressBook addressBook : selectedAddressBooks) {
			mailToList.add(addressBook.getUserName());
		}

		if (mailToList.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public List<Long> getDeletedNodeIDs(List<MessageNote> messageNotes, User loggedInUser) {
		List<Long> notesIds = new ArrayList<Long>();
		for (MessageNote messageNote : messageNotes) {
			if (messageNote.isChecked()) {
				if (loggedInUser.getUserId().equals(messageNote.getCreatedBy().getUserId())) {
					notesIds.add(messageNote.getNoteId());
				} else {
					return null;
				}
			}
		}

		return notesIds;
	}

	@Override
	public List<SearchResultEntity> getPageListMessages(int maxFetchSize, List<SearchResultEntity> result) {
		return null;
	}

	private String formatKeyword(String keyword) {
		String formatedkeyword = null;

		String mxKeywordValueARR[] = keyword.split(":");

		if (mxKeywordValueARR != null && mxKeywordValueARR.length > 1) {

			formatedkeyword = mxKeywordValueARR[mxKeywordValueARR.length - 1];

		} else if (keyword.lastIndexOf("/") != -1) {

			formatedkeyword = keyword.substring(keyword.lastIndexOf("/") + 1, keyword.length());

		}

		return formatedkeyword;
	}

	@Override
	public void fillMXKeywordsLabel(MessageDetails messageDetails) {
		if (messageDetails.getMesgIdentifier() != null && !messageDetails.getMesgIdentifier().isEmpty()) {
			Identifier identifier = getMXIdentifierKeyword(messageDetails.getMesgIdentifier());
			if (identifier != null) {
				String mxKeyword1 = identifier.getMxKeyword1() != null && !identifier.getMxKeyword1().isEmpty() ? formatKeyword(identifier.getMxKeyword1()) : "";
				messageDetails.setMxKeyword1(mxKeyword1);

				String mxKeyword2 = identifier.getMxKeyword2() != null && !identifier.getMxKeyword2().isEmpty() ? formatKeyword(identifier.getMxKeyword2()) : "";
				messageDetails.setMxKeyword2(mxKeyword2);

				String mxKeyword3 = identifier.getMxKeyword3() != null && !identifier.getMxKeyword3().isEmpty() ? formatKeyword(identifier.getMxKeyword3()) : "";
				messageDetails.setMxKeyword3(mxKeyword3);
			} else {
				messageDetails.setMxKeyword1(messageDetails.getMesgXmlQueryRef1() != null && !messageDetails.getMesgXmlQueryRef1().isEmpty() ? "MX Keyword 1" : "");

				messageDetails.setMxKeyword2(messageDetails.getMesgXmlQueryRef2() != null && !messageDetails.getMesgXmlQueryRef2().isEmpty() ? "MX Keyword 2" : "");

				messageDetails.setMxKeyword3(messageDetails.getMesgXmlQueryRef3() != null && !messageDetails.getMesgXmlQueryRef3().isEmpty() ? "MX Keyword 3" : "");

			}

		}
	}

	@Override
	public String preperMailTo(List<SearchResultEntity> messages, boolean enableAddressBook, List<String> mailToList, String mailToFromView, String subject, Map<String, String> addressBookMap) {
		String mailTo = "";
		if (messages.isEmpty()) {
			return MailMessagesStatus.EMPTY_MESSAGE.getStatus();
		}

		String userEmail = "";
		if (enableAddressBook) {
			if (mailToList != null && mailToList.size() > 0) {
				for (String name : mailToList) {
					userEmail = userEmail.isEmpty() ? name.trim() : userEmail + "," + name.trim();
				}
			}
		} else {
			userEmail = mailToFromView.trim();
		}

		if (StringUtils.isEmpty(subject) && StringUtils.isEmpty(userEmail)) {
			return MailMessagesStatus.EMPTY_SUBJECT_AND_MAIL.getStatus();
		}
		if (StringUtils.isEmpty(subject)) {
			return MailMessagesStatus.EMPTY_SUBJECT.getStatus();
		}

		if (StringUtils.isEmpty(userEmail)) {
			return MailMessagesStatus.EMPTY_EMAIL.getStatus();
		}

		if (ViewerServiceUtils.containsFileMessage(messages)) {
			return MailMessagesStatus.CONTAINS_FILE_MESSAGE.getStatus();
		}

		String[] usersEmailArr = userEmail.split("(;|,)");

		if (enableAddressBook) {
			for (String email : usersEmailArr) {

				String emailAddress = null;

				email = email.trim();
				if (addressBookMap.get(email) != null) {
					if (!ViewerServiceUtils.isValidEmail(addressBookMap.get(email).trim())) {
						return MailMessagesStatus.INVALID_ADDRESSBOOK_EMAIL.getStatus() + "," + email;
					} else {
						emailAddress = addressBookMap.get(email).trim();
					}
				} else if (!ViewerServiceUtils.isValidEmail(email)) {
					return MailMessagesStatus.INVALID_EMAIL.getStatus() + "," + email;
				} else {
					emailAddress = email;
				}
				mailTo = mailTo.isEmpty() ? emailAddress.trim() : mailTo + "," + emailAddress.trim();
			}

		} else {
			/*
			 * depend on E-mails provided by users
			 */
			for (String email : usersEmailArr) {
				// trim is used to make sure that spaces was added near ; or ,
				if (!ViewerServiceUtils.isValidEmail(email.trim())) {
					return MailMessagesStatus.INVALID_EMAIL.getStatus() + "," + email;
				}

				mailTo = mailTo.isEmpty() ? email.trim() : mailTo + "," + email.trim();
			}

		}

		return mailTo;

	}

	@Override
	public SearchResultEntity getMessageDetailsForExport(MessageDetails msgDetails, List<List<SearchResultEntity>> pageListMessages, String loggedInUser, int umidl, int umidh, int timeZoneOffset, Long loggedInUserGroupID, Integer aid) {
		for (List<SearchResultEntity> page : pageListMessages) {
			for (SearchResultEntity message : page) {
				if (Thread.interrupted()) {
					try {
						throw new InterruptedException();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (message.getAid().equals(msgDetails.getAid()) && message.getMesgUmidl().equals(msgDetails.getMesgUmidl()) && message.getMesgUmidh().equals(msgDetails.getMesgUmidh())) {
					return message;
				}
			}
		}

		try {
			return searchHL(loggedInUser, umidl, umidh, timeZoneOffset, loggedInUserGroupID, aid).get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<SearchResultEntity> getMessagesToExport(Integer messagesToPrint, List<List<SearchResultEntity>> pageListMessages, DaynamicViewerParam searchParamMethod, int currentPage) {
		List<SearchResultEntity> messages = new ArrayList<SearchResultEntity>();
		if (messagesToPrint == ExportMessageType.ALL_MESSAGES.getType()) {
			try {
				messages = search(searchParamMethod);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			int pageCount = 0;
			for (List<SearchResultEntity> page : pageListMessages) {
				for (SearchResultEntity message : page) {
					if ((message.isChecked() && messagesToPrint == ExportMessageType.SELECTED_MESSAGE.getType()) || (messagesToPrint == ExportMessageType.CURRENT_MESSAGE_IN_PAGE.getType() && currentPage == pageCount)) {
						messages.add(message);
					}
				}
				pageCount++;
			}
		}

		return messages;
	}

	@Override
	public SearchTempletStatus addNewMsgSearchTemplate(User user, String newSearchTemplate, String Template) throws Exception {
		if (newSearchTemplate != null && !newSearchTemplate.isEmpty()) {
			List<MessageSearchTemplate> resultList = filterMsgSearchTemplates(newSearchTemplate, 0);
			if (resultList != null && resultList.size() > 0) {
				return SearchTempletStatus.SearchCriteriaNameExistValidator;
			}
			addNewMsgSearchTemplate(newSearchTemplate, Template, user);
			return SearchTempletStatus.CorrectTemplate;
		} else {
			return SearchTempletStatus.SearchCriteriaNameValidator;
		}

	}

	@Override
	public String parseTempalateParam(ViewerSearchParam searchParam, String field_name, String field_value) {
		List<String> tempList;
		DateFormat df = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		try {
			if (field_name.equalsIgnoreCase(SearchCriteria.FIELD_SEARCH)) {
				return SearchCriteria.FIELD_SEARCH;
				// viewerBean.setFieldSearchList(convertStringToListFSI(preparteStringFormTemplate(field_value)));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.TIME_ZONE)) {
				return SearchCriteria.TIME_ZONE;
				// viewerBean.setTimeZoneOffset(Integer.parseInt(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.SAA_SOURCE)) {
				tempList = ApplicationUtils.convertStringToList(preparteStringFormTemplate(field_value));
				searchParam.setSourceSelectedSAA(tempList.size() > 0 ? tempList : new ArrayList<String>());
			} else if (field_name.equalsIgnoreCase(SearchCriteria.MULTIPLE_COUNTRY)) {
				tempList = ApplicationUtils.convertStringToList(preparteStringFormTemplate(field_value));
				if (tempList.isEmpty() || tempList.size() == 0 || tempList.get(0).isEmpty()) {
				} else {
					searchParam.setSourceSelectedCountry(tempList.size() > 0 ? tempList : new ArrayList<String>());
				}
			} else if (field_name.equalsIgnoreCase(SearchCriteria.MULTIPLE_CURRENCY)) {
				tempList = ApplicationUtils.convertStringToList(preparteStringFormTemplate(field_value));
				if (tempList.isEmpty() || tempList.size() == 0 || tempList.get(0).isEmpty()) {

				} else {

					searchParam.setSourceSelectedCurancy(tempList.size() > 0 ? tempList : new ArrayList<String>());
				}
			} else if (field_name.equalsIgnoreCase(SearchCriteria.MULTIPLE_MT)) {
				tempList = ApplicationUtils.convertStringToList(preparteStringFormTemplate(field_value));
				if (tempList.isEmpty() || tempList.size() == 0 || tempList.get(0).isEmpty()) {
					// doNothing
				} else {
					searchParam.setSourceSelectedMT(tempList.size() > 0 ? tempList : new ArrayList<String>());
				}
			} else if (field_name.equalsIgnoreCase(SearchCriteria.QUEUES_SELECTED)) {
				tempList = ApplicationUtils.convertStringToList(preparteStringFormTemplate(field_value));
				searchParam.setQueuesSelected(tempList.size() > 0 ? tempList : new ArrayList<String>());
			} else if (field_name.equalsIgnoreCase(SearchCriteria.UNITS_SELECTED)) {
				tempList = ApplicationUtils.convertStringToList(preparteStringFormTemplate(field_value));
				searchParam.setUnitsSelected(tempList.size() > 0 ? tempList : new ArrayList<String>());
			} else if (field_name.equalsIgnoreCase(SearchCriteria.CREATION_DATE)) {
				searchParam.getCreationDate().parseFormatted(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.CREATION_FROM_DATE)) {// support
				// old
				// from
				// date,
				// this
				// was
				// before
				// introduce
				// the
				// Advance
				// date.
				searchParam.getCreationDate().setType(AdvancedDate.TYPE_DATE);
				searchParam.getCreationDate().setDate(df.parse(preparteStringFormTemplate(field_value)));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.CREATION_TO_DATE)) {// support
				// old
				// to
				// date,
				// this
				// was
				// before
				// introduce
				// the
				// Advance
				// date.
				searchParam.getCreationDate().setSecondDate(df.parse(preparteStringFormTemplate(field_value)));
			} else if (parseTempalateUMIDParams(searchParam, field_name, field_value)) {
				return "";
			} else if (parseTempalateContentParams(searchParam, field_name, field_value)) {
				return "";
			} else if (parseTempalateTransmissionParams(searchParam, field_name, field_value)) {
				return "";
			} else if (field_name.equalsIgnoreCase(SearchCriteria.SERVICE_NAME)) {
				searchParam.setServiceName(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.timeFrom)) {
				searchParam.getCreationDate().parseTimeFrom(field_value);
			} else if (field_name.equalsIgnoreCase(SearchCriteria.timeTo)) {
				searchParam.getCreationDate().parseTimeTo(field_value);
			} else if (field_name.equalsIgnoreCase(SearchCriteria.SERVICE_NAME_EXT)) {
				searchParam.setServiceNameExt(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.IDENTIFIER)) {
				searchParam.setIdentifier(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.INSTANCE_STATUS)) {
				searchParam.setInstanceStatus(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.SEARCH_IN)) {
				searchParam.setSearchInValue(Integer.parseInt(preparteStringFormTemplate(field_value)));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.BANKING_PRIORITY)) {
				searchParam.setBankingPriority(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.FIN_COPY)) {
				searchParam.setFinCopy(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.FIN_INFORM)) {
				searchParam.setFinInform(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.MUR)) {
				searchParam.setMur(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.UETR)) {
				searchParam.setUetr(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.SLA_ID)) {
				searchParam.setSlaId(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.REQUESTOR_DN)) {
				searchParam.setRequestorDN(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.RESPONDER_DN)) {
				searchParam.setResponserDN(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.MX_KEYWORD_1)) {
				searchParam.setMxKeyword1(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.MX_KEYWORD_2)) {
				searchParam.setMxKeyword2(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_AMOUNT_FROM)) {
				searchParam.setContentAmountFrom(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_AMOUNT_TO)) {
				searchParam.setContentAmountTo(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_AMOUNT_CURRENCY)) {
				searchParam.setContentAmountCurrency(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_VALUE_DATE_FROM)) {
				searchParam.setContentValueDateFrom(field_value.trim().length() == 0 ? null : df.parse(preparteStringFormTemplate(field_value)));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_VALUE_DATE_TO)) {
				searchParam.setContentValueDateTo(field_value.trim().length() == 0 ? null : df.parse(preparteStringFormTemplate(field_value)));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.copy)) {
				if (field_value.equalsIgnoreCase("true")) {
					searchParam.setCopy(true);
				} else {
					searchParam.setCopy(false);
				}
			} else if (field_name.equalsIgnoreCase(SearchCriteria.INCLUDE_SYS_MSG)) {

				return SearchCriteria.INCLUDE_SYS_MSG;
			} else if (field_name.equalsIgnoreCase(SearchCriteria.SEARCH_TEXT_CASE_SENSITIVE)) {

				return SearchCriteria.SEARCH_TEXT_CASE_SENSITIVE;
			} else if (field_name.equalsIgnoreCase(SearchCriteria.MX_KEYWORD_3)) {
				searchParam.setMxKeyword3(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.Account_With_INS)) {
				searchParam.setAccountWithInstitution(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.ORDERING_INS)) {
				searchParam.setOrderingInstitution(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.ORDERING_Cust)) {
				searchParam.setOrderingCustomer(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.BENFICIARY_CUST)) {
				searchParam.setBeneficiaryCustomer(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.DETAILS_OF_CHARGE)) {
				searchParam.setDetailOfCharge(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.TRANSACTION_STATUS)) {
				searchParam.setTransactionStatus(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.DEDUCT_FROM)) {
				searchParam.setDeductsFrom(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.DEDUCT_TO)) {
				searchParam.setDeductsTo(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.RESAON_CODE)) {
				searchParam.setReasonCodes(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.STATUS_CODE)) {
				searchParam.setStatusCode(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.GSRP_REASON)) {
				searchParam.setgSRPReasonCode(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.STATUS_ORG_BIC)) {
				searchParam.setStatusOriginatorBic(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.FORWORD_BIC)) {
				searchParam.setForwordBic(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.SETTLMENT_METHOD)) {
				searchParam.setSattlmentMethod(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.CLERING_SYSTEM)) {
				searchParam.setClearingSystemList(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.SERVICE_TYPE)) {
				searchParam.setServiceType(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.SENDER_CORR)) {
				searchParam.setSenderCorr(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.RECIVER_CORR)) {
				searchParam.setReceiverCorr(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.REIMBURSEMENT_INST)) {
				searchParam.setReimbursementInst(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.INS_AMOUNT_FROM)) {
				searchParam.setInsAmountFrom(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.INS_AMOUNT_TO)) {
				searchParam.setInsAmountTo(preparteStringFormTemplate(field_value));
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.GPI_CUR)) {
				searchParam.setGpiCur(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.ENABLE_GPI)) {
				String flag = (field_value.equalsIgnoreCase("true")) ? SearchCriteria.ENABLE_GPI : "";
				return flag;
			}

			else if (field_name.equalsIgnoreCase(SearchCriteria.LOGICAL_FILE_NAME)) {
				searchParam.setLogicalName(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.TRANSFER_DESCRIPTION)) {
				searchParam.setTransferDescription(preparteStringFormTemplate(field_value));
			} else if (field_name.equalsIgnoreCase(SearchCriteria.SELECTED_XPATH_EXPRESSIONS)) {
				searchParam.setXmlConditionsMetadata(ApplicationUtils.convertStringToXMLMetadata(field_value));
				if (searchParam.getXmlConditionsMetadata() != null && !searchParam.getXmlConditionsMetadata().isEmpty()) {
					/*
					 * xsdReader.setSelectedXSDName(searchParam. getXmlConditionsMetadata().get(0).getIdentifier()); searchParam.getXmlConditionsMetadata().get(0).
					 * setIdentiferValue(xsdReader.getSelectedIdentifierValue()) ; initXSD(null);
					 */
				}
			}

		} catch (Exception ex) {
			throw new WebClientException(ex.getMessage());
		}

		return field_name;
	}

	private String preparteStringFormTemplate(String str) {
		if (str == null || str.trim().length() == 0)
			return null;
		// just to make sure that a string within the template will not contain
		// && , as we use it as a separator
		str = str.replace("\\&", "&");
		return str;
	}

	private boolean parseTempalateUMIDParams(ViewerSearchParam searchParam, String field_name, String field_value) {
		if (field_name.equalsIgnoreCase(SearchCriteria.UMID_FORMAT)) {
			searchParam.setUmidFormat(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.UMID_IO)) {
			searchParam.setUmidIO(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.UMID_CORRESPONDENT)) {
			searchParam.setUmidCorrespondent(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.UMID_TYPE)) {
			searchParam.setUmidType(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.UMID_QUAL)) {
			searchParam.setUmidQual(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.UMID_REFERENCE)) {
			searchParam.setUmidReference(preparteStringFormTemplate(field_value));
		} else {
			return false;
		}

		return true;
	}

	private boolean parseTempalateContentParams(ViewerSearchParam searchParam, String field_name, String field_value) throws Exception {
		DateFormat df = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);

		if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_SENDER)) {
			searchParam.setContentSender(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_SENDER_INSTITUTION)) {
			searchParam.setContentSenderInstitution(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_SENDER_DEPARTMENT)) {
			searchParam.setContentSenderDepartment(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_SENDER_FIRSTNAME)) {
			searchParam.setContentSenderFirstName(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_SENDER_LASTNAME)) {
			searchParam.setContentSenderLastName(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_RECEIVER)) {
			searchParam.setContentReceiver(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_RECEIVER_INSTITUTION)) {
			searchParam.setContentReceiverInstitution(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_RECEIVER_DEPARTMENT)) {
			searchParam.setContentReceiverDepartment(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_RECEIVER_FIRSTNAME)) {
			searchParam.setContentReceiverFirstName(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_RECEIVER_LASTNAME)) {
			searchParam.setContentReceiverLastName(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_NATURE)) {
			searchParam.setContentNature(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_TRANSACTION_REFERENCE)) {
			searchParam.setContentTransactionReference(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_RELATED_REFERENCE)) {
			searchParam.setContentRelatedRefference(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_AMOUNT_FROM)) {
			searchParam.setContentAmountFrom(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_AMOUNT_TO)) {
			searchParam.setContentAmountTo(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_AMOUNT_CURRENCY)) {
			searchParam.setContentAmountCurrency(preparteStringFormTemplate(field_value));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_VALUE_DATE_FROM)) {
			searchParam.setContentValueDateFrom(field_value.trim().length() == 0 ? null : df.parse(preparteStringFormTemplate(field_value)));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_VALUE_DATE_TO)) {
			searchParam.setContentValueDateTo(field_value.trim().length() == 0 ? null : df.parse(preparteStringFormTemplate(field_value)));
		} else if (field_name.equalsIgnoreCase(SearchCriteria.CONTENT_SEARCH_TEXT)) {
			searchParam.setContentSearchText(preparteStringFormTemplate(field_value));
		} else {
			return false;
		}

		return true;
	}

	private boolean parseTempalateTransmissionParams(ViewerSearchParam searchParam, String fieldName, String fieldValue) {
		if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_NETWORK_NAME)) {
			searchParam.setInterventionsNetworkName(preparteStringFormTemplate(fieldValue));
		} else if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_FROM_TO_NETWORK)) {
			searchParam.setInterventionsFromToNetwork(preparteStringFormTemplate(fieldValue));
		} else if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_SESSION_HOLDER)) {
			searchParam.setInterventionsSessionHolder(preparteStringFormTemplate(fieldValue));
		} else if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_SESSION_NUMBER)) {
			searchParam.setInterventionsSessionNumber(preparteStringFormTemplate(fieldValue));
		} else if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_SEQUENCE_NUMBER_FROM)) {
			searchParam.setInterventionsSequenceNumberFrom(preparteStringFormTemplate(fieldValue));
		} else if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_SEQUENCE_NUMBER_TO)) {
			searchParam.setInterventionsSequenceNumberTo(preparteStringFormTemplate(fieldValue));
		} else if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_NETWORK_STATUS)) {
			searchParam.setEmiNetworkDeliveryStatus(preparteStringFormTemplate(fieldValue));
		} else if (fieldName.equalsIgnoreCase(SearchCriteria.INTERVENTIONS_HISTORY_TEXT)) {
			searchParam.setHistoryQueue(preparteStringFormTemplate(fieldValue));
		} else {
			return false;
		}
		return true;

	}

	@Override
	public List<SearchResultEntity> getMessagesReportMessages(DaynamicViewerParam daynamicViewerParam, String msgType, Integer reportMessagesToPrint, List<List<SearchResultEntity>> pageListMessages, int currentPage) throws SQLException {
		List<SearchResultEntity> result = new ArrayList<SearchResultEntity>();
		if (reportMessagesToPrint == ExportMessageType.ALL_MESSAGES.getType()) {
			result = getReportMessages(daynamicViewerParam, msgType);
		} else {
			int pageCount = 0;
			for (List<SearchResultEntity> page : pageListMessages) {
				for (SearchResultEntity message : page) {
					if (((message.isChecked() && reportMessagesToPrint == ExportMessageType.SELECTED_MESSAGE.getType()) || (reportMessagesToPrint == ExportMessageType.CURRENT_MESSAGE_IN_PAGE.getType() && pageCount == currentPage))
							&& "Swift".equalsIgnoreCase(message.getMesgFrmtName())) {
						// for future use: when all message types will be
						// supported
						if (msgType == null || msgType.trim().isEmpty()) {
							result.add(message);
						} else {// handle 202 and 202.COV
							if (msgType.startsWith("202") == true && message.getMesgIdentifier().endsWith(msgType)) {
								result.add(message);
							} // handle others example 103, 103.STP, 910
							else if (msgType.startsWith("202") == false && message.getMesgIdentifier().contains(msgType)) {
								result.add(message);
							}
						}
					}
				}
				pageCount++;
			}
		}
		return result;
	}

	@Override
	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg1, String errMsg2) {
		viewerDAO.insertIntoErrorld(errorExe, errorLevel, errorModule, errMsg1, errMsg2);
	}

	@Override
	public String mailMessagesForGpi(int mailAttempts, String loggedInUser, String subject, String to, List<NotifierMessage> messages, boolean expandText, boolean includeHistory, int timeZoneOffset, String duration, List<String> messagesRjeText)
			throws InterruptedException {
		ViewerReportGenerator generator = new ViewerReportGenerator(viewerDAO, getCommonDAO(), xmlReaderDAO, dateFormat, timeFormat);
		List<MessageDetails> detailedMessages = new ArrayList<MessageDetails>();

		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String curuntDate = "";
		String report = "";
		if (messagesRjeText == null || messagesRjeText.isEmpty()) {
			for (NotifierMessage message : messages) {
				try {

					curuntDate = formater.format(new Date());

					MessageDetails messageDetails = getMessageDetails(
							buildMsgDeaitlsParam(loggedInUser, message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB(), timeZoneOffset, includeHistory, false, null, null));
					messageDetails.setQueueName(message.getQueueName());
					if (messageDetails != null) {
						message.setSendMail(true);
						detailedMessages.add(messageDetails);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			report = generator.printReportForGpiNotifier(detailedMessages, true, false, expandText, includeHistory, timeZoneOffset);
		} else {
			report = generator.printReportRJEText(messagesRjeText, true, false, expandText, includeHistory, timeZoneOffset);
			for (NotifierMessage message : messages) {
				message.setSendMail(true);
			}
		}

		report = report.replaceAll("<", "&lt;");
		report = report.replaceAll(">", "&gt;");

		if (report == null || report.isEmpty()) {
			return "NOT Valid";
		}

		return mailService.sendMail(subject, to, report, null, false);

	}

	@Override
	public List<DetailsHistory> getGpiDetailsHistory(Date mesg_crea_date, String uetr, String messageHistory) throws Exception {

		List<DetailsHistory> detailsHistories = viewerDAO.getGpiDetailsHistory(uetr, mesg_crea_date);
		for (DetailsHistory detailsHistory : detailsHistories) {
			if (messageHistory != null) {
				if (!messageHistory.isEmpty())
					detailsHistory.setMesgHistory(messageHistory);
				else
					detailsHistory.setMesgHistory(null);
			}
			if (!enableCorrChacing) {
				detailsHistory.setSenderName(getExpandedBicValue(detailsHistory.getSenderBic()));
				detailsHistory.setReceiverName(getExpandedBicValue(detailsHistory.getReceiverBic()));
			} else {
				if (detailsHistory.getSenderBic() != null) {
					CorrespondentBean bicExband = correspondentsInformationMap.get(detailsHistory.getSenderBic().substring(0, 8));
					if (bicExband != null) {
						detailsHistory.setSenderName(bicExband.getCorrInstitutionName());
					}
				}
				if (detailsHistory.getReceiverBic() != null) {
					CorrespondentBean bicExband = correspondentsInformationMap.get(detailsHistory.getReceiverBic().substring(0, 8));
					if (bicExband != null) {
						detailsHistory.setReceiverName(bicExband.getCorrInstitutionName());
					}
				}
			}

		}
		return detailsHistories;
	}

	private String getExpandedBicValue(String bic) {
		CorrInfo corrInf = new CorrInfo();
		if (bic != null && !bic.isEmpty()) {
			if (correspondentsInformationMap != null && !correspondentsInformationMap.isEmpty() && correspondentsInformationMap.get(bic.substring(0, 7)) != null) {
				CorrespondentBean correspondentBean = correspondentsInformationMap.get(bic.substring(0, 7));
				if (correspondentBean != null) {
					return correspondentBean.getCorrInstitutionName();
				}
			} else {
				corrInf = getBicInfo(bic.substring(0, 7));
				if (corrInf != null) {
					CorrespondentBean corr = new CorrespondentBean();
					corr.setCorrInstitutionName(corrInf.getCorrInstitutionName());
					correspondentsInformationMap.put(bic.substring(0, 7), corr);
					return corrInf.getCorrInstitutionName();
				}
			}
		}
		return "";
	}

	@Override
	public List<AppendixDetails> getAppendixList(int aid, int umidl, int umidh, Date mesg_crea_date, int instance_no, int timeZoneOffset) {
		return viewerDAO.getAppendixList(aid, umidl, umidh, mesg_crea_date, instance_no, timeZoneOffset);
	}

	@Override
	public MessageKey getMessageKeyByUetr(String uetr, String acountNumber, String msgDirection, boolean isViewerTraker, String fromDate, String toDate) {
		try {
			return viewerDAO.getMessageKeyByUetr(uetr, acountNumber, msgDirection, isViewerTraker, fromDate, toDate);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> maskingMessagesFields(List<SearchResultEntity> messages, List<EntryNode> checkedFields) {
		List<String> messagesNotAffected = new ArrayList<>();
		for (SearchResultEntity resultEntity : messages) {
			String updateResponse = viewerDAO.maskingMessagesFields(resultEntity, checkedFields);
			if (!updateResponse.equalsIgnoreCase("succeed")) {
				messagesNotAffected.add(updateResponse);
			}
		}
		return messagesNotAffected;
	}

	public Map<String, Integer> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, Integer> currencies) {
		this.currencies = currencies;
	}

	@Override
	public Map<String, Integer> getCurrenciesISO() {
		return currencies;
	}

	@Override
	public CorrInfo getBicInfo(String bic) {
		return viewerDAO.getBicInfo(bic);
	}

	@Override
	public Message getMessageParser(String syntaxVersion, String messageType) {
		try {
			return viewerDAO.getMessageParser(syntaxVersion, messageType);
		} catch (SQLException e) {
			LOGGER.error("Error getMessageParser " + e.getMessage());
		}
		return null;
	}

	@Override
	public String getMessageTex(SearchResultEntity message, String userName) throws Exception {
		return ViewerServiceUtils.getMessageTextBlock(userName, message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB(), message.getTimeZoneOffset(), viewerDAO, null);
	}

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public ViewerDAO getViewerDAO() {
		return viewerDAO;
	}

	public void setViewerDAO(ViewerDAO viewerDAO) {
		this.viewerDAO = viewerDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public MailService getMailService() {
		return mailService;
	}

	public DBPortabilityHandler getDbPortabilityHandler() {
		return dbPortabilityHandler;
	}

	public void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandler) {
		this.dbPortabilityHandler = dbPortabilityHandler;
		ViewerServiceUtils.setDbPortabilityHandler(dbPortabilityHandler);// pass
		// it
		// to
		// the
		// utils
		// class
	}

	public AdminDAO getAdminDAO() {
		return adminDAO;
	}

	public void setAdminDAO(AdminDAO adminDAO) {
		this.adminDAO = adminDAO;
	}

	public ViewerSearchQueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public void setQueryBuilder(ViewerSearchQueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}

	public boolean isDebugSearch() {
		return debugSearch;
	}

	public void setDebugSearch(boolean debugSearch) {
		this.debugSearch = debugSearch;
	}

	public Map<String, CorrespondentBean> getCorrespondentsInformationMap() {
		return correspondentsInformationMap;
	}

	public void setCorrespondentsInformationMap(Map<String, CorrespondentBean> correspondentsInformationMap) {
		this.correspondentsInformationMap = correspondentsInformationMap;
	}

	public boolean isEnableCorrChacing() {
		return enableCorrChacing;
	}

	public void setEnableCorrChacing(boolean enableCorrChacing) {
		this.enableCorrChacing = enableCorrChacing;
	}

}
