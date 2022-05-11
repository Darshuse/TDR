package com.eastnets.extraction.service.export;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.AppendixDetails;
import com.eastnets.extraction.bean.CorrInfo;
import com.eastnets.extraction.bean.InstanceDetails;
import com.eastnets.extraction.bean.InterventionDetails;
import com.eastnets.extraction.bean.MessageDetails;
import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.StatisticsFile;
import com.eastnets.extraction.bean.TextFieldData;
import com.eastnets.extraction.bean.XMLMessage;
import com.eastnets.extraction.dao.search.SearchDAO;
import com.eastnets.extraction.dao.search.XMLReaderDAO;
import com.eastnets.extraction.service.helper.ExportingUtils;
import com.eastnets.extraction.service.helper.FileWriterUtils;
import com.eastnets.extraction.service.helper.Pair;
import com.eastnets.extraction.service.helper.ReportGenerator;
import com.eastnets.extraction.service.helper.SearchUtils;

@Service
@Qualifier("ExitPointExportingImpl")
public class ExitPointExportingImpl implements ExportingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExitPointExportingImpl.class);

	@Autowired
	private SearchDAO searchDAO;

	@Autowired
	private XMLReaderDAO xmlReaderDAO;

	@Autowired
	private ExportingUtils exportingUtils;

	String dateFormat = "yyyy/MM/dd";
	String timeFormat = "HH:mm:ss";

	@Value("${cacheCorr:true}")
	private boolean cacheCorr;

	@Value("${showBeingUpdated:false}")
	private boolean showBeingUpdated;

	Map<String, CorrInfo> corrMap = new HashMap<String, CorrInfo>();

	@Override
	public void exportMessages(List<SearchResult> searchResult, SearchParam searchParam, StatisticsFile statisticsFile) {

		LOGGER.info("Exit point printing format process started.");

		List<String> exitPointList = new ArrayList<String>();
		try {
			exitPointList = getExitPointFormatList(searchResult, searchParam, statisticsFile);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (exitPointList.size() > 0) {
			LOGGER.info("Exit point printing format process finished.");
		} else {
			LOGGER.info("No Exit point printing format will be generated.");
		}

		if (!searchParam.isDryRun() && exitPointList.size() > 0) {
			LOGGER.info("Exit point printing format file/s generator started.");
			FileWriterUtils.generateFile(exitPointList, searchParam, "txt", statisticsFile);
			LOGGER.info("Exit point printing format file/s generator finished.");
		}
	}

	private List<String> getExitPointFormatList(List<SearchResult> searchResult, SearchParam searchParam, StatisticsFile statisticsFile) throws InterruptedException {
		List<MessageDetails> detailedMessages = new ArrayList<MessageDetails>();
		ReportGenerator generator = new ReportGenerator(searchDAO, xmlReaderDAO, dateFormat, timeFormat);

		try {

			String buildCompsiteKeyString = exportingUtils.buildCompsiteKeyString(searchResult);
			List<MessageDetails> messageDetails = getMessageDetails(buildCompsiteKeyString, searchParam);

			for (MessageDetails message : messageDetails) {
				if ("file".equalsIgnoreCase(message.getMesgFrmtName())) {
					continue;
				}

				if (message != null) {
					detailedMessages.add(message);
				}

			}

			statisticsFile.setNumberOfGeneratedExitPoint(detailedMessages.size() + statisticsFile.getNumberOfGeneratedExitPoint());
		} catch (Exception ex) {
			LOGGER.error("DataExtraction exception :: " + ex.getMessage(), ex);
		}
		return generator.printReport(detailedMessages, false, false, searchParam.isExpand(), searchParam.isHistory(), 0, true, false, false);

	}

	public List<MessageDetails> getMessageDetails(String compsiteKeyString, SearchParam searchParam) throws Exception {

		List<MessageDetails> messageDetails = searchDAO.getMessageDetails(compsiteKeyString, searchParam.isEnableDebugFull());

		List<InstanceDetails> instanceList = searchDAO.getInstanceList(compsiteKeyString);

		if (instanceList != null && !instanceList.isEmpty()) {
			messageDetails.stream()
					.forEach(n -> n.setMesgInstances(instanceList.stream().filter(x -> x.getAid().equals(n.getAid()) && x.getMesgUmidl().equals(n.getMesgUmidl()) && x.getMesgUmidh().equals(n.getMesgUmidh())).collect(Collectors.toList())));
		}
		if (searchParam.isHistory()) {

			List<InterventionDetails> interventionList = searchDAO.getInterventionList(compsiteKeyString);
			// Map intervention details to it's message details
			messageDetails.stream().forEach(
					n -> n.setInterventionDetailsList(interventionList.stream().filter(x -> x.getAid().equals(n.getAid()) && x.getMesgUmidl().equals(n.getMesgUmidl()) && x.getMesgUmidh().equals(n.getMesgUmidh())).collect(Collectors.toList())));

			List<AppendixDetails> appendixList = searchDAO.getAppendixList(compsiteKeyString);
			// Map appendix details to it's message details
			messageDetails.stream()
					.forEach(n -> n.setAppendixDetailsList(appendixList.stream().filter(x -> x.getAid().equals(n.getAid()) && x.getMesgUmidl().equals(n.getMesgUmidl()) && x.getMesgUmidh().equals(n.getMesgUmidh())).collect(Collectors.toList())));

			getHistory(messageDetails);
		}

		List<MessageDetails> mxMessages = messageDetails.stream().filter(x -> x.getMesgFrmtName() != null && (x.getMesgFrmtName().equals("File") || x.getMesgFrmtName().equals("MX") || x.getMesgFrmtName().equals("AnyXML")))
				.collect(Collectors.toList());
		if (mxMessages != null && !mxMessages.isEmpty()) {
			List<XMLMessage> xmlMessage = xmlReaderDAO.getXMLMessage(compsiteKeyString, true);
			mxMessages.stream().forEach(n -> n.setMesgCanExpand(true));
			messageDetails.stream().forEach(n -> n.setXmlMessages(xmlMessage.stream().filter(x -> x.getAid().equals(n.getAid()) && x.getMesgUmidl().equals(n.getMesgUmidl()) && x.getMesgUmidh().equals(n.getMesgUmidh())).collect(Collectors.toList())));
		}

		List<MessageDetails> nonMxMessages = messageDetails.stream().filter(x -> x.getMesgFrmtName() != null && (!x.getMesgFrmtName().equals("File") || !x.getMesgFrmtName().equals("MX") || !x.getMesgFrmtName().equals("AnyXML")))
				.collect(Collectors.toList());
		if (nonMxMessages != null && !nonMxMessages.isEmpty()) {
			nonMxMessages = getUnexpandedText(nonMxMessages);
		}

		List<MessageDetails> compineLists = Stream.concat(mxMessages.stream(), nonMxMessages.stream()).collect(Collectors.toList());

		if (isShowBeingUpdated()) {
			List<String> beingUpdated = searchDAO.isBeingUpdated(compsiteKeyString);
			compineLists.stream().forEach(x -> x.setIsBeingUpdated(beingUpdated.stream().anyMatch(z -> z.contains(String.valueOf(x.getAid())))));
		} else {
			compineLists.stream().forEach(n -> n.setIsBeingUpdated(false));
		}

		for (MessageDetails messageDetail : compineLists) {

			messageDetail.setMesgStatus(getMesgStatus(messageDetail));

			messageDetail.setMesgSAAName("");// i will fill it from the
			// presentation layer for optimization

			CorrInfo corrInf = new CorrInfo();
			corrInf.setCorrType(messageDetail.getMesgSenderCorrType());
			corrInf.setCorrX1(messageDetail.getMesgSenderX1());
			corrInf.setCorrX2(messageDetail.getMesgSenderX2());
			corrInf.setCorrX3(messageDetail.getMesgSenderX3());
			corrInf.setCorrX4(messageDetail.getMesgSenderX4());
			corrInf.setMesgDate(messageDetail.getMesgCreaDateTime());
			messageDetail.setMesgSenderAddress(getCorrInfoString(corrInf));

			corrInf = new CorrInfo();
			corrInf.setCorrType(messageDetail.getInstReceiverCorrType());
			corrInf.setCorrX1(messageDetail.getInstReceiverX1());
			corrInf.setCorrX2(messageDetail.getInstReceiverX2());
			corrInf.setCorrX3(messageDetail.getInstReceiverX3());
			corrInf.setCorrX4(messageDetail.getInstReceiverX4());
			corrInf.setMesgDate(messageDetail.getMesgCreaDateTime());
			messageDetail.setMesgReceiverAddress(getCorrInfoString(corrInf));

		}
		return messageDetails;
	}

	private void getHistory(List<MessageDetails> messageDetails) {

		for (MessageDetails messageDetail : messageDetails) {
			String buffer = "";
			for (int i = 0; i < messageDetail.getMesgInstances().size(); ++i) {
				String sInstType = formatCase(StringUtils.substring(StringUtils.defaultString(messageDetail.getMesgInstances().get(i).getInstType()).trim(), 10));
				Integer iSeqNo = messageDetail.getMesgInstances().get(i).getInstNum();
				buffer = buffer + "*" + sInstType;
				if (iSeqNo > 0) {
					buffer = buffer + " - " + iSeqNo;
				}
				if (StringUtils.equalsIgnoreCase(messageDetail.getMesgInstances().get(i).getInstStatus(), "LIVE")) {
					buffer = buffer + " (Live in '" + StringUtils.defaultString(messageDetail.getMesgInstances().get(i).getInstRpName()) + "')\n";
				} else {
					buffer = buffer + " (Completed)\n";
				}
				if (iSeqNo == 0) {
					buffer = buffer + "\n";
				}
				SimpleDateFormat formatter1 = new SimpleDateFormat(dateFormat);
				SimpleDateFormat formatter2 = new SimpleDateFormat(timeFormat);

				if (StringUtils.equalsIgnoreCase(messageDetail.getMesgInstances().get(i).getInstCreaMpfnName(), "mpc") || StringUtils.equalsIgnoreCase(messageDetail.getMesgInstances().get(i).getInstCreaMpfnName(), "_SI_to_SWIFT")) {
					Date date = messageDetail.getMesgInstances().get(i).getInstCreaDateTime();
					buffer = buffer + "Created at '" + StringUtils.defaultString(messageDetail.getMesgInstances().get(i).getInstCreaRpName()) + "' on " + formatter1.format(date) + " at " + formatter2.format(date) + "\n";
				}
				if (StringUtils.equalsIgnoreCase(messageDetail.getMesgInstances().get(i).getInstCreaMpfnName(), "RMA") || StringUtils.equalsIgnoreCase(messageDetail.getMesgInstances().get(i).getInstCreaMpfnName(), "_SI_TO_SWIFTNET")
						|| StringUtils.equalsIgnoreCase(messageDetail.getMesgInstances().get(i).getInstCreaMpfnName(), "_SI_FROM_SWIFTNET")) {
					Date date = messageDetail.getMesgInstances().get(i).getInstCreaDateTime();
					buffer = buffer + "Created at '" + StringUtils.defaultString(messageDetail.getMesgInstances().get(i).getInstRpName()) + "' on " + formatter1.format(date) + " at " + formatter2.format(date) + "\n";
				}

				List<InterventionDetails> interventionList = new ArrayList<InterventionDetails>();
				for (InterventionDetails intv : messageDetail.getInterventionDetailsList()) {
					if (intv.getIntvInstNum() == messageDetail.getMesgInstances().get(i).getInstNum()) {
						interventionList.add(intv);
					}
				}

				long iIntvNo = 0;
				long iIntvMax = -1;
				if (interventionList.size() != 0) {
					iIntvMax = interventionList.size() - 1;
				}

				List<AppendixDetails> appendixList = new ArrayList<AppendixDetails>();
				for (AppendixDetails app : messageDetail.getAppendixDetailsList()) {
					if (app.getAppeInstNum() == messageDetail.getMesgInstances().get(i).getInstNum()) {
						appendixList.add(app);
					}
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
						buffer = buffer + SearchUtils.formatIntvStatus(interventionList.get((int) iIntvNo), iIntvNo);
						iIntvNo = iIntvNo + 1;
					} else if (dAppeDate.compareTo(dIntvDate) == 0) {
						if (nAppeSeq > nIntvSeq) {
							buffer = buffer + SearchUtils.formatIntvStatus(interventionList.get((int) iIntvNo), iIntvNo);
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
			messageDetail.setMesgHistory(buffer);

		}

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
					+ SearchUtils.formatAckSts(appendixDetails.getAppeNetworkDeliveryStatus(), appendixDetails.getAppeIAppName()) + "\n";
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
					+ SearchUtils.formatAckSts(appendixDetails.getAppeNetworkDeliveryStatus(), appendixDetails.getAppeIAppName()) + "\n";
		}

		if (!StringUtils.isEmpty(appendixDetails.getAppePkiAuthResult())) {
			String sAuthResultFormatted = "";
			sAuthResultFormatted = SearchUtils.formatAuthResult(StringUtils.defaultString(appendixDetails.getAppePkiAuthResult()));
			if (!StringUtils.isEmpty(sAuthResultFormatted)) {
				sBuf2 = sBuf2 + " MAC-Equivalent PKI Signed with result " + sAuthResultFormatted + "\n";
			}
		}
		if (!StringUtils.isEmpty(appendixDetails.getAppePkiPac2Result())) {
			String sAuthResultFormatted = SearchUtils.formatAuthResult(StringUtils.defaultString(appendixDetails.getAppePkiPac2Result()));
			if (!StringUtils.isEmpty(sAuthResultFormatted)) {
				sBuf2 = sBuf2 + " PAC-Equivalent PKI Signed with result " + sAuthResultFormatted + "\n";
			}
		}
		if (!StringUtils.isEmpty(appendixDetails.getAppeRmaCheckResult())) {
			String sAuthResultFormatted = SearchUtils.formatRmaCheckResult(StringUtils.defaultString(appendixDetails.getAppeRmaCheckResult()));
			if (!StringUtils.isEmpty(sAuthResultFormatted) && !StringUtils.equals(sAuthResultFormatted, "-")) {
				sBuf2 = sBuf2 + " RMA Check with result " + sAuthResultFormatted + "\n";
			}
		}
		return sBuf2;

	}

	public CorrInfo getCorrInfo(CorrInfo corr) {

		try {
			if (!cacheCorr) {
				return searchDAO.getCorrInfo(corr);
			}

			if (corr.getCorrX1() == null || corr.getCorrX1().isEmpty()) {
				return searchDAO.getCorrInfo(corr);
			}

			CorrInfo corrInfo = corrMap.get(corr.getCorrX1());
			if (corrInfo == null) {
				corrInfo = searchDAO.getCorrInfo(corr);
				if (corrInfo != null) {
					corrMap.put(corrInfo.getCorrX1(), corrInfo);
				}
				return corrInfo;
			} else {
				return corrInfo;
			}
		} catch (Exception e) {
			return searchDAO.getCorrInfo(corr);
		}

	}

	public String getCorrInfoString(CorrInfo corr) {
		CorrInfo corrInfo = getCorrInfo(corr);
		if (corrInfo != null) {
			String tmp = StringUtils.defaultString(corrInfo.getCorrInstitutionName()) + "\n";
			if (!StringUtils.isEmpty(corrInfo.getCorrBranchInfo())) {
				tmp = tmp + StringUtils.defaultString(corrInfo.getCorrBranchInfo()) + "\n";
			}
			if (!StringUtils.isEmpty(corrInfo.getCorrLocation())) {
				tmp = tmp + StringUtils.defaultString(corrInfo.getCorrLocation()) + "\n";
			}
			if (!StringUtils.isEmpty(corrInfo.getCorrCityName())) {
				tmp = tmp + StringUtils.defaultString(corrInfo.getCorrCityName()) + "\n";
			}
			tmp = tmp + StringUtils.defaultString(corrInfo.getCorrCtryCode()) + "\n";
			tmp = tmp + StringUtils.defaultString(corrInfo.getCorrCtryName());
			return tmp;
		}
		return "";
	}

	private String getBlock5(MessageDetails messsageDetails) {
		if (messsageDetails.getTextSwiftBlock5() == null || messsageDetails.getTextSwiftBlock5().trim().isEmpty()) {
			return "";
		}
		return "\r\nBlock 5:\r\n" + messsageDetails.getTextSwiftBlock5().trim();
	}

	private List<MessageDetails> getUnexpandedText(List<MessageDetails> messageDetails) {

		List<MessageDetails> emptyTextBlock = messageDetails.stream().filter(x -> x.getTextDataBlock() == null || SearchUtils.convertClob2String(x.getTextDataBlock()) == null || SearchUtils.convertClob2String(x.getTextDataBlock()).isEmpty())
				.collect(Collectors.toList());
		List<MessageDetails> notEmptyTextBlock = messageDetails.stream().filter(x -> x.getTextDataBlock() != null && !SearchUtils.convertClob2String(x.getTextDataBlock()).isEmpty()).collect(Collectors.toList());

		Pair<String, Boolean> result = new Pair<String, Boolean>();

		if (emptyTextBlock != null && !emptyTextBlock.isEmpty()) {

			List<MessageDetails> systemMessages = emptyTextBlock.stream().filter(x -> SearchUtils.IsSystemMessage(x.getMesgType())).collect(Collectors.toList());
			List<MessageDetails> nonSystemMessages = emptyTextBlock.stream().filter(x -> !SearchUtils.IsSystemMessage(x.getMesgType())).collect(Collectors.toList());

			if (nonSystemMessages != null && !nonSystemMessages.isEmpty()) {
				String nonSystemMessagesCompositekey = exportingUtils.buildCompsiteKeyForMessageDetails(nonSystemMessages);
				List<TextFieldData> textFieldData = searchDAO.getTextFieldData(nonSystemMessagesCompositekey);

				nonSystemMessages.stream()
						.forEach(n -> n.setTextFieldDataList(textFieldData.stream().filter(x -> x.getAid().equals(n.getAid()) && x.getMesgUmidl().equals(n.getMesgUmidl()) && x.getMesgUmidh().equals(n.getMesgUmidh())).collect(Collectors.toList())));

				for (MessageDetails textFieldDataList : messageDetails) {
					if (textFieldDataList.getTextFieldDataList() != null && !textFieldDataList.getTextFieldDataList().isEmpty()) {
						textFieldDataList.setPairs(buildUnexpandedText(textFieldDataList.getTextFieldDataList()));
					}
				}
			}
			if (systemMessages != null && !systemMessages.isEmpty()) {
				String systemMessagesCompositekey = exportingUtils.buildCompsiteKeyForMessageDetails(systemMessages);
				List<TextFieldData> textFieldData = searchDAO.getSystemTextFieldData(systemMessagesCompositekey);

				systemMessages.stream()
						.forEach(n -> n.setTextFieldDataList(textFieldData.stream().filter(x -> x.getAid().equals(n.getAid()) && x.getMesgUmidl().equals(n.getMesgUmidl()) && x.getMesgUmidh().equals(n.getMesgUmidh())).collect(Collectors.toList())));

				for (MessageDetails textFieldData2 : systemMessages) {

					for (int i = 0; i < textFieldData2.getTextFieldDataList().size(); ++i) {
						if (textFieldData.get(i).getFieldCode() == null) {
							NullPointerException up = new NullPointerException();
							throw up;
						}
						String str = textFieldData.get(i).getValue();
						str = StringUtils.replace(str, "\\r\\n", "\r\n");
						result.setKey(result.getKey() + "{" + textFieldData.get(i).getFieldCode().toString() + ":" + StringUtils.defaultString(str) + "}");
						result.setValue(false);
						textFieldData2.setPairs(result);
					}
				}
			}
		}
		if (notEmptyTextBlock != null && !notEmptyTextBlock.isEmpty()) {
			for (MessageDetails messageDetails2 : notEmptyTextBlock) {
				if ((!SearchUtils.IsSystemMessage(StringUtils.defaultString(messageDetails2.getMesgType(), "XXX")) && StringUtils.equalsIgnoreCase(StringUtils.substring(result.getKey(), 0, 2), "\n:"))
						|| (StringUtils.equalsIgnoreCase(messageDetails2.getMesgFrmtName(), "MX"))) {
					result.setValue(true);
				} else {
					result.setValue(false);
				}
				result.setKey(SearchUtils.convertClob2String(messageDetails2.getTextDataBlock()));
				messageDetails2.setPairs(result);
			}
		}

		List<MessageDetails> compineLists = Stream.concat(notEmptyTextBlock.stream(), emptyTextBlock.stream()).collect(Collectors.toList());

		// if it is an xml message we will just format the xml within it, cause it can't be expanded
		for (MessageDetails compineList : compineLists) {
			if (compineList.getPairs() != null) {
				String str = compineList.getPairs().getKey();
				if (str.startsWith("<") && str.endsWith(">")) {

					String strFormatted = SearchUtils.formatXML("<?xml version=\"1.0\"?><s>" + str + "</s>");// xml
					// messages that come from swift are not complete, as they has more that one root node, this was added to fix this

					if (strFormatted == null) {
						strFormatted = str;
						strFormatted = strFormatted.replace("\\n", "");
						strFormatted = strFormatted.replace("\\\"", "\"");
						strFormatted = SearchUtils.formatXML("<?xml version=\"1.0\"?><s>" + strFormatted + "</s>");// xml
						// messages that come from swift are not complete, as they has more that one root node, this was added to fix this

					}

					if (strFormatted != null) {
						strFormatted = strFormatted.substring(strFormatted.indexOf("<s>") + 4, strFormatted.length() - 5);
						strFormatted = strFormatted.replace("\n ", "\n");
						strFormatted = strFormatted.substring(2);
						str = strFormatted;
					}
				} else {
					compineList.setMesgBlock5(getBlock5(compineList));
				}
				compineList.setMesgUnExpandedText(str);
				compineList.setMesgCanExpand(compineList.getPairs().getValue());
			}
		}

		return compineLists;

	}

	private Pair<String, Boolean> buildUnexpandedText(List<TextFieldData> textFieldData) {
		Pair<String, Boolean> result = new Pair<String, Boolean>();
		result.setValue(true);
		result.setKey("");
		for (int i = 0; i < textFieldData.size(); ++i) {
			if (textFieldData.get(i).getFieldCode() == null) {
				NullPointerException up = new NullPointerException();
				throw up;
			}
			int iCode = textFieldData.get(i).getFieldCode();
			if (iCode == 0) {
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = SearchUtils.convertClob2String(textFieldData.get(i).getValueMemo());
				}
				result.setKey(result.getKey() + "\n" + StringUtils.defaultString(valueMemo));
			} else if (iCode > 0 && iCode < 100) {
				String valueMemo = "";
				if (textFieldData.get(i).getValueMemo() != null) {
					valueMemo = SearchUtils.convertClob2String(textFieldData.get(i).getValueMemo());
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
					valueMemo = SearchUtils.convertClob2String(textFieldData.get(i).getValueMemo());
				}
				result.setKey(result.getKey() + valueMemo);
			}
		}
		return result;
	}

	private String getMesgStatus(MessageDetails messsageDetails) {
		String status = "";

		if (messsageDetails.getMesgUserIssuedAsPde() != null && messsageDetails.getMesgUserIssuedAsPde().equals("1")) {
			status = status + "Possible Duplicate Emission, ";
		}
		if (StringUtils.containsIgnoreCase(messsageDetails.getMesgPossibleDupCreation(), "PDE") && status.isEmpty()) {
			status = status + "Possible Duplicate Emission, ";
		}
		if (StringUtils.containsIgnoreCase(messsageDetails.getMesgPossibleDupCreation(), "PDR")) {
			status = status + "Possible Duplicate Reception, ";
		}
		if (messsageDetails.getMesgIsPartial() == true) {
			status = status + "Partial Message, ";
		}
		if (StringUtils.equalsIgnoreCase(messsageDetails.getMesgClass(), "MESG_NORMAL")) {
			status = status + "Normal, ";
		}
		if (StringUtils.equalsIgnoreCase(messsageDetails.getMesgClass(), "MESG_SCISSOR")) {
			status = status + "Scissor, ";
		}
		if (StringUtils.equalsIgnoreCase(messsageDetails.getMesgClass(), "MESG_BROADCAST")) {
			status = status + "Broadcast " + messsageDetails.getMesgReceiverAliaName() + ",";
		} else if (messsageDetails.getMesgIsLive() != null && messsageDetails.getMesgIsLive() == false) {
			status = status + "Test Message, ";
		}
		if (messsageDetails.getMesgIsTextReadonly() == true) {
			status = status + "Read-only, ";
		}
		if (messsageDetails.getMesgIsTextModified() == true) {
			status = status + "Message Modified, ";
		}
		if (messsageDetails.getMesgIsDeleteInhibited() == false) {
			status = status + "Deletable, ";
		}
		if (messsageDetails.getMesgIsRetrieved() != null && messsageDetails.getMesgIsRetrieved() == true) {
			status = status + "Message Retrieved, ";
		}
		if (!status.isEmpty()) {
			status = status.substring(0, status.length() - 2);
		}

		return status;
	}

	private String formatCase(String str) {
		return StringUtils.capitalize(StringUtils.lowerCase(str));
	}

	public boolean isCacheCorr() {
		return cacheCorr;
	}

	public void setCacheCorr(boolean cacheCorr) {
		this.cacheCorr = cacheCorr;
	}

	public boolean isShowBeingUpdated() {
		return showBeingUpdated;
	}

	public void setShowBeingUpdated(boolean showBeingUpdated) {
		this.showBeingUpdated = showBeingUpdated;
	}

}
