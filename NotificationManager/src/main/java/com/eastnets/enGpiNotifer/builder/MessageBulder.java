package com.eastnets.enGpiNotifer.builder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.domain.viewer.TextFieldData;
import com.eastnets.enGpiNotifer.utility.DataSourceParser;
import com.eastnets.enGpiNotifer.utility.GpiHelper;
import com.eastnets.enGpiNotifer.utility.Queue;
import com.eastnets.service.viewer.helper.ViewerServiceUtils;
import com.eastnets.utils.ApplicationUtils;

/**
 * @author MKassab 2020
 * 
 */
public class MessageBulder {

	private DataSourceParser dataSourceParser;

	public String buildRJE(NotifierMessage message, MessageDetails mesg, boolean pdeFlag, boolean notFirstMessage, Map<String, Queue> QueuesMap, DataSourceParser dataSourceParser) {
		String mesgType;
		String subFormat;
		String sender;
		String receiver;
		String sequenceNbr;
		String sessionNbr;

		String block1;
		String block2;
		String block3;
		String block4;
		String block5;

		this.dataSourceParser = dataSourceParser;
		String apperemoteinputreference = "";
		String appelocaloutputtime = "";
		String apperemoteinputtime = "";
		mesgType = "199";
		subFormat = formatCase(mesg.getMesgSubFormat());
		receiver = StringUtils.defaultString(mesg.getMesgReceiverSwiftAddress(), "XXXXXXXXXXXX");
		sender = mesg.getMesgSenderSwiftAddress();

		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		sequenceNbr = "000000";
		sessionNbr = "0000";

		// ------------- BLOCK 1
		block1 = getBlock1(mesgType, subFormat, sender, receiver, sequenceNbr, sessionNbr);

		// ------------- BLOCK 2
		block2 = getBlock2(mesg, mesgType, subFormat, sender, receiver, apperemoteinputreference, appelocaloutputtime, apperemoteinputtime);

		// ------------- BLOCK 3
		block3 = getBlock3(mesg);

		// ------------- BLOCK 4
		block4 = getBlock4(mesg, message, QueuesMap);

		// ------------- BLOCK 5

		// block5 = getBlock5(mesg, subFormat, pdeFlag );

		if (notFirstMessage) {
			return "$" + block1 + block2 + block3 + block4;// + block5;
		}
		return block1 + block2 + block3 + block4;// + block5;
	}

	String formatCase(String str) {
		return StringUtils.capitalize(StringUtils.lowerCase(str));
	}

