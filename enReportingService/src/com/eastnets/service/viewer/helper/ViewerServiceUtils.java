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

package com.eastnets.service.viewer.helper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.viewer.DaynamicViewerParam;
import com.eastnets.domain.viewer.InterventionDetails;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.domain.xml.XMLConditionMetadata;
import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.utils.ApplicationUtils;

/**
 * Viewer Service Utils
 * 
 * @author EastNets
 * @since October 2, 2012
 */
public class ViewerServiceUtils implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6446954090619760817L;
	private static DBPortabilityHandler dbPortabilityHandler;

	public static void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandlerV) {
		dbPortabilityHandler = dbPortabilityHandlerV;
	}

	public static String checkAnd(String base, String ext) {
		if (!StringUtils.isEmpty(base)) {
			return base + " and " + ext;
		}
		return ext;
	}

	public static String addWildCard(String mulValue) {

		return "%" + mulValue + "%";

	}

	public static String buildXMLCondition(List<XMLConditionMetadata> xmlMetadataList) {
		if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			return buildOracleXMLCondition(xmlMetadataList);
		} else if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
			return buildSQLServerXMLCondition(xmlMetadataList);
		}
		return null;
	}

	private static String buildOracleXMLCondition(List<XMLConditionMetadata> xmlConditionMetadataList) {
		if (xmlConditionMetadataList == null || xmlConditionMetadataList.isEmpty()) {
			return "";
		}
		String condition = null;
		String xpath = null;
		StringBuilder conditions = new StringBuilder();
		if (!xmlConditionMetadataList.get(0).getIdentifier().equals("ahV10")) {
			conditions.append("and xmlText.mesg_identifier = '" + xmlConditionMetadataList.get(0).getIdentiferValue()).append('\'');
		}

		for (XMLConditionMetadata xmlConditionMeta : xmlConditionMetadataList) {
			xpath = xmlConditionMeta.getXpath().replace("/", "/*:");
			xpath = xpath.substring(0, xpath.lastIndexOf("/*:"));

			// AND XMLCast(XMLQuery( '/*:Document/*:AcctDtlsConf/*:ConfDtls/*:ConfTp' PASSING XMLTEXT_DATA RETURNING CONTENT) AS VARCHAR2(100))

			if (!xmlConditionMeta.isHeader()) {
				condition = "and XMLCast(XMLQuery( ".concat('\'' + xpath).concat("' PASSING XMLTEXT_DATA RETURNING CONTENT) AS VARCHAR2(2000))").concat(xmlConditionMeta.getOperator().getPrefixMeaning()).concat(xmlConditionMeta.getValue())
						.concat(xmlConditionMeta.getOperator().getPostfixMeaning());
			} else {
				condition = "and XMLCast(XMLQuery( ".concat('\'' + xpath).concat("' PASSING XMLTEXT_HEADER RETURNING CONTENT) AS VARCHAR2(2000))").concat(xmlConditionMeta.getOperator().getPrefixMeaning()).concat(xmlConditionMeta.getValue())
						.concat(xmlConditionMeta.getOperator().getPostfixMeaning());
			}
			conditions = conditions.append(condition);
		}
		return conditions.toString();
	}

	private static String buildSQLServerXMLCondition(List<XMLConditionMetadata> xmlConditionMetadataList) {
		String condition = null;
		String xpath = null;
		StringBuilder conditions = new StringBuilder();
		if (!xmlConditionMetadataList.get(0).getIdentifier().equals("ahV10")) {
			conditions.append("and xmlText.mesg_identifier = '" + xmlConditionMetadataList.get(0).getIdentiferValue()).append('\'');
		}
		for (XMLConditionMetadata xmlConditionMeta : xmlConditionMetadataList) {
			xpath = xmlConditionMeta.getXpath().replace("/", "/*:");
			xpath = xpath.substring(0, xpath.lastIndexOf("/*:"));
			xpath = "\'(".concat(xpath).concat(")[1]\' , 'VARCHAR(max)'");
			// xmltext_data.value('(/*/*:Document/*:semt.001.001.01/*:RltdRef/*:Ref)[1]', 'VARCHAR(max)')
			if (!xmlConditionMeta.isHeader()) {
				condition = "and xmltext_data.value(".concat(xpath).concat(")").concat(xmlConditionMeta.getOperator().getPrefixMeaning()).concat(xmlConditionMeta.getValue()).concat(xmlConditionMeta.getOperator().getPostfixMeaning());
			} else {
				condition = "and xmltext_header.value( ".concat(xpath).concat(")").concat(xmlConditionMeta.getOperator().getPrefixMeaning()).concat(xmlConditionMeta.getValue()).concat(xmlConditionMeta.getOperator().getPostfixMeaning());
			}
			conditions = conditions.append(condition);
		}
		return conditions.toString();
	}

	public static String getFormattedDate(Date date, Boolean withTime, List<Object> queryVariableBinding) {
		if (date == null) {
			return "";
		}
		String dateTime = "";
		if (withTime) {
			dateTime = " HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd" + dateTime);
		String dateStr = formatter.format(date);
		if (queryVariableBinding != null)
			queryVariableBinding.add(dateStr);
		String pattern = "YYYYMMDD";
		if (withTime) {
			pattern += " HH24:MI:SS";
		}
		return dbPortabilityHandler.getDate(dateStr, pattern);
	}

	public static String getFormattedDate(Date date, Boolean withTime) {
		if (date == null) {
			return "";
		}
		String dateTime = "";
		if (withTime) {
			dateTime = " HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd" + dateTime);
		String dateStr = formatter.format(date);
		String pattern = "YYYYMMDD";
		if (withTime) {
			pattern += " HH24:MI:SS";
		}
		return dbPortabilityHandler.getDateWithPatternNoBinding(dateStr, pattern);
	}

	public static String getBIC(String base, String institution, String department, String lastName, String firstName, List<Object> queryBindingVariable) {
		String tmp = base + "X1 like ? ";
		queryBindingVariable.add(institution);
		if (!StringUtils.isEmpty(department)) {
			tmp = tmp + " and " + base + "X2 like ? ";
			queryBindingVariable.add(department);
		}
		if (!StringUtils.isEmpty(lastName)) {
			tmp = tmp + " and " + base + "X3 like ? ";
			queryBindingVariable.add(lastName);
		}
		if (!StringUtils.isEmpty(firstName)) {
			tmp = tmp + " and " + base + "X4 like ? ";
			queryBindingVariable.add(firstName);
		}
		return tmp;
	}

	public static String sqlEscapeString(String str) {
		return StringUtils.replace(str, "'", "''");
	}

	public static String checkForWildCard(String str, List<Object> queryVariables) {
		if (StringUtils.contains(str, '%') || StringUtils.contains(str, '*') || StringUtils.contains(str, '_') || StringUtils.contains(str, '?')) {
			String tmp = StringUtils.replace(str, "*", "%");
			tmp = StringUtils.replace(str, "?", "_");
			queryVariables.add(tmp);
			return " like ? ";
		}
		queryVariables.add(str);
		return "  = ? ";
	}

	public static boolean IsSystemMessage(String mesg_type) {
		if (!StringUtils.equalsIgnoreCase(mesg_type, "XXX")) {
			// Substitute value for NULL.
			if (NumberUtils.isNumber(mesg_type)) {
				// Contains only numeric characters
				Integer val = new Integer(mesg_type);
				// Integer val = NumberUtils.createInteger(mesg_type);
				if (val != null && val > 0 && val < 100) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean IsSystemMessageServiceIdinfier(String mesg_type) {
		if (!StringUtils.equalsIgnoreCase(mesg_type, "XXX")) {
			// Substitute value for NULL.
			if (NumberUtils.isNumber(mesg_type)) {
				// Contains only numeric characters
				Integer val = new Integer(mesg_type);
				// Integer val = NumberUtils.createInteger(mesg_type);
				if (val != null) {
					String strVal = String.valueOf(val);
					if (strVal.equalsIgnoreCase("05") || strVal.equalsIgnoreCase("5")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String formatRmaCheckResult(String sts) {
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_N_A"))
			return "-";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_SUCCESS"))
			return "Success";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_FAILURE"))
			return "Failure";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_BYPASSED"))
			return "Bypassed";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NO_REC"))
			return "No Record";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_ENABLED"))
			return "Not Enabled";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_IN_VALID_PERIOD"))
			return "Invalid Period";
		if (StringUtils.equalsIgnoreCase(sts, "RMA_CHECK_NOT_AUTHORISED"))
			return "Not allowed";
		return "";
	}

	public static String formatAuthResult(String sts) {
		if (StringUtils.equalsIgnoreCase(sts, "DLV_N_A"))
			return "";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_N_A"))
			return "";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS"))
			return "Success";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS_OLD_KEY"))
			return "Old Key";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_SUCCESS_FUTURE_KEY"))
			return "Future KEY";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_FAILURE"))
			return "Failure";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_NO_KEY"))
			return "No Key";
		if (StringUtils.equalsIgnoreCase(sts, "AUTH_BYPASSED"))
			return "Bypassed";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_NO_RMA_REC"))
			return "No RMA Rec";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_RMA_NOT_ENABLED"))
			return "RMA Not Enabled";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_RMA_NOT_IN_VALID_PERIOD"))
			return "RMA not in valid period";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_DIGEST"))
			return "Invalid Digest";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_SIGN_DN"))
			return "Invalid Sign Onx";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_SIG_FAILURE"))
			return "Sig Failure";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_INVALID_CERT_ID"))
			return "Invalid Cert ID";
		if (StringUtils.equalsIgnoreCase(sts, "ADK_AUTH_NO_RMAU_BKEY"))
			return "No RMAU BKEY";
		return "";
	}

	public static String formatIntvStatus(InterventionDetails interventionDetails, long iIntvNo) {
		String str = ApplicationUtils.convertClob2String(interventionDetails.getIntvText());
		if (!StringUtils.isEmpty(str)) {
			return "By " + StringUtils.defaultString(interventionDetails.getIntvOperNickname()) + " : " + StringUtils.defaultString(str) + "\n";
		}
		// else
		return "--- Invertention " + String.format("%04d", iIntvNo) + ": Text not accessible ---\n";
	}

	public static String trimWildcard(String conditionValue) {
		Integer iLeft;
		Integer iLen;

		// Remove trailing % if defined
		iLeft = 0;
		String firstChar = StringUtils.substring(conditionValue, 0, 1);
		if (firstChar == "%") {
			iLeft = 1;
		}

		iLen = StringUtils.defaultString(conditionValue).length() - iLeft;
		String lastChar = StringUtils.substring(conditionValue, StringUtils.defaultString(conditionValue).length() - 1, StringUtils.defaultString(conditionValue).length());
		if (lastChar == "%") {
			iLen = iLen - 1;
		}

		if (StringUtils.defaultString(conditionValue).length() <= 1) {
			if (StringUtils.defaultString(conditionValue) == "%")
				return "";
			return StringUtils.defaultString(conditionValue);
		}
		return StringUtils.mid(conditionValue, iLeft, iLen);
	}

	public static String checkUpperSQL(String str, boolean caseSensitive) {
		return dbPortabilityHandler.checkUpperSQL(str, caseSensitive);
	}

	public static String checkEmptyClobSQL(String str) {
		return dbPortabilityHandler.checkEmptyClobSQL(str);
	}

	public static boolean isSqlServerDB() {
		return dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL;
	}

	public static String formatXML(String unformattedXml) {
		if (unformattedXml == null || unformattedXml.trim().length() == 0)
			return "";

		int stack = 0;
		StringBuilder pretty = new StringBuilder();
		String[] rows = unformattedXml.trim().replaceAll(">", ">\n").replaceAll("<", "\n<").split("\n");

		boolean prevRowEndTag = false;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == null || rows[i].trim().length() == 0)
				continue;

			String row = rows[i].trim();
			if (row.startsWith("<?")) {
				// xml version tag
				pretty.append(row + "\n");
				prevRowEndTag = false;
			} else if (row.startsWith("</")) {
				// closing tag
				--stack;
				if (prevRowEndTag) {
					String indent = repeatString("    ", stack);
					pretty.append("\n" + indent);
				}
				pretty.append(row);
				prevRowEndTag = true;
			} else if (row.startsWith("<")) {
				// starting tag
				String indent = repeatString("    ", stack++);
				pretty.append("\n" + indent + row);
				prevRowEndTag = false;
			} else {
				// tag data
				pretty.append(row);
				prevRowEndTag = false;
			}
		}

		return pretty.toString().trim();
	}

	private static String repeatString(String string, int identation) {
		String str = "";
		for (int i = 0; i < identation; ++i) {
			str += string;
		}
		return str;
	}

	public static String formatMessageInputReference(Date appeDateTime, String appeSessionHolder, Integer appeSessionNbr, Integer appeSequenceNbr, String appeIAppName, String appeType, String appeAckNackText) {
		String result = "";
		if (StringUtils.equalsIgnoreCase(appeIAppName, "SWIFT") && StringUtils.equalsIgnoreCase(appeType, "APPE_EMISSION")) {
			if (!StringUtils.isEmpty(appeAckNackText)) {
				String textblock = appeAckNackText;
				int index = StringUtils.indexOf(textblock, "{177:");
				if (index >= 0) {
					result = /* StringUtils.substring(textblock, index +11, index + 15) + " " + */
							StringUtils.substring(textblock, index + 5, index + 11);
				}
			}
		} else {
			// SimpleDateFormat sdf= new SimpleDateFormat("yyMMdd");
			// result = result + sdf.format( appeDateTime );
		}

		result = result + StringUtils.left(appeSessionHolder, 12);

		result = result + String.format("%04d", appeSessionNbr);
		result = result + String.format("%06d", appeSequenceNbr);
		return result;
	}

	public static String formatMessageOutputReference(Date appeLocalOutputTime, String appeSessionHolder, Integer appeSessionNbr, Integer appeSequenceNbr) {
		String result = "";
		if (appeLocalOutputTime != null) {
			SimpleDateFormat df = new SimpleDateFormat("HHmm yyMMdd");
			result = df.format(appeLocalOutputTime);
		}
		if (appeSessionHolder != null) {
			result = result + StringUtils.left(appeSessionHolder, 12);
		}
		if (appeSessionNbr != null) {
			result = result + String.format("%04d", appeSessionNbr);
		}

		if (appeSequenceNbr != null) {
			result = result + String.format("%06d", appeSequenceNbr);
		}

		return result;
	}

	public static String formatAckNak(String status) {
		if (StringUtils.equalsIgnoreCase(status, "DLV_ACKED")) {
			return "ACK";
		}
		if (StringUtils.equalsIgnoreCase(status, "DLV_NACKED")) {
			return "NAK";
		}
		return "";
	}

	public static String formatAckSts(String status, String app_name) {
		if (!StringUtils.isEmpty(status)) {
			if (StringUtils.equalsIgnoreCase(status, "DLV_N_A"))
				return "N/A";
			if (StringUtils.equalsIgnoreCase(status, "DLV_WAITING_ACK"))
				return "Waiting " + StringUtils.defaultString(app_name) + " Ack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_TIMED_OUT"))
				return StringUtils.defaultString(app_name) + " Time out";
			if (StringUtils.equalsIgnoreCase(status, "DLV_ACKED"))
				return StringUtils.defaultString(app_name) + " Ack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_NACKED"))
				return StringUtils.defaultString(app_name) + " Nack";
			if (StringUtils.equalsIgnoreCase(status, "DLV_REJECTED_LOCALLY"))
				return "Rejected Locally";
			if (StringUtils.equalsIgnoreCase(status, "DLV_ABORTED"))
				return StringUtils.defaultString(app_name) + " Aborted";
		}
		// else
		return "-";
	}

	public static String getMessageTextBlock(String loggedInUser, int aid, int umidl, int umidh, java.util.Date mesg_crea_date, Integer timeZoneOffset, ViewerDAO viewerDao, MessageDetails messageDetails) throws Exception {
		String block4 = "";
		String textBlock = viewerDao.getTextMessages(aid, umidl, umidh, mesg_crea_date, timeZoneOffset);

		String textdatablock = StringUtils.defaultString(textBlock, "");
		if (!StringUtils.isEmpty(textdatablock)) {
			// Try to get it from the rText table (SIDE Express)
			// -------------------------------------------------
			// Get full text
			block4 = textdatablock;
		} else {
			// Try to get it from the rTextField table
			// ---------------------------------------
			List<TextFieldData> textFieldDataList = viewerDao.getTextFieldData(aid, umidl, umidh, mesg_crea_date, timeZoneOffset);

			block4 = "\r\n";

			for (TextFieldData textField : textFieldDataList) {
				String filedOption = StringUtils.defaultString(textField.getFieldOption(), "");
				String fieldValue = "";
				String field = "";
				if (StringUtils.isEmpty(textField.getValue())) {
					fieldValue = ApplicationUtils.convertClob2String(textField.getValueMemo()) + "\r\n";
				} else {
					fieldValue = textField.getValue() + "\r\n";
				}

				switch (textField.getFieldCode()) {
				case 0:
					field = "";
					break;
				case 255:
					field = "";
					break;
				default:
					field = ":" + textField.getFieldCode() + filedOption + ":";
					break;
				}

				block4 = block4 + field + fieldValue;
			}
		}
		return block4;
	}

	// For Refactoring

	public static boolean isValidEmail(String mailTo) {
		Pattern pattern = Pattern.compile("^([_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))+$");
		Matcher matcher = pattern.matcher(mailTo);
		return matcher.matches();
	}

	public static String buildFileNmae(String dataTimeFormat, String extension) {
		SimpleDateFormat sdf = new SimpleDateFormat(dataTimeFormat);
		String fileName = sdf.format(new Date()) + extension;
		fileName = fileName.replace("/", "-");
		fileName = fileName.replace("\\", "-");
		fileName = fileName.replace(":", "-");
		return fileName;

	}

	public static boolean containsFileMessage(List<SearchResultEntity> messages) {
		for (SearchResultEntity message : messages) {
			if ("file".equalsIgnoreCase(message.getMesgFrmtName())) {
				return true;
			}
		}
		return false;
	}

	public static String substring(String val, String startIndexStr, String lengthStr) {
		int startIndex = Integer.parseInt(startIndexStr);
		int length = Integer.parseInt(lengthStr);
		return StringUtils.substring(val, startIndex, startIndex + length);
	}

	public static String substring(String val, String startIndex) {
		return StringUtils.substring(val, Integer.parseInt(startIndex));
	}

	public static String formatCase(String str) {
		return StringUtils.capitalize(StringUtils.lowerCase(str));
	}

	public static String buildInstanceStatusStr(String instStatus, String instLastMpfnResult, String instRpName, String instMpfnName) {
		if (StringUtils.trim(instStatus).equalsIgnoreCase("LIVE")) {
			return "Instance in rp (" + StringUtils.defaultString(instRpName) + ") previously processed by (" + StringUtils.defaultString(instMpfnName) + ") with a return status=" + instLastMpfnResult;
		} else {
			return "Instance completed last processed by (" + StringUtils.defaultString(instMpfnName) + ") with a return status=" + instLastMpfnResult;
		}

	}

	public static boolean comparinDuration(String creatDate, String curuntDate, String duration) {
		if (creatDate.isEmpty())
			return true;

		String dateStart = creatDate;
		String dateStop = curuntDate;

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		long diffMinutes = 0;
		long diffHours = 0;
		long diffDays = 0;
		long diffSeconds = 0;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000) % 24;
			diffDays = diff / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!duration.contains(":")) {
			duration = duration + ":";
		}

		String[] durArr = duration.split(":");
		if (durArr[0].equalsIgnoreCase("00") && durArr[1].equalsIgnoreCase("00")) {
			return true;
		}

		if (diffDays >= 1) {
			return true;
		} else {
			if (!durArr[0].equalsIgnoreCase("00")) {
				if (diffHours > Long.parseLong(durArr[0])) {
					return true;
				} else if (diffHours == Long.parseLong(durArr[0])) {
					if (durArr[1].equalsIgnoreCase("00")) {
						return true;
					} else {
						if (diffMinutes >= Long.parseLong(durArr[1])) {
							return true;
						} else {
							return false;
						}
					}
				} else if (diffHours < Long.parseLong(durArr[0])) {
					return false;
				}
			}

			else {
				if (diffHours == 0) {
					if (!durArr[1].equalsIgnoreCase("00")) {
						if (diffMinutes >= Long.parseLong(durArr[1])) {
							return true;
						}
					}
				} else if (!durArr[1].equalsIgnoreCase("00")) {
					return true;

				}

			}

		}
		return false;

	}

	public static DaynamicViewerParam buildSearchParam(String loggedInUser, String uetr, String msgType) {
		DaynamicViewerParam paramSearchMethod = new DaynamicViewerParam();
		paramSearchMethod.setLoggedInUser(loggedInUser);
		paramSearchMethod.setAddColums(true);
		paramSearchMethod.setParams(preperMsgSearchCriteriaParam(uetr, msgType));
		paramSearchMethod.setListFilter("");
		paramSearchMethod.setListMax(2000);
		paramSearchMethod.setTimeZoneOffset(0);
		paramSearchMethod.setFieldSearch(null);
		paramSearchMethod.setAddColums(true);
		paramSearchMethod.setShowInternalMessages(true);
		paramSearchMethod.setTextDecompostionType(1);
		paramSearchMethod.setIncludeSysMessages(true);
		paramSearchMethod.setEnableUnicodeSearch(false);
		paramSearchMethod.setEnableGpiSearch(false);
		paramSearchMethod.setDecimalAmountFormat(null);
		paramSearchMethod.setThousandAmountFormat(null);
		return paramSearchMethod;
	}

	private static ViewerSearchParam preperMsgSearchCriteriaParam(String uetr, String msgType) {
		ViewerSearchParam params = new ViewerSearchParam();
		params.setUetr(uetr);
		params.setUmidType(msgType);
		params.setUmidFormat("Swift");
		params.setContentNature("all");
		params.getCreationDate().setType(AdvancedDate.TYPE_DATE);
		params.setContentSender("Any");
		params.setContentReceiver("Any");

		params.setInterventionsNetworkName("Any");
		params.setInterventionsFromToNetwork("Any");
		params.setEmiNetworkDeliveryStatus("Any");
		params.setInstanceStatus("Any");

		return params;
	}

	/**
	 * Parse text into key/value parsed text
	 * 
	 * @param text
	 * @param parsedMessage
	 */
	public static ParsedMessage extractParsedMessage(String text) {

		// parsing REGEX
		Pattern pattern = Pattern.compile(":(\\w+?):(.*)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		Pattern pattern77e = Pattern.compile(":77E:(.*)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

		ParsedMessage parsedMessage = new ParsedMessage();

		// check 77E field (sub message)
		String field77E = null;
		Matcher matcher77e = pattern77e.matcher(text);
		if (matcher77e.find()) {
			field77E = matcher77e.group(1);
			text = matcher77e.replaceAll("");
		}

		/*
		 * fixed for the following bugs 32142 Dynamic Report: MT586 Message Expand Failed 32140 Dynamic Report: MT565 Message Expand Failed work arround to ignore last space char in text
		 */
		text = text.replaceAll("\\s+$", "");
		// line splitter
		String[] fields = text.split("\\\\r\\\\n:|\\r\\n:|\n:");

		// create ParsedField and attach it to the ParsedMessage object
		for (String field : fields) {
			Matcher matcher = pattern.matcher(":" + field);
			if (matcher.find()) {

				ParsedField parsedField = new ParsedField();
				String code = matcher.group(1);
				parsedField.setFieldCode(code);
				parsedField.setFieldOption(getFieldCodeOption(code));
				parsedField.setValue(matcher.group(2));
				parsedMessage.addParsedField(parsedField);
			}
		}

		// add sub message (77E)
		if (field77E != null) {
			ParsedField parsedField = new ParsedField();
			parsedField.setFieldCode("77E");
			parsedField.setValue(field77E);
			parsedField.setFieldOption("E");
			parsedMessage.addParsedField(parsedField);
		}

		return parsedMessage;
	}

	private static String getFieldCodeOption(String code) {
		if (code != null && code.length() == 3) {
			return code.substring(2);
		}
		return null;
	}

}
