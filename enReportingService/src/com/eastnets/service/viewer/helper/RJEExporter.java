package com.eastnets.service.viewer.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.domain.relatedMessage.RelatedMessage;
import com.eastnets.domain.viewer.AppendixJREDetails;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.service.ServiceLocator;
import com.eastnets.utils.ApplicationUtils;

public class RJEExporter {
	private ViewerDAO viewerDAO;

	private ServiceLocator serviceLocator = new ServiceLocator();

	public RJEExporter(ViewerDAO viewerDAO) {
		setViewerDAO(viewerDAO);
	}

	public ViewerDAO getViewerDAO() {
		return viewerDAO;
	}

	public void setViewerDAO(ViewerDAO viewerDAO) {
		this.viewerDAO = viewerDAO;
	}

	public String generateRJE(List<SearchResultEntity> messages, boolean addPDEFlag, int timeZoneOffset, String loggedInUser, boolean showBolckFour) throws InterruptedException {

		String result = "";

		boolean isFirstMessage = true;

		for (SearchResultEntity message : messages) {

			if (Thread.interrupted()) {
				throw new InterruptedException();
			}

			try {

				MessageDetails messageDetails = viewerDAO.getMessageDetails(message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB(), timeZoneOffset, loggedInUser);

				if (StringUtils.equalsIgnoreCase(messageDetails.getMesgNetworkApplInd(), "FIN")) {
					result += buildRJE(messageDetails, addPDEFlag, !isFirstMessage, showBolckFour, true, false);
					isFirstMessage = false;
				} else {
					System.out.println(messageDetails.getMesgNetworkApplInd() + " messages are not supported in RJE.");
				}
			} catch (Exception ex) {
				System.out.println("Mail Message error : " + ex.getMessage());
			}

		}

		return result;
	}

	public List<String> generateJsonRJE(List<MessageDetails> messages, boolean addPDEFlag, int timeZoneOffset, String loggedInUser, boolean showBolckFour, boolean includeHistory) throws InterruptedException {

		List<String> results = new ArrayList<>();

		boolean isFirstMessage = true;

		for (MessageDetails message : messages) {

			if (Thread.interrupted()) {
				throw new InterruptedException();
			}

			try {

				if (StringUtils.equalsIgnoreCase(message.getMesgNetworkApplInd(), "FIN")) {
					results.add(buildRJE(message, addPDEFlag, !isFirstMessage, showBolckFour, false, includeHistory));
					isFirstMessage = false;
				} else {
					System.out.println(message.getMesgNetworkApplInd() + " messages are not supported in RJE.");
				}
			} catch (Exception ex) {
				System.out.println("Mail Message error : " + ex.getMessage());
			}
		}

		return results;
	}

	public String generateRJEForRelated(String loggedInUser, RelatedMessage messages, boolean addPDEFlag, int timeZoneOffset, boolean showBolckFour) throws InterruptedException {

		String result = "";

		boolean isFirstMessage = true;

		boolean addNewLine = false;

		if (addNewLine) {
			result += "\r\n";
		}
		try {
			// there are lots of fields in the message details are not set by viewerDAO.getMessageDetails like the text and the history, but they are not required here
			// for more about what is missing see ViewerServiceImp.getMessageDetails()
			MessageDetails messageDetails = viewerDAO.getMessageDetails(messages.getAid(), messages.getMesg_s_umidl(), messages.getMesg_s_umidh(), messages.getCreationDate(), timeZoneOffset, loggedInUser);

			if (StringUtils.equalsIgnoreCase(messageDetails.getMesgNetworkApplInd(), "FIN")) {
				result += buildRJE(messageDetails, addPDEFlag, !isFirstMessage, showBolckFour, true, false);
				isFirstMessage = false;
			} else {
				System.out.println(messageDetails.getMesgNetworkApplInd() + " messages are not supported in RJE.");
			}
		} catch (Exception ex) {
			System.out.println("Mail Message error : " + ex.getMessage());
		}

		addNewLine = true;

		return result;
	}

