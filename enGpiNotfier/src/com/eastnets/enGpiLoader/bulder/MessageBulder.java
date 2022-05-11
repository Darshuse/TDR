package com.eastnets.enGpiLoader.bulder;

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
import com.eastnets.enGpiLoader.utility.DataSourceParser;
import com.eastnets.enGpiLoader.utility.GpiHelper;
import com.eastnets.enGpiLoader.utility.Queue;
import com.eastnets.service.viewer.helper.ViewerServiceUtils;
import com.eastnets.utils.ApplicationUtils;

/**
 * @author MKassab
 * 
 */
public class MessageBulder {

	private DataSourceParser dataSourceParser;


	public String buildRJE(NotifierMessage message,MessageDetails mesg, boolean pdeFlag, boolean notFirstMessage,Map<String, Queue> QueuesMap,DataSourceParser dataSourceParser) {
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

		this.dataSourceParser=dataSourceParser;
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
		block2 = getBlock2(mesg, mesgType, subFormat, sender, receiver, apperemoteinputreference, appelocaloutputtime,
				apperemoteinputtime);

		// ------------- BLOCK 3
		block3 = getBlock3(mesg);

		// ------------- BLOCK 4
		block4 = getBlock4(mesg,message,QueuesMap);

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

	private String getBlock1(String mesgType, String subFormat, String sender, String receiver, String sequenceNbr,
			String sessionNbr) {
		
		if(dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic() == null || dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic().isEmpty()){
			//doNothing
		}else{
			receiver=dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic();
			sender=dataSourceParser.getxMLNotifierConfigHelper().getDelegatBic();
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

	private String getBlock2(MessageDetails mesg, String mesgType, String subFormat, String sender, String receiver,
			String apperemoteinputreference, String appelocaloutputtime, String apperemoteinputtime) {
	/*	String mesgnetworkpriority = StringUtils.defaultString(mesg.getMesgNetworkPriority(), "-1");
		String mesgnetworkdelvnotifreq = StringUtils.defaultString(mesg.getMesgNetworkDelvNotifReq(), "0");
		String mesgdelvoverduewarnreq = StringUtils.defaultString(mesg.getMesgDelvOverdueWarnReq(), "0");
		String mesgnetworkobsoperiod = "0";
		if (mesg.getMesgNetworkObsoPeriod() != null) {
			mesgnetworkobsoperiod = "" + mesg.getMesgNetworkObsoPeriod();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		String mesgcreadatetime = sdf.format(mesg.getMesgCreaDateTime());

		String block2 = "";
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
		if ("PRISYSTEM".equalsIgnoreCase(str)) {
			block2 = "{2:I" + mesgType + receiver + "S" + obsolescence + "}";
		} else if ("PRIURGENT".equalsIgnoreCase(str)) {
			block2 = "{2:I" + mesgType + receiver + "U" + obsolescence + "}";
		} else if ("PRINORMAL".equalsIgnoreCase(str)) {
			block2 = "{2:I" + mesgType + receiver + "N" + obsolescence + "}";
		} else {
			block2 = "{2:I" + mesgType + receiver + "N}";
		}
		return block2;*/
		String block2 = "";
		if(dataSourceParser.getxMLNotifierConfigHelper().getEnvironmentType().equalsIgnoreCase("Test")){ 
			if(mesg.getMesgType().contains("103")){
				block2="{2:I" + "199TRCKCHZ0XXXXN"+ "}";
			}else if(mesg.getMesgType().contains("202") || mesg.getMesgType().contains("205")){
				block2="{2:I" + "299TRCKCHZ0XXXXN"+ "}";
			} 
		} 
		else{ 
			if(mesg.getMesgType().contains("103")){
				block2="{2:I" + "199TRCKCHZZXXXXN"+ "}";
			}else if(mesg.getMesgType().contains("202") || mesg.getMesgType().contains("205")){
				block2="{2:I" + "299TRCKCHZZXXXXN"+ "}";
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

		if (!StringUtils.isEmpty(mesg.getMesgReleaseInfo())) {
			block3 = block3 + "{115:" + mesg.getMesgReleaseInfo() + "}";
			hasBlock3 = true;
		}

		if (!StringUtils.isEmpty(mesg.getMesgMesgUserGroup())) {
			block3 = block3 + "{119:" + mesg.getMesgMesgUserGroup() + "}";
			hasBlock3 = true;
		} 
			block3 = block3 + "{111:" + "001" + "}";
			hasBlock3 = true;
	 
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

	private List<TextFieldData> getTextFieldData(MessageDetails mesg,NotifierMessage message  ,Map<String, Queue> QueuesMap) {
		List<TextFieldData> textFieldList = new ArrayList<TextFieldData>();
		String rejex=dataSourceParser.getxMLNotifierConfigHelper().getRejex();
		TextFieldData textFieldDataRef = new TextFieldData().new TextFieldDataBulder(20, "", GpiHelper.generetTrnRef(rejex), null).build();
		textFieldList.add(textFieldDataRef);
		TextFieldData textFieldDataRelRef = new TextFieldData().new TextFieldDataBulder(21, "", mesg.getMesgTrnRef(), null)
				.build();
		textFieldList.add(textFieldDataRelRef);
		TextFieldData textFieldDataConfirmation = new TextFieldData().new TextFieldDataBulder(79, "",
				buildConfirmationField(message,QueuesMap,mesg), null).build();
		textFieldList.add(textFieldDataConfirmation);
		return textFieldList;
	}

	private String buildConfirmationField( NotifierMessage message  ,Map<String, Queue> QueuesMap,MessageDetails mesg) {  
		String confirmation=""; 
		String statusOrginatoLine="";
		String dataTimeLine = "//"+new SimpleDateFormat("yyMMddHHmmZ").format(new Date());
		String statusCodeLine = "//ACSP/"+((message.getDataSource().equals(DataSource.SAA)) ? QueuesMap.get(message.getQueueName()).getReasonCode():message.getReasonCode());
		if(mesg.getMesgSubFormat().equalsIgnoreCase("Input")){
			 statusOrginatoLine = "//"+mesg.getMesgSenderX1();
		}else{
			 statusOrginatoLine = "//"+mesg.getInstReceiverX1();
		}
		
		String currencyAmountLine = "//"+mesg.getxFinCcy()+getAmountFormated(mesg.getxFinAmount());
		confirmation=dataTimeLine + System.lineSeparator() + statusCodeLine + System.lineSeparator() + statusOrginatoLine
				+ System.lineSeparator() + currencyAmountLine;
		
		String repetitiveField71F =build71Field(mesg.getMesgSndChargesCurr(),mesg.getMesgSndChargesAmount());
		if(!repetitiveField71F.isEmpty()){
			confirmation=dataTimeLine + System.lineSeparator() + statusCodeLine + System.lineSeparator() + statusOrginatoLine
					+ System.lineSeparator() + currencyAmountLine+System.lineSeparator()+repetitiveField71F;	
		}
		return confirmation;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getAmountFormated(new BigDecimal("100.5000")));
		//System.out.println(new DecimalFormat("0.##########").format(new BigDecimal(1455400.0050100000)));
	}
	
	
	private static String getAmountFormated(BigDecimal amount){
		if(amount == null) 
			return "";
	String amountdb=new DecimalFormat("0.##########").format(amount);
	String amountSAA="";
		if(!amountdb.contains(".")){
			amountSAA=amountdb+",";
		} 
		else{ 
			amountSAA=amountdb.replace(".", ",");	
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
		 
		String finalmountCur="";
		if(mesgSndChargesCurr  == null ||  mesgSndChargesCurr.isEmpty() )
			return "";
		
		String [] msgCur=mesgSndChargesCurr.split(",");
		String [] msgAmount=mesgSndChargesAmount.split(","); 
		String amountCur="";
		int count=0;
		for(String cur:msgCur){
			String amount=msgAmount[count].replace('.', ',');
			amountCur="//:71F:"+cur+amount; 
			if(count==(msgCur.length-1)){
				if(amountCur.contains(",")){ 
					finalmountCur=finalmountCur+(amountCur);
				}else{
					finalmountCur=finalmountCur+(amountCur+",");
				}
					
			}else{
				if(amountCur.contains(",")){ 
					finalmountCur=finalmountCur+(amountCur+System.lineSeparator());		
				}else{

					finalmountCur=finalmountCur+(amountCur+","+System.lineSeparator());		
				}
			}
			
			amountCur="";
			count++;
		}
		return finalmountCur;
	}
	
	
 

	private String getBlock4(MessageDetails mesg,NotifierMessage message  ,Map<String, Queue> QueuesMap) {
		String block4 = "";
		String filedOption;
		String filedValue;
		String filed;



		// Try to get it from the rTextField table
		// ---------------------------------------
		List<TextFieldData> textFieldDataList = null;
		textFieldDataList = getTextFieldData(mesg,message,QueuesMap);

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
	 * private String getBlock5(MessageDetails mesg, String subFormat, boolean
	 * pdeFlag) { String block5 = "";
	 * 
	 * String textswiftblock5; String mesgpossibledupcreation; String
	 * mesguserissuedaspde;
	 * 
	 * textswiftblock5 = StringUtils.defaultString(mesg.getTextSwiftBlock5(),
	 * ""); mesgpossibledupcreation =
	 * StringUtils.defaultString(mesg.getMesgPossibleDupCreation(), "");
	 * mesguserissuedaspde= ""; if ( mesg.getMesgUserIssuedAsPde() != null ){
	 * mesguserissuedaspde= mesg.getMesgUserIssuedAsPde(); }
	 * 
	 * if (StringUtils.equalsIgnoreCase(subFormat , "Output") &&
	 * !StringUtils.isEmpty( textswiftblock5 )){ block5 = "{5:" +
	 * textswiftblock5 + "}"; } else{ if
	 * ("PDE".equalsIgnoreCase(mesgpossibledupcreation) ||
	 * "PDR".equalsIgnoreCase(mesgpossibledupcreation) ||
	 * !StringUtils.isEmpty(mesguserissuedaspde) || pdeFlag == true) { block5 =
	 * "{5:" + textswiftblock5 + "{PDE:}}"; block5 = StringUtils.replace(block5,
	 * "{PDE:}{PDE:}", "{PDE:}");
	 * 
	 * } else { if (!StringUtils.isEmpty(textswiftblock5)) { block5 = "{5:" +
	 * textswiftblock5 + "}"; } } } return block5; }
	 */

}
