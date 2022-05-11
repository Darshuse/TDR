package com.eastnets.extraction.service.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.config.Logging;
import com.eastnets.extraction.config.YAMLConfig;
import com.eastnets.extraction.dao.search.SearchDAO;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@Service
public class SearchParamInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchParamInitializer.class);

	@Autowired
	private YAMLConfig myConfig;

	public SearchParam initialize() {
		LOGGER.debug("Parameter initializer started.");
		LOGGER.debug("Parameter initializer started.");
		myConfig.setEnableDebug(CheckerUtils.defaultIsFalse(myConfig.isEnableDebug()));
		myConfig.setEnableDebugFull(CheckerUtils.defaultIsFalse(myConfig.isEnableDebugFull()));
		myConfig.setPrevious(CheckerUtils.defaultIsFalse(myConfig.isPrevious()));
		myConfig.setExtractFlagged(CheckerUtils.defaultIsFalse(myConfig.isExtractFlagged()));

		if (myConfig.isEnableDebug()) {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			ch.qos.logback.classic.Logger packageLogger = loggerContext.getLogger("com.eastnets.extraction");
			packageLogger.setLevel(Level.DEBUG);
		}
		if (myConfig.isEnableDebugFull()) {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			ch.qos.logback.classic.Logger packageLogger = loggerContext.getLogger("com.eastnets.extraction");
			packageLogger.setLevel(Level.TRACE);

		}
		SearchParam searchParam = new SearchParam();

		searchParam.setDirection(CheckerUtils.checkDirection(myConfig.getDirection()));
		searchParam.setFromDate(stringToDate(myConfig.getFromDate()));
		searchParam.setToDate(stringToDate(myConfig.getToDate()));
		searchParam.setMesgIsLive(CheckerUtils.defaultIsFalse(myConfig.isMesgIsLive()));
		searchParam.setMesgTypeList(separateByComma((myConfig.getMesgType() == null ? "" : myConfig.getMesgType())));
		searchParam.setIdentifier(myConfig.getIdentifier());
		searchParam.setReceiverBIC(myConfig.getReceiverBIC());
		searchParam.setSenderBIC(myConfig.getSenderBIC());
		searchParam.setBICFile((myConfig.getBICFile() == null ? "" : myConfig.getBICFile()));
		searchParam.setHistory(CheckerUtils.defaultIsFalse(myConfig.isHistory()));
		searchParam.setExpand(CheckerUtils.defaultIsFalse(myConfig.isExpand()));
		searchParam.setDate(CheckerUtils.checkDateColumn(myConfig.getDate()));
		searchParam.setDryRun(CheckerUtils.defaultIsFalse(myConfig.isDryRun()));
		searchParam.setEnableDebug(CheckerUtils.defaultIsFalse(myConfig.isEnableDebug()));
		searchParam.setEnableDebugFull(CheckerUtils.defaultIsFalse(myConfig.isEnableDebugFull()));
		searchParam.setAid(CheckerUtils.checkAid(myConfig.getAid()));
		searchParam.setMesgFormat(CheckerUtils.checkMesgFormat(myConfig.getMesgFormat()));
		searchParam.setFilePath(CheckerUtils.checkFilePath(myConfig.getFilePath()));
		searchParam.setMode(CheckerUtils.checkMode(myConfig.getMode()));
		searchParam.setFileSize(CheckerUtils.checkFileSize(myConfig.getFileSize()));
		searchParam.setSkipWeeks(CheckerUtils.checkSkipWeeks(myConfig.getSkipWeeks()));
		searchParam.setXmlCriteriaFile(myConfig.getRulesFile());
		searchParam.setXmlTemplateFile(myConfig.getTemplateFile());
		searchParam.setScheduler(myConfig.getScheduler());
		searchParam.setTransactionNumber(0);
		searchParam.setMessageNumber(CheckerUtils.checkMessageNumber(myConfig.getMessageNumber()));
		searchParam.setFlag(CheckerUtils.defaultIsFalse(myConfig.isFlag()));
		searchParam.setExtractFlagged(myConfig.isExtractFlagged());
		searchParam.setPrevious(myConfig.isPrevious());
		searchParam.setSource(myConfig.getSource());
		searchParam.setPartitioned(CheckerUtils.defaultIsFalse(myConfig.isPartitioned()));
		searchParam.setDbUsername(myConfig.getDbUsername());
		searchParam.setDbPassword(myConfig.getDbPassword());
		searchParam.setServerName(myConfig.getServerName());
		searchParam.setDatabaseName(myConfig.getDatabaseName());
		searchParam.setDbServiceName(myConfig.getDbServiceName());
		searchParam.setInstanceName(myConfig.getInstanceName());
		if (myConfig.getPortNumber().length() != 0) {
			searchParam.setPortNumber(Integer.parseInt(myConfig.getPortNumber()));
		}
		searchParam.setDbType(myConfig.getDbType());
		searchParam.setServerName(myConfig.getServerName());
		searchParam.setInstanceName(myConfig.getInstanceName());
		searchParam.setTnsPath(myConfig.getTnsPath());
		searchParam.setEcfPath(myConfig.getEcfPath());
		Logging logging = new Logging();
		logging.setFile(myConfig.getLogging().getFile());
		logging.setPattern(myConfig.getLogging().getPattern());
		logging.setLevel(myConfig.getLogging().getLevel());
		searchParam.setLogging(logging);

		DBPortabilityHandler.setDbType(CheckerUtils.checkDbType(myConfig.getDbType()));

		if (myConfig.getDayNumber() != null && myConfig.getDayNumber().length() > 0) {
			searchParam.setDayNumber(Integer.parseInt(myConfig.getDayNumber()));
			searchParam.setFromDate(getDateBeforeXDay(searchParam.getDayNumber()));
		} else if (myConfig.getDayNumber() != null && myConfig.getFromDate().length() == 0) {
			LOGGER.warn("No day number selected nor from date, Default is 1 day.");
			searchParam.setFromDate(getDateBeforeXDay(1));
		}

		if (searchParam.getSkipWeeks() != 0) {
			searchParam.setToDate(skipWeeks(searchParam.getSkipWeeks()));
		}

		if (searchParam.isPrevious()) {
			searchParam.setFromDate(getDateOfPreviousExtractedMessage());
		}

		searchParam.setFileName(myConfig.getFileName());
		setGeneratedFilesDate(searchParam);

		return searchParam;
	}

	private void setGeneratedFilesDate(SearchParam searchParam) {
		String fileName = FileNameUtils.getFormatedFileName(searchParam);
		searchParam.getGeneratedFilesData().setRjeFileName(fileName);
		searchParam.getGeneratedFilesData().setMxFileName(fileName);
		searchParam.getGeneratedFilesData().setExitPointFileName(fileName);
		searchParam.getGeneratedFilesData().setXmlFileName(fileName);
	}

	@Autowired
	private SearchDAO searchDAO;

	private Date getDateOfPreviousExtractedMessage() {
		return searchDAO.getDateOfPreviousExtractedMessage();
	}

	private Date getDateBeforeXDay(int XDay) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -XDay);
		Date dateBeforeXDays = cal.getTime();
		return dateBeforeXDays;
	}

	private Date stringToDate(String dateStr) {
		if (dateStr == null || dateStr == "") {
			return new Date();
		} else {
			dateStr = dateStr.replace("T", " ");
		}

		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("Date format is not valid, Date format should follow this yyyy/MM/dd HH:mm:ss");
			e.printStackTrace();
		}
		return date;
	}

	private Date skipWeeks(int numberOfWeeksToSkip) {
		return getDateBeforeXDay(numberOfWeeksToSkip * 7);
	}

	private List<String> separateByComma(String mesgType) {
		List<String> mesgTypeList = new ArrayList<String>();
		if (mesgType.trim().length() > 0) {
			mesgTypeList = Arrays.asList(mesgType.split(","));
		}
		return mesgTypeList;
	}
}
