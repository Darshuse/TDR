
package com.eastnets.extraction.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.StatisticsFile;
import com.eastnets.extraction.service.export.ExitPointExportingImpl;
import com.eastnets.extraction.service.export.ExportXMLFromTemplate;
import com.eastnets.extraction.service.export.PayloadExportingImpl;
import com.eastnets.extraction.service.export.RJEExportingImpl;
import com.eastnets.extraction.service.export.XMLExportingImpl;
import com.eastnets.extraction.service.helper.SearchParamInitializer;
import com.eastnets.extraction.service.search.SearchService;
import com.eastnets.extraction.service.statistic.StatisticalService;

@Service
public class ExtractorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorController.class);

	@Autowired
	private SearchService searchService;

	@Autowired
	private RJEExportingImpl rjeExportingImpl;

	@Autowired
	private XMLExportingImpl xmlExportingImpl;

	@Autowired
	private ExitPointExportingImpl exitPointExportingImpl;

	@Autowired
	private StatisticsFile statisticsFile;

	@Autowired
	private StatisticalService statisticalService;

	@Autowired
	private PayloadExportingImpl payloadExportingImpl;

	@Autowired
	private ExportXMLFromTemplate exportXMLFromTemplet;

	@Autowired
	private SearchParamInitializer searchParamInitializer;
	private SearchParam searchParam;

	@PostConstruct
	public void onStartup() {
		startExtractData();
	}

	private void startExtractData() {
		try {

			LOGGER.info("Extractor tool started");

			statisticsFile.setStartingTime(new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date()));

			searchParam = searchParamInitializer.initialize();

			boolean allFetched = false;
			List<String> allAid = null;
			List<String> allUmidl = null;
			List<String> allUmidh = null;

			while (!allFetched) {
				LOGGER.trace("fetching messages under processing  ");
				List<SearchResult> searchResult = new ArrayList<>();
				List<Map<String, Object>> resultList = new ArrayList<>();

				if ((searchParam.getMode() & 3) != 0) {
					searchResult = searchService.search(searchParam);
				}

				if ((searchParam.getMode() & 4) != 0) {
					resultList = searchService.searchMode4(searchParam);
				}

				allFetched = searchResult.size() == 0 && resultList.size() == 0 ? true : false;

				if (allFetched) {
					LOGGER.info("No more messages to be extracted");
					continue;
				}

				if ((searchParam.getMode() & 3) != 0) {
					statisticsFile.setNumberOfGeneratedMessages(searchResult.size() + statisticsFile.getNumberOfGeneratedMessages());
					if (statisticsFile.getNumberOfGeneratedMessages() > 0) {
						LOGGER.info("Total number of messages fetched so far : " + statisticsFile.getNumberOfGeneratedMessages());
					}
				}

				if ((searchParam.getMode() & (1 << 0)) == 1) {
					rjeExportingImpl.exportMessages(searchResult, searchParam, statisticsFile);
					xmlExportingImpl.exportMessages(searchResult, searchParam, statisticsFile);
					payloadExportingImpl.exportMessages(searchResult, searchParam, statisticsFile);
				}

				if ((searchParam.getMode() & (1 << 1)) == 2) {
					exitPointExportingImpl.exportMessages(searchResult, searchParam, statisticsFile);
				}

				if ((searchParam.getMode() & (1 << 2)) == 4) {
					allAid = new ArrayList<String>();
					allUmidl = new ArrayList<String>();
					allUmidh = new ArrayList<String>();

					allAid.addAll(resultList.stream().map(x -> x.get("aid").toString()).collect(Collectors.toList()));
					allUmidl.addAll(resultList.stream().map(x -> x.get("umidl").toString()).collect(Collectors.toList()));
					allUmidh.addAll(resultList.stream().map(x -> x.get("umidh").toString()).collect(Collectors.toList()));

					exportXMLFromTemplet.exportXMLMessages(resultList, searchParam, statisticsFile);

				}

				searchParam.setTransactionNumber(searchParam.getTransactionNumber() + 1);

			}

			if (searchParam.isFlag() && !searchParam.isDryRun()) {
				searchService.flagExtractedMessages(searchParam, allAid, allUmidl, allUmidh);
			}
			LOGGER.info("Total number of messages generated : " + (statisticsFile.getNumberOfGeneratedMessages() - statisticsFile.getNumberOfMessagesThatHaveErrors()));
			LOGGER.info("Total number of messages that have errors : " + (statisticsFile.getNumberOfMessagesThatHaveErrors()));
			statisticsFile.setFinishingTime(new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date()));
			LOGGER.trace(" finished at " + statisticsFile.getFinishingTime());
			statisticalService.generateStatisticalFile(statisticsFile, searchParam);

			LOGGER.info("Extractor tool Finished");

		} catch (InterruptedException | SQLException e) {
			LOGGER.error("DataExtraction  exception  :: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
