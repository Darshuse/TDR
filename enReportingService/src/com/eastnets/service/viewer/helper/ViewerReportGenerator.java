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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.dao.xml.XMLReaderDAO;
import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.Pair;
import com.eastnets.domain.viewer.AppendixDetails;
import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.InstanceTransmissionPrintInfo;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.domain.viewer.XMLMessage;
import com.eastnets.utils.ApplicationUtils;
import com.eastnets.utils.Utils;

/**
 * Viewer Report Generator
 * 
 * @author EastNets
 * @since November 11, 2012
 */
public class ViewerReportGenerator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6097976398516034443L;
	private ViewerDAO viewerDAO;
	private CommonDAO commonDAO;
	private XMLReaderDAO xmlReaderDAO;
	private String dateFormat = "yyyy/MM/dd";
	private String timeFormat = "HH:mi:ss";
	private Boolean showMessageText = true;
	private Boolean printToFile;
	private Boolean isMail;
	private Integer pageNo;
	private Boolean includeHistory;
	private String eyeCatcher = "       ";
	private String block5 = "";
	private Boolean expandText;
	private String textBlock;

	private String mesgVersion;
	private Date mesgDate;
	private String mesg_type;

	private Integer timeZoneOffset;

	public ViewerReportGenerator(ViewerDAO viewerDAO, CommonDAO commonDAO, XMLReaderDAO xmlReaderDAO, String dateFormat, String timeFormat) {
		this.viewerDAO = viewerDAO;
		this.commonDAO = commonDAO;
		this.dateFormat = dateFormat;
		this.timeFormat = timeFormat;
		this.xmlReaderDAO = xmlReaderDAO;
	}

	public String printReport(String loggedInUser, List<MessageDetails> messages, boolean isMail, boolean printToFile, boolean expandText, boolean includeHistory, int timeZoneOffset, boolean showMessageText, boolean printExsitPointFormatAsPDF,
			boolean enableCreatedBy) throws InterruptedException {
		this.printToFile = printToFile;
		this.isMail = isMail;
		this.includeHistory = includeHistory;
		this.expandText = expandText;
		this.timeZoneOffset = timeZoneOffset;
		this.showMessageText = showMessageText;

		StringBuilder msgsFormatedBuf = new StringBuilder("");

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " " + timeFormat);

		if (!enableCreatedBy) {
			if (!isMail) {
				msgsFormatedBuf.append(sdf.format(Calendar.getInstance().getTime()) + " Printed from enReporting 3.2\r\n\r\n");
			} else {
				msgsFormatedBuf.append(sdf.format(Calendar.getInstance().getTime()) + " Sent from enReporting 3.2\r\n\r\n");
			}
		} else {
			msgsFormatedBuf.append(sdf.format(Calendar.getInstance().getTime()) + " Created by " + loggedInUser + "\r\n\r\n");
		}

		pageNo = 0;
		if (printToFile) {
			msgsFormatedBuf.append(printHdrDetails());
		}

		boolean addNewLine = false;
		for (MessageDetails message : messages) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (addNewLine) {
				msgsFormatedBuf.append("\r\n\r\n");
				if (!isMail) {
					msgsFormatedBuf.append("<^$^>");// if you want to change this sequence please search for each occurrence of it, as far as i know its only used in the ViewerBean
				}
			}
			msgsFormatedBuf.append(printMessage(message, true));
			addNewLine = true;
		}

		/*
		 * if( printToFile ) { //close the file } else { if (isMail) { //Mail.SendMail(true); } else{ //Printer.EndDoc } }
		 */
		return msgsFormatedBuf.toString();
	}

	public String printReportForGpiNotifier(List<MessageDetails> messages, boolean isMail, boolean printToFile, boolean expandText, boolean includeHistory, int timeZoneOffset) throws InterruptedException {
		this.printToFile = printToFile;
		this.isMail = isMail;
		this.includeHistory = includeHistory;
		this.expandText = expandText;
		this.timeZoneOffset = timeZoneOffset;

		StringBuilder msgsFormatedBuf = new StringBuilder("");

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " " + timeFormat);

		pageNo = 0;
		if (printToFile) {
			msgsFormatedBuf.append(printHdrDetails());
		}

		boolean addNewLine = false;
		for (MessageDetails message : messages) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (addNewLine) {
				msgsFormatedBuf.append("\r\n\r\n");
				if (!isMail) {
					msgsFormatedBuf.append("<^$^>");// if you want to change this sequence please search for each occurrence of it, as far as i know its only used in the ViewerBean
				}
			}

			msgsFormatedBuf.append(sdf.format(Calendar.getInstance().getTime()) + " This Message in Queue : " + message.getQueueName() + "\r\n\r\n");
			msgsFormatedBuf.append(printMessage(message, false));
			addNewLine = true;
		}
		return msgsFormatedBuf.toString();
	}

	public String printReportRJEText(List<String> messages, boolean isMail, boolean printToFile, boolean expandText, boolean includeHistory, int timeZoneOffset) throws InterruptedException {
		this.printToFile = printToFile;
		this.isMail = isMail;
		this.includeHistory = includeHistory;
		this.expandText = expandText;
		this.timeZoneOffset = timeZoneOffset;

		StringBuilder msgsFormatedBuf = new StringBuilder("");

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " " + timeFormat);

		pageNo = 0;
		if (printToFile) {
			msgsFormatedBuf.append(printHdrDetails());
		}
		boolean addNewLine = false;
		for (String message : messages) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (addNewLine) {
				msgsFormatedBuf.append("\r\n\r\n");
				if (!isMail) {
					msgsFormatedBuf.append("<^$^>");// if you want to change this sequence please search for each occurrence of it, as far as i know its only used in the ViewerBean
				}
			}

			// msgsFormatedBuf.append(sdf.format(Calendar.getInstance().getTime() ) + " This Message in Queue : "+ message.split(",,,")[0] +"\r\n\r\n");
			// msgsFormatedBuf.append(message.split(",,,")[1]);
			// old solution
			msgsFormatedBuf.append(message);
			addNewLine = true;
		}
		return msgsFormatedBuf.toString();
	}

	private String printMessage(MessageDetails mesg, boolean fromViewer) {
		StringBuilder msgFormatedStr = new StringBuilder("");
		if (!printToFile && !isMail) {
			// Printer.FontSize = 10
			// Printer.CurrentY = 0
			printHdrDetails();
		}
		msgFormatedStr.append(printTransmissionInfo(mesg));

		msgFormatedStr.append(printHeaderInfo(mesg));

		if (showMessageText) {
			msgFormatedStr.append(PrintTextInfo(mesg));
		}
		msgFormatedStr.append(printTrailerInfo(mesg));

		if (includeHistory) {
			msgFormatedStr.append(printHistoryInfo(mesg, fromViewer));
		}

		// if ( printToFile && !isMail ){
		// //Printer.NewPage // Send new page.
		// }
		/// *if( frmPrintReport.btnAll )
		// frmMain.StatusBar1.Panels(1).Text = "Printing message " & MsgNo & " of " & LastMsg
		// End if*/

		return msgFormatedStr.toString();
	}

	private String PrintTextInfo(MessageDetails mesg) {
		String result = "";
		String banner;
		String tmpStr = "";
		Integer mesgTypeInt;

		result += printText("-      ", false);
		banner = "--------------------------- Message Text ---------------------------    -";

		// Decode message type in numeric format.
		mesgTypeInt = 0;
		String msgType = mesg.getMesgType();
		if (msgType != null) {
			msgType = msgType.trim();
		}
		if (NumberUtils.isNumber(msgType)) {
			mesgTypeInt = Integer.parseInt(msgType);
		}

		// expand MX message
		if (mesg.isXML()) {

			List<XMLMessage> xmlMessages = xmlReaderDAO.getXMLMessage(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), true);
			if (xmlMessages != null && xmlMessages.size() > 0) {

				for (XMLMessage xmlMessage : xmlMessages) {

					if (xmlMessage != null && xmlMessage.getXmlContent() != null && !xmlMessage.getXmlContent().isEmpty()) {
						textBlock = Utils.xmlFormatter(xmlMessage.getXmlContent());
					} else {
						textBlock = "";
					}
				}

			} else {
				textBlock = "  ";
			}
		}

		if (mesgTypeInt == 0 || (mesgTypeInt >= 100 && mesgTypeInt <= 999)) {
			if (StringUtils.isEmpty(textBlock)) {
				textBlock = mesg.getMesgUnExpandedText();
			}
			String formatName = mesg.getMesgFrmtName();
			if (formatName != null) {
				formatName = formatName.trim();
			}

			if (expandText == true && (!StringUtils.equalsIgnoreCase(formatName, "internal") && !StringUtils.equalsIgnoreCase(formatName, "MX"))) {// internal messages cannot be expanded

				if (!StringUtils.isEmpty(textBlock) && expandText) {
					try {
						textBlock = viewerDAO.getExpandedMesssageText(mesgVersion, mesg.getMesgType(), textBlock, mesgDate, mesg.getThousandAmountFormat(), mesg.getDecimalAmountFormat());
					} catch (Exception e) {
					}

					if (StringUtils.isEmpty(tmpStr)) {
						tmpStr = textBlock + "\r\n";
						tmpStr = StringUtils.replace(tmpStr, "\r\n\r\n", "\r\n"); // added to remove extra empty lines
					}
				}
			} else {
				tmpStr = textBlock + "\r\n";
				tmpStr = StringUtils.replace(tmpStr, "\r\n\r\n", "\r\n"); // added to remove extra empty lines
			}
		} else {
			if (StringUtils.isEmpty(textBlock)) {
				List<TextFieldData> textFieldData = viewerDAO.getSystemTextFieldData(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), mesg.getTimeZoneOffset());
				for (int i = 0; i < textFieldData.size(); i++) {
					textBlock += ":" + textFieldData.get(i).getFieldCode().toString() + ":" + StringUtils.defaultString(textFieldData.get(i).getValue()) + "\n";

				}
				tmpStr += textBlock;

			} else {
				tmpStr = textBlock;
			}
			tmpStr = StringUtils.replace(tmpStr, "\\r\\n", "\r\n"); // added to remove \r\n
			tmpStr = StringUtils.replace(tmpStr, "\r\n\r\n", "\r\n"); // added to remove extra empty lines
		}
		tmpStr = StringUtils.replace(tmpStr, "\r\n\r\n", "\r\n"); // added to remove extra empty lines

		if (!StringUtils.isEmpty(tmpStr)) {
			result += printBanner(banner);
			// keep XML message text format as it is
			if (mesg.isXML()) {
				result += tmpStr;
			} else {
				result += printMessageText(tmpStr);
			}
		}
		return result;
	}

	private String printMessageText(String mesgText) {
		String result = "";

		mesgText = mesgText + "\r\n";

		mesgText = mesgText.replace("\r\n", "\n");
		mesgText = mesgText.replace("\n", "\r\n");

		String[] lines = mesgText.split("\r\n");
		for (String line : lines) {
			if (StringUtils.isEmpty(line)) {
				result += printText(eyeCatcher, true);
			} else {
				List<String> brokenLine = breakLongLine(line, 79);// 80 chars cause its a 0 pased index
				for (String l : brokenLine) {
					result += printText(eyeCatcher + l, true);
				}
			}
		}
		return result;
	}

	private String printHistoryInfo(MessageDetails mesg, boolean fromViewer) {
		String[] msg;
		String result = "";
		String bufferStr;
		String banner;

		if (fromViewer) {
			result += printText("-      ", false);
		} else {

			// result+=printText( "\r\n- ", false);
		}

		banner = "-----------------------  Message History ---------------------------    -";
		result += printBanner(banner);

		bufferStr = mesg.getMesgHistory();

		msg = StringUtils.split(bufferStr, "\r\n");
		if (msg != null) {
			for (String line : msg) {
				if (StringUtils.isEmpty(line)) {
					result += printText(eyeCatcher, true);
				} else {
					List<String> brokenLine = breakLongLine(line, 79);// 80 chars cause its a 0 pased index
					for (String l : brokenLine) {
						result += printText(eyeCatcher + l, true);
					}
				}

			}
		}
		return result;
	}

	private String printTrailerInfo(MessageDetails mesg) {
		String result = "";
		String banner;
		String[] blocks;
		if (StringUtils.isEmpty(block5)) {
			result += printInputMessageTrailer(mesg);
			return result;
		}
		result += printText("-      ", false);
		banner = "--------------------------- Message Trailer ------------------------    -";
		result += printBanner(banner);

		// Split elements of array
		blocks = StringUtils.split(block5, "}");

		// Scan each element and print
		for (String block : blocks) {
			result += printText(eyeCatcher + block + "}", true);
		}
		return result;
	}

	private String printInputMessageTrailer(MessageDetails mesg) {
		String result = "";
		String banner;
		boolean stop = false;
		String mac;
		String chk;

		List<AppendixDetails> appeDetailsList = viewerDAO.getAppendixList(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), 0, timeZoneOffset);
		mac = "";
		chk = "";

		for (AppendixDetails appeDetails : appeDetailsList) {
			if ("SWIFT".equalsIgnoreCase(appeDetails.getAppeIAppName()) && "APPE_EMISSION".equalsIgnoreCase(appeDetails.getAppeType())) {

				AppendixExtDetails appeExtDetails = viewerDAO.getAppendixDetails(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), 0, appeDetails.getAppeSeqNbr(), appeDetails.getAppeDateTimeOnDB(),
						timeZoneOffset);

				if (appeExtDetails.getAppeAuthValue() != null) {
					mac = "{MAC:" + StringUtils.trim(appeExtDetails.getAppeAuthValue()) + "}";
				}
				if (!StringUtils.isEmpty(appeExtDetails.getAppeCheckSumValue())) {
					chk = "{CHK:" + StringUtils.trim(appeExtDetails.getAppeCheckSumValue()) + "}";
				}
				stop = true;
			}
		}

		if (!stop) {
			return result;
		}

		result += printText("-      ", false);
		banner = "--------------------------- Message Trailer ------------------------    -";
		result += printBanner(banner);

		if (!StringUtils.isEmpty(mac)) {
			result += printText(eyeCatcher + mac, true);
		}

		if (!StringUtils.isEmpty(chk)) {
			result += printText(eyeCatcher + chk, true);
		}
		return result;
	}

	private String printHeaderInfo(MessageDetails mesg) {
		StringBuilder result = new StringBuilder("");
		String mesgHeader;
		String banner;
		String sender;
		String receiver;
		String mur;
		String bankingPriority;
		String finCopyService;
		String SlaIdintfier;
		String uniqueTransactionReference;

		if (mesg.isXML()) {
			result.append(printText("-      ", false));
			banner = "--------------------------- Message Header -------------------------    -";
			result.append(printBanner(banner));
			// Swift Input/Output : FIN 999 Free Format Message
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + mesg.getMesgFrmtName();
			mesgHeader = mesgHeader + " Sub-Format";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));

			mesgHeader = formatCase(mesg.getMesgSubFormat());
			result.append(printText(mesgHeader, true));
			// ********************************

			if (mesg.getMesgNetworkApplInd() != null) {
				mesgHeader = mesg.getMesgNetworkApplInd() + " ";
			} else {
				mesgHeader = "";
			}
			// Store message type and version in global variables. Needed later by parser
			mesg_type = StringUtils.defaultString(mesg.getMesgType(), "000");
			mesgHeader = mesgHeader + mesg_type;
			if (StringUtils.isEmpty(mesg.getMesgMesgUserGroup())) {
				mesgHeader = mesgHeader + "." + mesg.getMesgMesgUserGroup();
			}
			mesgDate = mesg.getMesgCreaDateTime();
			mesgVersion = StringUtils.defaultString(mesg.getMesgSyntaxTableVer(), "0000");
			sender = StringUtils.defaultString(mesg.getMesgRequestorDn(), "");
			mur = StringUtils.defaultString(mesg.getMesgUserReferenceText(), "");
			bankingPriority = StringUtils.defaultString(mesg.getBankingPriority(), "");
			finCopyService = StringUtils.defaultString(mesg.getMesgCopyServiceId(), "");
			receiver = StringUtils.defaultString(mesg.getInstResponderDn(), "");
			SlaIdintfier = StringUtils.defaultString(mesg.getSlaId(), "");
			uniqueTransactionReference = StringUtils.defaultString(mesg.getUetr(), "");
			// Store text block (if it is stored in text record)
			textBlock = StringUtils.defaultString(ApplicationUtils.convertClob2String(mesg.getTextDataBlock()), "");

			/*
			 * it seems that we don't use the result passed from the procedure so why not !! //Get MT expansion if defined if( NumberUtils.isNumber(mesg_type) ){ int msgTypeInt=
			 * Integer.parseInt(mesg_type); if( msgTypeInt >= 100 && msgTypeInt <= 999 ){ Pair<String, String> pair= viewerDAO.getMTExpantion( mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(),
			 * mesg_type); if( !StringUtils.isEmpty( pair.getRight() ) ){ msg = msg + " " + pair.getRight(); } } }
			 */

			// TTP 626 ***********************
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + mesg.getMesgFrmtName();
			mesgHeader = mesgHeader + " Identifier";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			mesgHeader = StringUtils.defaultString(mesg.getMesgIdentifier(), "");
			// ********************************

			result.append(printText(mesgHeader, true));

			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "Requestor DN";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			result.append(printText(sender, true));
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "Responder DN";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			result.append(printText(receiver, true));
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "Service Name";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			result.append(printText(StringUtils.defaultString(mesg.getMesgService(), ""), true));

			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "Non-repudiation";
			result.append(printText(mesgHeader, false));
			result.append(positionText(": ", eyeCatcher + "Non-repudiation", mesgHeader));
			result.append(printText(mesg.getAppeNonRepudiationType(), true));

			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "Sign message";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			result.append(printText(mesg.getMesgSecurityRequiredBool(), true)); // Should be mapped to DB field

			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "MX keyword1";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			result.append(printText(StringUtils.defaultString(mesg.getMesgXmlQueryRef1(), ""), true));

			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "MX keyword2";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			result.append(printText(StringUtils.defaultString(mesg.getMesgXmlQueryRef2(), ""), true));

			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "MX keyword3";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
			result.append(printText(StringUtils.defaultString(mesg.getMesgXmlQueryRef3(), ""), true));
			return result.toString();
		}

		result.append(printText("-      ", false));
		banner = "--------------------------- Message Header -------------------------    -";
		result.append(printBanner(banner));
		// Swift Input/Output : FIN 999 Free Format Message
		mesgHeader = eyeCatcher;
		mesgHeader = mesgHeader + mesg.getMesgFrmtName();
		mesgHeader = mesgHeader + " ";
		mesgHeader = mesgHeader + formatCase(mesg.getMesgSubFormat());
		result.append(printText(mesgHeader, false));
		result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
		mesgHeader = "";
		if (mesg.getMesgNetworkApplInd() != null) {
			mesgHeader = mesg.getMesgNetworkApplInd() + " ";
		}
		// Store message type and version in global variables. Needed later by parser
		mesg_type = StringUtils.defaultString(mesg.getMesgType(), "000");
		mesgHeader = mesgHeader + mesg_type;
		if (!StringUtils.isEmpty(mesg.getMesgMesgUserGroup())) {
			mesgHeader = mesgHeader + "." + mesg.getMesgMesgUserGroup();
		}
		mesgDate = mesg.getMesgCreaDateTime();
		mesgVersion = StringUtils.defaultString(mesg.getMesgSyntaxTableVer(), "0000");
		sender = StringUtils.defaultString(mesg.getMesgSenderX1(), "");

		mur = StringUtils.defaultString(mesg.getMesgUserReferenceText(), "");
		bankingPriority = StringUtils.defaultString(mesg.getBankingPriority(), "");
		finCopyService = StringUtils.defaultString(mesg.getMesgCopyServiceId(), "");
		receiver = mesg.getInstReceiverX1();
		SlaIdintfier = StringUtils.defaultString(mesg.getSlaId(), "");
		uniqueTransactionReference = StringUtils.defaultString(mesg.getUetr(), "");
		// Store text block (if it is stored in text record)
		textBlock = StringUtils.defaultString(ApplicationUtils.convertClob2String(mesg.getTextDataBlock()), "");

		// Get MT expansion if defined
		if (StringUtils.isNumeric(mesg_type)) {
			int msgTypeInt = Integer.parseInt(mesg_type);
			if (msgTypeInt >= 100 && msgTypeInt <= 999) {
				Pair<String, String> pair = viewerDAO.getMTExpantion(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), mesg_type);
				if (!StringUtils.isEmpty(pair.getValue())) {
					mesgHeader = mesgHeader + " " + pair.getValue();
				}
			}
		}

		result.append(printText(mesgHeader, true));

		mesgHeader = eyeCatcher;
		mesgHeader = mesgHeader + "Sender";
		result.append(printText(mesgHeader, false));
		result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
		result.append(printText(sender, true));
		result.append(expandBIC(sender, eyeCatcher + "Internal Input : "));
		mesgHeader = eyeCatcher;
		mesgHeader = mesgHeader + "Receiver";
		result.append(printText(mesgHeader, false));
		result.append(positionText(" : ", eyeCatcher + "Internal Input", mesgHeader));
		result.append(printText(receiver, true));
		result.append(expandBIC(receiver, eyeCatcher + "Internal Input : "));
		if (!StringUtils.isEmpty(mur)) {
			// MUR
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "MUR";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "MUR", mesgHeader));
			if (!StringUtils.isEmpty(bankingPriority)) {
				result.append(printText(mur, false));
			} else {
				result.append(printText(mur, true));
			}
		}
		if (!StringUtils.isEmpty(bankingPriority)) {
			// Banking Priority
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "Banking Priority";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Banking Priority", mesgHeader));
			result.append(printText(bankingPriority, true));
		}
		if (!StringUtils.isEmpty(finCopyService)) {
			// Fin Copy Service
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "FIN Copy Service";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "FIN Copy Service", mesgHeader));
			result.append(printText(finCopyService, true));
		}

		if (!StringUtils.isEmpty(SlaIdintfier)) {
			// Fin Copy Service
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "SLA Identifier";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "SLA Identifier", mesgHeader));
			result.append(printText(SlaIdintfier, true));
		}

		if (!StringUtils.isEmpty(uniqueTransactionReference)) {
			// Fin Copy Service
			mesgHeader = eyeCatcher;
			mesgHeader = mesgHeader + "Unique End-to-end Transaction Reference";
			result.append(printText(mesgHeader, false));
			result.append(positionText(" : ", eyeCatcher + "Unique End-to-end Transaction Reference", mesgHeader));
			result.append(printText(uniqueTransactionReference, true));
		}

		return result.toString();
	}

	private String expandBIC(String bicCode, String tabLength) {
		String result = "";
		String tmpStr;

		CorrInfo corrInfo = new CorrInfo();
		corrInfo.setCorrType("CORR_TYPE_INSTITUTION");
		corrInfo.setCorrX1(bicCode);
		corrInfo.setCorrX2("");
		corrInfo.setCorrX3("");
		corrInfo.setCorrX4("");
		corrInfo.setMesgDate(mesgDate);
		corrInfo = commonDAO.getCorrInfo(corrInfo);

		// Note: assumes that it is always an institution code.
		if (corrInfo.getRetstatus() == 1) {
			// Record found.
			tmpStr = WordUtils.wrap(corrInfo.getCorrInstitutionName(), 105);

			// System.out.println(tmpStr.contains("\n"));

			if (!StringUtils.isEmpty(tmpStr)) {
				result += printText(eyeCatcher, false);

				if (tmpStr.contains("\n")) {
					String tmpStrArr[] = tmpStr.split("\n");

					for (String tmpStrItem : tmpStrArr) {
						result += positionText("", tabLength, eyeCatcher);
						result += printText(tmpStrItem, false);
						result += positionText("", tabLength, eyeCatcher);
					}
					result += "\r\n";
				} else {
					result += positionText("", tabLength, eyeCatcher);
					result += printText(tmpStr, true);
				}
			}
			// Branch Info (can be blank)
			tmpStr = WordUtils.wrap(corrInfo.getCorrBranchInfo(), 70);

			if (!StringUtils.isEmpty(tmpStr)) {
				result += printText(eyeCatcher, false);
				if (tmpStr.contains("\n")) {
					String tmpStrArr[] = tmpStr.split("\n");

					for (String tmpStrItem : tmpStrArr) {
						result += positionText("", tabLength, eyeCatcher);
						result += printText(tmpStrItem, false);
						result += positionText("", tabLength, eyeCatcher);
					}
					result += "\r\n";
				} else {
					result += positionText("", tabLength, eyeCatcher);
					result += printText(tmpStr, true);
				}
			}

			// location Info (can be blank)
			tmpStr = corrInfo.getCorrLocation();
			if (!StringUtils.isEmpty(tmpStr)) {
				result += printText(eyeCatcher, false);
				result += positionText("", tabLength, eyeCatcher);
				result += printText(tmpStr, true);
			}

			// City Name
			tmpStr = corrInfo.getCorrCityName();
			if (!StringUtils.isEmpty(tmpStr)) {
				result += printText(eyeCatcher, false);
				result += positionText("", tabLength, eyeCatcher);
				result += printText(tmpStr, true);
			}
			// Country
			tmpStr = corrInfo.getCorrCtryName() + " ";
			// Country Code
			tmpStr = tmpStr + corrInfo.getCorrCtryCode();
			if (!StringUtils.isEmpty(tmpStr)) {
				result += printText(eyeCatcher, false);
				result += positionText(tmpStr + "\r\n", tabLength, eyeCatcher);
			}
		}

		return result;
	}

	private String printTransmissionInfo(MessageDetails mesg) {
		String result = "";
		String mesgTransmissionInfoText;
		String banner;
		String notification;
		String mesgInputReference = "";
		String mesgOutputReference = "";

		boolean nothingPrinted = true;
		InstanceTransmissionPrintInfo instanceTransmissionPrintInfo = viewerDAO.getInstanceTransmissionPrintInfo(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), mesg.getTimeZoneOffset());

		boolean bInputMsg = StringUtils.equalsIgnoreCase(StringUtils.trim(mesg.getMesgSubFormat()), "INPUT");
		result += printText("\r\n-      ", false);

		banner = "---------------------  Instance Type and Transmission --------------    -";
		mesgTransmissionInfoText = eyeCatcher + StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(instanceTransmissionPrintInfo.getLastInstType(), 10)));

		notification = StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(instanceTransmissionPrintInfo.getLastInstNotificationType(), 18)));

		if (StringUtils.equalsIgnoreCase(notification, "Transmission") || StringUtils.equalsIgnoreCase(notification, "Reception")) {
			mesgTransmissionInfoText = mesgTransmissionInfoText + " (";
			mesgTransmissionInfoText = mesgTransmissionInfoText + notification + ")";
			mesgTransmissionInfoText = mesgTransmissionInfoText + " of ";
			mesgTransmissionInfoText = mesgTransmissionInfoText + StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(instanceTransmissionPrintInfo.getFirstInstType(), 10)));
		}
		if (instanceTransmissionPrintInfo.getAppeIappName() != null) {
			if (bInputMsg) {
				mesgInputReference = ViewerServiceUtils.formatMessageInputReference(instanceTransmissionPrintInfo.getAppeDateTime(), instanceTransmissionPrintInfo.getAppeSessionHolder(), instanceTransmissionPrintInfo.getAppeSessionNbr(),
						instanceTransmissionPrintInfo.getAppeSequenceNbr(), instanceTransmissionPrintInfo.getAppeIappName(), instanceTransmissionPrintInfo.getAppeType(), StringUtils.defaultString(instanceTransmissionPrintInfo.getAppeAckNackText()));
				mesgTransmissionInfoText = mesgTransmissionInfoText + " sent to ";
			} else {
				mesgOutputReference = ViewerServiceUtils.formatMessageOutputReference(instanceTransmissionPrintInfo.getAppeLocalOutputTime(), instanceTransmissionPrintInfo.getAppeSessionHolder(), instanceTransmissionPrintInfo.getAppeSessionNbr(),
						instanceTransmissionPrintInfo.getAppeSequenceNbr());
				SimpleDateFormat df = new SimpleDateFormat("HHmm");
				if (instanceTransmissionPrintInfo.getAppeRemoteInputTime() != null) {
					mesgInputReference = df.format(instanceTransmissionPrintInfo.getAppeRemoteInputTime()) + " ";
				}

				mesgInputReference = mesgInputReference + StringUtils.defaultString(instanceTransmissionPrintInfo.getAppeRemoteInputReference());
				mesgTransmissionInfoText = mesgTransmissionInfoText + " received from ";
			}
			mesgTransmissionInfoText = mesgTransmissionInfoText + instanceTransmissionPrintInfo.getAppeIappName();
			if (!StringUtils.isEmpty(ViewerServiceUtils.formatAckNak(instanceTransmissionPrintInfo.getAppeNetworkDeliveryStatus()))) {
				mesgTransmissionInfoText = mesgTransmissionInfoText + " (" + ViewerServiceUtils.formatAckNak(instanceTransmissionPrintInfo.getAppeNetworkDeliveryStatus()) + ")";
			}
			if (nothingPrinted) {
				result += printBanner(banner);
				nothingPrinted = false;
			}
			result += printText(mesgTransmissionInfoText, true);
			if (bInputMsg) {
				if (StringUtils.equalsIgnoreCase(notification, "Transmission")) {
					mesgTransmissionInfoText = eyeCatcher + "Network Delivery Status";
					result += printText(mesgTransmissionInfoText, false);
					result += positionText(" : ", eyeCatcher + "Correspondent Input Reference", mesgTransmissionInfoText);
					mesgTransmissionInfoText = ViewerServiceUtils.formatAckSts(instanceTransmissionPrintInfo.getAppeNetworkDeliveryStatus(), "Network");
					result += printText(mesgTransmissionInfoText, true);
				} else {
					if (StringUtils.equalsIgnoreCase(notification, "Delivery")) {
						mesgTransmissionInfoText = eyeCatcher + "User Delivery Status";
						result += printText(mesgTransmissionInfoText, false);
						result += positionText(" : ", eyeCatcher + "Correspondent Input Reference", mesgTransmissionInfoText);
						mesgTransmissionInfoText = ViewerServiceUtils.formatAckSts(instanceTransmissionPrintInfo.getAppeRcvDeliveryStatus(), "Network");
						result += printText(mesgTransmissionInfoText, true);
					}
				}
			}
		} else {
			mesgInputReference = "";
			mesgOutputReference = "";
		}

		block5 = StringUtils.defaultString(instanceTransmissionPrintInfo.getTextSwiftBlock5(), "");

		if (bInputMsg) {
			mesgTransmissionInfoText = eyeCatcher + "Priority/Delivery";
		} else {
			mesgTransmissionInfoText = eyeCatcher + "Priority";
		}

		if (!StringUtils.isEmpty(instanceTransmissionPrintInfo.getMesgNetworkPriority())) {
			if (nothingPrinted) {
				result += printBanner(banner);
				nothingPrinted = false;
			}
			result += printText(mesgTransmissionInfoText, false);
			String item1 = StringUtils.substring(instanceTransmissionPrintInfo.getMesgNetworkPriority(), StringUtils.indexOf(instanceTransmissionPrintInfo.getMesgNetworkPriority(), "_") + 1);
			result += positionText(" : ", eyeCatcher + "Correspondent Input Reference", mesgTransmissionInfoText);
			mesgTransmissionInfoText = StringUtils.capitalize(StringUtils.lowerCase(item1));
			result += printText(mesgTransmissionInfoText, true);
		}

		if (bInputMsg && !StringUtils.isEmpty(mesgInputReference)) {
			if (nothingPrinted) {
				result += printBanner(banner);
				nothingPrinted = false;
			}
			mesgTransmissionInfoText = eyeCatcher + "Message Input Reference";
			result += printText(mesgTransmissionInfoText, false);
			result += positionText(" : ", eyeCatcher + "Correspondent Input Reference", mesgTransmissionInfoText);
			result += printText(mesgInputReference, true);
		} else {
			if (!StringUtils.isEmpty(mesgOutputReference)) {
				if (nothingPrinted) {
					result += printBanner(banner);
					nothingPrinted = false;
				}
				mesgTransmissionInfoText = eyeCatcher + "Message Output Reference";
				result += printText(mesgTransmissionInfoText, false);
				result += positionText(" : ", eyeCatcher + "Correspondent Input Reference", mesgTransmissionInfoText);
				result += printText(mesgOutputReference, true);
			}
			if (!StringUtils.isEmpty(mesgInputReference)) {
				if (nothingPrinted) {
					result += printBanner(banner);
					nothingPrinted = false;
				}
				mesgTransmissionInfoText = eyeCatcher + "Correspondent Input Reference";
				result += printText(mesgTransmissionInfoText, false);
				result += positionText(" : ", eyeCatcher + "Correspondent Input Reference", mesgTransmissionInfoText);
				result += printText(mesgInputReference, true);
			}
		}
		return result;
	}

	private String printBanner(String banner) {
		String result = "";
		if ((!printToFile) && (!isMail)) {
			// result+="<b>";
		}
		result += printText(banner, true);
		if ((!printToFile) && (!isMail)) {
			// result+="</b>";
		}
		return result;
	}

	private String positionText(String strVal, String toPos, String fromPos) {
		String tmpStr = "";
		if (printToFile || isMail) {
			int offset = StringUtils.defaultString(toPos).length() - StringUtils.defaultString(fromPos).length();
			if (offset > 0) {
				// TempString = Space(Len(ToPos) - Len(FromPos)) & What
				tmpStr = StringUtils.leftPad("", offset, " ") + strVal;
			} else {
				tmpStr = strVal;
			}
		} else {
			// Printer.CurrentX = Printer.TextWidth(ToPos)
			tmpStr = strVal;
		}
		return printText(tmpStr, false);

	}

	private String printText(String mesgText, boolean withNewLine) {
		String result = "";
		if (printToFile) {
			if (withNewLine) {
				result += mesgText + "\r\n";
			} else {
				result += mesgText;
			}
		} else {
			if (isMail) {
				result += mesgText;
				if (withNewLine) {
					result += "\r\n";
				}
			} else {
				result += mesgText;
				if (withNewLine) {
					result += "\r\n";
				}
			}
		}
		return result;
	}

	private String printHdrDetails() {
		String result = "";
		String header;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat + " " + timeFormat);

		header = "";
		if (printToFile || isMail) {
			result += "_      ____________________________________________________________________      _";
		}
		pageNo++;
		header = pageNo + "";
		if (!printToFile && !isMail) {
			/*
			 * // - Printer.TextWidth("W") because printing on the last print position caused problems Printer.CurrentX = Printer.ScaleWidth - Printer.TextWidth(Hdr) - Printer.TextWidth("WW")
			 * Printer.Print Hdr
			 */
		}
		return result;
	}

	List<String> breakLongLine(String longLine, int maxLineLength) {
		List<String> lines = new ArrayList<String>();
		if (StringUtils.isEmpty(longLine)) {
			lines.add("");
			return lines;
		}

		while (longLine.length() > maxLineLength) {
			// find the index of the last space that is near the maxLineLength
			int spaceIndex = longLine.indexOf(32);
			int lastSpaceIndex = spaceIndex;
			while (spaceIndex >= 0 && spaceIndex < maxLineLength) {// keep going through spaces till you find the last space before the
				lastSpaceIndex = spaceIndex;
				spaceIndex = longLine.indexOf(32, spaceIndex + 1);
			}

			int indexToCutON = maxLineLength;
			if (lastSpaceIndex < maxLineLength && lastSpaceIndex > 0) { // if last space was found in the valid range specified for the line size, then use it to cut the string
				indexToCutON = lastSpaceIndex;
			}

			String line = StringUtils.left(longLine, indexToCutON);
			lines.add(line/* .trim() */ );
			longLine = StringUtils.substring(longLine, indexToCutON);
			longLine = longLine.trim();// make sure to remove spaces at the beginning of the line
		}
		if (!longLine.isEmpty()) {
			lines.add(longLine);
		}
		return lines;
	}

	String formatCase(String str) {
		return StringUtils.capitalize(StringUtils.lowerCase(str));
	}

	public XMLReaderDAO getXmlReaderDAO() {
		return xmlReaderDAO;
	}

	public void setXmlReaderDAO(XMLReaderDAO xmlReaderDAO) {
		this.xmlReaderDAO = xmlReaderDAO;
	}

	public Boolean getShowMessageText() {
		return showMessageText;
	}

	public void setShowMessageText(Boolean showMessageText) {
		this.showMessageText = showMessageText;
	}

}
