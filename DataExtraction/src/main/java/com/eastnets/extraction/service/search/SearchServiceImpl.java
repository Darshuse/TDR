package com.eastnets.extraction.service.search;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.AllianceInstance;
import com.eastnets.extraction.bean.MessageDetails;
import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.XMLMessage;
import com.eastnets.extraction.config.YAMLConfig;
import com.eastnets.extraction.dao.search.SearchDAO;
import com.eastnets.extraction.service.export.ExitPointExportingImpl;
import com.eastnets.extraction.service.helper.SearchQueryBuilder;
import com.eastnets.extraction.service.helper.Utils;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDAO searchDAO;

	@Autowired
	private SearchQueryBuilder searchQueryBuilder;

	@Autowired
	private ExitPointExportingImpl exitPointExportingImpl;

	private String query;

	@Autowired
	private YAMLConfig config;

	private static final String WIN_EOL = "\r\n";
	private static final String UNIX_EOL = "\n";

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

	private List<AllianceInstance> CashAllianceInstance;

	@PostConstruct
	public void init() {
		setCashAllianceInstance(CashAllianceInstanceByAid());
	}

	@Override
	public List<SearchResult> search(SearchParam searchParam) throws InterruptedException, SQLException {

		query = searchQueryBuilder.generateQuery(searchParam);
		LOGGER.info("Query execution started.");
		// execute the Query and get the result in List.
		List<SearchResult> searchResult = searchDAO.execSearchQuery(query);

		return searchResult;
	}

	private String buildCompsiteKeyString(List<Map<String, Object>> message) {
		StringBuilder compsiteKey = new StringBuilder();

		if (message != null & !message.isEmpty()) {
			if (config.isPartitioned()) {
				message.forEach((event) -> {
					compsiteKey.append('(').append(event.get("aid")).append(',').append(event.get("umidl")).append(',').append(event.get("umidh")).append(',').append(event.get("creation_date_time_query")).append("),");
				});
			} else {
				message.forEach((event) -> {
					compsiteKey.append('(').append(event.get("aid")).append(',').append(event.get("umidl")).append(',').append(event.get("umidh")).append("),");
				});
			}
			return compsiteKey.substring(0, compsiteKey.length() - 1);
		}
		return "";
	}

	@Override
	public List<Map<String, Object>> searchMode4(SearchParam searchParam) throws InterruptedException, SQLException {

		// prepare Query based on the search parameters, using searchQueryBuilder.
		String xmlQuery = searchQueryBuilder.generateQueryForXml(searchParam);
		LOGGER.debug("Query execution started. ");

		List<Map<String, Object>> resultList = searchDAO.execSearchQueryAsResultSet(xmlQuery);

		String buildCompsiteKeyString = buildCompsiteKeyString(resultList);

		if (!buildCompsiteKeyString.isEmpty()) {

			try {

				List<MessageDetails> messageDetails = null;
				if (config.getDbType().equalsIgnoreCase("mssql")) {
					messageDetails = exitPointExportingImpl.getMessageDetails(buildCompsiteKeyString, searchParam);
				} else {
					messageDetails = exitPointExportingImpl.getMessageDetails(buildCompsiteKeyString, searchParam);
				}
				for (Map<String, Object> message : resultList) {
					for (MessageDetails messageDetail : messageDetails) {
						if (Integer.valueOf(message.get("aid").toString()).equals(messageDetail.getAid()) && Integer.valueOf(message.get("umidl").toString()).equals(messageDetail.getMesgUmidl())
								&& Integer.valueOf(message.get("umidh").toString()).equals(messageDetail.getMesgUmidh())) {

							if (messageDetail.getMesgFrmtName().equalsIgnoreCase("MX") || messageDetail.getMesgFrmtName().equalsIgnoreCase("anyxml")) {
								String xmlContent = getXMLContent(messageDetail);
								xmlContent = xmlContent.replace("\r\n", "\n").replace("\\r", "").replace("\r", "").trim();
								xmlContent = xmlContent.replace("\\r\\n", UNIX_EOL).replace("\\n", UNIX_EOL).replace(WIN_EOL, UNIX_EOL);
								message.put("text_data_block", xmlContent);
							}

							searchParam.setHistory(searchParam.isHistory());

							if (messageDetail.getMesgSenderAddress().trim().length() > 0) {
								message.put("sender_info", messageDetail.getMesgSenderAddress());
							}

							if (messageDetail.getMesgReceiverAddress().trim().length() > 0) {
								message.put("receiver_info", messageDetail.getMesgReceiverAddress());
							}

							if (messageDetail.getMesgHistory() != null && messageDetail.getMesgHistory().length() > 0) {
								String mesgHistory = messageDetail.getMesgHistory();
								mesgHistory = mesgHistory.replace("\r\n", "\n").replace("\\r", "").replace("\r", "").trim();
								mesgHistory = mesgHistory.replace("\\r\\n", UNIX_EOL).replace("\\n", UNIX_EOL).replace(WIN_EOL, UNIX_EOL);
								message.put("history_data_block", mesgHistory);
							}

							if (!messageDetail.getMesgFrmtName().equalsIgnoreCase("MX") && !messageDetail.getMesgFrmtName().equalsIgnoreCase("anyxml")) {

								if (messageDetail.getMesgUnExpandedText() != null && messageDetail.getMesgUnExpandedText().length() > 0) {

									LOGGER.debug(" message before replacing additional line and tabs " + messageDetail.getMesgUnExpandedText());
									message.put("text_data_block", messageDetail.getMesgUnExpandedText().replaceAll("\r\n", "\n").trim());
									LOGGER.debug(" message after replacing additional line and tabs " + messageDetail.getMesgUnExpandedText());
								}
							}
							String expandedText = getMesgExpandedText(messageDetail);
							expandedText = expandedText.trim();
							if (expandedText.length() > 0) {
								message.put("text_data_block_expanded", expandedText);
							}

							message.put("creation_date_time ", messageDetail.getMesgCreaDateTime());

							message.put("sumid", getSumid(messageDetail.getMesgUmidl(), messageDetail.getMesgUmidh()));

							message.put("status", messageDetail.getMesgStatus());

							try {
								if (message.get("aid") instanceof Short) {
									Short aid = ((Short) (message.get("aid")));
									AllianceInstance allianceInstance = getCashAllianceInstance().stream().filter(inst -> inst.getAid().equals(aid.longValue())).collect(Collectors.toList()).get(0);
									message.put("alliance_instance", allianceInstance.getAllianceInstance());
								} else {
									Long aid = ((BigDecimal) (message.get("aid"))).longValue();
									AllianceInstance allianceInstance = getCashAllianceInstance().stream().filter(inst -> inst.getAid().equals(aid)).collect(Collectors.toList()).get(0);
									message.put("alliance_instance", allianceInstance.getAllianceInstance());
								}
							} catch (Exception e) {
								LOGGER.error("cannot be cast short to Bigdecimal");
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		LOGGER.debug("Query execution finished.");
		return resultList;
	}

	public String getSumid(Integer umidl, Integer umidh) {
		return Integer.toHexString(umidl) + Integer.toHexString(umidh);
	}

	public String getXMLContent(MessageDetails messageDetails) {
		String textBlock = "";

		if (messageDetails.isXML()) {

			if (messageDetails.getXmlMessages() != null && messageDetails.getXmlMessages().size() > 0) {

				for (XMLMessage xmlMessage : messageDetails.getXmlMessages()) {

					if (xmlMessage != null && xmlMessage.getXmlContent() != null && !xmlMessage.getXmlContent().isEmpty()) {
						textBlock = Utils.xmlFormatter(xmlMessage.getXmlContent());
						textBlock = textBlock.replace("<MX>", "");
						textBlock = textBlock.replace("</MX>", "");

					} else {
						textBlock = "";
					}
				}

			} else {
				textBlock = "  ";
			}
		}
		textBlock = StringUtils.replace(textBlock, "\\r\\n", "\r\n"); // added to remove \r\n
		textBlock = StringUtils.replace(textBlock, "\r\n\r\n", "\r\n"); // added to remove extra empty lines

		return textBlock;
	}

	public String getMesgExpandedText(MessageDetails messageDetails) {
		String textBlock = "", tmpStr = "";
		if ((!StringUtils.equalsIgnoreCase(messageDetails.getMesgFrmtName(), "internal") && !StringUtils.equalsIgnoreCase(messageDetails.getMesgFrmtName(), "MX"))) {// internal messages
																																										// cannot be
																																										// expanded

			if (!StringUtils.isEmpty(messageDetails.getMesgUnExpandedText())) {
				try {
					String mesgVersion = StringUtils.defaultString(messageDetails.getMesgSyntaxTableVer(), "0000");
					Date mesgDate = messageDetails.getMesgCreaDateTime();
					//
					textBlock = searchDAO.getExpandedMesssageText(messageDetails.getAid(), messageDetails.getMesgUmidl(), messageDetails.getMesgUmidh(), mesgVersion, messageDetails.getMesgType(), messageDetails.getMesgUnExpandedText(), mesgDate,
							null, null);
				} catch (Exception e) {
					LOGGER.error("message aid " + messageDetails.getAid() + " umidl " + messageDetails.getMesgUmidl() + " umidh" + messageDetails.getMesgUmidh());
				}

				if (StringUtils.isEmpty(tmpStr)) {
					tmpStr = textBlock + "\r\n";
					tmpStr = StringUtils.replace(tmpStr, "\r\n\r\n", "\r\n"); // added to remove extra empty
																				// lines
				}
			}
		} else {
			tmpStr = textBlock + "\r\n";
			tmpStr = StringUtils.replace(tmpStr, "\r\n\r\n", "\r\n"); // added to remove extra empty lines
		}

		tmpStr = StringUtils.replace(tmpStr, "\r\n\r\n", "\r\n"); // added to remove extra empty lines
		return tmpStr;
	}

	@Override
	public void flagExtractedMessages(SearchParam searchParam, List<String> allAid, List<String> allUmidl, List<String> allUmidh) {
		if ((searchParam.getMode() & 3) != 0) {
			String updateQuerey = searchQueryBuilder.generateUpdateQuery(query);
			searchDAO.execUpdateQuery(updateQuerey);
		}

		if ((searchParam.getMode() & 4) != 0) {
			if (allAid != null && !allAid.isEmpty()) {
				String xmlUpdateQuerey = searchQueryBuilder.generateXMLUpdateQuery(allAid, allUmidl, allUmidh);
				searchDAO.execUpdateQuery(xmlUpdateQuerey);
			}

		}

	}

	@Override
	public List<AllianceInstance> CashAllianceInstanceByAid() {
		return searchDAO.CashAllianceInstanceByAid();
	}

	public List<AllianceInstance> getCashAllianceInstance() {
		return CashAllianceInstance;
	}

	public void setCashAllianceInstance(List<AllianceInstance> cashAllianceInstance) {
		CashAllianceInstance = cashAllianceInstance;
	}

}
