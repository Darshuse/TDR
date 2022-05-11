package com.eastnets.enGpiLoader.parser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger; 
import com.eastnets.domain.viewer.MessageDetails; 
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.service.ServiceLocator; 

public class RjeMessageParser {
	
	MessageDetails mesg;

	private Map<String, MTType> mtTypesMap = null ;

	private static final Logger LOGGER = Logger.getLogger(RjeMessageParser.class);
	private ServiceLocator serviceLocator; 
	
	
	public RjeMessageParser(Map<String, MTType> mtTypesMap,ServiceLocator serviceLocator){
		this.mtTypesMap=mtTypesMap;
		this.serviceLocator=serviceLocator;
		
	}
	
	
	public MessageDetails parse(String mesgBlock){
		mesg=new MessageDetails(); 
		try{
			Map<String , String> messageBlocks= splitMessageBlocks(mesgBlock);  
			parseDataBlock2( messageBlocks.get("2"));
			parseDataBlock1( messageBlocks.get("1")); 
			if(messageBlocks.containsKey("3")){
				parseDataBlock3( messageBlocks.get("3"));
			} 
			if(messageBlocks.containsKey("4")){
				extractBlock4( messageBlocks.get("4"));
			} 	
		}
		catch (Exception e) {
			e.printStackTrace(); 
		}

		return mesg;
	}
	
	
	private Map<String , String> splitMessageBlocks(String mesgBlock) throws JAXBException{
		Map<String , String> messageBlocks = new HashMap<String, String>();
		int indexOfBlockOne = mesgBlock.indexOf("{1:F01");
		int indexOfBlockTwo = mesgBlock.indexOf("{2:");
		int indexOfBlockThree = mesgBlock.indexOf("{3:");
		int indexOfBlockFour = mesgBlock.indexOf("{4:");
		int indexOfBlockFive = mesgBlock.lastIndexOf("{5:");
		int indexOfBlockACK = mesgBlock.lastIndexOf("{1:F21");
		
	
		//Block 1
		String blockOne = mesgBlock.substring(indexOfBlockOne ,indexOfBlockTwo); // we take the whole block here
		blockOne = blockOne.substring(3,blockOne.length()-1); // we take only the specific text from the block
		messageBlocks.put("1", blockOne.trim());
		
		//Block 2
		String blockTwo= "";
		int indexEndBlock2=mesgBlock.indexOf("}",indexOfBlockTwo);
		if(indexEndBlock2<0){
			return null;
		}
		blockTwo = mesgBlock.substring(indexOfBlockTwo+3 ,indexEndBlock2);
		messageBlocks.put("2", blockTwo.trim());
		
		//Block 3
		if(indexOfBlockThree>0 && indexOfBlockThree<indexOfBlockFour){
			String blockThree = mesgBlock.substring(indexOfBlockThree ,indexOfBlockFour);
			blockThree = blockThree.substring(3,blockThree.length()-1);
			messageBlocks.put("3",blockThree.trim());
		}
		
		//Block 4
		
		if(indexOfBlockFour>0){ //some of system message doesn't has block4
			String blockFour = "";
			if(indexOfBlockFive>0){

				blockFour = mesgBlock.substring(indexOfBlockFour+3,indexOfBlockFive).trim();// +3 as we have to remove {4:
				if(blockFour.endsWith("\r\n-}"))
					blockFour=blockFour.substring(0,blockFour.length()-4 );
				else if(blockFour.endsWith("-}"))
					blockFour=blockFour.substring(0,blockFour.length()-2 );
			}else{
				int endIndex=mesgBlock.lastIndexOf("\r\n-}");
				if (endIndex < 0) {
					endIndex=mesgBlock.lastIndexOf("-}");
				}
				blockFour = mesgBlock.substring(indexOfBlockFour+3,endIndex);// +3 as we have to remove {4:
			}
			messageBlocks.put("4",blockFour.trim());
		}
		
		//Block5
		if(indexOfBlockFive>0){
			String blockFive = mesgBlock.substring(indexOfBlockFive);
			if(indexOfBlockACK>0){
				blockFive = blockFive.substring(3,blockFive.indexOf("{1:F21"));	
			}
			else{ 
				blockFive = blockFive.substring(3,blockFive.length()-1);
			}
			messageBlocks.put("5",blockFive.trim());
		}
		
		//BlockACK
		 if(indexOfBlockACK>0){
			String blockAck = mesgBlock.substring(indexOfBlockACK); 
			blockAck=blockAck.substring(blockAck.lastIndexOf("{4:"));
			blockAck=blockAck.substring(3);
			messageBlocks.put("6",blockAck); 
		}

		
		return messageBlocks;
	}
	
	

