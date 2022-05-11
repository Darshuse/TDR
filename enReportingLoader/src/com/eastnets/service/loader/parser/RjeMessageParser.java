package com.eastnets.service.loader.parser;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.log4j.Logger;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.domain.loader.Appe;
import com.eastnets.domain.loader.AppePK;
import com.eastnets.domain.loader.AppePart;
import com.eastnets.domain.loader.BeanSup;
import com.eastnets.domain.loader.BeanSuper;
import com.eastnets.domain.loader.Inst;
import com.eastnets.domain.loader.InstPK;
import com.eastnets.domain.loader.InstPart;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.Mesg;
import com.eastnets.domain.loader.MesgPK;
import com.eastnets.domain.loader.Rintv;
import com.eastnets.domain.loader.RintvPK;
import com.eastnets.domain.loader.RintvPart;
import com.eastnets.domain.loader.Text;
import com.eastnets.domain.loader.TextPK;
import com.eastnets.domain.loader.TextPart;
import com.eastnets.resilience.mtutil.MTType;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.service.loader.exceptions.MessageParsingException;
import com.eastnets.service.viewer.ViewerService;

/**
 * @author MKassab
 * 
 */
public class RjeMessageParser implements MessageParser {

	private static final Logger LOGGER = Logger.getLogger(RjeMessageParser.class);

	private Map<String, MTType> mtTypesMap = null;
	private LoaderMessage loaderMessage;
	private String syntaxVersion = "1405";
	private ViewerService viewerService;
	private SimpleDateFormat dateF = new SimpleDateFormat("hh:mm:ss");

	public RjeMessageParser(ViewerService viewerService, String syntaxVersion, Map<String, MTType> mtTypesMap, LoaderMessage loaderMessage) {
		this.mtTypesMap = mtTypesMap;
		this.loaderMessage = loaderMessage;
		this.viewerService = viewerService;
		this.syntaxVersion = syntaxVersion;
	}