	private String buildRJE(MessageDetails mesg, boolean pdeFlag, boolean notFirstMessage, boolean showBolckFour, boolean addDollarSign, boolean includeHistory) {
		String mesgType, subFormat, sender, receiver, sequenceNbr, sessionNbr;

		String block1, block2, block3, block4 = "", block5, history = "";

		String apperemoteinputtime = "", appelocaloutputtime = "", apperemoteinputreference = "";

		mesgType = StringUtils.defaultString(mesg.getMesgType(), "XXX");
		subFormat = formatCase(mesg.getMesgSubFormat());
		receiver = StringUtils.defaultString(mesg.getMesgReceiverSwiftAddress(), "XXXXXXXXXXXX");
		sender = mesg.getMesgSenderSwiftAddress();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");

		AppendixJREDetails appeDetails = viewerDAO.getAppendixJREDetails(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), "Input".equalsIgnoreCase(subFormat));

		if (appeDetails != null) {
			sequenceNbr = String.format("%06d", appeDetails.getAppeSequenceNbr());
			sessionNbr = String.format("%04d", appeDetails.getAppeSessionNbr());
			apperemoteinputreference = StringUtils.defaultString(appeDetails.getAppeRemoteInputReference(), "");

			if (appeDetails.getAppeLocalOutputTime() == null) {
				appelocaloutputtime = "";
			} else {

				appelocaloutputtime = sdf.format(appeDetails.getAppeLocalOutputTime());
			}

			if (appeDetails.getAppeRemoteInputTime() == null) {
				apperemoteinputtime = "";
			} else {
				apperemoteinputtime = sdf.format(appeDetails.getAppeRemoteInputTime());
			}
		} else {
			sequenceNbr = "000000";
			sessionNbr = "0000";
		}

		// ------------- BLOCK 1
		block1 = getBlock1(mesgType, subFormat, sender, receiver, sequenceNbr, sessionNbr);

		// ------------- BLOCK 2
		block2 = getBlock2(mesg, mesgType, subFormat, sender, receiver, apperemoteinputreference, appelocaloutputtime, apperemoteinputtime);

		// ------------- BLOCK 3
		block3 = getBlock3(mesg);

		// ------------- BLOCK 4

		if (showBolckFour) {
			block4 = getBlock4(mesg);
		}

		// ------------- BLOCK 5
		block5 = getBlock5(mesg, subFormat, pdeFlag);

		// ------------- History
		if (includeHistory) {
			history = getHistory(mesg);
		}