	private void parseDataBlock1( String blockOne) { 
		if(mesg.getMesgSubFormat().compareToIgnoreCase("INPUT")==0){
			mesg.setMesgSenderSwiftAddress(blockOne.substring(3, 15));
			mesg.setMesgSenderX1(blockOne.substring(3, 11)+blockOne.substring(12, 15));
		}else if(mesg.getMesgSubFormat().compareToIgnoreCase("OUTPUT")==0){
			mesg.setMesgReceiverSwiftAddress(blockOne.substring(3, 15));
			mesg.setInstReceiverX1(blockOne.substring(3, 11)+blockOne.substring(12, 15));
		}

		if(blockOne.length() <= 15){
			return;
		} 

	}


	
	private void  parseDataBlock2(String blockTwo) { 

		if(blockTwo.substring(0,1).equals("I"))
			mesg.setMesgSubFormat("INPUT");
		else
			mesg.setMesgSubFormat("OUTPUT");
		
		mesg.setMesgType(blockTwo.substring(1,4));  
		mesg.setMesgIdentifier("fin."+mesg.getMesgType());
		if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
			String tempReciever = blockTwo.substring(4, 16);
			mesg.setMesgReceiverSwiftAddress(tempReciever);  
			 

		} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) { 
			String tempSender = blockTwo.substring(14, 26);
			mesg.setMesgSenderSwiftAddress(tempSender);
			mesg.setMesgSenderX1(tempSender.substring(0,8)+tempSender.substring(9)); 

		}
	}


	private void parseDataBlock3(  String blockThree) {
		
		blockThree = extactBraces(blockThree);
		String[] keyValueArray = blockThree.split(":");
		ArrayList<Integer> blockUserkeys = new ArrayList<Integer>();
		ArrayList<String> blockUservalues = new ArrayList<String>();
		for (int i = 1; i <= keyValueArray.length; i++) {
			if ((i % 2) == 0) {
				// value
				blockUservalues.add(keyValueArray[i - 1].trim());
			} else {
				// key
				blockUserkeys.add(Integer.parseInt(keyValueArray[i - 1].trim()));

			}
		}
		  assginBlockUserToObjects( blockUserkeys, blockUservalues);
		String userGroup=mesg.getMesgMesgUserGroup();
		if(userGroup != null  && !userGroup.isEmpty()){
			String identifier=mesg.getMesgIdentifier();
			mesg.setMesgIdentifier(identifier+"."+userGroup);
		}
		
	}
	
	
	private void assginBlockUserToObjects( ArrayList<Integer> blockUserkeys, ArrayList<String> blockUservalues) {
		for (int i = 0; i < blockUserkeys.size(); i++) {
			switch (blockUserkeys.get(i)) {
			case 103:
				mesg.setMesgCopyServiceId(blockUservalues.get(i));
				break;
			case 113:
				mesg.setMesgUserPriorityCode(blockUservalues.get(i));
				break;
			case 108:
				mesg.setMesgUserReferenceText(blockUservalues.get(i));
				break;
			case 119:
				mesg.setMesgMesgUserGroup(blockUservalues.get(i));
				break;/*
			case 423:
				loaderMesg.getMesg().setUserBlockBalanceCeckPointDateTime(blockUservalues.get(i));
				System.out.println("userBlockBalanceCeckPointDateTime " + loaderMesg.getMesg().getUserBlockBalanceCeckPointDateTime());
				break;
			case 106:
				loaderMesg.getMesg().setUserBlockMesgInputRef(blockUservalues.get(i));
				System.out.println("userBlockMesgInputRef " + loaderMesg.getMesg().getUserBlockMesgInputRef());
				break;
			case 424:
				loaderMesg.getMesg().setUserBlockRelatedRef(blockUservalues.get(i));
				System.out.println("userBlockRelatedRef " + loaderMesg.getMesg().getUserBlockRelatedRef());
				break;*/
			case 115:
				mesg.setMesgReleaseInfo(blockUservalues.get(i));
				break;
			case 121:
				mesg.setUetr(blockUservalues.get(i));
				break;
			default:
				
				break;
			}
		} 

	}
	
	private String extactBraces(String block) {
		block = block.replace("}{", ":");
		block = block.replace("{", "");
		block = block.replace("}", "");
		return block;
	}


	private void extractBlock4( String blockFour){   

		if(!mesg.getMesgType().startsWith("0")){
			extractKeywordsFromBlock4(blockFour);
		}
		
		String mesgUUMID = "" ;
		if(mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")){
			mesgUUMID+="I"+mesg.getInstReceiverX1()+mesg.getMesgType()+mesg.getMesgTrnRef();
		}else if(mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")){
			mesgUUMID+="O"+mesg.getMesgSenderX1()+mesg.getMesgType()+mesg.getMesgTrnRef();
		}
		mesg.setMesgUumid(mesgUUMID);

	}

	private void extractKeywordsFromBlock4(String blockFour) {

		MTType mt = mtTypesMap.get(mesg.getMesgIdentifier().substring(4).toUpperCase());
		if(mt==null){
			mesg.setMesgIdentifier("fin."+mesg.getMesgType());
			mt = mtTypesMap.get(mesg.getMesgType());
		}

		if(mt==null){
			LOGGER.error(String.format("RJE Message:: Can't find the message %s in MT Map, for message ",mesg.getMesgIdentifier()));
		}else{
			String amtField = mt.getAmtField();
			String ccyField = mt.getCcyField();
			String valueDatqField = mt.getValueDateField();
			String syntaxVersion=serviceLocator.getViewerService().getLatestInstalledSyntaxVer("");
			try{
				ParsedMessage value = serviceLocator.getViewerService().getMessageExpandedTextJRE(syntaxVersion, mesg.getMesgType(),blockFour,mesg.getMesgCreaDateTime()); 
				if(value==null){
					LOGGER.error(String.format("RJE Message:: Error while pasring message [%s] of type %s","",mesg.getMesgIdentifier()));
				}else{
					Map<String,String> keywords = value.getExpandedMessageMap(amtField,ccyField,valueDatqField);
					fillMessageKeywords(keywords);
				}
			}catch(Exception ex){
				LOGGER.error(String.format("RJE Message:: Error while pasring message [%s] of type %s","",mesg.getMesgIdentifier()));
				ex.printStackTrace();
			}
		}

	}
	
	
	private void fillMessageKeywords(Map<String,String> keywords){
		//Amount value formater 
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);

		for(String str : keywords.keySet()){

			// transaction
			String [] ar = str.split(":");

			if(ar[0].equalsIgnoreCase("20")){
				mesg.setMesgTrnRef(keywords.get(str));
			}else if(ar[0].equalsIgnoreCase("20C")){
				mesg.setMesgTrnRef(keywords.get(str));
			}
			try{
				if(str.contains(":AMOUNT")){
					BigDecimal bigDecimal = (BigDecimal) decimalFormat.parse(keywords.get(str));					
					mesg.setxFinAmount(bigDecimal);
				}
			}catch (ParseException ex){ 
			}catch(Exception ex){
	 
			}

		 	if(str.contains(":CURRENCY")){
				mesg.setxFinCcy(keywords.get(str));
			} 
			
			if(str.contains(":DATE")){
				String dateFormat="yyMMdd";
				if(keywords.get(str).length() >= 8){
					dateFormat="yyyyMMdd";

				}else if(keywords.get(str).length()==6){
					dateFormat="yyMMdd";
				} 

			}
		}	
	}

}