	@Override
	public LoaderMessage parse() throws Exception {
		LOGGER.debug("Start parsing ::: RJE Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() + " @ " + dateF.format(new Date()));
		LOGGER.debug("Start parsing RJE Message :: " + loaderMessage.getMessageSequenceNo());
		if (loaderMessage == null || loaderMessage.getRowData() == null || loaderMessage.getRowData().isEmpty()) {
			LOGGER.error(String.format("RJE Message[%s] is Empty", loaderMessage.getMessageSequenceNo().toPlainString()));
			LOGGER.debug("end parsing with errors ::: RJE Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() + " @ " + dateF.format(new Date()));
			throw new MessageParsingException(null, loaderMessage);
		}
		String mesgBlock = loaderMessage.getRowData(); // getting the mesg text

		try {
			Map<String, String> messageBlocks = splitMessageBlocks(mesgBlock, loaderMessage);
			if (messageBlocks == null) {
				LOGGER.debug("end parsing with errors ::: RJE Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() + " @ " + dateF.format(new Date()));
				throw new MessageParsingException(null, loaderMessage);
			}

			createNewMesg(loaderMessage);
			// start by b2 to get type and subformat
			parseDataBlock2(loaderMessage, messageBlocks.get("2"));
			parseDataBlock1(loaderMessage, messageBlocks.get("1"));
			if (messageBlocks.containsKey("3")) {
				parseDataBlock3(loaderMessage, messageBlocks.get("3"));
			}

			if (messageBlocks.containsKey("4")) {
				extractBlock4(loaderMessage, messageBlocks.get("4"));
			}

			// block five is optional
			if (messageBlocks.containsKey("5"))
				exractTrailerBlockData(loaderMessage, messageBlocks.get("5"));

			// we add the constant values to the message
			rjeConst(mesgBlock, loaderMessage);
			// setting the message nature
			setMesgNature(loaderMessage);
			// setting the X_OWN_LT
			setXOwnLt(loaderMessage);
			// adding the keys to the message aid , umidl , umidh
			loaderMessage.fillMesgKeys();
			// set the category type for the message
			fillAppeInst(loaderMessage);
			if (messageBlocks.containsKey("6"))
				splitNakMessage(loaderMessage, messageBlocks.get("6"));
			fillRintv(loaderMessage);
			LOGGER.debug("end parsing ::: RJE Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() + " @ " + dateF.format(new Date()));

		} catch (Exception e) {
			e.printStackTrace();
			throw new MessageParsingException(null, loaderMessage);
		}

		return loaderMessage;
	}

	private void prepareDataForPartitionDatabase(Mesg mesg) throws IllegalAccessException, InvocationTargetException {

		Converter converter = new DateConverter(null);
		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
		beanUtilsBean.getConvertUtils().register(converter, BigDecimal.class);
		beanUtilsBean.getConvertUtils().register(converter, Date.class);

		// inst
		ArrayList<InstPart> rinstsPart = new ArrayList<InstPart>();
		InstPart instPart = new InstPart();
		AppePart appePart = new AppePart();
		RintvPart rintvPart = new RintvPart();

		BeanUtils.copyProperties(instPart, loaderMessage.getMesg().getRinsts().get(0));
		instPart.setMesgCreaDateTime(loaderMessage.getMesg().getMesgCreaDateTime());
		rinstsPart.add(instPart);

		BeanUtils.copyProperties(appePart, loaderMessage.getMesg().getRinsts().get(0).getRappes().get(0));
		appePart.setMesgCreaDateTime(loaderMessage.getMesg().getMesgCreaDateTime());

		BeanUtils.copyProperties(rintvPart, loaderMessage.getMesg().getRinsts().get(0).getrIntv().get(0));
		rintvPart.setMesgCreaDateTime(loaderMessage.getMesg().getMesgCreaDateTime());

		rinstsPart.get(0).addRappePart(appePart);
		rinstsPart.get(0).addRintPart(rintvPart);
		loaderMessage.getMesg().setRinstsPart(rinstsPart);

		loaderMessage.getMesg().getRinstsPart().get(0).setRappes(null);
		loaderMessage.getMesg().getRinstsPart().get(0).setrIntv(null);
		loaderMessage.getMesg().setRinsts(null);

		// text

		TextPart textPart = new TextPart();
		BeanUtils.copyProperties(textPart, loaderMessage.getMesg().getRtext());
		textPart.setMesgCreaDateTime(loaderMessage.getMesg().getMesgCreaDateTime());
		loaderMessage.getMesg().setRtextPart(textPart);
		loaderMessage.getMesg().setRtext(null);

	}

	private void fillRintv(LoaderMessage loaderMessage) {
		LOGGER.debug("Start Parsing Rintv Inst :: " + loaderMessage.getMessageSequenceNo());

		List<Rintv> lisrRIntv = new ArrayList<Rintv>();
		Rintv rintv = new Rintv();
		rintv.setId(new RintvPK());

		// ID
		rintv.getId().setAid(loaderMessage.getMesg().getId().getAid());
		rintv.getId().setiNTVSUMIDH(loaderMessage.getMesg().getId().getMesgSUmidh());
		rintv.getId().setiNTVSUMIDL(loaderMessage.getMesg().getId().getMesgSUmidl());
		rintv.getId().setIntvDateTime(loaderMessage.getMesg().getMesgCreaDateTime());

		// Content
		rintv.setIntvMergedText(loaderMessage.getRowHistory());
		rintv.setIntvAppeDateTime(loaderMessage.getMesg().getMesgCreaDateTime());
		// rintv.setxCreaDateTimeMesg(loaderMessage.getMesg().getMesgCreaDateTime());
		lisrRIntv.add(rintv);

		if (loaderMessage.getMesg().getRinsts().get(0) != null) {
			loaderMessage.getMesg().getRinsts().get(0).setrIntv(lisrRIntv);
		}

	}

	private List<String> listOfNAKedMessages(String NAKedMessagesBlock) {
		Matcher matcher = Pattern.compile("\\{([^\\}]+)").matcher(NAKedMessagesBlock);
		List<String> nackedMessagesList = new ArrayList<>();
		int pos = -1;
		while (matcher.find(pos + 1)) {
			pos = matcher.start();
			nackedMessagesList.add(matcher.group(1));
		}
		return nackedMessagesList;
	}

	private Map<String, String> nakedMessagesFieldValue(List<String> nackedMessagesList) {
		Map<String, String> faildMap = new HashMap<String, String>();
		for (String faild : nackedMessagesList) {
			String[] arrFaild = faild.split(":");
			faildMap.put(arrFaild[0], arrFaild[1]);
		}
		return faildMap;
	}

	private void splitNakMessage(LoaderMessage loaderMessage, String line) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMddHHmm");
		Map<String, String> fieldMap = nakedMessagesFieldValue(listOfNAKedMessages(line));

		Mesg mesg = loaderMessage.getMesg();
		if (mesg.getRinsts().get(0).getRappes().size() > 0) {
			Appe appe = mesg.getRinsts().get(0).getRappes().get(0);
			String appeNetworkDeliveryStatus = fieldMap.get("451");
			if (appeNetworkDeliveryStatus.equals("0")) {
				appe.setAppeNetworkDeliveryStatus("DLV_ACKED");
			} else {
				appe.setAppeNetworkDeliveryStatus("DLV_NACKED");
			}
			appe.setAppeNakReason(fieldMap.get("405"));
			try {
				appe.getId().setAppeDateTime(dateFormatter.parse(fieldMap.get("177")));
				mesg.getRinsts().get(0).setXLastEmiAppeDateTime(dateFormatter.parse(fieldMap.get("177")));
				mesg.getRinsts().get(0).setXLastEmiAppeSeqNbr(appe.getAppeSequenceNbr());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws ParseException, IllegalAccessException, InvocationTargetException {
		/*
		 * String line = "{177:1801140352}{451:1}{405:H51}{108:804MSOG180100004}{mkassab:001}{mkassab2:002}{mkassab3:003}}}"; Matcher matcher = Pattern.compile("\\{([^\\}]+)").matcher(line);
		 * List<String> tags = new ArrayList<>(); int pos = -1; while (matcher.find(pos+1)){ pos = matcher.start(); tags.add(matcher.group(1)); } for(String tag:tags){ System.out.println(tag); }
		 */
		/*
		 * long val = 1801140352; Date date=new Date(val); SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy"); String dateText = df2.format(date); System.out.println(dateText); SimpleDateFormat
		 * dateFormatter = new SimpleDateFormat("yyMMddHHmm"); System.out.println(dateFormatter.parse("1801140352"));
		 */
		// sdf.format
		Converter converter = new DateConverter(null);
		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
		beanUtilsBean.getConvertUtils().register(converter, BigDecimal.class);
		beanUtilsBean.getConvertUtils().register(converter, Date.class);
		BeanSuper bean1 = new BeanSuper();
		bean1.setName("kassab");
		BeanSup bean2 = new BeanSup();
		BeanUtils.copyProperties(bean2, bean1);
		System.out.println(bean2.getName());
	}

	private Map<String, String> splitMessageBlocks(String mesgBlock, LoaderMessage loaderMesg) {
		Map<String, String> messageBlocks = new HashMap<String, String>();
		int indexOfBlockOne = mesgBlock.indexOf("{1:F01");
		int indexOfBlockTwo = mesgBlock.indexOf("{2:");
		int indexOfBlockThree = mesgBlock.indexOf("{3:");
		int indexOfBlockFour = mesgBlock.indexOf("{4:");
		int indexOfBlockFive = mesgBlock.lastIndexOf("{5:");
		int indexOfBlockACK = mesgBlock.lastIndexOf("{1:F21");

		if (indexOfBlockOne < 0) {
			LOGGER.error(String.format("RJE Message:: Can't find  block 1 in the message [%s] ", loaderMesg.getMessageSequenceNo().toPlainString()));
			return null;
		}
		if (indexOfBlockTwo < 0) {
			LOGGER.error(String.format("RJE Message:: Can't find  block 2 in the message [%s] ", loaderMesg.getMessageSequenceNo().toPlainString()));
			return null;
		}
		// Block 1
		String blockOne = mesgBlock.substring(indexOfBlockOne, indexOfBlockTwo); // we take the whole block here
		blockOne = blockOne.substring(3, blockOne.length() - 1); // we take only the specific text from the block
		messageBlocks.put("1", blockOne.trim());

		// Block 2
		String blockTwo = "";
		int indexEndBlock2 = mesgBlock.indexOf("}", indexOfBlockTwo);
		if (indexEndBlock2 < 0) {
			LOGGER.error(String.format("RJE Message:: Can't find  end of block 2 in the message [%s] ", loaderMesg.getMessageSequenceNo().toPlainString()));
			return null;
		}
		blockTwo = mesgBlock.substring(indexOfBlockTwo + 3, indexEndBlock2);
		messageBlocks.put("2", blockTwo.trim());

		// Block 3
		if (indexOfBlockThree > 0 && indexOfBlockThree < indexOfBlockFour) {
			String blockThree = mesgBlock.substring(indexOfBlockThree, indexOfBlockFour);
			blockThree = blockThree.substring(3, blockThree.length() - 1);
			messageBlocks.put("3", blockThree.trim());
		}

		// Block 4

		if (indexOfBlockFour > 0) { // some of system message doesn't has block4
			String blockFour = "";
			if (indexOfBlockFive > 0) {

				blockFour = mesgBlock.substring(indexOfBlockFour + 3, indexOfBlockFive).trim();// +3 as we have to remove {4:
				if (blockFour.endsWith("\r\n-}"))
					blockFour = blockFour.substring(0, blockFour.length() - 4);
				else if (blockFour.endsWith("-}"))
					blockFour = blockFour.substring(0, blockFour.length() - 2);
			} else {
				int endIndex = mesgBlock.lastIndexOf("\r\n-}");
				if (endIndex < 0) {
					endIndex = mesgBlock.lastIndexOf("-}");
				}
				blockFour = mesgBlock.substring(indexOfBlockFour + 3, endIndex);// +3 as we have to remove {4:
			}
			messageBlocks.put("4", blockFour.trim());
		}

		// Block5
		if (indexOfBlockFive > 0) {
			String blockFive = mesgBlock.substring(indexOfBlockFive);
			if (indexOfBlockACK > 0) {
				blockFive = blockFive.substring(3, blockFive.indexOf("{1:F21"));
			} else {
				blockFive = blockFive.substring(3, blockFive.length() - 1);
			}
			messageBlocks.put("5", blockFive.trim());
		}

		// BlockACK
		if (indexOfBlockACK > 0) {
			String blockAck = mesgBlock.substring(indexOfBlockACK);
			blockAck = blockAck.substring(blockAck.lastIndexOf("{4:"));
			blockAck = blockAck.substring(3);
			messageBlocks.put("6", blockAck);
		}

		return messageBlocks;
	}

	private void createNewMesg(LoaderMessage loaderMesg) {

		loaderMesg.setMesg(new Mesg());
		loaderMesg.getMesg().setId(new MesgPK());

		Inst inst = new Inst();
		inst.setId(new InstPK());
		loaderMesg.getMesg().addRinst(inst);
	}

	private void fillAppeInst(LoaderMessage loaderMesg) {

		LOGGER.debug("Start Parsing Appe Inst :: " + loaderMesg.getMessageSequenceNo());
		Mesg mesg = loaderMesg.getMesg();

		Inst inst = mesg.getRinsts().get(0);

		inst.setInstAppeDateTime(mesg.getMesgCreaDateTime());
		inst.setInstAppeSeqNbr(new BigDecimal(0));
		inst.setInstUnitName(mesg.getXInst0UnitName());
		inst.setInstDeferredTime(mesg.getMesgCreaDateTime());
		inst.setInstCreaDateTime(mesg.getMesgCreaDateTime());
		inst.setxLastRecAppeDateTime(mesg.getMesgCreaDateTime());

		inst.setXLastEmiAppeDateTime(mesg.getMesgCreaDateTime());
		inst.setxLastEmiAppeDateTime(mesg.getMesgCreaDateTime());
		inst.setInstReceiverX1(mesg.getxReceiverX1()); // new for correspondent
		if (mesg.getRinsts().get(0).getRappes().size() > 0) {
			Appe appe = mesg.getRinsts().get(0).getRappes().get(0);
			appe.getId().setAppeDateTime(mesg.getMesgCreaDateTime());
			appe.setAppeIappName("SWIFT");
			if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
				appe.setAppeType("APPE_EMISSION");
				appe.setAppeTransmissionNbr(new BigDecimal(1));
				appe.setAppeCreaRpName("_SI_to_SWIFT");
			} else {
				appe.setAppeType("APPE_RECEPTION");
				appe.setAppeTransmissionNbr(new BigDecimal(1));
				appe.setAppeCreaRpName("_SI_from_SWIFT");
			}

		}

	}

	private void rjeConst(String mesgBlock, LoaderMessage loaderMesg) {
		Mesg mesg = loaderMesg.getMesg();

		mesg.setMesgCreaApplServName("SIBES");
		;
		mesg.setMesgCreaRpName("SIBES");

		/*
		 * The following lines well test if unit name, creation date, and creation operation exist in DB or not
		 */
		Map<String, Object> messageProperties = loaderMesg.getMesgProperties();
		String creationOperater = "Unknown";
		Date date = new Date();
		String unitName = "None";
		if (messageProperties != null && !messageProperties.isEmpty()) {
			if (messageProperties.get("unitname") != null && !messageProperties.get("unitname").toString().isEmpty()) {
				unitName = messageProperties.get("unitname").toString();
			}
			if (messageProperties.get("creationdate") != null) {
				date = (Date) messageProperties.get("creationdate");
			}
			if (messageProperties.get("creationoperator") != null && !messageProperties.get("creationoperator").toString().isEmpty()) {
				creationOperater = messageProperties.get("creationoperator").toString();
			}
		}
		// ====

		mesg.setMesgCreaOperNickname(creationOperater);
		mesg.setMesgCreaDateTime(date);
		mesg.setMesgModOperNickname(creationOperater);
		mesg.setMesgModDateTime(date);
		mesg.setMesgNetworkDelvNotifReq(new BigDecimal(0));
		mesg.setMesgDelvOverdueWarnReq(new BigDecimal(0));
		mesg.setMesgFrmtName("Swift");
		mesg.setMesgIsRetrieved(new BigDecimal(0));
		mesg.setMesgNetworkApplInd("FIN");
		mesg.setMesgSenderCorrType("CORR_TYPE_INSTITUTION");
		mesg.setMesgSyntaxTableVer(syntaxVersion);
		mesg.setMesgZz41IsPossibleDup(new BigDecimal(0));
		mesg.setMesgUumidSuffix(new BigDecimal(0));
		mesg.setXFinOcmtAmount(new BigDecimal(0));
		mesg.setxFinOcmtCcy(null);
		mesg.setMesgCcyAmount(null);
		mesg.setMesgValidationPassed("VAL_NO_VALIDATION"); // new
		mesg.setXInst0UnitName(unitName); // new

		if (mesg.getRinsts() != null) {
			Inst inst = mesg.getRinsts().get(0);
			inst.setInstReceiverCorrType("CORR_TYPE_INSTITUTION");
		}

		mesg.getRtext().setTextDataBlockLen(new BigDecimal(mesg.getRtext().getTextDataBlock().length())); // new

		if (mesg.getMesgSenderSwiftAddress().charAt(7) == '0')
			mesg.setMesgIsLive(new BigDecimal(0));
		else
			mesg.setMesgIsLive(new BigDecimal(1));

	}

	private void setXOwnLt(LoaderMessage loaderMesg) {
		if (loaderMesg.getMesg().getMesgSubFormat().equalsIgnoreCase("INPUT")) {
			loaderMesg.getMesg().setXOwnLt("" + loaderMesg.getMesg().getMesgSenderSwiftAddress().substring(0, 8));
		} else {
			loaderMesg.getMesg().setXOwnLt("" + loaderMesg.getMesg().getMesgReceiverSwiftAddress().substring(0, 8));

		}
	}

	private void setMesgNature(LoaderMessage loaderMesg) {
		Mesg mesg = loaderMesg.getMesg();
		String sec = "960 , 961 , 962 , 963 , 964 , 965 , 966 , 967 , 075 , 087 , 085";
		if (mesg.getMesgType().equals("999")) {
			mesg.setMesgNature("TEXT_MSG");
		} else if (mesg.getMesgType().equals("05")) {
			mesg.setMesgNature("SERVICE_MSG");
		} else if (sec.contains(mesg.getMesgType())) {
			mesg.setMesgNature("SECURITY_MSG");
		} else if (mesg.getMesgType().startsWith("0")) {
			mesg.setMesgNature("NETWORK_MSG");
		} else {
			mesg.setMesgNature("FINANCIAL_MSG");
		}
	}

	private void parseDataBlock1(LoaderMessage loaderMesg, String blockOne) {
		Mesg mesg = loaderMesg.getMesg();
		if (mesg.getMesgSubFormat().compareToIgnoreCase("INPUT") == 0) {
			mesg.setMesgSenderSwiftAddress(blockOne.substring(3, 15));
			mesg.setMesgSenderX1(blockOne.substring(3, 11) + blockOne.substring(12, 15));
		} else if (mesg.getMesgSubFormat().compareToIgnoreCase("OUTPUT") == 0) {
			mesg.setMesgReceiverSwiftAddress(blockOne.substring(3, 15));
			mesg.setxReceiverX1(blockOne.substring(3, 11) + blockOne.substring(12, 15));
		}

		if (blockOne.length() <= 15) {
			return;
		}

		Inst inst = mesg.getRinsts().get(0);
		String appeSessionNbr = blockOne.substring(15, 19);
		if (appeSessionNbr != null && !appeSessionNbr.isEmpty() && !appeSessionNbr.contains("0000")) {
			Appe appe = new Appe();
			appe.setId(new AppePK());

			appe.setAppeSessionNbr(new BigDecimal(appeSessionNbr));
			appe.setAppeSequenceNbr(new BigDecimal(blockOne.substring(19)));
			appe.getId().setAppeSeqNbr(Long.parseLong(blockOne.substring(19)));

			appe.setAppeNetworkDeliveryStatus("DLV_ACKED");
			appe.setAppeRcvDeliveryStatus("EM_UNKNOWN");
			if (mesg.getMesgSubFormat().compareToIgnoreCase("INPUT") == 0)
				appe.setAppeSessionHolder(mesg.getMesgSenderSwiftAddress() != null ? mesg.getMesgSenderSwiftAddress() + "F" : mesg.getMesgSenderSwiftAddress());
			else
				appe.setAppeSessionHolder(mesg.getMesgReceiverSwiftAddress() != null ? mesg.getMesgReceiverSwiftAddress() + "F" : mesg.getMesgReceiverSwiftAddress());

			if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT"))
				inst.setXLastEmiAppeSeqNbr(new BigDecimal(blockOne.substring(19)));
			else
				inst.setxLastRecAppeSeqNbr(new BigDecimal(blockOne.substring(19)));

			inst.addRappe(appe);
		}

	}

	private void parseDataBlock2(LoaderMessage loaderMesg, String blockTwo) {
		Mesg mesg = loaderMesg.getMesg();

		if (blockTwo.substring(0, 1).equals("I"))
			mesg.setMesgSubFormat("INPUT");
		else
			mesg.setMesgSubFormat("OUTPUT");

		mesg.setMesgType(blockTwo.substring(1, 4));
		if (mesg.getMesgType() != null && !mesg.getMesgType().isEmpty()) {
			mesg.setXCategory("" + mesg.getMesgType().charAt(0));
			mesg.setMesgIdentifier("fin." + mesg.getMesgType());
		} else {
			mesg.setXCategory("" + loaderMesg.getMesgType().charAt(0));
			mesg.setMesgIdentifier("fin." + loaderMesg.getMesg().getMesgType());
		}

		/*
		 * if (mesgBlock.length() > 33) { mesg.setMesgNetworkObsoPeriod(new BigDecimal(0)); }
		 */
		if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
			String tempReciever = blockTwo.substring(4, 16);
			mesg.setMesgReceiverSwiftAddress(tempReciever);
			mesg.setxReceiverX1(tempReciever.substring(0, 9) + tempReciever.substring(10));
			mesg.setMesgNetworkPriority(getMesgNetworkPriority(blockTwo.substring(15, 16)));

			if (blockTwo.length() <= 16) { // Optional Delivery Monitoring
				return;
			}

			String delMonitoring = blockTwo.substring(16, 17);
			if (delMonitoring.compareTo("1") == 0) {
				mesg.setMesgDelvOverdueWarnReq(new BigDecimal(1));
			} else if (delMonitoring.compareTo("2") == 0) {
				mesg.setMesgNetworkDelvNotifReq(new BigDecimal(1));
			} else if (delMonitoring.compareTo("3") == 0) {
				mesg.setMesgNetworkDelvNotifReq(new BigDecimal(1));
				mesg.setMesgDelvOverdueWarnReq(new BigDecimal(1));
			}

			if (blockTwo.length() <= 17) { // Optional Delivery Monitoring
				return;
			}

			mesg.setMesgNetworkObsoPeriod(new BigDecimal(blockTwo.substring(17)));

		} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
			String appeRemoteInputTime = blockTwo.substring(4, 14);
			String tempSender = blockTwo.substring(14, 26);
			mesg.setMesgSenderSwiftAddress(tempSender);
			mesg.setMesgSenderX1(tempSender.substring(0, 8) + tempSender.substring(9));
			String appeLocalOutputTime = blockTwo.substring(36, 46);
			mesg.setMesgNetworkPriority(getMesgNetworkPriority(blockTwo.substring(46, 47)));
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMddHHmm");
			try {
				if (mesg.getRinsts().size() > 0 && mesg.getRinsts().get(0).getRappes().size() > 0) {
					mesg.getRinsts().get(0).getRappes().get(0).setAppeRemoteInputTime(dateFormatter.parse(appeRemoteInputTime));
					mesg.getRinsts().get(0).getRappes().get(0).setAppeLocalOutputTime(dateFormatter.parse(appeLocalOutputTime));
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}

	private void parseDataBlock3(LoaderMessage loaderMesg, String blockThree) {

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
		loaderMesg = assginBlockUserToObjects(loaderMesg, blockUserkeys, blockUservalues);
		String userGroup = loaderMesg.getMesg().getMesgMesgUserGroup();
		if (userGroup != null && !userGroup.isEmpty()) {
			String identifier = loaderMesg.getMesg().getMesgIdentifier();
			loaderMesg.getMesg().setMesgIdentifier(identifier + "." + userGroup);
		}

	}

	private void extractBlock4(LoaderMessage loaderMesg, String blockFour) {
		Mesg mesg = loaderMesg.getMesg();

		Text rtext = new Text();
		rtext.setId(new TextPK());
		rtext.setTextDataBlock(blockFour);
		mesg.setRtext(rtext);

		if (!mesg.getMesgType().startsWith("0")) {
			extractKeywordsFromBlock4(mesg, blockFour, loaderMesg);
		}

		String mesgUUMID = "";
		if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
			mesgUUMID += "I" + mesg.getxReceiverX1() + mesg.getMesgType() + mesg.getMesgTrnRef();
		} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
			mesgUUMID += "O" + mesg.getMesgSenderX1() + mesg.getMesgType() + mesg.getMesgTrnRef();
		}
		mesg.setMesgUumid(mesgUUMID);

	}

	private void extractKeywordsFromBlock4(Mesg mesg, String blockFour, LoaderMessage loaderMesg) {

		MTType mt = mtTypesMap.get(mesg.getMesgIdentifier().substring(4).toUpperCase());
		if (mt == null) {
			mesg.setMesgIdentifier("fin." + mesg.getMesgType());
			mt = mtTypesMap.get(mesg.getMesgType());
		}

		if (mt == null) {
			LOGGER.error(String.format("RJE Message:: Can't find the message %s in MT Map, for message ", mesg.getMesgIdentifier()));
		} else {
			String amtField = mt.getAmtField();
			String ccyField = mt.getCcyField();
			String valueDatqField = mt.getValueDateField();
			try {
				ParsedMessage value = viewerService.getMessageExpandedTextJRE(syntaxVersion, mesg.getMesgType(), blockFour, mesg.getMesgCreaDateTime());
				if (value == null) {
					LOGGER.error(String.format("RJE Message:: Error while pasring message [%s] of type %s", loaderMesg.getMessageSequenceNo().toPlainString(), mesg.getMesgIdentifier()));
				} else {
					Map<String, String> keywords = value.getExpandedMessageMap(amtField, ccyField, valueDatqField);
					fillMessageKeywords(loaderMesg, mesg, keywords);
				}
			} catch (Exception ex) {
				LOGGER.error(String.format("RJE Message:: Error while pasring message [%s] of type %s", loaderMesg.getMessageSequenceNo().toPlainString(), mesg.getMesgIdentifier()));
				ex.printStackTrace();
			}
		}

	}

	private void fillMessageKeywords(LoaderMessage loaderMesg, Mesg mesg, Map<String, String> keywords) {
		// Amount value formater
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);

		for (String str : keywords.keySet()) {

			// transaction
			String[] ar = str.split(":");

			if (ar[0].equalsIgnoreCase("20")) {
				mesg.setMesgTrnRef(keywords.get(str));
			} else if (ar[0].equalsIgnoreCase("20C")) {
				mesg.setMesgTrnRef(keywords.get(str));
			}
			try {
				if (str.contains(":AMOUNT")) {
					BigDecimal bigDecimal = (BigDecimal) decimalFormat.parse(keywords.get(str));
					mesg.setxFinAmount(bigDecimal);
				}
			} catch (ParseException ex) {
				LOGGER.error(String.format("RJE Message:: Error while pasring  amount[%s] of message [%s] of type %s", keywords.get(str), loaderMesg.getMessageSequenceNo(), mesg.getMesgIdentifier()));
			} catch (Exception ex) {
				LOGGER.error(String.format("RJE Message:: Error while pasring  amount of message [%s] of type %s", loaderMesg.getMessageSequenceNo(), mesg.getMesgIdentifier()));
			}

			if (str.contains(":CURRENCY")) {
				mesg.setXFinCcy(keywords.get(str));
			}

			if (str.contains(":DATE")) {
				String dateFormat = "yyMMdd";
				if (keywords.get(str).length() >= 8) {
					dateFormat = "yyyyMMdd";

				} else if (keywords.get(str).length() == 6) {
					dateFormat = "yyMMdd";
				}

				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				Date valueDate = null;
				try {
					String dateString = keywords.get(str);
					if (dateString != null && dateString.length() > 8) {
						dateString = dateString.substring(0, 8);
					}
					valueDate = sdf.parse(dateString);
				} catch (Exception e) {
					throw new WebClientException("the parameter valueDate in RJEMessageParser is not valid, error message : " + e.getMessage());
				}

				mesg.setXFinValueDate(valueDate);
				if (keywords.get(str).length() == 8)
					mesg.setMesgFinValueDate(keywords.get(str).substring(2));
				else if (keywords.get(str).length() == 6)
					mesg.setMesgFinValueDate(keywords.get(str));

			}
		}
	}

	private String getMesgNetworkPriority(String str) {
		if (str.compareToIgnoreCase("S") == 0)
			return "PRI_SYSTEM";
		else if (str.compareToIgnoreCase("U") == 0)
			return "PRI_URGENT";
		return "PRI_NORMAL";
	}

	private String extactBraces(String block) {
		block = block.replace("}{", ":");
		block = block.replace("{", "");
		block = block.replace("}", "");
		return block;
	}

	private void exractTrailerBlockData(LoaderMessage loaderMesg, String trailersBlock) {
		if (trailersBlock.contains("PDE") || trailersBlock.contains("PDR")) {
			loaderMesg.getMesg().setMesgPossibleDupCreation("PDE");
			loaderMesg.getMesg().setMesgUserIssuedAsPde(new BigDecimal(1));
		}
		loaderMesg.getMesg().getRtext().setTextSwiftBlock5(trailersBlock);
	}

	private LoaderMessage assginBlockUserToObjects(LoaderMessage loaderMesg, ArrayList<Integer> blockUserkeys, ArrayList<String> blockUservalues) {
		for (int i = 0; i < blockUserkeys.size(); i++) {
			switch (blockUserkeys.get(i)) {
			case 103:
				loaderMesg.getMesg().setMesgCopyServiceId(blockUservalues.get(i));
				break;
			case 113:
				loaderMesg.getMesg().setMesgUserPriorityCode(blockUservalues.get(i));
				break;
			case 108:
				loaderMesg.getMesg().setMesgUserReferenceText(blockUservalues.get(i));
				break;
			case 111:// SLA
				loaderMesg.getMesg().setMesgSLA(blockUservalues.get(i));
				break;
			case 121:// UETR
				loaderMesg.getMesg().setMesgUetr(blockUservalues.get(i));
				break;
			case 119:
				loaderMesg.getMesg().setMesgMesgUserGroup(blockUservalues.get(i));
				break;/*
						 * case 423: loaderMesg.getMesg().setUserBlockBalanceCeckPointDateTime(blockUservalues.get(i)); System.out.println("userBlockBalanceCeckPointDateTime " +
						 * loaderMesg.getMesg().getUserBlockBalanceCeckPointDateTime()); break; case 106: loaderMesg.getMesg().setUserBlockMesgInputRef(blockUservalues.get(i));
						 * System.out.println("userBlockMesgInputRef " + loaderMesg.getMesg().getUserBlockMesgInputRef()); break; case 424:
						 * loaderMesg.getMesg().setUserBlockRelatedRef(blockUservalues.get(i)); System.out.println("userBlockRelatedRef " + loaderMesg.getMesg().getUserBlockRelatedRef()); break;
						 */
			case 115:
				loaderMesg.getMesg().setMesgReleaseInfo(blockUservalues.get(i));
				break;/*
						 * case 433: loaderMesg.getMesg().setUserBlockSanctionsScreeninInfor(blockUservalues.get(i)); System.out.println("userBlockSanctionsScreeninInfor " +
						 * loaderMesg.getMesg().getUserBlockSanctionsScreeninInfor()); break;
						 */
			default:

				break;
			}
		}
		return loaderMesg;

	}

	@Override
	public LoaderMessage call() throws Exception {
		/*
		 * parse and return loader message
		 */

		return parse();
	}
}