		if (notFirstMessage) {
			return ((addDollarSign) ? "$" + block1 + block2 + block3 + block4 + block5 + history : "" + block1 + block2 + block3 + block4 + block5 + history);
		}
		return block1 + block2 + block3 + block4 + block5 + history;
	}

	private String getHistory(MessageDetails mesg) {

		String history = "";
		history = StringUtils.defaultString(mesg.getMesgHistory(), "");

		if (!StringUtils.isEmpty(history)) {
			history = "{History:" + history + "}";
		}
		return history;
	}

	String formatCase(String str) {
		return StringUtils.capitalize(StringUtils.lowerCase(str));
	}

	private String getBlock1(String mesgType, String subFormat, String sender, String receiver, String sequenceNbr, String sessionNbr) {
		String block1 = "{1:F";
		if (ViewerServiceUtils.IsSystemMessageServiceIdinfier(mesgType)) {
			Integer i = Integer.parseInt(mesgType);
			block1 = block1 + String.format("%02d", i);
		} else {
			block1 = block1 + "01";
		}
		if ("Input".equalsIgnoreCase(subFormat)) {
			block1 = block1 + sender;
		} else {
			block1 = block1 + receiver;
		}
		block1 = block1 + sessionNbr;
		block1 = block1 + sequenceNbr;

		block1 = block1 + "}";
		return block1;
	}

	private String getBlock2(MessageDetails mesg, String mesgType, String subFormat, String sender, String receiver, String apperemoteinputreference, String appelocaloutputtime, String apperemoteinputtime) {
		String mesgnetworkpriority = StringUtils.defaultString(mesg.getMesgNetworkPriority(), "-1");
		String mesgnetworkdelvnotifreq = StringUtils.defaultString(mesg.getMesgNetworkDelvNotifReq(), "0");
		String mesgdelvoverduewarnreq = StringUtils.defaultString(mesg.getMesgDelvOverdueWarnReq(), "0");
		String mesgnetworkobsoperiod = "0";
		if (mesg.getMesgNetworkObsoPeriod() != null) {
			mesgnetworkobsoperiod = "" + mesg.getMesgNetworkObsoPeriod();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		String mesgcreadatetime = sdf.format(mesg.getMesgCreaDateTime());

		String block2 = "";
		if ("Input".equalsIgnoreCase(subFormat)) {
			String deliveryMonitoring;
			String obsolescence;

			// --> INPUT
			if (mesgnetworkdelvnotifreq.equals("0") && mesgdelvoverduewarnreq.equals("1")) {
				deliveryMonitoring = "1";
			} else if (mesgnetworkdelvnotifreq.equals("1") && mesgdelvoverduewarnreq.equals("0")) {
				deliveryMonitoring = "2";
			} else if (mesgnetworkdelvnotifreq.equals("1") && mesgdelvoverduewarnreq.equals("1")) {
				deliveryMonitoring = "3";
			} else {
				deliveryMonitoring = "";
			}

			if (mesgnetworkobsoperiod.equals("15")) {
				obsolescence = deliveryMonitoring + "003";
			} else if (mesgnetworkobsoperiod.equals("100")) {
				obsolescence = deliveryMonitoring + "020";
			} else {
				obsolescence = deliveryMonitoring;
			}
			String str = StringUtils.trim(mesgnetworkpriority);
			if ("PRI_SYSTEM".equalsIgnoreCase(str)) {
				block2 = "{2:I" + mesgType + receiver + "S" + obsolescence + "}";
			} else if ("PRI_URGENT".equalsIgnoreCase(str)) {
				block2 = "{2:I" + mesgType + receiver + "U" + obsolescence + "}";
			} else if ("PRI_NORMAL".equalsIgnoreCase(str)) {
				block2 = "{2:I" + mesgType + receiver + "N" + obsolescence + "}";
			} else {
				block2 = "{2:I" + mesgType + receiver + "N}";
			}
		} else {
			// --> OUTPUT
			// output block 2 {2:O2020843040309AGRIFRPPXXXX00040000240403090843N}

			// if we have no swift reception appendix, try to supply some info
			// all date time is the one from mesgcreadatetime
			// session & sequence are lost
			// bic is the one of the header
			if (StringUtils.isEmpty(apperemoteinputreference)) {
				apperemoteinputreference = StringUtils.left(mesgcreadatetime, 6) + sender + "0000000000";
				apperemoteinputtime = mesgcreadatetime;
				appelocaloutputtime = mesgcreadatetime;
			}
			String trimmed = StringUtils.trim(mesgnetworkpriority);
			if ("PRI_SYSTEM".equalsIgnoreCase(trimmed)) {
				block2 = "{2:O" + mesgType + StringUtils.right(apperemoteinputtime, 4) + apperemoteinputreference + appelocaloutputtime + "S}";
			} else if ("PRI_URGENT".equalsIgnoreCase(trimmed)) {
				block2 = "{2:O" + mesgType + StringUtils.right(apperemoteinputtime, 4) + apperemoteinputreference + appelocaloutputtime + "U}";
			} else if ("PRI_NORMAL".equalsIgnoreCase(trimmed)) {
				block2 = "{2:O" + mesgType + StringUtils.right(apperemoteinputtime, 4) + apperemoteinputreference + appelocaloutputtime + "N}";
			} else {
				block2 = "{2:O" + mesgType + StringUtils.right(apperemoteinputtime, 4) + apperemoteinputreference + appelocaloutputtime + "N}";
			}

			if ("X".equalsIgnoreCase(StringUtils.mid(block2, 26, 27))) {
				String b2 = StringUtils.substring(block2, 0, 26);
				b2 += "A";
				b2 += StringUtils.substring(block2, 27);
				block2 = b2;
			}

		}
		return block2;
	}

	private String getBlock3(MessageDetails mesg) {
		String block3 = "{3:";

		boolean hasBlock3 = false;
		mesg.getSlaId();
		if (!StringUtils.isEmpty(mesg.getMesgCopyServiceId())) {
			block3 = block3 + "{103:" + mesg.getMesgCopyServiceId() + "}";
			hasBlock3 = true;
		}

		if (!StringUtils.isEmpty(mesg.getMesgUserPriorityCode())) {
			block3 = block3 + "{113:" + mesg.getMesgUserPriorityCode() + "}";
			hasBlock3 = true;
		}

		if (!StringUtils.isEmpty(mesg.getMesgUserReferenceText())) {
			block3 = block3 + "{108:" + mesg.getMesgUserReferenceText() + "}";
			hasBlock3 = true;
		}

		if (!StringUtils.isEmpty(mesg.getMesgMesgUserGroup())) {
			block3 = block3 + "{119:" + mesg.getMesgMesgUserGroup() + "}";
			hasBlock3 = true;
		}
		if (!StringUtils.isEmpty(mesg.getSlaId())) {
			block3 = block3 + "{111:" + mesg.getSlaId() + "}";
			hasBlock3 = true;
		}
		if (!StringUtils.isEmpty(mesg.getUetr())) {
			block3 = block3 + "{121:" + mesg.getUetr() + "}";
			hasBlock3 = true;
		}
		// 55250:en.TDR, export RJE - tags in block 3 are in wrong order
		if (!StringUtils.isEmpty(mesg.getMesgReleaseInfo())) {
			block3 = block3 + "{115:" + mesg.getMesgReleaseInfo() + "}";
			hasBlock3 = true;
		}

		block3 = block3 + "}";

		if (!hasBlock3) {
			block3 = "";
		}
		return block3;
	}

	private String getBlock4(MessageDetails mesg) {
		String block4 = "";

		String filedOption;
		String filedValue;
		String filed;

		String textdatablock = StringUtils.defaultString(ApplicationUtils.convertClob2String(mesg.getTextDataBlock()), "");
		if (!StringUtils.isEmpty(textdatablock)) {
			// Try to get it from the rText table (SIDE Express)
			// -------------------------------------------------
			// Get full text
			block4 = "{4:" + ApplicationUtils.convertClob2String(mesg.getTextDataBlock()) + "\r\n-}";
		} else {
			// Try to get it from the rTextField table
			// ---------------------------------------
			List<TextFieldData> textFieldDataList = null;
			if (!ViewerServiceUtils.IsSystemMessage(mesg.getMesgType())) {
				textFieldDataList = viewerDAO.getTextFieldData(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), mesg.getTimeZoneOffset());
			} else {
				textFieldDataList = viewerDAO.getSystemTextFieldData(mesg.getAid(), mesg.getMesgUmidl(), mesg.getMesgUmidh(), mesg.getMesgCreaDateTimeOnDB(), mesg.getTimeZoneOffset());
			}
			block4 = "{4:\r\n";

			for (TextFieldData textField : textFieldDataList) {
				filedOption = StringUtils.defaultString(textField.getFieldOption(), "");
				if (StringUtils.isEmpty(textField.getValue())) {
					if (textField.getValueMemo() == null) {
						filedValue = "\r\n";
					} else {
						filedValue = ApplicationUtils.convertClob2String(textField.getValueMemo()) + "\r\n";
					}
				} else {
					filedValue = textField.getValue() + "\r\n";
				}

				switch (textField.getFieldCode()) {
				case 0:
					filed = "";
					break;
				case 255:
					filed = "";
					break;
				default:
					filed = ":" + textField.getFieldCode() + filedOption + ":";
					break;
				}

				block4 = block4 + filed + filedValue;
			}

			block4 = block4 + "-}";
		}

		return block4;
	}

	private String getBlock5(MessageDetails mesg, String subFormat, boolean pdeFlag) {
		String block5 = "";

		String textswiftblock5;
		String mesgpossibledupcreation;
		String mesguserissuedaspde;

		textswiftblock5 = StringUtils.defaultString(mesg.getTextSwiftBlock5(), "");
		mesgpossibledupcreation = StringUtils.defaultString(mesg.getMesgPossibleDupCreation(), "");
		mesguserissuedaspde = "";
		if (mesg.getMesgUserIssuedAsPde() != null) {
			mesguserissuedaspde = mesg.getMesgUserIssuedAsPde();
		}

		if (StringUtils.equalsIgnoreCase(subFormat, "Output") && !StringUtils.isEmpty(textswiftblock5)) {
			block5 = "{5:" + textswiftblock5 + "}";
		} else {
			if ("PDE".equalsIgnoreCase(mesgpossibledupcreation) || "PDR".equalsIgnoreCase(mesgpossibledupcreation) || !StringUtils.isEmpty(mesguserissuedaspde) || pdeFlag == true) {
				block5 = "{5:" + textswiftblock5 + "{PDE:}}";
				block5 = StringUtils.replace(block5, "{PDE:}{PDE:}", "{PDE:}");

			} else {
				if (!StringUtils.isEmpty(textswiftblock5)) {
					block5 = "{5:" + textswiftblock5 + "}";
				}
			}
		}
		return block5;
	}

}
