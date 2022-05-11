package com.eastnets.extraction.service.statistic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.StatisticsFile;
import com.eastnets.extraction.service.helper.ExportingUtils;
import com.eastnets.extraction.service.helper.FileWriterUtils;

@Service
public class StatisticalServiceImpl implements StatisticalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticalServiceImpl.class);

	@Value("${logging.level.com.eastnets.extraction}")
	private String levelExtraction;

	@Autowired
	private ExportingUtils exportingUtils;

	@Override
	public void generateStatisticalFile(StatisticsFile statisticsFile, SearchParam searchParam) {

		LOGGER.info("Statistics file generator started.");

		String fileName = exportingUtils.buildFileName("Statistics", ".txt");

		List<String> statisticsData = new ArrayList<String>();
		addRunningTime(statisticsData, statisticsFile);
		addBanner(statisticsData);
		addGeneratedMessagesNumbers(statisticsData, statisticsFile);
		addBanner(statisticsData);
		addGeneratedFilesNames(statisticsData, statisticsFile);
		addBanner(statisticsData);
		addSelectedCofig(searchParam, statisticsData);

		FileWriterUtils.writeStatisticsFile(searchParam.getFilePath(), statisticsData);
	}

	private void addGeneratedMessagesNumbers(List<String> statisticsData, StatisticsFile statisticsFile) {
		statisticsData.add("----------------- Generated Messages -----------------");
		statisticsData.add("Number of generated messages: " + (statisticsFile.getNumberOfGeneratedMessages() - statisticsFile.getNumberOfMessagesThatHaveErrors()) + "");
		statisticsData.add("Number of generated messages in RJE format: " + statisticsFile.getNumberOfGeneratedRJE());
		statisticsData.add("Number of generated messages in Exit point format: " + statisticsFile.getNumberOfGeneratedExitPoint());
		statisticsData.add("Number of generated messages in MX format: " + statisticsFile.getNumberOfGeneratedMX());
		statisticsData.add("Number of generated messages in File format: " + statisticsFile.getNumberofGeneratedFile());
		statisticsData.add("Number of generated messages in XML format: " + statisticsFile.getNumberOfGeneratedXMLMessages());
		statisticsData.add("Number of empty MX messages: " + statisticsFile.getNumberOfEmptyMXMessages());
		statisticsData.add("Number of messages that have errors: " + statisticsFile.getNumberOfMessagesThatHaveErrors());

	}

	private void addBanner(List<String> statisticsData) {
		statisticsData.add("\n\n");
	}

	private void addRunningTime(List<String> statisticsData, StatisticsFile statisticsFile) {
		statisticsData.add("----------------- Starting/Finishing Time -----------------");
		statisticsData.add("Starting Time	: " + statisticsFile.getStartingTime());
		statisticsData.add("Finishing Time: " + statisticsFile.getFinishingTime());
	}

	private void addGeneratedFilesNames(List<String> statisticsData, StatisticsFile statisticsFile) {
		statisticsData.add("----------------- Generated File Names -----------------");
		for (String generatedFileName : statisticsFile.getGeneratedFileNames()) {
			statisticsData.add(generatedFileName);
		}
		statisticsData.add(" ");
	}

	private void addSelectedCofig(SearchParam searchParam, List<String> statisticsData) {

		statisticsData.add("----------------- Selected Configurations -----------------");
		if (searchParam.getAid().size() > 0) {
			statisticsData.add("Aid: 			               " + "                   " + checkNull(searchParam.getAid().get(0)));
		} else {
			statisticsData.add("Aid: 			               " + "                   " + "");
		}
		statisticsData.add("dbUsername: 	                  " + "                   " + checkNull(searchParam.getDbUsername()));
		statisticsData.add("dbPassword: 	                  " + "                   " + checkNull(searchParam.getDbPassword()));
		statisticsData.add("serverName: 	                  " + "                   " + checkNull(searchParam.getServerName()));
		statisticsData.add("databaseName:	                  " + "                   " + checkNull(searchParam.getDatabaseName()));
		statisticsData.add("portNumber: 	                  " + "                   " + checkNull(searchParam.getPortNumber()));
		statisticsData.add("dbType:			           " + "                   " + checkNull(searchParam.getDbType()));
		statisticsData.add("dbServiceName:                    " + "                   " + checkNull(searchParam.getDbServiceName()));
		statisticsData.add("instanceName:	                  " + "                   " + checkNull(searchParam.getInstanceName()));
		statisticsData.add("partitioned: 	                  " + "                   " + checkNull(searchParam.isPartitioned()));
		statisticsData.add("tnsPath: 		          " + "                   " + checkNull(searchParam.getTnsPath()));
		statisticsData.add("enableTnsName: 	                  " + "                   " + checkBoolean(searchParam.isEnableTnsName()));
		statisticsData.add("ecfPath: 		          " + "                   " + checkNull(searchParam.getEcfPath()));
		statisticsData.add("fileName: 		          " + "                   " + checkNull(searchParam.getFileName()));
		statisticsData.add("filePath: 		          " + "                   " + checkNull(searchParam.getFilePath()));
		statisticsData.add("fileSize: 		          " + "                   " + checkNull(searchParam.getFileSize()));
		statisticsData.add("mode: 			          " + "                   " + checkNull(searchParam.getMode()));
		statisticsData.add("expand: 		          " + "                   " + checkNull(searchParam.isExpand()));
		statisticsData.add("history: 		          " + "                   " + checkNull(searchParam.isHistory()));
		statisticsData.add("flag: 			          " + "                   " + checkNull(searchParam.isFlag()));
		statisticsData.add("extractFlagged:                   " + "                   " + checkNull(searchParam.isExtractFlagged()));
		statisticsData.add("previous: 		          " + "                   " + checkNull(searchParam.isPrevious()));
		statisticsData.add("mesgIsLive: 	                  " + "                   " + checkNull(searchParam.isMesgIsLive()));
		statisticsData.add("mesgFormat: 	                  " + "                   " + checkNull(searchParam.getMesgFormat()));
		statisticsData.add("BICFile: 		          " + "                   " + checkNull(searchParam.getBICFile()));
		statisticsData.add("dayNumber: 		           " + "                   " + checkNull(searchParam.getDayNumber()));
		statisticsData.add("skipWeeks:	 	           " + "                   " + checkNull(searchParam.getSkipWeeks()));
		statisticsData.add("fromDate: 		           " + "                   " + checkNull(searchParam.getFromDate()));
		statisticsData.add("toDate: 		           " + "                   " + checkNull(searchParam.getToDate()));
		statisticsData.add("date: 			           " + "                   " + checkNull(searchParam.getDate()));
		statisticsData.add("mesgType: 		           " + "                   " + checkNull(searchParam.getMesgTypeList()));
		statisticsData.add("identifier: 	                  " + "                   " + checkNull(searchParam.getIdentifier()));
		statisticsData.add("senderBIC: 		           " + "                   " + checkNull(searchParam.getSenderBIC()));
		statisticsData.add("receiverBIC: 	                  " + "                   " + checkNull(searchParam.getReceiverBIC()));
		statisticsData.add("direction: 		           " + "                   " + checkNull(searchParam.getDirection()));
		statisticsData.add("messageNumber: 	                  " + "                   " + checkNull(searchParam.getMessageNumber()));
		statisticsData.add("rulesFile: 		           " + "                   " + checkNull(searchParam.getXmlCriteriaFile()));
		statisticsData.add("templateFile: 	                  " + "                   " + checkNull(searchParam.getXmlTemplateFile()));
		statisticsData.add("source: 		           " + "                   " + checkNull(searchParam.getSource()));
		statisticsData.add("enableDebug: 	                  " + "                   " + checkNull(searchParam.isEnableDebug()));
		statisticsData.add("dryRun: 		           " + "                   " + checkNull(searchParam.isDryRun()));
		statisticsData.add("enableDebugFull:                  " + "                   " + checkNull(searchParam.isEnableDebugFull()));
		statisticsData.add("scheduler: 		           " + "                   " + checkNull(searchParam.getScheduler()));
		statisticsData.add("threadLevel: 	                                                                      ");
		statisticsData.add("logging: 			                                                                       ");
		statisticsData.add(" file: 		           " + "                                                             ");
		statisticsData.add("  name: 		           " + "                   " + checkNull(searchParam.getLogging().getFile().getName()));
		statisticsData.add(" Pattern: 		                                                                          ");
		statisticsData.add("  console: 		           " + "                   " + checkNull(searchParam.getLogging().getPattern().getConsole()));
		statisticsData.add("  file: 		           " + "                   " + checkNull(searchParam.getLogging().getPattern().getFile()));
		statisticsData.add(" Level: 		                                                                             ");
		statisticsData.add("  com.eastnets.extraction : 		           " + "               " + levelExtraction);

	}

	private Object checkNull(Object param) {
		if (param == null) {
			return "";
		}
		return param;
	}

	private Boolean checkBoolean(Boolean param) {
		if (param == null) {
			return false;
		}
		return param;
	}

	public String getLevelExtraction() {
		return levelExtraction;
	}

	public void setLevelExtraction(String levelExtraction) {
		this.levelExtraction = levelExtraction;
	}

}
