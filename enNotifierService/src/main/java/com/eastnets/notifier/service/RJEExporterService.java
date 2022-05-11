
package com.eastnets.notifier.service;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.eastnets.notifier.domain.Message;
import com.eastnets.notifier.domain.PrimaryKey;

/**
 * 
 * @author AHammad
 *
 */
@Service
public class RJEExporterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RJEExporterService.class);

	public String buildRJE(Message mesg, PrimaryKey primaryKey) {

		String mesgType;
		String subFormat;
		String sender;
		String receiver;
		String sequenceNbr;
		String sessionNbr;

		String block1;
		String block2;
		String block3;
		String block4 = "";
		String block5;

		String apperemoteinputreference = "";
		String appelocaloutputtime = "";
		String apperemoteinputtime = "";

		mesgType = StringUtils.defaultString(mesg.getMessageType(), "XXX");
		subFormat = formatCase(mesg.getDirection());
		receiver = StringUtils.defaultString(mesg.getMesgReceiverSwiftAddress(), "XXXXXXXXXXXX");
		sender = mesg.getMesgSenderSwiftAddress();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		LOGGER.debug("check appendix data");
		if (mesg.getAppeSequenceNbr() != null) {
			sequenceNbr = String.format("%06d", mesg.getAppeSequenceNbr());
			sessionNbr = String.format("%04d", mesg.getAppeSessionNbr());
			apperemoteinputreference = StringUtils.defaultString(mesg.getAppeRemoteInputReference(), "");

			if (mesg.getAppeLocalOutputTime() == null) {
				appelocaloutputtime = "";
			} else {

				appelocaloutputtime = sdf.format(mesg.getAppeLocalOutputTime());
			}

			if (mesg.getAppeRemoteInputTime() == null) {
				apperemoteinputtime = "";
			} else {
				apperemoteinputtime = sdf.format(mesg.getAppeRemoteInputTime());
			}
		} else {
			LOGGER.debug("there is no appendix for message " + primaryKey);
			sequenceNbr = "000000";
			sessionNbr = "0000";
		}

		// ------------- BLOCK 1
		LOGGER.debug("start building first Block for message type " + mesgType + " and message Id is " + primaryKey);
		block1 = getBlock1(mesgType, subFormat, sender, receiver, sequenceNbr, sessionNbr);

		// ------------- BLOCK 2
		LOGGER.debug("start building second Block for message type " + mesgType + " and message Id is " + primaryKey);
		block2 = getBlock2(mesg, mesgType, subFormat, sender, receiver, apperemoteinputreference, appelocaloutputtime, apperemoteinputtime);

		// ------------- BLOCK 3
		LOGGER.debug("start building third Block for message type " + mesgType + " and message Id is " + primaryKey);
		block3 = getBlock3(mesg);

		// ------------- BLOCK 4
		LOGGER.debug("start building fourth Block for message type " + mesgType + " and message Id is " + primaryKey);
		block4 = getBlock4(mesg);

		// ------------- BLOCK 5
		LOGGER.debug("start building fifth Block for message type " + mesgType + " and message Id is " + primaryKey);
		block5 = getBlock5(mesg, subFormat);
		LOGGER.debug("finish building RJE message for id " + primaryKey);
		return block1 + block2 + block3 + block4 + block5;
	}

	private String getBlock1(String mesgType, String subFormat, String sender, String receiver, String sequenceNbr, String sessionNbr) {
		String block1 = "{1:F";
		if (IsSystemMessageServiceIdinfier(mesgType)) {
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

	private String getBlock2(Message mesg, String mesgType, String subFormat, String sender, String receiver, String apperemoteinputreference, String appelocaloutputtime, String apperemoteinputtime) {
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
			// output block 2
			// {2:O2020843040309AGRIFRPPXXXX00040000240403090843N}

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

	private String getBlock3(Message mesg) {
		String block3 = "{3:";

		boolean hasBlock3 = false;
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

		if (!StringUtils.isEmpty(mesg.getMesgReleaseInfo())) {
			block3 = block3 + "{115:" + mesg.getMesgReleaseInfo() + "}";
			hasBlock3 = true;
		}

		if (!StringUtils.isEmpty(mesg.getMesgUserGroup())) {
			block3 = block3 + "{119:" + mesg.getMesgUserGroup() + "}";
			hasBlock3 = true;
		}
		if (!StringUtils.isEmpty(mesg.getServiceTypeIdentifier())) {
			block3 = block3 + "{111:" + mesg.getServiceTypeIdentifier() + "}";
			hasBlock3 = true;
		}
		if (!StringUtils.isEmpty(mesg.getUterReferenceNumber())) {
			block3 = block3 + "{121:" + mesg.getUterReferenceNumber() + "}";
			hasBlock3 = true;
		}

		block3 = block3 + "}";

		if (!hasBlock3) {
			block3 = "";
		}
		return block3;
	}

	private String getBlock4(Message mesg) {
		String block4 = "";

		// String filedOption;
		// String filedValue;
		// String filed;

		String textdatablock = mesg.getText().getFullText(); // StringUtils.defaultString(
																// ApplicationUtils.convertClob2String(
																// mesg.getText().TextDataBlock()
																// ), "");
		// if(!StringUtils.isEmpty(textdatablock )){
		// Try to get it from the rText table (SIDE Express)
		// -------------------------------------------------
		// Get full text
		block4 = "{4:" + textdatablock + "\r\n-}";
		// } else {
		// // Try to get it from the rTextField table
		// // ---------------------------------------
		// List<TextFieldData> textFieldDataList = null;
		// if(!ViewerServiceUtils.IsSystemMessage(mesg.getMesgType())) {
		// textFieldDataList= viewerDAO.getTextFieldData(mesg.getAid(),
		// mesg.getMesgUmidl(), mesg.getMesgUmidh(),
		// mesg.getMesgCreaDateTimeOnDB(),mesg.getTimeZoneOffset());
		// } else {
		// textFieldDataList= viewerDAO.getSystemTextFieldData( mesg.getAid(),
		// mesg.getMesgUmidl(), mesg.getMesgUmidh(),
		// mesg.getMesgCreaDateTimeOnDB(),mesg.getTimeZoneOffset());
		// }
		// block4 = "{4:\r\n";
		//
		// for (TextFieldData textField : textFieldDataList ){
		// filedOption = StringUtils.defaultString( textField.getFieldOption(),
		// "");
		// if(StringUtils.isEmpty(textField.getValue())){
		// if(textField.getValueMemo()==null){
		// filedValue="\r\n";
		// }else{
		// filedValue = ApplicationUtils.convertClob2String(
		// textField.getValueMemo() ) + "\r\n";
		// }
		// }
		// else{
		// filedValue = textField.getValue() + "\r\n";
		// }
		//
		// switch (textField.getFieldCode()){
		// case 0:
		// filed = "";
		// break;
		// case 255:
		// filed = "";
		// break;
		// default:
		// filed = ":" + textField.getFieldCode() + filedOption + ":";
		// break;
		// }
		//
		// block4 = block4 + filed + filedValue;
		// }
		//
		// block4 = block4 + "-}";
		// }
		return block4;
	}

	private String getBlock5(Message mesg, String subFormat) {
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
			// if ("PDE".equalsIgnoreCase(mesgpossibledupcreation) ||
			// "PDR".equalsIgnoreCase(mesgpossibledupcreation)
			// || !StringUtils.isEmpty(mesguserissuedaspde) || pdeFlag == true)
			// {
			// block5 = "{5:" + textswiftblock5 + "{PDE:}}";
			// block5 = StringUtils.replace(block5, "{PDE:}{PDE:}", "{PDE:}");
			//
			// } else {
			if (!StringUtils.isEmpty(textswiftblock5)) {
				block5 = "{5:" + textswiftblock5 + "}";
				// }
			}
		}
		return block5;
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

	String formatCase(String str) {
		return StringUtils.capitalize(StringUtils.lowerCase(str));
	}

}