	private String getBlock1(String mesgType, String subFormat, String sender, String receiver, String sequenceNbr, String sessionNbr) {

		if (dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic() == null || dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic().isEmpty()) {
			// doNothing
		} else {
			receiver = dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic();
			sender = dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic();
		}

		String block1 = "{1:F";
		if (ViewerServiceUtils.IsSystemMessage(mesgType)) {
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
		/*
		 * String mesgnetworkpriority = StringUtils.defaultString(mesg.getMesgNetworkPriority(), "-1"); String mesgnetworkdelvnotifreq = StringUtils.defaultString(mesg.getMesgNetworkDelvNotifReq(),
		 * "0"); String mesgdelvoverduewarnreq = StringUtils.defaultString(mesg.getMesgDelvOverdueWarnReq(), "0"); String mesgnetworkobsoperiod = "0"; if (mesg.getMesgNetworkObsoPeriod() != null) {
		 * mesgnetworkobsoperiod = "" + mesg.getMesgNetworkObsoPeriod(); } SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm"); String mesgcreadatetime = sdf.format(mesg.getMesgCreaDateTime());
		 * 
		 * String block2 = ""; String deliveryMonitoring; String obsolescence;
		 * 
		 * // --> INPUT if (mesgnetworkdelvnotifreq.equals("0") && mesgdelvoverduewarnreq.equals("1")) { deliveryMonitoring = "1"; } else if (mesgnetworkdelvnotifreq.equals("1") &&
		 * mesgdelvoverduewarnreq.equals("0")) { deliveryMonitoring = "2"; } else if (mesgnetworkdelvnotifreq.equals("1") && mesgdelvoverduewarnreq.equals("1")) { deliveryMonitoring = "3"; } else {
		 * deliveryMonitoring = ""; }
		 * 
		 * if (mesgnetworkobsoperiod.equals("15")) { obsolescence = deliveryMonitoring + "003"; } else if (mesgnetworkobsoperiod.equals("100")) { obsolescence = deliveryMonitoring + "020"; } else {
		 * obsolescence = deliveryMonitoring; } String str = StringUtils.trim(mesgnetworkpriority); if ("PRISYSTEM".equalsIgnoreCase(str)) { block2 = "{2:I" + mesgType + receiver + "S" + obsolescence
		 * + "}"; } else if ("PRIURGENT".equalsIgnoreCase(str)) { block2 = "{2:I" + mesgType + receiver + "U" + obsolescence + "}"; } else if ("PRINORMAL".equalsIgnoreCase(str)) { block2 = "{2:I" +
		 * mesgType + receiver + "N" + obsolescence + "}"; } else { block2 = "{2:I" + mesgType + receiver + "N}"; } return block2;
		 */
		String block2 = "";
		if (dataSourceParser.getxMLNotifierConfigHelper().getEnvironmentType().equalsIgnoreCase("Test")) {
			if (mesg.getMesgType().contains("103")) {
				block2 = "{2:I" + "199TRCKCHZ0XXXXN" + "}";
			} else if (mesg.getMesgType().contains("202") || mesg.getMesgType().contains("205")) {
				block2 = "{2:I" + "299TRCKCHZ0XXXXN" + "}";
			}
		} else {
			if (mesg.getMesgType().contains("103")) {
				block2 = "{2:I" + "199TRCKCHZZXXXXN" + "}";
			} else if (mesg.getMesgType().contains("202") || mesg.getMesgType().contains("205")) {
				block2 = "{2:I" + "299TRCKCHZZXXXXN" + "}";
			}
		}
		return block2;
	}

	private String getBlock3(MessageDetails mesg) {
		String block3 = "{3:";

		boolean hasBlock3 = false;
		mesg.getSlaId();

		if (!dataSourceParser.getxMLNotifierConfigHelper().isEnabelUC()) {
			block3 = block3 + "{111:" + "001" + "}";
			hasBlock3 = true;
		}

		if (!StringUtils.isEmpty(mesg.getUetr())) {
			block3 = block3 + "{121:" + mesg.getUetr() + "}";
			hasBlock3 = true;
		}

		block3 = block3 + "}";

		if (!hasBlock3) {
			block3 = "";
		}
		return block3;
	}

	private List<TextFieldData> getTextFieldData(MessageDetails mesg, NotifierMessage message, Map<String, Queue> QueuesMap) {
		List<TextFieldData> textFieldList = new ArrayList<TextFieldData>();
		String rejex = dataSourceParser.getxMLNotifierConfigHelper().getRejex();
		TextFieldData textFieldDataRef = new TextFieldData().new TextFieldDataBulder(20, "", GpiHelper.generetTrnRef(rejex), null).build();
		textFieldList.add(textFieldDataRef);
		TextFieldData textFieldDataRelRef = new TextFieldData().new TextFieldDataBulder(21, "", mesg.getMesgTrnRef(), null).build();
		textFieldList.add(textFieldDataRelRef);
		TextFieldData textFieldDataConfirmation = new TextFieldData().new TextFieldDataBulder(79, "", buildConfirmationField(message, QueuesMap, mesg), null).build();
		textFieldList.add(textFieldDataConfirmation);
		return textFieldList;
	}

	private String buildConfirmationField(NotifierMessage message, Map<String, Queue> QueuesMap, MessageDetails mesg) {
		String confirmation = "";
		String statusOrginatoLine = "";

		// Line 1
		String dataTimeLine = "//" + new SimpleDateFormat("yyMMddHHmmZ").format(new Date());

		/////////////////////////////////////////////////////////

		// Line 2
		String statusCode = "";
		if ((message.getDataSource().equals(DataSource.SAA))) {
			if ((QueuesMap.get(message.getQueueName()).getStatusCode() == null || QueuesMap.get(message.getQueueName()).getStatusCode().isEmpty())) {
				statusCode = "ACSP";
			} else {
				statusCode = QueuesMap.get(message.getQueueName()).getStatusCode();
			}
		} else if ((message.getDataSource().equals(DataSource.MQ))) {
			if ((message.getStatusCode() == null || message.getStatusCode().isEmpty())) {
				statusCode = "ACSP";
			} else {
				statusCode = message.getStatusCode();
			}
		} else if ((message.getDataSource().equals(DataSource.EXT_DB))) {
			if ((message.getStatusCode() == null || message.getStatusCode().isEmpty())) {
				statusCode = "ACSP";
			} else {
				statusCode = message.getStatusCode();
			}
		}

		String reasonCode = "";
		if (statusCode.equalsIgnoreCase("ACSP") || statusCode.equalsIgnoreCase("RJCT")) {
			reasonCode = "/" + ((message.getDataSource().equals(DataSource.SAA)) ? QueuesMap.get(message.getQueueName()).getReasonCode() : message.getReasonCode());
		}

		String statusCodeLine = "//" + statusCode + reasonCode;

		////////////////////////////////////////////////////////

		// Line 3
		if (mesg.getMesgSubFormat().equalsIgnoreCase("Input")) {
			statusOrginatoLine = "//" + mesg.getMesgSenderX1();
		} else {
			statusOrginatoLine = "//" + mesg.getInstReceiverX1();
		}

		String sattlmentMethod = (message.getDataSource().equals(DataSource.SAA)) ? QueuesMap.get(message.getQueueName()).getSettlementMethod() : message.getSettlementMethod();
		String cleringSystem = (mesg.getMesgCopyServiceId() == null || mesg.getMesgCopyServiceId().isEmpty()) ? "" : "/" + mesg.getMesgCopyServiceId();

		if ((statusCode != null && statusCode.equalsIgnoreCase("ACSP")) && (reasonCode.contains("G000") || reasonCode.contains("G001"))) {
			statusOrginatoLine += (sattlmentMethod != null && !sattlmentMethod.isEmpty()) ? "//" : "";
			statusOrginatoLine += (sattlmentMethod != null && !sattlmentMethod.isEmpty()) ? sattlmentMethod : "";
			statusOrginatoLine += cleringSystem;
		}

		////////////////////////////////////////////////////////

		// Line number 4
		String chargesValue = (mesg.getMesgCharges() == null || mesg.getMesgCharges().isEmpty()) ? "" : "/" + mesg.getMesgCharges();
		String currencyAmountLine = "";
		if ((statusCode != null && statusCode.equalsIgnoreCase("ACSP")) && (reasonCode.contains("G000") || reasonCode.contains("G001"))) {
			currencyAmountLine = "//" + mesg.getxFinCcy() + getAmountFormated(mesg.getxFinAmount()) + chargesValue;
		} else {
			currencyAmountLine = "//" + mesg.getxFinCcy() + getAmountFormated(mesg.getxFinAmount());
		}

		////////////////////////////////////////////////////////

		// We can here just add the mandatory lines 1,2,3,4
		confirmation = dataTimeLine + System.lineSeparator() + statusCodeLine + System.lineSeparator() + statusOrginatoLine + System.lineSeparator() + currencyAmountLine;

		// now the line 5 and repetitive fields is not mandatory

		// Line number 5
		String exChangeRate = "";
		if ((statusCode != null) && (statusCode.equalsIgnoreCase("ACCC") || (statusCode.equalsIgnoreCase("ACSP") && (reasonCode.contains("G000") || reasonCode.contains("G001"))))) {
			if (mesg.getMesgExchangeRate() != null && !mesg.getMesgExchangeRate().isEmpty()) {
				exChangeRate = "//EXCH//" + mesg.getxFinCcy() + "/" + mesg.getMesgInstructedCur() + "/" + mesg.getMesgExchangeRate();
				confirmation += System.lineSeparator() + exChangeRate;
			}
		}

		// In case if there is repetitive Field 71F we should added to confirmation messages
		if ((statusCode != null) && (statusCode.equalsIgnoreCase("ACCC") || (statusCode.equalsIgnoreCase("ACSP") && (reasonCode.contains("G000") || reasonCode.contains("G001"))))) {
			if (chargesValue != null && !chargesValue.equalsIgnoreCase("OUR")) {
				String repetitiveField71F = build71Field(mesg.getSenderChargeCur(), mesg.getSenderChargeAmount());
				if (!repetitiveField71F.isEmpty()) {
					confirmation += System.lineSeparator() + repetitiveField71F;
				}
			}

		}
		return confirmation;
	}

	public static void main(String[] args) {
		System.out.println(getAmountFormated(new BigDecimal("100.5000")));
		// System.out.println(new DecimalFormat("0.##########").format(new BigDecimal(1455400.0050100000)));
	}

	private static String getAmountFormated(BigDecimal amount) {
		if (amount == null)
			return "";
		String amountdb = new DecimalFormat("0.##########").format(amount);
		String amountSAA = "";
		if (!amountdb.contains(".")) {
			amountSAA = amountdb + ",";
		} else {
			amountSAA = amountdb.replace(".", ",");
		}

		return amountSAA;
	}

	public static String getCurrentTimezoneOffset() {

		TimeZone tz = TimeZone.getDefault();
		Calendar cal = GregorianCalendar.getInstance(tz);
		int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

		String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
		offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

		return offset;
	}

	private static String build71Field(String mesgSndChargesCurr, String mesgSndChargesAmount) {

		String finalmountCur = "";
		if (mesgSndChargesCurr == null || mesgSndChargesCurr.isEmpty())
			return "";

		String[] msgCur = mesgSndChargesCurr.split(",");
		String[] msgAmount = mesgSndChargesAmount.split(",");
		String amountCur = "";
		int count = 0;
		for (String cur : msgCur) {
			String amount = msgAmount[count].replace('.', ',');
			amountCur = "//:71F:" + cur + amount;
			if (count == (msgCur.length - 1)) {
				if (amountCur.contains(",")) {
					finalmountCur = finalmountCur + (amountCur);
				} else {
					finalmountCur = finalmountCur + (amountCur + ",");
				}

			} else {
				if (amountCur.contains(",")) {
					finalmountCur = finalmountCur + (amountCur + System.lineSeparator());
				} else {

					finalmountCur = finalmountCur + (amountCur + "," + System.lineSeparator());
				}
			}

			amountCur = "";
			count++;
		}
		return finalmountCur;
	}

	private String getBlock4(MessageDetails mesg, NotifierMessage message, Map<String, Queue> QueuesMap) {
		String block4 = "";
		String filedOption;
		String filedValue;
		String filed;

		// Try to get it from the rTextField table
		// ---------------------------------------
		List<TextFieldData> textFieldDataList = null;
		textFieldDataList = getTextFieldData(mesg, message, QueuesMap);

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

		return block4;
	}

	public DataSourceParser getDataSourceParser() {
		return dataSourceParser;
	}

	public void setDataSourceParser(DataSourceParser dataSourceParser) {
		this.dataSourceParser = dataSourceParser;
	}

	/*
	 * private String getBlock5(MessageDetails mesg, String subFormat, boolean pdeFlag) { String block5 = "";
	 * 
	 * String textswiftblock5; String mesgpossibledupcreation; String mesguserissuedaspde;
	 * 
	 * textswiftblock5 = StringUtils.defaultString(mesg.getTextSwiftBlock5(), ""); mesgpossibledupcreation = StringUtils.defaultString(mesg.getMesgPossibleDupCreation(), ""); mesguserissuedaspde= "";
	 * if ( mesg.getMesgUserIssuedAsPde() != null ){ mesguserissuedaspde= mesg.getMesgUserIssuedAsPde(); }
	 * 
	 * if (StringUtils.equalsIgnoreCase(subFormat , "Output") && !StringUtils.isEmpty( textswiftblock5 )){ block5 = "{5:" + textswiftblock5 + "}"; } else{ if
	 * ("PDE".equalsIgnoreCase(mesgpossibledupcreation) || "PDR".equalsIgnoreCase(mesgpossibledupcreation) || !StringUtils.isEmpty(mesguserissuedaspde) || pdeFlag == true) { block5 = "{5:" +
	 * textswiftblock5 + "{PDE:}}"; block5 = StringUtils.replace(block5, "{PDE:}{PDE:}", "{PDE:}");
	 * 
	 * } else { if (!StringUtils.isEmpty(textswiftblock5)) { block5 = "{5:" + textswiftblock5 + "}"; } } } return block5; }
	 */

}
